package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.family.R;
import com.gcml.family.adapter.FamilyNewsAdapter;

public class FamilyNewsActivity extends AppCompatActivity {

    TranslucentToolBar mToolBar;
    RecyclerView mRecycler;
    FamilyNewsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_recycler);

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_family_recycler);
        mRecycler = findViewById(R.id.rv_family_recycler);
    }

    private void bindData() {
        mToolBar.setData("消 息 中 心", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
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
}
