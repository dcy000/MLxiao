package com.witspring.base;

import android.content.Context;

import com.witspring.model.entity.Result;

/**
 * @author Created by Goven on 17/6/2 上午11:51
 * @email gxl3999@gmail.com
 */
public interface BaseView {

    Context getContext();
    void startLoading();
    void startLoadingAlway();
    void stopLoading();
    void showToastShort(String value);
    void showToastLong(String value);
    void warningNoConnect();
    void warningUnknow(Result result);
    void warningNoData();

}
