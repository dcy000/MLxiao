package com.gcml.common.utils.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
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
