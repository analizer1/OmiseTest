package net.analizer.datalayer.api;

import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.models.Donation;
import net.analizer.domainlayer.models.DonationResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @GET("/")
    Observable<List<Charity>> getCharityList();

    @Headers("Content-Type: application/json")
    @POST("/donate")
    Observable<DonationResponse> donate(@Body Donation donation);
}
