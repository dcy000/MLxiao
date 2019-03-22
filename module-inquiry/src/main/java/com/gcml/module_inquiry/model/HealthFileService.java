package com.gcml.module_inquiry.model;

import com.gcml.common.data.DetectionResult;
import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.post.DetectionData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2019/1/21.
 */

public interface HealthFileService {
    String INQUIRY = "http://http://118.31.73.176:8080/ZZB/api/health/inquiry/";

    @GET("ZZB/br/seltoken")
    Observable<ApiResult<String>> getQiniuToken();

    @GET("ZZB/docter/seldoctors/")
    Observable<ApiResult<List<Docter>>> getDoctors(
            @Query("start") Integer index,
            @Query("limit") Integer limit);

    @FormUrlEncoded
    @POST("ZZB/br/qianyue/")
    Observable<ApiResult<Object>> bindDoctor(
            @Field("doid") String doid,
            @Field("bid") String bid,
            @Field("user_sign") String userSign);

    @POST("ZZB/api/healthMonitor/detection/{userId}/")
    Observable<ApiResult<List<DetectionResult>>> postMeasureData(
            @Path("userId") String userId,
            @Body ArrayList<DetectionData> datas);

    @POST("ZZB/api/health/inquiry/")
    Observable<ApiResult<WenZhenReultBean>> postWenZen(
            @Field("userId") String userId,
            @Body WenZhenBean bean);


}
