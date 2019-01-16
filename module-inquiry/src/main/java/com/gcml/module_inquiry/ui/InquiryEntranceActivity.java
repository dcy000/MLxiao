package com.gcml.module_inquiry.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.module_inquiry.R;

public class InquiryEntranceActivity extends AppCompatActivity implements View.OnClickListener {

    private com.gcml.common.widget.toolbar.TranslucentToolBar tb_inquiry_home;
    private RelativeLayout rl_inquiry_home_manage;
    private RelativeLayout rl_inquiry_home_file;
    private RelativeLayout rl_inquiry_home_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_entrance);
        bindViews();
    }

    private void bindViews() {
        tb_inquiry_home = (com.gcml.common.widget.toolbar.TranslucentToolBar) findViewById(R.id.tb_inquiry_home);
        rl_inquiry_home_manage = (RelativeLayout) findViewById(R.id.rl_inquiry_home_manage);
        rl_inquiry_home_file = (RelativeLayout) findViewById(R.id.rl_inquiry_home_file);
        rl_inquiry_home_home = (RelativeLayout) findViewById(R.id.rl_inquiry_home_home);

        rl_inquiry_home_manage.setOnClickListener(this);
        rl_inquiry_home_file.setOnClickListener(this);
        rl_inquiry_home_home.setOnClickListener(this);

        tb_inquiry_home.setData("智 能 健 康 管 理",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_icon_bluetooth_break, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                    }
                });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_inquiry_home_manage) {

        } else if (id == R.id.rl_inquiry_home_file) {

        } else if (id == R.id.rl_inquiry_home_home) {
            CC.obtainBuilder("com.gcml.old.home").build().callAsync();
        }
    }
}
