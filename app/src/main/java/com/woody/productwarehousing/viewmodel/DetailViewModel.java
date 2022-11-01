package com.woody.productwarehousing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.woody.productwarehousing.bean.SourceBean;
import com.woody.productwarehousing.constant.PAGE;
import com.woody.productwarehousing.constant.TAG;
import com.woody.productwarehousing.model.repository.DetailRepository;
import com.woody.productwarehousing.utils.SingleLiveEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DetailViewModel extends AndroidViewModel {

    private DetailRepository detailRepository;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public DetailViewModel(@NonNull Application application) {
        super(application);
        detailRepository = DetailRepository.getInstance();
    }

    public SingleLiveEvent<TAG> getInfoMessage(PAGE fragmentTag) {
        return detailRepository.getInfoMessage(fragmentTag);
    }

    public MutableLiveData<ArrayList<SourceBean>> getSourceDataList() {
        return detailRepository.getSourceDataList();
    }

    public MutableLiveData<ArrayList<SourceBean>> getInfoDataList() {
        return detailRepository.getInfoDataList();
    }

    public void getSourceData() {
        detailRepository.getSourceData();
    }

    public void getInfoData(SourceBean sourceBean) {
        detailRepository.getInfoData(sourceBean);
    }

    public void getInfoData(String mKOrdNO) {
        detailRepository.getInfoData(mKOrdNO);
    }

    //強匠只會有180天(半年)、365天(一年)兩種
    public String getValidDate(String manufactureDate, int validTime) throws ParseException {
        Date mfd = simpleDateFormat.parse(manufactureDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mfd);

        //如果有效天數是一年
        if (validTime == 365 || validTime > 300) {
            calendar.add(Calendar.YEAR,1);//日期加1年
        }
        //如果有效天數是半年
        else {
            calendar.add(Calendar.MONTH,6);//日期加6個月
        }
        return simpleDateFormat.format(calendar.getTime());
    }

    //檢查選擇日期是否在15天以內
    public boolean checkIfDateValid(String manufactureDate) throws ParseException {
        Date today = new Date();
        if (simpleDateFormat.format(today).equals(manufactureDate)) {
            return true;
        }
        long differenceDay = TimeUnit.MILLISECONDS.toDays(today.getTime() - simpleDateFormat.parse(manufactureDate).getTime());
        return (differenceDay < Integer.valueOf(TAG.LimitDay.getName()) && differenceDay > 0);
    }
}
