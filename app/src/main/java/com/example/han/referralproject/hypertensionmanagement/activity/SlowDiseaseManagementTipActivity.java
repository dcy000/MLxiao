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

public class SlowDiseaseManagementTipActivity extends BaseActivity implements TwoChoiceDialog.OnDialogClickListener {
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
                showOriginHypertensionDialog();
                break;
            case R.id.tv_to_mall:
                // TODO: 2018/7/27 前往商城
                break;
        }
    }

    private void showOriginHypertensionDialog() {
        TwoChoiceDialog dialog = new TwoChoiceDialog("您是否诊断过原发性高血压且正在进行高血压规范治疗？", "是", "否");
        dialog.setListener(this);
        dialog.show(getFragmentManager(), "yuanfa");
    }

    @Override
    public void onClickConfirm(String content) {
        startActivity(new Intent(this, BasicInformationActivity.class));
    }

    @Override
    public void onClickCancel() {
        showLoadingDialog("");
        NetworkApi.getDiagnoseInfo(LocalShared.getInstance(this).getUserId(), new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        DiagnoseInfoBean bean = new Gson().fromJson(response.body(), DiagnoseInfoBean.class);
                        if (bean != null && bean.tag && bean.data != null) {
                            if (bean.data.detectionDayCount >= 3) {
                                String hypertensionLevel = bean.data.hypertensionLevel;
                                switch (hypertensionLevel) {

                                    case "00"://正常
                                        startActivity(new Intent(SlowDiseaseManagementTipActivity.this, PressureNornalTipActivity.class));
                                        break;
                                    case "01"://偏低
                                        startActivity(new Intent(SlowDiseaseManagementTipActivity.this, PressureNornalTipActivity.class));
                                        break;
                                    case "02"://正常高值
                                        startActivity(new Intent(SlowDiseaseManagementTipActivity.this, NormalHighTipActivity.class));
                                        break;
                                    case "11:1":
                                    case "12:1":
                                    case "13:1":
                                    case "22:2":
                                    case "23:2":
                                    case "33:3":
                                    case "34:3":
                                        startActivity(new Intent(SlowDiseaseManagementTipActivity.this, HypertensionTipActivity.class));
                                        break;
                                }

                            } else {
                                // TODO: 2018/7/27 提示流程结束 
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideLoadingDialog();
                    }
                }
        );
    }


}
