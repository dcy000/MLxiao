package com.gcml.module_inquiry.inquiry.ui.fragment.base;

/**
 * Created by lenovo on 2019/3/21.
 */

interface ChildActionListener {
    void onBack();

    void onNext(Object... data);

    void onStartBack();

    void onFinalNext(Object... data);
}
