package com.example.han.referralproject.intelligent_diagnosis;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.lib_widget.progressbar.TextRoundProgressBar;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */

public class New_MonthlyReport2Fragment extends Fragment {

    private View view;
    private View viewLeft;
    private View viewRight;
    private TextView tvLeft;
    private TextView tvRight;
    private LinearLayout llLeft;
    private RadioButton rbFirst;
    private RadioButton rbSecond;
    private RadioButton rbThree;
    private RadioButton rbFour;
    private RadioGroup rg;
    private TextView tabMbGaoya;
    private TextView tabMbDiya;
    private TextView tabSjGaoya;
    private TextView tabSjDiya;
    private ImageView imgGaoya;
    private TextView pcGaoya;
    private ImageView imgDiya;
    private TextView pcDiya;
    private TextView tvProgress2;
    private TextRoundProgressBar rpbSum;
    private LinearLayout llRight;
    private WeeklyOrMonthlyReport report;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_fragment_monthly_report2, container, false);
        initView(view);
        dealLogic();
        return view;
    }

    private void dealLogic() {
        rbFirst.setChecked(true);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_first:
                        dealPerWeekData(0);
                        break;
                    case R.id.rb_second:
                        dealPerWeekData(1);
                        break;
                    case R.id.rb_three:
                        dealPerWeekData(2);
                        break;
                    case R.id.rb_four:
                        dealPerWeekData(3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initView(View view) {
        viewLeft = view.findViewById(R.id.view_left);
        viewRight = view.findViewById(R.id.view_right);
        tvLeft = view.findViewById(R.id.tv_left);
        tvRight = view.findViewById(R.id.tv_right);
        llLeft = view.findViewById(R.id.ll_left);
        rbFirst = view.findViewById(R.id.rb_first);
        rbSecond = view.findViewById(R.id.rb_second);
        rbThree = view.findViewById(R.id.rb_three);
        rbFour = view.findViewById(R.id.rb_four);
        rg = view.findViewById(R.id.rg);
        tabMbGaoya = view.findViewById(R.id.tab_mb_gaoya);
        tabMbDiya = view.findViewById(R.id.tab_mb_diya);
        tabSjGaoya = view.findViewById(R.id.tab_sj_gaoya);
        tabSjDiya = view.findViewById(R.id.tab_sj_diya);
        imgGaoya = view.findViewById(R.id.img_gaoya);
        pcGaoya = view.findViewById(R.id.pc_gaoya);
        imgDiya = view.findViewById(R.id.img_diya);
        pcDiya = view.findViewById(R.id.pc_diya);
        tvProgress2 = view.findViewById(R.id.tv_progress2);
        rpbSum = view.findViewById(R.id.rpb_sum);
        llRight = view.findViewById(R.id.ll_right);
    }

    public void notifyData(WeeklyOrMonthlyReport report) {
        this.report = report;
        dealPerWeekData(0);
    }

    private void dealPerWeekData(int week) {
        if (report == null) {
            return;
        }
        List<WeeklyOrMonthlyReport.WeekDateListBean> weekDateList = report.getWeekDateList();
        if (weekDateList == null) {
            return;
        }
        WeeklyOrMonthlyReport.WeekDateListBean weekDateListBean = weekDateList.get(week);
        if (weekDateListBean != null) {
            String highTarget = weekDateListBean.getHighTarget();
            String lowTarget = weekDateListBean.getLowTarget();
            String highPressureAvg = weekDateListBean.getHighPressureAvg();
            String lowPressureAvg = weekDateListBean.getLowPressureAvg();
            String highOffset = weekDateListBean.getHighOffset();
            String lowOffset = weekDateListBean.getLowOffset();
            String completion = weekDateListBean.getCompletion();
            if (!TextUtils.isEmpty(highTarget)) {
                tabMbGaoya.setText("<" + highTarget);
            } else {
                tabMbGaoya.setText("无数据");
            }
            if (!TextUtils.isEmpty(lowTarget)) {
                tabMbDiya.setText("<" + lowTarget);
            } else {
                tabMbDiya.setText("无数据");
            }
            if (!TextUtils.isEmpty(highPressureAvg)) {
                tabSjGaoya.setText(highPressureAvg);
            } else {
                tabSjGaoya.setText("无数据");
            }
            if (!TextUtils.isEmpty(lowPressureAvg)) {
                tabSjDiya.setText(lowPressureAvg);
            } else {
                tabSjDiya.setText("无数据");
            }
            if (!TextUtils.isEmpty(highOffset)) {
                float v_high = Float.parseFloat(highOffset);
                if (v_high > 0) {
                    imgGaoya.setImageResource(R.drawable.red_up);
                    imgGaoya.setVisibility(View.VISIBLE);
                    pcGaoya.setVisibility(View.VISIBLE);
                    pcGaoya.setText(highOffset);
                    pcGaoya.setTextColor(Color.parseColor("#FF5747"));
                    viewLeft.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgGaoya.setVisibility(View.GONE);
                    pcGaoya.setText("√");
                    pcGaoya.setVisibility(View.VISIBLE);
                    pcGaoya.setTextColor(Color.parseColor("#3CD478"));
                    viewLeft.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            } else {
                viewLeft.setBackgroundColor(Color.parseColor("#FF5747"));
                imgGaoya.setVisibility(View.GONE);
                pcGaoya.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(lowOffset)) {
                float v_low = Float.parseFloat(lowOffset);
                if (v_low > 0) {
                    imgDiya.setImageResource(R.drawable.red_up);
                    imgDiya.setVisibility(View.VISIBLE);
                    pcDiya.setVisibility(View.VISIBLE);
                    pcDiya.setText(lowOffset);
                    pcDiya.setTextColor(Color.parseColor("#FF5747"));
                    viewRight.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgDiya.setVisibility(View.GONE);
                    pcDiya.setText("√");
                    pcDiya.setVisibility(View.VISIBLE);
                    pcDiya.setTextColor(Color.parseColor("#3CD478"));
                    viewRight.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            } else {
                viewRight.setBackgroundColor(Color.parseColor("#FF5747"));
                imgDiya.setVisibility(View.GONE);
                pcDiya.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(completion)) {
                int int_completion = (int) Float.parseFloat(completion);
                tvProgress2.setText(int_completion + "%");
                rpbSum.setMax(100);
                rpbSum.setProgress(int_completion);
            } else {
                tvProgress2.setText("0%");
                rpbSum.setProgress(0);
            }
        }
    }
}
