package com.woody.productwarehousing.model.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.woody.productwarehousing.bean.MainApiUnits;
import com.woody.productwarehousing.bean.MainApiUnits.OrderQuery1Units;
import com.woody.productwarehousing.bean.OrderApiUnits;
import com.woody.productwarehousing.bean.OrderApiUnits.DetailData1;
import com.woody.productwarehousing.bean.PrintBean;
import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.model.dao.Dao;
import com.woody.productwarehousing.model.dao.MainDao;
import com.woody.productwarehousing.model.retrofit.ApiCallService;
import com.woody.productwarehousing.model.retrofit.ApiResource;
import com.woody.productwarehousing.model.retrofit.RetrofitLiveData;
import com.woody.productwarehousing.model.retrofit.RetrofitManager;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.utils.SingleLiveEvent;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.constant.TYPE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MainRepository extends BaseRepository {

    //一般資料存取
    private Set<String> prodIdSet = new HashSet<>();
    private List<SourceBean> sourceBeanList = new ArrayList<>();
    private List<String> expiredMKOrdNOList = new ArrayList<>();

    //私有的建構式讓別人不能創造，加volatile可以保證都從主記憶體讀取
    private static volatile MainRepository INSTANCE = null;

    private ApiCallService apiCallService;//API
    private Dao.MainDaoInterface mainDaoInterface;

    private MainRepository(Interceptor interceptor) {
        super();
        mainDaoInterface = new MainDao();
        apiCallService = RetrofitManager.getApiCallService(TAG.URL.getName(), TIMEOUT, interceptor);
    }

    public static MainRepository getInstance(Interceptor interceptor) {
        if (INSTANCE == null) {
            synchronized (MainDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainRepository(interceptor);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 觀察各種訊息變化
     */
    public SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentPage) {
        return mainDaoInterface.getInfoMessage(fragmentPage);
    }

    public MutableLiveData<List<PrintBean>> getPrintData() {
        return mainDaoInterface.getPrintData();
    }

    public MutableLiveData<ArrayList<SourceBean>> getBarcodeData() {
        return mainDaoInterface.getBarcodeData();
    }

    public MutableLiveData<ArrayList<String>> getOrderData() {
        return mainDaoInterface.getOrderData();
    }

    public MutableLiveData<Integer> getStaffData() {
        return mainDaoInterface.getStaffData();
    }

    public MutableLiveData<ArrayList<SourceBean>> getUploadData() {
        return mainDaoInterface.getUploadData();
    }

    public MutableLiveData<ArrayList<SourceBean>> getUploadDataCollected() {
        return mainDaoInterface.getUploadDataCollected();
    }

    public MutableLiveData<ArrayList<SourceBean>> getReprintData() {
        return mainDaoInterface.getReprintData();
    }

    public MutableLiveData<HashMap<String, List<SourceBean>>> getUploadFinalData() {
        return mainDaoInterface.getUploadFinalData();
    }

    public MutableLiveData<List<String>> getOrderFinalData() {
        return mainDaoInterface.getOrderFinalData();
    }

    /**
     * API相關資料存取
     */
    //取得單據資料(API)
    public LiveData<ApiResource<OrderApiUnits>> searchOrder(String MKOrdNO) {
        JsonObject object = mainDaoInterface.setOrderJson(MKOrdNO);
        Log.v("LINS", "searchOrder：" + object);
        Call<OrderApiUnits> call = apiCallService.getChiApiUnits(ORDER_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //取得單據詳細資料(API)
    public LiveData<ApiResource<MainApiUnits>> searchInfo() {
        JsonObject object = mainDaoInterface.setInfoJson(prodIdSet);
        Log.v("LINS", "searchInfo：" + object);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(REGAL_INFO_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //上傳入庫單資料
    public LiveData<ApiResource<MainApiUnits>> uploadData(List<SourceBean> sourceBeanList) {
        JsonObject object = mainDaoInterface.setUploadJson(sourceBeanList);
        Log.v("LINS", "uploadData：" + object);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(REGAL_UPLOAD_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //上傳資料庫檔案
    public LiveData<ApiResource<MainApiUnits>> uploadDataBase(String PAD_ID, String DATE, File DB_FILE) {
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), PAD_ID);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), DATE);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), DB_FILE);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", DB_FILE.getName(), requestFile);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(REGAL_DATABASE_URL, id, date, filePart);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //列印箱數條碼(API)
    public LiveData<ApiResource<MainApiUnits>> printBarcode(int dataIndex) {
        JsonObject object = mainDaoInterface.setBarcodeJson(dataIndex, PAGE.PrintFragment);
        Log.v("LINS", "printBarcode：" + object);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(BARCODE_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //補印箱數條碼(API)
    public LiveData<ApiResource<MainApiUnits>> reprintBarcode(int dataIndex) {
        JsonObject object = mainDaoInterface.setBarcodeJson(dataIndex, PAGE.ReprintFragment);
        Log.v("LINS", "reprintBarcode：" + object);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(RE_BARCODE_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //列印棧板條碼(API)
    public LiveData<ApiResource<MainApiUnits>> printPallet(int dataIndex) {
        JsonObject object = mainDaoInterface.setPalletJson(dataIndex, PAGE.PrintFragment);
        Log.v("LINS", "printPallet：" + object);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(PALLET_BIND_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //補印棧板條碼(API)
    public LiveData<ApiResource<MainApiUnits>> reprintPallet(int dataIndex) {
        JsonObject object = mainDaoInterface.setPalletJson(dataIndex, PAGE.ReprintFragment);
        Log.v("LINS", "reprintPallet：" + object);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(RE_PALLET_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    //作廢棧板條碼(API)
    public LiveData<ApiResource<MainApiUnits>> invalidPallet(int dataIndex) {
        JsonObject object = mainDaoInterface.setInvalidJson(dataIndex);
        Log.v("LINS", "invalidPallet：" + object);
        Call<MainApiUnits> call = apiCallService.getRegalUnits(PALLET_INVALID_URL, object);
        return new RetrofitLiveData(TAG.Loading, call);
    }

    /**
     * SQL相關資料存取
     */
    public void getStaffNumber() {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "getStaffNumber：" + Thread.currentThread().getName());
                mainDaoInterface.getStaffNumber();
            }
        });
    }

    public void deleteOldWorkTable() {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "deleteOldWorkTable：" + Thread.currentThread().getName());
                mainDaoInterface.deleteOldWorkTable();
            }
        });
    }

    public void deleteOldWareInTable() {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "deleteOldWareInTable：" + Thread.currentThread().getName());
                mainDaoInterface.deleteOldWareInTable();
            }
        });
    }

    public void resetAllSourceData() {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "resetAllSourceData：" + Thread.currentThread().getName());
                expiredMKOrdNOList.clear();
                prodIdSet.clear();
                sourceBeanList.clear();
                mainDaoInterface.resetSourceTable();
            }
        });
    }

    public void insertSourceTable(List<OrderQuery1Units> query1List) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "insertSourceTable：" + Thread.currentThread().getName());
                updateSourceBeanList(query1List);
                mainDaoInterface.insertSourceTable(sourceBeanList);
            }
        });
    }

    public void getSourceOrder() {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "getSourceOrder：" + Thread.currentThread().getName());
                mainDaoInterface.getSourceTableOrder();
            }
        });
    }

    public void setPrintBeanList(List<PrintBean> printBeanList) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "setPrintBeanList：" + Thread.currentThread().getName());
                mainDaoInterface.setPrintBeanList(printBeanList);
            }
        });
    }

    public void insertWorkTable(int dataIndex, String barcode) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "insertWorkTable：" + Thread.currentThread().getName());
                mainDaoInterface.insertWorkTable(dataIndex, barcode);
            }
        });
    }

    public void updateWorkTable(TYPE type, int dataIndex, String... data) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "updateWorkTable：" + Thread.currentThread().getName());
                mainDaoInterface.updateWorkTable(type, dataIndex, data);
            }
        });
    }

    public void getWorkTableData(PAGE page) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "getWorkTableData：" + Thread.currentThread().getName());
                mainDaoInterface.getWorkTableData(page);
            }
        });
    }

    public void getBarcodeRecord(int dataIndex) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "getBarcodeRecord：" + Thread.currentThread().getName());
                mainDaoInterface.getBarcodeRecord(dataIndex);
            }
        });
    }

    public void insertWareInTable(TAG tag, int dataIndex, boolean responseResult, String wareInNO, String errorMessage) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS", "insertWareInTable：" + Thread.currentThread().getName());
                mainDaoInterface.insertWareInTable(tag, dataIndex, responseResult, wareInNO, errorMessage);
            }
        });
    }

    /**
     * 一般資料的存取
     */
    public void setSourceBeanList(OrderApiUnits orderApiUnitsOrder) {
        for (DetailData1 detailData1 : orderApiUnitsOrder.getMasterData().get(0).getDetailData1()) {
            SourceBean sourceBean = new SourceBean();
            sourceBean.setMKOrdNO(orderApiUnitsOrder.getMasterData().get(0).getMKOrdNO());
            sourceBean.setMKOrdDate(orderApiUnitsOrder.getMasterData().get(0).getMKOrdDate());
            sourceBean.setProductId(orderApiUnitsOrder.getMasterData().get(0).getProductID());
            sourceBean.setProductName(orderApiUnitsOrder.getMasterData().get(0).getProductName());
            sourceBean.setSrcNoInQty(orderApiUnitsOrder.getMasterData().get(0).getSrcNoInQty());
            sourceBean.setEstWareInDate(orderApiUnitsOrder.getMasterData().get(0).getEstWareInDate());
            sourceBean.setRowNO(detailData1.getRowNo());
            sourceBean.setProdID(detailData1.getNCTProdID());
            sourceBeanList.add(sourceBean);
            prodIdSet.add(detailData1.getNCTProdID());
        }
    }

    private void updateSourceBeanList(List<OrderQuery1Units> query1List) {
        Map<String, OrderQuery1Units> prodIdMap = new HashMap<>();
        for (OrderQuery1Units q : query1List) {
            prodIdMap.put(q.getProdID(), q);
        }
        for (SourceBean s : sourceBeanList) {
            s.setProdName(prodIdMap.get(s.getProdID()).getProdName());
            s.setDefValidDays(Integer.valueOf(prodIdMap.get(s.getProdID()).getDefValidDays()));
            s.setQJSetWeight(prodIdMap.get(s.getProdID()).getQJSetWeight());
            s.setQJUppLimitWt(prodIdMap.get(s.getProdID()).getQJUppLimitWt());
            s.setQJLowLimitWt(prodIdMap.get(s.getProdID()).getQJLowLimitWt());
        }
    }

    public void setStaffNumber(int staffNumber) {
        mainDaoInterface.setStaffNumber(staffNumber);
    }

    public void setExpiredMKOrdNO(String mkOrdNO) {
        expiredMKOrdNOList.add(mkOrdNO);
    }

    public List<String> getExpiredMKOrdNO() {
        return expiredMKOrdNOList;
    }

    public void deleteOldLogFile() {
        mainDaoInterface.deleteOldLogFile();
    }


}