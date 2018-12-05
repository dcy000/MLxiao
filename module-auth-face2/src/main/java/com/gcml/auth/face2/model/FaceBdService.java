package com.gcml.auth.face2.model;

import com.gcml.auth.face2.model.entity.FaceBdAccessToken;
import com.gcml.auth.face2.model.entity.FaceBdAddFace;
import com.gcml.auth.face2.model.entity.FaceBdAddFaceParam;
import com.gcml.auth.face2.model.entity.FaceBdDeleteFaceParam;
import com.gcml.auth.face2.model.entity.FaceBdResult;
import com.gcml.auth.face2.model.entity.FaceBdSearch;
import com.gcml.auth.face2.model.entity.FaceBdSearchParam;
import com.gcml.auth.face2.model.entity.FaceBdVerify;
import com.gcml.auth.face2.model.entity.FaceBdVerifyParam;
import com.gcml.common.repository.http.ApiResult;
import com.gcml.common.user.UserToken;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FaceBdService {

    String GRANT_TYPE = "client_credentials";
    String API_KEY = "HVVhGHwxMbOQetB2wzcqaV53";
    String SECRET_KEY = "o1zeFFQAKfyQ81HECVbATrDolWdyYLhj";

    @GET("ZZB/br/seltoken")
    Observable<ApiResult<String>> getQiniuToken();

    @FormUrlEncoded
    @POST("ZZB/br/upUser_photo")
    Observable<ApiResult<Object>> uploadAvatarUrl(
            @Field("user_photo") String avatarUrl,
            @Field("bid") String userId,
            @Field("xfid") String faceId
    );

    @GET("ZZB/api/user/face/groups/")
    Observable<ApiResult<List<String>>> getGroups();

    @FormUrlEncoded
    @POST("ZZB/api/user/face/user/{userId}/")
    Observable<ApiResult<Object>> addFace(
            @Path("userId") String userId,
            @Field("image") String image,
            @Field("imageType") String imageType
    );

    @FormUrlEncoded
    @POST("ZZB/login/face")
    Observable<ApiResult<UserToken>> signInByFace(
            @Field("faceId") String faceId,
            @Field("groupId") String groupId
    );

    @GET("ZZB/api/user/face/{userId}/")
    Observable<ApiResult<Object>> getFace(
            @Path("userId") String userId
    );

    @Headers({"Domain-Name:baidubce"})
    @GET("oauth/2.0/token")
    Observable<FaceBdAccessToken> refreshToken(
            @Query("grant_type") String grantType,
            @Query("client_id") String apiKey,
            @Query("client_secret") String secretKey
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/detect")
    Observable<String> detect(
            @Query("access_token") String accessToken,
            @Body String detect
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/match")
    Observable<String> match(
            @Query("access_token") String accessToken,
            @Body String match
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/search")
    Observable<FaceBdResult<FaceBdSearch>> search(
            @Query("access_token") String accessToken,
            @Body FaceBdSearchParam search
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceverify")
    Observable<FaceBdResult<FaceBdVerify>> verify(
            @Query("access_token") String accessToken,
            @Body List<FaceBdVerifyParam> param
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/user/add")
    Observable<FaceBdResult<FaceBdAddFace>> addFace(
            @Query("access_token") String accessToken,
            @Body FaceBdAddFaceParam addFace
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/user/update")
    Observable<FaceBdResult<String>> updateFace(
            @Query("access_token") String accessToken,
            @Body String updateFace
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/user/delete")
    Observable<FaceBdResult<String>> deleteFace(
            @Query("access_token") String accessToken,
            @Body FaceBdDeleteFaceParam deleteFace
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/user/get")
    Observable<FaceBdResult<String>> getUserInfo(
            @Query("access_token") String accessToken,
            @Body String getUserInfo
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/face/getlist")
    Observable<FaceBdResult<String>> getUserFaceList(
            @Query("access_token") String accessToken,
            @Body String getFaceList
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/group/getusers")
    Observable<FaceBdResult<String>> getGroupUserList(
            @Query("access_token") String accessToken,
            @Body String getGroupUserList
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/user/copy")
    Observable<FaceBdResult<String>> copyUser(
            @Query("access_token") String accessToken,
            @Body String copyUser
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/user/delete")
    Observable<FaceBdResult<String>> deleteUser(
            @Query("access_token") String accessToken,
            @Body String deleteUser
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/group/add")
    Observable<FaceBdResult<String>> addGroup(
            @Query("access_token") String accessToken,
            @Body String addGroup
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/group/delete")
    Observable<FaceBdResult<String>> deleteGroup(
            @Query("access_token") String accessToken,
            @Body String deleteGroup
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/group/getlist")
    Observable<FaceBdResult<String>> getGroupList(
            @Query("access_token") String accessToken,
            @Body String getGroupList
    );

}
