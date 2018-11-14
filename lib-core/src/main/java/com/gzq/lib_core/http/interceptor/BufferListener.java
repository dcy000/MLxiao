package com.gzq.lib_core.http.interceptor;

import java.io.IOException;

import okhttp3.Request;

public interface BufferListener {
    String getJsonResponse(Request request) throws IOException;
}
