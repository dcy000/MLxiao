package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YueJingWenActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_jing_wen);
        ButterKnife.bind(this);
        initTilte();
        speak("主人,请输入上一次月经结束的时间");
        ActivityHelper.addActivity(this);
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
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
//        endDate.set(2099, 11, 31);

        OnTimeSelectListener listener = new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat birth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String birthString = birth.format(date);
                tvYuejingDate.setText(birthString);
            }
        };

        TimePickerView pvTime = new TimePickerBuilder(this, listener)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setSubmitText("确认")
                .setOutSideCancelable(false)
                .setDividerColor(Color.WHITE)
                .isCyclic(true)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setBgColor(Color.WHITE)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .build();
        pvTime.show();
    }
}
