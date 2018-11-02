package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.family.R;

public class FamilyContactActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    ImageView mUserIcon;
    TextView mUserName, mUserTag, mHealthRecode;
    TextView mUserMobile, mUserAge, mUserAddress, mFamilyList;
    TextView mActionAdd, mActionSure, mActionFalse;
    RelativeLayout mActionOther;
    int startType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_contact);

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_family_detail);
        mUserIcon = findViewById(R.id.iv_user_icon);
        mUserName = findViewById(R.id.tv_user_name);
        mUserTag = findViewById(R.id.tv_user_tag);
        mHealthRecode = findViewById(R.id.tv_health_recode);
        mUserMobile = findViewById(R.id.tv_user_mobile);
        mUserAge = findViewById(R.id.tv_user_age);
        mUserAddress = findViewById(R.id.tv_user_address);
        mFamilyList = findViewById(R.id.tv_family_list);
        mActionAdd = findViewById(R.id.tv_family_add);
        mActionOther = findViewById(R.id.rl_friend_other);
        mActionSure = findViewById(R.id.tv_family_sure);
        mActionFalse = findViewById(R.id.tv_family_false);
    }

    private void bindData() {
        if (startType == 0) {
            mToolBar.setData("详 细 资 料", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
                @Override
                public void onLeftClick() {
                    finish();
                }

                @Override
                public void onRightClick() {

                }
            });
            mActionAdd.setVisibility(View.VISIBLE);
            mActionOther.setVisibility(View.GONE);
        } else {
            mToolBar.setData("新 的 联 系 人", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
                @Override
                public void onLeftClick() {
                    finish();
                }

                @Override
                public void onRightClick() {

                }
            });
            mActionAdd.setVisibility(View.GONE);
            mActionOther.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_family_add) {

        } else if (v.getId() == R.id.tv_family_sure) {

        } else if (v.getId() == R.id.tv_family_false) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
