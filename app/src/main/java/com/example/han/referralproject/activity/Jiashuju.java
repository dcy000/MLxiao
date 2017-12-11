package com.example.han.referralproject.activity;

import com.example.han.referralproject.bean.BUA;
import com.example.han.referralproject.bean.BloodOxygenHistory;
import com.example.han.referralproject.bean.BloodPressureHistory;
import com.example.han.referralproject.bean.BloodSugarHistory;
import com.example.han.referralproject.bean.CholesterolHistory;
import com.example.han.referralproject.bean.TemperatureHistory;

import java.util.ArrayList;

/**
 * Created by gzq on 2017/12/10.
 */

public class Jiashuju {
    public static ArrayList<TemperatureHistory> getTemperatureHistory(){
        ArrayList<TemperatureHistory> data=new ArrayList<>();
        data.add(new TemperatureHistory(36.5f,1497059007L));
        data.add(new TemperatureHistory(34.5f,1500059007L));
        data.add(new TemperatureHistory(35.7f,1501059007L));
        data.add(new TemperatureHistory(36.8f,1502059007L));
        data.add(new TemperatureHistory(37.2f,1503059007L));
        data.add(new TemperatureHistory(37.6f,1504059007L));
        data.add(new TemperatureHistory(38.2f,1505059007L));
        data.add(new TemperatureHistory(36.9f,1506059007L));
        return data;
    }
    public static ArrayList<BloodPressureHistory> getBloodPressureHistory(){
        ArrayList<BloodPressureHistory> data=new ArrayList<>();
        data.add(new BloodPressureHistory(75f,110f,1497059007L));
        data.add(new BloodPressureHistory(82f,130f,1500059007L));
        data.add(new BloodPressureHistory(79f,128f,1501059007L));
        data.add(new BloodPressureHistory(90f,125f,1502059007L));
        data.add(new BloodPressureHistory(85f,134f,1503059007L));
        data.add(new BloodPressureHistory(83f,117f,1504059007L));
        data.add(new BloodPressureHistory(77f,132f,1505059007L));
        data.add(new BloodPressureHistory(79f,127f,1506059007L));
        return data;
    }
    public static ArrayList<BloodSugarHistory> getBloodSugarHistory(){
        ArrayList<BloodSugarHistory> data=new ArrayList<>();
        data.add(new BloodSugarHistory(4.25f,1497059007L));
        data.add(new BloodSugarHistory(5.32f,1500059007L));
        data.add(new BloodSugarHistory(4.78f,1501059007L));
        data.add(new BloodSugarHistory(5.33f,1502059007L));
        data.add(new BloodSugarHistory(3.73f,1503059007L));
        data.add(new BloodSugarHistory(5.6f,1504059007L));
        data.add(new BloodSugarHistory(6.23f,1505059007L));
        data.add(new BloodSugarHistory(3.95f,1506059007L));
        return data;
    }
    public static ArrayList<BloodOxygenHistory> getBloodOxygenHistory(){
        ArrayList<BloodOxygenHistory> data=new ArrayList<>();
        data.add(new BloodOxygenHistory(95.3f,1497059007L));
        data.add(new BloodOxygenHistory(98f,1500059007L));
        data.add(new BloodOxygenHistory(93f,1501059007L));
        data.add(new BloodOxygenHistory(100f,1502059007L));
        data.add(new BloodOxygenHistory(96.5f,1503059007L));
        data.add(new BloodOxygenHistory(98.5f,1504059007L));
        data.add(new BloodOxygenHistory(97f,1505059007L));
        data.add(new BloodOxygenHistory(96f,1506059007L));
        return data;
    }
    public static ArrayList<CholesterolHistory> getCholesterolHistory(){
        ArrayList<CholesterolHistory> data=new ArrayList<>();
        data.add(new CholesterolHistory(5.3f,1497059007L));
        data.add(new CholesterolHistory(3.4f,1500059007L));
        data.add(new CholesterolHistory(5.4f,1501059007L));
        data.add(new CholesterolHistory(4.7f,1502059007L));
        data.add(new CholesterolHistory(6.5f,1503059007L));
        data.add(new CholesterolHistory(4.5f,1504059007L));
        data.add(new CholesterolHistory(3.5f,1505059007L));
        data.add(new CholesterolHistory(2.5f,1506059007L));
        return data;
    }
    public static ArrayList<BUA> getBUA(){
        ArrayList<BUA> data=new ArrayList<>();
        data.add(new BUA(135f,1497059007L));
        data.add(new BUA(234f,1500059007L));
        data.add(new BUA(394f,1501059007L));
        data.add(new BUA(245f,1502059007L));
        data.add(new BUA(323f,1503059007L));
        data.add(new BUA(278f,1504059007L));
        data.add(new BUA(306f,1505059007L));
        data.add(new BUA(289f,1506059007L));
        return data;
    }
}
