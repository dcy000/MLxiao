package com.gcml.common.service;

import android.content.Context;

public interface IAppUpdateProvider {
    void checkAppVersion(Context context);
    void showDialog(Context context,String appUrl);
}
