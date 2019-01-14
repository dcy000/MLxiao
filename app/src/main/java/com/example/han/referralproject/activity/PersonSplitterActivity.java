package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.lib_widget.EclipseImageView;
import com.gcml.module_health_record.HealthRecordActivity;

public class PersonSplitterActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private EclipseImageView mIvPersonCenter;
    private EclipseImageView mIvHealthRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_splitter);
        initView();
        mTitleText.setText("个 人 中 心");

    }

    private void initView() {
        mIvPersonCenter = (EclipseImageView) findViewById(R.id.iv_person_center);
        mIvPersonCenter.setOnClickListener(this);
        mIvHealthRecord = (EclipseImageView) findViewById(R.id.iv_health_record);
        mIvHealthRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_person_center:
                CC.obtainBuilder("com.gcml.auth.profileInfo").build().callAsync();
                break;
            case R.id.iv_health_record:
                startActivity(new Intent(this, HealthRecordActivity.class));
                break;
        }
    }
}