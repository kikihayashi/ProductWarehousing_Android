package com.woody.productwarehousing.view.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.adapter.PrintAdapter;
import com.woody.productwarehousing.bean.MainApiUnits;
import com.woody.productwarehousing.bean.PrintBean;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.constant.TYPE;
import com.woody.productwarehousing.model.retrofit.ApiResource;
import com.woody.productwarehousing.utils.DialogManager;
import com.woody.productwarehousing.view.BaseActivity;
import com.woody.productwarehousing.view.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class PrintFragment extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {

    private Button single, multiple;
    private ImageView mail;
    private ConstraintLayout firstPageView;
    private PrintAdapter adapter;
    private AlertDialog alertDialog;
    private boolean firstClick = true;
    private static int SPAN_COUNT, ITEM_LAYOUT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_print, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        cardView = getView().findViewById(R.id.cardView);
        progressBarView = getView().findViewById(R.id.view_progressBar);
        tv_progressBar = progressBarView.findViewById(R.id.tv_progressBar);
        firstPageView = getView().findViewById(R.id.view_firstPage);
        single = firstPageView.findViewById(R.id.single);
        multiple = firstPageView.findViewById(R.id.multiple);
        mail = firstPageView.findViewById(R.id.mail);
        single.setOnClickListener(this);
        multiple.setOnClickListener(this);
        mail.setOnLongClickListener(this);
        firstPageView.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        setViewModel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == BaseActivity.DETAIL_REQUEST_CODE) {
            if (resultCode == BaseActivity.DETAIL_SAVE_CODE) {
                PrintBean printBean = (PrintBean) (intent.getSerializableExtra("PrintBean"));
                List<PrintBean> dataList = mainViewModel.getPrintData().getValue();
                dataList.remove(printBean.getIndex());
                dataList.add(printBean.getIndex(), printBean);
                mainViewModel.setPrintBeanList(dataList);
            }
        }
    }

    //設置ViewModel
    private void setViewModel() {
        //觀察來源Table製令單號的變化
        mainViewModel.getOrderData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> orderList) {
                if (adapter != null) {
                    if (orderList.size() > 0) {
                        adapter.setInfoVisibility(true);
                    } else {
                        adapter.setInfoVisibility(false);
                        //查詢資料庫資料並更新到printDataLive裡
                        mainViewModel.setPrintBeanList(createDataList());
                    }
                }
            }
        });

        //觀察列印資料頁面的變化
        mainViewModel.getPrintData().observe(getViewLifecycleOwner(), new Observer<List<PrintBean>>() {
            @Override
            public void onChanged(List<PrintBean> printBeanList) {
                if (adapter != null) {
                    unlockScreenTouch();
                    adapter.setToolModelList(printBeanList);
                    cardView.setVisibility(View.VISIBLE);
                    progressBarView.setVisibility(View.INVISIBLE);
                }
            }
        });

        //觀察處理資料的訊息變化
        mainViewModel.getInfoMessage(PAGE.PrintFragment).observe(getViewLifecycleOwner(), new Observer<TAG>() {
            @Override
            public void onChanged(TAG tag) {
                switch (tag) {
                    //箱子or棧板列印成功 -> 資料庫存入成功，更新畫面
                    case InsertSuccess:
                    case CollectUpdateSuccess:
                        mainViewModel.setPrintBeanList(mainViewModel.getPrintData().getValue());
                        mainViewModel.getWorkTableData(PAGE.UploadFragment);
                        mainViewModel.getWorkTableData(PAGE.ReprintFragment);
                        break;

                    //棧板作廢成功 -> 資料庫更新成功，更新畫面
                    case InvalidUpdateSuccess:
                        showSuccessMessage("棧板已作廢！");
                        mainViewModel.setPrintBeanList(mainViewModel.getPrintData().getValue());
                        mainViewModel.getWorkTableData(PAGE.ReprintFragment);
                        break;

                    //作業錯誤
                    case InsertFailure:
                    case CollectUpdateFailure:
                    case InvalidUpdateFailure:
                        showErrorMessage(tag.getName());
                        break;
                }
            }
        });
    }

    //RecyclerView設置
    private void setRecyclerView(ArrayList<PrintBean> dataList) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        adapter = new PrintAdapter(dataList, ITEM_LAYOUT, new PrintAdapter.RecyclerViewItemClickListener() {
            @Override
            public void onInfoClicked(int dataIndex) {
                if (firstClick) {
                    firstClick = false;
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DetailActivity.class);
                    intent.putExtra("PrintBean", mainViewModel.getPrintData().getValue().get(dataIndex));
                    getActivity().startActivityForResult(intent, BaseActivity.DETAIL_REQUEST_CODE);
                }
            }

            @Override
            public void onPrintClicked(int dataIndex) {
                showProgressBar("條碼列印中...");
                printBarcode(dataIndex);
            }

            @Override
            public void onPrintPalletClicked(int dataIndex) {
                showPalletDialog(dataIndex);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 列印相關Method
     */

    //箱子條碼列印
    private void printBarcode(int dataIndex) {
        mainViewModel.printBarcode(dataIndex).observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //如果有串成功
                        if (mainApiUnits.getMessage().equals("成功")) {
                            try {
                                Thread.sleep(Integer.valueOf(TAG.SleepTime.getName()));
                                mainViewModel.insertWorkTable(dataIndex, mainApiUnits.getQrcode());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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

    //顯示棧板處理種類
    private void showPalletDialog(int dataIndex) {
        //獲取detail_dialog螢幕寬高
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View view = View.inflate(getActivity(), R.layout.dialog_pallet, null);

        //AlertDialog設置
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.show();
        alertDialog.getWindow().setLayout((int) ((displayRectangle.width() * 0.9f)), (int) ((displayRectangle.height() * 0.8f)));

        //detail_dialog元件設置
        CardView palletDialog_cv_bind = view.findViewById(R.id.cv_bind);
        CardView palletDialog_cv_invalid = view.findViewById(R.id.cv_invalid);
        TextView palletDialog_tv_close = view.findViewById(R.id.close);

        palletDialog_cv_bind.setOnClickListener(view_bind -> executePallet(dataIndex, TYPE.Collect));
        palletDialog_cv_invalid.setOnClickListener(view_invalid -> showAdministratorDialog(dataIndex));
        palletDialog_tv_close.setOnClickListener(viewDialog -> alertDialog.dismiss());
    }

    //執行棧板動作
    private void executePallet(int dataIndex, TYPE type) {
        dialogManager.setBaseOption(getString(R.string.Prompt), BaseActivity.infoIcon,
                "確定要\"" + type.getName() + "\"棧板嗎？\n一旦" + type.getName() + "便無法還原！");
        dialogManager.setCancelable(false);
        dialogManager.setButtonCommand(null, getString(R.string.Enter), getString(R.string.Cancel),
                null,
                new DialogManager.NeutralCommand() {
                    @Override
                    public void neutralExecute() {
                        alertDialog.dismiss();
                        switch (type) {
                            case Collect:
                                showProgressBar("棧板列印中...");
                                printPallet(dataIndex);
                                break;

                            case Invalid:
                                showProgressBar("棧板作廢中...");
                                invalidPallet(dataIndex);
                                break;
                        }
                    }
                }, null);
        showHintDialog(dialogManager.createDialog(), SOUND.INFO);
    }

    //棧板綁定
    private void printPallet(int dataIndex) {
        mainViewModel.printPallet(dataIndex).observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //如果SQL Server有串成功
                        if (mainApiUnits.getMessage().equals("成功")) {
                            //更新作業Table(棧板資訊)
                            mainViewModel.updateWorkTable(TYPE.Collect, dataIndex, mainApiUnits.getQrcode());
                        }
                        //如果SQL Server沒有串成功
                        else {
                            showErrorMessage("棧板列印失敗：" + mainApiUnits.getMessage());
                        }
                        break;
                    case LoadingFailure:
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    //棧板作廢
    private void invalidPallet(int dataIndex) {
        mainViewModel.invalidPallet(dataIndex).observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //如果SQL Server有串成功
                        if (mainApiUnits.getMessage().contains("成功")) {
                            //更新作業Table(棧板作廢資訊)
                            mainViewModel.updateWorkTable(TYPE.Invalid, dataIndex);
                        }
                        //如果SQL Server沒有串成功
                        else {
                            showErrorMessage("棧板作廢失敗：" + mainApiUnits.getExMessage());
                        }
                        break;
                    case LoadingFailure:
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    /**
     * 初始化時，可以操作的Method
     */
    //決定使用哪個作業種類
    @Override
    public void onClick(View v) {
        firstPageView.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.single:
                VIEW_NUMBER = 1;
                SPAN_COUNT = 1;
                ITEM_LAYOUT = R.layout.item_print_single;
                break;

            case R.id.multiple:
                VIEW_NUMBER = 4;
                SPAN_COUNT = 2;
                ITEM_LAYOUT = R.layout.item_print_multiple;
                break;
        }
        //初始化一開始列印的頁面
        ArrayList<PrintBean> dataList = createDataList();
        setRecyclerView(dataList);
        //查詢資料庫資料並更新到printDataLive裡
        mainViewModel.setPrintBeanList(dataList);
        //SQL查詢
        mainViewModel.getSourceTableOrder();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("LINS", "print_hidden");
        } else {
            Log.v("LINS", "print_show");
            //SQL查詢
            mainViewModel.getSourceTableOrder();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        firstClick = true;
    }

    //長按信件圖示
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.mail:
//                showAdministratorDialog();
                break;
        }
        return true;
    }

    //顯示管理員Dialog
    private void showAdministratorDialog(int dataIndex) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_admin, null);
        EditText password = view.findViewById(R.id.edit_password);
        dialogManager.resetAllView();
        dialogManager.setTitle("請輸入管理員密碼");
        dialogManager.setView(view);
        dialogManager.setCancelable(true);
        dialogManager.setButtonCommand(null, null, getString(R.string.Enter),
                null, null, new DialogManager.PositiveCommand() {
                    @Override
                    public void positiveExecute() {
                        hideKeyboard2();
                        if (TAG.AdminPWD.getName().equals(password.getText().toString().trim())) {
                            executePallet(dataIndex, TYPE.Invalid);
                        } else {
                            Toast.makeText(getActivity(), "密碼錯誤！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        showHintDialog(dialogManager.createDialog(), SOUND.NONE);
    }
}