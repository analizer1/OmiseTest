package net.analizer.tamboon.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;

import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.models.CreditCartInfo;
import net.analizer.tamboon.R;
import net.analizer.tamboon.databinding.ActivityDonateBinding;
import net.analizer.tamboon.fragments.ProgressDialogFragment;
import net.analizer.tamboon.views.DonationPresenter;
import net.analizer.tamboon.views.DonationView;

import javax.inject.Inject;

public class DonateActivity extends BaseActivity implements DonationView {
    private static final String EXTRA_CHARITY = "charity";

    private final int GET_NEW_CARD = 2;

    @Inject
    DonationPresenter donationPresenter;

    private ActivityDonateBinding mViewBinding;
//    private Charity mCharity; this does not get used atm

    private TextWatcher mDonationAmountChangedWatcher;

    public static Intent getIntent(AppCompatActivity appCompatActivity, Charity charity) {
        Intent intent = new Intent(appCompatActivity, DonateActivity.class);
        intent.putExtra(EXTRA_CHARITY, charity);
        return intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_donate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeInjector();
        initializeUIs();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if (reqCode == GET_NEW_CARD && resultCode == RESULT_OK && data != null) {
            String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            CreditCartInfo creditCartInfo = new CreditCartInfo(
                    cardNumber,
                    cardHolderName,
                    expiry,
                    cvv
            );

            onDonationDetailsChanged(
                    creditCartInfo,
                    mViewBinding.donationAmountEditText.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        donationPresenter.setView(null);
        mViewBinding.donationAmountEditText.removeTextChangedListener(mDonationAmountChangedWatcher);
        super.onDestroy();
    }

    @Override
    public void enableDonateBtn() {
        mViewBinding.donateButton.setEnabled(true);
    }

    @Override
    public void disableDonateBtn() {
        mViewBinding.donateButton.setEnabled(false);
    }

    @Override
    public void displayDonationComplete() {
        Intent intent = CongratulationsActivity.getIntent(this);
        startActivity(intent);
        finish();
    }

    @Override
    public void displayCardEditor() {

        Intent intent = new Intent(DonateActivity.this, CardEditActivity.class);
        startActivityForResult(intent, GET_NEW_CARD);
    }

    @Override
    public void displayCreditCard(CreditCartInfo creditCartInfo) {
        mViewBinding.donationCreditCardView.setCardNumber(creditCartInfo.getCreditCardNo());
        mViewBinding.donationCreditCardView.setCardHolderName(creditCartInfo.getCreditCardHolderName());
        mViewBinding.donationCreditCardView.setCardExpiry(creditCartInfo.getCreditCardExpiry());
        mViewBinding.donationCreditCardView.setCVV(creditCartInfo.getCreditCardCVV());
    }

    @Override
    public void focusOnDonationAmountInput() {
        if (mViewBinding.donationAmountEditText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else {
            mViewBinding.donationAmountEditText.performClick();
        }
    }

    @Override
    public void displayInvalidCardInfo() {
        displayError(getString(R.string.error_credit_card_info));
    }

    @Override
    public void displayInvalidDonationAmount() {
        displayError(getString(R.string.error_donation_amount));
    }

    @Override
    public void displayError(@NonNull String errMsg) {
        showAlert(getString(R.string.error_title), errMsg);
    }

    @Override
    public void showLoading(boolean isCancelable) {
        if (canUpdateView()) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(isCancelable);
            supportFragmentManager
                    .beginTransaction()
                    .add(progressDialogFragment, ProgressDialogFragment.class.getName())
                    .commitNowAllowingStateLoss();
        }
    }

    @Override
    public void dismissLoading() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        ProgressDialogFragment loadingDialogFragment = (ProgressDialogFragment)
                supportFragmentManager.findFragmentByTag(ProgressDialogFragment.class.getName());
        if (loadingDialogFragment != null) {
            loadingDialogFragment.dismissAllowingStateLoss();
            supportFragmentManager.executePendingTransactions();
        }
    }

    private void initializeInjector() {
        getApplicationComponent().inject(this);

        // initialize Activity Component here (IF ANY)
    }

    private void initializeUIs() {
        donationPresenter.setView(this);

        // do other UIs initialization...
        mViewBinding = getViewBinding(ActivityDonateBinding.class);
//        Intent intent = getIntent();
//        mCharity = intent.getParcelableExtra(EXTRA_CHARITY);

        mDonationAmountChangedWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                CreditCartInfo creditCartInfo = getCurrentCreditCardInfo();
                onDonationDetailsChanged(creditCartInfo, editable.toString());
            }
        };

        mViewBinding.donationCreditCardView.setOnClickListener(view -> donationPresenter.onCreditCardClicked());
        mViewBinding.donationAmountEditText.addTextChangedListener(mDonationAmountChangedWatcher);
        mViewBinding.donateButton.setOnClickListener(view -> {
            CreditCartInfo creditCartInfo = getCurrentCreditCardInfo();
            Long donationAmount =
                    Long.parseLong(mViewBinding.donationAmountEditText.getText().toString());
            donationPresenter.onSubmitDonation(creditCartInfo, donationAmount);
        });
    }

    private CreditCartInfo getCurrentCreditCardInfo() {
        String cardHolderName = mViewBinding.donationCreditCardView.getCardHolderName();
        String cardNumber = mViewBinding.donationCreditCardView.getCardNumber();
        String expiry = mViewBinding.donationCreditCardView.getExpiry();
        String cvv = mViewBinding.donationCreditCardView.getCVV();

        return new CreditCartInfo(
                cardNumber,
                cardHolderName,
                expiry,
                cvv
        );
    }

    private void onDonationDetailsChanged(CreditCartInfo creditCartInfo, String donation) {
        Long donationAmount = null;
        if (donation != null && donation.length() > 0) {
            donationAmount = Long.parseLong(donation);
        }

        donationPresenter.onDonationDetailsEntered(
                creditCartInfo,
                donationAmount);
    }
}
