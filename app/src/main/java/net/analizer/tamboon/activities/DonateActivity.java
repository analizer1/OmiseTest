package net.analizer.tamboon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.utils.AccessKeyFactory;
import net.analizer.domainlayer.utils.AccessKeyStore;
import net.analizer.tamboon.R;
import net.analizer.tamboon.databinding.ActivityDonateBinding;
import net.analizer.tamboon.views.DonationPresenter;
import net.analizer.tamboon.views.DonationView;

import javax.inject.Inject;

import co.omise.android.ui.CreditCardActivity;

public class DonateActivity extends BaseActivity implements DonationView {
    private static final String EXTRA_CHARITY = "charity";

    @Inject
    DonationPresenter donationPresenter;

    @Inject
    AccessKeyFactory accessKeyFactory;

    private ActivityDonateBinding mViewBinding;
//    private Charity mCharity; this does not get used atm

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
        mViewBinding = getViewBinding(ActivityDonateBinding.class);

        initializeInjector();
        initializeUIs();
    }

    @Override
    protected void onDestroy() {
        donationPresenter.setView(null);
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

    }

    @Override
    public void dismissLoading() {

    }

    private void initializeInjector() {
        getApplicationComponent().inject(this);

        // initialize Activity Component here (IF ANY)
    }

    private void initializeUIs() {
        donationPresenter.setView(this);

        // do other UIs initialization...
//        Intent intent = getIntent();
//        mCharity = intent.getParcelableExtra(EXTRA_CHARITY);
    }

    private void showCreditCardForm() {
        AccessKeyStore accessKeyStore = accessKeyFactory.createAccessKeyStore();

        Intent intent = new Intent(this, CreditCardActivity.class);
        intent.putExtra(CreditCardActivity.EXTRA_PKEY, accessKeyStore.getAccessKey());
        startActivityForResult(intent, accessKeyStore.getRequestCC());
    }
}
