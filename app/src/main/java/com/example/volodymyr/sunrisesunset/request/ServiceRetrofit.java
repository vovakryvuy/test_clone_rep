package com.example.volodymyr.sunrisesunset.request;

import android.app.Application;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Volodymyr on 1/22/2018.
 */

public class ServiceRetrofit extends Application {
    private Retrofit mRetrofit;
    private static InterfaceResponse sInterfaceResponse;
    private static final String BASE_URL = "https://api.sunrise-sunset.org";

    @Override
    public void onCreate() {
        super.onCreate();


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(httpLoggingInterceptor);

        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client.build())
                .build();
        Log.d("LOG","Create Retrofit");

        sInterfaceResponse = mRetrofit.create(InterfaceResponse.class);
    }

    public static InterfaceResponse getService(){
        return sInterfaceResponse;
    }
}
