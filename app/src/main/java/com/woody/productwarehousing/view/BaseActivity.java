package com.woody.productwarehousing.view;

import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.utils.DialogManager;

import java.text.SimpleDateFormat;

public class BaseActivity extends AppCompatActivity {

    private static long lastClickTime;
    public static int MAIN_LOGIN_CODE = 100;
    public static int MAIN_LOGOUT_CODE = 101;
    public static int DETAIL_REQUEST_CODE = 200;
    public static int DETAIL_SAVE_CODE = 201;
    private int alertError, alertInfo;
    private SoundPool soundPool;
    public DialogManager dialogManager;
    private int requestedOrientation;
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    //錯誤標誌
    public static int errorIcon = R.drawable.ico_error;
    //提示標誌
    public static int infoIcon = R.drawable.ico_info;
    //成功標誌
    public static int successIcon = R.drawable.ico_success;
    //空標誌
    public static int emptyIcon = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(requestedOrientation);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        alertError = soundPool.load(this, R.raw.error, 1);
        alertInfo = soundPool.load(this, R.raw.info, 2);
        dialogManager = new DialogManager(this);
    }

    public void setToolbar(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //設置返回鍵可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);//標題
        toolbar.setTitleTextColor(Color.WHITE);//顏色
    }

    //多選開窗音效
    public void infoSound() {
        //通知音效
        soundPool.play(alertInfo, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    //07-錯誤震動及音效
    public void setVibrateSound(int time) {
        //需加入權限- <uses-permission android:name="android.permission.VIBRATE" />
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);

        //錯誤音效
        soundPool.play(alertError, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    protected AlertDialog showHintDialog(AlertDialog alertDialog, @Nullable SOUND sound) {
        alertDialog.show();
        switch (sound) {
            case ERROR:
                setVibrateSound(300);
                break;
            case INFO:
                infoSound();
                break;
            case NONE:
                break;
        }
        return alertDialog;
    }

    public enum SOUND {
        ERROR, INFO, NONE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (dialogManager != null) {
                dialogManager.dismissDialog();
                dialogManager = null;
            }
            if (soundPool != null) {
                soundPool.release();
                soundPool = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(requestedOrientation);
    }

    protected void hideKeyboard() {
        final View view = getCurrentFocus();
        if (view != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }, 100);
        }
    }

    protected static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0L < timeD && timeD < 1000L) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}