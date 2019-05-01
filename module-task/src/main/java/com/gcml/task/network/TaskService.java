package com.gcml.task.network;

import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.post.TaskSchemaResultBean;
import com.gcml.task.bean.Post.TaskWheelBean;
import com.gcml.task.bean.get.TaskHealthBean;
import com.gcml.task.bean.Post.TaskSchemaBean;
import com.gcml.task.bean.get.TaskBean;
import com.gcml.task.bean.get.TaskReportBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskService {

    /**
     * 获取用户任务列表.
     */
    @GET("/ZZB/api/health/compliance/task/{userId}/")
    Observable<ApiResult<TaskBean>> task(
            @Path("userId") String userId
    );

    /**
     * 获取用户健康问卷.
     */
    @GET("/ZZB/api/healthMonitor/questionnaire/hypertension/compliance/")
    Observable<ApiResult<TaskHealthBean>> taskHealth();

    /**
     * 查询用户是否完成健康问卷.
     */
    @GET("/ZZB/api/healthMonitor/questionnaire/hypertension/compliance/{userId}/")
    Observable<ApiResult<Object>> isTaskHealth(
            @Path("userId") String userId
    );

    /**
     * 查询用户是否完成健康问卷.
     */
    @GET("/ZZB/ai/sel/")
    Observable<ApiResult<TaskReportBean>> taskReport(
            @Query("userid") String userid,
            @Query("state") String state
    );

    /**
     * 上传用户健康问卷答案.
     */
    @POST("/ZZB/api/healthMonitor/questionnaire/hypertension/compliance/{userId}/")
    Observable<ApiResult<TaskSchemaResultBean>> taskHealthUpload(
            @Body() TaskSchemaBean tModel,
            @Path("userId") String userId
    );

    /**
     * 删除用户Wheel选择答案.
     */
    @DELETE("/ZZB/api/healthMonitor/questionnaire/hypertension/compliance/{userId}/")
    Observable<ApiResult<Object>> taskHealthDelete(
            @Path("userId") String userId
    );

    /**
     * 上传用户Wheel选择答案.
     */
    @POST("/ZZB/api/healthMonitor/detection/influence/{userId}/")
    Observable<ApiResult<Object>> taskWheelUpload(
            @Body() TaskWheelBean factor,
            @Path("userId") String userId
    );
}
