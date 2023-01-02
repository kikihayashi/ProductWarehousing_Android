package com.woody.productwarehousing.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.woody.productwarehousing.R;
import com.woody.productwarehousing.adapter.DetailAdapter;
import com.woody.productwarehousing.adapter.DetailDialogAdapter;
import com.woody.productwarehousing.bean.PrintBean;
import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.utils.DialogManager;
import com.woody.productwarehousing.viewmodel.DetailViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ImageView img_up_down;
//    private EditText edit_mKOrdNO;
    private TextView productName, prodName, manufactureDate, expiryDate, validTime, prodID;
    private Button selectProduct, selectDate, save, scrollUpDown;
    private BottomSheetBehavior bottomSheetBehavior;
    private PrintBean printBean;
    private SourceBean sourceBean;
    private RecyclerView recyclerView;
    private DetailAdapter adapter;
    private DetailViewModel detailViewModel;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_button);
        //不自動彈出鍵盤
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        toolbar = findViewById(R.id.toolbar);
//        edit_mKOrdNO = findViewById(R.id.edit_mKOrdNO);
//        prodID = findViewById(R.id.prodID);
        productName = findViewById(R.id.name);
        prodName = findViewById(R.id.partName);
        manufactureDate = findViewById(R.id.mfd);
        expiryDate = findViewById(R.id.exp);
        validTime = findViewById(R.id.validTime);
        selectProduct = findViewById(R.id.selectProduct);
        selectDate = findViewById(R.id.selectDate);
        save = findViewById(R.id.save);
        scrollUpDown = findViewById(R.id.scrollUpDown);
        img_up_down = findViewById(R.id.img_up_down);
        recyclerView = findViewById(R.id.recyclerView);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));

        //toolbar設置
        setToolbar(toolbar, "詳情");
