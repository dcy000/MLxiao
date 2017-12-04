package com.medlink.danbogh.healthdetection;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.BloodOxygenHistory;
import com.example.han.referralproject.bean.BloodPressureHistory;
import com.example.han.referralproject.bean.BloodSugarHistory;
import com.example.han.referralproject.bean.HeartRateHistory;
import com.example.han.referralproject.bean.PulseHistory;
import com.example.han.referralproject.bean.TemperatureHistory;
import com.example.han.referralproject.formatter.TimeFormatter;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HealthRecordActivity extends BaseActivity implements View.OnClickListener {

    private static final String UrlFormat = NetworkApi.BasicUrl + "/ZZB/br/cl?bid=%s&temp=%d";

    @BindView(R.id.tiwen_chart)
    LineChart tiwenChart;
    @BindView(R.id.xueya_chart)
    LineChart xueyaChart;
    @BindView(R.id.rg_health_record)
    RadioGroup rgHealthRecord;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    public Unbinder mUnbinder;
    @BindView(R.id.rb_record_temperature)
    RadioButton rbRecordTemperature;
    @BindView(R.id.rb_record_blood_pressure)
    RadioButton rbRecordBloodPressure;
    @BindView(R.id.rb_record_blood_glucose)
    RadioButton rbRecordBloodGlucose;
    @BindView(R.id.rb_record_blood_oxygen)
    RadioButton rbRecordBloodOxygen;
    @BindView(R.id.xuetang_chart)
    LineChart xuetangChart;
    @BindView(R.id.xueyang_chart)
    LineChart xueyangChart;
    @BindView(R.id.rb_record_heart_rate)
    RadioButton rbRecordHeartRate;
    @BindView(R.id.rb_record_pulse)
    RadioButton rbRecordPulse;
    @BindView(R.id.xinlv_chart)
    LineChart xinlvChart;
    @BindView(R.id.maibo_chart)
    LineChart maiboChart;

    private String temp = "1";//记录选择的标签,默认是1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        mUnbinder = ButterKnife.bind(this);
        tvTitle.setText("健康档案");
        //默认选择第一个
        rgHealthRecord.check(R.id.rb_record_temperature);
        setTiwenChart();
        getTiwen();
        rbRecordTemperature.setOnClickListener(this);
        rbRecordBloodPressure.setOnClickListener(this);
        rbRecordBloodGlucose.setOnClickListener(this);
        rbRecordBloodOxygen.setOnClickListener(this);
        rbRecordHeartRate.setOnClickListener(this);
        rbRecordPulse.setOnClickListener(this);
    }

    /**
     * 体温图的设置
     */
    private void setTiwenChart() {
        //x轴右下角文字描述
        tiwenChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        tiwenChart.setTouchEnabled(true);

        // enable scaling and dragging 启用坐标轴是否可以上下拖动
        tiwenChart.setDragEnabled(true);
        //启用缩放
        tiwenChart.setScaleEnabled(true);
        tiwenChart.setPinchZoom(false);
        //禁止y轴缩放
        tiwenChart.setScaleYEnabled(false);
        tiwenChart.setExtraLeftOffset(50);
        tiwenChart.setExtraRightOffset(80);


        XAxis xAxis = tiwenChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用绘制轴标签 //默认是true
        xAxis.setDrawLabels(true);
        //启用轴线实体线 //默认是true
        xAxis.setDrawAxisLine(true);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextSize(15);


        LimitLine ll1 = new LimitLine(37.2f, "最高体温(37.2℃)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(14f);


        LimitLine ll2 = new LimitLine(36f, "最低体温(36.0℃)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(14f);

        //Y轴设置
        YAxis leftAxis = tiwenChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setAxisMinimum(35f);
        leftAxis.setLabelCount(15);

//        leftAxis.setYOffset(20f);
        //网格线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置网格线的颜色
//        leftAxis.setGridColor(Color.RED);

        //启用零线
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setTextSize(15);

        //禁用右边的Y轴
        tiwenChart.getAxisRight().setEnabled(false);
        tiwenChart.animateX(2500);

    }

    /**
     * 设置体温的走势
     *
     * @param values
     */
    private void setTiwen(ArrayList<Entry> values) {

        LineDataSet set1;
        if (tiwenChart.getData() != null &&
                tiwenChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) tiwenChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            tiwenChart.getData().notifyDataChanged();
            tiwenChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "体温");
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"

            //设置选中指示线的样式
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setHighLightColor(Color.BLUE);


            //走势线的样式
            set1.enableDashedLine(10f, 0f, 0f);
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLACK);
            set1.setValueTextColor(Color.RED);
            //走势线的粗细
            set1.setLineWidth(3f);
            //封顶圆圈的直径
            set1.setCircleRadius(3f);
            //是否镂空
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);

            //左下角指示器样式
            set1.setFormLineWidth(4f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 0f}, 0f));
            set1.setFormSize(15f);
