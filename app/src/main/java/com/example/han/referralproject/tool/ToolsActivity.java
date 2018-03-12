package com.example.han.referralproject.tool;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        ButterKnife.bind(this);
        initTitle();
        initRV();
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("小工具");
    }

    private void initRV() {
    }

}
