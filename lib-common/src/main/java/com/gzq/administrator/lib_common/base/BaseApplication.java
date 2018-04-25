package com.gzq.administrator.lib_common.base;

import android.app.Application;
import android.content.res.Configuration;

import com.gzq.administrator.lib_common.utils.ScreenUtils;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.gzq.administrator.lib_common.utils.UiUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lzy.okgo.OkGo;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by gzq on 2018/4/12.
 */

public class BaseApplication extends Application{
    private static BaseApplication mInstance;
    private static OkGo okGoInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        //初始化内存泄漏检测工具
        LeakCanary.install(this);
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
        //初始化网络请求框架
        okGoInstance=OkGo.getInstance().init(this);
        //初始化px转pt工具
        UiUtils.init(this,1920,1200);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UiUtils.compatWithOrientation(newConfig);
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * 提供此方法的作用是方便在二次封装的时候进行相关参数的设置，比如是否缓存，缓存时间，请求头
     * @return
     */
    public static OkGo getOkgoInstance(){
        return okGoInstance;
    }
}
