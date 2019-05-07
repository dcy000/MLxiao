package com.gcml.module_video.net;

import com.gcml.common.http.ApiResult;
import com.gcml.module_video.video.VideoEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoService {
    @GET("ZZB/vc/selAllUpload")
    Observable<ApiResult<List<VideoEntity>>> getVideoList(
            @Query("tag1")int tag1,
            @Query("tag2")String tag2,
            @Query("flag")String flag,
            @Query("page")int page,
            @Query("pagesize")int pagesize
    );
}
