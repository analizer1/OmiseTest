package net.analizer.tamboon.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.CreditCartInfo;
import net.analizer.domainlayer.models.Donation;
import net.analizer.domainlayer.models.DonationResponse;
import net.analizer.domainlayer.rx.NetworkObserver;
import net.analizer.tamboon.presenters.BasicPresenter;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
            donationView.disableDonateBtn();
        }
    }

    public void onSubmitDonation(@NonNull CreditCartInfo creditCartInfo, long donationAmount) {

        DonationView donationView = mDonateViewRef.get();
        if (donationView == null) {
            return;
        }

        donationView.showLoading(false);

        Observable<String> tokenObservable = mApiInterface.getToken(creditCartInfo);
        tokenObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .flatMap(accessToken -> {
                    if (TextUtils.isEmpty(accessToken)) {
                        return Observable.error(new Throwable("Invalid Access Token"));
                    }

                    Donation donation = new Donation(
                            creditCartInfo.getCreditCardHolderName(),
                            accessToken,
                            donationAmount);

                    return mApiInterface.donate(donation)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread());
                })
                .subscribe(new NetworkObserver<DonationResponse>() {
                    @Override
                    public void onResponse(DonationResponse response) {
                        donationView.dismissLoading();
                        donationView.displayDonationComplete();
                    }

                    @Override
                    public void onNetworkError(Throwable t) {
                        donationView.dismissLoading();

                        // In this case,
                        // Client cannot be sure of how well the server is implemented.
                        // These checks reassure that we do not display NULL message.
                        String msg = null;

                        if (HttpException.class.isAssignableFrom(t.getClass())) {
                            HttpException exception = (HttpException) t;
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
                            msg = t.getMessage();
                        }

                        if (TextUtils.isEmpty(msg)) {
                            msg = t.toString();
                        }

                        if (TextUtils.isEmpty(msg)) {
                            msg = "Unknown Error";
                        }

                        donationView.displayError(msg);
                    }
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
