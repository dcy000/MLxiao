package com.gzq.lib_core.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.gzq.lib_core.base.App;

/**
 * @author vondear
 * @date 2016/12/21
 */

public class ProcessUtils {

    /**
     * 获取当前进程名
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
