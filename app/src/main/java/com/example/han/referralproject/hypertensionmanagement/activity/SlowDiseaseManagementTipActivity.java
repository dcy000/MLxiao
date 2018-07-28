package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.bean.DiagnoseInfoBean;
import com.example.han.referralproject.hypertensionmanagement.dialog.TwoChoiceDialog;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlowDiseaseManagementTipActivity extends BaseActivity {
    public static final String CONTENT = "为给您更好健康方案，以下在方案制定过程中，" +
            "会提示你测量血压，血脂和血糖，以及身高，体重,腰围测量。如咨询的内容非实际情况，" +
            "或设备不满足，可能无法帮你做精确的健康方案。如需购买设备，请前往商城，如已有设备，" +
            "直接点击下一步。";
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
        mlSpeak(CONTENT);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("慢 病 管 理");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        tvContent.setText(CONTENT);
        mRightView.setOnClickListener(v -> startActivity(new Intent(SlowDiseaseManagementTipActivity.this, WifiConnectActivity.class)));
    }

    @OnClick({R.id.tv_next_step, R.id.tv_to_mall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next_step:
                startActivity(new Intent(this, BasicInformationActivity.class)
                .putExtra("fromWhere","tipHealthManage"));
                break;
            case R.id.tv_to_mall:
                // TODO: 2018/7/27 前往商城
                break;
        }
    }


}
