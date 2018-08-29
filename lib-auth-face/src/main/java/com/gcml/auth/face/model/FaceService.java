package com.gcml.auth.face.model;

import com.gcml.common.repository.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FaceService {

    @GET("ZZB/br/seltoken")
    Observable<ApiResult<String>> getQiniuToken();

    @POST("ZZB/br/upUser_photo")
    Observable<ApiResult<Object>> uploadAvatarUrl(
            @Query("user_photo") String avatarUrl,
            @Query("bid") String userId,
            @Query("xfid") String faceId
    );
}
