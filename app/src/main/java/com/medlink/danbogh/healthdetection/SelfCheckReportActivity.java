package com.medlink.danbogh.healthdetection;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelfCheckReportActivity extends AppCompatActivity {

    @BindView(R.id.tv_report_title1)
    TextView tvReportTitle1;
    @BindView(R.id.tv_report_description1)
    TextView tvReportDescription1;
    @BindView(R.id.tv_report_detail1)
    TextView tvReportDetail1;
    @BindView(R.id.tv_report_title2)
    TextView tvReportTitle2;
    @BindView(R.id.tv_report_description2)
    TextView tvReportDescription2;
    @BindView(R.id.tv_report_title3)
    TextView tvReportTitle3;
    @BindView(R.id.tv_report_description3)
    TextView tvReportDescription3;
    @BindView(R.id.cl_report_card1)
    ConstraintLayout mClReportCard1;
    @BindView(R.id.cl_report_card2)
    ConstraintLayout mClReportCard2;
    @BindView(R.id.cl_report_card3)
    ConstraintLayout mClReportCard3;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_check_report);
        mUnbinder = ButterKnife.bind(this);
        tvTitle.setText("自查报告");
        tvReportTitle1.setText("1、脑动脉供血不足");
        tvReportTitle2.setText("2、脑动脉供血不足");
        tvReportTitle3.setText("3、脑动脉供血不足");
        tvReportDescription1.setText("患病概率 51.85%\n常见症状：头晕、脑血管供血不足、反复头晕");
        tvReportDescription2.setText("患病概率 51.85%\n常见症状：头晕、脑血管供血不足、反复头晕");
        tvReportDescription3.setText("患病概率 51.85%\n常见症状：头晕、脑血管供血不足、反复头晕");
    }

    @OnClick(R.id.tv_report_detail1)
    public void onTvReportDetail1Clicked() {
        T.show("期待...");
    }

    @OnClick(R.id.tv_report_detail2)
    public void onTvReportDetail2Clicked() {
        T.show("期待...");
    }

    @OnClick(R.id.tv_report_detail3)
    public void onTvReportDetail3Clicked() {
        T.show("期待...");
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_home)
    public void onIvHomeClicked() {

    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
