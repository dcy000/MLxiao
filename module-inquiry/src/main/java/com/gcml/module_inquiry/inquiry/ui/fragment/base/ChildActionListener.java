package com.gcml.module_inquiry.inquiry.ui.fragment.base;

/**
 * Created by lenovo on 2019/3/21.
 */

interface ChildActionListener {
    void onBack(String ...data);

    void onNext(String... data);

    void onStartBack(String ... data);

    void onFinalNext(String... data);
}
