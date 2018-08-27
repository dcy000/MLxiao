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
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.lib_widget.progressbar.TextRoundProgressBar;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class New_WeeklyReport2Fragment extends Fragment {
    private View view;
    private View viewLeft;
    private View viewRight;
    private TextView tvLeft;
    private TextView tvRight;
    private LinearLayout llLeft;
    private TextView tvTitle;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_fragment_weekly_report2, container, false);
        initView(view);
        return view;
    }

    public void notifyData(WeeklyOrMonthlyReport report) {
        if (report == null) {
            return;
        }
        List<WeeklyOrMonthlyReport.WeekDateListBean> weekDateList = report.getWeekDateList();
        if (weekDateList != null && weekDateList.size() > 0) {
            WeeklyOrMonthlyReport.WeekDateListBean weekDateListBean = weekDateList.get(0);
            String highTarget = weekDateListBean.getHighTarget();
            String lowTarget = weekDateListBean.getLowTarget();
            String highPressureAvg = weekDateListBean.getHighPressureAvg();
            String lowPressureAvg = weekDateListBean.getLowPressureAvg();
            String highOffset = weekDateListBean.getHighOffset();
            String lowOffset = weekDateListBean.getLowOffset();
            String completion = report.getCompletion();
            if (!TextUtils.isEmpty(highTarget)) {
                tabMbGaoya.setText("<" + highTarget);
            }
            if (!TextUtils.isEmpty(lowTarget)) {
                tabMbDiya.setText("<" + lowTarget);
            }
            if (!TextUtils.isEmpty(highPressureAvg)) {
                tabSjGaoya.setText(highPressureAvg);
            }
            if (!TextUtils.isEmpty(lowPressureAvg)) {
                tabSjDiya.setText(lowPressureAvg);
            }
            if (!TextUtils.isEmpty(highOffset)) {
                float v_high = Float.parseFloat(highOffset);
                if (v_high > 0) {
                    imgGaoya.setImageResource(R.drawable.red_up);
                    pcGaoya.setText(highOffset);
                    viewLeft.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgGaoya.setVisibility(View.GONE);
                    pcGaoya.setText("√");
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
                    pcDiya.setText(lowOffset);
                    viewRight.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgDiya.setVisibility(View.GONE);
                    pcDiya.setText("√");
                    pcDiya.setTextColor(Color.parseColor("#3CD478"));
                    viewRight.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            } else {
                viewRight.setBackgroundColor(Color.parseColor("#FF5747"));
                pcDiya.setVisibility(View.GONE);
                imgDiya.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(completion)) {
                int int_completion = (int) Float.parseFloat(completion);
                tvProgress2.setText(int_completion + "%");
                rpbSum.setMax(100);
                rpbSum.setProgress(int_completion);
            }
        }
    }

    private void initView(View view) {
        viewLeft = view.findViewById(R.id.view_left);
        viewRight = view.findViewById(R.id.view_right);
        tvLeft = view.findViewById(R.id.tv_left);
        tvRight = view.findViewById(R.id.tv_right);
        llLeft = view.findViewById(R.id.ll_left);
        tvTitle = view.findViewById(R.id.tv_title);
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
}
