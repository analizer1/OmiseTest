package net.analizer.tamboon.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.CreditCartInfo;
import net.analizer.domainlayer.models.Donation;
import net.analizer.tamboon.presenters.BasicPresenter;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@SuppressWarnings("WeakerAccess")
public class DonationPresenter implements BasicPresenter<DonationView> {

    // Prevent a cyclic reference
    private WeakReference<DonationView> mDonateViewRef;

    private ApiInterface mApiInterface;

    @Inject
    public DonationPresenter(ApiInterface apiInterface) {
        this.mApiInterface = apiInterface;
    }

    @Override
    public void setView(@Nullable DonationView view) {

        if (view != null) {
            mDonateViewRef = new WeakReference<>(view);

        } else if (mDonateViewRef != null) {
            mDonateViewRef.clear();
            mDonateViewRef = null;
        }
    }

    @Nullable
    @Override
    public DonationView getView() {
        DonationView charityListView = null;

        if (mDonateViewRef != null) {
            charityListView = mDonateViewRef.get();
        }

        return charityListView;
    }

    public void onDonationDetailsEntered(@NonNull CreditCartInfo creditCartInfo,
                                         @Nullable Long donationAmount) {

        DonationView donationView = mDonateViewRef.get();
        if (donationView == null) {
            return;
        }

        if (!creditCartInfo.isValid()) {
            donationView.disableDonateBtn();
            donationView.displayInvalidCardInfo();
            return;
        }

        donationView.displayCreditCard(creditCartInfo);

        if (donationAmount != null) {
            if (donationAmount < 0) {
                donationView.disableDonateBtn();
                donationView.displayInvalidDonationAmount();
                return;

            } else if (donationAmount == 0) {
                donationView.disableDonateBtn();
                return;
            }

            donationView.enableDonateBtn();

        } else {
            donationView.focusOnDonationAmountInput();
            donationView.disableDonateBtn();
        }
    }

    public void onSubmitDonation(@NonNull CreditCartInfo creditCartInfo, long donationAmount) {

        DonationView donationView = mDonateViewRef.get();
        if (donationView == null) {
            return;
        }

        donationView.showLoading(false);

        Single<String> tokenSingle = mApiInterface.getToken(creditCartInfo);
        tokenSingle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .flatMap((Function<String, SingleSource<?>>) accessToken -> {

                    Donation donation = new Donation(
                            creditCartInfo.getCreditCardHolderName(),
                            accessToken,
                            donationAmount);

                    return mApiInterface.donate(donation)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread());
                })
                .subscribe(response -> {
                    donationView.dismissLoading();
                    donationView.displayDonationComplete();

                }, throwable -> {
                    donationView.dismissLoading();

                    // In this case,
                    // Client cannot be sure of how well the server is implemented.
                    // These checks reassure that we do not display NULL message.
                    String msg = null;

                    if (HttpException.class.isAssignableFrom(throwable.getClass())) {
                        HttpException exception = (HttpException) throwable;
                        if (exception.response() != null) {
                            ResponseBody responseBody = exception.response().errorBody();
                            if (responseBody != null) {
                                try {
                                    msg = responseBody.string();
                                } catch (IOException e) {
                                }
                            }
                        }

                    } else {
                        msg = throwable.getMessage();
                    }

                    if (TextUtils.isEmpty(msg)) {
                        msg = throwable.toString();
                    }

                    if (TextUtils.isEmpty(msg)) {
                        msg = "Unknown Error";
                    }

                    donationView.displayError(msg);
                });
    }

    public void onCreditCardClicked() {
        DonationView donationView = mDonateViewRef.get();
        if (donationView == null) {
            return;
        }

        donationView.displayCardEditor();
    }
}
