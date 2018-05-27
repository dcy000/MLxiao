package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YueJingWenActivity extends BaseActivity {

    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.tv_yuejing_date)
    EditText tvYuejingDate;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_jing_wen);
        ButterKnife.bind(this);
        initTilte();
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

    @OnClick({R.id.tv_yuejing_date, R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_yuejing_date:
                break;
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                goForward();
                break;
        }
    }

    private void goForward() {
        String time = tvYuejingDate.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            speak("主人,日期不能为空");
            return;
        }
        LocalShared.getInstance(this.getApplicationContext()).setYueJingDate(time);
        startActivity(new Intent(this, DrinkWenActivity.class));

    }
}
