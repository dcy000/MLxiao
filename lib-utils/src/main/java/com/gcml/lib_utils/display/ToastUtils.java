package com.gcml.lib_utils.display;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.gcml.lib_utils.UtilsManager;

/**
 * Created by gzq on 2018/3/9.
 */

public class ToastUtils {
    private static Toast mToast = null;//全局唯一的Toast

    private static Application getApplication(){
        return UtilsManager.getApplication();
    }

    private ToastUtils() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
            mToast.setGravity(Gravity.BOTTOM,0,0);
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
            mToast = Toast.makeText(getApplication(), resId, Toast.LENGTH_SHORT);
            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
            mToast.setGravity(Gravity.BOTTOM,0,0);
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
            mToast = Toast.makeText(getApplication(), message, Toast.LENGTH_LONG);
            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
            mToast.setGravity(Gravity.BOTTOM,0,0);
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
            mToast = Toast.makeText(getApplication(), resId, Toast.LENGTH_LONG);
            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
            mToast.setGravity(Gravity.BOTTOM,0,0);
            v.setTextSize(28);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShortOnTop(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
            v.setTextSize(28);
            mToast.setGravity(Gravity.TOP,0,0);
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
    public static void showShortOnTop( int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplication(), resId, Toast.LENGTH_SHORT);
            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
            v.setTextSize(28);
            mToast.setGravity(Gravity.TOP,0,0);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }
}
