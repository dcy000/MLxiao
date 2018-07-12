package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDCardNumberRegisterActivity extends BaseActivity {

    @BindView(R.id.textView17)
    TextView textView17;
    @BindView(R.id.textView18)
    TextView textView18;
    @BindView(R.id.ccet_phone)
    CanClearEditText ccetPhone;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_number_register);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 注 册");

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.yiyua_wifi_icon);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IDCardNumberRegisterActivity.this, WifiConnectActivity.class));
            }
        });

        speak("请输入您的身份号码");
    }

    public void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String phone = ccetPhone.getPhone();
        if (TextUtils.isEmpty(phone)) {
            speak("请输入您的身份号码");
            return;
        }

        if (!Utils.checkIdCard1(phone)) {
            speak(R.string.sign_up_id_card_tip);
            return;
        }

        neworkCheckIdCard();

    }

    /**
     * 网络检测身份证是否注册
     */
    private void neworkCheckIdCard() {
        startActivity(new Intent(this, PhoneAndCodeActivity.class)
                .putExtra(PhoneAndCodeActivity.FROM_WHERE, PhoneAndCodeActivity.FROM_REGISTER_BY_IDCARD_NUMBER));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
    }
}
