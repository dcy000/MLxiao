package com.example.han.referralproject.application;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.han.referralproject.BuildConfig;
import com.gzq.lib_core.base.GlobalConfig;
import com.gzq.lib_core.base.config.CrashManagerConfig;
import com.gzq.lib_core.base.config.OkhttpConfig;
import com.gzq.lib_core.base.config.RetrofitConfig;
import com.gzq.lib_core.base.config.RoomDatabaseConfig;
import com.gzq.lib_core.base.config.SessionManagerConfig;
import com.gzq.lib_core.base.delegate.GlobalModule;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.crash.CaocConfig;
import com.gzq.lib_core.http.interceptor.CacheInterceptor;
import com.gzq.lib_core.session.MmkvSessionManager;
import com.gzq.lib_core.session.SessionConfig;
import com.gzq.lib_core.session.SessionToken;
import com.gzq.lib_core.utils.DeviceUtils;
import com.gzq.lib_core.utils.ProcessUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import tech.linjiang.pandora.Pandora;
import timber.log.Timber;

public class GlobalConfiguration implements GlobalModule {
    private static final String TAG = "GlobalConfiguration";

    @Override
    public void applyOptions(Context context, GlobalConfig.Builder builder) {
        Timber.i("GlobalConfiguration---->applyOptions");
        builder
                .baseurl(BuildConfig.SERVER_ADDRESS)
                .roomName("ABC")
                .designWidth(1200)
                .designHeight(1920)
                .okhttpConfiguration(new OkhttpConfig() {
                    @Override
                    public void okhttp(Context context, OkHttpClient.Builder builder) {

                        builder.cache(new Cache(new File(context.getCacheDir().getAbsolutePath(), "gcml"), 10 * 1024 * 1024))
                                .addInterceptor(new CacheInterceptor())
                                //添加统一请求头
                                .addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request request = chain.request()
                                                .newBuilder()
                                                .addHeader("equipmentId", DeviceUtils.getIMEI())
                                                .build();
                                        return chain.proceed(request);
                                    }
                                });

                        if (context.getPackageName().equals(ProcessUtils.getCurProcessName(context))) {
                            builder.addInterceptor(Pandora.get().getInterceptor());

                        }
                    }
                })
                .sessionManagerConfiguration(new SessionManagerConfig() {
                    @Override
                    public void session(Context context, SessionConfig.Builder builder) {
                        builder.userClass(UserInfoBean.class).tokenClass(SessionToken.class);
                        builder.sessionManager(new MmkvSessionManager(context));
                    }
                })
                .roomDatabaseConfiguration(new RoomDatabaseConfig() {
                    @Override
                    public void room(Context context, RoomDatabase.Builder builder) {

                    }
                })
                .retrofitConfiguration(new RetrofitConfig() {
                    @Override
                    public void retrofit(Context context, Retrofit.Builder builder) {
                        Timber.i("retrofitConfiguration");
                    }
                })
                .crashManagerConfiguration(new CrashManagerConfig() {
                    @Override
                    public void crash(Context context, CaocConfig.Builder builder) {

                        //关闭崩溃全局监听
                        builder.enabled(BuildConfig.DEBUG);
                    }
                });

    }
}
