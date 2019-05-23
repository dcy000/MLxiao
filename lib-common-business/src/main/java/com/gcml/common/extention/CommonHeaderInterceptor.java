package com.gcml.common.extention;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.UM;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
                .addHeader("version", "1.0")
                .addHeader("Authorization", UserSpHelper.getToken())
                .addHeader("token", UserSpHelper.getToken())
                .addHeader("source", "gcml")
                .addHeader("agent", "Android")
                .addHeader("equipmentId", Utils.getDeviceId(UM.getApp().getContentResolver()))
                .addHeader("eqid", Utils.getDeviceId(UM.getApp().getContentResolver()))
                .build();
        return chain.proceed(newRequest);
    }
}
