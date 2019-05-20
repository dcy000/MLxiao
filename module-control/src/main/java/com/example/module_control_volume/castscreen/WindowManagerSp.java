package com.example.module_control_volume.castscreen;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.module_control_volume.R;

public class WindowManagerSp {
    WindowManager windowManager = null;
    Activity activity = null;
    Context _context = null;

    public WindowManagerSp(Activity activity) {
        if (activity == null) return;
        this.activity = activity;
        _context = activity.getBaseContext();
        windowManager = (WindowManager) activity.getApplicationContext().getSystemService(activity.WINDOW_SERVICE);
    }

    public void addBackButton() {
        if (windowManager == null) return;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.width = 90;
        layoutParams.height = 90;
        layoutParams.x = 0;
        layoutParams.y = 0;

        final ImageView backButton = new ImageView(_context);
        backButton.setBackgroundResource(R.drawable.common_icon_back);// 请自行添加相应的背景图片

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSp.restartActivity(activity, false);// 在另一个类中
                if (windowManager != null) {
                    windowManager.removeView(backButton);
                }
                windowManager = null;
            }
        });
        activity.finish();// 关闭activity，在返回时再次开启
        windowManager.addView(backButton, layoutParams);
    }
}
