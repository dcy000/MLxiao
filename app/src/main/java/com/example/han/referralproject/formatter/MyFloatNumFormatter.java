package com.example.han.referralproject.formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by gzq on 2017/12/5.
 */

public class MyFloatNumFormatter implements IValueFormatter {
    private DecimalFormat mFormat;

    public MyFloatNumFormatter(String flag) {
        switch (flag){
            case "1"://体温
                mFormat = new DecimalFormat("#0.0");
                break;
        }

    }


    @Override
    public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
        return mFormat.format(entry.getY());
    }
}
