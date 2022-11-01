package com.woody.productwarehousing.model.dao;

import androidx.lifecycle.MutableLiveData;
import com.google.gson.JsonObject;
import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.bean.PrintBean;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.utils.SingleLiveEvent;
import com.woody.productwarehousing.constant.TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface Dao {

    interface BaseDaoInterface {

        SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentTag);
    }

    interface MainDaoInterface extends BaseDaoInterface {

        JsonObject setOrderJson(String MKOrdNO);

        JsonObject setInfoJson(Set<String> prodIdSet);

        JsonObject setUploadJson(List<SourceBean> sourceBeanList);

        JsonObject setBarcodeJson(int dataIndex, PAGE page);

        JsonObject setPalletJson(int dataIndex, PAGE page);

        JsonObject setInvalidJson(int dataIndex);

        MutableLiveData<List<PrintBean>> getPrintData();

        MutableLiveData<ArrayList<SourceBean>> getBarcodeData();

        MutableLiveData<Integer> getStaffData();

        MutableLiveData<ArrayList<String>> getOrderData();

        MutableLiveData<ArrayList<SourceBean>> getUploadData();

        MutableLiveData<ArrayList<SourceBean>> getUploadDataCollected();

        MutableLiveData<ArrayList<SourceBean>> getReprintData();

        MutableLiveData<List<String>> getOrderFinalData();

        MutableLiveData<HashMap<String, List<SourceBean>>> getUploadFinalData();

        void getStaffNumber();

        void deleteOldWorkTable();

        void deleteOldWareInTable();

        void deleteOldLogFile();

        void resetSourceTable();

        void insertSourceTable(List<SourceBean> sourceBeanList);

        void getSourceTableOrder();

        void setPrintBeanList(List<PrintBean> printBeanList);

        void insertWorkTable(int dataIndex, String serial);

        void updateWorkTable(TYPE type, int dataIndex, String... data);

        void getWorkTableData(PAGE page);

        void getBarcodeRecord(int dataIndex);

        void setStaffNumber(int staffNumber);

        void insertWareInTable(TAG tag, int dataIndex, boolean responseResult, String wareInNO, String errorMessage);
    }

    interface DetailDaoInterface extends BaseDaoInterface {

        MutableLiveData<ArrayList<SourceBean>> getSourceDataList();

        MutableLiveData<ArrayList<SourceBean>> getInfoDataList();

        void getSourceData();

        void getInfoData(SourceBean sourceBean);

        void getInfoData(String mKOrdNO);

    }
}
