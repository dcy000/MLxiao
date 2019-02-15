package com.example.han.referralproject.yiyuan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2019/2/15.
 */

public class IncomeActivity extends BaseActivity {
    @BindView(R.id.tv_less_10)
    TextView tvLess10;
    @BindView(R.id.tv_less_30)
    TextView tvLess30;
    @BindView(R.id.tv_more_30)
    TextView tvMore30;
    @BindView(R.id.ll_income)
    LinearLayout llIncome;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        initTitle();
        ActivityHelper.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        speak("主人,您的家庭年收入是多少");
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_less_10, R.id.tv_less_30, R.id.tv_more_30, R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_less_10:
                click("0");
                break;
            case R.id.tv_less_30:
                click("1");
                break;
            case R.id.tv_more_30:
                click("2");
                break;
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                next();
                break;
        }
    }

    private void next() {
        if (!checkSelect()) {
            speak("主人,请至择一项");
        }

        T.show(getSelectItem());
    }

    private boolean checkSelect() {
        for (int i = 0; i < llIncome.getChildCount(); i++) {
            if (llIncome.getChildAt(i).isSelected()) {
                return true;
            }
        }
        return false;
    }

    private String getSelectItem() {
        for (int i = 0; i < llIncome.getChildCount(); i++) {
            if (llIncome.getChildAt(i).isSelected()) {
                return (String) llIncome.getChildAt(i).getTag();
            }
        }
        return "0";
    }

    private void click(String flag) {
        for (int i = 0; i < llIncome.getChildCount(); i++) {
            String tag = (String) llIncome.getChildAt(i).getTag();
            if (TextUtils.equals(tag, flag)) {
                llIncome.getChildAt(i).setSelected(true);
            } else {
                llIncome.getChildAt(i).setSelected(false);
            }
        }
    }


}
