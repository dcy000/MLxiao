package com.gcml.module_health_record.others;

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
        switch (flag) {
            case "1":
                //体温
                mFormat = new DecimalFormat("#0.0");
                break;
            case "2":
                mFormat = new DecimalFormat("#0");
                break;
            case "3":
                //心率
                mFormat = new DecimalFormat("#0.0");
                break;
            case "4":
                //血糖
                mFormat = new DecimalFormat("#0.0");
                break;
            case "7":
                //胆固醇
                mFormat = new DecimalFormat("#0.00");
                break;
            default:
                break;
        }

    }


    @Override
    public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
        return mFormat.format(entry.getY());
    }
}
