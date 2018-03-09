package com.example.han.referralproject.settting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.rl_voice_set)
    RelativeLayout rlVoiceSet;
    @BindView(R.id.rl_wifi_set)
    RelativeLayout rlWifiSet;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.rl_reset)
    RelativeLayout rlReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        speak("主人欢迎来到设置页面");
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("设置");
    }

    @OnClick({R.id.rl_voice_set, R.id.rl_wifi_set, R.id.rl_clear_cache, R.id.rl_update, R.id.rl_about, R.id.rl_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_voice_set:
                //声音设置
                startActivity(new Intent(this, VoiceSettingActivity.class));
                break;
            case R.id.rl_wifi_set:
                //设置页面
                startActivity(new Intent(this, WifiConnectActivity.class));
                break;
            case R.id.rl_clear_cache:
                //清理缓存
                break;
            case R.id.rl_update:
                //检测更新
                break;
            case R.id.rl_about:
                //关于

                break;
            case R.id.rl_reset:
                //恢复出厂设置
                break;
        }
    }
}
