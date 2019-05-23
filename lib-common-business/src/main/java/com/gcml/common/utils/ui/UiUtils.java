package com.gcml.common.utils.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by zqs on 2017/10/19.
 */

public class UiUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    private static float sDesignWidth = 1920;

    private static float sDesignHeight = 1200;

    public static void init(Context context, float designWidth, float designHeight) {
        sContext = context.getApplicationContext();
        sDesignWidth = designWidth;
        sDesignHeight = designHeight;
        compat(context, designWidth);
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                UiUtils.compat(activity, designWidth);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    private static void compat(Context context, float designWidth) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getSize(size);
            context.getResources().getDisplayMetrics().xdpi = 72.0f * size.x / designWidth;
        }
    }

    public static void compatWithOrientation(Configuration config) {
        //设计图如果是横着的按宽度算，如果设计图是竖着的按高度算
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            compat(sContext, sDesignWidth);
            return;
        }
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            compat(sContext, sDesignWidth);
        }
    }

    public static int pt(float pt) {
        float xdpi = sContext.getResources().getDisplayMetrics().xdpi;
        return (int) (pt * xdpi / 72f);
    }
}
