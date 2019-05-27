package com.gcml.module_health_profile.data;

import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.module_health_profile.bean.GuardianInfo;
import com.gcml.module_health_profile.bean.HealthProfileMenuBean;
import com.gcml.module_health_profile.bean.HealthRecordBean;
import com.gcml.module_health_profile.bean.OutputMeasureBean;
import com.gcml.module_health_profile.bean.TiZhiBean;
import com.gcml.module_health_profile.bean.WarnBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HealthProfileAPI {
    /**
     * 获取档案列表
     *
     * @return
     */
    @Headers({"Domain-Name:ylservice"})
    @GET("ZZB/api/health/record/records/")
    Observable<ApiResult<List<HealthProfileMenuBean>>> getHealthProfileMenu();

    /**
     * 获取记录列表
     *
     * @param rdRecordId
     * @param userId
     * @return
     */
    @Headers({"Domain-Name:ylservice"})
    @GET("ZZB/api/health/record/{rdRecordId}/user/{userId}/")
    Observable<ApiResult<List<HealthRecordBean>>> getHealthRecordList(
            @Path("rdRecordId") String rdRecordId,
            @Path("userId") String userId
    );

    /**
     * 查询医生的详细资料
     *
     * @param doctorId
     * @return
     */
    @Headers({"Domain-Name:ylservice"})
    @GET("ZZB/docter/sel_one_doctor_con")
    Observable<ApiResult<Doctor>> queryDoctorInfo(@Query("docterid") String doctorId);

    @Headers({"Domain-Name:ylservice"})
    @GET("ZZB/api/health/record/{rdRecordId}/detection/{userRecordId}/")
    Observable<ApiResult<List<OutputMeasureBean>>> getHealthRecordMeasureResult(
            @Path("rdRecordId") String rdRecordId,
            @Path("userRecordId") String userRecordId
    );

    @Headers({"Domain-Name:ylservice"})
    @GET("ZZB/api/guardian/user/{userId}/guardians/")
    Observable<ApiResult<List<GuardianInfo>>> getGuardians(@Path("userId") String userId);

    @Headers({"Domain-Name:ylservice"})
    @GET("ZZB/api/user/warning/warnings/")
    Observable<ApiResult<List<WarnBean>>> getWanings(@Query("userId") String userId);

    // 体质检测 记录
    @Headers({"Domain-Name:ylservice"})
    @GET("ZZB/api/health/inquiry/constitution/history/")
    Observable<ApiResult<List<TiZhiBean>>> getConstitution(@Query("userId") String userId);

}
