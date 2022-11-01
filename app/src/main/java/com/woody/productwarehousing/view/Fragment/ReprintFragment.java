package com.woody.productwarehousing.view.Fragment;

import android.app.AlertDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.adapter.ReprintAdapter;
import com.woody.productwarehousing.adapter.ReprintDialogAdapter;
import com.woody.productwarehousing.bean.MainApiUnits;
import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.model.retrofit.ApiResource;

import java.util.ArrayList;
import java.util.List;

public class ReprintFragment extends BaseFragment {

    private ReprintAdapter adapter;
    private AlertDialog alertDialog;
    private static boolean IS_FIRST_CLICK;//處理程式重新開啟時，會跳出Dialog狀況

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reprint, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        cardView = getView().findViewById(R.id.cardView);
        progressBarView = getView().findViewById(R.id.view_progressBar);
        tv_progressBar = progressBarView.findViewById(R.id.tv_progressBar);
        setRecyclerView();
        setViewModel();
        IS_FIRST_CLICK = true;
    }

    //設置ViewModel
    private void setViewModel() {
        //SQL查詢
        mainViewModel.getWorkTableData(PAGE.ReprintFragment);

        mainViewModel.getReprintData().observe(getViewLifecycleOwner(), new Observer<ArrayList<SourceBean>>() {
            @Override
            public void onChanged(ArrayList<SourceBean> dataList) {
                if (dataList.size() > 0) {
                    adapter.setToolModelList(mainViewModel.sortDataList(dataList));
                } else {
                    adapter.setToolModelList(dataList);
                }
            }
        });

        mainViewModel.getBarcodeData().observe(getViewLifecycleOwner(), new Observer<ArrayList<SourceBean>>() {
            @Override
            public void onChanged(ArrayList<SourceBean> barcodeList) {
                if (barcodeList.size() > 0 && !IS_FIRST_CLICK) {
                    showBarcodeDialog(barcodeList);
                }
            }
        });
    }

    //RecyclerView設置
    private void setRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReprintAdapter(new ArrayList<>(), new ReprintAdapter.RecyclerViewItemClickListener() {
            @Override
            public void onReprintClicked(int dataIndex) {
                showProgressBar("棧板補印中...");
                reprintPallet(dataIndex);
            }

            @Override
            public void onReprintBarcodeClicked(int dataIndex) {
                infoSound();
                IS_FIRST_CLICK = false;
                mainViewModel.getBarcodeRecord(dataIndex);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    //秀出以前使用過的流水號(條碼)
    private void showBarcodeDialog(List<SourceBean> dataList) {
        //獲取detail_dialog螢幕寬高
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View view = View.inflate(getActivity(), R.layout.dialog_reprint, null);

        //AlertDialog設置
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.show();
        alertDialog.getWindow().setLayout((int) ((displayRectangle.width() * 0.9f)), (int) ((displayRectangle.height() * 0.8f)));

        //detail_dialog元件設置
        TextView detailDialog_tv_title = view.findViewById(R.id.title);
        TextView detailDialog_tv_lot = view.findViewById(R.id.lot);
        TextView detailDialog_tv_qty = view.findViewById(R.id.qty);
        TextView detailDialog_tv_close = view.findViewById(R.id.close);
        RecyclerView detailDialog_rv = view.findViewById(R.id.recyclerview);

        detailDialog_tv_title.setText("補印流水號" + ((dataList.get(0).getPallet().equals("")) ? "" : ("(" + dataList.get(0).getPallet() + ")")));
        detailDialog_tv_lot.setText("批號：" + dataList.get(0).getBatchID());
        detailDialog_tv_qty.setText("數量：" + dataList.size());
        detailDialog_tv_close.setOnClickListener(viewDialog -> alertDialog.dismiss());
        detailDialog_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        detailDialog_rv.setAdapter(new ReprintDialogAdapter(dataList, new ReprintDialogAdapter.RecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(int dataIndex) {
                alertDialog.dismiss();
                showProgressBar("條碼補印中...");
                reprintBarcode(dataIndex);
            }
        }));
    }

    //棧板條碼補印
    private void reprintPallet(int dataIndex) {
        mainViewModel.reprintPallet(dataIndex).observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //如果SQL Sever有串成功
                        if (mainApiUnits.getMessage().equals("成功")) {
                            showSuccessMessage("棧板補印成功");
                        }
                        //如果SQL Sever沒有串成功
                        else {
                            showErrorMessage("棧板補印失敗：" + mainApiUnits.getMessage());
                        }
                        break;
                    case LoadingFailure:
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    //箱子條碼補印
    private void reprintBarcode(int dataIndex) {
        mainViewModel.reprintBarcode(dataIndex).observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //如果有串成功
                        if (mainApiUnits.getMessage().equals("成功")) {
                            showSuccessMessage("條碼補印成功");
                        }
                        //如果沒有串成功
                        else {
                            showErrorMessage("條碼列印失敗：\n" + mainApiUnits.getQrcode() +
                                    "\n\n錯誤訊息：" + mainApiUnits.getMessage());
                        }
                        break;
                    case LoadingFailure:
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("LINS", "reprint_hidden");
        } else {
            Log.v("LINS", "reprint_show");
        }
    }
}
