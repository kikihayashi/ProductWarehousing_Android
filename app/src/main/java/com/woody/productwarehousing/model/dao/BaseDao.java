package com.woody.productwarehousing.model.dao;

import android.database.sqlite.SQLiteDatabase;

import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.sqlite.SQLiteHelper;
import com.woody.productwarehousing.utils.SingleLiveEvent;

import java.text.SimpleDateFormat;

public class BaseDao implements Dao.BaseDaoInterface {

    //訊息LiveData
    protected SingleLiveEvent<TAG> infoMessageLivePrint = new SingleLiveEvent<>();
    protected SingleLiveEvent<TAG> infoMessageLiveCreate = new SingleLiveEvent<>();
    protected SingleLiveEvent<TAG> infoMessageLiveUpload = new SingleLiveEvent<>();
    protected SingleLiveEvent<TAG> infoMessageLiveDetail = new SingleLiveEvent<>();

    //SQLite所需參數
    protected SQLiteDatabase sqLiteDatabase;
    protected final String SOURCE_TABLE = "JGPST";
    protected final String WORK_TABLE = "JGPWT";
    protected final String WARE_IN_TABLE = "CJFIT";

    protected SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    protected SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected String remark = "";//備註

    public BaseDao() {
        this.sqLiteDatabase = SQLiteHelper.getDataBase();
    }

    @Override
    public SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentTag) {
        switch (fragmentTag) {
            case CreateFragment:
                return infoMessageLiveCreate;
            case PrintFragment:
                return infoMessageLivePrint;
            case UploadFragment:
                return infoMessageLiveUpload;
            case DetailActivity:
                return infoMessageLiveDetail;
            default:
                throw new IllegalArgumentException();
        }
    }
}
