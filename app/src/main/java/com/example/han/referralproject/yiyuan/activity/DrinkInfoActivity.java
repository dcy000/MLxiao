package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrinkInfoActivity extends BaseActivity {

    @BindView(R.id.tv_drink_yes)
    TextView tvDrinkYes;
    @BindView(R.id.tv_drink_not)
    TextView tvDrinkNot;
    @BindView(R.id.ll_drink_yes_not)
    LinearLayout llDrinkYesNot;
    @BindView(R.id.tv_baijiu)
    TextView tvBaijiu;
    @BindView(R.id.tv_liaojiu)
    TextView tvLiaojiu;
    @BindView(R.id.tv_pijiu)
    TextView tvPijiu;
    @BindView(R.id.tv_mijiu)
    TextView tvMijiu;
    @BindView(R.id.ll_drink_what)
    LinearLayout llDrinkWhat;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_info);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
        speak("主人,请选择您本周内的饮酒情况");
    }

    @OnClick({R.id.tv_drink_yes, R.id.tv_drink_not, R.id.tv_baijiu, R.id.tv_liaojiu, R.id.tv_pijiu, R.id.tv_mijiu, R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_drink_yes:
                tvDrinkYes.setSelected(!tvDrinkYes.isSelected());
                tvDrinkNot.setSelected(false);
                break;
            case R.id.tv_drink_not:
                tvDrinkNot.setSelected(!tvDrinkNot.isSelected());
                tvDrinkYes.setSelected(false);

                for (int i = 0; i < llDrinkWhat.getChildCount(); i++) {
                    llDrinkWhat.getChildAt(i).setSelected(false);
                }

                break;
            case R.id.tv_baijiu:
                reverse(tvBaijiu);
                break;
            case R.id.tv_liaojiu:
                reverse(tvLiaojiu);
                break;
            case R.id.tv_pijiu:
                reverse(tvPijiu);
                break;
            case R.id.tv_mijiu:
                reverse(tvMijiu);
                break;
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                getResult();
                break;
        }
    }

    private void reverse(View view) {
        view.setSelected(!view.isSelected());
        tvDrinkYes.setSelected(true);
        tvDrinkNot.setSelected(false);
    }

    private void getResult() {
        String drink=tvDrinkYes.isSelected()?"1":"0";
        LocalShared.getInstance(this).setIsDrinkOrNot(drink);

        String drinkWhat = "";

        if (tvBaijiu.isSelected()) {
            drinkWhat+="0"+",";
        }

        if (tvLiaojiu.isSelected()) {
            drinkWhat+="1"+",";
        }

        if (tvPijiu.isSelected()) {
            drinkWhat+="2"+",";
        }

        if (tvMijiu.isSelected()) {
            drinkWhat+="3"+",";
        }

        if ((!tvDrinkNot.isSelected())&&(!tvDrinkYes.isSelected())){
            speak("主人,您本周内是否有饮酒");
            return;
        }

        LocalShared.getInstance(this).setDringInto(drinkWhat);
        LocalShared.getInstance(this).setIsDrinkOrNot(drink);

        startActivity(new Intent(this, GuoMinAndJiBingActivity.class));
    }


}
