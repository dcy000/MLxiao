package com.example.module_doctor_advisory.service;

import com.gzq.lib_core.bean.AlreadyYuyue;
import com.example.module_doctor_advisory.bean.Docter;
import com.example.module_doctor_advisory.bean.Doctor;
import com.example.module_doctor_advisory.bean.RobotAmount;
import com.example.module_doctor_advisory.bean.YuYueInfo;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DoctorAPI {
    /**
     * 查询所有医生
     *
     * @param start
     * @param limit
     * @return
     */
    @GET("ZZB/docter/seldoctors")
    Observable<HttpResult<List<Docter>>> getDoctors(
            @Query("start") int start,
            @Query("limit") int limit
    );
    /**
     * 在线医生
     *
     * @param online_status
     * @param doctername
     * @param page
     * @param pagesize
     * @return
     */
    @GET("ZZB/docter/search_online_status")
    Observable<HttpResult<List<Docter>>> getOnlineDoctor(
            @Query("online_status") int online_status,
            @Query("doctername") String doctername,
            @Query("page") int page,
            @Query("pagesize") int pagesize
    );

    /**
     * 根据机器id查询这台机器剩余金额
     *
     * @return
     */
    @GET("ZZB/eq/eq_amount")
    Observable<HttpResult<RobotAmount>> queryMoneyById(@Query("eqid") String eqid);

    /**
     * 查询医生的详细资料
     *
     * @param doctorId
     * @return
     */
    @GET("ZZB/docter/sel_one_doctor_con")
    Observable<HttpResult<Doctor>> queryDoctorInfo(@Query("docterid") String doctorId);

    /**
     * 取消签约医生
     *
     * @param userId
     * @return
     */
    @POST("ZZB/br/updateUserState")
    Observable<Object> cancelSignDoctor(@Query("bid") String userId);

    /**
     * 查询预约信息
     *
     * @param userId
     * @param docId
     * @return
     */
    @GET("ZZB/bl/selAllreserveByDoidAndUserid")
    Observable<HttpResult<List<YuYueInfo>>> querySignedDoctorInfo(
            @Query("userid") String userId,
            @Query("docterid") String docId);

    @POST("ZZB/bl/delReserveByRid")
    Observable<Object> cancelReservation(
            @Query("rid")int rid
    );

    /**
     * 预约自己的签约医生
     *
     * @param start_time
     * @param end_time
     * @param userId
     * @param docterid
     * @return
     */
    @POST("ZZB/bl/insertReserve")
    Observable<Object> signMyDotor(
            @Query("start_time") String start_time,
            @Query("end_time") String end_time,
            @Query("userid") String userId,
            @Query("docterid") String docterid
    );
    /**
     * 查询该医生被预约的时间段
     *
     * @param docterid
     * @return
     */
    @POST("ZZB/bl/selReserveStart_time")
    Observable<HttpResult<List<AlreadyYuyue>>> queryDoctorReservationList(
            @Query("docterid") String docterid
    );
}
