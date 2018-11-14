package com.gzq.lib_core.http.download;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by wzm on 2017/6/15.
 */

public interface DownLoadService {

    @Streaming
    @GET
    Flowable<ResponseBody> startDownLoad(@Url String fileUrl);

}
