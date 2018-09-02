package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.util.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlowDiseaseManagementTipActivity extends BaseActivity {
    public static final String CONTENT = "为给您提供更好的健康方案，在方案制定过程中，" +
            "请您根据提示测量血压、血脂、血糖、身高、体重、以及腰围。若咨询的内容非实际情况，" +
            "或缺少设备，会影响到方案的精确性。如需购买设备，请前往商城，如已有设备，请直接点击下一步。";
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
        mlSpeak("主人，小E来帮您控制血压吧。");
        AppManager.getAppManager().addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("慢 病 管 理");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
        tvContent.setText(CONTENT);
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
