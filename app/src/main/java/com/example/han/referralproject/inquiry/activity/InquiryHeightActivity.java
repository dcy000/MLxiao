package com.example.han.referralproject.inquiry.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.model.DetailsModel;
import com.example.han.referralproject.inquiry.fragment.HealthDiaryDetailsFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;

/**
 * Created by lenovo on 2019/1/16.
 */

public class InquiryHeightActivity extends AppCompatActivity implements HealthDiaryDetailsFragment.OnActionListener {
    private TranslucentToolBar tb;
    private FrameLayout contaniner;
    private DetailsModel mUiModel;
    private HealthDiaryDetailsFragment mUiFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_height);
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        tb.setData("身高",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
    }

    private void initData() {
        mUiModel = new DetailsModel();
        mUiModel.setWhat(0);
        mUiModel.setTitle("选择您的身高");
        mUiModel.setUnitPosition(0);
        mUiModel.setUnits(new String[]{"cm"});
        mUiModel.setUnitSum(new String[]{"cm"});
        mUiModel.setSelectedValues(new float[]{60f});
        mUiModel.setMinValues(new float[]{100f});
        mUiModel.setMaxValues(new float[]{260f});
        mUiModel.setPerValues(new float[]{1});
        mUiModel.setAction("下一步");
    }

    private void initView() {
        tb = findViewById(R.id.tb_inquiry_height);
        contaniner = findViewById(R.id.fr_inquiry_height);

        String tag = HealthDiaryDetailsFragment.class.getName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        android.support.v4.app.Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null && fragment.isHidden()) {
            transaction.show(fragment);
        } else {
            fragment = HealthDiaryDetailsFragment.newInstance(mUiModel);
            transaction.add(R.id.fr_inquiry_height, fragment, tag);
        }
        mUiFragment = (HealthDiaryDetailsFragment) fragment;
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onAction(int what, float selectedValue, int unitPosition, String item) {
        if (0 == what) {
            ToastUtils.showShort(selectedValue + "");
        }
    }
}
