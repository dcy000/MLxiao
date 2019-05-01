package com.gcml.task.component;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.service.ITaskProvider;
import com.gcml.task.network.TaskRepository;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;
@Route(path = "/task/task/provider")
public class TaskProviderImp implements ITaskProvider {
    @Override
    public Observable<Object> isTaskHealth() {
        return new TaskRepository().isTaskHealthListFromApi(UserSpHelper.getUserId());
    }
}
