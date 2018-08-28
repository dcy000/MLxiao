package com.gcml.task.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.task.R;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class TaskPromptActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTitle, mMessage;
    ImageView mBack;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_prompt);
        bindView();
        bindData();
    }

    private void bindView() {
        mTitle = findViewById(R.id.tv_task_prompt_title);
        mMessage = findViewById(R.id.tv_task_prompt_message);
        mBack = findViewById(R.id.iv_task_prompt_back);
    }

    private void bindData() {
        mTitle.setText("请跟小E来做个问卷吧，");
        mMessage.setText("小E会根据您的问卷结果给您制定健康任务。");
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                "请跟小E来做个问卷吧，小E会根据您的问卷结果给您制定健康任务。",
                false);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_task_prompt_back) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CC.obtainBuilder("app.component.task.health").build().callAsync();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }

}
