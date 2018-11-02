package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.family.R;

public class FamilyPrivacyPermissionActivity extends AppCompatActivity {

    TranslucentToolBar mToolBar;
    RecyclerView mRecycler;
    int startType = 0;

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
        if (startType == 0) {
            mToolBar.setData("查 看 档 案 权 限", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
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
        } else {
            mToolBar.setData("修 改 档 案 权 限", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
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
}
