package com.gcml.common.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gcml.common.demo.R;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.IconDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.dialog.SheetDialog;

import timber.log.Timber;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

    }

    public void showAlertDialog(View view) {
//        new AlertDialog(DialogActivity.this).builder()
//                .setMsg("AlertDialog默认样式")
//                .setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                })
//                .setPositiveButton("确认", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                }).show();
        new AlertDialog(this).builder()
                .setMsg("您已完成风险评估，为了更好的体验，您可以通过每日任务引导开启健康之旅。")
                .setPositiveButton("开始体验", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.e("点击了主页面");

                    }
                }).show();
    }

    public void showIconDialog(View view) {
        new IconDialog(DialogActivity.this).builder()
                .setIcon(R.drawable.common_ic_robot)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public void showSheetDialog1(View view) {
        new SheetDialog(DialogActivity.this).builder()
                .setLeftData(R.drawable.ic_launcher_background, "选项1", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setRightData(R.drawable.ic_launcher_background, "选项2", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public void showSheetDialog2(View view) {
        new SheetDialog(DialogActivity.this).builder()
                .setLeftData(R.drawable.ic_launcher_background, "选项1", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setMiddleData(R.drawable.ic_launcher_background, "选项2", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setRightData(R.drawable.ic_launcher_background, "选项3", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    LoadingDialog tipDialog;
    Handler mHandler = new Handler();

    public void showLoadingDialog1(View view) {
        tipDialog = new LoadingDialog.Builder(DialogActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 10000);
    }

    public void showLoadingDialog2(View view) {
        tipDialog = new LoadingDialog.Builder(DialogActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("加载成功")
                .create();
        tipDialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1000);
    }

    public void showLoadingDialog3(View view) {
        tipDialog = new LoadingDialog.Builder(DialogActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord("加载失败")
                .create();
        tipDialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

