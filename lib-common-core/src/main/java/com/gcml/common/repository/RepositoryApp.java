package com.gcml.common.repository;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gcml.common.BuildConfig;
import com.gcml.common.repository.cache.Cache;
import com.gcml.common.repository.cache.LruCache;
import com.gcml.common.repository.di.ClientModule;
import com.gcml.common.repository.di.DaggerRepositoryComponent;
import com.gcml.common.repository.di.RepositoryComponent;
import com.gcml.common.repository.di.RepositoryConfigModule;
import com.gcml.common.repository.di.RepositoryModule;
import com.gcml.common.repository.http.HttpLogInterceptor;
import com.gcml.common.repository.utils.Preconditions;
import com.gcml.common.utils.ManifestParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
//import tech.linjiang.pandora.Pandora;

public enum RepositoryApp implements IRepositoryApp {
    INSTANCE;

    private Application mApplication;
    private RepositoryComponent mRepositoryComponent;
    private RepositoryModule mRepositoryModule;

    private static final String REPOSITORY_CONFIGURATION = "RepositoryConfig";
    private ManifestParser<RepositoryConfigModule.Configuration> mConfigurationManifestParser;
    private final List<RepositoryConfigModule.Configuration> mConfigurations = new ArrayList<>();

    RepositoryApp() {
        mConfigurations.add(new Configuration());
    }

    public Application app() {
        return mApplication;
    }

    public void attachBaseContext(Application application, Context base) {

    }

    public void onCreate(Application application) {
        Stetho.initializeWithDefaults(application);
        this.mApplication = application;
        if (mConfigurationManifestParser == null) {
            mConfigurationManifestParser = new ManifestParser<>(mApplication, REPOSITORY_CONFIGURATION);
            List<RepositoryConfigModule.Configuration> configurations = mConfigurationManifestParser.parse();
            this.mConfigurations.addAll(configurations);
        }

        if (mRepositoryModule == null) {
            mRepositoryModule = new RepositoryModule(mApplication);
        }
        mRepositoryComponent = DaggerRepositoryComponent.builder()
                .repositoryModule(mRepositoryModule)
                .clientModule(new ClientModule(mApplication))
                .repositoryConfigModule(getRepositoryConfigModule(mApplication, mConfigurations))
                .build();
        mRepositoryComponent.inject(this);
    }

    private RepositoryConfigModule getRepositoryConfigModule(Context context, List<RepositoryConfigModule.Configuration> configurations) {
        RepositoryConfigModule.Builder builder = RepositoryConfigModule.builder();
        for (RepositoryConfigModule.Configuration configuration : configurations) {
            configuration.configRepository(context, builder);
        }
        return builder.application(mApplication).build();
    }

    public void onTerminate(Application application) {
        this.mRepositoryModule = null;
        this.mRepositoryComponent = null;
        this.mApplication = null;
    }

    @Override
    public RepositoryComponent repositoryComponent() {
        Preconditions.checkNotNull(mRepositoryComponent,
                "%s cannot be null,first call %s#onCreate(Application) in %s#onCreate()",
                RepositoryComponent.class.getName(), getClass().getName(), mApplication.getClass().getName());
        return this.mRepositoryComponent;
    }

    @Override
    public RepositoryModule repositoryModule() {
        Preconditions.checkNotNull(mRepositoryComponent,
                "%s cannot be null,first call %s#onCreate(Application) in %s#onCreate()",
                RepositoryModule.class.getName(), getClass().getName(), mApplication.getClass().getName());
        return this.mRepositoryModule;
    }

    public static class Configuration implements RepositoryConfigModule.Configuration {

        @Override
        public void configRepository(Context context, RepositoryConfigModule.Builder builder) {
            //Release 时,让框架不再打印 Http 请求和响应的信息
            boolean debug = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (!debug) {
                builder.printHttpLogLevel(HttpLogInterceptor.Level.NONE);
            }

            RetrofitUrlManager.getInstance().putDomain("seniverse", BuildConfig.API_SENIVERSE);

            String baseUrl = BuildConfig.SERVER_ADDRESS;
            builder.baseUrl(baseUrl)
                    // 这里提供一个全局处理 Http 请求和响应结果的处理类,可以比客户端提前一步拿到服务器返回的结果
                    .httpInterceptor(new HttpInterceptorImpl())
                    .gsonConfiguration((context1, gsonBuilder) -> {
                        gsonBuilder
//                                .serializeNulls()//支持序列化null的参数
                                .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                    })
                    //这里可以自己自定义配置Retrofit的参数,甚至你可以替换系统配置好的okhttp对象
                    .retrofitConfiguration((context1, retrofitBuilder) -> {
                     /*比如使用fastjson替代gson
                     retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());*/
                    /*适配 LiveData
                    retrofitBuilder.addCallAdapterFactory(new LiveDataCallAdapterFactory());*/
                    })
                    //这里可以自己自定义配置Okhttp的参数
                    .okhttpConfiguration((context1, okHttpBuilder) -> {
                        //支持 Https
                        //okHttpBuilder.sslSocketFactory()
                        RetrofitUrlManager.getInstance().with(okHttpBuilder);
                        okHttpBuilder
                                .addNetworkInterceptor(new StethoInterceptor())
                                .writeTimeout(10, TimeUnit.SECONDS);
//                                .addInterceptor(Pandora.get().getInterceptor());
                    })
                    //这里可以自己自定义配置 RxCache 的参数
                    .rxCacheConfiguration((context1, rxCacheBuilder) -> {
                        rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                        return null;
                    })
                    //这里可以自定义配置 RoomDatabase，比如数据库迁移升级
                    .roomConfiguration((context1, dbName, roomBuilder) -> {
                        if ("com.gcml.auth.model.UserDb".equals(dbName)) {
                            roomBuilder.addMigrations(new Migration(1, 2) {
                                @Override
                                public void migrate(@NonNull SupportSQLiteDatabase database) {
                                    database.execSQL("ALTER TABLE UserOld ADD COLUMN waist TEXT");
                                }
                            });
                        }
                    })
                    //可根据当前项目的情况以及环境为框架某些部件提供自定义的缓存策略,具有强大的扩展性
                    .cacheFactory(type -> {
                        switch (type) {
                            case EXTRAS_CACHE_TYPE:
                                //外部 extras 默认最多只能缓存500个内容
                                return new LruCache(500);
                        /*case CUSTOM_CACHE_TYPE:
                            return new CustomCache();//自定义 Cache*/
                            default:
                                //RepositoryManager 中的容器默认缓存 100 个内容
                                return new LruCache(Cache.Factory.DEFAULT_CACHE_SIZE);
                        }
                    });
        }
    }

    public static class HttpInterceptorImpl implements ClientModule.HttpInterceptor {

        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            return request;
        }

        @Override
        public Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response response) {
            return response;
        }
    }
}
