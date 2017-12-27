package com.medlink.crash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by afirez on 2017/12/26.
 */

public class NoCrash implements CrashHelper.OnCrashListener {

    private static final String TAG = "NoCrash";

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static NoCrash getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static NoCrash sInstance = new NoCrash();
    }

    private NoCrash () {

    }

    @Override
    public void onCrash(Thread thread, Throwable throwable) {
        try {
            dumpCrashToSdcard(throwable);
            uploadCrashToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throwable.printStackTrace();

        Thread.UncaughtExceptionHandler handler = CrashHelper.getDefaultUncaughtExceptionHandler();
        if (handler != null) {
            handler.uncaughtException(thread, throwable);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/crash/";
    private static final String PREFIX = "crash";
    private static final String SUFFIX = ".trace";

    private void dumpCrashToSdcard(Throwable throwable) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.w(TAG, "Sdcard unmounted, skip dump crash");
            return;
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long millis = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                .format(new Date(millis));
        File file = new File(PATH + PREFIX + time + SUFFIX);

        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.println(time);
            dumpPhoneInfo(writer);
            writer.println();
            throwable.printStackTrace(writer);
            writer.close();
        } catch (Exception e) {
            Log.e(TAG, "Dump crash to sdcard failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter writer) throws PackageManager.NameNotFoundException {
        PackageManager pm = sContext.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(sContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        writer.print("app_version: ");
        writer.print(packageInfo.versionName);
        writer.print("_");
        writer.println(packageInfo.versionCode);

        writer.print("os_version: ");
        writer.print(Build.VERSION.RELEASE);
        writer.print("_");
        writer.println(Build.VERSION.SDK_INT);

        writer.print("vendor: ");
        writer.println(Build.MANUFACTURER);

        writer.print("model: ");
        writer.println(Build.MODEL);

        writer.print("cpu_abi: ");
        writer.println(Build.CPU_ABI);
    }

    private void uploadCrashToServer() {
        // TODO: 2017/12/26  upload crash info to your web server
    }

    public void install() {
        CrashHelper.install(this);
    }

    public void uninstall() {
        CrashHelper.uninstall();
    }
}
