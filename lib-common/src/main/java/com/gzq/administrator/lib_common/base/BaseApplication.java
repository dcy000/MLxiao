package com.gzq.administrator.lib_common.base;

import android.app.Application;
import android.content.Context;
import com.gzq.administrator.lib_common.utils.ScreenUtils;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by gzq on 2018/4/12.
 */

public class BaseApplication extends Application{
    private static BaseApplication mInstance;
    public String userId;
    public String userName;
    private RefWatcher refWatcher;
    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
//        //初始化内存泄漏检测工具
        refWatcher = LeakCanary.install(this);
        //吐司工具类初始化
        ToastTool.init(this);
        //常用屏幕单位转换工具类
        ScreenUtils.init(this);
        //语音模块初始化
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility.createUtility(this, builder.toString());
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

}
