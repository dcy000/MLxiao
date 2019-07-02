package com.gcml.common.internal;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gcml.common.R;
import com.gcml.common.api.AppLifecycleCallbacks;
import com.gcml.common.utils.AppUtils;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.google.auto.service.AutoService;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.List;

import timber.log.Timber;

@AutoService(AppLifecycleCallbacks.class)
public class QualityAppLifecycleCallbacks implements AppLifecycleCallbacks {

    private Application mApp;

    @Override
    public void attachBaseContext(Application app, Context base) {
        mApp = app;
        initTimber();
    }


    @Override
    public void onCreate(Application application) {
        CrashReport.initCrashReport(application, application.getString(R.string.app_id_bugly), isDebug());
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(application);
        userStrategy.setAppVersion(AppUtils.getAppInfo().getVersionName());
        String[] splits = AppUtils.getAppInfo().getVersionName().split("\\.");
        if (splits != null && splits.length >=1) {
            Timber.i("初始化渠道名："+splits[0]);
            userStrategy.setAppChannel(splits[0]);
        }else{
            Timber.i("初始化渠道名：失败");
        }
        Bugly.init(application, application.getString(R.string.app_id_bugly), isDebug(), userStrategy);
//        LeakCanaryHelper.INSTANCE.install(application);
//        BlockCanary.install(application, new AppBlockCanaryContext()).start();
    }

    @Override
    public void onTerminate(Application application) {

    }

    private void initTimber() {
        if (isDebug()) {
            Timber.plant(new Timber.DebugTree());
        } else {
//            Timber.plant(new Timber.DebugTree());
            Timber.plant(new CrashReportingTree());
        }
    }

    private boolean isDebug() {
        return (mApp.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

    /**
     * Not a real crash reporting library!
     */
    public static final class FakeCrashLibrary {
        public static void log(int priority, String tag, String message) {
            // TODO add log entry to circular buffer.
        }

        public static void logWarning(Throwable t) {
            // TODO report non-fatal warning.
        }

        public static void logError(Throwable t) {
            // TODO report non-fatal error.
        }

        private FakeCrashLibrary() {
            throw new AssertionError("No instances.");
        }
    }

    private static class AppBlockCanaryContext extends BlockCanaryContext {
        public AppBlockCanaryContext() {
            super();
        }

        @Override
        public Context provideContext() {
            return super.provideContext();
        }

        @Override
        public String provideQualifier() {
            //version + flavor
            return super.provideQualifier();
        }

        @Override
        public String provideUid() {
            //user id
            return super.provideUid();
        }

        @Override
        public String provideNetworkType() {
            // 2G, 3G, 4G, wifi
            return super.provideNetworkType();
        }

        @Override
        public int provideMonitorDuration() {
            // stop after duration
            return super.provideMonitorDuration();
        }

        @Override
        public int provideBlockThreshold() {
            //1000
            return 600;
//            return super.provideBlockThreshold();
        }

        @Override
        public int provideDumpInterval() {
            //1000
            return super.provideDumpInterval();
        }

        @Override
        public String providePath() {
            return super.providePath();
        }

        @Override
        public boolean displayNotification() {
            return super.displayNotification();
        }

        @Override
        public boolean zip(File[] src, File dest) {
            return super.zip(src, dest);
        }

        @Override
        public void upload(File zippedFile) {
            super.upload(zippedFile);
        }

        @Override
        public List<String> concernPackages() {
            return super.concernPackages();
        }

        @Override
        public boolean filterNonConcernStack() {
            return super.filterNonConcernStack();
        }

        @Override
        public List<String> provideWhiteList() {
            return super.provideWhiteList();
        }

        @Override
        public boolean deleteFilesInWhiteList() {
            return super.deleteFilesInWhiteList();
        }

        @Override
        public void onBlock(Context context, BlockInfo blockInfo) {
            super.onBlock(context, blockInfo);
        }

        @Override
        public boolean stopWhenDebugging() {
            return super.stopWhenDebugging();
        }
    }
}
