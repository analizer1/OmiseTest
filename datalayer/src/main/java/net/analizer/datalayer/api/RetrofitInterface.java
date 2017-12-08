package net.analizer.datalayer.api;

import net.analizer.datalayer.models.CharityListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @GET("/")
    Observable<CharityListResponse> getCharityList();

    @POST("/donate")
    Observable<String> donate();
}
