package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;

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
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_drink);
        ButterKnife.bind(this);
        initTilte();
        initView();
    }

    private void initView() {
        rgIsDrink.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int id) {
                        switch (id) {
                            case R.id.rb_drink_yes:
                                break;
                            case R.id.rb_drink_no:
                                for (int i = 0; i < rgDrinkWhat.getChildCount(); i++) {
                                    ((RadioButton) rgDrinkWhat.getChildAt(i)).setChecked(false);
                                }
                                break;
                        }


                    }
                }
        );


    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

    @OnClick({R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward, R.id.rb_drink_baijiu, R.id.rb_drink_liaojiu, R.id.rb_drink_pijiu, R.id.rb_drink_mijiu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                getResult();
                break;
            case R.id.rb_drink_baijiu:
                if (rbDrinkNo.isChecked()) {
                    rbDrinkBaijiu.setChecked(false);
                }
                break;
            case R.id.rb_drink_liaojiu:
                if (rbDrinkNo.isChecked()) {
                    rbDrinkLiaojiu.setChecked(false);
                }
                break;
            case R.id.rb_drink_pijiu:
                if (rbDrinkNo.isChecked()) {
                    rbDrinkPijiu.setChecked(false);
                }
                break;
            case R.id.rb_drink_mijiu:
                if (rbDrinkNo.isChecked()) {
                    rbDrinkMijiu.setChecked(false);
                }
                break;
        }
    }

    private void getResult() {
        String drink = "";
        for (int i = 0; i < rgDrinkWhat.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) rgDrinkWhat.getChildAt(i);
            if (childAt.isChecked()) {
                drink += childAt.getText().toString().trim();
            }
        }
        LocalShared.getInstance(this).setDringInto(drink);

        startActivity(new Intent(this, GuoMinAndJiBingActivity.class));
    }

}
