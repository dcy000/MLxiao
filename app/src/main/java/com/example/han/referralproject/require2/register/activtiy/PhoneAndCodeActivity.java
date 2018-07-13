package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.wrap.PhoneVerificationCodeView;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.han.referralproject.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_ADDRESS;
import static com.example.han.referralproject.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_PHONE_NUMBER;
import static com.example.han.referralproject.require2.register.activtiy.InputFaceActivity.REGISTER_IDCARD_NUMBER;

public class PhoneAndCodeActivity extends BaseActivity implements PhoneVerificationCodeView.OnSendClickListener {
    public static final String FROM_WHERE = "from_where";
    public static final String FROM_REGISTER_BY_IDCARD = "register_by_idCard";
    public static final String FROM_REGISTER_BY_IDCARD_NUMBER = "register_by_idCard_number";
    @BindView(R.id.phone_view)
    PhoneVerificationCodeView phoneView;
    @BindView(R.id.tv_next)
    TextView tvNext;
    /**
     * 发手机号的验证码
     */
    private String phone = "";
    private String fromWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_and_code);
        ButterKnife.bind(this);
        intTitle();
        initEvent();
    }

    private void intTitle() {
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
                startActivity(new Intent(PhoneAndCodeActivity.this, WifiConnectActivity.class));
            }
        });

        fromWhere = getIntent().getStringExtra(FROM_WHERE);
        if (fromWhere.equals(FROM_REGISTER_BY_IDCARD)) {
            mTitleText.setText("身 份 证 扫 描 注 册");
        }
    }

    private void initEvent() {
        phoneView.setListener(this);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String code = phoneView.getCode();
        if (TextUtils.isEmpty(code)) {
            mlSpeak("请输入验证码");
            return;
        }

        if (!this.phone.equals(phoneView.getPhone())) {
            mlSpeak("验证码错误");
            return;
        }
        if (fromWhere.equals(FROM_REGISTER_BY_IDCARD)) {
            startActivity(new Intent(this, InputFaceActivity.class)
                    .putExtras(getIntent())
                    .putExtra(REGISTER_PHONE_NUMBER, phone));
        } else if (fromWhere.equals(FROM_REGISTER_BY_IDCARD_NUMBER)) {
            if (code.equals(this.code)) {
                startActivity(new Intent(PhoneAndCodeActivity.this, RealNameActivity.class)
                        .putExtra(REGISTER_PHONE_NUMBER, phone)
                        .putExtras(getIntent()));
            } else {
                mlSpeak("验证码错误");
            }
        }


    }

    private String code = "";

    @Override
    public void onSendCode(final String phone) {
        this.phone = phone;
        showLoadingDialog("正在获取验证码...");
        NetworkApi.canRegister(phone, "3", new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                hideLoadingDialog();
                NetworkApi.getCode(phone, new NetworkManager.SuccessCallback<String>() {

                    @Override
                    public void onSuccess(String codeJson) {
                        try {
                            JSONObject codeObj = new JSONObject(codeJson);
                            String code = codeObj.getString("code");
                            if (code != null) {
                                PhoneAndCodeActivity.this.code = code;
                                T.show("获取验证码成功");
                                mlSpeak("获取验证码成功");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            T.show("获取验证码失败");
                            mlSpeak("获取验证码失败");
                        }
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        T.show("获取验证码失败");
                        mlSpeak("获取验证码失败");
                    }
                });

            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                speak("手机号码已注册");
            }
        });

    }


    public void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
    }
}
