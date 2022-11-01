package com.woody.productwarehousing.constant;

public enum TYPE {

    Upload("上傳") ,
    Collect("綁定"),
    Invalid("作廢");

    private String name;

    TYPE(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
