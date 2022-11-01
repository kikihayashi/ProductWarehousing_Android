package com.woody.productwarehousing.adapter;

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

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

    private static int EMPTY_LAYOUT = R.layout.empty_view2;
    private static int ITEM_LAYOUT = R.layout.item_detail;

    private DetailAdapter.RecyclerViewItemClickListener recyclerViewItemClickListener;
    private ArrayList<SourceBean> toolModelList;

    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;

    public DetailAdapter(ArrayList<SourceBean> list, RecyclerViewItemClickListener listener) {
        toolModelList = list;
        recyclerViewItemClickListener = listener;
    }

    //存之後需要用到的View
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private CardView cardView;

        public MyViewHolder(View v)//定義tool_dialog.xml中RecyclerView上的view(tool_item.xml)
        {
            super(v);
            productName = v.findViewById(R.id.name);
            cardView = v.findViewById(R.id.cardView);
        }

        public void setData(SourceBean item)//將toolModel的資料設置到tool_item.xml裡的TextView上
        {
            productName.setText(item.getProductName() + "\n (" + item.getMKOrdNO() + ")");
            cardView.setBackgroundResource(R.drawable.shape_border_detail);//將View的背景優化(有邊框、圓角)
        }
    }

    @NonNull
    @Override
    //產生介面
    public DetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //將viewType存入layout，當作布局介面
        int layout = viewType;

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        MyViewHolder tool_item_holder = new MyViewHolder(v);

        return tool_item_holder;
    }

    @Override
    public int getItemViewType(int position) {
        //進行判斷，如果沒資料，設置viewType為空布局，背景使用空白，反之使用正常布局
        int viewType = (toolModelList == null || toolModelList.size() == 0)? EMPTY_LAYOUT : ITEM_LAYOUT;
        return viewType;
    }

    @Override
    //設置ViewHolder裡的View
    public void onBindViewHolder(@NonNull DetailAdapter.MyViewHolder holder, final int position) {
        if (toolModelList != null && toolModelList.size() != 0) {
            holder.setData(toolModelList.get(position));//將選單名稱(資料)設置好
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    recyclerViewItemClickListener.onItemClicked(position);
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
        void onItemClicked(int dataIndex);
    }
}