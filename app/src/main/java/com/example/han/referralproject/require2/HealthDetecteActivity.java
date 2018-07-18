package com.example.han.referralproject.require2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthDetecteActivity extends BaseActivity {

    @BindView(R.id.im_pay_item)
    ImageView imPayItem;
    @BindView(R.id.im_pressure_fllow_up)
    ImageView imPressureFllowUp;
    @BindView(R.id.im_health_detecte)
    ImageView imHealthDetecte;
    @BindView(R.id.im_sugar_fllow_up)
    ImageView imSugarFllowUp;
    @BindView(R.id.textView21)
    TextView textView21;
    @BindView(R.id.textView22)
    TextView textView22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_detecte);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 检 测");
        speak("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //关闭唤醒
        setDisableGlobalListen(true);
        //关闭语音实时识别
        setEnableListeningLoop(false);
    }

    @OnClick({R.id.im_pay_item, R.id.im_pressure_fllow_up, R.id.im_health_detecte, R.id.im_sugar_fllow_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_pay_item:

                break;
            case R.id.im_pressure_fllow_up:
                break;
            case R.id.im_health_detecte:
                break;
            case R.id.im_sugar_fllow_up:
                break;
        }
    }
}
