package com.example.han.referralproject.inquiry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;

/**
 * Created by lenovo on 2019/1/16.
 */

public class InquiryActivity extends AppCompatActivity {

    private TranslucentToolBar tb;
    private TextView confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        initView();
    }

    private void initView() {
        tb = findViewById(R.id.tb_inquiry);
        tb.setData("问 诊",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                    }
                });

        confirm = findViewById(R.id.tv_inquiry_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetection();
            }


        });
    }

    private void toDetection() {
        startActivity(new Intent(this, DetectionItemActivity.class));
    }
}
