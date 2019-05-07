package com.gcml.common.service;

import android.content.Context;

public interface ICallProvider {
    void login(String callId,String callPwd);
    void logout();
    void call(Context context,String callId);
}
