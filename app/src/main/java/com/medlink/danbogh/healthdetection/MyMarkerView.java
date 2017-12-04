
package com.medlink.danbogh.healthdetection;

import android.content.Context;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private String flag;
    private ArrayList<Long> times;
    public MyMarkerView(Context context, int layoutResource,String flag,ArrayList<Long> times) {
        super(context, layoutResource);
        this.flag=flag;
        this.times=times;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            switch (flag){
                case "1"://体温
                    tvContent.setText(com.example.han.referralproject.util.Utils.stampToDate(times.get((int) e.getX())) +"\n"+ e.getY()+"℃");
                    break;
                case "2"://血压
                    tvContent.setText(com.example.han.referralproject.util.Utils.stampToDate(times.get((int) e.getX())) +"  "+ e.getY()+"mmHg");
                    break;
                case "3"://心率
                    break;
                case "4"://血糖
                    tvContent.setText(com.example.han.referralproject.util.Utils.stampToDate(times.get((int) e.getX())) +"  "+ e.getY()+"mmol/L");
                    break;
                case "5"://血氧
                    tvContent.setText(com.example.han.referralproject.util.Utils.stampToDate(times.get((int) e.getX())) +"  "+ e.getY()+"%");
                    break;
                case "6"://脉搏
                    break;
            }

        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
