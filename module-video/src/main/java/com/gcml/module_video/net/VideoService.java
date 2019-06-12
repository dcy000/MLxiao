package com.gcml.module_video.net;

import com.gcml.common.http.ApiResult;
import com.gcml.module_video.video.VideoEntity;
import com.gcml.module_video.video.VideoType;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VideoService {
    //    POST http://47.96.98.60:8020/ZZB/api/type/getItemByCode?code=video_type
    @POST("ZZB/api/type/getItemByCode")
    Observable<ApiResult<List<VideoType>>> getVideoType(
            @Query("code") String code
    );

    @GET("ZZB/vc/selAllUpload")
    Observable<ApiResult<List<VideoEntity>>> getVideoList(
            @Query("tag1") int tag1,
            @Query("tag2") String tag2,
            @Query("flag") String flag,
            @Query("page") int page,
            @Query("limit") int pagesize
    );
}
