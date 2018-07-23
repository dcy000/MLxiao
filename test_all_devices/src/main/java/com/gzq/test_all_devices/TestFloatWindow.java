package com.gzq.test_all_devices;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.example.module_control_volume.VolumeControlFloatwindow;
import com.gcml.lib_utils.ui.dialog.DialogImage;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

public class TestFloatWindow extends AppCompatActivity {
    private int x = 0;
    private int y = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floatwindow);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(android.R.drawable.ic_lock_power_off);
        FloatWindow
                .with(getApplicationContext())
                .setView(imageView)
                .setWidth(Screen.height, 0.1f) //设置悬浮控件宽高
                .setHeight(Screen.width, 0.1f)
                .setX(Screen.width, 0.8f)
                .setY(Screen.height, 0.3f)
                .setMoveType(MoveType.slide, 0, 0)
                .setMoveStyle(500, new BounceInterpolator())
//                .setFilter(true, A_Activity.class, C_Activity.class)
                .setViewStateListener(new ViewStateListener() {
                    @Override
                    public void onPositionUpdate(int i, int i1) {
                        x = i;
                        y = i1;
                    }

                    @Override
                    public void onShow() {

                    }

                    @Override
                    public void onHide() {

                    }

                    @Override
                    public void onDismiss() {

                    }

                    @Override
                    public void onMoveAnimStart() {

                    }

                    @Override
                    public void onMoveAnimEnd() {

                    }

                    @Override
                    public void onBackToDesktop() {

                    }
                })
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }
                })
                .setDesktopShow(false)
                .setTag("button")
                .build();
        FloatWindow.get("button").show();

        imageView.setOnClickListener(v -> {
            FloatWindow.get("button").hide();
            DialogImage dialogImage = new DialogImage(getApplicationContext());
            dialogImage.setContentView(R.layout.app_volume_control_layout);
            dialogImage.setCanceledOnTouchOutside(true);
            dialogImage.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

            dialogImage.show();
            dialogImage.setOnDismissListener(dialog ->
                    FloatWindow.get("button").show());

        });
        VolumeControlFloatwindow.init(getApplicationContext());
    }

}
