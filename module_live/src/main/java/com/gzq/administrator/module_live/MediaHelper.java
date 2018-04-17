package com.gzq.administrator.module_live;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * Created by gzq on 2018/3/16.
 */

public class MediaHelper {
    public static double getCurrentSpan(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    public static float getFocusX(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float xPoint1 = event.getX(1);
        return (xPoint0 + xPoint1) / 2;
    }

    public static float getFocusY(MotionEvent event) {
        float yPoint0 = event.getY(0);
        float yPoint1 = event.getY(1);
        return (yPoint0 + yPoint1) / 2;
    }
    public static void showStatusBar(Activity activity) {
        WeakReference<Activity> weakReference=new WeakReference<Activity>(activity);
        WindowManager.LayoutParams params = weakReference.get().getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        weakReference.get().getWindow().setAttributes(params);
        weakReference.get().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void hideStatusBar(Activity activity) {
        WeakReference<Activity> weakReference=new WeakReference<Activity>(activity);
        WindowManager.LayoutParams params = weakReference.get().getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        weakReference.get().getWindow().setAttributes(params);
        weakReference.get().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
