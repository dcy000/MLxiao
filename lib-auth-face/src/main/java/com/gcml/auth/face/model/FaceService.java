package com.gcml.auth.face.model;

import com.gcml.common.repository.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FaceService {

//    @POST("ZZB/xf/insert_group_record")
//    Observable<ApiResult<List<XfGroupInfo>>> recordXfGroupInformation(
//            @Query("userid") String userid,
//            @Query("gid") String gid,
//            @Query("xfid") String xfid
//    );

    @GET("ZZB/br/seltoken")
    Observable<ApiResult<String>> getQiniuToken();

    @POST("ZZB/br/upUser_photo")
    Observable<ApiResult<Object>> uploadAvatarUrl(
            @Query("user_photo") String avatarUrl,
            @Query("bid") String userId,
            @Query("xfid") String faceId
    );
}
