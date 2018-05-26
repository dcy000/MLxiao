package com.example.han.referralproject.yiyuan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuoMinAndJiBingActivity extends BaseActivity {

    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.qingmeisu)
    TextView qingmeisu;
    @BindView(R.id.toubao)
    TextView toubao;
    @BindView(R.id.huanganlei)
    TextView huanganlei;
    @BindView(R.id.qita)
    TextView qita;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.wu1)
    TextView wu1;
    @BindView(R.id.title2)
    TextView title2;
    @BindView(R.id.gaouxeya)
    TextView gaouxeya;
    @BindView(R.id.tangniaobing)
    TextView tangniaobing;
    @BindView(R.id.shexian)
    TextView shexian;
    @BindView(R.id.xiaochuan)
    TextView xiaochuan;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    @BindView(R.id.qita2)
    TextView qita2;
    @BindView(R.id.wu2)
    TextView wu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guo_min_and_yao_wu);
        ButterKnife.bind(this);
        initTilte();
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }


    @OnClick({R.id.title1, R.id.qingmeisu, R.id.toubao, R.id.huanganlei, R.id.qita, R.id.ll_1, R.id.wu1, R.id.title2, R.id.gaouxeya, R.id.tangniaobing, R.id.shexian, R.id.xiaochuan, R.id.ll_2, R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward, R.id.qita2, R.id.wu2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title1:
                break;
            case R.id.qingmeisu:
                break;
            case R.id.toubao:
                break;
            case R.id.huanganlei:
                break;
            case R.id.qita:
                break;
            case R.id.ll_1:
                break;
            case R.id.wu1:
                break;
            case R.id.title2:
                break;
            case R.id.gaouxeya:
                break;
            case R.id.tangniaobing:
                break;
            case R.id.shexian:
                break;
            case R.id.xiaochuan:
                break;
            case R.id.ll_2:
                break;
            case R.id.tv_sign_up_go_back:
                break;
            case R.id.tv_sign_up_go_forward:
                break;
            case R.id.qita2:
                break;
            case R.id.wu2:
                break;
        }
    }
}
