package com.gcml.module_factory_test.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.gcml.module_factory_test.R;
import com.gcml.module_factory_test.widget.SignView;

import butterknife.ButterKnife;

public class ScreenTouchActivity extends ToolbarBaseActivity {

    SignView signView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_activity_screen_touch);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.GONE);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否退出当前页面?");//提示内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
