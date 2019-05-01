package com.gcml.task.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.task.R;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

/**
 * desc: 依从性调查问卷入口 .
 * author: wecent .
 * date: 2018/8/20 .
 */
@Route(path = "/task/task/comply/activity")
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
        mTitle.setText("请跟小E来做个问卷吧，");
        mMessage.setText("小E会根据您的问卷结果给您制定健康任务。");
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
                "请跟小忆来做个问卷吧，小忆会根据您的问卷结果给您制定健康任务",
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
                        super.onCompleted(speechError);
                        Routerfit.register(AppRouter.class).skipTaskComplyChoiceActivity(false);
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
