package com.woody.productwarehousing.model.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    public static ApiCallService getApiCallService(String baseUrl, int second) {
        OkHttpClient okHttpClient = setOkHttpTimeout(second).build();
        return getRetrofit(baseUrl, okHttpClient).create(ApiCallService.class);
    }

    public static ApiCallService getApiCallService(String baseUrl, int second, Interceptor interceptor) {
        OkHttpClient okHttpClient = setOkHttpTimeout(second)
                .addInterceptor(interceptor)
                .build();
        return getRetrofit(baseUrl, okHttpClient).create(ApiCallService.class);
    }

    private static OkHttpClient.Builder setOkHttpTimeout(int second){
        return new OkHttpClient().newBuilder()
                .connectTimeout(second, TimeUnit.SECONDS)   // 設置連線Timeout
                .writeTimeout(second, TimeUnit.SECONDS)
                .readTimeout(second, TimeUnit.SECONDS);
    }

    private static Retrofit getRetrofit(String baseUrl, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