//
            //曲线区域颜色填充
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            tiwenChart.setData(data);
        }
    }

    /**
     * 血压图的基本设置
     */
    private void setXueyaChart() {
        xueyaChart.getDescription().setEnabled(false);

        // enable touch gestures
        xueyaChart.setTouchEnabled(true);

        xueyaChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        xueyaChart.setDragEnabled(true);
        xueyaChart.setScaleEnabled(true);
        xueyaChart.setDrawGridBackground(false);
        xueyaChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        xueyaChart.setPinchZoom(true);

        // add data
//        setData(20, 30);
        xueyaChart.animateX(2500);
        //禁止y轴缩放
        xueyaChart.setScaleYEnabled(false);
        xueyaChart.setExtraLeftOffset(50f);
        xueyaChart.setExtraRightOffset(80f);

        LimitLine ll1 = new LimitLine(130f, "高压Max(130mmHg)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);


        LimitLine ll2 = new LimitLine(90f, "高压Min(90mmHg)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(10f);


        XAxis xAxis = xueyaChart.getXAxis();
        xAxis.setTextSize(15f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        LimitLine ll3 = new LimitLine(85f, "低压Max(85mmHg)");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#34b87f"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll3.setTextSize(10f);


        LimitLine ll4 = new LimitLine(60f, "低压Min(60mmHg)");
        ll4.setLineWidth(2f);
        ll4.setLineColor(Color.parseColor("#34b87f"));
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(10f);

        YAxis leftAxis = xueyaChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);
        leftAxis.addLimitLine(ll4);
        leftAxis.setAxisMinimum(50f);
        leftAxis.setDrawLimitLinesBehindData(true);

        xueyaChart.getAxisRight().setEnabled(false);


    }

    /**
     * 血糖设置
     */
    private void setXueTangChart() {
        //x轴右下角文字描述
        xuetangChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        xuetangChart.setTouchEnabled(true);

        // enable scaling and dragging 启用坐标轴是否可以上下拖动
        xuetangChart.setDragEnabled(true);
        //启用缩放
        xuetangChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        xuetangChart.setPinchZoom(false);
        //禁止y轴缩放
        xuetangChart.setScaleYEnabled(false);
        xuetangChart.setExtraLeftOffset(50);
        xuetangChart.setExtraRightOffset(80);


        XAxis xAxis = xuetangChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用绘制轴标签 //默认是true
        xAxis.setDrawLabels(true);
        //启用轴线实体线 //默认是true
        xAxis.setDrawAxisLine(true);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextSize(15);
        xAxis.setGranularity(1);

        LimitLine ll1 = new LimitLine(6.11f, "空腹Max(6.11mmol/L)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);


        LimitLine ll2 = new LimitLine(3.61f, "空腹Min(3.61mmol/L)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        LimitLine ll3 = new LimitLine(9.4f, "餐后1小时Max(9.4mmol/L)");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(10f);

        LimitLine ll4 = new LimitLine(6.7f, "餐后1小时Min(6.7mmol/L)");
        ll4.setLineWidth(2f);
        ll4.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll4.enableDashedLine(10.0f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll4.setTextSize(10f);

        LimitLine ll5 = new LimitLine(7.8f, "餐后2小时Max(7.8mmol/L)");
        ll5.setLineWidth(2f);
        ll5.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll5.enableDashedLine(10.0f, 10f, 0f);
        ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll5.setTextSize(10f);

        LimitLine ll6 = new LimitLine(3.61f, "餐后2小时Min(3.61mmol/L)");
        ll6.setLineWidth(2f);
        ll6.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll6.enableDashedLine(10.0f, 10f, 0f);
        ll6.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll6.setTextSize(10f);

        //Y轴设置
        YAxis leftAxis = xuetangChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);
        leftAxis.addLimitLine(ll4);
        leftAxis.addLimitLine(ll5);
        leftAxis.addLimitLine(ll6);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(10f);
        //网格线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        //启用零线
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        //禁用右边的Y轴
        xuetangChart.getAxisRight().setEnabled(false);

        xuetangChart.animateX(2500);

    }

    /**
     * 血氧基本设置
     */
    private void setXueyangChart() {
        //x轴右下角文字描述
        xueyangChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        xueyangChart.setTouchEnabled(true);

        // enable scaling and dragging 启用坐标轴是否可以上下拖动
        xueyangChart.setDragEnabled(true);
        //启用缩放
        xueyangChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        xueyangChart.setPinchZoom(false);
        //禁止y轴缩放
        xueyangChart.setScaleYEnabled(false);

        xueyangChart.setExtraLeftOffset(50);
        xueyangChart.setExtraRightOffset(80);


        XAxis xAxis = xueyangChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用绘制轴标签 //默认是true
        xAxis.setDrawLabels(true);
        //启用轴线实体线 //默认是true
        xAxis.setDrawAxisLine(true);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextSize(15);
        xAxis.setGranularity(1);


        LimitLine ll1 = new LimitLine(94f, "最低血氧饱和度(94%)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        //Y轴设置
        YAxis leftAxis = xueyangChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
//        leftAxis.setAxisMaximum(40f);
        leftAxis.setAxisMinimum(90f);
        leftAxis.setAxisMaximum(100f);
//        leftAxis.setYOffset(20f);
        //网格线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置网格线的颜色
//        leftAxis.setGridColor(Color.RED);

        //启用零线
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        //禁用右边的Y轴
        xueyangChart.getAxisRight().setEnabled(false);

//        setData(45, 100);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        xueyangChart.animateX(2500);

    }

    /**
     * 设置心率
     */
    private void setXinlvChart() {
        //x轴右下角文字描述
        xinlvChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        xinlvChart.setTouchEnabled(true);

        // enable scaling and dragging 启用坐标轴是否可以上下拖动
        xinlvChart.setDragEnabled(true);
        //启用缩放
        xinlvChart.setScaleEnabled(true);
        xinlvChart.setPinchZoom(false);
        //禁止y轴缩放
        xinlvChart.setScaleYEnabled(false);
        xinlvChart.setExtraLeftOffset(50);
        xinlvChart.setExtraRightOffset(80);


        XAxis xAxis = xinlvChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用绘制轴标签 //默认是true
        xAxis.setDrawLabels(true);
        //启用轴线实体线 //默认是true
        xAxis.setDrawAxisLine(true);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextSize(15);


        LimitLine ll1 = new LimitLine(100f, "最高心率(100/分钟)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);


        LimitLine ll2 = new LimitLine(60f, "最低心率(60/分钟)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        //Y轴设置
        YAxis leftAxis = xinlvChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setAxisMinimum(50f);

//        leftAxis.setYOffset(20f);
        //网格线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置网格线的颜色
//        leftAxis.setGridColor(Color.RED);

        //启用零线
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        //禁用右边的Y轴
        xinlvChart.getAxisRight().setEnabled(false);
        xinlvChart.animateX(2500);

    }

    /**
     * 设置脉搏
     */
    private void setMaiboChart() {
        //x轴右下角文字描述
        maiboChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        maiboChart.setTouchEnabled(true);

        // enable scaling and dragging 启用坐标轴是否可以上下拖动
        maiboChart.setDragEnabled(true);
        //启用缩放
        maiboChart.setScaleEnabled(true);
        maiboChart.setPinchZoom(false);
        //禁止y轴缩放
        maiboChart.setScaleYEnabled(false);
        maiboChart.setExtraLeftOffset(50);
        maiboChart.setExtraRightOffset(80);


        XAxis xAxis = maiboChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用绘制轴标签 //默认是true
        xAxis.setDrawLabels(true);
        //启用轴线实体线 //默认是true
        xAxis.setDrawAxisLine(true);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextSize(15);


        LimitLine ll1 = new LimitLine(100f, "最高脉搏(100/分钟)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);


        LimitLine ll2 = new LimitLine(60f, "最低脉搏(60/分钟)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#F0FC6D9A"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        //Y轴设置
        YAxis leftAxis = maiboChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setAxisMinimum(50f);

//        leftAxis.setYOffset(20f);
        //网格线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置网格线的颜色
//        leftAxis.setGridColor(Color.RED);

        //启用零线
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        //禁用右边的Y轴
        maiboChart.getAxisRight().setEnabled(false);
        maiboChart.animateX(2500);

    }
    private void getTiwen() {
        NetworkApi.getTemperatureHistory(temp, new NetworkManager.SuccessCallback<ArrayList<TemperatureHistory>>() {
            @Override
            public void onSuccess(ArrayList<TemperatureHistory> response) {
                ArrayList<Entry> values = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    values.add(new Entry(i, response.get(i).temper_ature));
                    times.add(response.get(i).time);
                }
                tiwenChart.getXAxis().setValueFormatter(new TimeFormatter(times));

                MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                mv.setChartView(tiwenChart);
                tiwenChart.setMarker(mv);

                setTiwen(values);
            }
        });
    }

    private void getXueya() {
        NetworkApi.getBloodpressureHistory(temp, new NetworkManager.SuccessCallback<ArrayList<BloodPressureHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodPressureHistory> response) {
                ArrayList<Entry> yVals1 = new ArrayList<Entry>();
                ArrayList<Entry> yVals2 = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    yVals1.add(new Entry(i, response.get(i).high_pressure));
                    times.add(response.get(i).time);
                }
                for (int i = 0; i < response.size(); i++) {
                    yVals2.add(new Entry(i, response.get(i).low_pressure));
                }
                xueyaChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                mv.setChartView(xueyaChart);
                xueyaChart.setMarker(mv);

                setXueya(yVals1, yVals2);
            }
        });
    }

    private void getXuetang() {
        NetworkApi.getBloodSugarHistory(temp, new NetworkManager.SuccessCallback<ArrayList<BloodSugarHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodSugarHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    value.add(new Entry(i, response.get(i).blood_sugar));
                    times.add(response.get(i).time);
                }
                xuetangChart.getXAxis().setValueFormatter(new TimeFormatter(times));

                MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                mv.setChartView(xuetangChart); // For bounds control
                xuetangChart.setMarker(mv); // Set the marker to the chart

                setXuetang(value);
            }
        });
    }

    private void getXueyang() {
        NetworkApi.getBloodOxygenHistory(temp, new NetworkManager.SuccessCallback<ArrayList<BloodOxygenHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodOxygenHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    value.add(new Entry(i, response.get(i).blood_oxygen));
                    times.add(response.get(i).time);
                }
                xueyangChart.getXAxis().setValueFormatter(new TimeFormatter(times));

                MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                mv.setChartView(xueyangChart); // For bounds control
                xueyangChart.setMarker(mv); // Set the marker to the chart
                setXueyang(value);
            }
        });
    }

    private void getXinlv() {
        NetworkApi.getHeartRateHistory(temp, new NetworkManager.SuccessCallback<ArrayList<HeartRateHistory>>() {
            @Override
            public void onSuccess(ArrayList<HeartRateHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    value.add(new Entry(i, response.get(i).heart_rate));
                    times.add(response.get(i).time);
                }
                xinlvChart.getXAxis().setValueFormatter(new TimeFormatter(times));

                MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                mv.setChartView(xinlvChart); // For bounds control
                xinlvChart.setMarker(mv); // Set the marker to the chart
                setXinlv(value);
            }
        });
    }

    private void getMaibo() {
        NetworkApi.getPulseHistory(temp, new NetworkManager.SuccessCallback<ArrayList<PulseHistory>>() {
            @Override
            public void onSuccess(ArrayList<PulseHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    value.add(new Entry(i, response.get(i).pulse));
                    times.add(response.get(i).time);
                }
                maiboChart.getXAxis().setValueFormatter(new TimeFormatter(times));

                MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                mv.setChartView(maiboChart); // For bounds control
                maiboChart.setMarker(mv); // Set the marker to the chart
                setMaibo(value);
            }
        });
    }
    /**
     * 设置血压走势
     *
     * @param yVals1
     * @param yVals2
     */
    private void setXueya(ArrayList<Entry> yVals1, ArrayList<Entry> yVals2) {
        LineDataSet set1, set2, set3;

        if (xueyaChart.getData() != null &&
                xueyaChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xueyaChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) xueyaChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            xueyaChart.getData().notifyDataChanged();
            xueyaChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "高压");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.RED);
            set1.setValueTextColor(Color.RED);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);

            // create a dataset and give it a type
            set2 = new LineDataSet(yVals2, "低压");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.parseColor("#09814E"));
            set2.setValueTextColor(Color.parseColor("#09814E"));
            set2.setCircleColor(Color.BLACK);

            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(set1, set2);

            data.setValueTextSize(9f);

            // set data
            xueyaChart.setData(data);
        }
    }

    /**
     * 设置血糖的走势
     *
     * @param values
     */
    private void setXuetang(ArrayList<Entry> values) {

        LineDataSet set1;
        if (xuetangChart.getData() != null &&
                xuetangChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xuetangChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            xuetangChart.getData().notifyDataChanged();
            xuetangChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "血糖");
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"

            //设置选中指示线的样式
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setHighLightColor(Color.BLUE);


            //走势线的样式
            set1.enableDashedLine(10f, 0f, 0f);
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLACK);
            set1.setValueTextColor(Color.RED);
            //走势线的粗细
            set1.setLineWidth(3f);
            //封顶圆圈的直径
            set1.setCircleRadius(3f);
            //是否镂空
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);

            //左下角指示器样式
            set1.setFormLineWidth(4f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 0f}, 0f));
            set1.setFormSize(15f);
