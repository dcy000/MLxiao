package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.gcml.common.data.AppManager;
import com.gcml.common.utils.UM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlowDiseaseManagementTipActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.tv_to_mall)
    TextView tvToMall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow_disease_management_tip);
        ButterKnife.bind(this);
        initTitle();
        mlSpeak(UM.getString(R.string.voice_health_manager));
        AppManager.getAppManager().addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(R.string.title_health_manager);
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
        tvContent.setText(R.string.connect_disase);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(SlowDiseaseManagementTipActivity.this, WifiConnectActivity.class)));
    }

    @OnClick({R.id.tv_next_step, R.id.tv_to_mall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next_step:
                startActivity(new Intent(this, BasicInformationActivity.class)
                        .putExtra("fromWhere", "tipHealthManage"));
                break;
            case R.id.tv_to_mall:
                startActivity(new Intent(this, MarketActivity.class));
                break;
        }
    }


}
