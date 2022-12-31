package com.woody.productwarehousing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.woody.productwarehousing.bean.LoginApiUnits;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.model.repository.LoginRepository;
import com.woody.productwarehousing.model.retrofit.ApiResource;
import com.woody.productwarehousing.utils.SingleLiveEvent;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository loginRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = LoginRepository.getInstance();
    }

    public SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentTag) {
        return loginRepository.getInfoMessage(fragmentTag);
    }

    public LiveData<ApiResource<LoginApiUnits>> loginSystem(String account, String password) {
        return loginRepository.loginSystem(account, password);
    }

    public void logoutSystem() {
        loginRepository.logoutSystem();
    }

    public void checkLogin(String account, String password) {
        if (account.length() == 0 || password.length() == 0) {
            TAG.LoadingFailure.setName("帳號或密碼不得為空！");
            loginRepository.getInfoMessage(PAGE.LoginActivity).setValue(TAG.LoadingFailure);
        } else {
            TAG.LoadingSuccess.setName(account + "/" + password);
            loginRepository.getInfoMessage(PAGE.LoginActivity).setValue(TAG.LoadingSuccess);
        }
    }
}