//
            //曲线区域颜色填充
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            xuetangChart.setData(data);
        }
    }

    /**
     * 设置血氧的走势
     *
     * @param values
     */
    private void setXueyang(ArrayList<Entry> values) {

        LineDataSet set1;
        if (xueyangChart.getData() != null &&
                xueyangChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xueyangChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            xueyangChart.getData().notifyDataChanged();
            xueyangChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "血氧");
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"

            //设置选中指示线的样式
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setHighLightColor(Color.BLUE);


            //走势线的样式
            set1.enableDashedLine(10f, 0f, 0f);
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLACK);
            set1.setValueTextColor(Color.RED);
            //走势线的粗细
            set1.setLineWidth(3f);
            //封顶圆圈的直径
            set1.setCircleRadius(3f);
            //是否镂空
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);

            //左下角指示器样式
            set1.setFormLineWidth(4f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 0f}, 0f));
            set1.setFormSize(15f);
//
            //曲线区域颜色填充
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            xueyangChart.setData(data);
        }
    }

    /**
     * 设置心率
     * @param values
     */
    private void setXinlv(ArrayList<Entry> values) {

        LineDataSet set1;
        if (xinlvChart.getData() != null &&
                xinlvChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xinlvChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            xinlvChart.getData().notifyDataChanged();
            xinlvChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "心率");
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"

            //设置选中指示线的样式
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setHighLightColor(Color.BLUE);


            //走势线的样式
            set1.enableDashedLine(10f, 0f, 0f);
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLACK);
            set1.setValueTextColor(Color.RED);
            //走势线的粗细
            set1.setLineWidth(3f);
            //封顶圆圈的直径
            set1.setCircleRadius(3f);
            //是否镂空
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);

            //左下角指示器样式
            set1.setFormLineWidth(4f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 0f}, 0f));
            set1.setFormSize(15f);
