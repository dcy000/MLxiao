package com.gcml.module_inquiry.model;

import com.gcml.common.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2019/1/21.
 */

public interface HealthFileService {

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


}
