package com.woody.productwarehousing.model.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;
import com.woody.productwarehousing.bean.LoginApiUnits;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.model.dao.Dao;
import com.woody.productwarehousing.model.dao.LoginDao;
import com.woody.productwarehousing.model.dao.MainDao;
import com.woody.productwarehousing.model.retrofit.ApiCallService;
import com.woody.productwarehousing.model.retrofit.ApiResource;
import com.woody.productwarehousing.model.retrofit.RetrofitLiveData;
import com.woody.productwarehousing.model.retrofit.RetrofitManager;
import com.woody.productwarehousing.utils.SingleLiveEvent;

import retrofit2.Call;

public class LoginRepository extends BaseRepository {

    //私有的建構式讓別人不能創造，加volatile可以保證都從主記憶體讀取
    private static volatile LoginRepository INSTANCE = null;

    private Dao.LoginDaoInterface loginDaoInterface;
    private ApiCallService apiCallService;//API

    private LoginRepository() {
        super();
        loginDaoInterface = new LoginDao();
        apiCallService = RetrofitManager.getApiCallService(TAG.URL.getName(), TIMEOUT);
    }

    public static LoginRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (MainDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoginRepository();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 觀察各種訊息變化
     */
    public LiveData<ApiResource<LoginApiUnits>> loginSystem(String account, String password) {
        JsonObject object = loginDaoInterface.setLoginJson(account, password);
        Log.v("LINS", "loginSystem：" + object);
        Call<LoginApiUnits> call = apiCallService.login(LOGIN_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    public void logoutSystem() {
        Call<LoginApiUnits> call = apiCallService.logout(LOGOUT_URL);
        new RetrofitLiveData(TAG.Loading, call);
    }

    public SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentTag) {
        return loginDaoInterface.getInfoMessage(fragmentTag);
    }
}
