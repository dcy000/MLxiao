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
import com.example.han.referralproject.bean.MonthlyReport;
import com.gcml.lib_widget.progressbar.TextRoundProgressBar;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyReport3Fragment extends Fragment {


    @BindView(R.id.view_left)
    View viewLeft;
    @BindView(R.id.view_right)
    View viewRight;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
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
    @BindView(R.id.tv_progress2)
    TextView tvProgress2;
    @BindView(R.id.rpb_sum)
    TextRoundProgressBar rpbSum;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    Unbinder unbinder;
    private MonthlyReport.MAPQ data;
    private String tips;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    public MonthlyReport3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_report2, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText("本月目标与进度");
        return view;
    }

    public void notifyData(MonthlyReport.MAPQ weeklyReport) {
        this.data = weeklyReport;
        float sj_gaoya, sj_diya, mb_gaoya, mb_diya;
        int pc_gaoya = 0, pc_diya = 0;
        if (data != null) {
            sj_gaoya = Float.parseFloat(data.gy_avg);
            sj_diya = Float.parseFloat(data.dy_avg);
            mb_gaoya = Float.parseFloat(data.p_gy);
            mb_diya = Float.parseFloat(data.p_dy);
            pc_gaoya = (int) (sj_gaoya - mb_gaoya);
            pc_diya = (int) (sj_diya - mb_diya);


            tabMbGaoya.setText("<"+String.format("%.0f",mb_gaoya));
            tabMbDiya.setText("<"+String.format("%.0f",mb_diya));
            tabSjGaoya.setText((int) sj_gaoya + "");
            tabSjDiya.setText((int) sj_diya + "");

            if (sj_gaoya > mb_gaoya) {
                imgGaoya.setImageResource(R.drawable.red_up);
                pcGaoya.setText((pc_gaoya) + "");
                viewLeft.setBackgroundColor(Color.parseColor("#FF5747"));
            } else {
                imgGaoya.setVisibility(View.GONE);
                pcGaoya.setText("√");
                pcGaoya.setTextColor(Color.parseColor("#3CD478"));
                viewLeft.setBackgroundColor(Color.parseColor("#49DF84"));
            }

            if (sj_diya > mb_diya) {
                imgDiya.setImageResource(R.drawable.red_up);
                pcDiya.setText((pc_diya) + "");
                viewRight.setBackgroundColor(Color.parseColor("#FF5747"));
            } else {
                imgDiya.setVisibility(View.GONE);
                pcDiya.setText("√");
                pcDiya.setTextColor(Color.parseColor("#3CD478"));
                viewRight.setBackgroundColor(Color.parseColor("#49DF84"));
            }

            rpbSum.setMax(100);
            float progress_percent = Float.parseFloat(data.zongw) * 100;
            rpbSum.setProgress(progress_percent);
            tvProgress2.setText((int) progress_percent + "%");
            tips = "主人，您的高压距离目标"
                    + pc_gaoya + "毫米汞柱" + ",低压距离目标" + pc_diya + "毫米汞柱。上月整体计划完成"
                    + (int) progress_percent + "%。请继续根据机器人的指导保持良好的生活习惯，积极锻炼。";

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            MLVoiceSynthetize.startSynthesize(tips);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
