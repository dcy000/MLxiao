package com.example.han.referralproject.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.medlink.danbogh.utils.FastClickUtil;

public class DoctorAskGuideActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 预约健康顾问
     */
    private ImageView mDoctorYuyue;
    /**
     * 在线健康顾问
     */
    private ImageView mDoctorZaixian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_ask_guide);

        setEnableListeningLoop(false);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 顾 问");
        speak("请点击选择我的健康顾问或在线健康顾问");
        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.doctor_yuyue:
                if (FastClickUtil.isFastClick()) {
                    NetworkApi.PersonInfo(UserSpHelper.getUserId(),
                            new NetworkManager.SuccessCallback<UserInfo>() {
                                @Override
                                public void onSuccess(UserInfo userInfo) {
                                    if (userInfo == null) {
                                        return;
                                    }
                                    String state = userInfo.getState();
                                    if ("0".equals(state)) {
                                        if (TextUtils.isEmpty(userInfo.getDoctername())) {
                                            Intent intent = new Intent(DoctorAskGuideActivity.this, OnlineDoctorListActivity.class);
                                            intent.putExtra("flag", "contract");
                                            DoctorAskGuideActivity.this.startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(DoctorAskGuideActivity.this, CheckContractActivity.class);
                                            DoctorAskGuideActivity.this.startActivity(intent);
                                        }

                                    } else if ("1".equals(state)) {
                                        DoctorAskGuideActivity.this.startActivity(new Intent(DoctorAskGuideActivity.this, DoctorappoActivity2.class));
                                    }
                                }
                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {
                                    ToastUtils.showShort("账号已失效，请重新登录");
                                    CC.obtainBuilder("com.gcml.auth")
                                            .build()
                                            .call();
                                }
                            });
                }
                break;
            case R.id.doctor_zaixian:
                if (FastClickUtil.isFastClick()) {
                    Log.d("============", "click");
                    startActivity(new Intent(this, OnlineDoctorListActivity.class));
                }
                break;
        }
    }

    private void initView() {
        mDoctorYuyue = findViewById(R.id.doctor_yuyue);
        mDoctorYuyue.setOnClickListener(this);
        mDoctorZaixian = findViewById(R.id.doctor_zaixian);
        mDoctorZaixian.setOnClickListener(this);
    }
}
