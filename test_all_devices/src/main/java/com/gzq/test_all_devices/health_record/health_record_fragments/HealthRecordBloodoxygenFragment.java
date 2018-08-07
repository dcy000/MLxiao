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
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.gzq.test_all_devices.R;
import com.gzq.test_all_devices.formatte.MyMarkerView;
import com.gzq.test_all_devices.formatte.TimeFormatter;
import com.gzq.test_all_devices.health_record_bean.BloodOxygenHistory;

import java.util.ArrayList;

public class HealthRecordBloodoxygenFragment extends BluetoothBaseFragment {
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
        mIndicator1.setText("血氧");
        mLlSecond.setVisibility(View.GONE);
        initChart();

    }

    private void initChart() {
        //x轴右下角文字描述
        mChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        mChart.setTouchEnabled(true);

        // 启用坐标轴是否可以上下拖动
        mChart.setDragEnabled(true);
        //启用缩放
        mChart.setScaleEnabled(true);

        //禁止y轴缩放
        mChart.setScaleYEnabled(false);

        mChart.setExtraLeftOffset(50);
        mChart.setExtraRightOffset(80);
        mChart.setMaxVisibleValueCount(20);
        mChart.setNoDataText("");

        XAxis xAxis = mChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用绘制轴标签 //默认是true
        xAxis.setDrawLabels(true);
        //启用轴线实体线 //默认是true
        xAxis.setDrawAxisLine(true);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextSize(20);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(4);


        LimitLine ll1 = new LimitLine(94f, "最低94%");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.setAxisMinimum(90f);
        leftAxis.setAxisMaximum(101f);
        //网格线
        leftAxis.setDrawGridLines(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //启用零线
        leftAxis.setDrawZeroLine(false);

        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setTextSize(20f);
        //禁用右边的Y轴
        mChart.getAxisRight().setEnabled(false);
        mChart.animateX(2500);
    }

    public void refreshData(ArrayList<BloodOxygenHistory> response, String temp) {
        ArrayList<Entry> value = new ArrayList<Entry>();
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).blood_oxygen < 94) {
                colors.add(Color.RED);
            } else {
                colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
            }
            value.add(new Entry(i, response.get(i).blood_oxygen));
            times.add(response.get(i).time);
        }
        if (times.size() != 0) {
            mChart.getXAxis().setValueFormatter(new TimeFormatter(times));
            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view, temp, times);
            mv.setChartView(mChart); // For bounds control
            mChart.setMarker(mv); // Set the marker to the chart


            LineDataSet set1;
            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(value);
                if (value.size() <= 3)
                    set1.setMode(LineDataSet.Mode.LINEAR);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(value, "");
                set1.setDrawIcons(false);
                //设置选中指示线的样式
                set1.setHighLightColor(Color.rgb(244, 117, 117));
                //走势线的样式
                set1.setValueTextColors(colors);
                set1.setColor(getResources().getColor(R.color.line_color));
                set1.setCircleColor(getResources().getColor(R.color.node_color));

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
                //曲线区域颜色填充
                set1.setDrawFilled(false);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_tiwen);
                    set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.parseColor("#B3DCE2F3"));
                }
                if (value.size() <= 3)
                    set1.setMode(LineDataSet.Mode.LINEAR);
                else
                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
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
