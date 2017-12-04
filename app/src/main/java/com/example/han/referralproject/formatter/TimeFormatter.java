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
        return Utils.stampToDate(times.get((int) v));
    }
}