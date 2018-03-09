package com.example.han.referralproject.children.study;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class ChildEduTurnOfMindActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_turn_of_mind);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("脑  经  急  转  弯");
    }
}
