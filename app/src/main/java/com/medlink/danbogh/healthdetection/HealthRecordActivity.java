package com.medlink.danbogh.healthdetection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.adapter.XindianAdapter;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.BUA;
import com.example.han.referralproject.bean.BloodOxygenHistory;
import com.example.han.referralproject.bean.BloodPressureHistory;
import com.example.han.referralproject.bean.BloodSugarHistory;
import com.example.han.referralproject.bean.CholesterolHistory;
import com.example.han.referralproject.bean.ECGHistory;
import com.example.han.referralproject.bean.HeartRateHistory;
import com.example.han.referralproject.bean.TemperatureHistory;
import com.example.han.referralproject.bean.WeightHistory;
import com.example.han.referralproject.formatter.MyFloatNumFormatter;
import com.example.han.referralproject.formatter.TimeFormatter;
import com.example.han.referralproject.intelligent_system.intelligent_diagnosis.MonthlyReportActivity;
import com.example.han.referralproject.intelligent_system.intelligent_diagnosis.WeeklyReportActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.ToastTool;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.UiUtils;
import com.ml.zxing.QrCodeUtils;

import java.util.ArrayList;
import java.util.Calendar;

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
    @BindView(R.id.xindiantu)
    RecyclerView xindianList;
    @BindView(R.id.rb_record_ecg)
    RadioButton getRbRecordECG;
    @BindView(R.id.tizhong_chart)
    LineChart tizhongChart;
    @BindView(R.id.rb_record_weight)
    RadioButton rbRecordWeight;
    @BindView(R.id.tv_record_qrcode)
    TextView tvRecordQrcode;

    private long currentTime = 0L, weekAgoTime = 0L, monthAgoTime, seasonAgoTime = 0L, yearAgoTime = 0L;
    private String temp = "1";//记录选择的标签,默认是1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏,7:胆固醇，8：血尿酸，9：心电
    private int eatedTime = 0;//默认空腹：0；饭后一小时：1；饭后两小时
    private int timeFlag = 1;//默认最近一周：1；一个月：2；一季度：3；一年：4；
    private int radioGroupPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        mToolbar.setVisibility(View.VISIBLE);
        mUnbinder = ButterKnife.bind(this);
        radioGroupPosition = getIntent().getIntExtra("position", 0);
        speak("主人，请查看历史记录");
//        tvTitle.setText("健康档案");

        mTitleText.setText(R.string.history_celiang);
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
        getRbRecordECG.setOnClickListener(this);
        rbRecordWeight.setOnClickListener(this);
        rbRecordHeartRate.setOnClickListener(this);

        currentTime = System.currentTimeMillis();
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) - 7);
        weekAgoTime = curr.getTimeInMillis();
        curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 1);
        monthAgoTime = curr.getTimeInMillis();
        curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 3);
        seasonAgoTime = curr.getTimeInMillis();
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) - 1);
        yearAgoTime = curr.getTimeInMillis();

        //默认选择第一个
        switch (radioGroupPosition) {
            case 0://体温
                rgHealthRecord.check(R.id.rb_record_temperature);
                clickTiwen();
                break;
            case 1://血压
                rgHealthRecord.check(R.id.rb_record_blood_pressure);
                clickXueya();
                break;
            case 2://血糖
                rgHealthRecord.check(R.id.rb_record_blood_glucose);
                clickXuetang();
                break;
            case 3://血氧
                rgHealthRecord.check(R.id.rb_record_blood_oxygen);
                clickXueyang();
                break;
            case 4://心跳
                rgHealthRecord.check(R.id.rb_record_heart_rate);
                clickXinlv();
                break;
            case 5://胆固醇
                rgHealthRecord.check(R.id.rb_record_cholesterol);
                clickDangu();
                break;
            case 6://血尿酸
                rgHealthRecord.check(R.id.rb_record_bua);
                clickNiaosuan();
                break;
            case 7://心电图
                rgHealthRecord.check(R.id.rb_record_ecg);
                clickXindian();
                break;
            case 8://体重
                rgHealthRecord.check(R.id.rb_record_weight);
                clickTizhong();
                break;
        }
