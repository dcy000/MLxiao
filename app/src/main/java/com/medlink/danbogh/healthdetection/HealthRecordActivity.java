package com.medlink.danbogh.healthdetection;

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

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.Jiashuju;
import com.example.han.referralproject.bean.BUA;
import com.example.han.referralproject.bean.BloodOxygenHistory;
import com.example.han.referralproject.bean.BloodPressureHistory;
import com.example.han.referralproject.bean.BloodSugarHistory;
import com.example.han.referralproject.bean.CholesterolHistory;
import com.example.han.referralproject.bean.HeartRateHistory;
import com.example.han.referralproject.bean.PulseHistory;
import com.example.han.referralproject.bean.TemperatureHistory;
import com.example.han.referralproject.formatter.MyFloatNumFormatter;
import com.example.han.referralproject.formatter.TimeFormatter;
import com.example.han.referralproject.music.ToastUtils;
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
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HealthRecordActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.tiwen_chart)
    LineChart tiwenChart;
    @BindView(R.id.xueya_chart)
    LineChart xueyaChart;
    @BindView(R.id.rg_health_record)
    RadioGroup rgHealthRecord;
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
    @BindView(R.id.danguchun_chart)
    LineChart danguchunChart;
    @BindView(R.id.xueniaosuan_chart)
    LineChart xueniaosuanChart;
    @BindView(R.id.rb_record_cholesterol)
    RadioButton rbRecordCholesterol;
    @BindView(R.id.rb_record_bua)
    RadioButton rbRecordBua;
    @BindView(R.id.color_1)
    TextView color1;
    @BindView(R.id.indicator_1)
    TextView indicator1;
    @BindView(R.id.color_2)
    TextView color2;
    @BindView(R.id.indicator_2)
    TextView indicator2;
    @BindView(R.id.ll_second)
    LinearLayout llSecond;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;

    @BindView(R.id.one_week)
    RadioButton oneWeek;
    @BindView(R.id.one_month)
    RadioButton oneMonth;
    @BindView(R.id.one_season)
    RadioButton oneSeason;
    @BindView(R.id.one_year)
    RadioButton oneYear;
    @BindView(R.id.ll_time)
    RadioGroup llTime;
    @BindView(R.id.rb_kongfu)
    RadioButton rbKongfu;
    @BindView(R.id.rb_one_hour)
    RadioButton rbOneHour;
    @BindView(R.id.rb_two_hour)
    RadioButton rbTwoHour;
    @BindView(R.id.rg_xuetang_time)
    RadioGroup rgXuetangTime;
    private long currentTime=0L,weekAgoTime=0L,monthAgoTime,seasonAgoTime=0L,yearAgoTime=0L;
    private String temp = "1";//记录选择的标签,默认是1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏,7:胆固醇，8：血尿酸
    private int eatedTime=0;//默认空腹：0；饭后一小时：1；饭后两小时
    private int timeFlag=1;//默认最近一周：1；一个月：2；一季度：3；一年：4；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        mToolbar.setVisibility(View.VISIBLE);
        mUnbinder = ButterKnife.bind(this);
