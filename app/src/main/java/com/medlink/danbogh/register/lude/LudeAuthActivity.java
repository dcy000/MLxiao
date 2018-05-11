package com.medlink.danbogh.register.lude;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.activity.ChooseRegisterTypeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.util.LocalShared;

public class LudeAuthActivity extends AppCompatActivity {

    private ImageView mIvBack;
    private ImageView mIvWifi;
    private ImageView mIvNewUser;
    private ImageView mIvHasUser;
    private TextView tvNetworkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lude_activity_auth);
        mIvBack = (ImageView) findViewById(R.id.auth_iv_back);
        mIvWifi = (ImageView) findViewById(R.id.auth_iv_wifi);
        mIvNewUser = (ImageView) findViewById(R.id.auth_iv_new_user);
        mIvHasUser = (ImageView) findViewById(R.id.auth_iv_has_user);
        tvNetworkMode = (TextView) findViewById(R.id.auth_tv_network_mode);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LudeAuthActivity.this, WifiConnectActivity.class);
                startActivity(intent);
            }
        });
        mIvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LudeAuthActivity.this, ChooseRegisterTypeActivity.class);
                startActivity(intent);
            }
        });
        mIvHasUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LudeAuthActivity.this, ChooseLoginTypeActivity.class);
                startActivity(intent);
            }
        });

        String netless = LocalShared.getInstance(this).getString("netless");
        tvNetworkMode.setText(TextUtils.isEmpty(netless) ? "有网模式" : "无网模式");
        tvNetworkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String netless = LocalShared.getInstance(v.getContext()).getString("netless");
                LocalShared.getInstance(v.getContext()).setString("netless", TextUtils.isEmpty(netless) ? "1" : "");
                tvNetworkMode.setText(TextUtils.isEmpty(netless) ? "无网模式" : "有网模式");
            }
        });
    }
}
