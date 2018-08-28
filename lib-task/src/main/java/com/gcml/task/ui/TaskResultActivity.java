package com.gcml.task.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;
import com.gcml.task.bean.Post.TaskSchemaResultBean;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * desc: TaskResultActivity .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskResultActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    TextView resultTitle;
    TextView totalContent1, totalContent2, totalContent3, totalContent4;
    TextView testContent1, testContent2, testContent3;
    TextView sportContent1, sportContent2, sportContent3;
    TextView dineContent1, dineContent2, dineContent3, dineContent4;
    TextView resultAgain, resultAffirm;
    TaskSchemaResultBean resultBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_result);
        resultBean = (TaskSchemaResultBean) getIntent().getExtras().getSerializable("resultBean");

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_task_result);
        resultTitle = findViewById(R.id.tv_task_result_title);

        totalContent1 = findViewById(R.id.tv_total_content1);
        totalContent2 = findViewById(R.id.tv_total_content2);
        totalContent3 = findViewById(R.id.tv_total_content3);
        totalContent4 = findViewById(R.id.tv_total_content4);

        testContent1 = findViewById(R.id.tv_test_content1);
        testContent2 = findViewById(R.id.tv_test_content2);
        testContent3 = findViewById(R.id.tv_test_content3);

        sportContent1 = findViewById(R.id.tv_sport_content1);
        sportContent2 = findViewById(R.id.tv_sport_content2);
        sportContent3 = findViewById(R.id.tv_sport_content3);

        dineContent1 = findViewById(R.id.tv_dine_content1);
        dineContent2 = findViewById(R.id.tv_dine_content2);
        dineContent3 = findViewById(R.id.tv_dine_content3);
        dineContent4 = findViewById(R.id.tv_dine_content4);

        resultAgain = findViewById(R.id.tv_task_result_again);
        resultAffirm = findViewById(R.id.tv_task_result_affirm);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), resultBean.result, false);
        mToolBar.setData("问 卷 结 果", R.drawable.common_icon_back, "返回", R.drawable.common_icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                CC.obtainBuilder("app.component.task.prompt").setContext(TaskResultActivity.this).build().callAsync();
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        resultTitle.setText(resultBean.result);

        totalContent1.setText(resultBean.initiative);
        totalContent2.setText(resultBean.compliance);
        totalContent3.setText(resultBean.attitude);
        totalContent4.setText(resultBean.awareness);

        testContent1.setText(resultBean.detectionPlan.HTN);
        testContent2.setText(resultBean.detectionPlan.weight);
        testContent3.setText(resultBean.detectionPlan.GLU);

        sportContent1.setText("");
        sportContent2.setText("");
        sportContent3.setText("");

        dineContent1.setText(resultBean.intake.naSalt);
        dineContent2.setText(resultBean.intake.grease);
        dineContent3.setText(resultBean.intake.drink);
        dineContent4.setText(resultBean.intake.smoke);

        resultAgain.setOnClickListener(this);
        resultAffirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_task_result_again) {
            CC.obtainBuilder("app.component.task.prompt").setContext(TaskResultActivity.this).build().callAsync();
            finish();
        } else if (v.getId() == R.id.tv_task_result_affirm) {
            CC.obtainBuilder("app.component.task").build().callAsync();
        }
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
