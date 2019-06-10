package com.gcml.module_detection.net;

import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.module_detection.bean.LatestDetecBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static com.gcml.common.constant.Global.URI;

public interface DetectionService {
    @POST(URI + "/api/healthMonitor/detection/{patientId}/")
    Observable<ApiResult<Object>> postMeasureData(
            @Path("patientId") String userId,
            @Body ArrayList<DetectionData> datas);

    @GET(URI + "/bl/getPatientHealthData")
    Observable<ApiResult<List<LatestDetecBean>>> getLatestDetectionData();
}
