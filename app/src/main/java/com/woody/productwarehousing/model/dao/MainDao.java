package com.woody.productwarehousing.model.dao;

import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.woody.productwarehousing.bean.PrintBean;
import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.sqlite.SQLiteHelper;
import com.woody.productwarehousing.utils.AesTool;
import com.woody.productwarehousing.utils.IoTool;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.constant.TYPE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainDao extends BaseDao implements Dao.MainDaoInterface {

    private MutableLiveData<Integer> staffDataLive = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> orderDataLive = new MutableLiveData<>();
    private MutableLiveData<List<PrintBean>> printDataLive = new MutableLiveData<>();
    private MutableLiveData<ArrayList<SourceBean>> reprintDataLive = new MutableLiveData<>();
    private MutableLiveData<ArrayList<SourceBean>> barcodeDataLive = new MutableLiveData<>();
    private MutableLiveData<ArrayList<SourceBean>> uploadDataLive = new MutableLiveData<>();
    private MutableLiveData<ArrayList<SourceBean>> uploadDataCollectedLive = new MutableLiveData<>();
    private MutableLiveData<List<String>> orderListFinalLive = new MutableLiveData<>();
    private MutableLiveData<HashMap<String, List<SourceBean>>> uploadMapFinalLive = new MutableLiveData<>();
    private String BillDate;

    public MainDao() {
        super();
    }

    /**
     * 取得 LiveData
     */
    @Override
    public MutableLiveData<Integer> getStaffData() {
        return staffDataLive;
    }

    @Override
    public MutableLiveData<ArrayList<String>> getOrderData() {
        return orderDataLive;
    }

    @Override
    public MutableLiveData<List<PrintBean>> getPrintData() {
        return printDataLive;
    }

    @Override
    public MutableLiveData<ArrayList<SourceBean>> getBarcodeData() {
        return barcodeDataLive;
    }

    @Override
    public MutableLiveData<ArrayList<SourceBean>> getUploadData() {
        return uploadDataLive;
    }

    @Override
    public MutableLiveData<ArrayList<SourceBean>> getUploadDataCollected() {
        return uploadDataCollectedLive;
    }

    @Override
    public MutableLiveData<ArrayList<SourceBean>> getReprintData() {
        return reprintDataLive;
    }

    @Override
    public MutableLiveData<HashMap<String, List<SourceBean>>> getUploadFinalData() {
        return uploadMapFinalLive;
    }

    @Override
    public MutableLiveData<List<String>> getOrderFinalData() {
        return orderListFinalLive;
    }

    /**
     * 設置 API JSON
     */
    //設置API JSON格式(製令單)
    @Override
    public JsonObject setOrderJson(String MKOrdNO) {
        JsonObject object = new JsonObject();
        try {
            JsonObject object_MKOrdNO = new JsonObject();
            object_MKOrdNO.addProperty("MKOrdNO", MKOrdNO);

            JsonArray array = new JsonArray();
            array.add(object_MKOrdNO);

            JsonObject object_MasterData = new JsonObject();
            object_MasterData.add("MasterData", array);

            object.addProperty("CID", TAG.CompanyID.getName());
            object.addProperty("UID", TAG.UserID.getName());
            object.addProperty("UPWD", AesTool.decrypt(TAG.UserPWD.getName()));
            object.addProperty("Tag", TAG.OrderTag.getName());
            object.add("Data", object_MasterData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //設置API JSON格式(製令單詳細資料)
    @Override
    public JsonObject setInfoJson(Set<String> prodIdSet) {
        JsonObject object = new JsonObject();
        try {
            object.addProperty("company", TAG.CompanyID.getName());
            object.add("Query", setProdIdJsonArray(prodIdSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //設置料號JsonArray
    private JsonArray setProdIdJsonArray(Set<String> prodIdSet) {
        JsonArray array = new JsonArray();
        for (String prodId : prodIdSet) {
            JsonObject object = new JsonObject();
            object.addProperty("ProdID", prodId);
            array.add(object);
        }
        return array;
    }

    //設置列印箱數 JSON格式
    @Override
    public JsonObject setBarcodeJson(int dataIndex, PAGE page) {
        SourceBean sourceBean;
        PrintBean printBean = null;
        switch (page) {
            case PrintFragment:
                printBean = printDataLive.getValue().get(dataIndex);
                sourceBean = printBean.getSourceBean();
                break;
            case ReprintFragment:
                sourceBean = barcodeDataLive.getValue().get(dataIndex);
                break;
            default:
                throw new IllegalArgumentException();
        }

        JsonObject object = new JsonObject();
        try {
            object.addProperty("Print_IP", TAG.PrintIP.getName());
            object.addProperty("Pro_Name", sourceBean.getProductName());
            object.addProperty("Lot_Name", sourceBean.getProdName());
            switch (page) {
                case PrintFragment:
                    object.addProperty("QRcode", createBarcode(printBean));
                    break;
                case ReprintFragment:
                    object.addProperty("QRcode", sourceBean.getQRcode());
                    break;
            }
            object.addProperty("Vaily_Day", String.valueOf(sourceBean.getDefValidDays()));
            object.addProperty("Weight_Max", sourceBean.getQJUppLimitWt());
            object.addProperty("Weight_Min", sourceBean.getQJLowLimitWt());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.v("LINS", "setBarcodeJson：" + object.toString());
        return object;
    }

    //設置綁定棧板 JSON格式
    @Override
    public JsonObject setPalletJson(int dataIndex, PAGE page) {
        SourceBean sourceBean;
        String batchId;
        String make_Date;
        BillDate = simpleDateFormat2.format(new Date());//yyyy-MM-dd HH:mm:ss
        switch (page) {
            case PrintFragment:
                sourceBean = printDataLive.getValue().get(dataIndex).getSourceBean();
                batchId = printDataLive.getValue().get(dataIndex).getManufactureDate().replaceAll("/", "");
                make_Date = BillDate.substring(0, 10).replace("-", "");
                break;
            case ReprintFragment:
                sourceBean = reprintDataLive.getValue().get(dataIndex);
                batchId = sourceBean.getBatchID();
                make_Date = sourceBean.getBillDate().substring(0, 10).replace("-", "");
                break;
            default:
                throw new IllegalArgumentException();
        }
        JsonObject object = new JsonObject();
        try {
            object.addProperty("Print_IP", TAG.PrintIP.getName());
            object.addProperty("Pro_Name", sourceBean.getProdName());
            object.addProperty("Pro_Id", sourceBean.getProdID());
            object.addProperty("Lot_Id", batchId);
            object.addProperty("OrderId", sourceBean.getMKOrdNO());
            object.addProperty("Make_Date", make_Date);
            object.addProperty("Weight_Set", sourceBean.getQJSetWeight());
            object.addProperty("MachineId", TAG.PDAID.getName());
            if (page == PAGE.ReprintFragment) {
                object.addProperty("PalletId", sourceBean.getPallet());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //設置作廢棧板 JSON格式
    @Override
    public JsonObject setInvalidJson(int dataIndex) {
        JsonObject object = new JsonObject();
        try {
            object.addProperty("company", TAG.CompanyID.getName());
            object.add("SerialQuery", setSerialQueryJsonArray(dataIndex));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //設置要作廢的流水號 JsonArray
    private JsonArray setSerialQueryJsonArray(int dataIndex) {
        PrintBean printBean = printDataLive.getValue().get(dataIndex);
        SourceBean sourceBean = printBean.getSourceBean();

        List<String> serialList = new ArrayList<>();
        Cursor cursor = null;
        String command = "SELECT Serial FROM " + WORK_TABLE + " WHERE " +
                "MKOrdNO='" + sourceBean.getMKOrdNO() + "' AND " +
                "ProdID='" + sourceBean.getProdID() + "' AND " +
                "BatchID='" + printBean.getManufactureDate().replaceAll("/", "") + "'";

        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String serial = cursor.getString(cursor.getColumnIndexOrThrow("Serial"));
                    serialList.add(serial);
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
        }

        JsonArray array = new JsonArray();
        for (String serial : serialList) {
            JsonObject object = new JsonObject();
            object.addProperty("Order_ID", sourceBean.getMKOrdNO());
            object.addProperty("Pro_ID", sourceBean.getProdID());
            object.addProperty("Lot_ID", printBean.getManufactureDate().replaceAll("/", ""));
            object.addProperty("Serial_ID", serial);
            array.add(object);
        }
        return array;
    }

    //設置上傳 JSON格式(單頭)
    public JsonObject setUploadJson(List<SourceBean> sourceBeanList) {
        JsonObject object = new JsonObject();
        try {
            JsonObject object_inner = new JsonObject();
            object_inner.addProperty("BillDate", simpleDateFormat1.format(new Date()).replaceAll("-", ""));
            object_inner.addProperty("MKOrdNO", sourceBeanList.get(0).getMKOrdNO());
            object_inner.addProperty("WorkTimeRealProc", sourceBeanList.get(0).getWorkTimeRealProc());
            object_inner.addProperty("MKOrdDate", String.valueOf(sourceBeanList.get(0).getMKOrdDate()));
            object_inner.addProperty("ProductId", sourceBeanList.get(0).getProductId());
            object_inner.addProperty("ProductName", sourceBeanList.get(0).getProductName());
            object_inner.addProperty("SrcNoInQty", sourceBeanList.get(0).getSrcNoInQty());
            object_inner.add("DetailData", setDetailDataJsonArray(sourceBeanList));

            JsonArray array = new JsonArray();
            array.add(object_inner);

            JsonObject object_MasterData = new JsonObject();
            object_MasterData.add("MasterData", array);

            JsonObject object_AllSerialNo = new JsonObject();
            object_AllSerialNo.add("AllSerialNo", setAllSerialNoJsonArray(sourceBeanList));

            object.addProperty("CID", TAG.CompanyID.getName());
            object.addProperty("UID", TAG.UserID.getName());
            object.addProperty("UPWD", AesTool.decrypt(TAG.UserPWD.getName()));
            object.addProperty("PDAID", TAG.PDAID.getName());
            object.addProperty("Tag", TAG.UploadTag.getName());
            object.add("Data", object_MasterData);
            object.add("SerialNo", object_AllSerialNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("LINS", "setUploadJson：" + object);
        IoTool.writeUploadJson(object.toString());
        return object;
    }

    //設置上傳所需要的流水序號 JsonArray
    private JsonArray setAllSerialNoJsonArray(List<SourceBean> sourceBeanList) {
        JsonArray array = new JsonArray();
        String PDAID = TAG.PDAID.getName();
        TreeSet<String> treeSet = new TreeSet<>();
        for (SourceBean sourceBean : sourceBeanList) {
            if (!treeSet.contains(sourceBean.getMKOrdNO())) {
                treeSet.add(sourceBean.getMKOrdNO());

                Cursor cursor = null;
                String command = "SELECT ProdID,BatchID,Serial,Pallet FROM " + WORK_TABLE +
                        " WHERE MKOrdNO='" + sourceBean.getMKOrdNO() + "'" +
                        " AND IsCollected='Y'" +
                        " AND IsUpload='N'";

                try {
                    cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            JsonObject object_inner = new JsonObject();

                            String ProdID = cursor.getString(cursor.getColumnIndexOrThrow("ProdID"));
                            String BatchID = cursor.getString(cursor.getColumnIndexOrThrow("BatchID"));
                            String Serial = cursor.getString(cursor.getColumnIndexOrThrow("Serial"));
                            String Pallet = cursor.getString(cursor.getColumnIndexOrThrow("Pallet"));

                            object_inner.addProperty("MKOrdNO", sourceBean.getMKOrdNO());
                            object_inner.addProperty("PDAID", PDAID);
                            object_inner.addProperty("ProdID", ProdID);
                            object_inner.addProperty("BatchID", BatchID);
                            object_inner.addProperty("Serial", Serial);
                            object_inner.addProperty("QtyorWeight", "1");
                            object_inner.addProperty("PalletNo", Pallet);
                            array.add(object_inner);
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
                }
            }
        }
        return array;
    }

    //設置上傳 JSON格式(DetailData單身)
    private JsonArray setDetailDataJsonArray(List<SourceBean> sourceBeanList) {
        JsonArray array = new JsonArray();

        for (SourceBean sourceBean : sourceBeanList) {
            JsonObject object = new JsonObject();
            object.addProperty("RowNO", String.valueOf(sourceBean.getRowNO()));
            object.addProperty("WareInClass", sourceBean.getWareInClass());
            object.addProperty("WareID", sourceBean.getWareID());
            object.addProperty("ProdID", sourceBean.getProdID());
            object.addProperty("ProdName", sourceBean.getProdName());
            object.addProperty("prd_Quantity", String.valueOf(sourceBean.getQuantity()));
            object.addProperty("ItemRemark", remark);
            object.add("DetailData", setInnerDetailDataJsonArray(sourceBean));
            array.add(object);
        }
        return array;
    }

    //設置上傳 JSON格式(InnerDetailData單身)
    private JsonArray setInnerDetailDataJsonArray(SourceBean sourceBean) {
        JsonArray array_inner = new JsonArray();
        Cursor cursor = null;
        String command = "SELECT *,SUM(ProdQuantity) AS Quantity " +
                " FROM " + WORK_TABLE +
                " WHERE IsUpload='N' AND Pallet<>''" +
                " AND MKOrdNO='" + sourceBean.getMKOrdNO() + "'" +
                " AND ProdID='" + sourceBean.getProdID() + "'" +
                " GROUP BY BatchID,WareID,WareInClass,StorageID";

//        Log.v("LINS", "getInnerObject_command：" + command);
        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int RowNO = cursor.getInt(cursor.getColumnIndexOrThrow("RowNO"));
                    String BatchID = cursor.getString(cursor.getColumnIndexOrThrow("BatchID"));
                    int Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    String ProduceDate = cursor.getString(cursor.getColumnIndexOrThrow("ProduceDate"));
                    String ValidDate = cursor.getString(cursor.getColumnIndexOrThrow("ValidDate"));
                    int EstWareInDate = cursor.getInt(cursor.getColumnIndexOrThrow("EstWareInDate"));
                    String StorageID = cursor.getString(cursor.getColumnIndexOrThrow("StorageID"));
                    String Serial = cursor.getString(cursor.getColumnIndexOrThrow("Serial"));
                    String QJSetWeight = cursor.getString(cursor.getColumnIndexOrThrow("QJSetWeight"));
                    String IsBox = cursor.getString(cursor.getColumnIndexOrThrow("IsBox"));
                    String Created_at = cursor.getString(cursor.getColumnIndexOrThrow("Created_at"));

                    JsonObject object_inner = new JsonObject();
                    object_inner.addProperty("ParentRowNO", RowNO);
                    object_inner.addProperty("BatchID", BatchID);
                    object_inner.addProperty("Quantity", String.valueOf(Quantity));
                    object_inner.addProperty("ProduceDate", ProduceDate);
                    object_inner.addProperty("ValidDate", ValidDate);
                    object_inner.addProperty("EstWareInDate", EstWareInDate);
                    object_inner.addProperty("StorageID", StorageID);
                    object_inner.addProperty("Serial", Serial);
                    object_inner.addProperty("QJSetWeight", QJSetWeight);
                    object_inner.addProperty("Memo", remark);
                    object_inner.addProperty("IsBox", IsBox);
                    object_inner.addProperty("Created_at", Created_at);
                    array_inner.add(object_inner);
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
        }
        return array_inner;
    }

    /**
     * MainActivity Method
     */
    //刪除15天以前，已上傳、作廢的舊資料
    @Override
    public void deleteOldWorkTable() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1 * Integer.valueOf(TAG.LimitDay.getName()));//今天日期減15天
        final String DEADLINE = simpleDateFormat1.format(calendar.getTime());

        String command = "DELETE FROM " + WORK_TABLE +
                " WHERE Created_at <'" + DEADLINE + "'";

        try {
            SQLiteHelper.execSQL(sqLiteDatabase, command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //刪除15天以前，上傳歷程的舊資料
    @Override
    public void deleteOldWareInTable() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1 * Integer.valueOf(TAG.LimitDay.getName()));//今天日期減15天
        final String DEADLINE = simpleDateFormat1.format(calendar.getTime());

        String command = "DELETE FROM " + WARE_IN_TABLE +
                " WHERE ResponseResult='True' AND BillDate < '" + DEADLINE + "'";
        try {
            SQLiteHelper.execSQL(sqLiteDatabase, command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //刪除15天以前，上傳的Log檔案
    @Override
    public void deleteOldLogFile() {
        IoTool.deleteUploadJson(Integer.valueOf(TAG.LimitDay.getName()));
    }

    //取得員工人數
    @Override
    public void getStaffNumber() {
        int staff = 1;
        Cursor cursor = null;
        String command = "SELECT Staff FROM " + WORK_TABLE + " " +
                "ORDER BY Created_at DESC LIMIT 1";

//        Log.v("LINS", "getStaffNumber_command：" + command);
        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    staff = cursor.getInt(cursor.getColumnIndexOrThrow("Staff"));
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
            staffDataLive.postValue(staff);
        }
    }

    //設置員工人數
    @Override
    public void setStaffNumber(int staffNumber) {
        staffDataLive.setValue(staffNumber);
    }

    /**
     * PrintFragment Method
     */
    //設置PrintBeanList(畫面更新)
    @Override
    public void setPrintBeanList(List<PrintBean> printBeanList) {
        //處理PrintFragment沒點選單品、多品，直接在UploadFragment按上傳會閃退的情況
        if (printBeanList != null) {
            List<PrintBean> printBeanListNew = new CopyOnWriteArrayList<>();

            for (PrintBean printBean : printBeanList) {
                //列印總數
                String commandPrint = "SELECT COUNT(IsCollected) FROM " + WORK_TABLE + " " +
                        "WHERE MKOrdNO='" + printBean.getSourceBean().getMKOrdNO() + "' " +
                        "AND ProdID='" + printBean.getSourceBean().getProdID() + "' " +
                        "AND BatchID='" + printBean.getManufactureDate().replaceAll("/", "") + "'";

                //棧板箱數
                String commandCollect = "SELECT COUNT(IsCollected) FROM " + WORK_TABLE + " " +
                        "WHERE MKOrdNO='" + printBean.getSourceBean().getMKOrdNO() + "' " +
                        "AND ProdID='" + printBean.getSourceBean().getProdID() + "' " +
                        "AND BatchID='" + printBean.getManufactureDate().replaceAll("/", "") + "' " +
                        "AND IsUpload='N' AND IsCollected='N'";

                String command = "SELECT " +
                        "(" + commandPrint + ") AS PrintNumber, " +
                        "(" + commandCollect + ") AS CollectNumber " +
                        "FROM " + WORK_TABLE + " LIMIT 1";

                Cursor cursor = null;
                try {
                    cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToNext()) {
                            int printNumber = cursor.getInt(cursor.getColumnIndexOrThrow("PrintNumber"));
                            int collectNumber = cursor.getInt(cursor.getColumnIndexOrThrow("CollectNumber"));
                            printBean.setPrintNumber(printNumber);
                            printBean.setCollectNumber(collectNumber);
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
                }
                printBeanListNew.add(printBean);
            }
            printDataLive.postValue(printBeanListNew);
        }
    }

    //產生箱數條碼
    private String createBarcode(PrintBean printBean) {
        Cursor cursor = null;
        String serial = "";
        int sequence = 1;
        String command = "SELECT Serial FROM " + WORK_TABLE + " " +
                "WHERE MKOrdNO='" + printBean.getSourceBean().getMKOrdNO() + "' " +
                "AND ProdID='" + printBean.getSourceBean().getProdID() + "' " +
                "AND BatchID='" + printBean.getManufactureDate().replaceAll("/", "") + "' " +
                "ORDER BY Serial DESC";

//        Log.v("LINS", "createBarcode_command：" + command);
        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    serial = cursor.getString(0);
                    sequence = Integer.parseInt(serial.substring(2)) + 1;
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
        }
        serial = (TAG.PDAID.getName()) + String.format("%06d", sequence);

        StringBuilder barcode = new StringBuilder();
        barcode.append(printBean.getSourceBean().getMKOrdNO() + ";");
        barcode.append(printBean.getManufactureDate().replaceAll("/", "") + ";");
        barcode.append(printBean.getSourceBean().getProdID() + ";");
        barcode.append(printBean.getManufactureDate().replaceAll("/", "") + ";");
        barcode.append(printBean.getExpiryDate().replaceAll("/", "") + ";");
        barcode.append(printBean.getSourceBean().getQJSetWeight() + ";");
        barcode.append(serial + ";");
        barcode.append(TAG.PrintType.getName());

//        Log.v("LINS", "barcode：" + barcode.toString());
        return barcode.toString();
    }

    //存入作業Table
    @Override
    public void insertWorkTable(int dataIndex, String barcode) {
        String serial = barcode.split(";")[6]; //機號 + 流水號
        PrintBean printBean = printDataLive.getValue().get(dataIndex);
        try {
            String command = ("INSERT INTO " + WORK_TABLE + " " +
                    "(BillDate,MKOrdNO,WorkTimeRealProc,MKOrdDate,ProductId,ProductName,SrcNoInQty," +
                    "RowNO,WareInClass,WareID,ProdID,ProdName,ParentRowNO,BatchID,ProdQuantity,ProduceDate," +
                    "ValidDate,EstWareInDate,StorageID,Serial,QJSetWeight,IsBox,Created_at,IsCollected,IsUpload,Pallet,QRcode,Staff) " +
                    "VALUES('" +
                    remark + "','" +
                    printBean.getSourceBean().getMKOrdNO() + "','" +
                    "0" + "','" +
                    printBean.getSourceBean().getMKOrdDate() + "','" +
                    printBean.getSourceBean().getProductId() + "','" +
                    printBean.getSourceBean().getProductName() + "','" +
                    printBean.getSourceBean().getSrcNoInQty() + "','" +
                    printBean.getSourceBean().getRowNO() + "','" +
                    (TAG.WareInClass.getName()) + "','" +
                    (TAG.WareID.getName()) + "','" +
                    printBean.getSourceBean().getProdID() + "','" +
                    printBean.getSourceBean().getProdName() + "','" +
                    printBean.getSourceBean().getRowNO() + "','" +
                    printBean.getManufactureDate().replaceAll("/", "") + "','" +
                    "1" + "','" +
                    printBean.getManufactureDate().replaceAll("/", "") + "','" +
                    printBean.getExpiryDate().replaceAll("/", "") + "','" +
                    printBean.getSourceBean().getEstWareInDate() + "','" +
                    (TAG.StorageID.getName()) + "','" +
                    serial + "','" +
                    printBean.getSourceBean().getQJSetWeight() + "','" +
                    "1" + "','" +
                    simpleDateFormat2.format(new Date()) + "','" +
                    "N" + "','" +
                    "N" + "','" +
                    remark + "','" +
                    barcode + "','" +
                    staffDataLive.getValue() + "')");

            SQLiteHelper.execSQL(sqLiteDatabase, command);
            infoMessageLivePrint.postValue(TAG.InsertSuccess);
        } catch (Exception e) {
            TAG.InsertFailure.setName("發生錯誤：" + e.toString());
            infoMessageLivePrint.postValue(TAG.InsertFailure);
            e.printStackTrace();
        }
    }

    //更新作業Table(棧板蒐集 or 上傳成功)
    @Override
    public void updateWorkTable(TYPE type, int dataIndex, String... data) {
        try {
            SourceBean sourceBean;
            String command = "";
            switch (type) {
                case Collect: //棧板列印
                    sourceBean = printDataLive.getValue().get(dataIndex).getSourceBean();
                    PrintBean printBean = printDataLive.getValue().get(dataIndex);
                    String pallet = data[0];
                    command = "UPDATE " + WORK_TABLE + " SET " +
                            "BillDate='" + BillDate + "', " +
                            "IsCollected='Y', " +
                            "Pallet='" + pallet + "' " +
                            "WHERE " +
                            "MKOrdNO='" + sourceBean.getMKOrdNO() + "' AND " +
                            "ProdID='" + sourceBean.getProdID() + "' AND " +
                            "BatchID='" + printBean.getManufactureDate().replaceAll("/", "") + "' AND " +
                            "IsCollected='N'";
                    break;

                case Invalid://棧板作廢
                    printBean = printDataLive.getValue().get(dataIndex);
                    sourceBean = printDataLive.getValue().get(dataIndex).getSourceBean();
                    command = "UPDATE " + WORK_TABLE + " SET " +
                            "BillDate='" + simpleDateFormat2.format(new Date()) + "', " +
                            "IsCollected='I', " +
                            "IsUpload='I' " +
                            "WHERE " +
                            "MKOrdNO='" + sourceBean.getMKOrdNO() + "' AND " +
                            "ProdID='" + sourceBean.getProdID() + "' AND " +
                            "BatchID='" + printBean.getManufactureDate().replaceAll("/", "") + "' AND " +
                            "IsCollected='N'";
                    break;

                case Upload://上傳
                    String MKOrdNO = orderListFinalLive.getValue().get(dataIndex);
                    command = "UPDATE " + WORK_TABLE + " SET " +
                            "IsUpload='Y' " +
                            "WHERE " +
                            "MKOrdNO='" + MKOrdNO + "' AND " +
                            "IsUpload='N' AND " +
                            "Pallet<>''";
                    break;
            }
            Log.v("LINS", "updateWorkTable_command：" + command);

            SQLiteHelper.execSQL(sqLiteDatabase, command);
        } catch (Exception e) {
            switch (type) {
                case Collect:
                    TAG.CollectUpdateFailure.setName("發生錯誤：" + e);
                    infoMessageLivePrint.postValue(TAG.CollectUpdateFailure);
                    break;
                case Invalid:
                    TAG.InvalidUpdateFailure.setName("發生錯誤：" + e);
                    infoMessageLivePrint.postValue(TAG.InvalidUpdateFailure);
                    break;
                case Upload:
                    TAG.UploadFailure.setName("發生錯誤：" + e);
                    infoMessageLiveUpload.postValue(TAG.UploadFailure);
                    break;
            }
            e.printStackTrace();
        }

        switch (type) {
            case Collect:
                infoMessageLivePrint.postValue(TAG.CollectUpdateSuccess);
                break;
            case Invalid:
                infoMessageLivePrint.postValue(TAG.InvalidUpdateSuccess);
                break;
            case Upload://data[0] = WareInNO(入庫單號)
                insertWareInTable(TAG.LoadingSuccess, dataIndex, true, data[0], "");
                break;
        }
    }

    /**
     * CreateFragment Method
     */
    //重置來源Table
    @Override
    public void resetSourceTable() {
        try {
            String command = "DELETE FROM " + SOURCE_TABLE;
            SQLiteHelper.execSQL(sqLiteDatabase, command);
            infoMessageLiveCreate.postValue(TAG.ResetSuccess);
        } catch (Exception e) {
            TAG.ResetFailure.setName(e.toString());
            infoMessageLiveCreate.postValue(TAG.ResetFailure);
            e.printStackTrace();
        }
    }

    //存入來源Table
    @Override
    public void insertSourceTable(List<SourceBean> sourceBeanList) {
        try {
            ArrayList sqlList = new ArrayList();
            for (SourceBean sourceBean : sourceBeanList) {
                sqlList.add("INSERT INTO " + SOURCE_TABLE + " " +
                        "(MKOrdNO,MKOrdDate,ProductId,ProductName,SrcNoInQty,RowNO,ProdID,ProdName," +
                        "DefValidDays,EstWareInDate,QJSetWeight,QJUppLimitWt,QJLowLimitWt) " +
                        "VALUES('" +
                        sourceBean.getMKOrdNO() + "','" +
                        sourceBean.getMKOrdDate() + "','" +
                        sourceBean.getProductId() + "','" +
                        sourceBean.getProductName() + "','" +
                        sourceBean.getSrcNoInQty() + "','" +
                        sourceBean.getRowNO() + "','" +
                        sourceBean.getProdID() + "','" +
                        sourceBean.getProdName() + "','" +
                        sourceBean.getDefValidDays() + "','" +
                        sourceBean.getEstWareInDate() + "','" +
                        sourceBean.getQJSetWeight() + "','" +
                        sourceBean.getQJUppLimitWt() + "','" +
                        sourceBean.getQJLowLimitWt() + "')");
            }
            SQLiteHelper.bulkInsert(sqLiteDatabase, sqlList);
            infoMessageLiveCreate.postValue(TAG.InsertSuccess);
        } catch (Exception e) {
            TAG.InsertFailure.setName("發生錯誤：" + e);
            infoMessageLiveCreate.postValue(TAG.InsertFailure);
            e.printStackTrace();
        }
    }

    //獲得之前API所獲得的製令單號
    @Override
    public void getSourceTableOrder() {
        ArrayList<String> orderData = new ArrayList<>();
        Cursor cursor = null;
        String command = "SELECT DISTINCT MKOrdNO FROM " + SOURCE_TABLE;

        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String MKOrdNO = cursor.getString(cursor.getColumnIndexOrThrow("MKOrdNO"));
                    orderData.add(MKOrdNO);
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
            orderDataLive.postValue(orderData);
        }
    }

    /**
     * UploadFragment Method
     */
    //取得作業Table資料
    @Override
    public void getWorkTableData(PAGE page) {
        Cursor cursor = null;
        ArrayList<SourceBean> uploadList = new ArrayList<>();
        ArrayList<SourceBean> uploadListCollected = new ArrayList<>();
        ArrayList<SourceBean> reprintList = new ArrayList<>();

        List<String> orderListFinal = new ArrayList<>();
        HashMap<String, List<SourceBean>> uploadMapFinal = new HashMap<>();

        String command;
        switch (page) {
            case UploadFragment:
                command = "SELECT *,SUM(ProdQuantity) AS Quantity, MAX(Created_at) AS LastCreated_at " +
                        "FROM " + WORK_TABLE + " " +
                        "WHERE IsCollected='Y' AND IsUpload='N' " +
                        "GROUP BY MKOrdNO, ProdID, WareInClass, WareID, StorageID " +
                        "ORDER BY IsCollected ASC, BillDate DESC";
                break;

            //補印資料需要
            case ReprintFragment:
                command = "SELECT BillDate,MKOrdNO,ProdName,ProdID,Pallet,BatchID,QJSetWeight," +
                        "WareInClass,WareID,StorageID,MAX(Created_at) AS LastCreated_at " +
                        "FROM " + WORK_TABLE + " " +
                        "WHERE IsCollected<>'I' AND IsUpload<>'I' " +
                        "AND WareInClass='" + (TAG.WareInClass.getName()) + "' " +
                        "AND WareID='" + (TAG.WareID.getName()) + "' " +
                        "AND StorageID='" + (TAG.StorageID.getName()) + "' " +
                        "GROUP BY MKOrdNO, ProdID, BatchID, Pallet, WareInClass, WareID, StorageID " +
                        "ORDER BY BillDate DESC";
                break;

            default:
                throw new IllegalArgumentException();
        }
//        Log.v("LINS", "getWorkTableData_command：" + command);
        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String BillDate = cursor.getString(cursor.getColumnIndexOrThrow("BillDate"));
                    String MKOrdNO = cursor.getString(cursor.getColumnIndexOrThrow("MKOrdNO"));
                    String ProdName = cursor.getString(cursor.getColumnIndexOrThrow("ProdName"));
                    String ProdID = cursor.getString(cursor.getColumnIndexOrThrow("ProdID"));
                    String Pallet = cursor.getString(cursor.getColumnIndexOrThrow("Pallet"));
                    String BatchID = cursor.getString(cursor.getColumnIndexOrThrow("BatchID"));
                    String QJSetWeight = cursor.getString(cursor.getColumnIndexOrThrow("QJSetWeight"));
                    String WareInClass = cursor.getString(cursor.getColumnIndexOrThrow("WareInClass"));
                    String WareID = cursor.getString(cursor.getColumnIndexOrThrow("WareID"));
                    String StorageID = cursor.getString(cursor.getColumnIndexOrThrow("StorageID"));
                    String LastCreated_at = cursor.getString(cursor.getColumnIndexOrThrow("LastCreated_at"));

                    SourceBean sourceBean = new SourceBean();
                    sourceBean.setBillDate(BillDate);
                    sourceBean.setMKOrdNO(MKOrdNO);
                    sourceBean.setProdName(ProdName);
                    sourceBean.setProdID(ProdID);
                    sourceBean.setPallet(Pallet);
                    sourceBean.setBatchID(BatchID);
                    sourceBean.setQJSetWeight(QJSetWeight);
                    sourceBean.setCreatedTime(LastCreated_at);
                    sourceBean.setWareInClass(WareInClass);
                    sourceBean.setWareID(WareID);
                    sourceBean.setStorageID(StorageID);

                    if (page == PAGE.UploadFragment) {
                        int MKOrdDate = cursor.getInt(cursor.getColumnIndexOrThrow("MKOrdDate"));
                        String ProductId = cursor.getString(cursor.getColumnIndexOrThrow("ProductId"));
                        String ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
                        String SrcNoInQty = cursor.getString(cursor.getColumnIndexOrThrow("SrcNoInQty"));
                        int RowNO = cursor.getInt(cursor.getColumnIndexOrThrow("RowNO"));
                        int Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                        String ProduceDate = cursor.getString(cursor.getColumnIndexOrThrow("ProduceDate"));
                        String ValidDate = cursor.getString(cursor.getColumnIndexOrThrow("ValidDate"));
                        int EstWareInDate = cursor.getInt(cursor.getColumnIndexOrThrow("EstWareInDate"));
                        String Serial = cursor.getString(cursor.getColumnIndexOrThrow("Serial"));
                        String IsBox = cursor.getString(cursor.getColumnIndexOrThrow("IsBox"));
                        String IsCollected = cursor.getString(cursor.getColumnIndexOrThrow("IsCollected"));

                        sourceBean.setWorkTimeRealProc(getWorkTime(Pallet));
                        sourceBean.setMKOrdDate(MKOrdDate);
                        sourceBean.setProductId(ProductId);
                        sourceBean.setProductName(ProductName);
                        sourceBean.setSrcNoInQty(SrcNoInQty);
                        sourceBean.setRowNO(RowNO);
                        sourceBean.setQuantity(Quantity);
                        sourceBean.setProduceDate(ProduceDate);
                        sourceBean.setValidDate(ValidDate);
                        sourceBean.setEstWareInDate(EstWareInDate);
                        sourceBean.setSerial(Serial);
                        sourceBean.setIsBox(IsBox);
                        sourceBean.setCollected(IsCollected.equals("Y"));
                    }

                    switch (page) {
                        case UploadFragment:
                            //記錄有列印過的資料
                            uploadList.add(sourceBean);

                            //記錄有蒐集過的資料
                            uploadListCollected.add(sourceBean);

                            if (uploadMapFinal.containsKey(MKOrdNO)) {
                                List<SourceBean> tempList = uploadMapFinal.get(MKOrdNO);
                                tempList.add(sourceBean);
                                uploadMapFinal.put(MKOrdNO, tempList);
                            } else {
                                List<SourceBean> tempList = new ArrayList<>();
                                tempList.add(sourceBean);
                                uploadMapFinal.put(MKOrdNO, tempList);

                                //記錄有蒐集過的資料
                                orderListFinal.add(MKOrdNO);
                            }
                            break;

                        //補印資料需要
                        case ReprintFragment:
                            //記錄有列印過棧板成功的資料
                            reprintList.add(sourceBean);
                            break;

                        default:
                            throw new IllegalArgumentException();
                    }
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
        }

        switch (page) {
            case UploadFragment:
                uploadDataCollectedLive.postValue(uploadListCollected);
                uploadDataLive.postValue(uploadList);
                uploadMapFinalLive.postValue(uploadMapFinal);
                orderListFinalLive.postValue(orderListFinal);
                break;

            //補印資料需要
            case ReprintFragment:
                reprintDataLive.postValue(reprintList);
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    //存入入庫Table
    @Override
    public void insertWareInTable(TAG tag, int dataIndex, boolean responseResult, String WareInNO, String errorMessage) {
        String mKOrdNO = orderListFinalLive.getValue().get(dataIndex);
        SourceBean sourceBean = uploadMapFinalLive.getValue().get(mKOrdNO).get(0);
        try {
            String command = ("INSERT INTO " + WARE_IN_TABLE + " " +
                    "(BillDate,ResponseDate,ResponseResult,WareInNO,ErrorMessage,MKOrdNO,ProductName,ProdID,ProdName,BatchID,Pallet,TotalWeight,PDAID) " +
                    "VALUES('" +
                    sourceBean.getBillDate() + "','" +
                    simpleDateFormat2.format(new Date()) + "','" +
                    (responseResult ? "True" : "False") + "','" +
                    (responseResult ? WareInNO : "") + "','" +
                    (responseResult ? "" : errorMessage) + "','" +
                    sourceBean.getMKOrdNO() + "','" +
                    sourceBean.getProductName() + "','" +
                    remark + "','" +
                    remark + "','" +
                    remark + "','" +
                    remark + "','" +
                    getTotalQuantity(sourceBean.getMKOrdNO()) + "','" +
                    (TAG.PDAID.getName()) + "')");

            SQLiteHelper.execSQL(sqLiteDatabase, command);

            if (tag == TAG.LoadingSuccess) {
                infoMessageLiveUpload.postValue(TAG.UploadSuccess);
            } else {
                TAG.UploadFailure.setName(errorMessage);
                infoMessageLiveUpload.postValue(TAG.UploadFailure);
            }
        } catch (Exception e) {
            TAG.UploadFailure.setName("發生錯誤：" + e);
            infoMessageLiveUpload.postValue(TAG.UploadFailure);
            e.printStackTrace();
        }
    }

    //取得總包裝數(相同製令)
    private String getTotalQuantity(String mkOrdNO) {
        int totalQty = 0;
        List<SourceBean> dataList = uploadMapFinalLive.getValue().get(mkOrdNO);
        for (SourceBean s : dataList) {
            totalQty += s.getQuantity();
        }
        return String.valueOf(totalQty);
    }

    //取得工時
    private String getWorkTime(String pallet) {
        float workTime = 0;
        Cursor cursor = null;
        String lastTime_command = "SELECT ROUND((JULIANDAY(Created_at))* 86400) " +
                "FROM " + WORK_TABLE + " WHERE " +
                "Pallet='" + pallet + "' ORDER BY Created_at DESC LIMIT 1";

        String firstTime_command = "SELECT ROUND((JULIANDAY(Created_at))* 86400) " +
                "FROM " + WORK_TABLE + " WHERE " +
                "Pallet='" + pallet + "' ORDER BY Created_at ASC LIMIT 1";

        String command = "SELECT Staff, ((" + lastTime_command + ") - (" + firstTime_command + ")) AS TimeDifference " +
                "FROM " + WORK_TABLE + " WHERE " +
                "Pallet='" + pallet + "' ORDER BY Created_at DESC LIMIT 1";

//        Log.v("LINS", "getWorkTime_command：" + command);
        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    int Staff = cursor.getInt(cursor.getColumnIndexOrThrow("Staff"));
                    float TimeDifference = cursor.getFloat(cursor.getColumnIndexOrThrow("TimeDifference"));
                    workTime = (TimeDifference * Staff / 3600);
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
        }
        return (workTime <= 0.01) ? "0.01" : String.format("%.2f", workTime);
    }

    /**
     * ReprintFragment Method
     */
    //取得箱數條碼資料
    @Override
    public void getBarcodeRecord(int dataIndex) {
        Cursor cursor = null;
        ArrayList<SourceBean> barcodeList = new ArrayList<>();
        String pallet = reprintDataLive.getValue().get(dataIndex).getPallet();
        String mkOrdNO = reprintDataLive.getValue().get(dataIndex).getMKOrdNO();
        String prodID = reprintDataLive.getValue().get(dataIndex).getProdID();
        String batchID = reprintDataLive.getValue().get(dataIndex).getBatchID();

        String command = "SELECT JGPWT.*,JGPST.DefValidDays,JGPST.QJUppLimitWt,JGPST.QJLowLimitWt " +
                "FROM " + WORK_TABLE + " " +
                "LEFT JOIN " + SOURCE_TABLE + " ON " +
                "JGPWT.MKOrdNO=JGPST.MKOrdNO AND " +
                "JGPWT.ProdID=JGPST.ProdID " +
                "WHERE Pallet='" + pallet + "' AND " +
                "JGPWT.MKOrdNO='" + mkOrdNO + "' AND " +
                "JGPWT.ProdID='" + prodID + "' AND " +
                "JGPWT.BatchID='" + batchID + "' AND " +
                "JGPWT.IsCollected<>'I' AND " +
                "JGPWT.IsUpload<>'I'";

        try {
            cursor = SQLiteHelper.rawQuery(sqLiteDatabase, command);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
                    String ProdName = cursor.getString(cursor.getColumnIndexOrThrow("ProdName"));
                    String QRcode = cursor.getString(cursor.getColumnIndexOrThrow("QRcode"));
                    int DefValidDays = cursor.getInt(cursor.getColumnIndexOrThrow("DefValidDays"));
                    String QJUppLimitWt = cursor.getString(cursor.getColumnIndexOrThrow("QJUppLimitWt"));
                    String QJLowLimitWt = cursor.getString(cursor.getColumnIndexOrThrow("QJLowLimitWt"));
                    String Serial = cursor.getString(cursor.getColumnIndexOrThrow("Serial"));
                    String Pallet = cursor.getString(cursor.getColumnIndexOrThrow("Pallet"));
                    String BatchID = cursor.getString(cursor.getColumnIndexOrThrow("BatchID"));

                    SourceBean sourceBean = new SourceBean();
                    sourceBean.setProductName(ProductName);
                    sourceBean.setProdName(ProdName);
                    sourceBean.setQRcode(QRcode);
                    sourceBean.setDefValidDays(DefValidDays);
                    sourceBean.setQJUppLimitWt(QJUppLimitWt);
                    sourceBean.setQJLowLimitWt(QJLowLimitWt);
                    sourceBean.setSerial(Serial);
                    sourceBean.setPallet(Pallet);
                    sourceBean.setBatchID(BatchID);
                    barcodeList.add(sourceBean);
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
            barcodeDataLive.postValue(barcodeList);
        }
    }
}