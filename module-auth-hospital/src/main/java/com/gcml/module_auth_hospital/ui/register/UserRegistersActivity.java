package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.sjtu.yifei.route.Routerfit;

public class UserRegistersActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imLoginByIdcard;
    private ImageView imLoginByFinger;
    private ImageView imLoginByFace;
    private ImageView imLoginByIdNumber;
    private TextView tvToRegister;
    private TranslucentToolBar authLoginsToobar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registers);
        initView();
    }

    private void initView() {
        authLoginsToobar = (TranslucentToolBar) findViewById(R.id.auth_registers_tb);
        imLoginByIdcard = (ImageView) findViewById(R.id.im_register_by_idcard);
        imLoginByIdcard.setOnClickListener(this);
        imLoginByFinger = (ImageView) findViewById(R.id.im_register_by_finger);
        imLoginByFinger.setOnClickListener(this);
        imLoginByFace = (ImageView) findViewById(R.id.im_register_by_face);
        imLoginByFace.setOnClickListener(this);
        imLoginByIdNumber = (ImageView) findViewById(R.id.im_register_by_id_number);
        imLoginByIdNumber.setOnClickListener(this);
        tvToRegister = (TextView) findViewById(R.id.tv_to_register);
        tvToRegister.setOnClickListener(this);

        authLoginsToobar.setData("用 户 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.auth_hospital_ic_setting, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.im_register_by_idcard) {
            startActivity(new Intent(this, ScanIdCardRegisterActivity.class));
        } else if (id == R.id.im_register_by_finger) {

        } else if (id == R.id.im_register_by_face) {

        } else if (id == R.id.im_register_by_id_number) {
            startActivity(new Intent(this, IDCardNuberRegisterActivity.class));
        } else if (id == R.id.tv_to_register) {

        }
    }

}
