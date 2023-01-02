package com.woody.productwarehousing.model.retrofit;

import android.accounts.NetworkErrorException;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.woody.productwarehousing.constant.TAG;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Response;

public class ApiResource<T> {

    @NonNull
    private final TAG tag;
    @Nullable
    private Response<T> response;

    private ApiResource(TAG tag, @Nullable Response<T> response) {
        this.tag = tag;
        this.response = response;
    }

    public static <T> ApiResource<T> requesting(TAG tag) {
        return new ApiResource<>(tag, null);
    }

    public static <T> ApiResource<T> create(TAG tag, Response<T> response) {
        if (response.isSuccessful()) {
            switch (tag) {
                case Loading:
                    tag = TAG.LoadingSuccess;
                    break;
                case Uploading:
                    tag = TAG.UploadingResult;
                    break;
            }
            return new ApiResource<>(tag, response);
        }
        String errorMessage = "";
        try {
            String jsonString = response.errorBody().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            errorMessage = jsonObject.getString("message");
        } catch (IOException | JSONException e) {
            errorMessage = "請檢查網路狀態或連線網址是否有誤！";
            e.printStackTrace();
        }
        Log.v("LINS", "response is not successful：" + response.code());
        String message = "HTTP " + response.code() + "：錯誤，" + errorMessage;
        TAG.LoadingFailure.setName(message);
        return new ApiResource<>(TAG.LoadingFailure, null);
    }

    public static <T> ApiResource<T> failure(Throwable t) {
        if (t != null) {
            String message;
            if (t instanceof SocketTimeoutException) {
                message = "網路連線逾時！" + "\n" + t.toString();
            } else if (t instanceof IOException) {
                if (t instanceof EOFException) {
                    message = "回傳資料為空！" + "\n" + t.toString();
                } else {
                    message = "資料取得失敗！" + "\n" + t.toString();
                }
            } else if (t instanceof NetworkErrorException) {
                message = "網路連線失敗，請檢查網路！" + "\n" + t.toString();
            } else {
                message = "伺服器問題，請稍後再試！" + "\n" + t.toString();
                ;
            }
            Log.v("LINS", "message_failure：" + message);
            TAG.LoadingFailure.setName(message);
        }
        return new ApiResource<>(TAG.LoadingFailure, null);
    }

    @Nullable
    public T body() {
        return response.body();
    }

    @NonNull
    public TAG getTag() {
        return tag;
    }

    public Headers getHeaders() {
        return response.headers();
    }

    public boolean isSuccessful() {
        return response.isSuccessful();
    }

    public int code() {
        return response.code();
    }

}