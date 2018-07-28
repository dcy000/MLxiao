package com.gzq.test_all_devices.health_record.health_record_fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.gzq.test_all_devices.R;
import com.gzq.test_all_devices.formatte.MyMarkerView;
import com.gzq.test_all_devices.formatte.TimeFormatter;
import com.gzq.test_all_devices.health_record.HealthRecordActivity;
import com.gzq.test_all_devices.health_record_bean.BloodPressureHistory;

import java.util.ArrayList;

public class HealthRecordBloodpressureFragment extends BaseFragment {
    private View view;
    private TextView mColor1;
    private TextView mIndicator1;
    private TextView mColor2;
    private TextView mIndicator2;
    private LinearLayout mLlSecond;
    private LinearLayout mLlIndicator;
    private LineChart mChart;
    private RadioButton mRbKongfu;
    private RadioButton mRbOneHour;
    private RadioButton mRbTwoHour;
    private RadioGroup mRgXuetangTime;

    @Override
    protected int initLayout() {
        return R.layout.fragment_health_record;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mRbKongfu = (RadioButton) view.findViewById(R.id.rb_kongfu);
        mRbOneHour = (RadioButton) view.findViewById(R.id.rb_one_hour);
        mRbTwoHour = (RadioButton) view.findViewById(R.id.rb_two_hour);
        mRgXuetangTime = (RadioGroup) view.findViewById(R.id.rg_xuetang_time);
        mColor1 = (TextView) view.findViewById(R.id.color_1);
        mIndicator1 = (TextView) view.findViewById(R.id.indicator_1);
        mColor2 = (TextView) view.findViewById(R.id.color_2);
        mIndicator2 = (TextView) view.findViewById(R.id.indicator_2);
        mLlSecond = (LinearLayout) view.findViewById(R.id.ll_second);
        mLlIndicator = (LinearLayout) view.findViewById(R.id.ll_indicator);
        mChart = (LineChart) view.findViewById(R.id.chart);
        mRgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        mColor1.setBackgroundColor(getResources().getColor(R.color.node_color));
        mIndicator1.setText("高压(mmHg)");
        mColor2.setBackgroundColor(getResources().getColor(R.color.node2_color));
        mIndicator2.setText("低压(mmHg)");
        mLlSecond.setVisibility(View.VISIBLE);
        initChart();
    }

    private void initChart() {
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.animateX(2500);
        //禁止y轴缩放
        mChart.setScaleYEnabled(false);
        mChart.setExtraLeftOffset(50f);
        mChart.setExtraRightOffset(80f);
        mChart.setMaxVisibleValueCount(20);
        mChart.setNoDataText("");


        LimitLine ll1 = new LimitLine(130f, "130mmHg");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line1));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(20f);


        LimitLine ll2 = new LimitLine(90f, "90mmHg");
        ll2.setLineWidth(2f);
        ll2.setLineColor(getResources().getColor(R.color.picket_line1));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(20f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(4);

        LimitLine ll3 = new LimitLine(85f, "85mmHg");
        ll3.setLineWidth(2f);
        ll3.setLineColor(getResources().getColor(R.color.picket_line2));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll3.setTextSize(20f);


        LimitLine ll4 = new LimitLine(60f, "60mmHg");
        ll4.setLineWidth(2f);
        ll4.setLineColor(getResources().getColor(R.color.picket_line2));
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(18f);

        YAxis leftAxis = mChart.getAxisLeft();
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
        mChart.getAxisRight().setEnabled(false);
    }

    public void refreshData(ArrayList<BloodPressureHistory> response, String temp) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> colors1 = new ArrayList<>();
        ArrayList<Integer> colors2 = new ArrayList<>();

        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).high_pressure > 130 || response.get(i).high_pressure < 90) {
                colors1.add(Color.RED);
            } else {
                colors1.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
            }

            if (response.get(i).low_pressure > 85 || response.get(i).low_pressure < 60) {
                colors2.add(Color.RED);
            } else {
                colors2.add(getResources().getColor(R.color.node2_color));
            }
            yVals1.add(new Entry(i, response.get(i).high_pressure));
            yVals2.add(new Entry(i, response.get(i).low_pressure));
            times.add(response.get(i).time);
        }
        if (times.size() != 0) {
            mChart.getXAxis().setValueFormatter(new TimeFormatter(times));
            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view, temp, times, response);
            mv.setChartView(mChart);
            mChart.setMarker(mv);


            LineDataSet set1, set2;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
                set1.setValues(yVals1);
                set2.setValues(yVals2);
                if (yVals1.size() <= 3)
                    set1.setMode(LineDataSet.Mode.LINEAR);
                if (yVals2.size() <= 3)
                    set2.setMode(LineDataSet.Mode.LINEAR);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(yVals1, "");
                //设置数据依赖左边的Y轴
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(getResources().getColor(R.color.line_color));
                set1.setValueTextColors(colors1);
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
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_xueya_gaoya);
                    set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.parseColor("#B3CBF8C3"));
                }
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
                set2.setValueTextColors(colors2);
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
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_xueya_diya);
                    set2.setFillDrawable(drawable);
                } else {
                    set2.setFillColor(Color.parseColor("#B3DCE2F3"));
                }
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
                mChart.setData(data);
            }
        }
    }

    public void refreshErrorData(String message) {
        ToastUtils.showShort(message);
        if (mChart != null) {
            mChart.setNoDataText(getResources().getString(R.string.noData));
            mChart.setData(null);
            mChart.invalidate();
        }
    }
}