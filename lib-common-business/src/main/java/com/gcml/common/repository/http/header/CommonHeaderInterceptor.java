package com.gcml.common.repository.http.header;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.UtilsManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
                .addHeader("equipmentId", Utils.getDeviceId(UtilsManager.getApplication().getContentResolver()))
                .addHeader("version", "1.0")
                .addHeader("Authorization", UserSpHelper.getToken())
                .build();
        return chain.proceed(newRequest);
    }
}