//
            //曲线区域颜色填充
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            xinlvChart.setData(data);
        }
    }
    /**
     * 设置脉搏
     * @param values
     */
    private void setMaibo(ArrayList<Entry> values) {

        LineDataSet set1;
        if (maiboChart.getData() != null &&
                maiboChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) maiboChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            maiboChart.getData().notifyDataChanged();
            maiboChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "脉搏");
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"

            //设置选中指示线的样式
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setHighLightColor(Color.BLUE);


            //走势线的样式
            set1.enableDashedLine(10f, 0f, 0f);
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLACK);
            set1.setValueTextColor(Color.RED);
            //走势线的粗细
            set1.setLineWidth(3f);
            //封顶圆圈的直径
            set1.setCircleRadius(3f);
            //是否镂空
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);

            //左下角指示器样式
            set1.setFormLineWidth(4f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 0f}, 0f));
            set1.setFormSize(15f);
//
            //曲线区域颜色填充
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            maiboChart.setData(data);
        }
    }
    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_home)
    public void onIvHomeClicked() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_record_temperature://体温
                temp = "1";
                rbRecordTemperature.setTextColor(Color.WHITE);
                rbRecordBloodPressure.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodGlucose.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodOxygen.setTextColor(Color.parseColor("#999999"));
                rbRecordHeartRate.setTextColor(Color.parseColor("#999999"));
                rbRecordPulse.setTextColor(Color.parseColor("#999999"));
                tiwenChart.setVisibility(View.VISIBLE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                setTiwenChart();
                getTiwen();
                break;
            case R.id.rb_record_blood_pressure://血压
                temp = "2";
                rbRecordTemperature.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodPressure.setTextColor(Color.WHITE);
                rbRecordBloodGlucose.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodOxygen.setTextColor(Color.parseColor("#999999"));
                rbRecordHeartRate.setTextColor(Color.parseColor("#999999"));
                rbRecordPulse.setTextColor(Color.parseColor("#999999"));
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.VISIBLE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                setXueyaChart();
                getXueya();
                break;
            case R.id.rb_record_blood_glucose://血糖
                temp = "4";
                rbRecordTemperature.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodPressure.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodGlucose.setTextColor(Color.WHITE);
                rbRecordBloodOxygen.setTextColor(Color.parseColor("#999999"));
                rbRecordHeartRate.setTextColor(Color.parseColor("#999999"));
                rbRecordPulse.setTextColor(Color.parseColor("#999999"));
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.VISIBLE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                setXueTangChart();
                getXuetang();
                break;
            case R.id.rb_record_blood_oxygen://血氧
                temp = "5";
                rbRecordTemperature.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodPressure.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodGlucose.setTextColor(Color.parseColor("#999999"));
                rbRecordHeartRate.setTextColor(Color.parseColor("#999999"));
                rbRecordPulse.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodOxygen.setTextColor(Color.WHITE);
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.VISIBLE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                setXueyangChart();
                getXueyang();
                break;
            case R.id.rb_record_heart_rate://心率
                temp = "3";
                rbRecordTemperature.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodPressure.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodGlucose.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodOxygen.setTextColor(Color.parseColor("#999999"));
                rbRecordHeartRate.setTextColor(Color.WHITE);
                rbRecordPulse.setTextColor(Color.parseColor("#999999"));
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.VISIBLE);
                maiboChart.setVisibility(View.GONE);
                setXinlvChart();
                getXinlv();
                break;
            case R.id.rb_record_pulse://脉搏
                temp = "6";
                rbRecordTemperature.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodPressure.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodGlucose.setTextColor(Color.parseColor("#999999"));
                rbRecordBloodOxygen.setTextColor(Color.parseColor("#999999"));
                rbRecordHeartRate.setTextColor(Color.parseColor("#999999"));
                rbRecordPulse.setTextColor(Color.WHITE);
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.VISIBLE);
                setMaiboChart();
                getMaibo();
                break;
        }

    }


}
