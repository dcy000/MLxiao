package com.gcml.task.network;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.recommend.bean.post.TaskSchemaResultBean;
import com.gcml.task.bean.Post.TaskWheelBean;
import com.gcml.task.bean.get.TaskHealthBean;
import com.gcml.task.bean.Post.TaskSchemaBean;
import com.gcml.task.bean.get.TaskBean;
import com.gcml.task.bean.get.TaskReportBean;

import io.reactivex.Observable;

public class TaskRepository {

    private TaskService mTaskService = RetrofitHelper.service(TaskService.class);

    public Observable<TaskBean> taskListFromApi(
            String userId) {
        return mTaskService.task(userId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<TaskHealthBean> taskHealthListFromApi() {
        return mTaskService.taskHealth()
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> isTaskHealthListFromApi(
            String userId) {
        return mTaskService.isTaskHealth(userId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<TaskReportBean> taskReportListFromApi(
            String userid, String state) {
        return mTaskService.taskReport(userid, state)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<TaskSchemaResultBean> taskHealthListForApi(
            TaskSchemaBean tModel,
            String userId) {
        return mTaskService.taskHealthUpload(tModel, userId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> taskHealthDeleteForApi(
            String userId) {
        return mTaskService.taskHealthDelete(userId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> taskWheelListForApi(
            TaskWheelBean factor,
            String userId) {
        return mTaskService.taskWheelUpload(factor, userId)
                .compose(RxUtils.apiResultTransformer());
    }
}
