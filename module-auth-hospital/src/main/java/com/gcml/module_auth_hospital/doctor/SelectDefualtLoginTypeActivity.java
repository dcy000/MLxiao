package com.gcml.module_auth_hospital.doctor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

/**
 * Created by lenovo on 2019/1/14.
 */

public class SelectDefualtLoginTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar tbSelectDoctorDefaultLogin;
    private RelativeLayout rlFaceLogin;
    private RelativeLayout rlAccountLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor_default_login);
        initView();

    }

    private void initView() {
        tbSelectDoctorDefaultLogin = (TranslucentToolBar) findViewById(R.id.tb_select_doctor_default_login);
        rlFaceLogin = (RelativeLayout) findViewById(R.id.rl_face_login);
        rlFaceLogin.setOnClickListener(this);
        rlAccountLogin = (RelativeLayout) findViewById(R.id.rl_account_login);
        rlAccountLogin.setOnClickListener(this);

        tbSelectDoctorDefaultLogin.setData("选 择 默 认 优 先 登 录 方 式",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
                    }
                });
        seletDefaultLogin(true);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_face_login) {
            seletDefaultLogin(true);
        } else if (id == R.id.rl_account_login) {
            seletDefaultLogin(false);
        }
    }


    private void seletDefaultLogin(Boolean selectedFaceLogin) {
        int faceChildCount = rlFaceLogin.getChildCount();
        for (int i = 0; i < faceChildCount; i++) {
            rlFaceLogin.getChildAt(i).setSelected(selectedFaceLogin);
        }

        int accountLoginCount = rlAccountLogin.getChildCount();
        for (int i = 0; i < accountLoginCount; i++) {
            rlAccountLogin.getChildAt(i).setSelected(!selectedFaceLogin);
        }
    }
}
