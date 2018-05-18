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
import com.example.han.referralproject.view.progress.RxTextRoundProgressBar;

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
    private RxTextRoundProgressBar rpbSum;
    private LinearLayout llRight;
    private RadioButton mRbFirst;
    private RadioButton mRbSecond;
    private RadioButton mRbThree;
    private RadioButton mRbFour;
    private WeeklyOrMonthlyBloodsugarReport report;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bloodsugar_monthly_report_fragment2, container, false);
        initView(view);
        return view;
    }

    public void notifyData(WeeklyOrMonthlyBloodsugarReport report) {
        this.report=report;
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
            String completion = report.getCompletion();
            if (bloodSugarTarget != null) {
                tabMbEmpty.setText(String.format("%.2f", bloodSugarTarget));
            }else{
                tabMbEmpty.setText("无数据");
            }
            if (bloodSugarOneTarget != null) {
                tabMbOne.setText(String.format("%.2f", bloodSugarOneTarget));
            }else{
                tabMbOne.setText("无数据");
            }
            if (bloodSugarTwoTarget != null) {
                tabMbTwo.setText(String.format("%.2f", bloodSugarTwoTarget));
            }else{
                tabMbTwo.setText("无数据");
            }
            if (bloodSugarAvg != null) {
                tabSjEmpty.setText(String.format("%.2f", bloodSugarAvg));
            }else{
                tabSjEmpty.setText("无数据");
            }
            if (bloodSugarOneAvg != null) {
                tabSjOne.setText(String.format("%.2f", bloodSugarOneAvg));
            }else {
                tabSjOne.setText("无数据");
            }
            if (bloodSugarTwoAvg != null) {
                tabSjTwo.setText(String.format("%.2f", bloodSugarTwoAvg));
            }else{
                tabSjTwo.setText("无数据");
            }
            if (bloodSugarOffset != null) {
                if (bloodSugarOffset > 0) {
                    imgEmpty.setImageResource(R.drawable.red_up);
                    imgEmpty.setVisibility(View.VISIBLE);
                    pcEmpty.setText(String.format("%.2f", bloodSugarOffset));
                    bloodsugarEmpty.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgEmpty.setVisibility(View.GONE);
                    pcEmpty.setText("√");
                    pcEmpty.setTextColor(Color.parseColor("#3CD478"));
                    bloodsugarEmpty.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }else{
                bloodsugarEmpty.setBackgroundColor(Color.parseColor("#FF5747"));
                imgEmpty.setVisibility(View.GONE);
                pcEmpty.setVisibility(View.GONE);
            }

            if (bloodSugarOneOffset != null) {
                if (bloodSugarOneOffset > 0) {
                    imgOne.setImageResource(R.drawable.red_up);
                    imgOne.setVisibility(View.VISIBLE);
                    pcOne.setText(String.format("%.2f", bloodSugarOneOffset));
                    bloodsugarOne.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgOne.setVisibility(View.GONE);
                    pcOne.setText("√");
                    pcOne.setTextColor(Color.parseColor("#3CD478"));
                    bloodsugarOne.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }else{
                bloodsugarOne.setBackgroundColor(Color.parseColor("#FF5747"));
                imgOne.setVisibility(View.GONE);
                pcOne.setVisibility(View.GONE);
            }
            if (bloodSugarTwoOffset != null) {
                if (bloodSugarOneOffset > 0) {
                    imgTwo.setImageResource(R.drawable.red_up);
                    imgTwo.setVisibility(View.VISIBLE);
                    pcTwo.setText(String.format("%.2f", bloodSugarOneOffset));
                    bloodsugarTwo.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgTwo.setVisibility(View.GONE);
                    pcTwo.setText("√");
                    pcTwo.setTextColor(Color.parseColor("#3CD478"));
                    bloodsugarTwo.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }else {
                bloodsugarTwo.setBackgroundColor(Color.parseColor("#FF5747"));
                imgTwo.setVisibility(View.GONE);
                pcTwo.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(completion)) {
                int int_completion = (int) Float.parseFloat(completion);
                progressDisplay.setText(int_completion + "%");
                rpbSum.setMax(100);
                rpbSum.setProgress(int_completion);
            }else{
                progressDisplay.setText("0%");
                rpbSum.setProgress(0);
            }
        }
    }

    private void initView(View view) {
        bloodsugarEmpty = (View) view.findViewById(R.id.bloodsugar_empty);
        bloodsugarOne = (View) view.findViewById(R.id.bloodsugar_one);
        bloodsugarTwo = (View) view.findViewById(R.id.bloodsugar_two);
        tvBloodsugarEmpty = (TextView) view.findViewById(R.id.tv_bloodsugar_empty);
        tvBloodsugarOne = (TextView) view.findViewById(R.id.tv_bloodsugar_one);
        tvBloodsugarTwo = (TextView) view.findViewById(R.id.tv_bloodsugar_two);
        llLeft = (LinearLayout) view.findViewById(R.id.ll_left);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tabMbEmpty = (TextView) view.findViewById(R.id.tab_mb_empty);
        tabMbOne = (TextView) view.findViewById(R.id.tab_mb_one);
        tabMbTwo = (TextView) view.findViewById(R.id.tab_mb_two);
        tabSjEmpty = (TextView) view.findViewById(R.id.tab_sj_empty);
        tabSjOne = (TextView) view.findViewById(R.id.tab_sj_one);
        tabSjTwo = (TextView) view.findViewById(R.id.tab_sj_two);
        imgEmpty = (ImageView) view.findViewById(R.id.img_empty);
        pcEmpty = (TextView) view.findViewById(R.id.pc_empty);
        imgOne = (ImageView) view.findViewById(R.id.img_one);
        pcOne = (TextView) view.findViewById(R.id.pc_one);
        imgTwo = (ImageView) view.findViewById(R.id.img_two);
        pcTwo = (TextView) view.findViewById(R.id.pc_two);
        progressDisplay = (TextView) view.findViewById(R.id.progress_display);
        rpbSum = (RxTextRoundProgressBar) view.findViewById(R.id.rpb_sum);
        llRight = (LinearLayout) view.findViewById(R.id.ll_right);
        mRbFirst = (RadioButton) view.findViewById(R.id.rb_first);
        mRbFirst.setChecked(true);
        mRbFirst.setOnClickListener(this);
        mRbSecond = (RadioButton) view.findViewById(R.id.rb_second);
        mRbSecond.setOnClickListener(this);
        mRbThree = (RadioButton) view.findViewById(R.id.rb_three);
        mRbThree.setOnClickListener(this);
        mRbFour = (RadioButton) view.findViewById(R.id.rb_four);
        mRbFour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
}
