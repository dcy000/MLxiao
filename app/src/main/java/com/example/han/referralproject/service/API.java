package com.example.han.referralproject.service;

import com.example.han.referralproject.bean.DetectionData;
import com.example.han.referralproject.bean.DetectionResult;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.MonthlyReport;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.children.model.SheetModel;
import com.example.han.referralproject.children.model.SongModel;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.radio.RadioEntity;
import com.example.han.referralproject.video.VideoEntity;
import com.example.module_doctor_advisory.bean.Doctor;
import com.gzq.lib_core.bean.SessionBean;
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
     * 获取医嘱信息
     *
     * @param userId
     * @return
     */
    @GET("ZZB/bl/selYzAndTime")
    Observable<HttpResult<List<YzInfoBean>>> getMedicalOrders(@Query("userid") String userId);




}