//        tvTitle.setText("健康档案");
        //默认选择第一个
        rgHealthRecord.check(R.id.rb_record_temperature);
        mTitleText.setText("历史测量");
        rbRecordTemperature.setOnClickListener(this);
        rbRecordBloodPressure.setOnClickListener(this);
        rbRecordBloodGlucose.setOnClickListener(this);
        rbRecordBloodOxygen.setOnClickListener(this);
        rbRecordHeartRate.setOnClickListener(this);
        rbRecordPulse.setOnClickListener(this);
        rbRecordCholesterol.setOnClickListener(this);
        rbRecordBua.setOnClickListener(this);
        llTime.setOnCheckedChangeListener(this);
        rbKongfu.setOnClickListener(this);
        rbOneHour.setOnClickListener(this);
        rbTwoHour.setOnClickListener(this);

        currentTime=System.currentTimeMillis();
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)-7);
        weekAgoTime=curr.getTimeInMillis();
        curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)-1);
        monthAgoTime=curr.getTimeInMillis();
        curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)-3);
        seasonAgoTime=curr.getTimeInMillis();
        curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)-1);
        yearAgoTime=curr.getTimeInMillis();

        getTiwen(weekAgoTime+"",currentTime+"");
    }

    @Override
    protected void backMainActivity() {
        ToastUtils.show("小意思");
    }

    /**
     * 体温图的设置
     */
    private void setTiwenChart() {

        //x轴右下角文字描述
        tiwenChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        tiwenChart.setTouchEnabled(true);

        //启用坐标轴是否可以上下拖动
        tiwenChart.setDragEnabled(true);
        //启用缩放
        tiwenChart.setScaleEnabled(true);
        //禁止y轴缩放
        tiwenChart.setScaleYEnabled(false);
        tiwenChart.setExtraLeftOffset(40);
        tiwenChart.setExtraRightOffset(80);
        //20个数据以后不再显示注释标签
        tiwenChart.setMaxVisibleValueCount(20);
        tiwenChart.setNoDataText(getResources().getString(R.string.noData));

        XAxis xAxis = tiwenChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用绘制轴标签 //默认是true
        xAxis.setDrawLabels(true);
        //启用轴线实体线 //默认是true
        xAxis.setDrawAxisLine(true);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        //缩放时候的粒度
        xAxis.setGranularity(1);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextSize(20f);
        //在可见范围只显示四个
        xAxis.setLabelCount(4);

        LimitLine ll1 = new LimitLine(37.2f, "最高体温(37.2)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(36f, "最低体温(36.0)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(getResources().getColor(R.color.picket_line));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = tiwenChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setAxisMinimum(35f);
        leftAxis.setTextSize(20f);


        //网格线
        leftAxis.setDrawGridLines(false);//不启用y轴的参考线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //启用零线
        leftAxis.setDrawZeroLine(false);

        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);

        //禁用右边的Y轴
        tiwenChart.getAxisRight().setEnabled(false);
        //动画时间
        tiwenChart.animateX(2500);

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
        xueyaChart.animateX(2500);
        //禁止y轴缩放
        xueyaChart.setScaleYEnabled(false);
        xueyaChart.setExtraLeftOffset(50f);
        xueyaChart.setExtraRightOffset(80f);
        xueyangChart.setNoDataText(getResources().getString(R.string.noData));

        LimitLine ll1 = new LimitLine(130f, "高压Max(130)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line1));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(20f);


        LimitLine ll2 = new LimitLine(90f, "高压Min(90)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(getResources().getColor(R.color.picket_line1));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);


        XAxis xAxis = xueyaChart.getXAxis();
        xAxis.setTextSize(20f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(4);

        LimitLine ll3 = new LimitLine(85f, "低压Max(85)");
        ll3.setLineWidth(2f);
        ll3.setLineColor(getResources().getColor(R.color.picket_line2));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll3.setTextSize(20f);


        LimitLine ll4 = new LimitLine(60f, "低压Min(60)");
        ll4.setLineWidth(2f);
        ll4.setLineColor(getResources().getColor(R.color.picket_line2));
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(18f);

        YAxis leftAxis = xueyaChart.getAxisLeft();
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

        // 启用坐标轴是否可以上下拖动
        xuetangChart.setDragEnabled(true);
        //启用缩放
        xuetangChart.setScaleEnabled(true);
        //禁止y轴缩放
        xuetangChart.setScaleYEnabled(false);
        xuetangChart.setExtraLeftOffset(50);
        xuetangChart.setExtraRightOffset(80);
        xuetangChart.setNoDataText(getResources().getString(R.string.noData));

        XAxis xAxis = xuetangChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(20f);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(4);

        LimitLine ll1 = new LimitLine(6.11f, "空腹Max(6.11)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(3.61f, "空腹Min(3.61)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(getResources().getColor(R.color.picket_line));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(18f);

//        LimitLine ll3 = new LimitLine(9.4f, "餐后1小时Max(9.4mmol/L)");
//        ll3.setLineWidth(2f);
//        ll3.setLineColor(Color.parseColor("#F0FC6D9A"));
//        ll3.enableDashedLine(10.0f, 10f, 0f);
//        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll3.setTextSize(10f);
//
//        LimitLine ll4 = new LimitLine(6.7f, "餐后1小时Min(6.7mmol/L)");
//        ll4.setLineWidth(2f);
//        ll4.setLineColor(Color.parseColor("#F0FC6D9A"));
//        ll4.enableDashedLine(10.0f, 10f, 0f);
//        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll4.setTextSize(10f);
//
//        LimitLine ll5 = new LimitLine(7.8f, "餐后2小时Max(7.8mmol/L)");
//        ll5.setLineWidth(2f);
//        ll5.setLineColor(Color.parseColor("#F0FC6D9A"));
//        ll5.enableDashedLine(10.0f, 10f, 0f);
//        ll5.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll5.setTextSize(10f);
//
//        LimitLine ll6 = new LimitLine(3.61f, "餐后2小时Min(3.61mmol/L)");
//        ll6.setLineWidth(2f);
//        ll6.setLineColor(Color.parseColor("#F0FC6D9A"));
//        ll6.enableDashedLine(10.0f, 10f, 0f);
//        ll6.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll6.setTextSize(10f);

        //Y轴设置
        YAxis leftAxis = xuetangChart.getAxisLeft();
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

        // if disabled, scaling can be done on x- and y-axis separately
        xueyangChart.setPinchZoom(false);
        //禁止y轴缩放
        xueyangChart.setScaleYEnabled(false);

        xueyangChart.setExtraLeftOffset(50);
        xueyangChart.setExtraRightOffset(80);
        xueyangChart.setMaxVisibleValueCount(20);
        xueyangChart.setVisibleXRangeMaximum(4);
        xueyangChart.setNoDataText(getResources().getString(R.string.noData));

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
        xAxis.setTextSize(20);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(4);


        LimitLine ll1 = new LimitLine(94f, "最低血氧饱和度(94%)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = xueyangChart.getAxisLeft();
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
        xueyangChart.getAxisRight().setEnabled(false);
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
        xinlvChart.setNoDataText(getResources().getString(R.string.noData));

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
        maiboChart.setNoDataText(getResources().getString(R.string.noData));

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

    /**
     * 设置胆固醇
     */
    private void setDanguchunChart() {

        //x轴右下角文字描述
        danguchunChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        danguchunChart.setTouchEnabled(true);

        danguchunChart.setDragEnabled(true);
        //启用缩放
        danguchunChart.setScaleEnabled(true);
        //禁止y轴缩放
        danguchunChart.setScaleYEnabled(false);
        danguchunChart.setExtraLeftOffset(40);
        danguchunChart.setExtraRightOffset(80);
        danguchunChart.setNoDataText(getResources().getString(R.string.noData));

        XAxis xAxis = danguchunChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        xAxis.setLabelCount(4);


        LimitLine ll1 = new LimitLine(2.9f, "成人Min(2.9)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#9CD793"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(6.0f, "成人Max(6.0)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#9CD793"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);

        LimitLine ll3 = new LimitLine(3.1f, "儿童Min(3.1)");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#6D80E2"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(18f);


        LimitLine ll4 = new LimitLine(5.2f, "儿童Max(5.2)");
        ll4.setLineWidth(2f);
        ll4.setLineColor(Color.parseColor("#6D80E2"));
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = danguchunChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);
        leftAxis.addLimitLine(ll4);

        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();

        //网格线
        leftAxis.setDrawGridLines(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        //启用零线
        leftAxis.setDrawZeroLine(false);

        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setTextSize(20f);

        //禁用右边的Y轴
        danguchunChart.getAxisRight().setEnabled(false);
        danguchunChart.animateX(2500);

    }

    /**
     * 设置血尿酸
     */
    private void setBUAChart() {

        //x轴右下角文字描述
        xueniaosuanChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        xueniaosuanChart.setTouchEnabled(true);

        //启用坐标轴是否可以上下拖动
        xueniaosuanChart.setDragEnabled(true);
        //启用缩放
        xueniaosuanChart.setScaleEnabled(true);
        //禁止y轴缩放
        xueniaosuanChart.setScaleYEnabled(false);
        xueniaosuanChart.setExtraLeftOffset(40);
        xueniaosuanChart.setExtraRightOffset(80);
        xueniaosuanChart.setNoDataText(getResources().getString(R.string.noData));

        XAxis xAxis = xueniaosuanChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        xAxis.setLabelCount(4);


        LimitLine ll1 = new LimitLine(149f, "男性Max(149)");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#CFD8F1"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(416f, "男性(416)");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#CFD8F1"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);

        LimitLine ll3 = new LimitLine(357f, "女性Max(357)");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#D3EFD0"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(18f);

        LimitLine ll4 = new LimitLine(89f, "女性Min(89)");
        ll4.setLineWidth(2f);
        ll4.setLineColor(Color.parseColor("#D3EFD0"));
        ll4.enableDashedLine(10.0f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll4.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = xueniaosuanChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);
        leftAxis.addLimitLine(ll4);
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setAxisMaximum(450);
        leftAxis.setAxisMinimum(70);
        //网格线
        leftAxis.setDrawGridLines(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //启用零线
        leftAxis.setDrawZeroLine(false);
        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setTextSize(20f);
        //禁用右边的Y轴
        xueniaosuanChart.getAxisRight().setEnabled(false);
        xueniaosuanChart.animateX(2500);

    }

    private void getTiwen(String start,String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("体温(℃)");
        llSecond.setVisibility(View.GONE);
        NetworkApi.getTemperatureHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<TemperatureHistory>>() {
            @Override
            public void onSuccess(ArrayList<TemperatureHistory> response) {
                ArrayList<Entry> values = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {

                    if (response.get(i).temper_ature > 37.2 || response.get(i).temper_ature < 36.0) {//超出正常范围的数据用红色表明
                        colors.add(Color.RED);
                    } else {
                        colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                    }
                    values.add(new Entry(i, response.get(i).temper_ature));
                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {
                    setTiwenChart();
                    tiwenChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(tiwenChart);
                    tiwenChart.setMarker(mv);
                }
                setTiwen(values, colors);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
                tiwenChart.setNoDataText(getResources().getString(R.string.noData));
                tiwenChart.setData(null);
                tiwenChart.invalidate();
            }
        });
    }

    private void getXueya(String start,String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("高压(mmHg)");
        color2.setBackgroundColor(getResources().getColor(R.color.node2_color));
        indicator2.setText("低压(mmHg)");
        llSecond.setVisibility(View.VISIBLE);

        NetworkApi.getBloodpressureHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<BloodPressureHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodPressureHistory> response) {
                ArrayList<Entry> yVals1 = new ArrayList<Entry>();
                ArrayList<Entry> yVals2 = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors1 = new ArrayList<>();
                ArrayList<Integer> colors2 = new ArrayList<>();

                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).high_pressure > 130 || response.get(i).high_pressure < 90 || response.get(i).low_pressure > 85 || response.get(i).low_pressure < 60) {
                        colors1.add(Color.RED);
                    } else {
                        colors1.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                    }
                    if(response.get(i).low_pressure>85||response.get(i).low_pressure<60){
                        colors2.add(Color.RED);
                    }else{
                        colors2.add(getResources().getColor(R.color.node2_color));
                    }
                    yVals1.add(new Entry(i, response.get(i).high_pressure));
                    times.add(response.get(i).time);
                }
                for (int i = 0; i < response.size(); i++) {
                    yVals2.add(new Entry(i, response.get(i).low_pressure));
                }
                if (times.size() != 0) {
                    setXueyaChart();
                    xueyaChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times, response);
                    mv.setChartView(xueyaChart);
                    xueyaChart.setMarker(mv);
                }
                setXueya(yVals1, yVals2, colors1, colors2);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
                xueyaChart.setNoDataText(getResources().getString(R.string.noData));
                xueyaChart.setData(null);
                xueyaChart.invalidate();
            }
        });
    }

    private void getXuetang(String start, String end, final int flag) {
        rgXuetangTime.setVisibility(View.VISIBLE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("血糖(mmol/L)");
        llSecond.setVisibility(View.GONE);
        NetworkApi.getBloodSugarHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<BloodSugarHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodSugarHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();

                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).blood_sugar > 6.11 || response.get(i).blood_sugar <3.61 ) {
                        colors.add(Color.RED);
                    } else {
                        colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                    }
                    switch (flag){
                        case 0:
                            if(response.get(i).sugar_time==0){
                                value.add(new Entry(i, response.get(i).blood_sugar));
                            }
                            break;
                        case 1:
                            if(response.get(i).sugar_time==1){
                                value.add(new Entry(i, response.get(i).blood_sugar));
                            }
                            break;
                        case 2:
                            if(response.get(i).sugar_time==2){
                                value.add(new Entry(i, response.get(i).blood_sugar));
                            }
                            break;
                    }

                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {
                    setXueTangChart();
                    xuetangChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xuetangChart); // For bounds control
                    xuetangChart.setMarker(mv); // Set the marker to the chart
                }

                setXuetang(value, colors);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
                xuetangChart.setNoDataText(getResources().getString(R.string.noData));
                xuetangChart.setData(null);
                xuetangChart.invalidate();
            }
        });
    }

    private void getXueyang(String start,String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("血氧");
        llSecond.setVisibility(View.GONE);
        NetworkApi.getBloodOxygenHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<BloodOxygenHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodOxygenHistory> response) {
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
                    setXueyangChart();
                    xueyangChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xueyangChart); // For bounds control
                    xueyangChart.setMarker(mv); // Set the marker to the chart
                }
                setXueyang(value, colors);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
                xueyangChart.setNoDataText(getResources().getString(R.string.noData));
                xueyangChart.setData(null);
                xueyangChart.invalidate();
            }
        });
    }

    private void getXinlv(String start,String end) {
        NetworkApi.getHeartRateHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<HeartRateHistory>>() {
            @Override
            public void onSuccess(ArrayList<HeartRateHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    value.add(new Entry(i, response.get(i).heart_rate));
                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {
                    setXinlvChart();
                    xinlvChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xinlvChart); // For bounds control
                    xinlvChart.setMarker(mv); // Set the marker to the chart
                }
                setXinlv(value);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
            }
        });
    }

    private void getMaibo(String start,String end) {
        NetworkApi.getPulseHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<PulseHistory>>() {
            @Override
            public void onSuccess(ArrayList<PulseHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    value.add(new Entry(i, response.get(i).pulse));
                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {
                    setMaiboChart();
                    maiboChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(maiboChart); // For bounds control
                    maiboChart.setMarker(mv); // Set the marker to the chart
                }
                setMaibo(value);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
            }
        });
    }

    private void getDangucun(String start,String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(Color.parseColor("#9CD793"));
        indicator1.setText("成人(mmol/L)");
        color2.setBackgroundColor(Color.parseColor("#6D80E2"));
        indicator2.setText("儿童(mmol/L)");
        llSecond.setVisibility(View.VISIBLE);
        NetworkApi.getCholesterolHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<CholesterolHistory>>() {
            @Override
            public void onSuccess(ArrayList<CholesterolHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).cholesterol < 2.9 || response.get(i).cholesterol > 6.0) {
                        colors.add(Color.RED);
                    } else {
                        colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                    }
                    value.add(new Entry(i, response.get(i).cholesterol));
                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {
                    setDanguchunChart();
                    danguchunChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(danguchunChart); // For bounds control
                    danguchunChart.setMarker(mv); // Set the marker to the chart
                }
                setDanguchun(value, colors);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
                danguchunChart.setNoDataText(getResources().getString(R.string.noData));
                danguchunChart.setData(null);
                danguchunChart.invalidate();
            }
        });
    }

    private void getXueniaosuan(String start,String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color2.setBackgroundColor(Color.parseColor("#9CD793"));
        indicator2.setText("女性");
        color1.setBackgroundColor(Color.parseColor("#6D80E2"));
        indicator1.setText("男性");
        llSecond.setVisibility(View.VISIBLE);

        NetworkApi.getBUAHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<BUA>>() {
            @Override
            public void onSuccess(ArrayList<BUA> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).uric_acid < 149 || response.get(i).uric_acid > 416) {
                        colors.add(Color.RED);
                    } else {
                        colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                    }
                    value.add(new Entry(i, response.get(i).uric_acid));
                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {
                    setBUAChart();
                    xueniaosuanChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xueniaosuanChart); // For bounds control
                    xueniaosuanChart.setMarker(mv); // Set the marker to the chart
                }
                setXueniaosuan(value, colors);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.show(message);
                xueniaosuanChart.setNoDataText(getResources().getString(R.string.noData));
                xueniaosuanChart.setData(null);
                xueniaosuanChart.invalidate();
            }
        });
    }

    /**
     * 设置体温的走势
     *
     * @param values
     */
    private void setTiwen(ArrayList<Entry> values, ArrayList<Integer> colors) {

        LineDataSet set1;
        if (tiwenChart.getData() != null &&
                tiwenChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) tiwenChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            tiwenChart.getData().notifyDataChanged();
            tiwenChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
            //不画节点上的图案
            set1.setDrawIcons(false);

            //设置选中指示线的样式
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setHighLightColor(Color.rgb(244, 117, 117));


            set1.setValueTextColors(colors);
            set1.setValueTextSize(18f);
            set1.setValueFormatter(new MyFloatNumFormatter(temp));

            //走势线的样式
//            set1.enableDashedLine(10f, 0f, 0f);
            //走势线的颜色
            set1.setColor(getResources().getColor(R.color.line_color));
            //节点圆圈的颜色
            set1.setCircleColor(getResources().getColor(R.color.node_color));

            //走势线的粗细
            set1.setLineWidth(6f);
            //封顶圆圈的直径
            set1.setCircleRadius(8f);
            //是否镂空
            set1.setDrawCircleHole(true);
            set1.setCircleHoleRadius(4f);


            //左下角指示器样式
            set1.setFormLineWidth(0f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
            set1.setFormSize(0f);
            //曲线区域颜色填充
            set1.setDrawFilled(false);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_tiwen);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.parseColor("#B3DCE2F3"));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            tiwenChart.setData(data);
        }
    }

    /**
     * 设置血压走势
     *
     * @param yVals1
     * @param yVals2
     */
    private void setXueya(ArrayList<Entry> yVals1, ArrayList<Entry> yVals2,ArrayList<Integer> colors1,ArrayList<Integer> colors2) {
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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_xueya_gaoya);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.parseColor("#B3CBF8C3"));
            }
            //左下角指示器样式
            set1.setFormLineWidth(0f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
            set1.setFormSize(0f);


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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_xueya_diya);
                set2.setFillDrawable(drawable);
            } else {
                set2.setFillColor(Color.parseColor("#B3DCE2F3"));
            }
            //左下角指示器样式
            set2.setFormLineWidth(0f);
            set2.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
            set2.setFormSize(0f);

            LineData data = new LineData(set1, set2);
            data.setValueTextSize(18f);
            xueyaChart.setData(data);
        }
    }

    /**
     * 设置血糖的走势
     *
     * @param values
     */
    private void setXuetang(ArrayList<Entry> values,ArrayList<Integer> colors) {

        LineDataSet set1;
        if (xuetangChart.getData() != null &&
                xuetangChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xuetangChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            xuetangChart.getData().notifyDataChanged();
            xuetangChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            xuetangChart.setData(data);
        }
    }

    /**
     * 设置血氧的走势
     *
     * @param values
     */
    private void setXueyang(ArrayList<Entry> values,ArrayList<Integer> colors) {

        LineDataSet set1;
        if (xueyangChart.getData() != null &&
                xueyangChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xueyangChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            xueyangChart.getData().notifyDataChanged();
            xueyangChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_tiwen);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.parseColor("#B3DCE2F3"));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            xueyangChart.setData(data);
        }
    }

    /**
     * 设置心率
     *
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
     *
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

    /**
     * 设置胆固醇
     *
     * @param values
     */
    private void setDanguchun(ArrayList<Entry> values,ArrayList<Integer> colors) {

        LineDataSet set1;
        if (danguchunChart.getData() != null &&
                danguchunChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) danguchunChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            danguchunChart.getData().notifyDataChanged();
            danguchunChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_tiwen);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.parseColor("#B3DCE2F3"));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            danguchunChart.setData(data);
        }
    }

    /**
     * 设置血尿酸
     *
     * @param values
     */
    private void setXueniaosuan(ArrayList<Entry> values,ArrayList<Integer> colors) {

        LineDataSet set1;
        if (xueniaosuanChart.getData() != null &&
                xueniaosuanChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xueniaosuanChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            xueniaosuanChart.getData().notifyDataChanged();
            xueniaosuanChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_tiwen);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.parseColor("#B3DCE2F3"));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            xueniaosuanChart.setData(data);
        }
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
                tiwenChart.setVisibility(View.VISIBLE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                danguchunChart.setVisibility(View.GONE);
                xueniaosuanChart.setVisibility(View.GONE);
                llTime.check(R.id.one_week);
                setTiwenChart();
                getTiwen(weekAgoTime+"",currentTime+"");
                break;
            case R.id.rb_record_blood_pressure://血压
                temp = "2";
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.VISIBLE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                danguchunChart.setVisibility(View.GONE);
                xueniaosuanChart.setVisibility(View.GONE);
                llTime.check(R.id.one_week);
                getXueya(weekAgoTime+"",currentTime+"");
                break;
            case R.id.rb_record_blood_glucose://血糖
                temp = "4";
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.VISIBLE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                danguchunChart.setVisibility(View.GONE);
                xueniaosuanChart.setVisibility(View.GONE);
                llTime.check(R.id.one_week);
                getXuetang(weekAgoTime+"",currentTime+"",eatedTime);
                break;
            case R.id.rb_record_blood_oxygen://血氧
                temp = "5";
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.VISIBLE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                danguchunChart.setVisibility(View.GONE);
                xueniaosuanChart.setVisibility(View.GONE);
                llTime.check(R.id.one_week);
                getXueyang(weekAgoTime+"",currentTime+"");
                break;
            case R.id.rb_record_heart_rate://心率
                temp = "3";
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.VISIBLE);
                maiboChart.setVisibility(View.GONE);
                danguchunChart.setVisibility(View.GONE);
                xueniaosuanChart.setVisibility(View.GONE);
                llTime.check(R.id.one_week);
                getXinlv(weekAgoTime+"",currentTime+"");
                break;
            case R.id.rb_record_pulse://脉搏
                temp = "6";
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.VISIBLE);
                danguchunChart.setVisibility(View.GONE);
                xueniaosuanChart.setVisibility(View.GONE);
                llTime.check(R.id.one_week);
                getMaibo(weekAgoTime+"",currentTime+"");
                break;
            case R.id.rb_record_cholesterol://胆固醇
                temp = "7";
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                danguchunChart.setVisibility(View.VISIBLE);
                xueniaosuanChart.setVisibility(View.GONE);
                getDangucun(weekAgoTime+"",currentTime+"");
                llTime.check(R.id.one_week);
                break;
            case R.id.rb_record_bua://血尿酸
                temp = "8";
                tiwenChart.setVisibility(View.GONE);
                xueyaChart.setVisibility(View.GONE);
                xuetangChart.setVisibility(View.GONE);
                xueyangChart.setVisibility(View.GONE);
                xinlvChart.setVisibility(View.GONE);
                maiboChart.setVisibility(View.GONE);
                danguchunChart.setVisibility(View.GONE);
                xueniaosuanChart.setVisibility(View.VISIBLE);
                getXueniaosuan(weekAgoTime+"",currentTime+"");
                llTime.check(R.id.one_week);
                break;
            case R.id.rb_kongfu:
                eatedTime=0;
                switch (timeFlag){
                    case 1://一周
                        getXuetang(weekAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 2://一个月
                        getXuetang(monthAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 3:
                        getXuetang(seasonAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 4:
                        getXuetang(yearAgoTime+"",currentTime+"",eatedTime);
                        break;
                }
                break;
            case R.id.rb_one_hour:
                eatedTime=1;
                switch (timeFlag){
                    case 1://一周
                        getXuetang(weekAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 2://一个月
                        getXuetang(monthAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 3:
                        getXuetang(seasonAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 4:
                        getXuetang(yearAgoTime+"",currentTime+"",eatedTime);
                        break;
                }
                break;
            case R.id.rb_two_hour:
                eatedTime=2;
                switch (timeFlag){
                    case 1://一周
                        getXuetang(weekAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 2://一个月
                        getXuetang(monthAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 3:
                        getXuetang(seasonAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case 4:
                        getXuetang(yearAgoTime+"",currentTime+"",eatedTime);
                        break;
                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.one_week://默认是1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏,7:胆固醇，8：血尿酸
                timeFlag=1;
                switch (temp){
                    case "1":
                        getTiwen(weekAgoTime+"",currentTime+"");
                        break;
                    case "2":
                        getXueya(weekAgoTime+"",currentTime+"");
                        break;
                    case "3":
                        getXinlv(weekAgoTime+"",currentTime+"");
                        break;
                    case "4":
                        getXuetang(weekAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case "5":
                        getXueyang(weekAgoTime+"",currentTime+"");
                        break;
                    case "6":
                        getMaibo(weekAgoTime+"",currentTime+"");
                        break;
                    case "7":
                        getDangucun(weekAgoTime+"",currentTime+"");
                        break;
                    case "8":
                        getXueniaosuan(weekAgoTime+"",currentTime+"");
                        break;
                }
                break;
            case R.id.one_month:
                timeFlag=2;
                switch (temp){
                    case "1":
                        getTiwen(monthAgoTime+"",currentTime+"");
                        break;
                    case "2":
                        getXueya(monthAgoTime+"",currentTime+"");
                        break;
                    case "3":
                        getXinlv(monthAgoTime+"",currentTime+"");
                        break;
                    case "4":
                        getXuetang(monthAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case "5":
                        getXueyang(monthAgoTime+"",currentTime+"");
                        break;
                    case "6":
                        getMaibo(monthAgoTime+"",currentTime+"");
                        break;
                    case "7":
                        getDangucun(monthAgoTime+"",currentTime+"");
                        break;
                    case "8":
                        getXueniaosuan(monthAgoTime+"",currentTime+"");
                        break;
                }
                break;
            case R.id.one_season:
                timeFlag=3;
                switch (temp){
                    case "1":
                        getTiwen(seasonAgoTime+"",currentTime+"");
                        break;
                    case "2":
                        getXueya(seasonAgoTime+"",currentTime+"");
                        break;
                    case "3":
                        getXinlv(seasonAgoTime+"",currentTime+"");
                        break;
                    case "4":
                        getXuetang(seasonAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case "5":
                        getXueyang(seasonAgoTime+"",currentTime+"");
                        break;
                    case "6":
                        getMaibo(seasonAgoTime+"",currentTime+"");
                        break;
                    case "7":
                        getDangucun(seasonAgoTime+"",currentTime+"");
                        break;
                    case "8":
                        getXueniaosuan(seasonAgoTime+"",currentTime+"");
                        break;
                }
                break;
            case R.id.one_year:
                timeFlag=4;
                switch (temp){
                    case "1":
                        getTiwen(yearAgoTime+"",currentTime+"");
                        break;
                    case "2":
                        getXueya(yearAgoTime+"",currentTime+"");
                        break;
                    case "3":
                        getXinlv(yearAgoTime+"",currentTime+"");
                        break;
                    case "4":
                        getXuetang(yearAgoTime+"",currentTime+"",eatedTime);
                        break;
                    case "5":
                        getXueyang(yearAgoTime+"",currentTime+"");
                        break;
                    case "6":
                        getMaibo(yearAgoTime+"",currentTime+"");
                        break;
                    case "7":
                        getDangucun(yearAgoTime+"",currentTime+"");
                        break;
                    case "8":
                        getXueniaosuan(yearAgoTime+"",currentTime+"");
                        break;
                }
                break;
        }
    }

}
