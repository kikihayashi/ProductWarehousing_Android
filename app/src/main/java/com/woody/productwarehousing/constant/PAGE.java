package com.woody.productwarehousing.constant;

import android.graphics.Color;

public enum PAGE {

    PrintFragment("條碼列印", Color.parseColor("#01579B")),
    CreateFragment("產品建立", Color.parseColor("#A8150F")),
    UploadFragment("資料上傳", Color.parseColor("#27756A")),
    ReprintFragment("資料補印", Color.parseColor("#37445C")),
    DetailActivity("詳情頁面", Color.parseColor("#01579B"));

    private String name;
    private int color;

    PAGE(String name,int color) {
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
