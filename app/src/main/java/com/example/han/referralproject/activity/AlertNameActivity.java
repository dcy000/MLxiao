package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertNameActivity extends BaseActivity {
    @BindView(R.id.tv_sign_up_name)
    TextView tvSignUpName;
    @BindView(R.id.et_sign_up_name)
    EditText etSignUpName;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    @BindView(R.id.cl_sign_up_root_name)
    ConstraintLayout clSignUpRootName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_name);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改姓名");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
    }

    @OnClick({R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                String name = etSignUpName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    speak("请输入姓名");
                }
                // TODO: 2018/8/9 调用接口
                break;
        }
    }
}
