package com.gcml.call;

import android.content.Context;

import com.gcml.common.service.ICallProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/call/call/provider")
public class CallProviderImp implements ICallProvider {

    @Override
    public void login(String callId, String callPwd) {
        CallAccountHelper.INSTANCE.login(callId, callPwd, null);
    }

    @Override
    public void logout() {
        CallAccountHelper.INSTANCE.logout();
    }

    @Override
    public void call(Context context, String callId) {
        CallHelper.outgoingCall(context, callId);
    }
}
