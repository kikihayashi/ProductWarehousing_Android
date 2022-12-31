package com.woody.productwarehousing.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.woody.productwarehousing.R;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.utils.DialogManager;
import com.woody.productwarehousing.view.BaseActivity;
import com.woody.productwarehousing.view.Fragment.CreateFragment;
import com.woody.productwarehousing.view.Fragment.PrintFragment;
import com.woody.productwarehousing.view.Fragment.ReprintFragment;
import com.woody.productwarehousing.view.Fragment.UploadFragment;
import com.woody.productwarehousing.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.TreeSet;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ConstraintLayout staff_cl;
    private ArrayList<Fragment> fragmentList;
    private TreeSet<Integer> alreadyUseFragment;
    private AlertDialog alertDialog;
    private CardView cardView, staff_cv;
    private TextView tv_progressBar, staff_tv;
    private ProgressBar progressBar;
    private LinearLayout progressBarView;
    //ProgressBar的圈圈大小設置
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
    public MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
//        if (ContextCompat.checkSelfPermission
//                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        } else {
//            init();
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            init();
//        } else {
//            finish();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST_CODE) {
            if (resultCode == DETAIL_SAVE_CODE) {
                fragmentList.get(0).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void init() {
        //findViewById
        toolbar = findViewById(R.id.toolbar);
        cardView = findViewById(R.id.cardView);
        staff_cv = findViewById(R.id.staff_cv);
        staff_tv = findViewById(R.id.staff_tv);
        staff_cl = findViewById(R.id.staff_cl);
        progressBarView = findViewById(R.id.view_progressBar);
        tv_progressBar = progressBarView.findViewById(R.id.tv_progressBar);
        progressBar = progressBarView.findViewById(R.id.progressBar);


        //toolbar設置
        setToolbar(toolbar, "條碼列印");
        staff_cv.setOnClickListener(this);
        staff_tv.setOnClickListener(this);

        //將需要的fragment新增到List裡
        fragmentList = new ArrayList<>();
        fragmentList.add(new PrintFragment());
        fragmentList.add(new CreateFragment());
        fragmentList.add(new UploadFragment());
        fragmentList.add(new ReprintFragment());

        //用來記錄有哪些fragment有初始化了
        alreadyUseFragment = new TreeSet<>();
        alreadyUseFragment.add(0);

        //先初始化第一頁的fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, fragmentList.get(0))
                .show(fragmentList.get(0))
                .commit();

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setBackground(new ColorDrawable(Color.parseColor("#DEE5E5")));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //顯示轉場效果
                showProgressBar();
                PAGE page = null;
                switch (item.getItemId()) {
                    case R.id.navigation_print:
                        page = PAGE.PrintFragment;
                        break;
                    case R.id.navigation_create:
                        page = PAGE.CreateFragment;
                        break;
                    case R.id.navigation_upload:
                        page = PAGE.UploadFragment;
                        break;
                    case R.id.navigation_reprint:
                        page = PAGE.ReprintFragment;
                        break;
                }
                staff_cl.setBackgroundColor(page.getColor());
                toolbar.setBackgroundColor(page.getColor());
                toolbar.setTitle(page.getName());

                //處理fragment轉場時，哪些要隱藏，哪些要顯示
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                int index = page.ordinal();
                for (int i = 0; i < fragmentList.size(); i++) {
                    if (i == index) {
                        if (!alreadyUseFragment.contains(index)) {
                            alreadyUseFragment.add(index);
                            fragmentTransaction.add(R.id.frame, fragmentList.get(i));
                        }
                        fragmentTransaction.show(fragmentList.get(i));
                    } else {
                        fragmentTransaction.hide(fragmentList.get(i));
                    }
                }
                //commit的CallBack(commit結束後才會執行hideProgressBar)
                fragmentTransaction.runOnCommit(() -> hideProgressBar());
                //執行fragmentTransaction的指令
                fragmentTransaction.commit();
                return true;
            }
        });

        setViewModel();
    }

    private void setViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getStaffData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer staffNumber) {
                staff_tv.setText(String.valueOf(staffNumber));
            }
        });

        //刪除LIMIT_DAY以前，已上傳、作廢的舊資料
        mainViewModel.deleteOldWorkTable();

        //刪除LIMIT_DAY以前，上傳歷程的舊資料
        mainViewModel.deleteOldWareInTable();

        //刪除LIMIT_DAY以前，上傳Log檔的舊資料
        mainViewModel.deleteOldLogFile();

        //取得員工人數
        mainViewModel.getStaffNumber();

//        mainViewModel.insertSudoData();
    }

    //顯示載入動畫
    private void showProgressBar() {
        tv_progressBar.setText("");
        progressBar.setLayoutParams(params);
        progressBarView.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.INVISIBLE);
    }

    //隱藏載入動畫
    private void hideProgressBar() {
        progressBarView.setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.VISIBLE);
    }

    //設置上傳、紀錄按鈕
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_productwarehousing, menu);
        return true;
    }

    //選擇上傳、紀錄、HOME鍵按鈕時的動作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isFastDoubleClick()) {
            return false;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        dialogManager.setBaseOption(getString(R.string.Logout), BaseActivity.emptyIcon, "確定要登出?");
        dialogManager.setCancelable(true);
        dialogManager.setButtonCommand(null, getString(R.string.Logout), getString(R.string.Cancel),
                null, new DialogManager.NeutralCommand() {
                    @Override
                    public void neutralExecute() {
                        setResult(MAIN_LOGOUT_CODE, getIntent());
                        finish();
                    }
                }, null);
        showHintDialog(dialogManager.createDialog(), SOUND.INFO);
    }

    @Override
    public void onClick(View view) {
        if (isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.staff_cv:
                infoSound();
                showStaffDialog();
                break;
        }
    }

    private void showStaffDialog() {
        //獲取detail_dialog螢幕寬高
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View view = View.inflate(this, R.layout.dialog_staff, null);

        //AlertDialog設置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.show();
        alertDialog.getWindow().setLayout((int) ((displayRectangle.width() * 0.9f)), (int) ((displayRectangle.height() * 0.4f)));

        //dialog元件設置
        EditText dialog_edit_staff = view.findViewById(R.id.staff_edit);
        TextView dialog_tv_enter = view.findViewById(R.id.enter);
        dialog_edit_staff.setText(staff_tv.getText().toString());

        int maxLength = 3;
        dialog_edit_staff.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        dialog_edit_staff.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog_edit_staff.setOnKeyListener((view1, actionId, keyEvent) -> {
            if (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_UP == keyEvent.getAction()) {
                hideKeyboard();
                alertDialog.dismiss();
                mainViewModel.setStaffNumber(dialog_edit_staff);
            }
            return false;
        });

        dialog_tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                alertDialog.dismiss();
                mainViewModel.setStaffNumber(dialog_edit_staff);
            }
        });
    }
}
