package com.example.han.referralproject.yisuotang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.tool.BaiKeActivtiy;
import com.example.han.referralproject.tool.CalculationActivity;
import com.example.han.referralproject.tool.CookBookActivity;
import com.example.han.referralproject.tool.DateInquireActivity;
import com.example.han.referralproject.tool.HistoryTodayActivity;
import com.example.han.referralproject.tool.JieMengActivity;
import com.example.han.referralproject.tool.RiddleActivity;
import com.ml.edu.OldRouter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecreationCenterActivity extends BaseActivity {

    @BindView(R.id.iv_jiemeng)
    ImageView ivJiemeng;
    @BindView(R.id.iv_jinjintian)
    ImageView ivJinjintian;
    @BindView(R.id.iv_riqi_chaxun)
    ImageView ivRiqiChaxun;
    @BindView(R.id.iv_caipu)
    ImageView ivCaipu;
    @BindView(R.id.iv_baike)
    ImageView ivBaike;
    @BindView(R.id.iv_jisuan)
    ImageView ivJisuan;
    @BindView(R.id.iv_caimi)
    ImageView ivCaimi;
    @BindView(R.id.iv_shengxiao)
    ImageView ivShengxiao;
    @BindView(R.id.iv_tongfan_ci)
    ImageView ivTongfanCi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreation_center);
        ButterKnife.bind(this);
        initTitle();
        speak("主人,欢迎来到娱乐中心");
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("娱乐中心");
    }


    @OnClick({R.id.iv_jiemeng, R.id.iv_jinjintian, R.id.iv_riqi_chaxun, R.id.iv_caipu, R.id.iv_baike, R.id.iv_jisuan, R.id.iv_caimi, R.id.iv_shengxiao, R.id.iv_tongfan_ci})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_jiemeng:
                startActivity(new Intent(this, JieMengActivity.class));
                break;
            case R.id.iv_jinjintian:
                startActivity(new Intent(this, HistoryTodayActivity.class));
                break;
            case R.id.iv_riqi_chaxun:
                startActivity(new Intent(this, DateInquireActivity.class));
                break;
            case R.id.iv_caipu:
                startActivity(new Intent(this, CookBookActivity.class));
                break;
            case R.id.iv_baike:
                startActivity(new Intent(this, BaiKeActivtiy.class));
                break;
            case R.id.iv_jisuan:
                startActivity(new Intent(this, CalculationActivity.class));
                break;
            case R.id.iv_caimi:
                startActivity(new Intent(this, RiddleActivity.class));
                break;
            case R.id.iv_shengxiao:
                OldRouter.routeToOldHomeActivity(this);
                break;
            case R.id.iv_tongfan_ci:
                startActivity(new Intent(this, ChildEduHomeActivity.class));
                break;
        }
    }
}
