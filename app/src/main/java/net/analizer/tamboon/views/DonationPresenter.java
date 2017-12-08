package net.analizer.tamboon.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.CreditCartInfo;
import net.analizer.domainlayer.models.Donation;
import net.analizer.domainlayer.rx.NetworkObserver;
import net.analizer.tamboon.presenters.BasicPresenter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    public void onDonationDetailsEntered(@NonNull CreditCartInfo creditCartInfo, long donationAmount) {

        DonationView donationView = mDonateViewRef.get();
        if (donationView == null) {
            return;
        }

        if (!isValidCreditCardInfo(creditCartInfo)) {
            donationView.disableDonateBtn();
            donationView.displayInvalidCardInfo();
            return;
        }

        if (donationAmount <= 0) {
            donationView.disableDonateBtn();
            donationView.displayInvalidDonationAmount();
            return;
        }

        donationView.enableDonateBtn();
    }

    public void onSubmitDonation(@NonNull CreditCartInfo creditCartInfo, long donationAmount) {

        DonationView donationView = mDonateViewRef.get();
        if (donationView == null) {
            return;
        }

        donationView.showLoading(false);

        Observable<String> tokenObservable = mApiInterface.getToken();
        tokenObservable
                .flatMap(accessToken -> {
                    if (TextUtils.isEmpty(accessToken)) {
                        return Observable.error(new Throwable("Invalid Access Token"));
                    }

                    Donation donation = new Donation(
                            creditCartInfo.creditCardHolderName,
                            accessToken,
                            donationAmount);
                    return mApiInterface.donate(donation);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetworkObserver<String>() {
                    @Override
                    public void onResponse(String response) {
                        donationView.dismissLoading();
                        donationView.displayDonationComplete();
                    }

                    @Override
                    public void onNetworkError(Throwable t) {
                        donationView.dismissLoading();

                        // In this case,
                        // Client cannot be sure of how well the server is implemented.
                        // These checkings reassure that we do not display NULL message.
                        String msg = t.getMessage();
                        if (TextUtils.isEmpty(msg) && t.getCause() != null) {
                            msg = t.getCause().getMessage();
                        }

                        if (TextUtils.isEmpty(msg)) {
                            msg = "Unknown Error";
                        }

                        donationView.displayError(msg);
                    }
                });
    }

    private boolean isValidCreditCardInfo(CreditCartInfo creditCartInfo) {
        boolean isValid = creditCartInfo != null
                && (creditCartInfo.creditCardHolderName != null && creditCartInfo.creditCardHolderName.length() > 0)
                && (creditCartInfo.creditCardNo != null && creditCartInfo.creditCardNo.length() > 0)
                && (creditCartInfo.creditCardExpiry != null && creditCartInfo.creditCardExpiry.length() > 0)
                && (creditCartInfo.creditCardCCV != null && creditCartInfo.creditCardCCV.length() > 0);

        return isValid;
    }
}
