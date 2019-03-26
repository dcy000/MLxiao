package com.example.han.referralproject.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
     * 预约医生
     */
    private ImageView mDoctorYuyue;
    /**
     * 在线医生
     */
    private ImageView mDoctorZaixian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_ask_guide);

        setEnableListeningLoop(false);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("咨 询 医 生");
        speak("请点击选择签约医生或在线医生");
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
                            userInfo -> {
                                if (userInfo == null) {
                                    return;
                                }
                                String state = userInfo.getState();
                                if ("0".equals(state)) {
                                    if (TextUtils.isEmpty(userInfo.getDoctername())) {
                                        Intent intent = new Intent(DoctorAskGuideActivity.this, OnlineDoctorListActivity.class);
                                        intent.putExtra("flag", "contract");
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(DoctorAskGuideActivity.this, CheckContractActivity.class);
                                        startActivity(intent);
                                    }

                                } else if ("1".equals(state)) {
                                    startActivity(new Intent(DoctorAskGuideActivity.this, DoctorappoActivity2.class));
                                }
                            }, message -> ToastUtils.showShort(message));
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
