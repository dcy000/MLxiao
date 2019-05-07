package com.gcml.module_doctor_advisory.net;

import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.get.RobotAmount;
import com.gcml.module_doctor_advisory.bean.ContractInfo;
import com.gcml.module_doctor_advisory.bean.DiseaseResult;
import com.gcml.module_doctor_advisory.bean.Docter;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.module_doctor_advisory.bean.DoctorInfoBean;
import com.gcml.module_doctor_advisory.bean.YzInfoBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2018/9/28.
 */

public interface QianYueService {

    @GET("/ZZB/docter/sel_one_doctor_con")
    Observable<ApiResult<DoctorInfoBean>> getDoctInfo(@Query("docterid") String doctorId);

    @GET("ZZB/br/updateUserState")
    Observable<ApiResult<Object>> cancelContract(@Query("bid") String bid);

    @GET("ZZB/docter/search_OneDocter")
    Observable<ApiResult<Doctor>> DoctorInfo(@Query("bid") String userId);

    /**
     * 获取拨打医生的号码
     *
     * @param doctorId
     * @return
     */
    @GET("ZZB/docter/getDocterYunXinId")
    Observable<ApiResult<String>> getCallId(
            @Query("doctorId") String doctorId
    );

    @GET("ZZB/bl/selYzAndTime")
    Observable<ApiResult<List<YzInfoBean>>> getYzList(@Query("userid") String userId);

    @GET("ZZB/bl/selSugByBname")
    Observable<ApiResult<DiseaseResult>> getJibing(@Query("bname") String bname);

    @GET("ZZB/docter/seldoctors")
    Observable<ApiResult<List<Docter>>> doctor_list(@Query("start") int start, @Query("limit") int limit);

    @GET("ZZB/docter/search_online_status")
    Observable<ApiResult<ArrayList<Docter>>> onlinedoctor_list(
            @Query("online_status") int online_status,
            @Query("doctername") String doctername,
            @Query("page") int page,
            @Query("pagesize") int pagesize
    );

    @GET("ZZB/br/selOneUser_con")
    Observable<ApiResult<UserEntity>> PersonInfo(@Query("bid") String userId);

    @GET("ZZB/eq/eq_amount")
    Observable<ApiResult<RobotAmount>> Person_Amount(@Query("eqid") String eqid);

    @GET("ZZB/docter/docter_user")
    Observable<ApiResult<ContractInfo>> getContractInfo(@Query("userid") String userid, @Query("docterid") String docterid);

    @POST("ZZB/br/qianyue")
    Observable<ApiResult<Object>> bindDoctor(@Query("bid") String userId, @Query("doid") String doid);
}
