package com.example.volodymyr.sunrisesunset.request;

import com.example.volodymyr.sunrisesunset.model_response.ResponseModel;
import com.example.volodymyr.sunrisesunset.model_response.city_information.ResponseCityInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Volodymyr on 1/22/2018.
 */

public interface InterfaceResponse {
    @GET("/json")
    Call<ResponseModel> getSunInfo(@Query("lat") float latitude, @Query("lng") float longitude,
                                   @Query("date")String date, @Query("callback")String callback,
                                   @Query("formatted") Integer formatted);
    @GET
    Call<ResponseCityInformation> getCityInformation(@Url String url,
                                                     @Query("latlng") String latlng,
                                                     @Query("key")String key);
}
