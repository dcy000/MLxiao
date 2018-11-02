package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.family.R;

public class FamilyPrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    TextView mPrivacyLook, mPrivacyChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_privacy);

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_family_privacy);
        mPrivacyLook = findViewById(R.id.tv_privacy_look);
        mPrivacyChange = findViewById(R.id.tv_privacy_change);

        mPrivacyLook.setOnClickListener(this);
        mPrivacyChange.setOnClickListener(this);
    }

    private void bindData() {
        mToolBar.setData("隐 私 设 置", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_privacy_look) {

        } else if (v.getId() == R.id.tv_privacy_change) {

        }
    }
}
