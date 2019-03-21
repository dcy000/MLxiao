package com.gcml.module_inquiry.inquiry.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gcml.common.FilterClickListener;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.InquiryBaseFrament;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by lenovo on 2019/3/21.
 */

public class YueJingTimeFragment extends InquiryBaseFrament implements View.OnClickListener {
    Calendar calendar = Calendar.getInstance();
    private TextView tvYuejingDate;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;

    private String year;
    private String month;
    private String day;

    @Override
    protected int layoutId() {
        return R.layout.fragment_yuejing;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tvYuejingDate = view.findViewById(R.id.tv_yuejing_date);
        tvSignUpGoBack = view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoForward = view.findViewById(R.id.tv_sign_up_go_forward);

        tvYuejingDate.setOnClickListener(new FilterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBirthday();
            }
        }));
        tvSignUpGoBack.setOnClickListener(new FilterClickListener(this));
        tvSignUpGoForward.setOnClickListener(new FilterClickListener(this));


        year = calendar.get(Calendar.YEAR) + "";
        month = calendar.get(Calendar.MONTH) + 1 + "";
        day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        tvYuejingDate.setText(year + "-" + month + "-" + day);
    }

    @Override
    public void onClick(View v) {
        if (listenerAdapter != null) {
            int id = v.getId();
            if (id == R.id.tv_sign_up_go_back) {
                listenerAdapter.onBack();
            } else if (id == R.id.tv_sign_up_go_forward) {
                goForward();
            }
        }
    }

    private void goForward() {
        String time = tvYuejingDate.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(time)) {
            ToastUtils.showShort("主人,日期不能为空");
            return;
        }
        listenerAdapter.onNext(time);
    }

    private void selectBirthday() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
//        endDate.set(2099, 11, 31);

        OnTimeSelectListener listener = (date, v) -> {
            SimpleDateFormat birth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String birthString = birth.format(date);
            tvYuejingDate.setText(birthString);
        };
        TimePickerView pvTime = new TimePickerBuilder(getContext(), listener)
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static YueJingTimeFragment newInstance(String param1, String param2) {
        YueJingTimeFragment fragment = new YueJingTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

}
