package com.ml.call.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ml.call.CallHelper;
import com.ml.call.NimAccountHelper;
import com.ml.call.R;
import com.ml.call.utils.T;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class DoctorSignInActivity extends AppCompatActivity {

    TextView mTvId;
    EditText mEtId;
    TextView mTvAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity_doctor_sign_in);
        mTvId = (TextView) findViewById(R.id.call_tv_id);
        mEtId = (EditText) findViewById(R.id.call_et_id);
        mTvAction = (TextView) findViewById(R.id.call_tv_action);
        mTvAction.setText("登录");
//        doctor_18940866150
//        doctor_18940866149
    }


    public void onActionClicked(View view) {
        String action = mTvAction.getText().toString();
        final String id = mEtId.getText().toString().trim();
        if ("登录".equals(action)) {
            mTvAction.setEnabled(false);
            mTvAction.setText("登录中...");
            NimAccountHelper.getInstance().login(id, "123456", new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {
                    mTvId.setText(id);
                    mEtId.setText("");
                    mTvAction.setText("呼叫");
                    mTvAction.setEnabled(true);
                }

                @Override
                public void onFailed(int code) {
                    T.show("登录失败");
                    mTvAction.setText("登录");
                    mTvAction.setEnabled(true);
                }

                @Override
                public void onException(Throwable exception) {
                    T.show("登录失败");
                    mTvAction.setText("登录");
                    mTvAction.setEnabled(true);
                }
            });
            return;
        }

        if ("呼叫".equals(action)
                && !TextUtils.isEmpty(id)) {
            CallHelper.launch(this, id);
        }
    }
}
