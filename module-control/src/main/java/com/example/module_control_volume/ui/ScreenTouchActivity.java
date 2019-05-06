package com.example.module_control_volume.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.module_control_volume.R;
import com.example.module_control_volume.wrap.SignView;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.dialog.AlertDialog;

public class ScreenTouchActivity extends ToolbarBaseActivity {

    SignView signView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_touch);
        signView = findViewById(R.id.signView);

//        signView.setOnDoubleClickListener(new SignView.OnDoubleClickListener() {
//            @Override
//            public void onDoubleClick(View v) {
//                backActivity();
//            }
//        });

        signView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                backActivity();
                return false;
            }
        });
    }

    private void backActivity() {
        new AlertDialog(this)
                .builder()
                .setMsg("是否退出当前页面?")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
