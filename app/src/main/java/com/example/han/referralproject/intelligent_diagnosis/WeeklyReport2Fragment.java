package com.example.han.referralproject.intelligent_diagnosis;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.WeeklyReport;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.example.han.referralproject.view.progress.RxTextRoundProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyReport2Fragment extends Fragment {
    @BindView(R.id.sj_gaoya)
    TextView sjGaoya;
    @BindView(R.id.rpb_sj_gaoya)
    RxRoundProgressBar rpbSjGaoya;
    @BindView(R.id.mb_gaoya)
    TextView mbGaoya;
    @BindView(R.id.rpb_mb_gaoya)
    RxRoundProgressBar rpbMbGaoya;
    @BindView(R.id.sj_diya)
    TextView sjDiya;
    @BindView(R.id.rpb_sj_diya)
    RxRoundProgressBar rpbSjDiya;
    @BindView(R.id.mb_diya)
    TextView mbDiya;
    @BindView(R.id.rpb_mb_diya)
    RxRoundProgressBar rpbMbDiya;
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.tab_mb_gaoya)
    TextView tabMbGaoya;
    @BindView(R.id.tab_mb_diya)
    TextView tabMbDiya;
    @BindView(R.id.tab_sj_gaoya)
    TextView tabSjGaoya;
    @BindView(R.id.tab_sj_diya)
    TextView tabSjDiya;
    @BindView(R.id.img_gaoya)
    ImageView imgGaoya;
    @BindView(R.id.pc_gaoya)
    TextView pcGaoya;
    @BindView(R.id.img_diya)
    ImageView imgDiya;
    @BindView(R.id.pc_diya)
    TextView pcDiya;
    @BindView(R.id.rpb_sum)
    RxTextRoundProgressBar rpbSum;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    Unbinder unbinder;
    @BindView(R.id.tv_progress2)
    TextView tvProgress2;
    @BindView(R.id.tv_advice)
    TextView tvAdvice;
    private View view;
    private WeeklyReport data;
    private String tips="未初始化";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weekly_report2, container, false);
//        data = (WeeklyReport) getArguments().getSerializable("data");
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    public void notifyData(WeeklyReport weeklyReport) {
        this.data = weeklyReport;
        float sj_gaoya, sj_diya, mb_gaoya, mb_diya;
        int pc_gaoya = 0, pc_diya = 0;
        if (data != null) {
            sj_gaoya = Float.parseFloat(data.gy_avg);
            sj_diya = Float.parseFloat(data.dy_avg);
            mb_gaoya = 120;
            mb_diya = 80;
            pc_gaoya = (int) (sj_gaoya - mb_gaoya);
            pc_diya = (int) (sj_diya - mb_diya);
            sjGaoya.setText((int) sj_gaoya + "");
            rpbSjGaoya.setMax(180);
            rpbSjGaoya.setProgress(sj_gaoya);

            sjDiya.setText((int) sj_diya + "");
            rpbSjDiya.setMax(100);
            rpbSjDiya.setProgress(sj_diya);

            mbGaoya.setText("120");
            rpbMbGaoya.setMax(180);
            rpbMbGaoya.setProgress(120);

            mbDiya.setText("80");
            rpbMbDiya.setMax(100);
            rpbMbDiya.setProgress(80);

            tabMbGaoya.setText("<120");
            tabMbDiya.setText("<80");
            tabSjGaoya.setText((int) sj_gaoya + "");
            tabSjDiya.setText((int) sj_diya + "");

            if (sj_gaoya > mb_gaoya) {
                imgGaoya.setImageResource(R.drawable.red_up);
                pcGaoya.setText((pc_gaoya) + "");
            } else {
                imgGaoya.setVisibility(View.GONE);
                pcGaoya.setText("达标");
                pcGaoya.setTextColor(Color.GREEN);
            }

            if (sj_diya > mb_diya) {
                imgDiya.setImageResource(R.drawable.red_up);
                pcDiya.setText((pc_diya) + "");
            } else {
                imgDiya.setVisibility(View.GONE);
                pcDiya.setText("达标");
                pcDiya.setTextColor(Color.GREEN);
            }

            rpbSum.setMax(100);
            float progress_percent = Float.parseFloat(data.zongw) * 100;
            rpbSum.setProgress(progress_percent);
            tvProgress2.setText((int)progress_percent+"%");
            tips="主人，您的高压距离目标"
                    + pc_gaoya + "毫米汞柱" + ",低压距离目标" + pc_diya + "毫米汞柱。上周整体计划完成"
                    + (int)progress_percent + "%。请继续根据机器人的指导保持良好的生活习惯，积极锻炼。";

            tvAdvice.setText(tips);
        }
    }
    public static boolean isSpeak=false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser&&isSpeak){
            isSpeak=false;
            ((WeeklyReportActivity) getActivity()).speak(tips);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
