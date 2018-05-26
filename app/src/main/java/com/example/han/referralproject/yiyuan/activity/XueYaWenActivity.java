package com.example.han.referralproject.yiyuan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XueYaWenActivity extends BaseActivity {

    @BindView(R.id.tv_yueya_wen)
    TextView tvYueyaWen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xue_ya_wen);
        ButterKnife.bind(this);
        initTilte();
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

    @OnClick(R.id.tv_yueya_wen)
    public void onViewClicked() {
    }
}
