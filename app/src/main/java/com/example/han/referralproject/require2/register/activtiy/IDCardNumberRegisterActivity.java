package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.idcard.SignInIdCardActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDCardNumberRegisterActivity extends BaseActivity implements CanClearEditText.OnTextChangeListener {

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
        ActivityHelper.addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 注 册");

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IDCardNumberRegisterActivity.this, WifiConnectActivity.class));
            }
        });

        speak("请输入您的身份证号码");
        ccetPhone.setListener(this);
    }

    public void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String phone = ccetPhone.getPhone();
        if (TextUtils.isEmpty(phone)) {
            speak("请输入您的身份证号码");
            return;
        }

        if (!Utils.checkIdCard1(phone)) {
            speak(R.string.sign_up_id_card_tip);
            return;
        }

        neworkCheckIdCard(phone);

    }

    /**
     * 网络检测身份证是否注册
     */
    private void neworkCheckIdCard(final String idCardNumber) {

        NetworkApi.isRegisteredByIdCard(idCardNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                T.show("您输入的身份证号码已注册,请重新注册");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                startActivity(new Intent(IDCardNumberRegisterActivity.this, PhoneAndCodeActivity.class)
                        .putExtra(PhoneAndCodeActivity.FROM_WHERE, PhoneAndCodeActivity.FROM_REGISTER_BY_IDCARD_NUMBER).putExtra(REGISTER_IDCARD_NUMBER, idCardNumber));
//
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
    }

    public static final String REGISTER_IDCARD_NUMBER = "registerIdCardNumber";
    public static final String REGISTER_PHONE_NUMBER = "registerPhoneNumber";
    public static final String REGISTER_REAL_NAME = "registeRrealName";
    public static final String REGISTER_SEX = "registerSex";
    public static final String REGISTER_ADDRESS = "registerAddress";

    @Override
    public void onTextChange(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
        }
    }
}
