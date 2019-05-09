package com.gcml.common.mvp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.gcml.common.utils.RxUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.lang.reflect.ParameterizedType;

/**
 * Created by afirez on 2017/7/11.
 */

public abstract class BaseActivity<V extends IView, P extends IPresenter>
        extends AppCompatActivity
        implements IView, UiFactory<V, P> {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = providePresenter();
        presenter.setView(provideView());
        presenter.setLifecycleOwner(this);
        getLifecycle().addObserver(presenter);
    }

    @Override
    public P providePresenter() {
        Class<P> pClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        String name = pClass.getName();
        HolderViewModel phvm = ViewModelProviders.of(this).get(HolderViewModel.class);
        Object presenter = phvm.get(name);
        if (presenter == null) {
            presenter = newInstance(pClass);
            phvm.put(name, presenter);
        }
        return (P) presenter;
    }

    private Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public V provideView() {
        return (V) this;
    }

    public <T> AutoDisposeConverter<T> autoDisposeConverter() {
        return RxUtils.autoDisposeConverter(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showTip(int resId) {

    }

    @Override
    public void showTip(String tip) {

    }

    @Override
    public void showError(int resId) {

    }

    @Override
    public void showError(String error) {

    }

    protected void addFragment(Fragment fragment, @IdRes int containerId) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    protected void replaceFragment(Fragment fragment, @IdRes int containerId) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    protected void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();
    }

    protected void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();
    }

    protected void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
    }

    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        finish();
    }
}