package com.gcml.common.service;

import io.reactivex.Observable;

public interface ITaskProvider {
    /**
     * 查询用户是否完成健康问卷.
     * @return
     */
    Observable<Object> isTaskHealth();
}
