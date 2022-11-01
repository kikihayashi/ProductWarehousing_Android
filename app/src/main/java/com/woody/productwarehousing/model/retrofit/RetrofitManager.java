package com.woody.productwarehousing.model.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static Retrofit retrofit;

    public static ApiCallService getApiCallService(String baseUrl, int second) {
        return getRetrofit(baseUrl, second).create(ApiCallService.class);
    }

    private static Retrofit getRetrofit(String baseUrl, int second) {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(second, TimeUnit.SECONDS)   // 設置連線Timeout
                .writeTimeout(second, TimeUnit.SECONDS)
                .readTimeout(second, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }
}
