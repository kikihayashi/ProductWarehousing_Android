package com.woody.productwarehousing.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.woody.productwarehousing.R;
import com.woody.productwarehousing.bean.SourceBean;

import java.util.ArrayList;
import java.util.List;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.MyViewHolder> {

    private static int EMPTY_LAYOUT = R.layout.empty_view;
    private static int ITEM_LAYOUT = R.layout.item_upload;

    private List<SourceBean> toolModelList;

    public UploadAdapter(List<SourceBean> list) {
        toolModelList = list;
    }

    //存之後需要用到的View
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView prodName, prodID, qty, status, batchID, mKOrdNO;
        private CardView cardView;

        public MyViewHolder(View v)//定義tool_dialog.xml中RecyclerView上的view(tool_item.xml)
        {
            super(v);
            mKOrdNO = v.findViewById(R.id.mKOrdNO);
            prodName = v.findViewById(R.id.name);
            prodID = v.findViewById(R.id.productNo);
            qty = v.findViewById(R.id.qty);
            status = v.findViewById(R.id.status);
            batchID = v.findViewById(R.id.batchID);
            cardView = v.findViewById(R.id.cardView);
        }

        public void setData(SourceBean item)//將toolModel的資料設置到tool_item.xml裡的TextView上
        {
            mKOrdNO.setText("單號：" + item.getMKOrdNO());
            prodName.setText(item.getProdName());
            prodID.setText(item.getProdID());
            qty.setText(String.valueOf(item.getQuantity()));
            status.setText(item.isCollected() ? "未上傳" : "未綁定");
            status.setTextColor(Color.parseColor(item.isCollected() ? "#E20000" : "#053B59"));
            batchID.setText(item.getBatchID());
            cardView.setBackgroundResource(R.drawable.shape_border);//將View的背景優化(有邊框、圓角)
        }
    }

    @NonNull
    @Override
    //產生介面
    public UploadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //將viewType存入layout，當作布局介面
        int layout = viewType;

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        UploadAdapter.MyViewHolder tool_item_holder = new UploadAdapter.MyViewHolder(v);

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
    public void onBindViewHolder(@NonNull UploadAdapter.MyViewHolder holder, final int position) {
        if (toolModelList != null && toolModelList.size() != 0) {
            holder.setData(toolModelList.get(position));//將選單名稱(資料)設置好
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
}