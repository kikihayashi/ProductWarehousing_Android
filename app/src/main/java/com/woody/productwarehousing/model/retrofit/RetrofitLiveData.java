package com.woody.productwarehousing.model.retrofit;

import android.util.Log;
import androidx.lifecycle.LiveData;
import com.woody.productwarehousing.constant.TAG;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitLiveData<T> extends LiveData<ApiResource<T>> {

    private Call<T> call;
    private TAG tag;

    public RetrofitLiveData(TAG tag, Call<T> call) {
        this.call = call;
        this.tag = tag;
        setValue(ApiResource.requesting(tag));
        call.enqueue(callback);
    }

    Callback<T> callback = new Callback<T>() {
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            setValue(ApiResource.create(tag, response));
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.v("LINS","Throwable：" + t.getMessage());
            setValue(ApiResource.failure(t));
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (!hasActiveObservers()) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }
}
