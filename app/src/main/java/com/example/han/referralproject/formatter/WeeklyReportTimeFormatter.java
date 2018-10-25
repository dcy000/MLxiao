package com.example.han.referralproject.formatter;

import com.gcml.common.utils.data.TimeUtils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by gzq on 2017/11/29.
 */

public class WeeklyReportTimeFormatter implements IAxisValueFormatter {
    private ArrayList<Long> times;

    public WeeklyReportTimeFormatter(ArrayList<Long> times) {
        this.times = times;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        if (v == -1) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        if (times.size() == 1) {
            return TimeUtils.milliseconds2String(times.get(0), format);
        }
        if (v > times.size()) {
            return "";
        }
        return TimeUtils.milliseconds2String(times.get((int) v), format);
    }
}
