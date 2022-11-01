package com.woody.test;

public enum TAG {
    //Initial Setting
    DBName("P20163"),//預設幾天刪除已上傳的舊資料：15天
    LimitDay("15"),
    SleepTime("1000"),//預設列印間隔時間：1秒
    AdminPWD("AAAA"),
    PrintType("1"),//列印類別 1:列印、2:不成箱

    //Settings
//    BaseURL("http://192.168.43.15:8080/"),
//    PrintURL("http://192.168.43.15:8080/"),
//    UpdateURL("http://192.168.43.15:8080/"),
    BaseURL("https://productwarehousing-on-heroku.herokuapp.com/"),
    PrintURL("https://productwarehousing-on-heroku.herokuapp.com/"),
    UpdateURL("https://productwarehousing-on-heroku.herokuapp.com/"),
    UserID("administrator") ,
    UserPWD("GxFwOlKKuFNAgE7jCXagGw=="),
    PDAID("25"),
    CompanyID("99"),
    WareInClass("A1"),
    WareID("W1"),
    StorageID("S1"),
    PrintIP("192.168.9.209"),
    OrderTag("Order_Query"),
    UploadTag("Upload_Data"),

    Loading("資料載入中"),
    LoadingSuccess("資料載入成功！"),//API
    LoadingFailure("資料載入失敗！"),//API

    InsertSuccess("資料庫儲存成功！"),//SQL
    InsertFailure("資料庫儲存失敗！"),//SQL

    CollectUpdateSuccess("資料庫更新成功！"),//SQL
    CollectUpdateFailure("資料庫更新失敗！"),//SQL

    InvalidUpdateSuccess("資料庫更新成功！"),//SQL
    InvalidUpdateFailure("資料庫更新失敗！"),//SQL

    Uploading("上傳中"),
    UploadingResult("上傳結果"),
    UploadSuccess("資料上傳成功！"),//API
    UploadFailure("資料上傳失敗！"),//API

    ResetSuccess("資料庫刪除成功！"),//SQL
    ResetFailure("資料庫刪除失敗！");//SQL

    private String name;

    TAG(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
