package com.gcml.module_detection.net;

import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.module_detection.bean.LatestDetecBean;
import com.gcml.common.data.PostDataCallBackBean;

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
    Observable<ApiResult<List<PostDataCallBackBean>>> postMeasureData(
            @Path("patientId") String userId,
            @Body ArrayList<DetectionData> datas);

    @GET(URI + "/bl/getPatientHealthData")
    Observable<ApiResult<List<LatestDetecBean>>> getLatestDetectionData();


    @GET("ZZB/api/health/record/{rdRecordId}/detection/")
    Observable<ApiResult<List<String>>> getDevices(
            @Path("rdRecordId") String rdRecordId
    );
    @POST("ZZB/api/health/record/healthRecord/{userRecordId}/")
    Observable<Object> postHealthRecordMeasureData(
            @Path("userRecordId") String userRecordId,
            @Body ArrayList<DetectionData> datas
    );
}
