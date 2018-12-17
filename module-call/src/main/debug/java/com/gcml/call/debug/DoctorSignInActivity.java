package com.gcml.call.debug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.call.CallAccountHelper;
import com.gcml.call.CallHelper;
import com.gcml.call.R;
import com.gcml.common.utils.display.ToastUtils;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;


public class DoctorSignInActivity extends AppCompatActivity {

    EditText etId;
    TextView btnLogin;
    EditText etPeerId;
    TextView btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity_doctor_sign_in);
        etId = (EditText) findViewById(R.id.call_et_id);
        btnLogin = (TextView) findViewById(R.id.call_btn_login);
        etPeerId = (EditText) findViewById(R.id.call_et_peer_id);
        btnCall = (TextView) findViewById(R.id.call_btn_call);
//        doctor_18940866150
//        doctor_18940866149
    }


    public void onLoginClicked(View view) {
        final String id = etId.getText().toString().trim();
        btnLogin.setEnabled(false);
        btnLogin.setText("登录中...");
        CallAccountHelper.INSTANCE.login(id, "123456", new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                ToastUtils.showShort("登录成功： " + id);
                btnLogin.setText("登录");
                btnLogin.setEnabled(true);
            }

            @Override
            public void onFailed(int code) {
                ToastUtils.showShort("登录失败： " + id);
                btnLogin.setText("登录");
                btnLogin.setEnabled(true);
            }

            @Override
            public void onException(Throwable exception) {
                ToastUtils.showShort("登录失败： " + id);
                btnLogin.setText("登录");
                btnLogin.setEnabled(true);
            }
        });

    }

    public void onCallClicked(View view) {
        final String id = etPeerId.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            ToastUtils.showShort("账号不能为空");
        }
        CallHelper.outgoingCall(this, id);
    }
}

