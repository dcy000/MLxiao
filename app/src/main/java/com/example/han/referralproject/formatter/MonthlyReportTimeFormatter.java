package com.example.han.referralproject.formatter;

import com.example.han.referralproject.util.Utils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by gzq on 2017/11/29.
 */

public class MonthlyReportTimeFormatter implements IAxisValueFormatter{
    private ArrayList<Entry> gaoyas;
    private String[] weeks=new String[]{"第一周","第二周","第三周","第四周"};
    public MonthlyReportTimeFormatter(ArrayList<Entry> times) {
        this.gaoyas=times;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        if(v==-1){
            return "";
        }
        if(gaoyas.size()==1){
            return weeks[0];
        }
        if(v>gaoyas.size()){
            return "";
        }
        return weeks[(int) v];
    }
}
