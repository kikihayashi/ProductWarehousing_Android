package com.woody.productwarehousing.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.bean.LoginApiUnits;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.model.retrofit.ApiResource;
import com.woody.productwarehousing.utils.DialogManager;
import com.woody.productwarehousing.viewmodel.LoginViewModel;

import java.net.HttpCookie;
import java.util.List;

import okhttp3.Headers;

public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;
    private CardView cardView;
    private EditText account_tv;
    private EditText password_tv;
    private ProgressBar progressBar;
    private TextView tv_progressBar;
    private LinearLayout progressBarView;
    //ProgressBar的圈圈大小設置
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            init();
        }
    }

    private void init() {
        cardView = findViewById(R.id.cv);
        progressBarView = findViewById(R.id.view_progressBar);
        tv_progressBar = progressBarView.findViewById(R.id.tv_progressBar);
        progressBar = progressBarView.findViewById(R.id.progressBar);
        account_tv = findViewById(R.id.tv_account);
        password_tv = findViewById(R.id.tv_password);
        setViewModel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == BaseActivity.MAIN_LOGIN_CODE) {
            if (resultCode == BaseActivity.MAIN_LOGOUT_CODE) {
                loginViewModel.logoutSystem();
                hideProgressBar();
            }
        }
    }

    //ViewModel設置
    private void setViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getInfoMessage(PAGE.LoginActivity).observe(this, new Observer<TAG>() {
            @Override
            public void onChanged(TAG tag) {
                switch (tag) {
                    case LoadingSuccess:
                        showProgressBar();
                        String account = tag.getName().split("/")[0];
                        String password = tag.getName().split("/")[1];
                        sendLogin(account, password);
                        break;
                    case LoadingFailure:
                        showErrorMessage(tag.getName());
                        break;
                }
            }
        });
    }

    public void login(View view) {
        String account = account_tv.getText().toString().trim();
        String password = password_tv.getText().toString().trim();
        loginViewModel.checkLogin(account, password);
    }

    private void sendLogin(String account, String password) {
        loginViewModel.loginSystem(account, password).observe(this, new Observer<ApiResource<LoginApiUnits>>() {
            @Override
            public void onChanged(ApiResource<LoginApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        // 獲取回應的標頭
                        Headers headers = response.getHeaders();
                        // 查找 'Set-Cookie' 標頭
                        String cookieHeader = headers.get("Set-Cookie");

                        if (cookieHeader != null) {
                            // 解析 'Set-Cookie' 標頭中包含的 Cookie信息
                            List<HttpCookie> cookies = HttpCookie.parse(cookieHeader);

                            for (HttpCookie cookie : cookies) {
                                String name = cookie.getName();
                                String value = cookie.getValue();
                                Log.v("LINS", "COOKIE_name：" + name);
                                Log.v("LINS", "COOKIE_value：" + value);
                                // 將 Cookie 存儲在本地
                                SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
                                preferences.edit().putString(name, value).apply();
                            }
                        }
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivityForResult(intent, BaseActivity.MAIN_LOGIN_CODE);
                        break;
                    case LoadingFailure:
                        hideProgressBar();
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    //錯誤訊息
    private void showErrorMessage(String message) {
        dialogManager.setBaseOption(getString(R.string.Error), errorIcon, message);
        dialogManager.setCancelable(false);
        dialogManager.setButtonCommand(null, null, getString(R.string.Enter),
                null, null, new DialogManager.PositiveCommand() {
                    @Override
                    public void positiveExecute() {
                        dialogManager.dismissDialog();
                    }
                });
        showHintDialog(dialogManager.createDialog(), SOUND.ERROR);
    }

    //顯示載入動畫
    private void showProgressBar() {
        tv_progressBar.setText("登入中...");
        progressBar.setLayoutParams(params);
        progressBarView.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.INVISIBLE);
    }

    //隱藏載入動畫
    private void hideProgressBar() {
        progressBarView.setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        dialogManager.setBaseOption(getString(R.string.Exit), BaseActivity.emptyIcon, "確定要退出?");
        dialogManager.setCancelable(true);
        dialogManager.setButtonCommand(null, getString(R.string.Exit), getString(R.string.Cancel),
                null, () -> finish(), null);
        showHintDialog(dialogManager.createDialog(), SOUND.INFO);
    }

}