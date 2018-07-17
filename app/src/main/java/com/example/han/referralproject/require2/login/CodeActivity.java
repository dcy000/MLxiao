package com.example.han.referralproject.require2.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileActivity;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeActivity extends BaseActivity {


    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.textView17)
    TextView textView17;
    private String phoneNumber;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        ButterKnife.bind(this);
        intTitle();
        initView();
    }

    private void initView() {
        phoneNumber = getIntent().getStringExtra("phone");
        String phoneStar = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        textView17.setText("请输入手机" + phoneStar + "收到的验证码");
        tvSendCode.setSelected(true);
    }

    private void intTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 登 录");

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.yiyua_wifi_icon);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入手机验证码登录");
    }


    @Override
    protected void onResume() {
        super.onResume();
        setEnableListeningLoop(false);
        setDisableGlobalListen(true);
    }

    @OnClick({R.id.tv_next, R.id.tv_send_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                next();
                break;
            case R.id.tv_send_code:
                sendCode();
                break;
        }
    }

    private void next() {
        final String phoneCode = etCode.getText().toString();
        if (TextUtils.isEmpty(phoneCode)) {
            mlSpeak("请输入手机验证码");
        }
        if (phoneCode.equals(code)) {
            startActivity(new Intent(CodeActivity.this, InquiryAndFileActivity.class));
        }
    }

    private void sendCode() {
        tvSendCode.setSelected(false);
        Handlers.ui().postDelayed(new Runnable() {
            @Override
            public void run() {
                count--;
                if (count <= 0) {
                    tvSendCode.setSelected(true);
                    tvSendCode.setText("发送验证码");
                    count = 60;
                    return;
                }
                tvSendCode.setText(count + "秒重发");
                Handlers.ui().postDelayed(this, 1000);
            }
        }, 1000);

        //请求验证码
        showLoadingDialog("");
        NetworkApi.getCode(phoneNumber, new NetworkManager.SuccessCallback<String>() {

            @Override
            public void onSuccess(String codeJson) {
                hideLoadingDialog();
                try {
                    JSONObject codeObj = new JSONObject(codeJson);
                    String code = codeObj.getString("code");
                    CodeActivity.this.code = code;
                    if (code != null) {
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
                hideLoadingDialog();
                T.show("获取验证码失败");
                mlSpeak("获取验证码失败");
            }
        });

    }

    private int count = 60;
}
