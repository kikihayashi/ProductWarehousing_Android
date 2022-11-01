package com.woody.productwarehousing.model.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.model.dao.Dao;
import com.woody.productwarehousing.model.dao.DetailDao;
import com.woody.productwarehousing.model.dao.MainDao;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.utils.SingleLiveEvent;
import com.woody.productwarehousing.constant.TAG;

import java.util.ArrayList;

public class DetailRepository extends BaseRepository {

    //私有的建構式讓別人不能創造，加volatile可以保證都從主記憶體讀取
    private static volatile DetailRepository INSTANCE = null;

    private Dao.DetailDaoInterface detailDaoInterface;

    private DetailRepository() {
        super();
        detailDaoInterface = new DetailDao();
    }

    public static DetailRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (MainDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DetailRepository();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 觀察各種訊息變化
     */
    public SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentTag) {
        return detailDaoInterface.getInfoMessage(fragmentTag);
    }

    /**
     * SQL相關資料存取
     */
    public MutableLiveData<ArrayList<SourceBean>> getSourceDataList() {
        return detailDaoInterface.getSourceDataList();
    }

    public MutableLiveData<ArrayList<SourceBean>> getInfoDataList() {
        return detailDaoInterface.getInfoDataList();
    }

    public void getSourceData() {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS","detail_getSourceData：" + Thread.currentThread().getName());
                detailDaoInterface.getSourceData();
            }
        });
    }

    public void getInfoData(SourceBean sourceBean) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS","detail_getInfoData：" + Thread.currentThread().getName());
                detailDaoInterface.getInfoData(sourceBean);
            }
        });
    }

    public void getInfoData(String mKOrdNO) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("LINS","detail_getInfoData：" + Thread.currentThread().getName());
                detailDaoInterface.getInfoData(mKOrdNO);
            }
        });
    }


}
