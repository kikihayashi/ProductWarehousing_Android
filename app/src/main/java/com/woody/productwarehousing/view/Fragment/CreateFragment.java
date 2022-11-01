package com.woody.productwarehousing.view.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.adapter.CreateAdapter;
import com.woody.productwarehousing.bean.MainApiUnits;
import com.woody.productwarehousing.bean.OrderApiUnits;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.model.retrofit.ApiResource;

import java.util.ArrayList;

public class CreateFragment extends BaseFragment implements View.OnClickListener {

    private ImageView add;
    private TextView tv_add;
    private EditText edit_order;
    private Button clear, search;
    private CreateAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        add = getView().findViewById(R.id.add);
        tv_add = getView().findViewById(R.id.tv_add);
        clear = getView().findViewById(R.id.clear);
        search = getView().findViewById(R.id.search);
        edit_order = getView().findViewById(R.id.order);
        cardView = getView().findViewById(R.id.cardView);
        progressBarView = getView().findViewById(R.id.view_progressBar);
        tv_progressBar = progressBarView.findViewById(R.id.tv_progressBar);

        add.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        clear.setOnClickListener(this);
        search.setOnClickListener(this);
        edit_order.requestFocus();
        edit_order.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_UP == keyEvent.getAction()) {
                    edit_order.selectAll();
                    hideKeyboard();
                    ArrayList<String> orderList = adapter.getToolModelList();
                    String order = edit_order.getText().toString().replaceAll(" ", "").trim();
                    if (order.length() != 11) {
                        showErrorMessage("製令單號長度需為11");
                        return true;
                    }
                    //驗證是否重複，如果有跳出警告
                    if (orderList.contains(order)) {
                        showErrorMessage("製令單號已存在！");
                        return true;
                    } else {
                        orderList.add(order);
                    }
                    adapter.setToolModelList(orderList);
                    tv_add.setText(String.valueOf(Long.valueOf(orderList.get(orderList.size() - 1)) + 1));
                    search.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        setRecyclerView();
        setViewModel();
    }

    //ViewModel設置
    private void setViewModel() {
        //SQL查詢是否有來源製令單
        mainViewModel.getSourceTableOrder();

        //觀察SQL來源製令單的變化
        mainViewModel.getOrderData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> orderList) {
                if (orderList.size() > 0) {
                    tv_add.setText(String.valueOf(Long.valueOf(orderList.get(orderList.size() - 1)) + 1));
                    search.setVisibility(View.VISIBLE);
                } else {
                    search.setVisibility(View.GONE);
                }
                adapter.setToolModelList(orderList);
            }
        });

        //觀察處理資料的訊息變化
        mainViewModel.getInfoMessage(PAGE.CreateFragment).observe(getViewLifecycleOwner(), new Observer<TAG>() {
            @Override
            public void onChanged(TAG tag) {
                switch (tag) {
                    //來源資料庫重置成功，開始串API
                    case ResetSuccess:
                        showProgressBar("與連線中...\n請勿離開此頁面");
                        searchOrderData(adapter.getToolModelList().get(0));
                        break;

                    //來源資料庫存入成功
                    case InsertSuccess:
                        if (adapter != null && adapter.getToolModelList().size() > 0) {
                            String message = "已取得製令單號資料！";
                            if (mainViewModel.getExpiredMKOrdNO().size() > 0) {
                                String expiredMKOrdNO = mainViewModel.getExpiredMKOrdNO().toString();
                                message += "\n其中製令單：" + expiredMKOrdNO.substring(1, expiredMKOrdNO.length() - 1) + " 已結案！";
                            }
                            showSuccessMessage(message);
                            mainViewModel.setPrintBeanList(createDataList());
                        } else {
                            showErrorMessage("錯誤：抓取資料時，請勿離開頁面！");
                        }
                        break;

                    //作業錯誤
                    case ResetFailure:
                    case InsertFailure:
                        showErrorMessage("發生錯誤：" + tag.getName());
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> orderList = new ArrayList<>();
        switch (v.getId()) {
            //清空全部
            case R.id.clear:
                adapter.setToolModelList(orderList);
                search.setVisibility(View.GONE);
                break;

            //快速新增
            case R.id.add:
            case R.id.tv_add:
                orderList = adapter.getToolModelList();
                String order = tv_add.getText().toString().replaceAll(" ", "").trim();
                if (!order.equals("")) {
                    if (orderList.size() > 0) {
                        if (orderList.contains(order)) {
                            showErrorMessage("製令單號" + order + "已存在！");
                            return;
                        } else {
                            orderList.add(order);
                        }
                    } else {
                        orderList.add(order);
                    }
                    adapter.setToolModelList(orderList);
                    recyclerView.getLayoutManager().scrollToPosition(orderList.size() - 1);
                    tv_add.setText(String.valueOf(Long.valueOf(orderList.get(orderList.size() - 1)) + 1));
                    search.setVisibility(View.VISIBLE);
                }
                break;

            //抓取資料
            case R.id.search:
                //準備串API
                if (adapter.getToolModelList().size() > 0) {
                    //先重置來源資料庫&來源BeanList
                    mainViewModel.resetAllSourceData();
                }
                break;
        }
    }

    //觀察API取到的狀態(串製令單號)
    private void searchOrderData(String MKOrdNO) {
        mainViewModel.searchOrder(MKOrdNO).observe(getViewLifecycleOwner(), new Observer<ApiResource<OrderApiUnits>>() {
            @Override
            public void onChanged(ApiResource<OrderApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        try {
                            OrderApiUnits orderApiUnitsOrder = response.body();
                            //如果有串成功
                            if (orderApiUnitsOrder.getResult().get(0).getIfSucceed().equals("True")) {
                                //如果此製令單可以入庫
                                if (orderApiUnitsOrder.getMasterData().get(0).getBillStatus() == 0) {
                                    showProgressBar("單號：" + MKOrdNO + "\n取得成功...");
                                    //先存入一些資訊，串INFO API會用到
                                    mainViewModel.setSourceBeanList(orderApiUnitsOrder);
                                }
                                //如果此製令單不可以入庫
                                else {
                                    //存入過期列表，最後提示
                                    mainViewModel.setExpiredMKOrdNO(MKOrdNO);
                                }
                                INDEX_ORDER++;
                                //如果目前串的數量 = 全部需要串的數量
                                if (INDEX_ORDER == adapter.getToolModelList().size()) {
                                    INDEX_ORDER = 0;
                                    searchInfoData();
                                } else {
                                    searchOrderData(adapter.getToolModelList().get(INDEX_ORDER));
                                }
                            }
                            //如果沒有串成功
                            else {
                                showErrorMessage("製令單號" + MKOrdNO + "取得失敗\n" +
                                        "錯誤訊息：" + orderApiUnitsOrder.getResult().get(0).getErrMessage());
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            showErrorMessage("製令單取得失敗，請勿跳轉頁面！");
                        }
                        break;
                    case LoadingFailure:
                        //重新查詢是否有存在的製令單
                        mainViewModel.getSourceTableOrder();
//                        showErrorMessage("製令單號" + MKOrdNO + "取得失敗，請勿跳轉頁面！");
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    //觀察API取到的狀態(串料號)
    private void searchInfoData() {
        mainViewModel.searchInfo().observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //如果有串成功
                        if (mainApiUnits.getMessage().equals("")) {
                            //存入來源Table
                            mainViewModel.insertSourceTable(mainApiUnits.getData().getQuery1());
                        }
                        //如果沒有串成功
                        else {
                            showErrorMessage("料號取得失敗\n" +
                                    "API錯誤訊息：" + mainApiUnits.getMessage());
                        }
                        break;
                    case LoadingFailure:
                        Log.v("LINS","LoadingFailure");
                        //重新查詢是否有存在的製令單
                        mainViewModel.getSourceTableOrder();
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    //RecyclerView設置
    private void setRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CreateAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        setItemTouchHelper();
    }

    //RecyclerView拖曳設置
    private void setItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int touchPosition = viewHolder.getAdapterPosition();
                adapter.removeItem(touchPosition);
                search.setVisibility((adapter.getToolModelList().size() == 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("LINS", "create_hidden");
        } else {
            Log.v("LINS", "create_show");
            //SQL查詢
            mainViewModel.getSourceTableOrder();
        }
    }

}