package com.gcml.auth.model;

import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.recommend.bean.get.RobotAmount;
import com.gcml.common.recommend.bean.get.ServicePackageBean;
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

    @FormUrlEncoded()
    @POST("ZZB/login/applogin")
    Observable<ApiResult<UserToken>> signIn(
            @Header("equipmentId") String deviceId,
            @Field("username") String account,
            @Field("password") String pwd
    );

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

    @FormUrlEncoded()
    @POST("ZZB/acc/selAccountIsExist")
    Observable<ApiResult<Object>> hasAccount2(
            @Field("cate") String cate,
            @Field("account") String account,
            @Field("name") String name
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
            @Query("sfz") String account,
            @Query("pwd") String pwd,
            @Query("bname") String name
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

    /**
     * 查询该用户的检测套餐是否生效
     *
     * @param userId
     * @return
     */
    @GET("ZZB/order/judge")
    Observable<ApiResult<ServicePackageBean>> queryServicePackage(
            @Query("userid") String userId
    );

    @GET("ZZB/eq/eq_amount")
    Observable<ApiResult<RobotAmount>> amount(@Query("eqid") String eqid);

    @GET("ZZB/docter/search_OneDocter")
    Observable<ApiResult<Doctor>> doctor(@Query("bid") String userId);
}