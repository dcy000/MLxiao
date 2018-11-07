package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.util.LocalShared;

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
    @BindView(R.id.wu11)
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
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guo_min_and_yao_wu);
        ButterKnife.bind(this);
        initTilte();
        speak("主人,您是与否有过敏史和疾病史");
    }


    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
    }


    @OnClick({R.id.title1, R.id.qingmeisu, R.id.toubao, R.id.huanganlei, R.id.qita, R.id.ll_1, R.id.wu11, R.id.title2, R.id.gaouxeya, R.id.tangniaobing, R.id.shexian, R.id.xiaochuan, R.id.ll_2, R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward, R.id.qita2, R.id.wu2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qingmeisu:
                qingmeisu.setSelected(!qingmeisu.isSelected());
                wu1.setSelected(false);
                break;
            case R.id.toubao:
                toubao.setSelected(!toubao.isSelected());
                wu1.setSelected(false);
                break;
            case R.id.huanganlei:
                huanganlei.setSelected(!huanganlei.isSelected());
                wu1.setSelected(false);
                break;
            case R.id.qita:
                qita.setSelected(!qita.isSelected());
                wu1.setSelected(false);
                break;
            case R.id.wu1:
                wu1.setSelected(!wu1.isSelected());
                qingmeisu.setSelected(false);
                toubao.setSelected(false);
                huanganlei.setSelected(false);
                qita.setSelected(false);
                break;
            case R.id.gaouxeya:
                gaouxeya.setSelected(!gaouxeya.isSelected());
                wu2.setSelected(false);
                break;
            case R.id.tangniaobing:
                tangniaobing.setSelected(!tangniaobing.isSelected());
                wu2.setSelected(false);
                break;
            case R.id.shexian:
                //guanxinbing
                shexian.setSelected(!shexian.isSelected());
                wu2.setSelected(false);
                break;
            case R.id.xiaochuan:
                xiaochuan.setSelected(!xiaochuan.isSelected());
                wu2.setSelected(false);
                break;
            case R.id.qita2:
                qita2.setSelected(!qita2.isSelected());
                wu2.setSelected(false);
                break;
            case R.id.wu2:
                wu2.setSelected(!wu2.isSelected());

                gaouxeya.setSelected(false);
                tangniaobing.setSelected(false);
                shexian.setSelected(false);
                xiaochuan.setSelected(false);
                qita2.setSelected(false);
                break;

            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                getGuoMin();
                getJiBing();
                nextStep();

                break;
        }
    }

    private void nextStep() {

        if (TextUtils.isEmpty(getGuoMin())){
            speak("主人,您是否有过敏史");
            return;
        }

        if (TextUtils.isEmpty(getJiBing())){
            speak("主人,您是否有疾病史");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("from", "Test");
        intent.putExtra("fromType", "xueya");
        intent.putExtra("inquiry", true);
        intent.setClass(this, DetectActivity.class);
        startActivity(intent);
    }


    public String getGuoMin() {
        String guomin = "";
        if (wu1.isSelected()) {
            guomin = "0";
            LocalShared.getInstance(this).setGuoMin(guomin);
            return "0";
        }

        if (qingmeisu.isSelected()) {
            guomin += "1,";
        }

        if (toubao.isSelected()) {
            guomin += "2,";
        }

        if (huanganlei.isSelected()) {
            guomin += "3,";
        }
        if (qita.isSelected()) {
            guomin += "4";
        }

        LocalShared.getInstance(this).setGuoMin(guomin);

        return guomin;
    }

    private String  getJiBing() {
        String jibing = "";
        if (wu2.isSelected()) {
            jibing = "0";
            LocalShared.getInstance(this).setJiBingShi(jibing);
            return "0";
        }

        if (gaouxeya.isSelected()) {
            jibing += "1,";
        }

        if (tangniaobing.isSelected()) {
            jibing += "2,";
        }

        if (shexian.isSelected()) {
            jibing += "3,";
        }

        if (xiaochuan.isSelected()) {
            jibing += "4,";
        }

        if (qita2.isSelected()) {
            jibing += "5,";
        }
        LocalShared.getInstance(this).setJiBingShi(jibing);
        return jibing;
    }

}
