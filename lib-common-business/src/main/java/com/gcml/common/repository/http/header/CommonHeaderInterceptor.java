package com.gcml.common.repository.http.header;

import com.gcml.common.data.UserSpHelper;

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
                .build();
        return chain.proceed(newRequest);
    }
}
