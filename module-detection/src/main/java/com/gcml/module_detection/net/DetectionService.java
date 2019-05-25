package com.gcml.module_detection.net;

import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.post.DetectionData;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DetectionService {
    @POST("/open/common/api/healthMonitor/detection/{patientId}/")
    Observable<ApiResult<Object>> postMeasureData(
            @Path("patientId") String userId,
            @Body ArrayList<DetectionData> datas);
}
