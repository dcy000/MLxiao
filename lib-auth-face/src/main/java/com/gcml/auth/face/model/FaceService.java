package com.gcml.auth.face.model;

import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FaceService {

    @FormUrlEncoded
    @POST("ZZB/xf/insert_group_record")
    Observable<Object> updateFaceGroup(
            @Field("userid") String userId,
            @Field("gid") String groupId,
            @Field("xfid") String faceId
    );

    @GET("ZZB/br/seltoken")
    Observable<HttpResult<String>> getQiniuToken();

    @FormUrlEncoded
    @POST("ZZB/br/upUser_photo")
    Observable<Object> uploadAvatarUrl(
            @Field("user_photo") String avatarUrl,
            @Field("bid") String userId,
            @Field("xfid") String faceId
    );

    @GET("ZZB/br/selMoreUser")
    Observable<HttpResult<List<UserInfoBean>>> getAllUsers(
            @Query("p") String usersIds
    );

    @GET("ZZB/api/xunfei/face/groups/")
    Observable<HttpResult<List<FaceGroup>>> getGroups();

    @GET("ZZB/api/xunfei/face/user/{userId}/")
    Observable<HttpResult<List<FaceInfo>>> getFaceInfo(
            @Path("userId") String userId
    );

    @PUT("ZZB/api/xunfei/face/user/{userId}/")
    Observable<HttpResult<List<FaceInfo>>> updateFaceInfo(
            @Path("userId") String userId,
            @Body FaceInfo faceInfo
    );

    @PUT("ZZB/api/xunfei/face/user/{userId}/xunfeiID/")
    Observable<HttpResult<String>> getFaceId(
            @Path("userId") String userId
    );

    @GET("ZZB/br/selOneUserEverything")
    Observable<HttpResult<HttpResult>> getProfile(
            @Query("bid") String userId
    );
}
