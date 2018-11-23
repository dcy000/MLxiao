package com.gzq.lib_core.base.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gzq.lib_core.base.App;

import java.util.Set;

public abstract class BaseActivity<V extends IView, P extends IPresenter>
        extends AppCompatActivity implements IView {
    protected P mPresenter;
    protected View mView;
    private Set<IEvents> events = App.getEvents();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = layoutId(savedInstanceState);
        if (layoutId <= 0) {
            throw new IllegalArgumentException("layout is null");
        }
        //设置布局
        setContentView(layoutId);
        //初始化状态页
        initStateView();
        //初始化基本参数
        initParams(getIntent());
        //初始化Presenter
        mPresenter = obtainPresenter();
        if (mPresenter == null || !(mPresenter instanceof LifecycleObserver)) {
            throw new IllegalArgumentException("obtain a wrong presenter");
        }
        getLifecycle().addObserver(mPresenter);
        //初始化控件id
        initView();

    }

    /**
     * 该方法是为了扩展其他基础类而增加的，普通继承类可不重写该方法。
     */
    protected void initStateView() {
    }

    public abstract int layoutId(Bundle savedInstanceState);

    public abstract void initParams(Intent intentArgument);

    public abstract void initView();

    public abstract P obtainPresenter();

    public void createEvent(String tag, Object... params) {
        for (IEvents event : events) {
            event.onEvent(tag, params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }
}
