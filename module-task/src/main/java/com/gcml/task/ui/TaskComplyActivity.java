package com.gcml.task.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.utils.UM;
import com.gcml.task.R;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * desc: 依从性调查问卷入口 .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskComplyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTitle, mMessage;
    ImageView mBack;

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
        mTitle.setText(R.string.have_a_questionnaire);
        mMessage.setText(R.string.develop_health_tasks);
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
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                UM.getString(R.string.make_a_questionnaire),
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
                        super.onCompleted(speechError);
                        CC.obtainBuilder("app.component.task.comply.choice").addParam("isFirst", false).build().callAsync();
                    }
                }, false);
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
