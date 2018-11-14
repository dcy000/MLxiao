package com.gzq.lib_core.base.config;

import android.content.Context;

import com.gzq.lib_core.crash.CaocConfig;

/**
 * created on 2018/10/19 17:04
 * created by: gzq
 * description: 崩溃配置
 */
public interface CrashManagerConfig {
    void crash(Context context, CaocConfig.Builder builder);
}
