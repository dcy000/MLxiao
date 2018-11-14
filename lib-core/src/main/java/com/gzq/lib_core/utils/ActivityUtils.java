package com.gzq.lib_core.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.gzq.lib_core.base.App;

import java.util.List;
import java.util.Stack;


public class ActivityUtils {

    private static Stack<Activity> activityStack;

    /**
     * 添加Activity 到栈
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前的Activity（堆栈中最后一个压入的)
     */
    public static Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        if (activityStack!=null){
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    public static void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public static void AppExit() {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) App.getApp().getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(App.getApp().getPackageName());
            System.exit(0);
        } catch (Exception e) {

        }
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 判断是否存在指定Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isExistActivity(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param goal
     */
    public static void skipActivityAndFinishAll(Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(currentActivity(), goal);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity().startActivity(intent);
        currentActivity().finish();
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param goal
     */
    public static void skipActivityAndFinishAll(Class<?> goal) {
        Intent intent = new Intent(currentActivity(), goal);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity().startActivity(intent);
        currentActivity().finish();
    }


    /**
     * Activity 跳转
     *
     * @param goal
     */
    public static void skipActivityAndFinish(Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(currentActivity(), goal);
        intent.putExtras(bundle);
        currentActivity().startActivity(intent);
        currentActivity().finish();
    }

    /**
     * Activity 跳转
     *
     * @param goal
     */
    public static void skipActivityAndFinish(Class<?> goal) {
        Intent intent = new Intent(currentActivity(), goal);
        currentActivity().startActivity(intent);
        currentActivity().finish();
    }


    /**
     * Activity 跳转
     *
     * @param goal
     */
    public static void skipActivity(Class<?> goal) {
        Intent intent = new Intent(currentActivity(), goal);
        currentActivity().startActivity(intent);
    }

    /**
     * Activity 跳转
     *
     * @param goal
     */
    public static void skipActivity(Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(currentActivity(), goal);
        intent.putExtras(bundle);
        currentActivity().startActivity(intent);
    }

    public static void skipActivityForResult(Class<?> goal, int requestCode) {
        Intent intent = new Intent(currentActivity(), goal);
        currentActivity().startActivityForResult(intent, requestCode);
    }

    public static void skipActivityForResult(Class<?> goal, Bundle bundle, int requestCode) {
        Intent intent = new Intent(currentActivity(), goal);
        intent.putExtras(bundle);
        currentActivity().startActivityForResult(intent, requestCode);
    }

    /**
     * 获取launcher activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return info.activityInfo.name;
            }
        }
        return "no " + packageName;
    }
}
