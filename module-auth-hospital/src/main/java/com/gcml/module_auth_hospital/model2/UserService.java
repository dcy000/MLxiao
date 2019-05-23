package com.gcml.module_auth_hospital.model2;

import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    /**
     * 患者账号登陆
     * 必填字段
     * "category"
     * "username"
     * "password"
     */
    @POST("/open/common/sys/login/appLogin")
    Observable<ApiResult<UserToken>> signIn(@Body UserPostBody body);

    /**
     * 患者身份证信息登录
     * 必填字段
     * "category"
     * "sfz"
     */
    @POST("/open/common/sys/login/sfzLogin")
    Observable<ApiResult<UserToken>> signInByIdCard(@Body UserPostBody body);


    @POST("ZZB/login/refresh/")
    Observable<ApiResult<UserToken>> refreshToken(
            @Header("equipmentId") String deviceId,
            @Query("userId") String userId,
            @Query("refreshToken") String refreshToken
    );

    @FormUrlEncoded()
    @POST("ZZB/acc/sel_account")
    Observable<ApiResult<Object>> hasAccount(
            @Field("cate") String cate,
            @Field("account") String account
    );

    @GET("ZZB/br/GainCode")
    Observable<ApiResult<Code>> fetchCode(
            @Query("mobile") String phone
    );

    @POST("ZZB/acc/update_account_pwd")
    Observable<ApiResult<Object>> updatePassword(
            @Query("account") String account,
            @Query("pwd") String pwd
    );

    @POST("ZZB/br/appadd")
    Observable<ApiResult<UserEntity>> signUp(
            @Query("eqid") String deviceId,
            @Query("tel") String account,
            @Query("pwd") String pwd
    );

    @POST("ZZB/br/appadd")
    Observable<ApiResult<UserEntity>> signUpByIdCard(
            @Query("eqid") String deviceId,
            @Query("bname") String name,
            @Query("sex") String sex,
//            @Query("nation") String nation
            @Query("sfz") String idrad,
            @Query("dz") String address,
            @Query("pwd") String pwd
    );

    @PUT("ZZB/api/user/info/{userId}/")
    Observable<ApiResult<Object>> putProfile(
            @Path("userId") String userId,
            @Body UserEntity user
    );

    @GET("ZZB/br/selOneUserEverything")
    Observable<ApiResult<UserEntity>> getProfile(
            @Query("bid") String userId
    );

    @GET("ZZB/api/user/info/idCard/{idCard}/")
    Observable<ApiResult<Object>> isIdCardNotExit(
            @Path("idCard") String idCard
    );

    @GET("ZZB/api/user/info/users/")
    Observable<ApiResult<List<UserEntity>>> getAllUsers(
            @Query("users") String usersIds
    );

    @POST("ZZB/api/server/login/")
    Observable<ApiResult<UserToken>> getServiceProvider(
            @Query("account") String server_account,
            @Query("password") String server_password);

    @GET("ZZB/api/server/")
    Observable<ApiResult<ServerBean>> getServer(
            @Header("serverId") String serverId);

    @FormUrlEncoded()
    @POST("ZZB/api/server/login/")
    Observable<ApiResult<ServerBean>> serverSignIn(
            @Header("equipmentId") String equipmentId,
            @Field("account") String account,
            @Field("password") String password);

    @FormUrlEncoded()
    @POST("ZZB/login/docter_login/")
    Observable<ApiResult<DoctorEntity>> doctorSignIn(
            @Header("equipmentId") String equipmentId,
            @Field("username") String account,
            @Field("password") String password);


}
