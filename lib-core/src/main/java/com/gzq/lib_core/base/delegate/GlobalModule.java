package com.gzq.lib_core.base.delegate;

import android.content.Context;
import com.gzq.lib_core.base.GlobalConfig;

/**
 * created on 2018/10/18 18:02
 * created by: gzq
 * description: 全局配置
 */
public interface GlobalModule {
    /**
     * 使用{@link com.gzq.lib_core.base.GlobalConfig.Builder}给框架配置一些配置参数
     * @param context
     * @param builder
     */
    void applyOptions(Context context, GlobalConfig.Builder builder);
}
