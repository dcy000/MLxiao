package com.medlink.danbogh.register.lude;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.activity.ChooseRegisterTypeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;

public class LudeAuthActivity extends AppCompatActivity {

    private ImageView mIvBack;
    private ImageView mIvWifi;
    private ImageView mIvNewUser;
    private ImageView mIvHasUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lude_activity_auth);
        mIvBack = (ImageView) findViewById(R.id.auth_iv_back);
        mIvWifi = (ImageView) findViewById(R.id.auth_iv_wifi);
        mIvNewUser = (ImageView) findViewById(R.id.auth_iv_new_user);
        mIvHasUser = (ImageView) findViewById(R.id.auth_iv_has_user);
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
    }
}
