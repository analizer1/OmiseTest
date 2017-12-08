package net.analizer.datalayer.api;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.models.CharityListResponse;
import net.analizer.domainlayer.models.Donation;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class ApiHelper implements ApiInterface {

    private RetrofitInterface mRetrofitInterface;

    public ApiHelper(RetrofitInterface retrofitInterface) {
        this.mRetrofitInterface = retrofitInterface;
    }

    @Override
    public Observable<List<Charity>> getCharityList() {
        return mRetrofitInterface.getCharityList()
                .map(new Function<CharityListResponse, List<Charity>>() {
                    @Override
                    public List<Charity> apply(CharityListResponse charityListResponse) throws Exception {
                        return charityListResponse.charityList;
                    }
                });
    }

    @Override
    public Observable<String> donate(Donation donation) {
        return mRetrofitInterface.donate(donation);
    }
}