//        edit_mKOrdNO.setOnKeyListener(this);
//        edit_mKOrdNO.requestFocus();
        selectProduct.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        save.setOnClickListener(this);
        scrollUpDown.setOnClickListener(this);

        //監聽bottom sheet狀態
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                switch (newState) {
                    //收回指令(看不到最底下訂單資料)
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        img_up_down.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        break;
                    //展開指令(可以看到最底下訂單資料)
                    case BottomSheetBehavior.STATE_EXPANDED:
                        img_up_down.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        break;
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        try {
            printBean = (PrintBean) getIntent().getSerializableExtra("PrintBean");
            sourceBean = printBean.getSourceBean();
//            edit_mKOrdNO.setText(printBean.getSourceBean().getMKOrdNO());
//            edit_mKOrdNO.selectAll();
//            prodID.setText(sourceBean.getProdID());
            productName.setText(printBean.getSourceBean().getProductName());
            prodName.setText(printBean.getSourceBean().getProdName());
            manufactureDate.setText(printBean.getManufactureDate());
            expiryDate.setText(printBean.getExpiryDate());
            validTime.setText(String.valueOf(printBean.getSourceBean().getDefValidDays()));

            selectDate.setVisibility(printBean.isSaved() ? View.VISIBLE : View.INVISIBLE);
            save.setVisibility(printBean.isSaved() ? View.VISIBLE : View.INVISIBLE);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        setRecyclerView();
        setViewModel();
    }

    //ViewModel設置
    private void setViewModel() {
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        detailViewModel.getSourceData();

        detailViewModel.getSourceDataList().observe(this, new Observer<ArrayList<SourceBean>>() {
            @Override
            public void onChanged(ArrayList<SourceBean> sourceDataList) {
                if (sourceDataList.size() > 0) {
                    adapter.setToolModelList(sourceDataList);
                }
            }
        });

        detailViewModel.getInfoDataList().observe(this, new Observer<ArrayList<SourceBean>>() {
            @Override
            public void onChanged(ArrayList<SourceBean> infoDataList) {
                if (infoDataList.size() > 0) {
                    if (infoDataList.size() == 1) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        sourceBean = infoDataList.get(0);
                        refreshView();
                    } else {
//                        Log.v("LINS","detailViewModel.getInfoDataList()："+ printBean.getIndex());
                        //獲取detail_dialog螢幕寬高
                        Rect displayRectangle = new Rect();
                        Window window = DetailActivity.this.getWindow();
                        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                        View view = View.inflate(DetailActivity.this, R.layout.dialog_detail_button, null);

                        //AlertDialog設置
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                        builder.setView(view);
                        builder.setCancelable(false);
                        alertDialog = builder.show();
                        alertDialog.getWindow().setLayout((int)((displayRectangle.width() * 0.9f)), 1500);

                        //detail_dialog元件設置
                        TextView detailDialog_tv_title = view.findViewById(R.id.title);
//                        EditText detailDialog_edit_prodID = view.findViewById(R.id.edit_prodID);
                        TextView detailDialog_tv_close = view.findViewById(R.id.close);
                        RecyclerView detailDialog_rv = view.findViewById(R.id.recyclerview);

                        detailDialog_tv_title.setText("產品名稱選擇");
//                        detailDialog_edit_prodID.requestFocus();
//                        detailDialog_edit_prodID.setOnKeyListener(new View.OnKeyListener() {
//                            @Override
//                            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                                if (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_UP == keyEvent.getAction()) {
//                                    hideKeyboard();
//                                    detailDialog_edit_prodID.selectAll();
//                                    if (detailDialog_edit_prodID.getText().toString().trim().equals("")) {
//                                        showErrorMessage("料號不可為空！");
//                                        return true;
//                                    }
//                                    int dataIndex = detailViewModel.checkIfProdIdValid(infoDataList, detailDialog_edit_prodID.getText().toString().trim());
//                                    if (dataIndex >= 0) {
//                                        alertDialog.dismiss();
//                                        sourceBean = infoDataList.get(dataIndex);
//                                        refreshView();
//                                    } else {
//                                        showErrorMessage("查無此料號！");
//                                        return true;
//                                    }
//                                }
//                                return false;
//                            }
//                        });
                        detailDialog_tv_close.setOnClickListener(viewDialog -> alertDialog.dismiss());
                        detailDialog_rv.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        detailDialog_rv.setAdapter(new DetailDialogAdapter(infoDataList, new DetailDialogAdapter.RecyclerViewItemClickListener() {
                            @Override
                            public void onItemClicked(int dataIndex) {
                                alertDialog.dismiss();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                sourceBean = infoDataList.get(dataIndex);
                                refreshView();
                            }
                        }));
                    }
                }
            }
        });

        detailViewModel.getInfoMessage(PAGE.DetailActivity).observe(this, new Observer<TAG>() {
            @Override
            public void onChanged(TAG tag) {
                switch (tag){
                    case LoadingSuccess:
                        infoSound();
                        break;

                    case LoadingFailure:
                        showErrorMessage(tag.getName());
                        break;
                }
            }
        });
    }

    //RecyclerView設置
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new DetailAdapter(new ArrayList<>(), new DetailAdapter.RecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(int dataIndex) {
                infoSound();
                detailViewModel.getInfoData(detailViewModel.getSourceDataList().getValue().get(dataIndex));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    //刷新頁面
    private void refreshView() {
        try {
            selectDate.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            productName.setText(sourceBean.getProductName());
//            prodID.setText(sourceBean.getProdID());
            prodName.setText(sourceBean.getProdName());
            validTime.setText(String.valueOf(sourceBean.getDefValidDays()));
            String today = simpleDateFormat.format(new Date());
            manufactureDate.setText(today);
            printBean.setManufactureDate(today);

            String finalDate = detailViewModel.getValidDate(today, sourceBean.getDefValidDays());
            expiryDate.setText(finalDate);
            printBean.setExpiryDate(finalDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //選擇製造日期
    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(DetailActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String day2 = String.format("%02d", day);
                String month2 = String.format("%02d", month + 1);
                String editDay = year + "/" + month2 + "/" + day2;

                try {
                    //驗證製造日期是否有符合條件(15天以前~今天)
                    if (detailViewModel.checkIfDateValid(editDay)) {
                        manufactureDate.setText(editDay);
                        printBean.setManufactureDate(editDay);

                        //得出有效日期
                        int validTime = Integer.valueOf(DetailActivity.this.validTime.getText().toString());
                        String finalDate = detailViewModel.getValidDate(editDay, validTime);
                        expiryDate.setText(finalDate);
                        printBean.setExpiryDate(finalDate);
                    } else {
                        showErrorMessage("製造日期只能選15天以前~今天");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDay).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scrollUpDown:
                switch (bottomSheetBehavior.getState()) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
                break;
            case R.id.selectProduct:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.selectDate:
                selectDate();
                break;

            case R.id.save:
                printBean.setSaved(true);
                printBean.setSourceBean(sourceBean);
                getIntent().putExtra("PrintBean", printBean);
                setResult(DETAIL_SAVE_CODE, getIntent());
                finish();
                break;
        }
    }

    //錯誤訊息
    private void showErrorMessage(String message) {
        dialogManager.setBaseOption(getString(R.string.Error), errorIcon, message);
        dialogManager.setCancelable(false);
        dialogManager.setButtonCommand(null, null, getString(R.string.Enter),
                null, null, new DialogManager.PositiveCommand() {
                    @Override
                    public void positiveExecute() {
                        dialogManager.dismissDialog();
                    }
                });
        showHintDialog(dialogManager.createDialog(), SOUND.ERROR);
    }

    //設置Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_productwarehousing, menu);
        return true;
    }

    //選擇HOME鍵按鈕時的動作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //確保Dialog不會跑出來
    @Override
    protected void onResume() {
        super.onResume();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

//    @Override
//    public boolean onKey(View v, int i, KeyEvent keyEvent) {
//        if (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_UP == keyEvent.getAction()) {
//            hideKeyboard();
//            switch (v.getId()) {
//                case R.id.edit_mKOrdNO:
//                    edit_mKOrdNO.selectAll();
//                    if (edit_mKOrdNO.getText().toString().trim().equals("")) {
//                        showErrorMessage("製令單號不可為空！");
//                        return true;
//                    }
//                    if (edit_mKOrdNO.getText().toString().trim().length()!=11) {
//                        showErrorMessage("製令單號長度需為11");
//                        return true;
//                    }
//                    detailViewModel.getInfoData(edit_mKOrdNO.getText().toString().trim());
//                    break;
//            }
//        }
//        return false;
//    }
}