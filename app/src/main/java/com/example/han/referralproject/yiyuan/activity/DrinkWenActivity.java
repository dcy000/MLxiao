package com.example.han.referralproject.yiyuan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrinkWenActivity extends BaseActivity {

    @BindView(R.id.rb_drink_yes)
    RadioButton rbDrinkYes;
    @BindView(R.id.rb_drink_no)
    RadioButton rbDrinkNo;
    @BindView(R.id.rg_is_drink)
    RadioGroup rgIsDrink;
    @BindView(R.id.rb_drink_baijiu)
    RadioButton rbDrinkBaijiu;
    @BindView(R.id.rb_drink_liaojiu)
    RadioButton rbDrinkLiaojiu;
    @BindView(R.id.rb_drink_pijiu)
    RadioButton rbDrinkPijiu;
    @BindView(R.id.rb_drink_mijiu)
    RadioButton rbDrinkMijiu;
    @BindView(R.id.rg_drink_what)
    RadioGroup rgDrinkWhat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_drink);
        ButterKnife.bind(this);
        initTilte();
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

    @OnClick({R.id.rb_drink_yes, R.id.rb_drink_no, R.id.rb_drink_baijiu, R.id.rb_drink_liaojiu, R.id.rb_drink_pijiu, R.id.rb_drink_mijiu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_drink_yes:
                break;
            case R.id.rb_drink_no:
                break;
            case R.id.rb_drink_baijiu:
                break;
            case R.id.rb_drink_liaojiu:
                break;
            case R.id.rb_drink_pijiu:
                break;
            case R.id.rb_drink_mijiu:
                break;
        }
    }
}
