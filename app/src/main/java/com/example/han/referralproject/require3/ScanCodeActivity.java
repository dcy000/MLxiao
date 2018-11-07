package com.example.han.referralproject.require3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanCodeActivity extends BaseActivity {

    @BindView(R.id.tv_skip)
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);

        setEnableListeningLoop(false);
        setDisableGlobalListen(true);

        initTitle();

    }

    private void initTitle() {
        mTitleText.setText("扫码关注");
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanCodeActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请用手机扫描以下的二维码，关注公众号。");
    }


    @Override
    protected void onStop() {
        super.onStop();
        MLVoiceSynthetize.stop();
    }


    @OnClick(R.id.tv_skip)
    public void onViewClicked() {
        startActivity(new Intent(this, InquiryAndFileActivity.class)
                .putExtras(getIntent()));
    }
}
