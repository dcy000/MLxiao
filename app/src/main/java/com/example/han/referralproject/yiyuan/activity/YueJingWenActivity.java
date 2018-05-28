package com.example.han.referralproject.yiyuan.activity;

import cn.qqtheme.framework.picker.DatePicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.util.ConvertUtils;

public class YueJingWenActivity extends BaseActivity {

    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.tv_yuejing_date)
    TextView tvYuejingDate;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_jing_wen);
        ButterKnife.bind(this);
        initTilte();
        speak("主人,请输入上一次月经结束的时间");
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

    @OnClick({R.id.tv_yuejing_date, R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_yuejing_date:
                onYearMonthDayPicker();
                break;
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                goForward();
                break;
        }
    }

    private void goForward() {
        String time = tvYuejingDate.getText().toString().trim().replaceAll(" ","");
        if (TextUtils.isEmpty(time)) {
            speak("主人,日期不能为空");
            return;
        }
        LocalShared.getInstance(this.getApplicationContext()).setYueJingDate(time);
        startActivity(new Intent(this, DrinkWenActivity.class));

    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeEnd(2111, 1, 11);
        picker.setRangeStart(2016, 8, 29);
        picker.setSelectedItem(2050, 10, 14);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvYuejingDate.setText(year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }


//    public void onYearMonthDayTimePicker(View view) {
//        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
//        picker.setDateRangeStart(2017, 1, 1);
//        picker.setDateRangeEnd(2025, 11, 11);
//        picker.setTimeRangeStart(9, 0);
//        picker.setTimeRangeEnd(20, 30);
//        picker.setTopLineColor(0x99FF0000);
//        picker.setLabelTextColor(0xFFFF0000);
//        picker.setDividerColor(0xFFFF0000);
//        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
//            @Override
//            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
//                showToast(year + "-" + month + "-" + day + " " + hour + ":" + minute);
//            }
//        });
//        picker.show();
//    }

}
