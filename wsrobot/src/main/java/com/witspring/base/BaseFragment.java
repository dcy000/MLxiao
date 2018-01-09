package com.witspring.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Created by Goven on 17/6/1 下午3:46
 * @email gxl3999@gmail.com
 */
public class BaseFragment extends Fragment {

    protected boolean hasInited = false;
    protected BaseActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        if (getPresenter() != null) {
            getPresenter().start();
        }
    }

    protected BasePresenter getPresenter() {
        return null;
    }

    public boolean hasInited() {
        return hasInited;
    }

    public void refresh() {}

    @Override
    public void onDestroy() {
        if (getPresenter() != null) {
            getPresenter().stop();
        }
        super.onDestroy();
    }

    public void startLoading() {
        activity.startLoading();
    }

    public void startLoading(String str) {
        activity.startLoading(str);
    }

    public void startLoadingAlawy() {
        activity.startLoadingAlway();
    }

    public void stopLoading() {
        activity.stopLoading();
    }

    public void showToastShort(String value) {
        activity.showToastShort(value);
    }

    public void showToastLong(String value) {
        activity.showToastLong(value);
    }

    public void warningNoConnect() {
        showToastShort("网络异常，内容加载失败！");
    }

    public void warningUnknow() {
        showToastShort("产品火热计算分析中，请稍等！");
    }

    public void warningNoData() {
        showToastShort("没有数据！");
    }

}
