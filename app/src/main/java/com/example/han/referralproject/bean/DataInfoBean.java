package com.example.han.referralproject.bean;

import android.text.TextUtils;

import com.example.han.referralproject.application.MyApplication;

import java.util.HashMap;
import java.util.Map;

public class DataInfoBean {
    public String temper_ature;
    public int high_pressure;
    public int low_pressure;
    public int pulse;

    public Map<String, String> getParamsMap(){
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramsMap.put("userid", MyApplication.getInstance().userId);
        if (!TextUtils.isEmpty(temper_ature)){
            paramsMap.put("temper_ature", temper_ature);
        }
        if (high_pressure != 0){
            paramsMap.put("high_pressure", String.valueOf(high_pressure));
            paramsMap.put("low_pressure", String.valueOf(low_pressure));
            paramsMap.put("pulse", String.valueOf(pulse));
        }
        return paramsMap;
    }
}
