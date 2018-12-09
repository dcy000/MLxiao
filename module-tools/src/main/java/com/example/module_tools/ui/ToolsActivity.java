package com.example.module_tools.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.example.module_tools.R;
import com.example.module_tools.R2;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToolsActivity extends ToolbarBaseActivity {


    @BindView(R2.id.iv_jiemeng)
    ImageView ivJiemeng;
    @BindView(R2.id.iv_jinjintian)
    ImageView ivJinjintian;
    @BindView(R2.id.iv_riqi_chaxun)
    ImageView ivRiqiChaxun;
    @BindView(R2.id.iv_caipu)
    ImageView ivCaipu;
    @BindView(R2.id.iv_baike)
    ImageView ivBaike;
    @BindView(R2.id.iv_jisuan)
    ImageView ivJisuan;
    @BindView(R2.id.iv_caimi)
    ImageView ivCaimi;
    @BindView(R2.id.iv_shengxiao)
    ImageView ivShengxiao;
    @BindView(R2.id.iv_tongfan_ci)
    ImageView ivTongfanCi;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_tools;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceSynthetize.startSynthesize("主人,欢迎来到工具页面,请选择您想使用的小工具");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mTitleText.setText("小工具");
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }


    @OnClick({R2.id.iv_jiemeng, R2.id.iv_jinjintian, R2.id.iv_riqi_chaxun, R2.id.iv_caipu, R2.id.iv_baike,
            R2.id.iv_jisuan, R2.id.iv_caimi, R2.id.iv_shengxiao, R2.id.iv_tongfan_ci})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_jiemeng) {
            startActivity(new Intent(this, JieMengActivity.class));

        } else if (i == R.id.iv_jinjintian) {
            startActivity(new Intent(this, HistoryTodayActivity.class));

        } else if (i == R.id.iv_riqi_chaxun) {
            startActivity(new Intent(this, DateInquireActivity.class));

        } else if (i == R.id.iv_caipu) {
            startActivity(new Intent(this, CookBookActivity.class));

        } else if (i == R.id.iv_baike) {
            startActivity(new Intent(this, BaiKeActivtiy.class));

        } else if (i == R.id.iv_jisuan) {
            startActivity(new Intent(this, CalculationActivity.class));

        } else if (i == R.id.iv_caimi) {
            startActivity(new Intent(this, RiddleActivity.class));

        } else if (i == R.id.iv_shengxiao) {
        } else if (i == R.id.iv_tongfan_ci) {
        }
    }
}
