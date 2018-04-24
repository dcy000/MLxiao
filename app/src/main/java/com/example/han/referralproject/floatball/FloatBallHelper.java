package com.example.han.referralproject.floatball;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.widget.PopupWindow;

import com.example.han.referralproject.application.MyApplication;

/**
 * Created by lenovo on 2018/4/24.
 */

public class FloatBallHelper {
    private FloatBallHelper() {
    }

    public static Handler handler = MyApplication.getInstance().getBgHandler();

    public static void showWindow(final PopupWindow popupWindow, Activity activity) {
        handler.removeCallbacksAndMessages(null);
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            autoDismiss(popupWindow, activity);
        }

    }

    public static void autoDismiss(final PopupWindow popupWindow, final Activity activity) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (popupWindow != null)
                            popupWindow.dismiss();
                    }
                });
            }
        }, 5000);
    }

}