//        getTiwen(weekAgoTime + "", currentTime + "");
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthRecordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvRecordQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = NetworkApi.BasicUrl + "/ZZB/br/whole_informations?bid=" + MyApplication.getInstance().userId + "&bname=" + MyApplication.getInstance().userName;
                MyDialogFragment.newInstance(text).show(getSupportFragmentManager(), MyDialogFragment.TAG);
            }
        });
    }

    @Override
    protected void backMainActivity() {

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
        tiwenChart.setNoDataText("");

        XAxis xAxis = tiwenChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        //缩放时候的粒度
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        //在可见范围只显示四个
        xAxis.setLabelCount(4);

        LimitLine ll1 = new LimitLine(37.2f, "37.2℃");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(36f, "36.0℃");
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
        leftAxis.setAxisMinimum(32f);
        leftAxis.setTextSize(20f);


        //网格线
        leftAxis.setDrawGridLines(false);//不启用y轴的参考线
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
        xueyaChart.setMaxVisibleValueCount(20);
        xueyaChart.setNoDataText("");


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


        XAxis xAxis = xueyaChart.getXAxis();
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
        xuetangChart.setNoDataText("");


        XAxis xAxis = xuetangChart.getXAxis();
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
        YAxis leftAxis = xuetangChart.getAxisLeft();
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
        xuetangChart.getAxisRight().setEnabled(false);
        xuetangChart.animateX(2500);

    }

    private void setXinlvChart() {

        //x轴右下角文字描述
        xinlvChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        xinlvChart.setTouchEnabled(true);

        //启用坐标轴是否可以上下拖动
        xinlvChart.setDragEnabled(true);
        //启用缩放
        xinlvChart.setScaleEnabled(true);
        //禁止y轴缩放
        xinlvChart.setScaleYEnabled(false);
        xinlvChart.setExtraLeftOffset(40);
        xinlvChart.setExtraRightOffset(80);
        //20个数据以后不再显示注释标签
        xinlvChart.setMaxVisibleValueCount(20);
        xinlvChart.setNoDataText("");

        XAxis xAxis = xinlvChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        //缩放时候的粒度
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        //在可见范围只显示四个
        xAxis.setLabelCount(4);

        LimitLine ll1 = new LimitLine(100f, "100次/分钟");
        ll1.setLineWidth(2f);
        ll1.setLineColor(getResources().getColor(R.color.picket_line));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(60f, "60次/分钟");
        ll2.setLineWidth(2f);
        ll2.setLineColor(getResources().getColor(R.color.picket_line));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = xinlvChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setAxisMinimum(50f);
        leftAxis.setTextSize(20f);


        //网格线
        leftAxis.setDrawGridLines(false);//不启用y轴的参考线
        //启用零线
        leftAxis.setDrawZeroLine(false);

        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);

        //禁用右边的Y轴
        xinlvChart.getAxisRight().setEnabled(false);
        //动画时间
        xinlvChart.animateX(2500);

    }

    /**
     * 血氧基本设置
     */
    private void setXueyangChart() {

        //x轴右下角文字描述
        xueyangChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        xueyangChart.setTouchEnabled(true);

        // 启用坐标轴是否可以上下拖动
        xueyangChart.setDragEnabled(true);
        //启用缩放
        xueyangChart.setScaleEnabled(true);

        //禁止y轴缩放
        xueyangChart.setScaleYEnabled(false);

        xueyangChart.setExtraLeftOffset(50);
        xueyangChart.setExtraRightOffset(80);
        xueyangChart.setMaxVisibleValueCount(20);
        xueyangChart.setNoDataText("");

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


        LimitLine ll1 = new LimitLine(94f, "最低94%");
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
        danguchunChart.setNoDataText("");

        XAxis xAxis = danguchunChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        xAxis.setLabelCount(4);


        LimitLine ll1 = new LimitLine(2.9f, "2.9mmol/L");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#9CD793"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(6.0f, "6.0mmol/L");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#9CD793"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);

        LimitLine ll3 = new LimitLine(3.1f, "3.1mmol/L");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#6D80E2"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(18f);


        LimitLine ll4 = new LimitLine(5.2f, "5.2mmol/L");
        ll4.setLineWidth(2f);
        ll4.setLineColor(Color.parseColor("#6D80E2"));
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = danguchunChart.getAxisLeft();
        leftAxis.setGranularity(0.01f);
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
        xueniaosuanChart.setNoDataText("");

        XAxis xAxis = xueniaosuanChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        xAxis.setLabelCount(4);


        LimitLine ll1 = new LimitLine(149f, "149μmol/L");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#CFD8F1"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(416f, "416μmol/L");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#CFD8F1"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);

        LimitLine ll3 = new LimitLine(357f, "357μmol/L");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#D3EFD0"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(18f);

        LimitLine ll4 = new LimitLine(89f, "89μmol/L");
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
        leftAxis.setAxisMaximum(500);
        leftAxis.setAxisMinimum(50);
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

    /**
     * 体温图的设置
     */
    private void setTizhongChart() {

        //x轴右下角文字描述
        tizhongChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        tizhongChart.setTouchEnabled(true);

        //启用坐标轴是否可以上下拖动
        tizhongChart.setDragEnabled(true);
        //启用缩放
        tizhongChart.setScaleEnabled(true);
        //禁止y轴缩放
        tizhongChart.setScaleYEnabled(false);
        tizhongChart.setExtraLeftOffset(40);
        tizhongChart.setExtraRightOffset(80);
        //20个数据以后不再显示注释标签
        tizhongChart.setMaxVisibleValueCount(20);
        tizhongChart.setNoDataText("");

        XAxis xAxis = tizhongChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        //缩放时候的粒度
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        //在可见范围只显示四个
        xAxis.setLabelCount(4);

//        LimitLine ll1 = new LimitLine(37.2f, "37.2℃");
//        ll1.setLineWidth(2f);
//        ll1.setLineColor(getResources().getColor(R.color.picket_line));
//        ll1.enableDashedLine(10.0f, 10f, 0f);
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll1.setTextSize(18f);
//
//
//        LimitLine ll2 = new LimitLine(36f, "36.0℃");
//        ll2.setLineWidth(2f);
//        ll2.setLineColor(getResources().getColor(R.color.picket_line));
//        ll2.enableDashedLine(10f, 10f, 0f);
//        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = tizhongChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setTextSize(20f);


        //网格线
        leftAxis.setDrawGridLines(false);//不启用y轴的参考线
        //启用零线
        leftAxis.setDrawZeroLine(false);

        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);

        //禁用右边的Y轴
        tizhongChart.getAxisRight().setEnabled(false);
        //动画时间
        tizhongChart.animateX(2500);

    }

    private void getTiwen(String start, String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("体温(℃)");
        llSecond.setVisibility(View.GONE);
        setTiwenChart();
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

                    tiwenChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(tiwenChart);
                    tiwenChart.setMarker(mv);
                    setTiwen(values, colors);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (tiwenChart != null) {
                    tiwenChart.setNoDataText(getResources().getString(R.string.noData));
                    tiwenChart.setData(null);
                    tiwenChart.invalidate();
                }
            }
        });
    }

    private void getXueya(String start, String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("高压(mmHg)");
        color2.setBackgroundColor(getResources().getColor(R.color.node2_color));
        indicator2.setText("低压(mmHg)");
        llSecond.setVisibility(View.VISIBLE);
        setXueyaChart();
        NetworkApi.getBloodpressureHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<BloodPressureHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodPressureHistory> response) {
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
                    xueyaChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times, response);
                    mv.setChartView(xueyaChart);
                    xueyaChart.setMarker(mv);
                    setXueya(yVals1, yVals2, colors1, colors2);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (xueyaChart != null) {
                    xueyaChart.setNoDataText(getResources().getString(R.string.noData));
                    xueyaChart.setData(null);
                    xueyaChart.invalidate();
                }
            }
        });
    }

    private void getXinlv(String start, String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("心率(次/分钟)");
        llSecond.setVisibility(View.GONE);
        setXinlvChart();
        NetworkApi.getHeartRateHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<HeartRateHistory>>() {
            @Override
            public void onSuccess(ArrayList<HeartRateHistory> response) {
                ArrayList<Entry> values = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {

                    if (response.get(i).heart_rate > 100 || response.get(i).heart_rate < 60) {//超出正常范围的数据用红色表明
                        colors.add(Color.RED);
                    } else {
                        colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
                    }
                    values.add(new Entry(i, response.get(i).heart_rate));
                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {
                    xinlvChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xinlvChart);
                    xinlvChart.setMarker(mv);
                    setXinlv(values, colors);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (xinlvChart != null) {
                    xinlvChart.setNoDataText(getResources().getString(R.string.noData));
                    xinlvChart.setData(null);
                    xinlvChart.invalidate();
                }
            }
        });
    }

    private void getXuetang(String start, String end, final int flag) {
        rgXuetangTime.setVisibility(View.VISIBLE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("血糖(mmol/L)");
        llSecond.setVisibility(View.GONE);
        setXueTangChart();
        NetworkApi.getBloodSugarHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<BloodSugarHistory>>() {
            @Override
            public void onSuccess(ArrayList<BloodSugarHistory> response) {
                ArrayList<Entry> value = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();

                for (int i = 0; i < response.size(); i++) {
                    times.add(response.get(i).time);
                    switch (flag) {
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
                    xuetangChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xuetangChart); // For bounds control
                    xuetangChart.setMarker(mv); // Set the marker to the chart
                    setXuetang(value, colors);
                } else {
                    xuetangChart.setNoDataText(getResources().getString(R.string.noData));
                    xuetangChart.setData(null);
                    xuetangChart.invalidate();
                }

            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (xuetangChart != null) {
                    xuetangChart.setNoDataText(getResources().getString(R.string.noData));
                    xuetangChart.setData(null);
                    xuetangChart.invalidate();
                }
            }
        });
    }

    private void getXueyang(String start, String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("血氧");
        llSecond.setVisibility(View.GONE);
        setXueyangChart();
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
                    xueyangChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xueyangChart); // For bounds control
                    xueyangChart.setMarker(mv); // Set the marker to the chart
                    setXueyang(value, colors);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (xueyangChart != null) {
                    xueyangChart.setNoDataText(getResources().getString(R.string.noData));
                    xueyangChart.setData(null);
                    xueyangChart.invalidate();
                }
            }
        });
    }

    private void getDangucun(String start, String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(Color.parseColor("#9CD793"));
        indicator1.setText("成人(mmol/L)");
        color2.setBackgroundColor(Color.parseColor("#6D80E2"));
        indicator2.setText("儿童(mmol/L)");
        llSecond.setVisibility(View.VISIBLE);
        setDanguchunChart();
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

                    danguchunChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(danguchunChart); // For bounds control
                    danguchunChart.setMarker(mv); // Set the marker to the chart
                    setDanguchun(value, colors);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (danguchunChart != null) {
                    danguchunChart.setNoDataText(getResources().getString(R.string.noData));
                    danguchunChart.setData(null);
                    danguchunChart.invalidate();
                }
            }
        });
    }

    private void getXueniaosuan(String start, String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color2.setBackgroundColor(Color.parseColor("#9CD793"));
        indicator2.setText("女性");
        color1.setBackgroundColor(Color.parseColor("#6D80E2"));
        indicator1.setText("男性");
        llSecond.setVisibility(View.VISIBLE);
        setBUAChart();
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
                    xueniaosuanChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(xueniaosuanChart); // For bounds control
                    xueniaosuanChart.setMarker(mv); // Set the marker to the chart
                    setXueniaosuan(value, colors);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (xueniaosuanChart != null) {
                    xueniaosuanChart.setNoDataText(getResources().getString(R.string.noData));
                    xueniaosuanChart.setData(null);
                    xueniaosuanChart.invalidate();
                }
            }
        });
    }

    private void getXindian(String start, String end) {

        NetworkApi.getECGHistory(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<ECGHistory>>() {
            @Override
            public void onSuccess(ArrayList<ECGHistory> response) {
                xindianList.setLayoutManager(new LinearLayoutManager(HealthRecordActivity.this));
                xindianList.setAdapter(new XindianAdapter(R.layout.item_message, response));
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
            }
        });
    }

    private void getTizhong(String start, String end) {
        rgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        color1.setBackgroundColor(getResources().getColor(R.color.node_color));
        indicator1.setText("体重(Kg)");
        llSecond.setVisibility(View.GONE);
        setTizhongChart();
        NetworkApi.getWeight(start, end, temp, new NetworkManager.SuccessCallback<ArrayList<WeightHistory>>() {
            @Override
            public void onSuccess(ArrayList<WeightHistory> response) {
                ArrayList<Entry> values = new ArrayList<Entry>();
                ArrayList<Long> times = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {

//                    if (response.get(i).temper_ature > 37.2 || response.get(i).temper_ature < 36.0) {//超出正常范围的数据用红色表明
//                        colors.add(Color.RED);
//                    } else {
                    colors.add(getResources().getColor(R.color.node_text_color));//正常字体的颜色
//                    }
                    values.add(new Entry(i, response.get(i).weight));
                    times.add(response.get(i).time);
                }
                if (times.size() != 0) {

                    tizhongChart.getXAxis().setValueFormatter(new TimeFormatter(times));
                    MyMarkerView mv = new MyMarkerView(HealthRecordActivity.this, R.layout.custom_marker_view, temp, times);
                    mv.setChartView(tizhongChart);
                    tizhongChart.setMarker(mv);
                    setTizhong(values, colors);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastTool.showShort(message);
                if (tizhongChart != null) {
                    tizhongChart.setNoDataText(getResources().getString(R.string.noData));
                    tizhongChart.setData(null);
                    tizhongChart.invalidate();
                }
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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
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
    private void setXueya(ArrayList<Entry> yVals1, ArrayList<Entry> yVals2, ArrayList<Integer> colors1, ArrayList<Integer> colors2) {
        LineDataSet set1, set2;

        if (xueyaChart.getData() != null &&
                xueyaChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xueyaChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) xueyaChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            if (yVals1.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            if (yVals2.size() <= 3)
                set2.setMode(LineDataSet.Mode.LINEAR);
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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_xueya_diya);
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
            xueyaChart.setData(data);
        }
    }

    private void setXinlv(ArrayList<Entry> values, ArrayList<Integer> colors) {

        LineDataSet set1;
        if (xinlvChart.getData() != null &&
                xinlvChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xinlvChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            xinlvChart.getData().notifyDataChanged();
            xinlvChart.notifyDataSetChanged();
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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
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
            xinlvChart.setData(data);
        }
    }

    /**
     * 设置血糖的走势
     *
     * @param values
     */
    private void setXuetang(ArrayList<Entry> values, ArrayList<Integer> colors) {

        LineDataSet set1;
        if (xuetangChart.getData() != null &&
                xuetangChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xuetangChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

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
    private void setXueyang(ArrayList<Entry> values, ArrayList<Integer> colors) {

        LineDataSet set1;
        if (xueyangChart.getData() != null &&
                xueyangChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xueyangChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            xueyangChart.setData(data);
        }
    }


    /**
     * 设置胆固醇
     *
     * @param values
     */
    private void setDanguchun(ArrayList<Entry> values, ArrayList<Integer> colors) {

        LineDataSet set1;
        if (danguchunChart.getData() != null &&
                danguchunChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) danguchunChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
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
            set1.setValueFormatter(new MyFloatNumFormatter(temp));
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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
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
    private void setXueniaosuan(ArrayList<Entry> values, ArrayList<Integer> colors) {

        LineDataSet set1;
        if (xueniaosuanChart.getData() != null &&
                xueniaosuanChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) xueniaosuanChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            xueniaosuanChart.setData(data);
        }
    }

    private void setTizhong(ArrayList<Entry> values, ArrayList<Integer> colors) {

        LineDataSet set1;
        if (tizhongChart.getData() != null &&
                tizhongChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) tizhongChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            tizhongChart.getData().notifyDataChanged();
            tizhongChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
            //不画节点上的图案
            set1.setDrawIcons(false);

            //设置选中指示线的样式
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setHighLightColor(Color.rgb(244, 117, 117));


            set1.setValueTextColors(colors);
            set1.setValueTextSize(18f);
//            set1.setValueFormatter(new MyFloatNumFormatter(temp));

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
            if (values.size() <= 3)
                set1.setMode(LineDataSet.Mode.LINEAR);
            else
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
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
            tizhongChart.setData(data);
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    private void clickTiwen() {
        temp = "1";
        tiwenChart.setVisibility(View.VISIBLE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.VISIBLE);
        llTime.check(R.id.one_week);
        getTiwen(weekAgoTime + "", currentTime + "");

    }

    private void clickXueya() {
        temp = "2";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.VISIBLE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.VISIBLE);
        llTime.check(R.id.one_week);
        getXueya(weekAgoTime + "", currentTime + "");
    }

    private void clickXinlv() {
        temp = "3";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.VISIBLE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.VISIBLE);
        llTime.check(R.id.one_week);
        getXinlv(weekAgoTime + "", currentTime + "");
    }

    private void clickXuetang() {
        temp = "4";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.VISIBLE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.VISIBLE);
        llTime.check(R.id.one_week);
        getXuetang(weekAgoTime + "", currentTime + "", eatedTime);
    }

    private void clickXueyang() {
        temp = "5";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.VISIBLE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.VISIBLE);
        llTime.check(R.id.one_week);
        getXueyang(weekAgoTime + "", currentTime + "");
    }

    private void clickDangu() {
        temp = "7";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.VISIBLE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.VISIBLE);
        getDangucun(weekAgoTime + "", currentTime + "");
        llTime.check(R.id.one_week);
    }

    private void clickNiaosuan() {
        temp = "8";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.VISIBLE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.VISIBLE);
        getXueniaosuan(weekAgoTime + "", currentTime + "");
        llTime.check(R.id.one_week);
    }

    private void clickXindian() {
        temp = "9";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.VISIBLE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.GONE);
        getXindian(weekAgoTime + "", currentTime + "");
        llTime.check(R.id.one_week);
    }

    private void clickTizhong() {
        temp = "10";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.VISIBLE);
        llIndicator.setVisibility(View.VISIBLE);
        getTizhong(weekAgoTime + "", currentTime + "");
        llTime.check(R.id.one_week);
    }

    private void clickQrcode() {
        temp = "11";
        tiwenChart.setVisibility(View.GONE);
        xueyaChart.setVisibility(View.GONE);
        xuetangChart.setVisibility(View.GONE);
        xueyangChart.setVisibility(View.GONE);
        xinlvChart.setVisibility(View.GONE);
        maiboChart.setVisibility(View.GONE);
        danguchunChart.setVisibility(View.GONE);
        xueniaosuanChart.setVisibility(View.GONE);
        xindianList.setVisibility(View.GONE);
        tizhongChart.setVisibility(View.GONE);
        llIndicator.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_record_temperature://体温
                clickTiwen();
                break;
            case R.id.rb_record_blood_pressure://血压
                clickXueya();
                break;
            case R.id.rb_record_heart_rate:
                clickXinlv();
                break;
            case R.id.rb_record_blood_glucose://血糖
                clickXuetang();
                break;
            case R.id.rb_record_blood_oxygen://血氧
                clickXueyang();
                break;

            case R.id.rb_record_cholesterol://胆固醇
                clickDangu();
                break;
            case R.id.rb_record_bua://血尿酸
                clickNiaosuan();
                break;
            case R.id.rb_record_ecg://心电图
                clickXindian();
                break;
            case R.id.rb_record_weight://体重
                clickTizhong();
                break;
            case R.id.rb_kongfu:
                eatedTime = 0;
                switch (timeFlag) {
                    case 1://一周
                        getXuetang(weekAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 2://一个月
                        getXuetang(monthAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 3:
                        getXuetang(seasonAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 4:
                        getXuetang(yearAgoTime + "", currentTime + "", eatedTime);
                        break;
                }
                break;
            case R.id.rb_one_hour:
                eatedTime = 1;
                switch (timeFlag) {
                    case 1://一周
                        getXuetang(weekAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 2://一个月
                        getXuetang(monthAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 3:
                        getXuetang(seasonAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 4:
                        getXuetang(yearAgoTime + "", currentTime + "", eatedTime);
                        break;
                }
                break;
            case R.id.rb_two_hour:
                eatedTime = 2;
                switch (timeFlag) {
                    case 1://一周
                        getXuetang(weekAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 2://一个月
                        getXuetang(monthAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 3:
                        getXuetang(seasonAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case 4:
                        getXuetang(yearAgoTime + "", currentTime + "", eatedTime);
                        break;
                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.one_week://默认是1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏,7:胆固醇，8：血尿酸
                timeFlag = 1;
                switch (temp) {
                    case "1":
                        getTiwen(weekAgoTime + "", currentTime + "");
                        break;
                    case "2":
                        getXueya(weekAgoTime + "", currentTime + "");
                        break;
                    case "3":
                        getXinlv(weekAgoTime + "", currentTime + "");
                        break;
                    case "4":
                        getXuetang(weekAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case "5":
                        getXueyang(weekAgoTime + "", currentTime + "");
                        break;
                    case "7":
                        getDangucun(weekAgoTime + "", currentTime + "");
                        break;
                    case "8":
                        getXueniaosuan(weekAgoTime + "", currentTime + "");
                        break;
                    case "9":
                        getXindian(weekAgoTime + "", currentTime + "");
                        break;
                    case "10":
                        getTizhong(weekAgoTime + "", currentTime + "");
                        break;
                }
                break;
            case R.id.one_month:
                timeFlag = 2;
                switch (temp) {
                    case "1":
                        getTiwen(monthAgoTime + "", currentTime + "");
                        break;
                    case "2":
                        getXueya(monthAgoTime + "", currentTime + "");
                        break;
                    case "3":
                        getXinlv(monthAgoTime + "", currentTime + "");
                        break;
                    case "4":
                        getXuetang(monthAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case "5":
                        getXueyang(monthAgoTime + "", currentTime + "");
                        break;
                    case "7":
                        getDangucun(monthAgoTime + "", currentTime + "");
                        break;
                    case "8":
                        getXueniaosuan(monthAgoTime + "", currentTime + "");
                        break;
                    case "9":
                        getXindian(monthAgoTime + "", currentTime + "");
                        break;
                    case "10":
                        getTizhong(monthAgoTime + "", currentTime + "");
                        break;
                }
                break;
            case R.id.one_season:
                timeFlag = 3;
                switch (temp) {
                    case "1":
                        getTiwen(seasonAgoTime + "", currentTime + "");
                        break;
                    case "2":
                        getXueya(seasonAgoTime + "", currentTime + "");
                        break;
                    case "3":
                        getXinlv(seasonAgoTime + "", currentTime + "");
                        break;
                    case "4":
                        getXuetang(seasonAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case "5":
                        getXueyang(seasonAgoTime + "", currentTime + "");
                        break;
                    case "7":
                        getDangucun(seasonAgoTime + "", currentTime + "");
                        break;
                    case "8":
                        getXueniaosuan(seasonAgoTime + "", currentTime + "");
                        break;
                    case "9":
                        getXindian(seasonAgoTime + "", currentTime + "");
                        break;
                    case "10":
                        getTizhong(seasonAgoTime + "", currentTime + "");
                        break;
                }
                break;
            case R.id.one_year:
                timeFlag = 4;
                switch (temp) {
                    case "1":
                        getTiwen(yearAgoTime + "", currentTime + "");
                        break;
                    case "2":
                        getXueya(yearAgoTime + "", currentTime + "");
                        break;
                    case "3":
                        getXinlv(yearAgoTime + "", currentTime + "");
                        break;
                    case "4":
                        getXuetang(yearAgoTime + "", currentTime + "", eatedTime);
                        break;
                    case "5":
                        getXueyang(yearAgoTime + "", currentTime + "");
                        break;
                    case "7":
                        getDangucun(yearAgoTime + "", currentTime + "");
                        break;
                    case "8":
                        getXueniaosuan(yearAgoTime + "", currentTime + "");
                        break;
                    case "9":
                        getXindian(yearAgoTime + "", currentTime + "");
                        break;
                    case "10":
                        getTizhong(yearAgoTime + "", currentTime + "");
                        break;
                }
                break;
        }
    }


    public static class MyDialogFragment extends DialogFragment {
        private static final String TAG = "QrcodeDialog";

        private View mView;
        private ImageView ivQrcode;

        private String text;

        private float dimAmount;
        private boolean showBottom;
        private boolean cancelable;

        public static MyDialogFragment newInstance(String text) {
            return newInstance(text, 0f, false, true);
        }

        public static MyDialogFragment newInstance(
                String text,
                float dimAmount,
                boolean showBottom,
                boolean cancelable) {
            Bundle args = new Bundle();
            args.putString("text", text);
            args.putFloat("dimAmount", dimAmount);
            args.putBoolean("showBottom", showBottom);
            args.putBoolean("cancelable", cancelable);
            MyDialogFragment fragment = new MyDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }

        private DialogInterface.OnDismissListener onDismissListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {
                onDismissListener = (DialogInterface.OnDismissListener) context;
            } catch (Throwable e) {
                e.printStackTrace();
                onDismissListener = null;
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
            Bundle arguments = getArguments();
            if (arguments != null) {
                text = arguments.getString("text");
                dimAmount = arguments.getFloat("dimAmount", 0f);
                showBottom = arguments.getBoolean("showBottom", false);
                cancelable = arguments.getBoolean("cancelable", true);
            }
        }

        @Nullable
        @Override
        public View onCreateView(
                @NonNull LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.health_dialog_fragment_qrcode, container, false);
            ivQrcode = (ImageView) findViewById(R.id.health_record_iv_qrcode);
            findViewById(R.id.health_diary_tv_week_report).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentActivity activity = getActivity();
                    if (activity == null) {
                        return;
                    }
                    Intent intent = new Intent(activity, WeeklyReportActivity.class);
                    activity.startActivity(intent);
                }
            });
            findViewById(R.id.health_diary_tv_month_report).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentActivity activity = getActivity();
                    if (activity == null) {
                        return;
                    }
                    Intent intent = new Intent(activity, MonthlyReportActivity.class);
                    activity.startActivity(intent);
                }
            });
            if (TextUtils.isEmpty(text)) {
                return mView;
            }
            Handlers.bg().post(new Runnable() {
                @Override
                public void run() {
//                    if (text.startsWith("http")){
//                        text = MyDialogFragment.this.text.replaceFirst("https://|http://", "");
//                    }
                    final Bitmap bitmap = QrCodeUtils.encodeQrCode(text, dp(260), dp(260));
                    if (bitmap != null && ivQrcode != null) {
                        Handlers.ui().post(new Runnable() {
                            @Override
                            public void run() {
                                ivQrcode.setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            });
            return mView;
        }

        @Override
        public void onStart() {
            // initWindowStyle
            super.onStart();
            initWindowParams();
        }

        private void initWindowParams() {
            Window window = getDialog().getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.dimAmount = dimAmount;
                //是否在底部显示
                if (showBottom) {
                    lp.gravity = Gravity.BOTTOM;
                } else {
                    lp.gravity = Gravity.CENTER;
                }

                lp.width = UiUtils.pt(1280);
                lp.height = UiUtils.pt(840);

                window.setAttributes(lp);
            }
            setCancelable(cancelable);
        }

        @Override
        public void onStop() {
            Handlers.ui().removeCallbacksAndMessages(null);
            Handlers.bg().removeCallbacksAndMessages(null);
            super.onStop();
        }

        public int dp(float value) {
            float density = getResources().getDisplayMetrics().density;
            return (int) (density * value + 0.5f);
        }

        public <V extends View> V findViewById(@IdRes int id) {
            if (mView == null) {
                return null;
            }
            return (V) mView.findViewById(id);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (onDismissListener != null) {
                onDismissListener.onDismiss(dialog);
            }
        }

        @Override
        public void onDetach() {
            onDismissListener = null;
            super.onDetach();
        }
    }
}
