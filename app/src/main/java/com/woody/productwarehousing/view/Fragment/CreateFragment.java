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
                        showErrorMessage("????????????????????????11");
                        return true;
                    }
                    //??????????????????????????????????????????
                    if (orderList.contains(order)) {
                        showErrorMessage("????????????????????????");
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

    //ViewModel??????
    private void setViewModel() {
        //SQL??????????????????????????????
        mainViewModel.getSourceTableOrder();

        //??????SQL????????????????????????
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

        //?????????????????????????????????
        mainViewModel.getInfoMessage(PAGE.CreateFragment).observe(getViewLifecycleOwner(), new Observer<TAG>() {
            @Override
            public void onChanged(TAG tag) {
                switch (tag) {
                    //???????????????????????????????????????API
                    case ResetSuccess:
                        showProgressBar("??????API???...\n?????????????????????");
                        searchOrderData(adapter.getToolModelList().get(0));
                        break;

                    //???????????????????????????
                    case InsertSuccess:
                        if (adapter != null && adapter.getToolModelList().size() > 0) {
                            String message = "??????????????????????????????";
                            if (mainViewModel.getExpiredMKOrdNO().size() > 0) {
                                String expiredMKOrdNO = mainViewModel.getExpiredMKOrdNO().toString();
                                message += "\n??????????????????" + expiredMKOrdNO.substring(1, expiredMKOrdNO.length() - 1) + " ????????????";
                            }
                            showSuccessMessage(message);
                            mainViewModel.setPrintBeanList(createDataList());
                        } else {
                            showErrorMessage("????????????????????????????????????????????????");
                        }
                        break;

                    //????????????
                    case ResetFailure:
                    case InsertFailure:
                        showErrorMessage("???????????????" + tag.getName());
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> orderList = new ArrayList<>();
        switch (v.getId()) {
            //????????????
            case R.id.clear:
                adapter.setToolModelList(orderList);
                search.setVisibility(View.GONE);
                break;

            //????????????
            case R.id.add:
            case R.id.tv_add:
                orderList = adapter.getToolModelList();
                String order = tv_add.getText().toString().replaceAll(" ", "").trim();
                if (!order.equals("")) {
                    if (orderList.size() > 0) {
                        if (orderList.contains(order)) {
                            showErrorMessage("????????????" + order + "????????????");
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

            //????????????
            case R.id.search:
                //?????????API
                if (adapter.getToolModelList().size() > 0) {
                    //????????????????????????&??????BeanList
                    mainViewModel.resetAllSourceData();
                }
                break;
        }
    }

    //??????API???????????????(???????????????)
    private void searchOrderData(String MKOrdNO) {
        mainViewModel.searchOrder(MKOrdNO).observe(getViewLifecycleOwner(), new Observer<ApiResource<OrderApiUnits>>() {
            @Override
            public void onChanged(ApiResource<OrderApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        try {
                            OrderApiUnits orderApiUnitsOrder = response.body();
                            //??????????????????
                            if (orderApiUnitsOrder.getResult().get(0).getIfSucceed().equals("True")) {
                                //??????????????????????????????
                                if (orderApiUnitsOrder.getMasterData().get(0).getBillStatus() == 0) {
                                    showProgressBar("?????????" + MKOrdNO + "\n????????????...");
                                    //???????????????????????????INFO API?????????
                                    mainViewModel.setSourceBeanList(orderApiUnitsOrder);
                                }
                                //?????????????????????????????????
                                else {
                                    //?????????????????????????????????
                                    mainViewModel.setExpiredMKOrdNO(MKOrdNO);
                                }
                                INDEX_ORDER++;
                                //???????????????????????? = ????????????????????????
                                if (INDEX_ORDER == adapter.getToolModelList().size()) {
                                    INDEX_ORDER = 0;
                                    searchInfoData();
                                } else {
                                    searchOrderData(adapter.getToolModelList().get(INDEX_ORDER));
                                }
                            }
                            //?????????????????????
                            else {
                                showErrorMessage("????????????" + MKOrdNO + "????????????\n" +
                                        "???????????????" + orderApiUnitsOrder.getResult().get(0).getErrMessage());
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            showErrorMessage("?????????????????????????????????????????????");
                        }
                        break;
                    case LoadingFailure:
                        //???????????????????????????????????????
                        mainViewModel.getSourceTableOrder();
//                        showErrorMessage("????????????" + MKOrdNO + "????????????????????????????????????");
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    //??????API???????????????(?????????)
    private void searchInfoData() {
        mainViewModel.searchInfo().observe(getViewLifecycleOwner(), new Observer<ApiResource<MainApiUnits>>() {
            @Override
            public void onChanged(ApiResource<MainApiUnits> response) {
                switch (response.getTag()) {
                    case LoadingSuccess:
                        MainApiUnits mainApiUnits = response.body();
                        //??????????????????
                        if (mainApiUnits.getMessage().equals("")) {
                            //????????????Table
                            mainViewModel.insertSourceTable(mainApiUnits.getData().getQuery1());
                        }
                        //?????????????????????
                        else {
                            showErrorMessage("??????????????????\n" +
                                    "API???????????????" + mainApiUnits.getMessage());
                        }
                        break;
                    case LoadingFailure:
                        Log.v("LINS","LoadingFailure");
                        //???????????????????????????????????????
                        mainViewModel.getSourceTableOrder();
                        showErrorMessage(TAG.LoadingFailure.getName());
                        break;
                }
            }
        });
    }

    //RecyclerView??????
    private void setRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CreateAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        setItemTouchHelper();
    }

    //RecyclerView????????????
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
            //SQL??????
            mainViewModel.getSourceTableOrder();
        }
    }

}