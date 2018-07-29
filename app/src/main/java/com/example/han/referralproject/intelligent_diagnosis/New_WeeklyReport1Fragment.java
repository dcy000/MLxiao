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
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.formatter.WeeklyReportTimeFormatter;
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
 * Created by Administrator on 2018/5/9.
 */

public class New_WeeklyReport1Fragment extends Fragment {
    private View view;
    private TextView mTvGao;
    private RxRoundProgressBar mRpbGao;
    private TextView mTvDi;
    private RxRoundProgressBar mRpbDi;
    private WaveProgress mWaveProgressBar;
    private LineChart mWeekXueyaChart;
    private TextView mTvAdvice;
    private int fenshuNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_fragment_weekly_report1, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        mTvGao = (TextView) view.findViewById(R.id.tv_gao);
        mRpbGao = (RxRoundProgressBar) view.findViewById(R.id.rpb_gao);
        mTvDi = (TextView) view.findViewById(R.id.tv_di);
        mRpbDi = (RxRoundProgressBar) view.findViewById(R.id.rpb_di);
        mWaveProgressBar = (WaveProgress) view.findViewById(R.id.wave_progress_bar);
        mWeekXueyaChart = (LineChart) view.findViewById(R.id.week_xueya_chart);
        mTvAdvice = (TextView) view.findViewById(R.id.tv_advice);
    }

    public void notifyData(WeeklyOrMonthlyReport report) {
        if (report==null)
            return;
        String highPressureAvg = report.getHighPressureAvg();
        if (!TextUtils.isEmpty(highPressureAvg)){
            mTvGao.setText(highPressureAvg);
            mRpbGao.setMax(180);
            mRpbGao.setProgress(Float.parseFloat(highPressureAvg));
        }
        String lowPressureAvg = report.getLowPressureAvg();
        if (!TextUtils.isEmpty(lowPressureAvg)) {
            mTvDi.setText(lowPressureAvg);
            mRpbDi.setMax(100);
            mRpbDi.setProgress(Float.parseFloat(lowPressureAvg));
        }
        String healthScore = report.getHealthScore();
        if (!TextUtils.isEmpty(healthScore)){
            int int_healthScore= (int) Float.parseFloat(healthScore);
            if (int_healthScore>=80){
                mWaveProgressBar.setWaveDarkColor(Color.parseColor("#5BD78C"));
                mWaveProgressBar.setWaveLightColor(Color.parseColor("#86F77D"));
                mWaveProgressBar.setValueColor(Color.parseColor("#ffffff"));
            }else if(int_healthScore>=60){
                mWaveProgressBar.setWaveDarkColor(Color.parseColor("#F78237"));
                mWaveProgressBar.setWaveLightColor(Color.parseColor("#FBBF81"));
                mWaveProgressBar.setValueColor(Color.parseColor("#ffffff"));
            }else{
                mWaveProgressBar.setWaveDarkColor(Color.parseColor("#FE5848"));
                mWaveProgressBar.setWaveLightColor(Color.parseColor("#F88A78"));
                mWaveProgressBar.setValueColor(Color.parseColor("#FE5848"));
            }
            mWaveProgressBar.setMaxValue(100);
            mWaveProgressBar.setValue(int_healthScore);
            mWaveProgressBar.setHealthValue(int_healthScore + "分");
        }
        setXueyaChart(report);
        StringBuffer buffer=new StringBuffer();
        buffer.append("您上周平均<strong><font color='#333333'>血压为"+report.getWeekDateList().get(0).getLowPressureAvg()
                +"-"+report.getWeekDateList().get(0).getHighPressureAvg()
        +"</font></strong>,血压整体呈现<strong><font color='#333333'>"+report.getTrend()+"</font></strong>,初步诊断为<strong><font color='#333333'>"+report.getHypertensionLevel()
        +"</font></strong>,心血管发病风险为<strong><font color='#333333'>"+report.getHeartDanger()+"</font></strong>;上周血压控制<strong><font color='#333333'>"+report.getControl()+
        "</font></strong>,请每周定期测量，参照健康报告进行生活方式干预治疗。");
        mTvAdvice.setText(Html.fromHtml(buffer.toString()));
    }
    /**
     * 血压图的基本设置
     * @param report
     */
    private void setXueyaChart(WeeklyOrMonthlyReport report) {
        mWeekXueyaChart.getDescription().setEnabled(false);
        mWeekXueyaChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mWeekXueyaChart.setDragEnabled(false);
        mWeekXueyaChart.setScaleEnabled(false);
        mWeekXueyaChart.setDrawGridBackground(false);
        mWeekXueyaChart.setHighlightPerDragEnabled(true);
        mWeekXueyaChart.animateX(2500);
        //禁止y轴缩放
        mWeekXueyaChart.setScaleYEnabled(false);
        mWeekXueyaChart.setExtraLeftOffset(50f);
        mWeekXueyaChart.setExtraRightOffset(80f);
        mWeekXueyaChart.setMaxVisibleValueCount(20);
        mWeekXueyaChart.setNoDataText("");


        LimitLine ll1 = new LimitLine(130f, "");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line1));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(20f);


        LimitLine ll2 = new LimitLine(90f, "");
        ll2.setLineWidth(2f);
        ll2.setLineColor(getResources().getColor(R.color.picket_line1));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);


        XAxis xAxis = mWeekXueyaChart.getXAxis();
        xAxis.setTextSize(20f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);

        LimitLine ll3 = new LimitLine(85f, "");
        ll3.setLineWidth(2f);
        ll3.setLineColor(getResources().getColor(R.color.picket_line2));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll3.setTextSize(20f);


        LimitLine ll4 = new LimitLine(60f, "");
        ll4.setLineWidth(2f);
        ll4.setLineColor(getResources().getColor(R.color.picket_line2));
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(18f);

        YAxis leftAxis = mWeekXueyaChart.getAxisLeft();
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
        mWeekXueyaChart.getAxisRight().setEnabled(false);


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Long> times = new ArrayList<>();
        List<WeeklyOrMonthlyReport.WeekDateListBean.DetectionListBean> detectionList = report.getWeekDateList().get(0).getDetectionList();
        for (int i=0;i<detectionList.size();i++){
            int highPressure = detectionList.get(i).getHighPressure();
            int lowPressure = detectionList.get(i).getLowPressure();
            String time = detectionList.get(i).getTimeStamp();
            yVals1.add(new Entry(i, highPressure));
            yVals2.add(new Entry(i, lowPressure));
            times.add(Long.parseLong(time));
        }

        if (times.size() != 0) {
            mWeekXueyaChart.getXAxis().setValueFormatter(new WeeklyReportTimeFormatter(times));
//            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view, temp, times, response);
//            mv.setChartView(xueyaChart);
//            xueyaChart.setMarker(mv);
            setXueya(yVals1, yVals2);
        }
    }

    private void setXueya(ArrayList<Entry> yVals1, ArrayList<Entry> yVals2) {
        LineDataSet set1, set2;
        if (mWeekXueyaChart.getData() != null &&
                mWeekXueyaChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mWeekXueyaChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mWeekXueyaChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            if (yVals1.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            if (yVals2.size() <= 3)
                set2.setMode(LineDataSet.Mode.LINEAR);
            mWeekXueyaChart.getData().notifyDataChanged();
            mWeekXueyaChart.notifyDataSetChanged();
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
            mWeekXueyaChart.setData(data);
        }
    }
}
