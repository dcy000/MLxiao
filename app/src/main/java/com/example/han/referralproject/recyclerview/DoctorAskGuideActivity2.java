package com.example.han.referralproject.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.medlink.danbogh.utils.FastClickUtil;

public class DoctorAskGuideActivity2 extends BaseActivity implements View.OnClickListener {
    /**
     * 预约健康顾问
     */
    private ImageView mDoctorYuyue;
    /**
     * 在线健康顾问
     */
    private ImageView mDoctorZaixian;
    private ImageView mCallFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_ask_guide2);

        setEnableListeningLoop(false);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("视  频  咨  询");
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
                            userInfo -> {
                                if (userInfo == null) {
                                    return;
                                }
                                String state = userInfo.getState();
                                if ("0".equals(state)) {
                                    if (TextUtils.isEmpty(userInfo.getDoctername())) {
                                        Intent intent = new Intent(DoctorAskGuideActivity2.this, OnlineDoctorListActivity.class);
                                        intent.putExtra("flag", "contract");
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(DoctorAskGuideActivity2.this, CheckContractActivity.class);
                                        startActivity(intent);
                                    }

                                } else if ("1".equals(state)) {
                                    startActivity(new Intent(DoctorAskGuideActivity2.this, DoctorappoActivity2.class));
                                }
                            }, message -> ToastUtils.showShort(message));
                }
                break;
            case R.id.doctor_zaixian:
                if (FastClickUtil.isFastClick()) {
                    Log.d("============", "click");
                    startActivity(new Intent(this, OnlineDoctorListActivity.class).putExtra("title", "在线健康顾问"));
                }
                break;
            case R.id.call_family:
                startActivity(new Intent(this, OnlineDoctorListActivity.class).putExtra("title", "联 系 家 人"));
                break;
        }
    }

    private void initView() {
        mDoctorYuyue = findViewById(R.id.doctor_yuyue);
        mDoctorYuyue.setOnClickListener(this);
        mDoctorZaixian = findViewById(R.id.doctor_zaixian);
        mDoctorZaixian.setOnClickListener(this);
        mCallFamily = findViewById(R.id.call_family);
        mCallFamily.setOnClickListener(this);
    }
}
