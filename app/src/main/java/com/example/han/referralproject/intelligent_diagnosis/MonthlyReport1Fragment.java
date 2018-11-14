package com.example.han.referralproject.intelligent_diagnosis;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.MonthlyReport;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyReport1Fragment extends Fragment implements OnChartValueSelectedListener {
    @BindView(R.id.mChart)
    PieChart mChart;
    Unbinder unbinder;
    @BindView(R.id.tv_yinjiu_percent)
    TextView tvYinjiuPercent;
    @BindView(R.id.tv_yundong_percent)
    TextView tvYundongPercent;
    @BindView(R.id.tv_yan_percent)
    TextView tvYanPercent;
    @BindView(R.id.tv_tizhong_percent)
    TextView tvTizhongPercent;
    @BindView(R.id.tv_suggest)
    TextView tvSuggest;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_monthly_report1, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    private float bf_yinjiu, bf_yundong, bf_yan, bf_tizhong;
    private String tips = "";

    public void notifyData(MonthlyReport.MAPQ weeklyReport) {
        bf_yinjiu = Float.parseFloat(weeklyReport.drinkx);
        bf_yan = Float.parseFloat(weeklyReport.nax);
        bf_yundong = Float.parseFloat(weeklyReport.sportsx);
        bf_tizhong = Float.parseFloat(weeklyReport.bmix);

        List<HealthCompare> healthCompares = new ArrayList<>();
        healthCompares.add(new HealthCompare(bf_yinjiu, "饮酒"));
        healthCompares.add(new HealthCompare(bf_yan, "钠盐摄入量"));
        healthCompares.add(new HealthCompare(bf_yundong, "缺少运动"));
        healthCompares.add(new HealthCompare(bf_tizhong, "体重超标"));
        Collections.sort(healthCompares);

        tvYinjiuPercent.setText(bf_yinjiu * 100 + "%");
        tvYundongPercent.setText(bf_yundong * 100 + "%");
        tvYanPercent.setText(bf_yan * 100 + "%");
        tvTizhongPercent.setText(bf_tizhong * 100 + "%");
        tvSuggest.setText(tips = "主人，根据您这一个月的健康日记和测量数据，为您统计出影响血压的主要因素：\n1."
                + healthCompares.get(0).tips + "\n2." + healthCompares.get(1).tips + "\n3." + healthCompares.get(2).tips
                + "\n4." + healthCompares.get(3).tips);
        setChart(weeklyReport);
        ((MonthlyReportActivity) getActivity()).speak(tips);
    }

    private void setChart(MonthlyReport.MAPQ weeklyReport) {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(40f);
        mChart.setTransparentCircleRadius(45f);

        mChart.setDrawCenterText(false);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(weeklyReport);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.setDrawEntryLabels(false);
    }

    private void setData(MonthlyReport.MAPQ weeklyReport) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(bf_yinjiu));
        entries.add(new PieEntry(bf_yundong));
        entries.add(new PieEntry(bf_yan));
        entries.add(new PieEntry(bf_tizhong));
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5)));
//        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#69D53E"));
        colors.add(Color.parseColor("#F25252"));
        colors.add(Color.parseColor("#F1BB48"));
        colors.add(Color.parseColor("#3599EA"));

        dataSet.setColors(colors);
        //左下角指示器样式
        dataSet.setFormLineWidth(0f);
        dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
        dataSet.setFormSize(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {

    }
}
