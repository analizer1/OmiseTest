package net.analizer.datalayer.api;

import net.analizer.datalayer.keystores.AccessKeyFactory;
import net.analizer.datalayer.keystores.AccessKeyStore;
import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.models.CreditCartInfo;
import net.analizer.domainlayer.models.Donation;
import net.analizer.domainlayer.models.DonationResponse;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.omise.android.Client;
import co.omise.android.TokenRequest;
import co.omise.android.TokenRequestListener;
import co.omise.android.models.Token;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

@Singleton
public class ApiImp implements ApiInterface {

    private ReplaySubject<String> mTokenSubject;

    private RetrofitInterface mRetrofitInterface;
    private AccessKeyFactory mAccessKeyFactory;

    @Inject
    public ApiImp(RetrofitInterface retrofitInterface, AccessKeyFactory accessKeyFactory) {
        this.mRetrofitInterface = retrofitInterface;
        this.mAccessKeyFactory = accessKeyFactory;
    }

    @Override
    public Observable<List<Charity>> getCharityList() {
        return mRetrofitInterface.getCharityList();
    }

    @Override
    public Single<DonationResponse> donate(Donation donation) {
        return mRetrofitInterface.donate(donation);
    }

    @Override
    public Single<String> getToken(CreditCartInfo creditCartInfo) {

        mTokenSubject = ReplaySubject.create();

        AccessKeyStore accessKeyStore = mAccessKeyFactory.createAccessKeyStore();
        Client client;
        try {
            client = new Client(accessKeyStore.getAccessKey());
        } catch (GeneralSecurityException ex) {
            return Single.error(ex);
        }

        TokenRequest request = new TokenRequest();
        request.number = creditCartInfo.getCreditCardNo();
        request.name = creditCartInfo.getCreditCardHolderName();
        request.expirationMonth = creditCartInfo.getExpiryMonth();
        request.expirationYear = creditCartInfo.getExpiryYear();
        request.securityCode = creditCartInfo.getCreditCardCVV();

        client.send(request, new TokenRequestListener() {
            @Override
            public void onTokenRequestSucceed(TokenRequest request, Token token) {
                mTokenSubject.onNext(token.id);
                mTokenSubject.onComplete();
            }

            @Override
            public void onTokenRequestFailed(TokenRequest request, Throwable throwable) {
                mTokenSubject.onError(throwable);
            }
        });

        return mTokenSubject.singleOrError();
    }
}
