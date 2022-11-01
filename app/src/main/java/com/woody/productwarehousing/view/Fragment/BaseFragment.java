package com.woody.productwarehousing.view.Fragment;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.bean.PrintBean;
import com.woody.productwarehousing.utils.DialogManager;
import com.woody.productwarehousing.view.BaseActivity;
import com.woody.productwarehousing.viewmodel.MainViewModel;

import java.util.ArrayList;

public class BaseFragment extends Fragment {

    private int alertError, alertInfo;
    private SoundPool soundPool;
    protected DialogManager dialogManager;
    protected RecyclerView recyclerView;
    protected LinearLayout progressBarView;
    protected TextView tv_progressBar;
    protected CardView cardView;

    //串API需要的參數
    protected static int UPLOAD_INDEX = 0;
    protected static int INDEX_ORDER = 0;
    protected static int INDEX_INFO = 0;
    protected static int NUMBER_INFO = 0;
    protected static int VIEW_NUMBER = 4;
    protected static String ERROR_MESSAGE = "";
    protected static String SUCCESS_MESSAGE = "";

    protected MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        alertError = soundPool.load(getActivity(), R.raw.error, 1);
        alertInfo = soundPool.load(getActivity(), R.raw.info, 2);
        dialogManager = new DialogManager(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    }

    protected void hideKeyboard() {
        final View view = getActivity().getCurrentFocus();
        if (view != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }, 100);
        }
    }

    protected void hideKeyboard2() {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    //多選開窗音效
    protected void infoSound() {
        //通知音效
        soundPool.play(alertInfo, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    //07-錯誤震動及音效
    protected void setVibrateSound(int time) {
        //需加入權限- <uses-permission android:name="android.permission.VIBRATE" />
        Vibrator myVibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
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

    //提示訊息
    protected void showAlertMessage(String message) {
        unlockScreenTouch();
        progressBarView.setVisibility(View.INVISIBLE);
        dialogManager.setBaseOption(getString(R.string.Prompt), BaseActivity.infoIcon, message);
        dialogManager.setCancelable(false);
        dialogManager.setButtonCommand(null, null, getString(R.string.Enter),
                null, null, new DialogManager.PositiveCommand() {
                    @Override
                    public void positiveExecute() {
                        cardView.setVisibility(View.VISIBLE);
                    }
                });
        showHintDialog(dialogManager.createDialog(), SOUND.INFO);
    }

    //成功訊息
    protected void showSuccessMessage(String message) {
        unlockScreenTouch();
        progressBarView.setVisibility(View.INVISIBLE);
        dialogManager.setBaseOption(getString(R.string.Success), BaseActivity.successIcon, message);
        dialogManager.setCancelable(false);
        dialogManager.setButtonCommand(null, null, getString(R.string.Enter),
                null, null, new DialogManager.PositiveCommand() {
                    @Override
                    public void positiveExecute() {
                        cardView.setVisibility(View.VISIBLE);
                    }
                });
        showHintDialog(dialogManager.createDialog(), SOUND.INFO);
    }

    //錯誤訊息
    protected void showErrorMessage(String message) {
        unlockScreenTouch();
        resetIndex();
        progressBarView.setVisibility(View.INVISIBLE);
        dialogManager.setBaseOption(getString(R.string.Error), BaseActivity.errorIcon, message);
        dialogManager.setCancelable(false);
        dialogManager.setButtonCommand(null, null, getString(R.string.Enter),
                null, null, new DialogManager.PositiveCommand() {
                    @Override
                    public void positiveExecute() {
                        cardView.setVisibility(View.VISIBLE);
                    }
                });
        showHintDialog(dialogManager.createDialog(), SOUND.ERROR);
    }

    //重置
    protected void resetIndex() {
        UPLOAD_INDEX = 0;
        INDEX_ORDER = 0;
        INDEX_INFO = 0;
        NUMBER_INFO = 0;
        ERROR_MESSAGE = "";
        SUCCESS_MESSAGE = "";
    }

    protected ArrayList<PrintBean> createDataList() {
        ArrayList<PrintBean> dataList = new ArrayList<>();
        for (int i = 0; i < VIEW_NUMBER; i++) {
            PrintBean printBean = new PrintBean();
            printBean.setIndex(i);
            dataList.add(printBean);
        }
        return dataList;
    }

    protected void showProgressBar(String message) {
        lockScreenTouch();
        cardView.setVisibility(View.INVISIBLE);
        progressBarView.setVisibility(View.VISIBLE);
        tv_progressBar.setText(message);
    }

    protected void lockScreenTouch() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void unlockScreenTouch() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
