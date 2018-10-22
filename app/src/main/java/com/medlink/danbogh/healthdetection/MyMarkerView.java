
package com.medlink.danbogh.healthdetection;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.BloodPressureHistory;
import com.gcml.lib_utils.data.TimeUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private final DecimalFormat mFormat;
    private TextView text_1, text_2, color_1, color_2, title_1, title_2, time;
    private LinearLayout ll2;
    private String flag;
    private ArrayList<Long> times;
    private ArrayList<BloodPressureHistory> xueya;

    public MyMarkerView(Context context, int layoutResource, String flag, ArrayList<Long> times) {
        super(context, layoutResource);
        this.flag = flag;
        this.times = times;
        text_1 = findViewById(R.id.text_1);
        text_2 = findViewById(R.id.text_2);
        ll2 = findViewById(R.id.ll2);
        color_1 = findViewById(R.id.color_1);
        color_2 = findViewById(R.id.color_2);
        title_1 = findViewById(R.id.title_1);
        title_2 = findViewById(R.id.title_2);
        time = findViewById(R.id.time);
        mFormat = new DecimalFormat("##0");
    }

    public MyMarkerView(Context context, int layoutResource, String flag, ArrayList<Long> times, ArrayList<BloodPressureHistory> xueya) {
        super(context, layoutResource);
        this.flag = flag;
        this.times = times;
        text_1 = findViewById(R.id.text_1);
        text_2 = findViewById(R.id.text_2);
        ll2 = findViewById(R.id.ll2);
        color_1 = findViewById(R.id.color_1);
        color_2 = findViewById(R.id.color_2);
        title_1 = findViewById(R.id.title_1);
        title_2 = findViewById(R.id.title_2);
        time = findViewById(R.id.time);
        mFormat = new DecimalFormat("##0");
        this.xueya = xueya;
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;

            text_1.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            switch (flag) {
                case "1"://体温
                    ll2.setVisibility(GONE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("体温");
                    text_1.setText(e.getY() + "");
                    break;
                case "2"://血压
                    ll2.setVisibility(VISIBLE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_gao));
                    color_2.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("收缩压");
                    title_2.setText("舒张压");
                    text_1.setText(xueya.get((int) e.getX()).high_pressure + "");
                    text_2.setText(xueya.get((int) e.getX()).low_pressure + "");
                    break;
                case "3"://心率
                    ll2.setVisibility(GONE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("心率");
                    text_1.setText(e.getY() + "");
                    break;
                case "4"://血糖
                    ll2.setVisibility(GONE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("血糖");
                    text_1.setText(e.getY() + "");
                    break;
                case "5"://血氧
                    ll2.setVisibility(GONE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("血氧");
                    text_1.setText(e.getY() + "");
                    break;
                case "6"://脉搏
                    break;
                case "7":
                    ll2.setVisibility(GONE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("胆固醇");
                    text_1.setText(String.format("%.2f", e.getY()));
                    break;
                case "8":
                    ll2.setVisibility(GONE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("血尿酸");
                    text_1.setText(e.getY() + "");
                    break;
                case "10":
                    ll2.setVisibility(GONE);
                    color_1.setBackground(getResources().getDrawable(R.drawable.xueya_diya));
                    title_1.setText("体重");
                    text_1.setText(e.getY() + "");
                    break;
            }
            time.setText(TimeUtils.milliseconds2String(times.get((int) e.getX()), new SimpleDateFormat("yyyy-MM-dd HH:mm")));

        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
