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
import com.example.han.referralproject.view.progress.RxTextRoundProgressBar;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */

public class New_MonthlyReport2Fragment extends Fragment {

    private View view;
    private View viewLeft;
    private View viewRight;
    private TextView tvLeft;
    /**
     * 低压
     */
    private TextView tvRight;
    private LinearLayout llLeft;
    /**
     * 第一周
     */
    private RadioButton rbFirst;
    /**
     * 第二周
     */
    private RadioButton rbSecond;
    /**
     * 第三周
     */
    private RadioButton rbThree;
    /**
     * 第四周
     */
    private RadioButton rbFour;
    private RadioGroup rg;
    /**  */
    private TextView tabMbGaoya;
    /**  */
    private TextView tabMbDiya;
    /**  */
    private TextView tabSjGaoya;
    /**  */
    private TextView tabSjDiya;
    private ImageView imgGaoya;
    /**  */
    private TextView pcGaoya;
    private ImageView imgDiya;
    /**  */
    private TextView pcDiya;
    /**  */
    private TextView tvProgress2;
    private RxTextRoundProgressBar rpbSum;
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
                }
            }
        });
    }

    private void initView(View view) {

        viewLeft = (View) view.findViewById(R.id.view_left);
        viewRight = (View) view.findViewById(R.id.view_right);
        tvLeft = (TextView) view.findViewById(R.id.tv_left);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        llLeft = (LinearLayout) view.findViewById(R.id.ll_left);
        rbFirst = (RadioButton) view.findViewById(R.id.rb_first);
        rbSecond = (RadioButton) view.findViewById(R.id.rb_second);
        rbThree = (RadioButton) view.findViewById(R.id.rb_three);
        rbFour = (RadioButton) view.findViewById(R.id.rb_four);
        rg = (RadioGroup) view.findViewById(R.id.rg);
        tabMbGaoya = (TextView) view.findViewById(R.id.tab_mb_gaoya);
        tabMbDiya = (TextView) view.findViewById(R.id.tab_mb_diya);
        tabSjGaoya = (TextView) view.findViewById(R.id.tab_sj_gaoya);
        tabSjDiya = (TextView) view.findViewById(R.id.tab_sj_diya);
        imgGaoya = (ImageView) view.findViewById(R.id.img_gaoya);
        pcGaoya = (TextView) view.findViewById(R.id.pc_gaoya);
        imgDiya = (ImageView) view.findViewById(R.id.img_diya);
        pcDiya = (TextView) view.findViewById(R.id.pc_diya);
        tvProgress2 = (TextView) view.findViewById(R.id.tv_progress2);
        rpbSum = (RxTextRoundProgressBar) view.findViewById(R.id.rpb_sum);
        llRight = (LinearLayout) view.findViewById(R.id.ll_right);
    }

    public void notifyData(WeeklyOrMonthlyReport report) {
        this.report = report;
        dealPerWeekData( 0);
    }

    private void dealPerWeekData( int week) {
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
            String completion = report.getCompletion();
            if (!TextUtils.isEmpty(highTarget)) {
                tabMbGaoya.setText(highTarget);
            }
            if (!TextUtils.isEmpty(lowTarget)) {
                tabMbDiya.setText(lowTarget);
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
                    imgGaoya.setVisibility(View.VISIBLE);
                    pcGaoya.setText(highOffset);
                    pcGaoya.setTextColor(Color.parseColor("#FF5747"));
                    viewLeft.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgGaoya.setVisibility(View.GONE);
                    pcGaoya.setText("√");
                    pcGaoya.setTextColor(Color.parseColor("#3CD478"));
                    viewLeft.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }

            if (!TextUtils.isEmpty(lowOffset)) {
                float v_low = Float.parseFloat(lowOffset);
                if (v_low > 0) {
                    imgDiya.setImageResource(R.drawable.red_up);
                    imgDiya.setVisibility(View.VISIBLE);
                    pcDiya.setText(lowOffset);
                    pcDiya.setTextColor(Color.parseColor("#FF5747"));
                    viewRight.setBackgroundColor(Color.parseColor("#FF5747"));
                } else {
                    imgDiya.setVisibility(View.GONE);
                    pcDiya.setText("√");
                    pcDiya.setTextColor(Color.parseColor("#3CD478"));
                    viewRight.setBackgroundColor(Color.parseColor("#49DF84"));
                }
            }

            if (!TextUtils.isEmpty(completion)) {
                int int_completion = (int) Float.parseFloat(completion);
                tvProgress2.setText(int_completion + "%");
                rpbSum.setMax(100);
                rpbSum.setProgress(int_completion);
            }
        }

    }
}
