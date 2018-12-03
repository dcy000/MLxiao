package com.example.han.referralproject.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.iflytek.synthetize.MLVoiceSynthetize;
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

        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("医 生 咨 询");
        MLVoiceSynthetize.startSynthesize("主人，请点击选择签约医生或在线医生");
        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.doctor_yuyue:
                UserInfoBean user = Box.getSessionManager().getUser();
                if (TextUtils.isEmpty(user.doid)) {
                    Intent intent = new Intent(DoctorAskGuideActivity.this, OnlineDoctorListActivity.class);
                    intent.putExtra("flag", "contract");
                    startActivity(intent);
                } else {
                    if ("0".equals(user.state)) {
                        Intent intent = new Intent(DoctorAskGuideActivity.this, CheckContractActivity.class);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(DoctorAskGuideActivity.this, DoctorappoActivity.class));
                    }
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
        mDoctorYuyue = (ImageView) findViewById(R.id.doctor_yuyue);
        mDoctorYuyue.setOnClickListener(this);
        mDoctorZaixian = (ImageView) findViewById(R.id.doctor_zaixian);
        mDoctorZaixian.setOnClickListener(this);
    }
}
