package com.gzq.administrator.lib_common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainLooperMonitor {
    private static final String TAG = "MainLooperMonitor";

    private static final String MSG_START = ">>>>> Dispatching";
    private static final String MSG_END = "<<<<< Finished";
    private static final int TIME = 200;
    private Handler mWorkerHandler;
    private Runnable mTaskRunnable;

    private Context context;

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final MainLooperMonitor INSTANCE = new MainLooperMonitor();
    }

    public static MainLooperMonitor getInstance() {
        return Holder.INSTANCE;
    }

    private MainLooperMonitor() {
        context = ContextFactory.getContext();
        HandlerThread workerThread = new HandlerThread("LooperMonitor");
        workerThread.start();
        mWorkerHandler = new Handler(workerThread.getLooper());
        mTaskRunnable = new TaskRunnable();
    }

    public void install() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                if (x.startsWith(MSG_START)) {
                    mWorkerHandler.postDelayed(mTaskRunnable, TIME);
                } else if (x.startsWith(MSG_END)) {
                    mWorkerHandler.removeCallbacks(mTaskRunnable);
                }
            }
        });
    }

    private class TaskRunnable implements Runnable {

        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement ste : stackTrace) {
                sb.append(ste).append("\n");
            }
            Log.e(TAG, sb.toString());
            dumpCrashToSdcard(sb.toString());
        }
    }

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + File.separator +"crash" + File.separator;
    private static final String PREFIX = "crash-";
    private static final String SUFFIX = ".trace";

    private void dumpCrashToSdcard(String info) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.w(TAG, "Sdcard unmounted, skip dump crash");
            return;
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long millis = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                .format(new Date(millis));
        File file = new File(PATH + "ui-" + time + SUFFIX);

        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.println(time);
            dumpPhoneInfo(writer);
            writer.println();
            writer.println(info);
            writer.close();
            Log.e(TAG, "Dump crash to sdcard success");
        } catch (Exception e) {
            Log.e(TAG, "Dump crash to sdcard failed", e);
        }
    }

    private void dumpCrashToSdcard(Throwable throwable) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.w(TAG, "Sdcard unmounted, skip dump crash");
            return;
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long millis = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                .format(new Date(millis));
        File file = new File(PATH + PREFIX + time + SUFFIX);

        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.println(time);
            dumpPhoneInfo(writer);
            writer.println();
            throwable.printStackTrace(writer);
            writer.close();
            Log.e(TAG, "Dump crash to sdcard success");
        } catch (Exception e) {
            Log.e(TAG, "Dump crash to sdcard failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter writer) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),
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
}
