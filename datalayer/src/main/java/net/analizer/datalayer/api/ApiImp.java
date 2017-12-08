package net.analizer.datalayer.api;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.models.Donation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class ApiImp implements ApiInterface {

    private RetrofitInterface mRetrofitInterface;

    @Inject
    public ApiImp(RetrofitInterface retrofitInterface) {
        this.mRetrofitInterface = retrofitInterface;
    }

    @Override
    public Observable<List<Charity>> getCharityList() {
        return mRetrofitInterface.getCharityList();
    }

    @Override
    public Observable<String> donate(Donation donation) {
        return mRetrofitInterface.donate(donation);
    }

    @Override
    public Observable<String> getToken() {
        // TODO: 8/12/17 implement getAccessToken
        return Observable.just("Some Valid Token");
    }
}
