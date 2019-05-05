package com.gcml.module_hypertension_manager.net;

import com.gcml.common.http.ApiResult;
import com.gcml.module_hypertension_manager.bean.DailyRecommendIntake;
import com.gcml.module_hypertension_manager.bean.DiagnoseInfoBean;
import com.gcml.module_hypertension_manager.bean.FoodMateratilDetail;

import java.util.ArrayList;

import io.reactivex.Observable;
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
}
