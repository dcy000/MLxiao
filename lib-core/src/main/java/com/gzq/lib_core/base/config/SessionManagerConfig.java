package com.gzq.lib_core.base.config;

import android.content.Context;

import com.gzq.lib_core.session.SessionConfig;

/**
 * created on 2018/10/19 17:04
 * created by: gzq
 * description: 用户信息管理里配置
 */
public interface SessionManagerConfig {
    void session(Context context, SessionConfig.Builder builder);
}
