package com.medlink.danbogh.call2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.medlink.danbogh.utils.T;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DoctorSignInActivity extends AppCompatActivity {

    @BindView(R.id.tv_id)
    TextView mTvId;
    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.tv_action)
    TextView mTvAction;
    public Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_in);
        mUnbinder = ButterKnife.bind(this);
        mTvAction.setText("登录");
    }

    @OnClick(R.id.tv_action)
    public void onActionClicked() {
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
            NimCallActivity.launch(this, id);
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
