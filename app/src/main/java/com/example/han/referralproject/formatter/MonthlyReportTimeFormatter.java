package com.example.han.referralproject.formatter;

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
    private String[] unweeks=new String[]{"第一周\n(未测量)","第二周\n(未测量)","第三周\n(未测量)","第四周\n(未测量)"};
    public MonthlyReportTimeFormatter(ArrayList<Entry> times) {
        this.gaoyas=times;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        if(v==-1){
            return "";
        }
        if(gaoyas.size()==1){
            if (gaoyas.get(0).getY()==0){
                return unweeks[0];
            }
            return weeks[0];
        }
        if(v>gaoyas.size()){
            return "";
        }
        if (gaoyas.get(((int) v)).getY()==0){
            return unweeks[((int) v)];
        }
        return weeks[(int) v];
    }
}
