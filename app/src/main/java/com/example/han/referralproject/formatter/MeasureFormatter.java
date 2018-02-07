package com.example.han.referralproject.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by gzq on 2018/2/6.
 */

public class MeasureFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value==0){
            return "高压";
        }
        if (value==1){
            return "低压";
        }
        return "";
    }
}
