package com.gzq.lib_core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.WindowManager;

import timber.log.Timber;

/**
 * Created by gzq on 2017/10/19.
 */

public class UiUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    private static float sDesignWidth = 720;

    private static float sDesignHeight = 1080;

    public static void init(Context context, float designWidth, float designHeight) {
        sContext = context.getApplicationContext();
        sDesignWidth = designWidth;
        sDesignHeight = designHeight;
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            compat(context, designWidth);
        }else if (orientation==Configuration.ORIENTATION_LANDSCAPE){
            compat(context,designHeight);
        }
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
            Timber.tag("ORIENTATION_LANDSCAPE").i(sDesignHeight+"");
            compat(sContext, sDesignHeight);
            return;
        }
        //设计图如果是横着的按高度算，如果设计图是竖着的按宽度算
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Timber.tag("ORIENTATION_PORTRAIT").i(sDesignWidth+"");
            compat(sContext, sDesignWidth);
        }
    }

    public static int pt(float pt) {
        float xdpi = sContext.getResources().getDisplayMetrics().xdpi;
        return (int) (pt * xdpi / 72f);
    }
}
