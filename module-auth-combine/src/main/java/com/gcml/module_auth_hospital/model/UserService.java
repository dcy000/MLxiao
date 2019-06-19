package com.gcml.module_auth_hospital.model;

import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.module_auth_hospital.postinputbean.SignUpBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.gcml.common.constant.Global.URI;


public interface UserService {

    /**
     * 患者账号退出登录
     * 必填字段
     * "bid"
     */
    @POST(URI + "/sys/br/off/line")
    Observable<ApiResult<Object>> signOut(@Query("bid") String userId);

    /**
     * 患者账号登陆
     * 必填字段
     * "category"
     * "username"
     * "password"
     */
    @POST(URI + "/sys/login/appLogin")
    Observable<ApiResult<UserToken>> signIn(@Body UserPostBody body);

    /**
     * 患者身份证信息登录
     * 必填字段
     * "category"
     * "sfz"
     */
    @POST(URI + "/sys/login/sfzLogin")
    Observable<ApiResult<UserToken>> signInByIdCard(@Body UserPostBody body);

    /**
     * 校验密码
     *
     * @param body
     * @return
     */
    @POST(URI + "/sys/login/sfzLogin/pwd")
    Observable<ApiResult<UserToken>> signInByIdCardPsw(@Body UserPostBody body);

    /**
     * 注册
     */
    @POST(URI + "/br/appadd")
    Observable<ApiResult<UserEntity>> signUp(@Body SignUpBean bean, @Query("pwd") String pwd);

    /**
     * 根据token获取 用户信息
     */
    @GET(URI + "/api/user/info/byToken/")
    Observable<ApiResult<UserEntity>> getUserInfoByToken();

    @PUT(URI + "/api/user/info/{patientId}/")
    Observable<ApiResult<Object>> updateUserInfo(@Path("patientId") String path, @Body PostUserEntity entity);

    /**
     * @param type: 1.账号 2.手机号 3.身份证 4.百度人脸id
     */
    @POST(URI + "/acc/sel_account")
    Observable<ApiResult<Object>> isAccountExist(
            @Query("account") String account,
            @Query("type") int type
    );

    /**
     * 雄安项目专用
     *
     * @param account
     * @param type    1.账号 2.手机号 3.身份证 4.百度人脸id
     * @param cate    1.运营人员,2:医生,3:患者,4:财务
     * @return
     */
    @POST(URI + "/acc/sel_account")
    Observable<ApiResult<Object>> isAccountExist(
            @Query("account") String account,
            @Query("type") int type,
            @Query("cate") int cate
    );
}
