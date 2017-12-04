package com.example.han.referralproject.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.constant.ConstantData;

public class DoctorAskGuideActivity extends BaseActivity implements View.OnClickListener {


    private SharedPreferences sharedPreferences;
    /**
     * 预约医生
     */
    private ImageView mDoctorYuyue;
    /**
     * 在线医生
     */
    private ImageView mDoctorZaixian;
    private ImageView mIconBack;
    private LinearLayout mLinearlayou;
    private ImageView mIconHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_ask_guide);
        initView();
        sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.doctor_yuyue:
                startActivity(new Intent(this, DoctorappoActivity.class));
                break;
            case R.id.doctor_zaixian:
                startActivity(new Intent(this, OnlineDoctorListActivity.class));
                break;

            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void initView() {
        mDoctorYuyue = (ImageView) findViewById(R.id.doctor_yuyue);
        mDoctorYuyue.setOnClickListener(this);
        mDoctorZaixian = (ImageView) findViewById(R.id.doctor_zaixian);
        mDoctorZaixian.setOnClickListener(this);
        mIconBack = (ImageView) findViewById(R.id.icon_back);
        mIconBack.setOnClickListener(this);
        mLinearlayou = (LinearLayout) findViewById(R.id.linearlayou);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);
    }
}