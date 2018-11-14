package com.gzq.lib_core.base;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzq.lib_core.BuildConfig;
import com.gzq.lib_core.R;
import com.gzq.lib_core.base.config.CrashManagerConfig;
import com.gzq.lib_core.base.config.GsonConfig;
import com.gzq.lib_core.base.config.OkhttpConfig;
import com.gzq.lib_core.base.config.RetrofitConfig;
import com.gzq.lib_core.base.config.RoomDatabaseConfig;
import com.gzq.lib_core.base.config.SessionManagerConfig;
import com.gzq.lib_core.constant.Constants;
import com.gzq.lib_core.crash.CaocConfig;
import com.gzq.lib_core.http.interceptor.CacheInterceptor;
import com.gzq.lib_core.http.interceptor.Level;
import com.gzq.lib_core.http.interceptor.LoggingInterceptor;
import com.gzq.lib_core.session.MmkvSessionManager;
import com.gzq.lib_core.session.SessionConfig;
import com.gzq.lib_core.session.SessionManager;
import com.gzq.lib_core.utils.UiUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

final class ObjectFactory {
    /**
     * 默认超时时间
     */
    private static final int DEFAULT_TIMEOUT = 10;
    private static GsonBuilder gsonBuilder = new GsonBuilder();
    private static OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    private static Map<String, RoomDatabase.Builder> roomBuilders;
    private static SessionConfig.Builder sessionBuilder = new SessionConfig.Builder();
    private static CaocConfig.Builder crashBuilder = CaocConfig.Builder.create();


    public static Gson getGson(Context context, GlobalConfig globalConfig) {
        GsonConfig gsonConfig = globalConfig.getGsonConfig();
        if (gsonConfig != null) {
            gsonConfig.gson(context, gsonBuilder);
        }
        return gsonBuilder.create();
    }

    /**
     * @param context
     * @param globalConfig
     * @return
     */
    public static OkHttpClient getOkHttpClient(Context context, GlobalConfig globalConfig) {
        okhttpBuilder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        okhttpBuilder.addInterceptor(getLoggingInterceptor());
        OkhttpConfig okhttpConfig = globalConfig.getOkhttpConfig();
        if (okhttpConfig != null) {
            okhttpConfig.okhttp(context, okhttpBuilder);
        }
        //添加动态变更BaseUrl的能力
        RetrofitUrlManager.getInstance().with(okhttpBuilder).build();
        return okhttpBuilder.build();
    }


    /**
     * @param context
     * @param globalConfig
     * @return {@see https://blog.csdn.net/K_Hello/article/details/81318856}
     */
    public static Retrofit getRetrofit(Context context, GlobalConfig globalConfig) {
        String baseUrl = globalConfig.getBaseUrl();
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("baseUrl is null");
        }
        retrofitBuilder.baseUrl(baseUrl);
        OkHttpClient okHttpClient = getOkHttpClient(context, globalConfig);
        retrofitBuilder.client(okHttpClient);
        RetrofitConfig retrofitConfig = globalConfig.getRetrofitConfig();
        if (retrofitConfig != null) {
            retrofitConfig.retrofit(context, retrofitBuilder);
        }
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());

        return retrofitBuilder.build();
    }

    /**
     * 获取Room数据库实例
     *
     * @param context
     * @param clzz
     * @param globalConfig
     * @param <DB>
     * @return
     */
    public static <DB extends RoomDatabase> DB getRoomDatabase(Context context, Class clzz, GlobalConfig globalConfig) {
        RoomDatabaseConfig roomDatabaseConfig = globalConfig.getRoomDatabaseConfig();
        String roomName = globalConfig.getRoomName();
        if (TextUtils.isEmpty(roomName)) {
            roomName = Constants.NAME_ROOM_DATABASE;
        }
        if (roomBuilders == null) {
            roomBuilders = new HashMap<>();
        }
        String keyRoomBuilder = clzz.getName() + "_" + roomName;
        if (roomBuilders.containsKey(keyRoomBuilder)) {
            return (DB) roomBuilders.get(keyRoomBuilder).build();
        }
        RoomDatabase.Builder roomBuilder = Room.databaseBuilder(context, clzz, roomName);
        if (roomBuilder == null) {
            throw new NullPointerException("create room fail");
        }
        roomBuilders.put(keyRoomBuilder, roomBuilder);

        if (roomDatabaseConfig != null) {
            roomDatabaseConfig.room(context, roomBuilder);
        }
        return (DB) roomBuilder.build();
    }

    public static void initSessionManager(Context context, GlobalConfig globalConfig) {
        sessionBuilder.withContext(context)
                //默认使用腾讯的MMKV作为存储用户信息的工具
                .sessionManager(new MmkvSessionManager(context));
        SessionManagerConfig sessionManagerConfig = globalConfig.getSessionManagerConfig();
        if (sessionManagerConfig != null) {
            sessionManagerConfig.session(context, sessionBuilder);
        }
        SessionManager.init(sessionBuilder.build());
    }

    public static void initCrashManager(Context context, GlobalConfig globalConfig) {
        crashBuilder
                //背景模式,开启沉浸式
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
                //是否启动全局异常捕获
                .enabled(true)
                //是否显示错误详细信息
                .showErrorDetails(true)
                //是否显示重启按钮
                .showRestartButton(true)
                //是否跟踪Activity
                .trackActivities(true)
                //崩溃的间隔时间(毫秒)
                .minTimeBetweenCrashesMs(2000)
                //错误图标
                .errorDrawable(R.drawable.ic_error);
        CrashManagerConfig crashManagerConfig = globalConfig.getCrashManagerConfig();
        if (crashManagerConfig != null) {
            crashManagerConfig.crash(context, crashBuilder);
        }
        crashBuilder.apply();
    }

    public static void initUiUtils(GlobalConfig globalConfig) {
        int designWidth = globalConfig.getDesignWidth();
        int designHeight = globalConfig.getDesignHeight();
        if (designWidth > 0 && designHeight > 0) {
            UiUtils.init(App.getApp(), designWidth, designHeight);
        } else {
            UiUtils.init(App.getApp(), 720, 1280);
        }
    }

    public static LoggingInterceptor getLoggingInterceptor() {
        return new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.WARN)
                .request("Request")
                .response("Response")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .enableAndroidStudio_v3_LogsHack(true)
                .build();
    }

    public static void clear() {
        gsonBuilder = null;
        okhttpBuilder = null;
        retrofitBuilder = null;
        if (roomBuilders != null) {
            roomBuilders.clear();
            roomBuilders = null;
        }
    }

}
