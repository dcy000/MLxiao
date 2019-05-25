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
//                .addHeader("token", "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VyQWdlbnQiOiJ3aW5kb3dzIG50Iiwic291cmNlIjoiZ2NtbCIsInV1aWQiOiI1YmM4OGQxMjA2NTA0OTVlYWNhNWM5M2I0YjIyYTNlYSIsImNhdGVnb3J5IjoiMyIsInN1YiI6IjViYzg4ZDEyMDY1MDQ5NWVhY2E1YzkzYjRiMjJhM2VhIiwiaWF0IjoxNTU4Njc3MjU0LCJleHAiOjE1NTkyODIwNTR9.CQsNhFoKP0VSdMcYLN1lV0al4as4ascKwC7aGwyvtttTIrAGB-55wgLoq3LlOmj57tI1LhJZ0PAa2Z5ILxjVHw")
                .addHeader("source", "gcml")
                .addHeader("agent", "Android")
                .addHeader("equipmentId", Utils.getDeviceId(UM.getApp().getContentResolver()))
                .addHeader("eqid", Utils.getDeviceId(UM.getApp().getContentResolver()))
                .build();
        return chain.proceed(newRequest);
    }
}
