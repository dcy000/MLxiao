package com.example.han.referralproject.intelligent_diagnosis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.WeeklyReport;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.example.han.referralproject.view.progress.RxTextRoundProgressBar;
import com.littlejie.circleprogress.WaveProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyReport1Fragment extends Fragment {

    @BindView(R.id.tv_week_title)
    TextView tvWeekTitle;
    @BindView(R.id.tv_gaoya)
    TextView tvGaoya;
    @BindView(R.id.rpb_gao)
    RxRoundProgressBar rpbGao;
    @BindView(R.id.tv_diya)
    TextView tvDiya;
    @BindView(R.id.rpb_diya)
    RxRoundProgressBar rpbDiya;
    @BindView(R.id.wave_progress_bar)
    WaveProgress waveProgressBar;
    @BindView(R.id.cl_left)
    LinearLayout clLeft;
    @BindView(R.id.gaoya_mubiao)
    TextView gaoyaMubiao;
    @BindView(R.id.diya_mubiao)
    TextView diyaMubiao;
    @BindView(R.id.na_mubiao)
    TextView naMubiao;
    @BindView(R.id.yundong_mubiao)
    TextView yundongMubiao;
    @BindView(R.id.tizhong_mubiao)
    TextView tizhongMubiao;
    @BindView(R.id.yinjiu_mubiao)
    TextView yinjiuMubiao;
    @BindView(R.id.rpb_sum)
    RxTextRoundProgressBar rpbSum;
    @BindView(R.id.tv_advice)
    TextView tvAdvice;
    Unbinder unbinder;
    @BindView(R.id.tv_progress1)
    TextView tvProgress1;
    //    @BindView(R.id.tv_progress)
//    TextView tvProgress;
    private View view;
    private WeeklyReport data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weekly_report1, container, false);
//        data = (WeeklyReport) getArguments().getSerializable("data");
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    public void notifyData(WeeklyReport weeklyReport) {
        this.data = weeklyReport;
        float sj_gaoya, sj_diya, mb_gaoya, mb_diya;
        if (data != null) {
            sj_gaoya = Float.parseFloat(data.gy_avg);
            sj_diya = Float.parseFloat(data.dy_avg);
            mb_gaoya = 120;
            mb_diya = 80;

            tvGaoya.setText(String.format("%.0f", sj_gaoya));
            rpbGao.setMax(180);
            rpbGao.setProgress(sj_gaoya);

            tvDiya.setText(String.format("%.0f", sj_diya));
            rpbDiya.setMax(100);
            rpbDiya.setProgress(sj_diya);

            waveProgressBar.setMaxValue(100);
            waveProgressBar.setValue(Float.parseFloat(data.health));
            waveProgressBar.setHealthValue(data.health + "分");


            gaoyaMubiao.setText("120");
            diyaMubiao.setText("80");
            naMubiao.setText(String.format("%.0f", Float.parseFloat(data.nam)));
            yundongMubiao.setText(String.format("%.0f", Float.parseFloat(data.sportsm)));
            String height_s = LocalShared.getInstance(getContext()).getUserHeight();
            float height_f = Float.parseFloat(height_s);
            float weight = Float.parseFloat(data.bmim) * (height_f / 100.0f) * (height_f / 100.0f);
            tizhongMubiao.setText((int) weight + "");
            yinjiuMubiao.setText(String.format("%.0f", Float.parseFloat(data.drinkm)));

            rpbSum.setMax(100);
            float progress_percent = Float.parseFloat(data.zongw) * 100;
            rpbSum.setProgress(progress_percent);
            tvProgress1.setText((int)progress_percent+"%");
            String tips="";
            if (sj_gaoya < mb_gaoya && sj_diya < mb_diya) {
                tips = "主人，恭喜您本周血压降至目标值以下，请继续保持良好的生活习惯,积极锻炼。查看详细的报告，请向左滑动页面！";

            } else if (sj_diya < mb_diya) {
                tips = "主人，恭喜您本周低压降至目标值以下，但高压仍然偏高。本周计划完成"
                        + (int)progress_percent + "%。请继续根据机器人的指导保持良好的生活习惯，积极锻炼。查看详细的报告，请向左滑动页面！";
            } else if (sj_gaoya < mb_gaoya) {
                tips = "主人，恭喜您本周高压降至目标值以下，但低压仍然偏高。本周计划完成"
                        + (int)progress_percent + "%。请继续根据机器人的指导保持良好的生活习惯，积极锻炼。查看详细的报告，请向左滑动页面！";
            } else {
                tips = "主人，您的血压仍偏高。本周计划完成"
                        + (int)progress_percent + "%，未完成目标计划，请继续根据机器人的指导保持良好的生活习惯，积极锻炼。查看详细的报告，请向左滑动页面！";
            }
            tvAdvice.setText(tips);
            ((WeeklyReportActivity) getActivity()).speak(tips);
        }
    }
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser){//显示
//            ((WeeklyReportActivity) getActivity()).speak(tips);
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        WeeklyReport2Fragment.isSpeak=true;
        WeeklyReport3Fragment.isSpeak=true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
