package com.example.han.referralproject.formatter;

import com.example.han.referralproject.util.Utils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by gzq on 2017/11/29.
 */

public class TimeFormatter implements IAxisValueFormatter{
    private ArrayList<Long> times;
    public TimeFormatter(ArrayList<Long> times) {
        this.times=times;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        if(v==-1){
            return "";
        }
        if(times.size()==1){
            return Utils.stampToDate(times.get(0));
        }
        if(v>times.size()){
            return "";
        }
        return Utils.stampToDate(times.get((int) v));
    }
}
