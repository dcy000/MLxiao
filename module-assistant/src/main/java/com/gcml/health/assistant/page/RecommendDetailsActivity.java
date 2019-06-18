package com.gcml.health.assistant.page;

import android.os.Bundle;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.health.assistant.R;

public class RecommendDetailsActivity extends ToolbarBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistant_activity_recommend_details);
        mTitleText.setText("智 能 建 议");
    }
}
