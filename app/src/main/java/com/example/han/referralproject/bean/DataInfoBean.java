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
    public String blood_sugar;
    public String blood_oxygen;
    public int heart_rate;
    public double weight;
    public int ecg;//心电结果
    public String cholesterol;//胆固醇
    public String uric_acid;//尿酸
    public String sugar_time;//测量血糖的时间：空腹，饭后一小时，饭后两小时
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
        if (!TextUtils.isEmpty(blood_sugar)){
            paramsMap.put("blood_sugar", blood_sugar);
        }
        if (!TextUtils.isEmpty(blood_oxygen)){
            paramsMap.put("blood_oxygen", blood_oxygen);
        }
        if (heart_rate != 0){
            paramsMap.put("heart_rate", String.valueOf(heart_rate));
            paramsMap.put("ecg", String.valueOf(ecg));
        }
        if (!TextUtils.isEmpty(cholesterol)){
            paramsMap.put("cholesterol", cholesterol);
        }
        if (!TextUtils.isEmpty(uric_acid)){
            paramsMap.put("uric_acid", uric_acid);
        }
        if(!TextUtils.isEmpty(sugar_time)){
            paramsMap.put("sugar_time",sugar_time);
        }
        if (weight != 0){
            paramsMap.put("weight", String.valueOf(weight));
        }
        return paramsMap;
    }
}
