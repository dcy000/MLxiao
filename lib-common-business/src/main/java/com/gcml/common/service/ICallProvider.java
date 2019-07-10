package com.gcml.common.service;

import android.app.Activity;

public interface ICallProvider {
    void login(String callId,String callPwd);
    void logout();
    void call(Activity context, String callId);
}
