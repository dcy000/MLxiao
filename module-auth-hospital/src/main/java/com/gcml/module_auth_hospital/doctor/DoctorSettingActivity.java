package com.gcml.module_auth_hospital.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.communication.InterFaceManagerment;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.update.IUpdateAgent;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

/**
 * Created by lenovo on 2019/1/15.
 */

public class DoctorSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar mTbDoctorSetting;
    private TextView mTvSettingWifi;
    private TextView mTvSettingVoice;
    private TextView mTvSettingKeyword;
    private TextView mTvSettingDoctorLogin;
    private TextView mTvSettingUserRegister;
    private TextView mTvSettingUserLogin;
    private TextView mTvSettingClear;
    private TextView mTvSettingUpdate;
    private TextView mTvSettingAbout;
    private TextView mTvSettingReset;
    private View mLineSettingReset;
    private TextView mTvSettingClearcache;
    private TextView mTvSettingExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setting);
        initView();
    }

    private void initView() {
        mTbDoctorSetting = (TranslucentToolBar) findViewById(R.id.tb_doctor_setting);
        mTvSettingWifi = (TextView) findViewById(R.id.tv_setting_wifi);
        mTvSettingWifi.setOnClickListener(this);
        mTvSettingVoice = (TextView) findViewById(R.id.tv_setting_voice);
        mTvSettingVoice.setOnClickListener(this);
        mTvSettingKeyword = (TextView) findViewById(R.id.tv_setting_keyword);
        mTvSettingDoctorLogin = (TextView) findViewById(R.id.tv_setting_doctor_login);
        mTvSettingDoctorLogin.setOnClickListener(this);
        mTvSettingUserRegister = (TextView) findViewById(R.id.tv_setting_user_register);
        mTvSettingUserRegister.setOnClickListener(this);
        mTvSettingUserLogin = (TextView) findViewById(R.id.tv_setting_user_login);
        mTvSettingUserLogin.setOnClickListener(this);
        mTvSettingClear = (TextView) findViewById(R.id.tv_setting_clear);
        mTvSettingClear.setOnClickListener(this);
        mTvSettingUpdate = (TextView) findViewById(R.id.tv_setting_update);
        mTvSettingUpdate.setOnClickListener(this);
        mTvSettingAbout = (TextView) findViewById(R.id.tv_setting_about);
        mTvSettingAbout.setOnClickListener(this);
        mTvSettingReset = (TextView) findViewById(R.id.tv_setting_reset);
        mTvSettingReset.setOnClickListener(this);
        mLineSettingReset = (View) findViewById(R.id.line_setting_reset);
        mTvSettingClearcache = (TextView) findViewById(R.id.tv_setting_clearcache);
        mTvSettingExit = (TextView) findViewById(R.id.tv_setting_exit);
        mTvSettingExit.setOnClickListener(this);

        mTbDoctorSetting.setData("设置", R.drawable.common_btn_back, "返回",
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_setting_wifi:
                CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
                break;
            case R.id.tv_setting_voice:
                CC.obtainBuilder("com.gcml.old.setting.voice").build().callAsync();
                break;
            case R.id.tv_setting_doctor_login:
                startActivity(new Intent(this, SelectDefualtLoginTypeActivity.class));
                break;
            case R.id.tv_setting_user_register:
                startActivity(new Intent(this, SelectUserDefualtLoginTypeActivity.class));
                break;
            case R.id.tv_setting_user_login:
                startActivity(new Intent(this, SelectUserDefualtLoginTypeActivity.class));
                break;
            case R.id.tv_setting_clear:
                showClearCacheDialog();
                break;
            case R.id.tv_setting_update:
                if (InterFaceManagerment.update != null) {
                    InterFaceManagerment.update.update(this);
                }
                break;
            case R.id.tv_setting_about:
                CC.obtainBuilder("com.gcml.old.setting.about").build().callAsync();
                break;
            case R.id.tv_setting_reset:
                showResetDialog();
                break;
            case R.id.tv_setting_exit:
                break;
        }
    }

    private void showClearCacheDialog() {
        new AlertDialog(this).builder()
                .setMsg("确认清除本地缓存吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void showResetDialog() {
        new AlertDialog(this).builder()
                .setMsg("确认恢复出厂设置吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //恢复出厂设置
                        UserSpHelper.clear(getApplicationContext());
                        CC.obtainBuilder("com.gcml.auth.face.deleteGroup")
                                .build()
                                .callAsync();
                        CC.obtainBuilder("com.gcml.auth.deleteUsers")
                                .build()
                                .callAsync();
                        CC.obtainBuilder("com.gcml.old.welcome")
                                .build()
                                .callAsync();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
