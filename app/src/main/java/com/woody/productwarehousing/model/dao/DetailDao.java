package com.woody.productwarehousing.model.dao;

import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;

import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.sqlite.SQLiteHelper;
import com.woody.productwarehousing.constant.TAG;

import java.util.ArrayList;

public class DetailDao extends BaseDao implements Dao.DetailDaoInterface {

    private MutableLiveData<ArrayList<SourceBean>> sourceDataLive = new MutableLiveData<>();
    private MutableLiveData<ArrayList<SourceBean>> infoDataLive = new MutableLiveData<>();

    public DetailDao() {
        super();
    }

    @Override
    public MutableLiveData<ArrayList<SourceBean>> getSourceDataList() {
        return sourceDataLive;
    }

    public MutableLiveData<ArrayList<SourceBean>> getInfoDataList() {
        return infoDataLive;
    }

    @Override
    public void getSourceData() {
        ArrayList<SourceBean> sourceDataList = new ArrayList<>();
        Cursor cursor = null;
        String command = "SELECT DISTINCT MKOrdNO,ProductId,ProductName FROM " + SOURCE_TABLE;

        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String MKOrdNO = cursor.getString(cursor.getColumnIndexOrThrow("MKOrdNO"));
                    String ProductId = cursor.getString(cursor.getColumnIndexOrThrow("ProductId"));
                    String ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));

                    SourceBean sourceBean = new SourceBean();
                    sourceBean.setMKOrdNO(MKOrdNO);
                    sourceBean.setProductId(ProductId);
                    sourceBean.setProductName(ProductName);
                    sourceDataList.add(sourceBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
            sourceDataLive.postValue(sourceDataList);
        }
    }

    @Override
    public void getInfoData(SourceBean sourceBean) {
        ArrayList<SourceBean> infoDataList = new ArrayList<>();
        Cursor cursor = null;
        String command = "SELECT * FROM " + SOURCE_TABLE + " WHERE " +
                "MKOrdNO='" + sourceBean.getMKOrdNO() + "' AND " +
                "ProductId='" + sourceBean.getProductId() + "'";
        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    String MKOrdNO = cursor.getString(cursor.getColumnIndexOrThrow("MKOrdNO"));
                    int MKOrdDate = cursor.getInt(cursor.getColumnIndexOrThrow("MKOrdDate"));
                    String ProductId = cursor.getString(cursor.getColumnIndexOrThrow("ProductId"));
                    String ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
                    String SrcNoInQty = cursor.getString(cursor.getColumnIndexOrThrow("SrcNoInQty"));
                    int RowNO = cursor.getInt(cursor.getColumnIndexOrThrow("RowNO"));
                    String ProdID = cursor.getString(cursor.getColumnIndexOrThrow("ProdID"));
                    String ProdName = cursor.getString(cursor.getColumnIndexOrThrow("ProdName"));
                    int DefValidDays = cursor.getInt(cursor.getColumnIndexOrThrow("DefValidDays"));
                    int EstWareInDate = cursor.getInt(cursor.getColumnIndexOrThrow("EstWareInDate"));
                    String QJSetWeight = cursor.getString(cursor.getColumnIndexOrThrow("QJSetWeight"));
                    String QJUppLimitWt = cursor.getString(cursor.getColumnIndexOrThrow("QJUppLimitWt"));
                    String QJLowLimitWt = cursor.getString(cursor.getColumnIndexOrThrow("QJLowLimitWt"));

                    SourceBean sourceBeanWithInfo = new SourceBean();
                    sourceBeanWithInfo.setMKOrdNO(MKOrdNO);
                    sourceBeanWithInfo.setMKOrdDate(MKOrdDate);
                    sourceBeanWithInfo.setProductId(ProductId);
                    sourceBeanWithInfo.setProductName(ProductName);
                    sourceBeanWithInfo.setSrcNoInQty(SrcNoInQty);
                    sourceBeanWithInfo.setRowNO(RowNO);
                    sourceBeanWithInfo.setProdID(ProdID);
                    sourceBeanWithInfo.setProdName(ProdName);
                    sourceBeanWithInfo.setDefValidDays(DefValidDays);
                    sourceBeanWithInfo.setEstWareInDate(EstWareInDate);
                    sourceBeanWithInfo.setQJSetWeight(QJSetWeight);
                    sourceBeanWithInfo.setQJUppLimitWt(QJUppLimitWt);
                    sourceBeanWithInfo.setQJLowLimitWt(QJLowLimitWt);
                    infoDataList.add(sourceBeanWithInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
            infoDataLive.postValue(infoDataList);
        }
    }


    @Override
    public void getInfoData(String mKOrdNO) {
        ArrayList<SourceBean> infoDataList = new ArrayList<>();
        Cursor cursor = null;
        String command = "SELECT * FROM " + SOURCE_TABLE + " WHERE " +
                "MKOrdNO='" + mKOrdNO + "'";
        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    String MKOrdNO = cursor.getString(cursor.getColumnIndexOrThrow("MKOrdNO"));
                    int MKOrdDate = cursor.getInt(cursor.getColumnIndexOrThrow("MKOrdDate"));
                    String ProductId = cursor.getString(cursor.getColumnIndexOrThrow("ProductId"));
                    String ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
                    String SrcNoInQty = cursor.getString(cursor.getColumnIndexOrThrow("SrcNoInQty"));
                    int RowNO = cursor.getInt(cursor.getColumnIndexOrThrow("RowNO"));
                    String ProdID = cursor.getString(cursor.getColumnIndexOrThrow("ProdID"));
                    String ProdName = cursor.getString(cursor.getColumnIndexOrThrow("ProdName"));
                    int DefValidDays = cursor.getInt(cursor.getColumnIndexOrThrow("DefValidDays"));
                    int EstWareInDate = cursor.getInt(cursor.getColumnIndexOrThrow("EstWareInDate"));
                    String QJSetWeight = cursor.getString(cursor.getColumnIndexOrThrow("QJSetWeight"));
                    String QJUppLimitWt = cursor.getString(cursor.getColumnIndexOrThrow("QJUppLimitWt"));
                    String QJLowLimitWt = cursor.getString(cursor.getColumnIndexOrThrow("QJLowLimitWt"));

                    SourceBean sourceBeanWithInfo = new SourceBean();
                    sourceBeanWithInfo.setMKOrdNO(MKOrdNO);
                    sourceBeanWithInfo.setMKOrdDate(MKOrdDate);
                    sourceBeanWithInfo.setProductId(ProductId);
                    sourceBeanWithInfo.setProductName(ProductName);
                    sourceBeanWithInfo.setSrcNoInQty(SrcNoInQty);
                    sourceBeanWithInfo.setRowNO(RowNO);
                    sourceBeanWithInfo.setProdID(ProdID);
                    sourceBeanWithInfo.setProdName(ProdName);
                    sourceBeanWithInfo.setDefValidDays(DefValidDays);
                    sourceBeanWithInfo.setEstWareInDate(EstWareInDate);
                    sourceBeanWithInfo.setQJSetWeight(QJSetWeight);
                    sourceBeanWithInfo.setQJUppLimitWt(QJUppLimitWt);
                    sourceBeanWithInfo.setQJLowLimitWt(QJLowLimitWt);
                    infoDataList.add(sourceBeanWithInfo);
                }
                infoMessageLiveDetail.postValue(TAG.LoadingSuccess);
                infoDataLive.postValue(infoDataList);
            } else {
                TAG.LoadingFailure.setName("查無此製令單號！");
                infoMessageLiveDetail.postValue(TAG.LoadingFailure);
            }
        } catch (Exception e) {
            TAG.LoadingFailure.setName("查無此製令單號！");
            infoMessageLiveDetail.postValue(TAG.LoadingFailure);
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    }
}
