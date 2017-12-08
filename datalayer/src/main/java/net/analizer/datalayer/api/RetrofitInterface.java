package net.analizer.datalayer.api;

import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.models.Donation;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @GET("/")
    Observable<List<Charity>> getCharityList();

    @POST("/donate")
    Observable<String> donate(@Body Donation body);

    // TODO: 8/12/17 implement getAccessToken
}
