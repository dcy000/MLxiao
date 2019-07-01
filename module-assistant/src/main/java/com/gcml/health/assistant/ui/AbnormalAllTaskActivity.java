package com.gcml.health.assistant.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.health.assistant.R;

public class AbnormalAllTaskActivity extends ToolbarBaseActivity {

    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abnormal_activity_all_task);
        taskId = getIntent().getIntExtra("taskId", 0);
        mTitleText.setText("全 部 任 务");
//        mRightView.setImageResource(R.drawable.common_icon_home);
        FragmentManager fm = getSupportFragmentManager();
        AbnormalAllTaskFragment fragment = AbnormalAllTaskFragment.newInstance(taskId);
        fm.beginTransaction()
                .replace(R.id.flContainer, fragment, "AbnormalAllTaskFragment")
                .commitNowAllowingStateLoss();
    }

//    @Override
//    protected void backMainActivity() {
//        Routerfit.register(AppRouter.class).skipMainActivity();
//    }
}
