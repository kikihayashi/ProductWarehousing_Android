package com.woody.productwarehousing.viewmodel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.woody.productwarehousing.bean.MainApiUnits;
import com.woody.productwarehousing.bean.MainApiUnits.OrderQuery1Units;
import com.woody.productwarehousing.bean.OrderApiUnits;
import com.woody.productwarehousing.bean.PrintBean;
import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.constant.TYPE;
import com.woody.productwarehousing.model.repository.MainRepository;
import com.woody.productwarehousing.model.retrofit.ApiResource;
import com.woody.productwarehousing.utils.IoTool;
import com.woody.productwarehousing.utils.SingleLiveEvent;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel extends AndroidViewModel {

    private MainRepository mainRepository;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    public MainViewModel(@NonNull Application application) {
        super(application);
        Interceptor interceptor = createInterceptor(application);
        mainRepository = MainRepository.getInstance(interceptor);
    }

    private Interceptor createInterceptor(Application application) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // 獲取本地存儲的 Cookie
                SharedPreferences preferences = application.getSharedPreferences("auth", MODE_PRIVATE);
                String cookie = "JSESSIONID=" + preferences.getString("JSESSIONID", "");

                // 獲取原始請求
                Request originalRequest = chain.request();

                // 構建新請求
                Request newRequest = originalRequest.newBuilder()
                        .header("Cookie",  cookie)  // 加入 Cookie 到標頭中
                        .build();

                // 發送新請求
                return chain.proceed(newRequest);
            }
        };
        return interceptor;
    }

    /**
     * 觀察各種訊息變化
     */
    public SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentTag) {
        return mainRepository.getInfoMessage(fragmentTag);
    }

    public MutableLiveData<List<PrintBean>> getPrintData() {
        return mainRepository.getPrintData();
    }

    public MutableLiveData<ArrayList<SourceBean>> getBarcodeData() {
        return mainRepository.getBarcodeData();
    }

    public MutableLiveData<ArrayList<String>> getOrderData() {
        return mainRepository.getOrderData();
    }

    public MutableLiveData<ArrayList<SourceBean>> getUploadData() {
        return mainRepository.getUploadData();
    }

    public MutableLiveData<Integer> getStaffData() {
        return mainRepository.getStaffData();
    }

    public MutableLiveData<ArrayList<SourceBean>> getUploadDataCollected() {
        return mainRepository.getUploadDataCollected();
    }

    public MutableLiveData<ArrayList<SourceBean>> getReprintData() {
        return mainRepository.getReprintData();
    }

    public MutableLiveData<HashMap<String, List<SourceBean>>> getUploadFinalData() {
        return mainRepository.getUploadFinalData();
    }

    public MutableLiveData<List<String>> getOrderFinalData() {
        return mainRepository.getOrderFinalData();
    }

    /**
     * API相關資料存取
     */
    public LiveData<ApiResource<MainApiUnits>> uploadDataBase(File DB_FILE) {
        String PDA_ID = TAG.PDAID.getName();
        String DATE = simpleDateFormat2.format(new Date());
        return mainRepository.uploadDataBase(PDA_ID, DATE, DB_FILE);
    }

    public LiveData<ApiResource<OrderApiUnits>> searchOrder(String MKOrdNO) {
        return mainRepository.searchOrder(MKOrdNO);
    }

    public LiveData<ApiResource<MainApiUnits>> searchInfo() {
        return mainRepository.searchInfo();
    }

    public LiveData<ApiResource<MainApiUnits>> printBarcode(int dataIndex) {
        return mainRepository.printBarcode(dataIndex);
    }

    public LiveData<ApiResource<MainApiUnits>> reprintBarcode(int dataIndex) {
        return mainRepository.reprintBarcode(dataIndex);
    }

    public LiveData<ApiResource<MainApiUnits>> invalidPallet(int dataIndex) {
        return mainRepository.invalidPallet(dataIndex);
    }

    public LiveData<ApiResource<MainApiUnits>> printPallet(int dataIndex) {
        return mainRepository.printPallet(dataIndex);
    }

    public LiveData<ApiResource<MainApiUnits>> reprintPallet(int dataIndex) {
        return mainRepository.reprintPallet(dataIndex);
    }

    public LiveData<ApiResource<MainApiUnits>> uploadData(List<SourceBean> sourceBeanList) {
        return mainRepository.uploadData(sourceBeanList);
    }

    /**
     * SQL相關資料存取
     */
    public void getStaffNumber() {
        mainRepository.getStaffNumber();
    }

    public void deleteOldWorkTable() {
        mainRepository.deleteOldWorkTable();
    }

    public void deleteOldWareInTable() {
        mainRepository.deleteOldWareInTable();
    }

    public void setPrintBeanList(List<PrintBean> printBeanList) {
        mainRepository.setPrintBeanList(printBeanList);
    }

    public void resetAllSourceData() {
        mainRepository.resetAllSourceData();
    }

    public void insertSourceTable(List<OrderQuery1Units> query1List) {
        mainRepository.insertSourceTable(query1List);
    }

    public void getSourceTableOrder() {
        mainRepository.getSourceOrder();
    }

    public void insertWorkTable(int dataIndex, String barcode) {
        mainRepository.insertWorkTable(dataIndex, barcode);
    }

    public void updateWorkTable(TYPE type, int dataIndex, String... data) {
        mainRepository.updateWorkTable(type, dataIndex, data);
    }

    public void getWorkTableData(PAGE page) {
        mainRepository.getWorkTableData(page);
    }

    public void getBarcodeRecord(int dataIndex) {
        mainRepository.getBarcodeRecord(dataIndex);
    }

    public void insertWareInTable(TAG tag, int dataIndex, boolean responseResult, String wareInNO, String errorMessage) {
        mainRepository.insertWareInTable(tag, dataIndex, responseResult, wareInNO, errorMessage);
    }

    /**
     * 一般資料的存取
     */
    //設置列印畫面(主頁)
    public void setSourceBeanList(OrderApiUnits orderApiUnitsOrder) {
        mainRepository.setSourceBeanList(orderApiUnitsOrder);
    }

    //整理補印資料排列
    public ArrayList<SourceBean> sortDataList(ArrayList<SourceBean> dataList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dataList.sort(new Comparator<SourceBean>() {
                @Override
                public int compare(SourceBean sb1, SourceBean sb2) {
                    if (sb1.getBillDate().equals("")) {
                        if (sb2.getBillDate().equals("")) {
                            return 0;
                        } else {
                            return -1;
                        }
                    } else {
                        if (sb2.getBillDate().equals("")) {
                            return 1;
                        } else {
                            try {
                                return (int) TimeUnit.MILLISECONDS.toSeconds(
                                        simpleDateFormat.parse(sb2.getBillDate()).getTime() -
                                                simpleDateFormat.parse(sb1.getBillDate()).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    }
                }
            });
        }

        //找出上一次列印的料號
        SourceBean lastSourceBean = Collections.max(dataList, new Comparator<SourceBean>() {
            @Override
            public int compare(SourceBean sb1, SourceBean sb2) {
                try {
                    long sb1LastTime = getLastTime(sb1);
                    long sb2LastTime = getLastTime(sb2);

                    //如果return < 0，就是要找的最新的列印料號
                    return (int) TimeUnit.MILLISECONDS.toSeconds(sb1LastTime - sb2LastTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        int index = dataList.indexOf(lastSourceBean);
        dataList.remove(index);
        dataList.add(0, lastSourceBean);
        return dataList;
    }

    //取得製令單，最後一次列印的時間
    private long getLastTime(SourceBean sourceBean) throws ParseException {
        if (sourceBean.getBillDate().equals("")) {
            //尚未綁定棧板
            return simpleDateFormat.parse(sourceBean.getCreatedTime()).getTime();
        } else {
            //有綁定棧板
            return simpleDateFormat.parse(sourceBean.getBillDate()).getTime();
        }
    }

    //設置員工人數
    public void setStaffNumber(EditText edit_staff) {
        int staffNumber;
        if (!edit_staff.getText().toString().equals("")) {
            staffNumber = Integer.parseInt(edit_staff.getText().toString());
            if (staffNumber == 0) {
                staffNumber = 1;
            }
        } else {
            staffNumber = 1;
        }
        mainRepository.setStaffNumber(staffNumber);
    }

    //設置已結案製令單號
    public void setExpiredMKOrdNO(String mkOrdNO) {
        mainRepository.setExpiredMKOrdNO(mkOrdNO);
    }

    //獲得已結案製令單號列表
    public List<String> getExpiredMKOrdNO() {
        return mainRepository.getExpiredMKOrdNO();
    }

    //刪除LIMIT_DAY以前，舊的上傳Log檔
    public void deleteOldLogFile() {
        mainRepository.deleteOldLogFile();
    }

    //匯出資料庫
    public File exportDatabase() {
        File DB_FILE = null;
        try {
            //Log檔案處理
            final String LOG_ZIP_NAME = "Log.zip";
            final String LOG_FOLDER_NAME = "Log";
            //客戶使用的DB檔案處理
            final String DB_NAME = TAG.DBName.getName();//資料庫檔名
            final String ZIP_NAME = DB_NAME + ".zip";
            final String FOLDER_NAME = DB_NAME + "DBExport";
            final String DB_NAME_EXTENSION = DB_NAME + ".db3";

            //將Download底下的Log資料夾做成壓縮檔
            IoTool.createZip(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + LOG_FOLDER_NAME,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + LOG_ZIP_NAME);

            //匯出檔案到指定資料夾(Log檔、客戶的DB檔)
            IoTool.exportFile(FOLDER_NAME, LOG_ZIP_NAME);
            IoTool.exportFile(FOLDER_NAME, DB_NAME_EXTENSION);
            IoTool.exportFile(FOLDER_NAME, DB_NAME_EXTENSION + "-shm");
            IoTool.exportFile(FOLDER_NAME, DB_NAME_EXTENSION + "-wal");

            //將上述檔案包成一個壓縮檔
            IoTool.createZip(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + ZIP_NAME);

            DB_FILE = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + ZIP_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DB_FILE;
    }

}
