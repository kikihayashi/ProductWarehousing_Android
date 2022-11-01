package com.woody.productwarehousing.view.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.adapter.UploadAdapter;
import com.woody.productwarehousing.bean.MainApiUnits;
import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.constant.TYPE;
import com.woody.productwarehousing.model.retrofit.ApiResource;
import com.woody.productwarehousing.utils.IoTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadFragment extends BaseFragment implements View.OnClickListener {

    private Button upload;
    private TextView not_upload, not_collect;
    private UploadAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        upload = getView().findViewById(R.id.upload);
        not_upload = getView().findViewById(R.id.not_upload);
        not_collect = getView().findViewById(R.id.not_collect);
        cardView = getView().findViewById(R.id.cardView);
        progressBarView = getView().findViewById(R.id.view_progressBar);
        tv_progressBar = progressBarView.findViewById(R.id.tv_progressBar);
        cardView.setVisibility(View.INVISIBLE);
        upload.setVisibility(View.GONE);
        upload.setOnClickListener(this);
        setRecyclerView();
        setViewModel();
    }

    //設置ViewModel
    private void setViewModel() {
        //SQL查詢
        mainViewModel.getWorkTableData(PAGE.UploadFragment);

        //觀察上傳資料的變化
        mainViewModel.getUploadData().observe(getViewLifecycleOwner(), new Observer<ArrayList<SourceBean>>() {
            @Override
            public void onChanged(ArrayList<SourceBean> dataList) {
                //已綁定棧板尚未上傳
                int notUploadNumber = dataList.size();
                //尚未綁定棧板
                int notCollectNumber = 0;
                upload.setVisibility((notUploadNumber > 0) ? View.VISIBLE : View.GONE);
                not_upload.setVisibility((notUploadNumber > 0) ? View.VISIBLE : View.GONE);
                not_collect.setVisibility((notCollectNumber > 0) ? View.VISIBLE : View.GONE);
                not_upload.setText("未上傳資料數：" + notUploadNumber);
                not_collect.setText("未綁定棧板數：" + notCollectNumber);
                cardView.setVisibility(View.VISIBLE);
                adapter.setToolModelList(dataList);
            }
        });

        //觀察處理資料的訊息變化
        mainViewModel.getInfoMessage(PAGE.UploadFragment).observe(getViewLifecycleOwner(), new Observer<TAG>() {
            @Override
            public void onChanged(TAG tag) {
                switch (tag) {
                    //上傳資料到成功
                    case UploadSuccess:
                        iterativeUpload();
                        break;
                    //上傳資料到失敗
                    case UploadFailure:
                        showErrorMessage(tag.getName());
                        break;
                }
            }
        });
    }

    //RecyclerView設置
    private void setRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UploadAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                showProgressBar("上傳資料中\n請勿離開此頁面");
                IoTool.setDistinctFileName();
                upload(0);
                break;
        }
    }

    //上傳入庫資料
    private void upload(int dataIndex) {
        HashMap<String, List<SourceBean>> sourceBeanMap = mainViewModel.getUploadFinalData().getValue();
        List<String> orderList = mainViewModel.getOrderFinalData().getValue();
        List<SourceBean> sourceBeanList = sourceBeanMap.get(orderList.get(dataIndex));
        mainViewModel.uploadData(sourceBeanList).observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //如果上傳有串成功
                        if (mainApiUnits.getResult().getIfSucceed().equals("True")) {
                            String WareInNO = mainApiUnits.getResult().getWorkId();

                            SUCCESS_MESSAGE += "單號：" + orderList.get(dataIndex) + "上傳成功\n" +
                                            "入庫單號為 " + WareInNO + "\n\n";

                            showProgressBar("單號：" + orderList.get(dataIndex) + "上傳成功...");

                            mainViewModel.updateWorkTable(TYPE.Upload, dataIndex, WareInNO);
                        }
                        //如果上傳沒有串成功
                        else {
                            String errorMessage = mainApiUnits.getResult().getErrMessage();
                            ERROR_MESSAGE += "單號" + orderList.get(dataIndex) +"上傳失敗\n" +
                                            "錯誤訊息：" + errorMessage + "\n\n";

                            mainViewModel.insertWareInTable(TAG.LoadingSuccess, dataIndex, false, "", errorMessage);
                        }
                        break;
                    case LoadingFailure:
                        mainViewModel.insertWareInTable(TAG.LoadingFailure, dataIndex, false, "", TAG.LoadingFailure.getName());
                        mainViewModel.setPrintBeanList(mainViewModel.getPrintData().getValue());
                        break;
                }
            }
        });
    }

    //循環上傳入庫單
    private void iterativeUpload() {
        UPLOAD_INDEX++;
        if (UPLOAD_INDEX == mainViewModel.getOrderFinalData().getValue().size()) {
            //匯出資料庫壓縮檔
            File DB_FILE = mainViewModel.exportDatabase();
            //如果有壓縮檔
            if (DB_FILE != null) {
                mainViewModel.uploadDataBase(DB_FILE).observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
                    @Override
                    public void onChanged(ApiResource<MainApiUnits> response) {
                        switch (response.getTag()) {
                            case LoadingSuccess:
                                MainApiUnits mainApiUnits = response.body();
                                //如果上傳資料庫有串成功
                                if (mainApiUnits.getSuccess().equals("true")) {
                                    SUCCESS_MESSAGE = "資料庫上傳成功\n\n" + SUCCESS_MESSAGE;
                                }
                                //如果上傳資料庫沒有串成功
                                else {
                                   ERROR_MESSAGE = "資料庫上傳失敗\n\n" + ERROR_MESSAGE;
                                }
                                arrangeResultMessage();
                                break;
                            case LoadingFailure:
                                ERROR_MESSAGE = "資料庫上傳失敗\n\n" + ERROR_MESSAGE;
                                arrangeResultMessage();
                                break;
                        }
                    }
                });
            }
            //如果沒有壓縮檔
            else {
                ERROR_MESSAGE = "資料庫匯出失敗\n\n" + ERROR_MESSAGE;
                arrangeResultMessage();
            }
        }
        else {
            upload(UPLOAD_INDEX);
        }
    }

    //結束上傳入庫畫面，整理成功錯誤訊息
    private void arrangeResultMessage() {
        if (ERROR_MESSAGE.equals("") && !SUCCESS_MESSAGE.equals("")) {
            showSuccessMessage(SUCCESS_MESSAGE);
        } else if (SUCCESS_MESSAGE.equals("") && !ERROR_MESSAGE.equals("")) {
            showErrorMessage(ERROR_MESSAGE);
        } else {
            showAlertMessage(SUCCESS_MESSAGE + ERROR_MESSAGE);
        }
        resetIndex();
        mainViewModel.getWorkTableData(PAGE.UploadFragment);
        mainViewModel.setPrintBeanList(mainViewModel.getPrintData().getValue());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("LINS", "upload_hidden");
        } else {
            Log.v("LINS", "upload_show");
        }
    }
}
