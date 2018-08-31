package com.gcml.auth.face.model;

import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FaceService {

    @FormUrlEncoded
    @POST("ZZB/xf/insert_group_record")
    Observable<ApiResult<List<FaceGroup>>> updateFaceGroup(
            @Field("userid") String userId,
            @Field("gid") String groupId,
            @Field("xfid") String faceId
    );

    @GET("ZZB/br/seltoken")
    Observable<ApiResult<String>> getQiniuToken();

    @FormUrlEncoded
    @POST("ZZB/br/upUser_photo")
    Observable<ApiResult<Object>> uploadAvatarUrl(
            @Field("user_photo") String avatarUrl,
            @Field("bid") String userId,
            @Field("xfid") String faceId
    );

    @GET("ZZB/br/selMoreUser")
    Observable<ApiResult<List<UserEntity>>> getAllUsers(
            @Query("p") String usersIds
    );
}
