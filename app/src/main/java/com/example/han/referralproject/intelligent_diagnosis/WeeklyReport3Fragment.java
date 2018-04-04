package com.example.han.referralproject.intelligent_diagnosis;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.WeeklyReport;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.view.progress.RxTextRoundProgressBar;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 应该把模型中的数据类型改成float的
 * A simple {@link Fragment} subclass.
 */
public class WeeklyReport3Fragment extends Fragment {
    @BindView(R.id.chart)
    RadarChart mChart;
    Unbinder unbinder;
    @BindView(R.id.tab_mb_yan)
    TextView tabMbYan;
    @BindView(R.id.tab_mb_yundong)
    TextView tabMbYundong;
    @BindView(R.id.tab_mb_tizhong)
    TextView tabMbTizhong;
    @BindView(R.id.tab_mb_yinjiu)
    TextView tabMbYinjiu;
    @BindView(R.id.tab_sj_yan)
    TextView tabSjYan;
    @BindView(R.id.tab_sj_yundong)
    TextView tabSjYundong;
    @BindView(R.id.tab_sj_tizhong)
    TextView tabSjTizhong;
    @BindView(R.id.tab_sj_yinjiu)
    TextView tabSjYinjiu;
    @BindView(R.id.img_yan)
    ImageView imgYan;
    @BindView(R.id.pc_yan)
    TextView pcYan;
    @BindView(R.id.img_yundong)
    ImageView imgYundong;
    @BindView(R.id.pc_yundong)
    TextView pcYundong;
    @BindView(R.id.img_tizhong)
    ImageView imgTizhong;
    @BindView(R.id.pc_tizhong)
    TextView pcTizhong;
    @BindView(R.id.img_yinjiu)
    ImageView imgYinjiu;
    @BindView(R.id.pc_yinjiu)
    TextView pcYinjiu;
    @BindView(R.id.rpb_sum)
    RxTextRoundProgressBar rpbSum;
    //    @BindView(R.id.tv_progress)
//    TextView tvProgress;
    @BindView(R.id.tv_progress3)
    TextView tvProgress3;
    private View view;
    private WeekReportModel.LastWeek data;
    private float sj_tizhong, sj_yundong, sj_yinjiu, sj_yan;
    private float mb_tizhong, mb_yundong, mb_yinjiu, mb_yan;
    private int pc_tizhong, pc_yundong, pc_yinjiu, pc_yan;
    private String tips = "未初始化";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weekly_report3, container, false);
