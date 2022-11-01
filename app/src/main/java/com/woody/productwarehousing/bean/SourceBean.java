package com.woody.productwarehousing.bean;

import java.io.Serializable;

public class SourceBean implements Serializable {

    private String MKOrdNO = "";         //製令單號
    private int MKOrdDate = 0;           //製令單日期
    private String ProductId = "";       //品號
    private String ProductName = "";     //品名
    private String SrcNoInQty = "";      //剩餘數量
    private int RowNO = 0;               //取製令查詢
    private String ProdID = "";          //料號
    private String ProdName = "";        //料號名稱
    private int DefValidDays = 0;        //有效天數
    private int EstWareInDate = 0;       //完工日(API會給)
    private String QJSetWeight = "";     //定重
    private String QJUppLimitWt = "";    //定重上限
    private String QJLowLimitWt = "";    //定重下限

    //上傳需要的元件-------------------------------------------------
    private String BillDate = "";        //單據日期(刷條碼當天)
    private String WorkTimeRealProc = "";//工時(預設1)
    private String BatchID = "";         //批號
    private int Quantity = 0;            //已收總數量
    private String ProduceDate = "";     //選擇日期
    private String ValidDate = "";       //有效日期
    private String Serial = "";          //列印序號
    private String IsBox = "";           //成箱不成箱
    private String CreatedTime = "";     //建立時間
    private String WareInClass = "";     //入庫類別
    private String WareID = "";          //倉庫別
    private String StorageID = "";       //儲位
    private boolean IsUpload = false;    //是否上傳
    private boolean IsCollected = false; //是否綁定棧板
    private String Pallet = "";          //棧板條碼

    //補印需要的元件-------------------------------------------------
    private String QRcode = "";          //箱子條碼

    public String getMKOrdNO() {
        return MKOrdNO;
    }

    public void setMKOrdNO(String MKOrdNO) {
        this.MKOrdNO = MKOrdNO;
    }

    public int getMKOrdDate() {
        return MKOrdDate;
    }

    public void setMKOrdDate(int MKOrdDate) {
        this.MKOrdDate = MKOrdDate;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getSrcNoInQty() {
        return SrcNoInQty;
    }

    public void setSrcNoInQty(String srcNoInQty) {
        SrcNoInQty = srcNoInQty;
    }

    public int getRowNO() {
        return RowNO;
    }

    public void setRowNO(int rowNO) {
        RowNO = rowNO;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public int getDefValidDays() {
        return DefValidDays;
    }

    public void setDefValidDays(int defValidDays) {
        DefValidDays = defValidDays;
    }

    public int getEstWareInDate() {
        return EstWareInDate;
    }

    public void setEstWareInDate(int estWareInDate) {
        EstWareInDate = estWareInDate;
    }

    public String getQJSetWeight() {
        return QJSetWeight;
    }

    public void setQJSetWeight(String QJSetWeight) {
        this.QJSetWeight = QJSetWeight;
    }

    public String getQJUppLimitWt() {
        return QJUppLimitWt;
    }

    public void setQJUppLimitWt(String QJUppLimitWt) {
        this.QJUppLimitWt = QJUppLimitWt;
    }

    public String getQJLowLimitWt() {
        return QJLowLimitWt;
    }

    public void setQJLowLimitWt(String QJLowLimitWt) {
        this.QJLowLimitWt = QJLowLimitWt;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getWorkTimeRealProc() {
        return WorkTimeRealProc;
    }

    public void setWorkTimeRealProc(String workTimeRealProc) {
        WorkTimeRealProc = workTimeRealProc;
    }

    public String getBatchID() {
        return BatchID;
    }

    public void setBatchID(String batchID) {
        BatchID = batchID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getProduceDate() {
        return ProduceDate;
    }

    public void setProduceDate(String produceDate) {
        ProduceDate = produceDate;
    }

    public String getValidDate() {
        return ValidDate;
    }

    public void setValidDate(String validDate) {
        ValidDate = validDate;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public String getIsBox() {
        return IsBox;
    }

    public void setIsBox(String isBox) {
        IsBox = isBox;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getWareInClass() {
        return WareInClass;
    }

    public void setWareInClass(String wareInClass) {
        WareInClass = wareInClass;
    }

    public String getWareID() {
        return WareID;
    }

    public void setWareID(String wareID) {
        WareID = wareID;
    }

    public String getStorageID() {
        return StorageID;
    }

    public void setStorageID(String storageID) {
        StorageID = storageID;
    }

    public boolean isUpload() {
        return IsUpload;
    }

    public void setUpload(boolean upload) {
        IsUpload = upload;
    }

    public boolean isCollected() {
        return IsCollected;
    }

    public void setCollected(boolean collected) {
        IsCollected = collected;
    }

    public String getPallet() {
        return Pallet;
    }

    public void setPallet(String pallet) {
        Pallet = pallet;
    }

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }
}
