package com.gcml.health.assistant.model;

import com.gcml.common.http.ApiResult;
import com.gcml.health.assistant.model.entity.AbnormalEntity;
import com.gcml.health.assistant.model.entity.AbnormalRecommendEntity;
import com.gcml.health.assistant.model.entity.AbnormalTaskList;
import com.gcml.health.assistant.model.entity.AllTaskList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.gcml.common.constant.Global.URI;

public interface AssistantService {

    /**
     * 异常指标
     */
    @POST(URI + "/api/app/user/get/target/anomaly")
    Observable<ApiResult<List<AbnormalEntity>>> abnormals(
            @Query("userId") String userId
    );

    /**
     * 指标建议
     */
    @POST(URI + "/api/app/user/get/target/advice")
    Observable<ApiResult<List<AbnormalRecommendEntity>>> abnormalRecommends(
            @Query("anomalyId") int abnormalId
    );

    /**
     * 任务列表
     */
    @POST(URI + "/api/app/user/get/target/task/list")
    Observable<ApiResult<AbnormalTaskList>> abnormalTasks(
            @Query("userId") String userId,
            @Query("anomalyTargetId") int abnormalId,
            @Query("adviceId") int recommendId
    );

    /**
     * 确认并执行任务
     */
    @POST(URI + "/api/app/user/target/task/confirm")
    Observable<ApiResult<Object>> confirmTask(
            @Query("taskId") int taskId
    );

    /**
     * 全部任务
     */
    @POST(URI + "/api/app/user/get/target/user/task/list")
    Observable<ApiResult<AllTaskList>> allTask(
            @Query("taskId") int taskId
    );


    /**
     * 全部任务
     */
    @POST(URI + "/api/app/user/target/one/task/confirm")
    Observable<ApiResult<Object>> comfirmOneTask(
            @Query("userTaskId") int taskId
    );
}
