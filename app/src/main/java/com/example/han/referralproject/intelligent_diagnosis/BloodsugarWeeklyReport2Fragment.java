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
import com.gcml.lib_widget.progressbar.TextRoundProgressBar;

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
    private TextRoundProgressBar rpbSum;
    private View mBloodsugarEmpty;
    private View mBloodsugarOne;
    private View mBloodsugarTwo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.bloodsugar_weekly_report_fragment2, container, false);
            initView(view);
        }
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
                tabMbEmpty.setText("<" + String.format("%.2f", bloodSugarTarget));
            }
            if (bloodSugarOneTarget != null) {
                tabMbOne.setText("<" + String.format("%.2f", bloodSugarOneTarget));
            }
            if (bloodSugarTwoTarget != null) {
                tabMbTwo.setText("<" + String.format("%.2f", bloodSugarTwoTarget));
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
            }else{
                mBloodsugarEmpty.setBackgroundColor(Color.parseColor("#FF5747"));
                imgEmpty.setVisibility(View.GONE);
                pcEmpty.setVisibility(View.GONE);
            }

            if (bloodSugarOneOffset != null) {
                if (bloodSugarOneOffset > 0) {
                    imgOne.setImageResource(R.drawable.red_up);
                    imgOne.setVisibility(View.VISIBLE);
                    pcOne.setText(String.format("%.2f", bloodSugarOneOffset));
                    mBloodsugarOne.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgOne.setVisibility(View.GONE);
                    pcOne.setText("√");
                    pcOne.setTextColor(Color.parseColor("#3CD478"));
                    mBloodsugarOne.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }else {
                mBloodsugarOne.setBackgroundColor(Color.parseColor("#FF5747"));
                imgOne.setVisibility(View.GONE);
                pcOne.setVisibility(View.GONE);
            }
            if (bloodSugarTwoOffset != null) {
                if (bloodSugarOneOffset > 0) {
                    imgTwo.setImageResource(R.drawable.red_up);
                    imgTwo.setVisibility(View.VISIBLE);
                    pcTwo.setText(String.format("%.2f", bloodSugarOneOffset));
                    mBloodsugarTwo.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgTwo.setVisibility(View.GONE);
                    pcTwo.setText("√");
                    pcTwo.setTextColor(Color.parseColor("#3CD478"));
                    mBloodsugarTwo.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }else{
                mBloodsugarTwo.setBackgroundColor(Color.parseColor("#FF5747"));
                imgTwo.setVisibility(View.GONE);
                pcTwo.setVisibility(View.GONE);
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
        tvBloodsugarEmpty = view.findViewById(R.id.tv_bloodsugar_empty);
        tvBloodsugarOne = view.findViewById(R.id.tv_bloodsugar_one);
        tvBloodsugarTwo = view.findViewById(R.id.tv_bloodsugar_two);
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
        mBloodsugarEmpty = view.findViewById(R.id.bloodsugar_empty);
        mBloodsugarOne = view.findViewById(R.id.bloodsugar_one);
        mBloodsugarTwo = view.findViewById(R.id.bloodsugar_two);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
