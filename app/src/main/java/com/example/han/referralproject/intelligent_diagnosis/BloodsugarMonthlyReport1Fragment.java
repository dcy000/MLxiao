package com.example.han.referralproject.intelligent_diagnosis;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.formatter.MonthlyReportTimeFormatter;
import com.example.han.referralproject.formatter.WeeklyReportTimeFormatter;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.littlejie.circleprogress.WaveProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class BloodsugarMonthlyReport1Fragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tvXueyaTitle;
    private TextView bloodsugarEmpty;
    private RxRoundProgressBar rpbBloodsugarEmpty;
    private TextView bloodsugarOne;
    private RxRoundProgressBar rpbBloodsugarOne;
    private TextView bloodsugarTwo;
    private RxRoundProgressBar rpbBloodsugarTwo;
    private WaveProgress waveProgressBar;
    private LinearLayout ll1;
    private RadioButton rbEmpty;
    private RadioButton rbOne;
    private RadioButton rbTwo;
    private LineChart weekXueyaChart;
    private TextView tvAdvice;
    private LinearLayout ll2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bloodsugar_weekly_report_fragment, container, false);
        initView(view);
        return view;
    }

    public void notifyData(WeeklyOrMonthlyBloodsugarReport report) {
        if (report == null) {
            return;
        }
        Double bloodSugarAvg = report.getBloodSugarAvg();
        if (bloodSugarAvg != null) {
            bloodsugarEmpty.setText(String.format("%.2f", bloodSugarAvg));
            rpbBloodsugarEmpty.setMax(10);
            rpbBloodsugarEmpty.setProgress(Float.parseFloat(bloodSugarAvg.toString()));
        } else {
            bloodsugarEmpty.setText("未测量");
        }
        Double bloodSugarOneAvg = report.getBloodSugarOneAvg();
        if (bloodSugarAvg != null) {
            bloodsugarOne.setText(String.format("%.2f", bloodSugarOneAvg));
            rpbBloodsugarOne.setMax(10);
            rpbBloodsugarOne.setProgress(Float.parseFloat(bloodSugarOneAvg.toString()));
        } else {
            bloodsugarOne.setText("未测量");
        }
        Double bloodSugarTwoAvg = report.getBloodSugarTwoAvg();
        if (bloodSugarTwoAvg != null) {
            bloodsugarTwo.setText(String.format("%.2f", bloodSugarTwoAvg));
            rpbBloodsugarTwo.setMax(10);
            rpbBloodsugarTwo.setProgress(Float.parseFloat(bloodSugarTwoAvg.toString()));
        } else {
            bloodsugarTwo.setText("未测量");
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
        setXueTangChart(report);
        StringBuffer buffer = new StringBuffer();
        String currentEmptyBloodsugar = "";
        if (bloodSugarAvg != null) {
            currentEmptyBloodsugar = String.format("%.2f", bloodSugarAvg);
        }
        if (TextUtils.isEmpty(report.getDiabetesLevel())) {
            buffer.append("您上月平均<strong><font color='#333333'>空腹血糖为" + currentEmptyBloodsugar
                    + "</font></strong>,血糖整体呈现<strong><font color='#333333'>" + report.getTrend()
                    + "</font></strong>," + "血糖控制" + report.getControl() + ",请每周定期测量，参照健康报告进行生活方式干预治疗。");
        } else {
            buffer.append("您上月平均<strong><font color='#333333'>空腹血糖为" + currentEmptyBloodsugar
                    + "</font></strong>,血糖整体呈现<strong><font color='#333333'>" + report.getTrend()
                    + "</font></strong>," + "血糖控制" + report.getControl() + ",初步诊断为<strong><font color='#333333'>" + report.getDiabetesLevel()
                    + "</font></strong>,请每周定期测量，参照健康报告进行生活方式干预治疗。");
        }
        tvAdvice.setText(Html.fromHtml(buffer.toString()));
    }

    /**
     * 血糖设置
     *
     * @param report
     */
    private void setXueTangChart(WeeklyOrMonthlyBloodsugarReport report) {

        //x轴右下角文字描述
        weekXueyaChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        weekXueyaChart.setTouchEnabled(true);

        // 启用坐标轴是否可以上下拖动
        weekXueyaChart.setDragEnabled(true);
        //启用缩放
        weekXueyaChart.setScaleEnabled(true);
        //禁止y轴缩放
        weekXueyaChart.setScaleYEnabled(false);

        weekXueyaChart.setExtraLeftOffset(50);
        weekXueyaChart.setExtraRightOffset(80);
        weekXueyaChart.setNoDataText("");


        XAxis xAxis = weekXueyaChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(20f);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(4);

        LimitLine ll1 = new LimitLine(7.0f, "7.0mmol/L");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(3.61f, "3.61mmol/L");
        ll2.setLineWidth(2f);
        ll2.setLineColor(getResources().getColor(R.color.picket_line));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = weekXueyaChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);

        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(10f);
        //网格线
        leftAxis.setDrawGridLines(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        //启用零线
        leftAxis.setDrawZeroLine(false);
        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        //禁用右边的Y轴
        weekXueyaChart.getAxisRight().setEnabled(false);
        weekXueyaChart.animateX(2500);

        ArrayList<Entry> value = new ArrayList<Entry>();
        ArrayList<Long> times = new ArrayList<>();
        List<WeeklyOrMonthlyBloodsugarReport.WeekDateListBean> weekDateList = report.getWeekDateList();
        if (weekDateList != null && weekDateList.size() > 0) {
            WeeklyOrMonthlyBloodsugarReport.WeekDateListBean weekDateListBean = weekDateList.get(0);
            if (weekDateListBean != null) {
                List<WeeklyOrMonthlyBloodsugarReport.WeekDateListBean.DetectionListBean> detectionList = weekDateListBean.getDetectionList();
                if (detectionList != null) {
                    for (int i = 0; i < detectionList.size(); i++) {
                        value.add(new Entry(i, Float.parseFloat(detectionList.get(i).getBloodSugar().toString())));
                        times.add(Long.parseLong(detectionList.get(i).getTimeStamp()));
                    }
                }
            }
        }
        if (times.size() != 0) {
            weekXueyaChart.getXAxis().setValueFormatter(new MonthlyReportTimeFormatter(value));
            setXuetang(value);
        }
    }

    private void setXuetang(ArrayList<Entry> values) {
        LineDataSet set1;
        if (weekXueyaChart.getData() != null &&
                weekXueyaChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) weekXueyaChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            weekXueyaChart.getData().notifyDataChanged();
            weekXueyaChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
            set1.setDrawIcons(false);

            //设置选中指示线的样式
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            //走势线的样式
            set1.setColor(getResources().getColor(R.color.line_color));
            set1.setCircleColor(getResources().getColor(R.color.node_color));
            set1.setValueTextColor(Color.parseColor("#3F86FC"));
            //走势线的粗细
            set1.setLineWidth(6f);
            //封顶圆圈的直径
            set1.setCircleRadius(8f);
            //是否镂空
            set1.setDrawCircleHole(true);
            set1.setCircleHoleRadius(4f);
            set1.setValueTextSize(18f);

            //左下角指示器样式
            set1.setFormLineWidth(0f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
            set1.setFormSize(0f);
//
            //曲线区域颜色填充
            set1.setDrawFilled(false);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            weekXueyaChart.setData(data);
        }
    }

    private void initView(View view) {
        tvXueyaTitle = (TextView) view.findViewById(R.id.tv_xueya_title);
        tvXueyaTitle.setText("上月平均血糖");
        bloodsugarEmpty = (TextView) view.findViewById(R.id.bloodsugar_empty);
        rpbBloodsugarEmpty = (RxRoundProgressBar) view.findViewById(R.id.rpb_bloodsugar_empty);
        bloodsugarOne = (TextView) view.findViewById(R.id.bloodsugar_one);
        rpbBloodsugarOne = (RxRoundProgressBar) view.findViewById(R.id.rpb_bloodsugar_one);
        bloodsugarTwo = (TextView) view.findViewById(R.id.bloodsugar_two);
        rpbBloodsugarTwo = (RxRoundProgressBar) view.findViewById(R.id.rpb_bloodsugar_two);
        waveProgressBar = (WaveProgress) view.findViewById(R.id.wave_progress_bar);
        ll1 = (LinearLayout) view.findViewById(R.id.ll_1);
        rbEmpty = (RadioButton) view.findViewById(R.id.rb_empty);
        rbEmpty.setOnClickListener(this);
        rbOne = (RadioButton) view.findViewById(R.id.rb_one);
        rbOne.setOnClickListener(this);
        rbTwo = (RadioButton) view.findViewById(R.id.rb_two);
        rbTwo.setOnClickListener(this);
        weekXueyaChart = (LineChart) view.findViewById(R.id.week_xueya_chart);
        tvAdvice = (TextView) view.findViewById(R.id.tv_advice);
        ll2 = (LinearLayout) view.findViewById(R.id.ll_2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rb_empty:
                break;
            case R.id.rb_one:
                break;
            case R.id.rb_two:
                break;
        }
    }
}
