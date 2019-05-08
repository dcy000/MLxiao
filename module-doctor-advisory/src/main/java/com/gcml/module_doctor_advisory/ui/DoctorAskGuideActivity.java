package com.gcml.module_doctor_advisory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_doctor_advisory.R;
import com.gcml.module_doctor_advisory.net.QianYueRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/doctor/advisory/doctor/ask/guide/activity")
public class DoctorAskGuideActivity extends ToolbarBaseActivity implements View.OnClickListener {
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
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("视  频  咨  询");
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请点击选择我的健康顾问或在线健康顾问");
        initView();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.doctor_yuyue) {
            new QianYueRepository()
                    .PersonInfo(UserSpHelper.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity userEntity) {
                            if (userEntity == null) {
                                return;
                            }
                            if (TextUtils.isEmpty(userEntity.doctorId)) {
                                Intent intent = new Intent(DoctorAskGuideActivity.this, OnlineDoctorListActivity.class);
                                intent.putExtra("flag", "contract");
                                intent.putExtra("title", "签 约 医 生");
                                startActivity(intent);
                            } else {
                                if ("0".equals(userEntity.state)) {
                                    Intent intent = new Intent(DoctorAskGuideActivity.this, CheckContractActivity.class);
                                    startActivity(intent);
                                } else {
                                    startActivity(new Intent(DoctorAskGuideActivity.this, DoctorappoActivity2.class));
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (i == R.id.doctor_zaixian) {
            startActivity(new Intent(this, OnlineDoctorListActivity.class));
        } else {
        }
    }

    private void initView() {
        mDoctorYuyue = findViewById(R.id.doctor_yuyue);
        mDoctorYuyue.setOnClickListener(this);
        mDoctorZaixian = findViewById(R.id.doctor_zaixian);
        mDoctorZaixian.setOnClickListener(this);
    }
}
