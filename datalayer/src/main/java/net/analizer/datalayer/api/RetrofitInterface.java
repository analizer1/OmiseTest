package net.analizer.datalayer.api;

import net.analizer.domainlayer.models.CharityListResponse;
import net.analizer.domainlayer.models.Donation;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @GET("/")
    Observable<CharityListResponse> getCharityList();

    @POST("/donate")
    Observable<String> donate(@Body Donation body);
}
