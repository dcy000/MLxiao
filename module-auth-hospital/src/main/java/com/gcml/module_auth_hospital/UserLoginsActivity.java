package com.gcml.module_auth_hospital;

import android.os.Bundle;

import com.gcml.common.mvvm.BaseActivity;
import com.gcml.module_auth_hospital.databinding.ActivityUserloginsBinding;

public class UserLoginsActivity extends BaseActivity<ActivityUserloginsBinding, UserLoginsViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.activity_userlogins;
    }

    @Override
    protected int variableId() {
        return BR.userloginsViewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
    }

    public void onClickIdCardNumberLogin() {

    }

    public void onClickFaceLogin() {

    }

    public void onClickIdCitizenLogin() {


    }

    public void onClickIdCardLogin() {

    }


}