//        data = (WeeklyReport) getArguments().getSerializable("data");
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    private void initData() {
        String height_s = LocalShared.getInstance(getActivity()).getUserHeight();
        float height_f = Float.parseFloat(height_s);
        sj_tizhong = Float.parseFloat(data.bmis) * (height_f / 100.0f) * (height_f / 100.0f);
        sj_yundong = Float.parseFloat(data.sportss);
        sj_yinjiu = Float.parseFloat(data.drinks);
        sj_yan = Float.parseFloat(data.nas);

        mb_tizhong = Float.parseFloat(data.bmim) * (height_f / 100.0f) * (height_f / 100.0f);
        mb_yundong = Float.parseFloat(data.sportsm);
        mb_yinjiu = Float.parseFloat(data.drinkm);
        mb_yan = Float.parseFloat(data.nam);

        pc_tizhong = (int) (sj_tizhong - mb_tizhong);
        pc_yundong = (int) (sj_yundong - mb_yundong);
        pc_yinjiu = (int) (sj_yinjiu - mb_yinjiu);
        pc_yan = (int) (sj_yan - mb_yan);


        tabMbYan.setText("<" + (int) mb_yan);
        tabMbYundong.setText(">" + (int) mb_yundong);
        tabMbTizhong.setText("<" + (int) mb_tizhong);
        tabMbYinjiu.setText("<" + (int) mb_yinjiu);

        tabSjYan.setText((int) sj_yan + "");
        tabSjYundong.setText((int) sj_yundong + "");
        tabSjTizhong.setText((int) sj_tizhong + "");
        tabSjYinjiu.setText((int) sj_yinjiu + "");

        if (pc_yan > 0) {
            imgYan.setImageResource(R.drawable.red_up);
            pcYan.setText((int) pc_yan + "");
        } else {
            imgYan.setVisibility(View.GONE);
            pcYan.setText("√");
            pcYan.setTextColor(Color.parseColor("#3CD478"));
        }

        if (pc_yundong < 0) {
            imgYundong.setImageResource(R.drawable.red_down);
            pcYundong.setText((int) pc_yundong + "");
        } else {
            imgYundong.setVisibility(View.GONE);
            pcYundong.setText("√");
            pcYundong.setTextColor(Color.parseColor("#3CD478"));
        }

        if (pc_tizhong > 0) {
            imgTizhong.setImageResource(R.drawable.red_up);
            pcTizhong.setText((int) pc_tizhong + "");
        } else {
            imgTizhong.setVisibility(View.GONE);
            pcTizhong.setText("√");
            pcTizhong.setTextColor(Color.parseColor("#3CD478"));
        }

        if (pc_yinjiu > 0) {
            imgYinjiu.setImageResource(R.drawable.red_up);
            pcYinjiu.setText((int) pc_yinjiu + "");
        } else {
            imgYinjiu.setVisibility(View.GONE);
            pcYinjiu.setText("√");
            pcYinjiu.setTextColor(Color.parseColor("#3CD478"));
        }

        rpbSum.setMax(100);
        float progress_percent = Float.parseFloat(data.zongw) * 100;
        rpbSum.setProgress(progress_percent);
        tvProgress3.setText((int) progress_percent + "%");

        tips = "主人，您的生活目标总体完成"
                + progress_percent + "%。食盐距离目标" + pc_yan + "克，运动距离目标" + pc_yundong +
                "分钟，体重距离目标" + pc_tizhong + "千克，饮酒距离目标" + pc_yinjiu + "毫升。";
    }

    private void setChart() {
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.getDescription().setEnabled(false);

        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.parseColor("#999999"));
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.parseColor("#999999"));
        mChart.setWebAlpha(100);
        mChart.setExtraOffsets(50, 50, 50, 50);

        MarkerView mv = new RadarMarkerView(getActivity(), R.layout.radar_markerview);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        setData();

        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(16f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] mActivities = new String[]{"体重（Kg）", "运动（min）", "饮酒（ml）", "盐（g）"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.parseColor("#333333"));

        YAxis yAxis = mChart.getYAxis();
        yAxis.setLabelCount(4, false);
        yAxis.setTextSize(16f);
        yAxis.setDrawLabels(false);
    }

    public void setData() {


        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();


        entries1.add(new RadarEntry(mb_tizhong));
        entries1.add(new RadarEntry(mb_yundong));
        entries1.add(new RadarEntry(mb_yinjiu));
        entries1.add(new RadarEntry(mb_yan));
        entries2.add(new RadarEntry(sj_tizhong));
        entries2.add(new RadarEntry(sj_yundong));
        entries2.add(new RadarEntry(sj_yinjiu));
        entries2.add(new RadarEntry(sj_yan));
        RadarDataSet set1 = new RadarDataSet(entries1, "");
//        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.parseColor("#49DF84"));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(1f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);
        //左下角指示器样式
        set1.setFormLineWidth(0f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
        set1.setFormSize(0f);

        RadarDataSet set2 = new RadarDataSet(entries2, "");
//        set2.setColor(Color.rgb(121, 162, 175));


        set2.setFillColor(Color.parseColor("#FF5747"));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(1f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        //左下角指示器样式
        set2.setFormLineWidth(0f);
        set2.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
        set2.setFormSize(0f);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setDrawValues(false);

        mChart.setData(data);
        mChart.invalidate();
    }

    public void notifyData(WeekReportModel.LastWeek weeklyReport) {
        this.data = weeklyReport;
        if (data != null) {
            initData();
            setChart();
        }

    }


    public static boolean isSpeak = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isSpeak) {
            isSpeak = false;
            ((WeeklyReportActivity) getActivity()).speak(tips);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
