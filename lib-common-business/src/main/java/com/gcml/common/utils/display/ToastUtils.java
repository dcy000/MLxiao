package com.gcml.common.utils.display;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.gcml.common.utils.UtilsManager;


/**
 * Created by gzq on 2018/3/9.
 */

public class ToastUtils {
    private static Toast mToast = null;//全局唯一的Toast

    private static Application getApplication() {
        return UtilsManager.getApplication();
    }

    private ToastUtils() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(final CharSequence message) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
                    TextView v = mToast.getView().findViewById(android.R.id.message);
                    v.setTextSize(28);
                } else {
                    mToast.setText(message);
                }
                mToast.setGravity(Gravity.BOTTOM, 0, 50);
                mToast.show();
            }
        });
    }

    /**
     * 短时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplication(), resId, Toast.LENGTH_SHORT);
                    TextView v = mToast.getView().findViewById(android.R.id.message);
                    v.setTextSize(28);
                } else {
                    mToast.setText(resId);
                }
                mToast.setGravity(Gravity.BOTTOM, 0, 50);
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
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplication(), message, Toast.LENGTH_LONG);
                    TextView v = mToast.getView().findViewById(android.R.id.message);

                    v.setTextSize(28);
                } else {
                    mToast.setText(message);
                }
                mToast.setGravity(Gravity.BOTTOM, 0, 50);
                mToast.show();
            }
        });
    }

    /**
     * 长时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplication(), resId, Toast.LENGTH_LONG);
                    TextView v = mToast.getView().findViewById(android.R.id.message);
                    v.setTextSize(28);
                } else {
                    mToast.setText(resId);
                }
                mToast.setGravity(Gravity.BOTTOM, 0, 50);
                mToast.show();
            }
        });
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShortOnTop(final CharSequence message) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
                    TextView v = mToast.getView().findViewById(android.R.id.message);
                    v.setTextSize(28);
                } else {
                    mToast.setText(message);
                }
                mToast.setGravity(Gravity.TOP, 0, 0);
                mToast.show();
            }
        });
    }

    /**
     * 短时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShortOnTop(final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplication(), resId, Toast.LENGTH_SHORT);
                    TextView v = mToast.getView().findViewById(android.R.id.message);
                    v.setTextSize(28);
                } else {
                    mToast.setText(resId);
                }
                mToast.setGravity(Gravity.TOP, 0, 600);
                mToast.show();
            }
        });
    }

    public static void showShortOnCentet(final CharSequence message) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
                    TextView v = mToast.getView().findViewById(android.R.id.message);
                    v.setTextSize(28);
                } else {
                    mToast.setText(message);
                }
                mToast.setGravity(Gravity.TOP, 0, 600);
                mToast.show();
            }
        });
    }
}
