package com.gcml.common.mvvm;

import android.os.Bundle;

import com.gcml.common.demo.R;
import com.gcml.common.demo.BR;
import com.gcml.common.demo.databinding.ActivityMvvmBinding;


public class MvvmActivity extends BaseActivity<ActivityMvvmBinding, HelloViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.activity_mvvm;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        viewModel.getHello().set("Hello DataBinding");
    }
}
