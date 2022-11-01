package com.woody.productwarehousing.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainApiUnits {

    @Expose
    @SerializedName("Qrcode")
    private String Qrcode;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("HeadInfo")
    private HeadInfo HeadInfo;
    @Expose
    @SerializedName("Data")
    private Data Data;
    @Expose
    @SerializedName("exMessage")
    private String exMessage;
    @Expose
    @SerializedName("Result")
    private Result Result;
    @Expose
    @SerializedName("ID")
    private String ID;
    @Expose
    @SerializedName("success")
    private String success;
    @Expose
    @SerializedName("Date")
    private String Date;

    public String getQrcode() {
        return Qrcode;
    }

    public String getMessage() {
        return Message;
    }

    public HeadInfo getHeadInfo() {
        return HeadInfo;
    }

    public Data getData() {
        return Data;
    }

    public String getExMessage() {
        return exMessage;
    }

    public MainApiUnits.Result getResult() {
        return Result;
    }

    public String getID() {
        return ID;
    }

    public String getSuccess() {
        return success;
    }

    public String getDate() {
        return Date;
    }

    public static class HeadInfo {
        @Expose
        @SerializedName("company")//公司別
        private String company;

        public String getCompany() {
            return company;
        }
    }

    public static class Data {
        @Expose
        @SerializedName("Query1")
        private List<OrderQuery1Units> Query1;
        @Expose
        @SerializedName("Query2")
        private Object Query2;
        @Expose
        @SerializedName("Query3")
        private Object Query3;
        @Expose
        @SerializedName("Query4")
        private Object Query4;

        public List<OrderQuery1Units> getQuery1() {
            return Query1;
        }
    }

    public static class OrderQuery1Units {

        @Expose
        @SerializedName("ProdID")        //料號
        private String ProdID;
        @Expose
        @SerializedName("ProdName")      //料號名稱
        private String ProdName;
        @Expose
        @SerializedName("QJSetWeight")   //定重
        private String QJSetWeight;
        @Expose
        @SerializedName("QJUppLimitWt")  //上限重
        private String QJUppLimitWt;
        @Expose
        @SerializedName("QJLowLimitWt")  //下限重
        private String QJLowLimitWt;
        @Expose
        @SerializedName("DefValidDays")  //有效天數
        private String DefValidDays;

        public String getProdID() {
            return ProdID;
        }

        public String getProdName() {
            return ProdName;
        }

        public String getQJSetWeight() {
            return QJSetWeight;
        }

        public String getQJUppLimitWt() {
            return QJUppLimitWt;
        }

        public String getQJLowLimitWt() {
            return QJLowLimitWt;
        }

        public String getDefValidDays() {
            return DefValidDays;
        }
    }

    public static class Result{
        @Expose
        @SerializedName("IfSucceed")
        private String ifSucceed;

        @Expose
        @SerializedName("ErrMessage")
        private String errMessage;

        @Expose
        @SerializedName("IfBiz")
        private String ifBiz;

        @Expose
        @SerializedName("ErrMethodName")
        private String errMethodName;

        @Expose
        @SerializedName("WorkId")
        private String WorkId;

        public String getIfSucceed() {
            return ifSucceed;
        }

        public String getErrMessage() {
            return errMessage;
        }

        public String getIfBiz() {
            return ifBiz;
        }

        public String getErrMethodName() {
            return errMethodName;
        }

        public String getWorkId() {
            return WorkId;
        }
    }
}
