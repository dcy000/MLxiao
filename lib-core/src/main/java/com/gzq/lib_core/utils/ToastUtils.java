package com.gzq.lib_core.utils;

import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.TextView;
import android.widget.Toast;

import com.gzq.lib_core.base.Box;

/**
 * Created by gzq on 2018/3/9.
 */

public class ToastUtils {
    private static Toast mToast = null;//全局唯一的Toast

    private ToastUtils() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(final CharSequence message) {
        new WeakHandler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (mToast == null) {
                            mToast = Toast.makeText(Box.getApp(), message, Toast.LENGTH_SHORT);
                            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
                            v.setTextSize(28);
                        } else {
                            mToast.setText(message);
                        }
                        mToast.show();
                    }
                });
    }

    /**
     * 短时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(@StringRes final int resId) {
        new WeakHandler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (mToast == null) {
                            mToast = Toast.makeText(Box.getApp(), resId, Toast.LENGTH_SHORT);
                            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
                            v.setTextSize(28);
                        } else {
                            mToast.setText(resId);
                        }
                        mToast.show();
                    }
                });

    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(final CharSequence message) {
        new WeakHandler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (mToast == null) {
                            mToast = Toast.makeText(Box.getApp(), message, Toast.LENGTH_LONG);
                            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
                            v.setTextSize(28);
                        } else {
                            mToast.setText(message);
                        }
                        mToast.show();
                    }
                });
    }

    /**
     * 长时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(@StringRes final int resId) {
        new WeakHandler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (mToast == null) {
                            mToast = Toast.makeText(Box.getApp(), resId, Toast.LENGTH_LONG);
                            TextView v = (TextView) mToast.getView().findViewById(android.R.id.message);
                            v.setTextSize(28);
                        } else {
                            mToast.setText(resId);
                        }
                        mToast.show();
                    }
                });
    }
}
