package com.gzq.lib_core.base.quality;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.utils.NetworkUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.2.5
 * created on 2018/10/16 11:59
 * created by: gzq
 * description: TODO
 */
public class QualityBlockCanary implements AppLifecycle{
    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        BlockCanary.install(application,new MyConfigBlockCanary(application)).start();
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public IEvents provideEvents() {
        return null;
    }

    static class MyConfigBlockCanary extends BlockCanaryContext {
        private Application application;
        private static final String TAG = "MyConfigBlockCanary";
        public MyConfigBlockCanary(Application application) {
            this.application=application;
        }

        /**
         * Implement in your project.
         *
         * @return Qualifier which can specify this installation, like version + flavor.
         */
        @Override
        public String provideQualifier() {
            String qualifier = "";
            try {
                PackageInfo info = application.getPackageManager()
                        .getPackageInfo(application.getPackageName(), 0);
                qualifier += info.versionCode + "_" + info.versionName + "_YYB";
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "provideQualifier exception", e);
            }
            return qualifier;
        }

        /**
         * Implement in your project.
         *
         * @return user id
         */
        @Override
        public String provideUid() {
            return super.provideUid();
        }

        /**
         * Network type
         *
         * @return {@link String} like 2G, 3G, 4G, wifi, etc.
         */
        @Override
        public String provideNetworkType() {
            return NetworkUtils.getNetWorkTypeName();
        }

        /**
         * Config monitor duration, after this time BlockCanary will stop, use
         * with {@code BlockCanary}'s isMonitorDurationEnd
         *
         * @return monitor last duration (in hour)
         */
        @Override
        public int provideMonitorDuration() {
            return super.provideMonitorDuration();
        }

        /**
         * Config block threshold (in millis), dispatch over this duration is regarded as a BLOCK. You may set it
         * from performance of device.
         *
         * @return threshold in mills
         */
        @Override
        public int provideBlockThreshold() {
            return 600;
        }

        /**
         * Thread stack dump interval, use when block happens, BlockCanary will dump on main thread
         * stack according to current sample cycle.
         * <p>
         * Because the implementation mechanism of Looper, real dump interval would be longer than
         * the period specified here (especially when cpu is busier).
         * </p>
         *
         * @return dump interval (in millis)
         */
        @Override
        public int provideDumpInterval() {
            return provideBlockThreshold();
        }

        /**
         * Path to save log, like "/blockcanary/", will save to sdcard if can.
         *
         * @return path of log files
         */
        @Override
        public String providePath() {
            return "/blockcanary/";
        }

        /**
         * If need notification to notice block.
         *
         * @return true if need, else if not need.
         */
        @Override
        public boolean displayNotification() {
            return true;
        }

        /**
         * Implement in your project, bundle files into a zip file.
         *
         * @param src  files before compress
         * @param dest files compressed
         * @return true if compression is successful
         */
        @Override
        public boolean zip(File[] src, File dest) {
            return false;
        }

        /**
         * Implement in your project, bundled log files.
         *
         * @param zippedFile zipped file
         */
        @Override
        public void upload(File zippedFile) {
            throw new UnsupportedOperationException();
        }


        /**
         * Packages that developer concern, by default it uses process name,
         * put high priority one in pre-order.
         *
         * @return null if simply concern only package with process name.
         */
        @Override
        public List<String> concernPackages() {
            return super.concernPackages();
        }

        /**
         * Filter stack without any in concern package, used with @{code concernPackages}.
         *
         * @return true if filter, false it not.
         */
        @Override
        public boolean filterNonConcernStack() {
            return super.filterNonConcernStack();
        }

        /**
         * Provide white list, entry in white list will not be shown in ui list.
         *
         * @return return null if you don't need white-list filter.
         */
        @Override
        public List<String> provideWhiteList() {
            LinkedList<String> whiteList = new LinkedList<>();
            whiteList.add("org.chromium");
            return whiteList;
        }

        /**
         * Whether to delete files whose stack is in white list, used with white-list.
         *
         * @return true if delete, false it not.
         */
        @Override
        public boolean deleteFilesInWhiteList() {
            return super.deleteFilesInWhiteList();
        }

        /**
         * Block interceptor, developer may provide their own actions.
         */
        @Override
        public void onBlock(Context context, BlockInfo blockInfo) {

        }
    }
}
