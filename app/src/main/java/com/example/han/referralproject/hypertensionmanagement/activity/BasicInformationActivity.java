package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.medlink.danbogh.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasicInformationActivity extends BaseActivity {

    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.tv_name_info)
    TextView tvNameInfo;
    @BindView(R.id.tv_sex_info)
    TextView tvSexInfo;
    @BindView(R.id.tv_birth_info)
    TextView tvBirthInfo;
    @BindView(R.id.tv_height_info)
    TextView tvHeightInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initView() {
        NetworkApi.getMyBaseData(response -> {
            if (response != null) {
                tvNameInfo.setText(response.bname);
                tvSexInfo.setText(response.sex);
                tvHeightInfo.setText(response.height + "cm");
            }
        }, message -> ToastUtils.showShort(message));
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(v -> startActivity(new Intent(this, WifiConnectActivity.class)));
    }

    @OnClick({R.id.tv_next_step, R.id.tv_birth_info, R.id.tv_height_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next_step:
                break;
            case R.id.tv_birth_info:
                break;
            case R.id.tv_height_info:
                break;
        }
    }
}
