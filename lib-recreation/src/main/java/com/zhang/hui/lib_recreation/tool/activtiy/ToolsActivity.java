package com.zhang.hui.lib_recreation.tool.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.zhang.hui.lib_recreation.R;

public class ToolsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvJiemeng;
    private ImageView mIvJinjintian;
    private ImageView mIvRiqiChaxun;
    private ImageView mIvCaipu;
    private ImageView mIvBaike;
    private ImageView mIvJisuan;
    private ImageView mIvCaimi;
    private ImageView mIvShengxiao;
    private ImageView mIvTongfanCi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        initView();
    }

    private void initView() {
        mIvJiemeng = (ImageView) findViewById(R.id.iv_jiemeng);
        mIvJiemeng.setOnClickListener(this);
        mIvJinjintian = (ImageView) findViewById(R.id.iv_jinjintian);
        mIvJinjintian.setOnClickListener(this);
        mIvRiqiChaxun = (ImageView) findViewById(R.id.iv_riqi_chaxun);
        mIvRiqiChaxun.setOnClickListener(this);
        mIvCaipu = (ImageView) findViewById(R.id.iv_caipu);
        mIvCaipu.setOnClickListener(this);
        mIvBaike = (ImageView) findViewById(R.id.iv_baike);
        mIvBaike.setOnClickListener(this);
        mIvJisuan = (ImageView) findViewById(R.id.iv_jisuan);
        mIvJisuan.setOnClickListener(this);
        mIvCaimi = (ImageView) findViewById(R.id.iv_caimi);
        mIvCaimi.setOnClickListener(this);
        mIvShengxiao = (ImageView) findViewById(R.id.iv_shengxiao);
        mIvShengxiao.setOnClickListener(this);
        mIvTongfanCi = (ImageView) findViewById(R.id.iv_tongfan_ci);
        mIvTongfanCi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_jiemeng) {

            startActivity(new Intent(this, JieMengActivity.class));
        } else if (i == R.id.iv_jinjintian) {
            startActivity(new Intent(this, HistoryTodayActivity.class));
        } else if (i == R.id.iv_riqi_chaxun) {
            startActivity(new Intent(this, DateInquireActivity.class));
        } else if (i == R.id.iv_caipu) {
            startActivity(new Intent(this, CookBookActivity.class));
        } else if (i == R.id.iv_baike) {
            startActivity(new Intent(this, BaikeActivity.class));
        } else if (i == R.id.iv_jisuan) {
            startActivity(new Intent(this, CalculationActivity.class));
        } else if (i == R.id.iv_caimi) {
        } else if (i == R.id.iv_shengxiao) {
        } else if (i == R.id.iv_tongfan_ci) {
        }
    }
}
