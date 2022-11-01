package com.woody.productwarehousing.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.bean.SourceBean;

import java.util.ArrayList;
import java.util.List;

public class ReprintAdapter extends RecyclerView.Adapter<ReprintAdapter.MyViewHolder> {

    private static int EMPTY_LAYOUT = R.layout.empty_view;
    private static int ITEM_LAYOUT = R.layout.item_reprint;

    private ReprintAdapter.RecyclerViewItemClickListener recyclerViewItemClickListener;
    private List<SourceBean> toolModelList;

    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;

    public ReprintAdapter(List<SourceBean> list, ReprintAdapter.RecyclerViewItemClickListener listener) {
        toolModelList = list;
        recyclerViewItemClickListener = listener;
    }

    //存之後需要用到的View
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView prodName, prodID, pallet, billDate, mKOrdNO;
        private Button reprint, reprintBarcode;
        private CardView cardView;

        public MyViewHolder(View v)//定義tool_dialog.xml中RecyclerView上的view(tool_item.xml)
        {
            super(v);
            mKOrdNO = v.findViewById(R.id.mKOrdNO);
            prodName = v.findViewById(R.id.name);
            prodID = v.findViewById(R.id.prodID);
            pallet = v.findViewById(R.id.pallet);
            billDate = v.findViewById(R.id.billDate);
            reprint = v.findViewById(R.id.reprint_btn);
            reprintBarcode = v.findViewById(R.id.reprint_barcode_btn);
            cardView = v.findViewById(R.id.cardView);
        }

        public void setData(SourceBean item)//將toolModel的資料設置到tool_item.xml裡的TextView上
        {
            mKOrdNO.setText("單號：" + item.getMKOrdNO());
            prodName.setText(item.getProdName());
            prodID.setText(item.getProdID());
            String wareInInfo = " (" + item.getWareID() + "-" + item.getStorageID() + "-" + item.getWareInClass() + ")";
            if (item.getPallet().equals("")) {
                pallet.setText("*尚未綁定棧板*" + wareInInfo);
                pallet.setTextColor(Color.parseColor("#FF7800"));
                reprint.setVisibility(View.GONE);
                billDate.setVisibility(View.GONE);
            } else {
                pallet.setText("棧板：" + item.getPallet() + wareInInfo);
                pallet.setTextColor(Color.parseColor("#01579B"));
                reprint.setVisibility(View.VISIBLE);
                billDate.setVisibility(View.VISIBLE);
                billDate.setText(item.getBillDate());
            }
            cardView.setBackgroundResource(R.drawable.shape_border);//將View的背景優化(有邊框、圓角)
        }

        public void setColor(int position) {
            if (position == 0) {
                cardView.setBackgroundResource(R.drawable.shape_border_focus);//將View的背景優化(有邊框、圓角)
            }
        }
    }

    @NonNull
    @Override
    //產生介面
    public ReprintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //將viewType存入layout，當作布局介面
        int layout = viewType;

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        ReprintAdapter.MyViewHolder tool_item_holder = new ReprintAdapter.MyViewHolder(v);

        return tool_item_holder;
    }

    @Override
    public int getItemViewType(int position) {
        //進行判斷，如果沒資料，設置viewType為空布局，背景使用空白，反之使用正常布局
        int viewType = (toolModelList == null || toolModelList.size() == 0) ? EMPTY_LAYOUT : ITEM_LAYOUT;
        return viewType;
    }

    @Override
    //設置ViewHolder裡的View
    public void onBindViewHolder(@NonNull ReprintAdapter.MyViewHolder holder, final int position) {
        if (toolModelList != null && toolModelList.size() != 0) {
            holder.setData(toolModelList.get(position));//將選單名稱(資料)設置好
            holder.setColor(position);//將選單顏色設置好
            holder.reprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    recyclerViewItemClickListener.onReprintClicked(position);
                }
            });
            holder.reprintBarcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    recyclerViewItemClickListener.onReprintBarcodeClicked(position);
                }
            });
        }
    }

    @Override
    //RecyclerView總共有幾筆(組)資料
    public int getItemCount() {
        //如果沒有資料，設置Item為1，反之正常設置
        return (toolModelList == null || toolModelList.size() == 0) ? 1 : toolModelList.size();
    }

    public void setToolModelList(ArrayList<SourceBean> list) {
        this.toolModelList = list;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // 刷新操作
                notifyDataSetChanged();
            }
        });
    }

    //RecyclerViewItemClickListener(抽象)介面，需在Activity中實作clickOnItem抽象方法
    public interface RecyclerViewItemClickListener {

        void onReprintClicked(int dataIndex);

        void onReprintBarcodeClicked(int dataIndex);
    }
}

