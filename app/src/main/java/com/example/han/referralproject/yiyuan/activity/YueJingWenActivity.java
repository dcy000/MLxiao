package com.example.han.referralproject.yiyuan.activity;

import cn.qqtheme.framework.picker.DatePicker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.Utils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.util.ConvertUtils;

public class YueJingWenActivity extends BaseActivity implements OnDateSetListener {

    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.tv_yuejing_date)
    TextView tvYuejingDate;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    private String year;
    private String month;
    private String day;
    private TimePickerDialog mDialogAll;

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
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        year = calendar.get(Calendar.YEAR) + "";
        month = calendar.get(Calendar.MONTH) + 1 + "";
        day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        tvYuejingDate.setText(year + "-" + month + "-" + day);
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

    Calendar calendar = Calendar.getInstance();

    private void goForward() {
        String time = tvYuejingDate.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(time)) {
            speak("主人,日期不能为空");
            return;
        }
        LocalShared.getInstance(this.getApplicationContext()).setYueJingDate(time);
        startActivity(new Intent(this, DrinkInfoActivity.class));

    }


    public void onYearMonthDayPicker() {
//        final DatePicker picker = new DatePicker(this);
//        picker.setCanceledOnTouchOutside(true);
//        picker.setUseWeight(true);
//        picker.setTopPadding(ConvertUtils.toPx(this, 10));
//        picker.setRangeEnd(2888, 8, 8);
//        picker.setRangeStart(2016, 6, 6);
//
//        picker.setSelectedItem(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
//        picker.setResetWhileWheel(false);
//        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
//            @Override
//            public void onDatePicked(String year, String month, String day) {
//                tvYuejingDate.setText(year + "-" + month + "-" + day);
//            }
//        });
//        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
//            @Override
//            public void onYearWheeled(int index, String year) {
//                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
//            }
//
//            @Override
//            public void onMonthWheeled(int index, String month) {
//                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
//            }
//
//            @Override
//            public void onDayWheeled(int index, String day) {
//                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
//            }
//        });
//
//
//        picker.setTextColor(Color.parseColor("#ff333333"));
//        picker.setTextSize(100);
//        picker.show();


        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextSize(36)
                .setTitleStringId("请选择日期")
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(R.color.toolbar_bg)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "data");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerDialog, long time) {
        tvYuejingDate.setText(Utils.stampToYMD(time));
    }

}
