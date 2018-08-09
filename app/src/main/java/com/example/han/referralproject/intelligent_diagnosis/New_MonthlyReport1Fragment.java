package com.example.han.referralproject.intelligent_diagnosis;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.formatter.MonthlyReportTimeFormatter;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.littlejie.circleprogress.WaveProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */

public class New_MonthlyReport1Fragment extends Fragment {
    private View view;
    private TextView tvGao;
    private RxRoundProgressBar rpbGao;
    private TextView tvDi;
    private RxRoundProgressBar rpbDi;
    private WaveProgress waveProgressBar;
    private LinearLayout ll1;
    private LineChart weekXueyaChart;
    private TextView tvAdvice;
    private LinearLayout ll2;
    private TextView tvXueyaTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_fragment_weekly_report1, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvGao = view.findViewById(R.id.tv_gao);
        rpbGao = view.findViewById(R.id.rpb_gao);
        tvDi = view.findViewById(R.id.tv_di);
        rpbDi = view.findViewById(R.id.rpb_di);
        waveProgressBar = view.findViewById(R.id.wave_progress_bar);
        ll1 = view.findViewById(R.id.ll_1);
        weekXueyaChart = view.findViewById(R.id.week_xueya_chart);
        tvAdvice = view.findViewById(R.id.tv_advice);
        ll2 = view.findViewById(R.id.ll_2);
        tvXueyaTitle = view.findViewById(R.id.tv_xueya_title);
        tvXueyaTitle.setText("上月平均血压");
    }

    public void notifyData(WeeklyOrMonthlyReport report) {
        if (report == null)
            return;
        String highPressureAvg = report.getHighPressureAvg();
        if (!TextUtils.isEmpty(highPressureAvg)) {
            tvGao.setText(highPressureAvg);
            rpbGao.setMax(180);
            rpbGao.setProgress(Float.parseFloat(highPressureAvg));
        }
        String lowPressureAvg = report.getLowPressureAvg();
        if (!TextUtils.isEmpty(lowPressureAvg)) {
            tvDi.setText(lowPressureAvg);
            rpbDi.setMax(100);
            rpbDi.setProgress(Float.parseFloat(lowPressureAvg));
        }
        String healthScore = report.getHealthScore();
        if (!TextUtils.isEmpty(healthScore)) {
            int int_healthScore = (int) Float.parseFloat(healthScore);
            if (int_healthScore >= 80) {
                waveProgressBar.setWaveDarkColor(Color.parseColor("#5BD78C"));
                waveProgressBar.setWaveLightColor(Color.parseColor("#86F77D"));
                waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
            } else if (int_healthScore >= 60) {
                waveProgressBar.setWaveDarkColor(Color.parseColor("#F78237"));
                waveProgressBar.setWaveLightColor(Color.parseColor("#FBBF81"));
                waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
            } else {
                waveProgressBar.setWaveDarkColor(Color.parseColor("#FE5848"));
                waveProgressBar.setWaveLightColor(Color.parseColor("#F88A78"));
                waveProgressBar.setValueColor(Color.parseColor("#FE5848"));
            }
            waveProgressBar.setMaxValue(100);
            waveProgressBar.setValue(int_healthScore);
            waveProgressBar.setHealthValue(int_healthScore + "分");
        }
        setXueyaChart(report);
        StringBuffer buffer = new StringBuffer();
        buffer.append("您上月平均<strong><font color='#333333'>血压为" + report.getLowPressureAvg()
                + "-" + report.getHighPressureAvg()
                + "</font></strong>,血压整体呈现<strong><font color='#333333'>" + report.getTrend() + "</font></strong>,初步诊断为<strong><font color='#333333'>" + report.getHypertensionLevel()
                + "</font></strong>,心血管发病风险为<strong><font color='#333333'>" + report.getHeartDanger() + "</font></strong>;上月血压控制<strong><font color='#333333'>" + report.getControl() +
                "</font></strong>,请每周定期测量，参照健康报告进行生活方式干预治疗。");
        tvAdvice.setText(Html.fromHtml(buffer.toString()));
    }


    /**
     * 血压图的基本设置
     *
     * @param report
     */
    private void setXueyaChart(WeeklyOrMonthlyReport report) {
        weekXueyaChart.getDescription().setEnabled(false);
        weekXueyaChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        weekXueyaChart.setDragEnabled(false);
        weekXueyaChart.setScaleEnabled(false);
        weekXueyaChart.setDrawGridBackground(false);
        weekXueyaChart.setHighlightPerDragEnabled(true);
        weekXueyaChart.animateX(2500);
        //禁止y轴缩放
        weekXueyaChart.setScaleYEnabled(false);
        weekXueyaChart.setExtraLeftOffset(50f);
        weekXueyaChart.setExtraRightOffset(80f);
        weekXueyaChart.setMaxVisibleValueCount(20);
        weekXueyaChart.setNoDataText("");


        LimitLine ll1 = new LimitLine(130f, "");
        ll1.setLineWidth(2f);
        if (isAdded()) {
            ll1.setLineColor(getResources().getColor(R.color.picket_line1));
        }
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(20f);


        LimitLine ll2 = new LimitLine(90f, "");
        ll2.setLineWidth(2f);
        if (isAdded()) {
            ll2.setLineColor(getResources().getColor(R.color.picket_line1));
        }
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);


        XAxis xAxis = weekXueyaChart.getXAxis();
        xAxis.setTextSize(20f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(4);

        LimitLine ll3 = new LimitLine(85f, "");
        ll3.setLineWidth(2f);
        if (isAdded()) {
            ll3.setLineColor(getResources().getColor(R.color.picket_line2));
        }
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll3.setTextSize(20f);


        LimitLine ll4 = new LimitLine(60f, "");
        ll4.setLineWidth(2f);
        if (isAdded()) {
            ll4.setLineColor(getResources().getColor(R.color.picket_line2));
        }
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(18f);

        YAxis leftAxis = weekXueyaChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);
        leftAxis.addLimitLine(ll4);
        leftAxis.setAxisMinimum(50f);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setTextSize(20f);
        weekXueyaChart.getAxisRight().setEnabled(false);


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        List<WeeklyOrMonthlyReport.WeekDateListBean> weekDateList = report.getWeekDateList();
        if (weekDateList != null && weekDateList.size() > 0) {
            for (int i = 0; i < weekDateList.size(); i++) {
                String highPressureAvg = weekDateList.get(i).getHighPressureAvg();
                if (!TextUtils.isEmpty(highPressureAvg)) {
                    yVals1.add(new Entry(i, Float.parseFloat(highPressureAvg)));
                }
                String lowPressureAvg = weekDateList.get(i).getLowPressureAvg();
                if (!TextUtils.isEmpty(lowPressureAvg)) {
                    yVals2.add(new Entry(i, Float.parseFloat(lowPressureAvg)));
                }
            }
        }

        if (yVals1.size() != 0) {
            weekXueyaChart.getXAxis().setValueFormatter(new MonthlyReportTimeFormatter(yVals1));
            setXueya(yVals1, yVals2);
        }
    }

    private void setXueya(ArrayList<Entry> yVals1, ArrayList<Entry> yVals2) {
        LineDataSet set1, set2;
        if (weekXueyaChart.getData() != null &&
                weekXueyaChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) weekXueyaChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) weekXueyaChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            if (yVals1.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            if (yVals2.size() <= 3)
                set2.setMode(LineDataSet.Mode.LINEAR);
            weekXueyaChart.getData().notifyDataChanged();
            weekXueyaChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(yVals1, "");
            //设置数据依赖左边的Y轴
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.line_color));
            set1.setValueTextColor(Color.parseColor("#3F86FC"));
            set1.setCircleColor(getResources().getColor(R.color.node_color));
            set1.setLineWidth(6f);

            set1.setCircleRadius(8f);
            set1.setDrawCircleHole(true);
            set1.setCircleHoleRadius(4f);

            set1.setHighLightColor(Color.rgb(244, 117, 117));
            //设置直线圆滑过渡
//            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //曲线区域颜色填充
            set1.setDrawFilled(false);
            //左下角指示器样式
            set1.setFormLineWidth(0f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
            set1.setFormSize(0f);
            if (yVals1.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set2 = new LineDataSet(yVals2, "");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(getResources().getColor(R.color.line2_color));
            set2.setValueTextColor(Color.parseColor("#3ACC61"));
            set2.setCircleColor(getResources().getColor(R.color.node2_color));

            set2.setLineWidth(6f);
            set2.setCircleRadius(8f);
            set2.setDrawCircleHole(true);
            set2.setCircleHoleRadius(4f);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //设置直线圆滑过渡
//            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //曲线区域颜色填充
            set2.setDrawFilled(false);
            //左下角指示器样式
            set2.setFormLineWidth(0f);
            set2.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
            set2.setFormSize(0f);
            if (yVals2.size() <= 3)
                set2.setMode(LineDataSet.Mode.LINEAR);
            else
                set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData data = new LineData(set1, set2);
            data.setValueTextSize(18f);
            weekXueyaChart.setData(data);
        }
    }
}
