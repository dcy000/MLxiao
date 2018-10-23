package com.gcml.module_health_record.others;

import com.gcml.common.utils.data.TimeUtils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by gzq on 2017/11/29.
 */

public class TimeFormatter implements IAxisValueFormatter {
    private ArrayList<Long> times;

    public TimeFormatter(ArrayList<Long> times) {
        this.times = times;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        if (v == -1) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (times.size() == 1) {

            return TimeUtils.milliseconds2String(times.get(0),
                    format);
        }
        if (v > times.size()) {
            return "";
        }
        return TimeUtils.milliseconds2String(times.get((int) v),
                format);
    }
}
