package com.woody.productwarehousing.adapter;

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
import com.woody.productwarehousing.bean.PrintBean;

import java.util.List;

public class PrintAdapter extends RecyclerView.Adapter<PrintAdapter.MyViewHolder> {

    private static int ITEM_LAYOUT;
    private PrintAdapter.RecyclerViewItemClickListener recyclerViewItemClickListener;
    private static boolean hasData = false;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;

    private List<PrintBean> toolModelList;

    public PrintAdapter(List<PrintBean> list, int itemLayout, RecyclerViewItemClickListener listener) {
        ITEM_LAYOUT = itemLayout;
        toolModelList = list;
        recyclerViewItemClickListener = listener;
    }

    //存之後需要用到的View
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView partName, printNumber, collectNumber;
        private CardView cardView;
        private Button info, print, printPallet;

        public MyViewHolder(View v)//定義tool_dialog.xml中RecyclerView上的view(tool_item.xml)
        {
            super(v);
            partName = v.findViewById(R.id.partName);
            printNumber = v.findViewById(R.id.printNumber);
            collectNumber = v.findViewById(R.id.collectNumber);
            info = v.findViewById(R.id.info);
            print = v.findViewById(R.id.print);
            printPallet = v.findViewById(R.id.printPallet);
            cardView = v.findViewById(R.id.cardView);
        }

        public void setData(PrintBean item)//將toolModel的資料設置到tool_item.xml裡的TextView上
        {
            if (hasData) {
                if (item.isSaved()) {
                    print.setVisibility(View.VISIBLE);
                    printPallet.setVisibility((item.getCollectNumber() > 0) ? View.VISIBLE : View.INVISIBLE);
                } else {
                    print.setVisibility(View.INVISIBLE);
                    printPallet.setVisibility(View.INVISIBLE);
                }
                partName.setText(item.getSourceBean().getProdName().equals("") ?
                        "請點選詳情選取" : item.getSourceBean().getProdName() + "\n(" + item.getManufactureDate() + ")");
            } else {
                print.setVisibility(View.INVISIBLE);
                printPallet.setVisibility(View.INVISIBLE);
                partName.setText("請到產品建立\n抓取資料");
            }
            info.setVisibility(hasData ? View.VISIBLE : View.INVISIBLE);
            printNumber.setText(hasData ? String.valueOf(item.getPrintNumber()) : "0");
            collectNumber.setText(hasData ? String.valueOf(item.getCollectNumber()) : "0");
            cardView.setBackgroundResource(R.drawable.shape_border);//將View的背景優化(有邊框、圓角)
        }
    }

    @NonNull
    @Override
    //產生介面
    public PrintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
        int viewType = ITEM_LAYOUT;
        return viewType;
    }

    @Override
    //設置ViewHolder裡的View
    public void onBindViewHolder(@NonNull PrintAdapter.MyViewHolder holder, final int position) {
        if (toolModelList != null && toolModelList.size() != 0) {
            holder.setData(toolModelList.get(position));//將選單名稱(資料)設置好
            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    recyclerViewItemClickListener.onInfoClicked(position);
                }
            });

            holder.print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    recyclerViewItemClickListener.onPrintClicked(position);
                }
            });

            holder.printPallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    recyclerViewItemClickListener.onPrintPalletClicked(position);
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

    public void setToolModelList(List<PrintBean> list) {
        this.toolModelList = list;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // 刷新操作
                notifyDataSetChanged();
            }
        });
    }

    public void setInfoVisibility(boolean canVisibility) {
        this.hasData = canVisibility;
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
        void onInfoClicked(int dataIndex);

        void onPrintClicked(int dataIndex);

        void onPrintPalletClicked(int dataIndex);
    }
}