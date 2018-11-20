package com.example.han.referralproject.service;

import com.example.han.referralproject.bean.DetectionData;
import com.example.han.referralproject.bean.DetectionResult;
import com.example.han.referralproject.bean.SessionBean;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.shopping.Order;
import com.gzq.lib_core.http.model.HttpResult;
import com.ml.edu.data.ApiResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    /**
     * 检查是否是正常血压数据
     *
     * @param userId
     * @param datas
     * @return
     */
    @POST("ZZB/api/healthMonitor/detection/{userId}/check/")
    Observable<HttpResult<Object>> checkIsNormalData(@Path("userId") String userId, @Body List<DetectionData> datas);

    /**
     * 新的上传数据的接口
     *
     * @param userId
     * @param datas
     * @return
     */
    @POST("ZZB/api/healthMonitor/detection/{userId}/")
    Observable<HttpResult<List<DetectionResult>>> postMeasureData(@Path("userId") String userId, @Body ArrayList<DetectionData> datas);

    /**
     * 获取登录token
     *
     * @param userName
     * @param password
     * @return
     */
    @POST("/ZZB/login/applogin")
    Observable<HttpResult<SessionBean>> login(@Query("username") String userName, @Query("password") String password);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    @GET("ZZB/br/selOneUserEverything")
    Observable<HttpResult<UserInfoBean>> queryUserInfo(@Query("bid") String userId);

    /**
     * 绑定签约医生
     *
     * @param userId
     * @param doctorId
     * @return
     */
    @POST("ZZB/br/qianyue")
    Observable<Object> bindDoctor(@Query("bid") String userId, @Query("doid") String doctorId);

    /**
     * 生成订单的
     *
     * @param userId
     * @param eqid
     * @param articles
     * @param number
     * @param price
     * @param photo
     * @param time
     * @return
     */
    @POST("ZZB/order/panding_pay")
    Observable<HttpResult<Object>> orderInfo(
            @Query("userid") String userId,
            @Query("eqid") String eqid,
            @Query("articles") String articles,
            @Query("number") String number,
            @Query("price") String price,
            @Query("photo") String photo,
            @Query("time") String time
    );

    /**
     * 修改个人基本信息
     *
     * @param userId
     * @param height
     * @param weight
     * @param eatingHabits
     * @param smoke
     * @param drink
     * @param exerciseHabits
     * @param mh
     * @param dz
     * @return
     */
    @POST("ZZB/br/update_user_onecon")
    Observable<Object> alertUserInfo(
            @Query("bid") String userId,
            @Query("height") String height,
            @Query("weight") String weight,
            @Query("eating_habits") String eatingHabits,
            @Query("smoke") String smoke,
            @Query("drink") String drink,
            @Query("exercise_habits") String exerciseHabits,
            @Query("mh") String mh,
            @Query("dz") String dz
    );
}
