package com.gcml.health.measure.utils;

import android.arch.lifecycle.Lifecycle;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/23 16:01
 * created by: gzq
 * description: TODO
 */
public interface LifecycleUtils {
    /**
     * fragment中Retrofit解绑的生命周期节点
     */
    Lifecycle.Event LIFE= Lifecycle.Event.ON_PAUSE;
}
