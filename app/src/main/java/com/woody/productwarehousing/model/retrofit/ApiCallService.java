package com.woody.productwarehousing.model.retrofit;

import com.google.gson.JsonObject;
import com.woody.productwarehousing.bean.OrderApiUnits;
import com.woody.productwarehousing.bean.MainApiUnits;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiCallService {
    //設置一個POST連線，路徑為ORDER_LIST_URL
    //取得的回傳資料用物件接收，連線名稱取為getBaseUnits()

    @POST("{Send}")
    Call<OrderApiUnits> getChiApiUnits(@Path(value = "Send", encoded = true) String SEND_URL, @Body JsonObject body);

    @POST("{Send}")
    Call<MainApiUnits> getRegalUnits(@Path(value = "Send", encoded = true) String SEND_URL, @Body JsonObject body);

    @Multipart
    @POST("{Send}")
    Call<MainApiUnits> getRegalUnits(@Path("Send") String SEND_URL,
                                     @Part("ID") RequestBody id,
                                     @Part("Date") RequestBody date,
                                     @Part MultipartBody.Part filePart);

//    @GET("{Send}")
//    Call<ChiApiUnits> getChiApiUnits(@Path(value = "Send", encoded = true) String SEND_URL);
//
//    @GET("{Send}")
//    Call<RegalApiUnits> getRegalUnits(@Path(value = "Send", encoded = true) String SEND_URL);




//    @POST("SearchOutcheck/SearchOutcheck")
//    Call<BaseUnits2> getBaseUnits(@Body RequestBody params);
//
//    @POST("{Send}")
//    Call<ApiUnits> getBaseUnits(@Path("Send") String SEND_URL, @Body RequestBody params);
//
//    @POST("T357/api")
//    Call<ApiUnits> getApiUnits(@Body JsonObject body);
//
//    @POST("{Send}")
//    Call<ApiUnits> getBaseUnits(@Path("Send") String SEND_URL, @Body HashMap<String, String> body);
}
