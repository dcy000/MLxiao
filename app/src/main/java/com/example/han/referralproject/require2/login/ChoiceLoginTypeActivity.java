package com.example.han.referralproject.require2.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.require2.healthservice.BindHealthManageSystemActivity;
import com.example.han.referralproject.require2.register.activtiy.ChoiceIDCardRegisterTypeActivity;
import com.example.han.referralproject.require2.register.activtiy.RegisterByIdCardActivity;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceLoginTypeActivity extends BaseActivity {

    @BindView(R.id.im_login_by_idcard)
    ImageView imLoginByIdcard;
    @BindView(R.id.im_login_by_finger)
    ImageView imLoginByFinger;
    @BindView(R.id.im_login_by_face)
    ImageView imLoginByFace;
    @BindView(R.id.im_login_by_id_number)
    ImageView imLoginByIdNumber;
    @BindView(R.id.tv_to_register)
    TextView tvToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_idcard_login_type);
        ButterKnife.bind(this);
        initTitle();
        get3BindInfo();
        ActivityHelper.addActivity(this);
    }

    /**
     *
     */
    private void get3BindInfo() {
        NetworkApi.getBindAccountInfo(Utils.getDeviceId(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject object = new JSONObject(body);
                    if (!object.getBoolean("tag")) {
                        startActivity(new Intent(ChoiceLoginTypeActivity.this, BindHealthManageSystemActivity.class));
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("选 择 登 录 方 式");

        mLeftText.setVisibility(View.GONE);
        mLeftView.setVisibility(View.GONE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiceLoginTypeActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请选择登录方式");
    }

    @OnClick({R.id.im_login_by_idcard, R.id.im_login_by_finger, R.id.im_login_by_face, R.id.im_login_by_id_number, R.id.tv_to_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_login_by_idcard:
                //刷身份证 登录,注册公用界面 跳转刷身份证登录界面 intent传login
                startActivity(new Intent(this, RegisterByIdCardActivity.class)
                        .putExtra("login", true));
                break;
            case R.id.im_login_by_finger:
                T.show("敬请期待");
                break;
            case R.id.im_login_by_face:
                startActivity(new Intent(this, FaceLoginActivity.class).putExtra("from", "Welcome"));
                break;
            case R.id.im_login_by_id_number:
                startActivity(new Intent(this, LoginByIDCardNuberActivity.class));

                break;
            case R.id.tv_to_register:
                startActivity(new Intent(this, ChoiceIDCardRegisterTypeActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEnableListeningLoop(false);
        setDisableGlobalListen(true);
    }
}
