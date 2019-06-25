package com.gcml.health.assistant.model;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.health.assistant.model.entity.AbnormalEntity;
import com.gcml.health.assistant.model.entity.AbnormalRecommendEntity;
import com.gcml.health.assistant.model.entity.AbnormalTaskList;
import com.gcml.health.assistant.model.entity.AllTaskList;

import java.util.List;

import io.reactivex.Observable;

public class AssistantRepository {

    private AssistantService service = RetrofitHelper.service(AssistantService.class);

    public Observable<List<AbnormalEntity>> abnormals() {
        return service.abnormals(UserSpHelper.getUserId())
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<AbnormalRecommendEntity>> abnormalRecommends(int abnormalId) {
        return service.abnormalRecommends(abnormalId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<AbnormalTaskList> abnormalTasks(int abnormalId, int recommendId) {
        return service.abnormalTasks(UserSpHelper.getUserId(), abnormalId, recommendId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> confirmTask(int taskId) {
        return service.confirmTask(taskId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<AllTaskList> allTask(int taskId) {
        return service.allTask(taskId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> confirmOneTask(int taskId) {
        return service.comfirmOneTask(taskId)
                .compose(RxUtils.apiResultTransformer());
    }
}
