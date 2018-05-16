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
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.view.progress.RxTextRoundProgressBar;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BloodsugarWeeklyReport2Fragment extends Fragment {
    private View view;
    private TextView tvBloodsugarEmpty;
    private TextView tvBloodsugarOne;
    private TextView tvBloodsugarTwo;
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
    private View mBloodsugarEmpty;
    private View mBloodsugarOne;
    private View mBloodsugarTwo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bloodsugar_weekly_report_fragment2, container, false);
        initView(view);
        return view;
    }

    public void notifyData(WeeklyOrMonthlyBloodsugarReport report) {
        if (report == null) {
            return;
        }

        List<WeeklyOrMonthlyBloodsugarReport.WeekDateListBean> weekDateList = report.getWeekDateList();
        if (weekDateList != null && weekDateList.size() > 0) {
            WeeklyOrMonthlyBloodsugarReport.WeekDateListBean weekDateListBean = weekDateList.get(0);
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
            }
            if (bloodSugarOneTarget != null) {
                tabMbOne.setText(String.format("%.2f", bloodSugarOneTarget));
            }
            if (bloodSugarTwoTarget != null) {
                tabMbTwo.setText(String.format("%.2f", bloodSugarTwoTarget));
            }
            if (bloodSugarAvg != null) {
                tabSjEmpty.setText(String.format("%.2f", bloodSugarAvg));
            }
            if (bloodSugarOneAvg != null) {
                tabSjOne.setText(String.format("%.2f", bloodSugarOneAvg));
            }
            if (bloodSugarTwoAvg != null) {
                tabSjTwo.setText(String.format("%.2f", bloodSugarTwoAvg));
            }
            if (bloodSugarOffset != null) {
                if (bloodSugarOffset > 0) {
                    imgEmpty.setImageResource(R.drawable.red_up);
                    imgEmpty.setVisibility(View.VISIBLE);
                    pcEmpty.setText(String.format("%.2f", bloodSugarOffset));
                    mBloodsugarEmpty.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgEmpty.setVisibility(View.GONE);
                    pcEmpty.setText("√");
                    pcEmpty.setTextColor(Color.parseColor("#3CD478"));
                    mBloodsugarEmpty.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }

            if (bloodSugarOneOffset!=null) {
                if (bloodSugarOneOffset > 0) {
                    imgOne.setImageResource(R.drawable.red_up);
                    imgOne.setVisibility(View.VISIBLE);
                    pcOne.setText(String.format("%.2f",bloodSugarOneOffset));
                    mBloodsugarOne.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgOne.setVisibility(View.GONE);
                    pcOne.setText("√");
                    pcOne.setTextColor(Color.parseColor("#3CD478"));
                    mBloodsugarOne.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }
            if (bloodSugarTwoOffset!=null){
                if (bloodSugarOneOffset>0){
                    imgTwo.setImageResource(R.drawable.red_up);
                    imgTwo.setVisibility(View.VISIBLE);
                    pcTwo.setText(String.format("%.2f",bloodSugarOneOffset));
                    mBloodsugarTwo.setBackgroundColor(Color.parseColor("#FF5747"));
                }else{
                    imgTwo.setVisibility(View.GONE);
                    pcTwo.setText("√");
                    pcTwo.setTextColor(Color.parseColor("#3CD478"));
                    mBloodsugarTwo.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }
            if (!TextUtils.isEmpty(completion)) {
                int int_completion = (int) Float.parseFloat(completion);
                progressDisplay.setText(int_completion + "%");
                rpbSum.setMax(100);
                rpbSum.setProgress(int_completion);
            }
        }
    }

    private void initView(View view) {
        tvBloodsugarEmpty = (TextView) view.findViewById(R.id.tv_bloodsugar_empty);
        tvBloodsugarOne = (TextView) view.findViewById(R.id.tv_bloodsugar_one);
        tvBloodsugarTwo = (TextView) view.findViewById(R.id.tv_bloodsugar_two);
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
        mBloodsugarEmpty = (View) view.findViewById(R.id.bloodsugar_empty);
        mBloodsugarOne = (View) view.findViewById(R.id.bloodsugar_one);
        mBloodsugarTwo = (View) view.findViewById(R.id.bloodsugar_two);
    }
}
