package com.woody.productwarehousing.model.repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseRepository {
    //設定TimeOut時間
    public static final int TIMEOUT = 30;
    //取得訂單、詳細資料
    public static final String ORDER_URL = "order";
    public static final String REGAL_INFO_URL = "info";
    //列印、補印、作廢API
    public static final String BARCODE_URL = "barcode/print";
    public static final String PALLET_BIND_URL = "pallet/print";
    public static final String PALLET_INVALID_URL = "pallet/invalid";
    public static final String RE_PALLET_URL = "pallet/reprint";
    public static final String RE_BARCODE_URL = "barcode/reprint";
    //上傳訂單、資料庫
    public static final String REGAL_UPLOAD_URL = "upload";
    public static final String REGAL_DATABASE_URL = "uploadDB";

    //執行緒
    protected static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
}
