package com.gcml.common.mvvm;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.ParameterizedType;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends AndroidViewModel> extends AppCompatActivity {

    protected B binding;

    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, layoutId());
        viewModel = initViewModel();
        binding.setVariable(variableId(), viewModel);
        initView(savedInstanceState);
    }

    protected VM initViewModel() {
        Class<VM> vmClass = (Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        return ViewModelProviders.of(this).get(vmClass);
    }

    protected abstract int layoutId();

    protected abstract int variableId();

    protected abstract void initView(Bundle savedInstanceState);
}
