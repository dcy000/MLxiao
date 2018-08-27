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
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.lib_widget.progressbar.TextRoundProgressBar;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class BloodsugarMonthlyReport2Fragment extends Fragment implements View.OnClickListener {
    private View view;
    private View bloodsugarEmpty;
    private View bloodsugarOne;
    private View bloodsugarTwo;
    private TextView tvBloodsugarEmpty;
    private TextView tvBloodsugarOne;
    private TextView tvBloodsugarTwo;
    private LinearLayout llLeft;
    private TextView tvTitle;
    private TextView tabMbEmpty;
    private TextView tabMbOne;
    private TextView tabMbTwo;
    private TextView tabSjEmpty;
    private TextView tabSjOne;
    private TextView tabSjTwo;
    private ImageView imgEmpty;
    private TextView pcEmpty;
    private ImageView imgOne;
    private TextView pcOne;
    private ImageView imgTwo;
    private TextView pcTwo;
    private TextView progressDisplay;
    private TextRoundProgressBar rpbSum;
    private LinearLayout llRight;
    private RadioButton mRbFirst;
    private RadioButton mRbSecond;
    private RadioButton mRbThree;
    private RadioButton mRbFour;
    private WeeklyOrMonthlyBloodsugarReport report;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.bloodsugar_monthly_report_fragment2, container, false);
            initView(view);
        }
        return view;
    }

    public void notifyData(WeeklyOrMonthlyBloodsugarReport report) {
        this.report = report;
        dealData(0);
    }

    private void dealData(int week) {
        if (report == null) {
            return;
        }
        List<WeeklyOrMonthlyBloodsugarReport.WeekDateListBean> weekDateList = report.getWeekDateList();
        if (weekDateList != null && weekDateList.size() > 0) {
            WeeklyOrMonthlyBloodsugarReport.WeekDateListBean weekDateListBean = weekDateList.get(week);
            Double bloodSugarTarget = weekDateListBean.getBloodSugarTarget();
            Double bloodSugarOneTarget = weekDateListBean.getBloodSugarOneTarget();
            Double bloodSugarTwoTarget = weekDateListBean.getBloodSugarTwoTarget();
            Double bloodSugarAvg = weekDateListBean.getBloodSugarAvg();
            Double bloodSugarOneAvg = weekDateListBean.getBloodSugarOneAvg();
            Double bloodSugarTwoAvg = weekDateListBean.getBloodSugarTwoAvg();
            Double bloodSugarOffset = weekDateListBean.getBloodSugarOffset();
            Double bloodSugarOneOffset = weekDateListBean.getBloodSugarOneOffset();
            Double bloodSugarTwoOffset = weekDateListBean.getBloodSugarTwoOffset();
            String completion = weekDateListBean.getCompletion();
            if (bloodSugarTarget != null) {
                tabMbEmpty.setText("<" + String.format("%.2f", bloodSugarTarget));
            } else {
                tabMbEmpty.setText("无数据");
            }
            if (bloodSugarOneTarget != null) {
                tabMbOne.setText("<" + String.format("%.2f", bloodSugarOneTarget));
            } else {
                tabMbOne.setText("无数据");
            }
            if (bloodSugarTwoTarget != null) {
                tabMbTwo.setText("<" + String.format("%.2f", bloodSugarTwoTarget));
            } else {
                tabMbTwo.setText("无数据");
            }
            if (bloodSugarAvg != null) {
                tabSjEmpty.setText(String.format("%.2f", bloodSugarAvg));
            } else {
                tabSjEmpty.setText("无数据");
            }
            if (bloodSugarOneAvg != null) {
                tabSjOne.setText(String.format("%.2f", bloodSugarOneAvg));
            } else {
                tabSjOne.setText("无数据");
            }
            if (bloodSugarTwoAvg != null) {
                tabSjTwo.setText(String.format("%.2f", bloodSugarTwoAvg));
            } else {
                tabSjTwo.setText("无数据");
            }
            if (bloodSugarOffset != null) {
                if (bloodSugarOffset > 0) {
                    imgEmpty.setImageResource(R.drawable.red_up);
                    imgEmpty.setVisibility(View.VISIBLE);
                    pcEmpty.setVisibility(View.VISIBLE);
                    pcEmpty.setText(String.format("%.2f", bloodSugarOffset));
                    pcEmpty.setTextColor(Color.parseColor("#FF5747"));
                    bloodsugarEmpty.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgEmpty.setVisibility(View.GONE);
                    pcEmpty.setText("√");
                    pcEmpty.setVisibility(View.VISIBLE);
                    pcEmpty.setTextColor(Color.parseColor("#3CD478"));
                    bloodsugarEmpty.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            } else {
                bloodsugarEmpty.setBackgroundColor(Color.parseColor("#FF5747"));
                imgEmpty.setVisibility(View.GONE);
                pcEmpty.setVisibility(View.GONE);
            }

            if (bloodSugarOneOffset != null) {
                if (bloodSugarOneOffset > 0) {
                    imgOne.setImageResource(R.drawable.red_up);
                    imgOne.setVisibility(View.VISIBLE);
                    pcOne.setVisibility(View.VISIBLE);
                    pcOne.setText(String.format("%.2f", bloodSugarOneOffset));
                    pcOne.setTextColor(Color.parseColor("#FF5747"));
                    bloodsugarOne.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgOne.setVisibility(View.GONE);
                    pcOne.setText("√");
                    pcOne.setVisibility(View.VISIBLE);
                    pcOne.setTextColor(Color.parseColor("#3CD478"));
                    bloodsugarOne.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            } else {
                bloodsugarOne.setBackgroundColor(Color.parseColor("#FF5747"));
                imgOne.setVisibility(View.GONE);
                pcOne.setVisibility(View.GONE);
            }
            if (bloodSugarTwoOffset != null) {
                if (bloodSugarOneOffset > 0) {
                    imgTwo.setImageResource(R.drawable.red_up);
                    imgTwo.setVisibility(View.VISIBLE);
                    pcTwo.setVisibility(View.VISIBLE);
                    pcTwo.setText(String.format("%.2f", bloodSugarOneOffset));
                    pcTwo.setTextColor(Color.parseColor("#FF5747"));
                    bloodsugarTwo.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgTwo.setVisibility(View.GONE);
                    pcTwo.setText("√");
                    pcTwo.setVisibility(View.VISIBLE);
                    pcTwo.setTextColor(Color.parseColor("#3CD478"));
                    bloodsugarTwo.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            } else {
                bloodsugarTwo.setBackgroundColor(Color.parseColor("#FF5747"));
                imgTwo.setVisibility(View.GONE);
                pcTwo.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(completion)) {
                int int_completion = (int) Float.parseFloat(completion);
                progressDisplay.setText(int_completion + "%");
                rpbSum.setMax(100);
                rpbSum.setProgress(int_completion);
            } else {
                progressDisplay.setText("0%");
                rpbSum.setProgress(0);
            }
        }
    }

    private void initView(View view) {
        bloodsugarEmpty = view.findViewById(R.id.bloodsugar_empty);
        bloodsugarOne = view.findViewById(R.id.bloodsugar_one);
        bloodsugarTwo = view.findViewById(R.id.bloodsugar_two);
        tvBloodsugarEmpty = view.findViewById(R.id.tv_bloodsugar_empty);
        tvBloodsugarOne = view.findViewById(R.id.tv_bloodsugar_one);
        tvBloodsugarTwo = view.findViewById(R.id.tv_bloodsugar_two);
        llLeft = view.findViewById(R.id.ll_left);
        tvTitle = view.findViewById(R.id.tv_title);
        tabMbEmpty = view.findViewById(R.id.tab_mb_empty);
        tabMbOne = view.findViewById(R.id.tab_mb_one);
        tabMbTwo = view.findViewById(R.id.tab_mb_two);
        tabSjEmpty = view.findViewById(R.id.tab_sj_empty);
        tabSjOne = view.findViewById(R.id.tab_sj_one);
        tabSjTwo = view.findViewById(R.id.tab_sj_two);
        imgEmpty = view.findViewById(R.id.img_empty);
        pcEmpty = view.findViewById(R.id.pc_empty);
        imgOne = view.findViewById(R.id.img_one);
        pcOne = view.findViewById(R.id.pc_one);
        imgTwo = view.findViewById(R.id.img_two);
        pcTwo = view.findViewById(R.id.pc_two);
        progressDisplay = view.findViewById(R.id.progress_display);
        rpbSum = view.findViewById(R.id.rpb_sum);
        llRight = view.findViewById(R.id.ll_right);
        mRbFirst = view.findViewById(R.id.rb_first);
        mRbFirst.setChecked(true);
        mRbFirst.setOnClickListener(this);
        mRbSecond = view.findViewById(R.id.rb_second);
        mRbSecond.setOnClickListener(this);
        mRbThree = view.findViewById(R.id.rb_three);
        mRbThree.setOnClickListener(this);
        mRbFour = view.findViewById(R.id.rb_four);
        mRbFour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_first:
                dealData(0);
                break;
            case R.id.rb_second:
                dealData(1);
                break;
            case R.id.rb_three:
                dealData(2);
                break;
            case R.id.rb_four:
                dealData(3);
                break;
        }
    }
    @Override
    public void onDestroyView() {
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        super.onDestroyView();
    }
}
