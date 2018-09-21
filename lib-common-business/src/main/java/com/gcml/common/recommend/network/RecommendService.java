package com.gcml.common.recommend.network;

import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.repository.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2018/9/21.
 */

public interface RecommendService {

    @GET("/ZZB/api/mall/goods/")
    Observable<ApiResult<List<GoodBean>>> searchGoodsByName(
            @Query("goodsName") String goodName
    );


    @GET("/ZZB/api/mall/recommend/{userId}/")
    Observable<ApiResult<List<GoodBean>>> recommendGoodsByUser(
            @Path("userId") String userId
    );

    @POST("/ZZB/api/mall/recommend/detection/")
    Observable<ApiResult<List<GoodBean>>> recommendGoodsByDetection(
            @Body() List<DetectionData> detectionBeans
    );
}
