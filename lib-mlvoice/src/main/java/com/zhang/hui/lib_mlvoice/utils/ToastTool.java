package com.zhang.hui.lib_mlvoice.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.hui.lib_mlvoice.VoiceApp;

/**
 * Created by gzq on 2018/3/9.
 */

public class ToastTool {
    private static Context contextApplication;
    private static Toast mToast = null;//全局唯一的Toast

    public static void init(Context context) {
        contextApplication = context.getApplicationContext();
    }

    private ToastTool() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(VoiceApp.app.getApplicationContext(), message, Toast.LENGTH_SHORT);
            TextView v = mToast.getView().findViewById(android.R.id.message);
            v.setTextSize(28);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param resId   资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort( int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(contextApplication, resId, Toast.LENGTH_SHORT);
            TextView v = mToast.getView().findViewById(android.R.id.message);
            v.setTextSize(28);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong( CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(contextApplication, message, Toast.LENGTH_LONG);
            TextView v = mToast.getView().findViewById(android.R.id.message);
            v.setTextSize(28);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param resId   资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(contextApplication, resId, Toast.LENGTH_LONG);
            TextView v = mToast.getView().findViewById(android.R.id.message);
            v.setTextSize(28);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }
}
