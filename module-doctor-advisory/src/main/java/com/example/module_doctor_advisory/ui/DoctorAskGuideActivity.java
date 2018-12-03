package com.example.module_doctor_advisory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.module_doctor_advisory.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class DoctorAskGuideActivity extends ToolbarBaseActivity implements View.OnClickListener {
    /**
     * 预约医生
     */
    private ImageView mDoctorYuyue;
    /**
     * 在线医生
     */
    private ImageView mDoctorZaixian;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_doctor_ask_guide;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceSynthetize.startSynthesize("主人，请点击选择签约医生或在线医生");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.doctor_yuyue) {
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

        } else if (i == R.id.doctor_zaixian) {
            startActivity(new Intent(this, OnlineDoctorListActivity.class));

        } else {
        }
    }

    @Override
    public void initView() {
        mTitleText.setText("医 生 咨 询");
        mDoctorYuyue = (ImageView) findViewById(R.id.doctor_yuyue);
        mDoctorYuyue.setOnClickListener(this);
        mDoctorZaixian = (ImageView) findViewById(R.id.doctor_zaixian);
        mDoctorZaixian.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }
}
