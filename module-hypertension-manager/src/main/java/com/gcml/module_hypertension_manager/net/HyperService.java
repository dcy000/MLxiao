package com.gcml.module_hypertension_manager.net;

import com.gcml.common.http.ApiResult;
import com.gcml.module_hypertension_manager.bean.DailyRecommendIntake;
import com.gcml.module_hypertension_manager.bean.DiagnoseInfoBean;
import com.gcml.module_hypertension_manager.bean.FoodMateratilDetail;
import com.gcml.module_hypertension_manager.bean.MedicineBean;
import com.gcml.module_hypertension_manager.bean.PrimaryHypertensionBean;
import com.gcml.module_hypertension_manager.bean.PrimaryHypertensionQuestionnaireBean;
import com.gcml.module_hypertension_manager.bean.SportPlan;
import com.gcml.module_hypertension_manager.bean.ThisWeekHealthPlan;
import com.gcml.module_hypertension_manager.bean.WeekDietPlan;
import com.gcml.module_hypertension_manager.zhongyi.bean.HealthManagementAnwserBean;
import com.gcml.module_hypertension_manager.zhongyi.bean.HealthManagementResultBean;
import com.gcml.module_hypertension_manager.zhongyi.bean.OlderHealthManagementBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HyperService {
    @GET("ZZB/api/healthMonitor/hypertension/diagnose/{userId}/")
    Observable<ApiResult<DiagnoseInfoBean.DataBean>> getDiagnoseInfo(@Path("userId") String userId);

    @GET("ZZB/api/healthMonitor/hypertension/diagnose/{userId}/new/")
    Observable<ApiResult<DiagnoseInfoBean.DataBean>> getDiagnoseInfoNew(@Path("userId") String userId);

    @POST("ZZB/api/healthMonitor/hypertension/diagnose/primary/{userId}/")
    Observable<ApiResult<Object>> postOriginHypertension(@Path("userId") String userId, @Query("hypertensionPrimaryState") String hypertensionPrimaryState);

    @GET("ZZB/api/healthMonitor/lifeTherapy/food/dailyIntake/")
    Observable<ApiResult<DailyRecommendIntake>> getDailyRecommendedIntake(@Query("userId") String userId);

    @GET("ZZB/api/healthMonitor/lifeTherapy/food/")
    Observable<ApiResult<ArrayList<FoodMateratilDetail>>> getDailyFoodRecommendation(@Query("userId") String userId);

    @GET("ZZB/api/healthMonitor/medicine/hypertension/{userId}/")
    Observable<ApiResult<MedicineBean>> getMedicineProgram(@Path("userId") String userId);

    @GET("ZZB/api/healthMonitor/lifeTherapy/sport/")
    Observable<ApiResult<SportPlan>> getSportHealthPlan(@Query("userId") String userId);

    @GET("ZZB/api/healthMonitor/lifeTherapy/detectionReport/")
    Observable<ApiResult<ThisWeekHealthPlan>> getThisWeekPlan(@Query("userId") String userId);

    @GET("ZZB/api/healthMonitor/lifeTherapy/food/cookbook/")
    Observable<ApiResult<WeekDietPlan>> getWeekDietPlan(@Query("userId") String userId);

    @GET("ZZB/api/healthMonitor/questionnaire/hypertension/risk/")
    Observable<ApiResult<PrimaryHypertensionQuestionnaireBean.DataBean>> getNormalHightQuestion();

    @POST("ZZB/api/healthMonitor/questionnaire/hypertension/risk/{userId}/")
    Observable<ApiResult<String>> postNormalHightQuestion(@Path("userId") String userId, @Body PrimaryHypertensionBean bean);

    @POST("ZZB/api/healthMonitor/hypertension/diagnose/target/{userId}/")
    Observable<ApiResult<Object>> postTargetHypertension(@Path("userId") String userId, @Query("hypertensionTarget") String state);

    @GET("ZZB/api/healthMonitor/questionnaire/hypertension/heart/")
    Observable<ApiResult<PrimaryHypertensionQuestionnaireBean.DataBean>> getHypertensionQuestion();

    @POST("ZZB/api/healthMonitor/questionnaire/hypertension/heart/{userId}/")
    Observable<ApiResult<Object>> postHypertensionQuestion(@Path("userId") String userId, @Body PrimaryHypertensionBean bean);

    @GET("ZZB/api/healthMonitor/questionnaire/hypertension/primary/")
    Observable<ApiResult<PrimaryHypertensionQuestionnaireBean.DataBean>> getPrimaryHypertensionQuestion();

    @POST("ZZB/api/healthMonitor/questionnaire/hypertension/primary/{userId}/")
    Observable<ApiResult<Object>> postPrimaryHypertensionQuestion(@Path("userId") String userId, @Body PrimaryHypertensionBean bean);

    /**
     * 旧版中医体质获取问卷
     *
     * @return
     */
    @GET("ZZB/api/health/inquiry/constitution/questionnaire/")
    Observable<ApiResult<OlderHealthManagementBean.DataBean>> getHealthManagementForOlder();

    /**
     * 合版中医体质获取问卷
     *
     * @return
     */
    @GET("/open/common/api/health/inquiry/constitution/questionnaire/")
    Observable<ApiResult<OlderHealthManagementBean.DataBean>> getQuestionnaireForOlder();

    /**
     * 旧版中医体质提交问卷
     *
     * @param bean
     * @return
     */
    @POST("ZZB/api/health/inquiry/constitution/questionnaire/")
    Observable<ApiResult<List<HealthManagementResultBean.DataBean>>> postHealthManagementAnwser(@Body HealthManagementAnwserBean bean);

    /**
     * 合版中医体质提交问卷
     *
     * @return
     */
    @POST("/open/common/api/health/inquiry/constitution/questionnaire/")
    Observable<ApiResult<List<HealthManagementResultBean.DataBean>>> postQuestionnaireForOlder(@Body HealthManagementAnwserBean bean);
}
