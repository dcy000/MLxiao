package com.example.han.referralproject.service;

import com.example.han.referralproject.bean.DetectionData;
import com.example.han.referralproject.bean.DetectionResult;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.MonthlyReport;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.children.model.SheetModel;
import com.example.han.referralproject.children.model.SongModel;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.radio.RadioEntity;
import com.example.han.referralproject.shopping.Goods;
import com.example.han.referralproject.shopping.Orders;
import com.example.han.referralproject.video.VideoEntity;
import com.example.module_doctor_advisory.bean.Doctor;
import com.gzq.lib_core.bean.SessionBean;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.model.HttpResult;

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





    /**
     * 根据useId查询所有用户信息
     *
     * @param userIds
     * @return
     */
    @GET("ZZB/api/user/info/users/")
    Observable<HttpResult<ArrayList<UserInfoBean>>> queryAllLocalUsers(@Query("users") String userIds);

    /**
     * 提交健康日记的数据
     *
     * @param userId
     * @param na
     * @param sports
     * @param drink
     * @return
     */
    @POST("ZZB/ai/insert_influence")
    Observable<Object> postHealthDiary(
            @Query("userid") String userId,
            @Query("na") String na,
            @Query("sports") String sports,
            @Query("drink") String drink
    );

    /**
     * 获取儿童娱乐资源
     *
     * @param page
     * @param limit
     * @return
     */
    @GET("ZZB/rep/sel_music_danforapp")
    Observable<HttpResult<List<SheetModel>>> getChildEduSheetList(
            @Query("page") int page,
            @Query("limit") int limit
    );

    /**
     * 获取儿歌
     *
     * @param page
     * @param limit
     * @param mid
     * @param type
     * @param wr
     * @return
     */
    @GET("ZZB/rep/selSomeImitate")
    Observable<HttpResult<List<SongModel>>> getChildEduSongListBySheetId(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("mid") int mid,
            @Query("type") int type,
            @Query("wr") String wr
    );

    /**
     * 获取广播列表
     *
     * @param type
     * @param page
     * @param limit
     * @param mid
     * @return
     */
    @GET("ZZB/rep/selSomeImitate")
    Observable<HttpResult<List<RadioEntity>>> getFM(
            @Query("type") int type,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("mid") int mid
    );

    /**
     * 获取视频列表
     *
     * @param tag1
     * @param tag2
     * @param flag
     * @param page
     * @param pagesize
     * @return
     */
    @GET("ZZB/vc/selAllUpload")
    Observable<HttpResult<List<VideoEntity>>> getVideoList(
            @Query("tag1") int tag1,
            @Query("tag2") int tag2,
            @Query("flag") int flag,
            @Query("page") int page,
            @Query("pagesize") int pagesize
    );

    /**
     * 充值
     *
     * @param eqid
     * @param bba
     * @param time
     * @param userId
     * @return
     */
    @POST("ZZB/br/chongzhi")
    Observable<HttpResult<String>> recharge(
            @Query("eqid") String eqid,
            @Query("bba") double bba,
            @Query("time") long time,
            @Query("bid") String userId
    );

    /**
     * 根据userId查询该账户绑定的医生信息
     *
     * @param userId
     * @return
     */
    @Deprecated
    @GET("ZZB/docter/search_OneDocter")
    Observable<HttpResult<Doctor>> queryDoctorInformation(@Query("bid") String userId);

    /**
     * 视频通话扣费
     *
     * @param docterid
     * @param eqid
     * @param time
     * @param bid
     * @return
     */
    @POST("ZZB/eq/koufei")
    Observable<String> charge(
            @Query("docterid") String docterid,
            @Query("eqid") String eqid,
            @Query("time") int time,
            @Query("bid") String bid
    );


    /**
     * 获取月报告
     *
     * @param userId
     * @param state
     * @return
     */
    @GET("ZZB/ai/sel")
    Observable<HttpResult<MonthlyReport>> getMonthlyReport(
            @Query("userid") String userId,
            @Query("state") String state
    );

    /**
     * 获取周报告
     *
     * @param userId
     * @param state
     * @return
     */
    @GET("ZZB/ai/sel")
    Observable<HttpResult<WeekReportModel>> getWeeklyReport(
            @Query("userid") String userId,
            @Query("state") String state
    );

    /**
     * 根据疾病名字获取疾病详情
     *
     * @param disableName
     * @return
     */
    @GET("ZZB/bl/selSugByBname")
    Observable<HttpResult<DiseaseResult>> queryDisableDetailByName(@Query("bname") String disableName);

    /**
     * 重置密码
     *
     * @param account
     * @param pwd
     * @return
     */
    @POST("ZZB/acc/update_account_pwd")
    Observable<Object> setPassWord(@Query("account") String account, @Query("pwd") String pwd);

    /**
     * 检验该手机账号是否存在
     *
     * @param cate
     * @param account
     * @return
     */
    @GET("ZZB/acc/sel_account")
    Observable<Object> isPhoneUsable(@Query("cate") String cate, @Query("account") String account);


    /**
     * 查询app版本号
     *
     * @param channelName
     * @return
     */
    @GET("ZZB/vc/selone")
    Observable<HttpResult<VersionInfoBean>> getAppVersion(@Query("vname") String channelName);

    /**
     * 获取医嘱信息
     *
     * @param userId
     * @return
     */
    @GET("ZZB/bl/selYzAndTime")
    Observable<HttpResult<List<YzInfoBean>>> getMedicalOrders(@Query("userid") String userId);



    /**
     * 获取订单列表
     *
     * @param pay_state
     * @param delivery_state
     * @param display_state
     * @param bname
     * @param page
     * @param limit
     * @return
     */
    @GET("ZZB/order/one_more_orders")
    Observable<HttpResult<List<Orders>>> getOrderList(
            @Query("pay_state") String pay_state,
            @Query("delivery_state") String delivery_state,
            @Query("display_state") String display_state,
            @Query("bname") String bname,
            @Query("page") String page,
            @Query("limit") String limit
    );

    /**
     * 取消发起的预付订单
     *
     * @param pay_state
     * @param delivery_state
     * @param display_state
     * @param orderid
     * @return
     */
    @POST("ZZB/order/delivery_del")
    Observable<Object> cancelPayOrder(
            @Query("pay_state") String pay_state,
            @Query("delivery_state") String delivery_state,
            @Query("display_state") String display_state,
            @Query("orderid") String orderid);

    /**
     * 商品列表
     *
     * @param state
     * @return
     */
    @GET("ZZB/order/OneType_state")
    Observable<HttpResult<List<Goods>>> getGoods(@Query("state") int state);




    /**
     * 对医生进行评价
     *
     * @param docId
     * @param userId
     * @param content
     * @param score
     * @param time
     * @param daid
     * @return
     */
    @POST("ZZB/pj/insert")
    Observable<Object> appraiseDoctor(
            @Query("docterid") String docId,
            @Query("bid") String userId,
            @Query("content") String content,
            @Query("score") int score,
            @Query("time") long time,
            @Query("daid") int daid
    );


}
