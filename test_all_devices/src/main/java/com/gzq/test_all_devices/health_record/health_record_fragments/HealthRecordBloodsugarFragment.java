package com.gzq.test_all_devices.health_record.health_record_fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.gzq.test_all_devices.R;
import com.gzq.test_all_devices.formatte.MyMarkerView;
import com.gzq.test_all_devices.formatte.TimeFormatter;
import com.gzq.test_all_devices.health_record_bean.BloodSugarHistory;

import java.util.ArrayList;

public class HealthRecordBloodsugarFragment extends BaseFragment implements View.OnClickListener {
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
    private int eatedTime = 0;//默认空腹：0；饭后一小时：1；饭后两小时


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
        mRbKongfu.setOnClickListener(this);
        mRbOneHour.setOnClickListener(this);
        mRbTwoHour.setOnClickListener(this);

        mRgXuetangTime.setVisibility(View.VISIBLE);
        //指示器的颜色
        mColor1.setBackgroundColor(getResources().getColor(R.color.node_color));
        mIndicator1.setText("血糖(mmol/L)");
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
        mChart.setNoDataText("");


        XAxis xAxis = mChart.getXAxis();
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

        LimitLine ll3 = new LimitLine(11.1f, "11.1mmol/L");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(10f);

        LimitLine ll4 = new LimitLine(3.61f, "3.61mmol/L");
        ll4.setLineWidth(2f);
        ll4.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll4.enableDashedLine(10.0f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll4.setTextSize(10f);

        LimitLine ll5 = new LimitLine(7.8f, "7.8mmol/L");
        ll5.setLineWidth(2f);
        ll5.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll5.enableDashedLine(10.0f, 10f, 0f);
        ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll5.setTextSize(10f);

        LimitLine ll6 = new LimitLine(3.61f, "3.61mmol/L");
        ll6.setLineWidth(2f);
        ll6.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll6.enableDashedLine(10.0f, 10f, 0f);
        ll6.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll6.setTextSize(10f);

        //Y轴设置
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        if (eatedTime == 0) {
            leftAxis.addLimitLine(ll1);
            leftAxis.addLimitLine(ll2);
        } else if (eatedTime == 1) {
            leftAxis.addLimitLine(ll3);
            leftAxis.addLimitLine(ll4);
        } else if (eatedTime == 2) {
            leftAxis.addLimitLine(ll5);
            leftAxis.addLimitLine(ll6);
        }

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
        mChart.getAxisRight().setEnabled(false);
        mChart.animateX(2500);
    }

    public void refreshData(ArrayList<BloodSugarHistory> response, String temp) {
        ArrayList<Entry> value = new ArrayList<Entry>();
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < response.size(); i++) {
            times.add(response.get(i).time);
            switch (eatedTime) {
                case 0://空腹
                    if (response.get(i).sugar_time == 0) {
                        value.add(new Entry(i, response.get(i).blood_sugar));
                        if (response.get(i).blood_sugar > 7.0 || response.get(i).blood_sugar < 3.61) {
                            colors.add(Color.RED);
                        } else {
                            colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                        }
                    }
                    break;
                case 1://饭后一小时
                    if (response.get(i).sugar_time == 1) {
                        times.add(response.get(i).time);
                        value.add(new Entry(i, response.get(i).blood_sugar));
                        if (response.get(i).blood_sugar > 11.1 || response.get(i).blood_sugar < 3.61) {
                            colors.add(Color.RED);
                        } else {
                            colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                        }
                    }
                    break;
                case 2://饭后两小时
                    if (response.get(i).sugar_time == 2) {
                        times.add(response.get(i).time);
                        value.add(new Entry(i, response.get(i).blood_sugar));
                        if (response.get(i).blood_sugar > 7.8 || response.get(i).blood_sugar < 3.61) {
                            colors.add(Color.RED);
                        } else {
                            colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                        }
                    }
                    break;
            }


        }

        if (value.size() != 0) {
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
                set1.setColor(getResources().getColor(R.color.line_color));
                set1.setCircleColor(getResources().getColor(R.color.node_color));
                set1.setValueTextColors(colors);
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
                if (value.size() <= 3)
                    set1.setMode(LineDataSet.Mode.LINEAR);
                else
                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);

                LineData data = new LineData(dataSets);
                mChart.setData(data);
            }
        } else {
            mChart.setNoDataText(getResources().getString(R.string.noData));
            mChart.setData(null);
            mChart.invalidate();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rb_kongfu:
                eatedTime = 0;
                if (bloodsugarSelectTime!=null){
                    bloodsugarSelectTime.requestData();
                }
                break;
            case R.id.rb_one_hour:
                eatedTime = 1;
                if (bloodsugarSelectTime!=null){
                    bloodsugarSelectTime.requestData();
                }
                break;
            case R.id.rb_two_hour:
                eatedTime = 2;
                if (bloodsugarSelectTime!=null){
                    bloodsugarSelectTime.requestData();
                }
                break;
        }
    }
    public interface BloodsugarSelectTime{
        void  requestData();
    }
    private BloodsugarSelectTime bloodsugarSelectTime;
    public void setRequestBloodsugarData(BloodsugarSelectTime bloodsugarSelectTime){
        this.bloodsugarSelectTime=bloodsugarSelectTime;
    }
}
