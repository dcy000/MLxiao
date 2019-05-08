package com.gcml.common.service;

import android.content.Context;

public interface IAppUpdateProvider {
    void checkAppVersion(Context context,boolean isNeedVoiceAndToast);
    void showDialog(Context context,String appUrl);
}
