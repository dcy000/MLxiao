package com.gcml.common.mvp;

/**
 * Created by afirez on 2017/7/13.
 */

public interface UiFactory<V extends IView, P extends IPresenter> {
    P providePresenter();

    V provideView();
}
