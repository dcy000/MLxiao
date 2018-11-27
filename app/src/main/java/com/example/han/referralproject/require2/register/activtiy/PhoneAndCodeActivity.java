package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.wrap.PhoneVerificationCodeView;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.han.referralproject.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_PHONE_NUMBER;

public class PhoneAndCodeActivity extends BaseActivity implements PhoneVerificationCodeView.OnSendClickListener {
    public static final String FROM_WHERE = "from_where";
    public static final String FROM_REGISTER_BY_IDCARD = "register_by_idCard";
    public static final String FROM_REGISTER_BY_IDCARD_NUMBER = "register_by_idCard_number";
    public static final String DEFAULT_CODE = "8888";
    @BindView(R.id.phone_view)
    PhoneVerificationCodeView phoneView;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_code_phone_add_contact)
    TextView tvCodePhoneAddContact;
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
        mlSpeak("请输入手机号码,点击发送验证码");
        ActivityHelper.addActivity(this);
    }

    private void intTitle() {
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

        tvCodePhoneAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneAndCodeActivity.this,
                        AddcontactPhoneActivity.class).putExtras(getIntent()));
            }
        });
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        if (TextUtils.isEmpty(phoneView.getPhone())) {
            mlSpeak("请输入手机号码");
            return;
        }

        String code = phoneView.getCode();
        if (TextUtils.isEmpty(code)) {
            mlSpeak("请输入验证码");
            return;
        }

        if (!isSendedCode) {
            mlSpeak("请点击按钮发送验证码");
            return;
        }

        if (!this.phone.equals(phoneView.getPhone())) {
            mlSpeak("验证码错误");
            return;
        }

        if (fromWhere.equals(FROM_REGISTER_BY_IDCARD)) {
            if (code.equals(this.code) || DEFAULT_CODE.equals(code)) {
                startActivity(new Intent(this, InputFaceActivity.class)
                        .putExtras(getIntent())
                        .putExtra(REGISTER_PHONE_NUMBER, phone));
            } else {
                mlSpeak("验证码错误");
            }
        } else if (fromWhere.equals(FROM_REGISTER_BY_IDCARD_NUMBER)) {
            if (code.equals(this.code) || DEFAULT_CODE.equals(code)) {
                startActivity(new Intent(PhoneAndCodeActivity.this, RealNameActivity.class)
                        .putExtra(REGISTER_PHONE_NUMBER, phone)
                        .putExtras(getIntent()));
            } else {
                mlSpeak("验证码错误");
            }
        }


    }

    private String code = "";
    private boolean isSendedCode = false;

    @Override
    public void onSendCode(final String phone) {
        isSendedCode = true;
        this.phone = phone;
        getCode(phone);
       /* showLoadingDialog("正在获取验证码...");
        NetworkApi.canRegister(phone, "3", new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {

            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                T.show("该手机号码已使用,请确认后重新使用");
            }
        });*/

    }

    private void getCode(String phone) {
        showLoadingDialog("");
        NetworkApi.getCode(phone, new NetworkManager.SuccessCallback<String>() {

            @Override
            public void onSuccess(String codeJson) {
                hideLoadingDialog();
                try {
                    JSONObject codeObj = new JSONObject(codeJson);
                    String code = codeObj.getString("code");
                    if (code != null) {
                        PhoneAndCodeActivity.this.code = code;
                        T.show("获取验证码成功");
//                                mlSpeak("获取验证码成功");
                        phoneView.countDown();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    T.show("获取验证码失败");
//                            mlSpeak("获取验证码失败");
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show("获取验证码失败");
                hideLoadingDialog();
//                        mlSpeak("获取验证码失败");
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
