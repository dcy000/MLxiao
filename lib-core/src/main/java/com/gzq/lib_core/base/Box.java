package com.gzq.lib_core.base;

import android.app.Application;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.quality.LeakCanaryUtil;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.session.SessionManager;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.UiUtils;

import java.util.Objects;

import retrofit2.Retrofit;
import timber.log.Timber;

public class Box implements AppLifecycle {
    private static final String TAG = "Box";
    private static Application mApplication;
    private static Gson gson;
    private static Retrofit retrofit;

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //初始化全局变量
        mApplication = application;
        gson = ObjectFactory.getGson(mApplication, App.getClobalConfig());
        retrofit = ObjectFactory.getRetrofit(mApplication, App.getClobalConfig());
        //初始化Ui工具
        ObjectFactory.initUiUtils(App.getClobalConfig());
        //初始化LeakCanary
        LeakCanaryUtil.getInstance().init(application);
        //初始化KVUtil
        KVUtils.init(application);
        //用户信息管理器
        ObjectFactory.initSessionManager(mApplication, App.getClobalConfig());
        //崩溃拦截配置
        ObjectFactory.initCrashManager(mApplication, App.getClobalConfig());
        Timber.tag(TAG).i("onCreate");
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        ObjectFactory.clear();
        mApplication = null;
        gson = null;
        retrofit = null;
        Timber.tag(TAG).i("onTerminate");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        UiUtils.compatWithOrientation(newConfig);
    }

    @Override
    public IEvents provideEvents() {
        return null;
    }


    public static Application getApp() {
        return mApplication;
    }

    /**
     * 全局基础Url
     *
     * @return
     */
    public static String getBaseUrl() {
        return App.getClobalConfig().getBaseUrl();
    }

    /**
     * Gson
     *
     * @return
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * @param serviceClazz
     * @param <T>
     * @return
     */
    public static <T> T getRetrofit(Class<T> serviceClazz) {
        return retrofit.create(serviceClazz);
    }

    /**
     * 数据库Room
     *
     * @param databaseClazz
     * @param <DB>
     * @return
     */
    public static <DB extends RoomDatabase> DB getRoomDataBase(Class databaseClazz) {
        return ObjectFactory.getRoomDatabase(mApplication, databaseClazz, App.getClobalConfig());
    }

    /**
     * 用户信息管理器
     *
     * @return
     */
    public static SessionManager getSessionManager() {
        return SessionManager.get();
    }

    /**
     * 因为全局太多地方使用userId,所以在此处提供；
     * 如果要保证lib-core的纯粹性，请不要在此处这么做
     *
     * @return
     */
    public static String getUserId() {
        UserInfoBean user = getSessionManager().getUser();
        if (user == null) {
            return null;
        }
        return user.bid;
    }

    /**
     * 为了解决在fragment中使用{@link android.support.v4.app.Fragment#getString(int)}
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     *
     * @param id 字符串资源id
     * @return
     */
    public static String getString(@StringRes int id) {
        return getApp().getResources().getString(id);
    }

    public static String[] getStrings(@ArrayRes int id) {
        return getApp().getResources().getStringArray(id);
    }

    /**
     * 为了解决在fragment中使用{@link Fragment#getContext()} getContext().getColor(int color)
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     *
     * @param id 颜色资源id
     * @return
     */
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getApp(), id);
    }
}
