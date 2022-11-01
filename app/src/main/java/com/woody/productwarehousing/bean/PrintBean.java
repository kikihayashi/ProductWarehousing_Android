package com.woody.productwarehousing.bean;

import java.io.Serializable;

public class PrintBean implements Serializable {

    private int index = 0;                              //Grid位置
    private int printNumber = 0;                        //列印次數
    private int collectNumber = 0;                      //蒐集次數
    private String manufactureDate = "";                //製造日期
    private String expiryDate = "";                     //有效日期
    private SourceBean sourceBean = new SourceBean();   //來源資料
    private boolean isSaved = false;                    //是否有儲存過了

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(int printNumber) {
        this.printNumber = printNumber;
    }

    public int getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(int collectNumber) {
        this.collectNumber = collectNumber;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public SourceBean getSourceBean() {
        return sourceBean;
    }

    public void setSourceBean(SourceBean sourceBean) {
        this.sourceBean = sourceBean;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}
