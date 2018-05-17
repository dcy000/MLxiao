package com.example.han.referralproject.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class UpdateAppManager {
    // 文件分隔符
    private static final String FILE_SEPARATOR = "/";
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + FILE_SEPARATOR +"autoupdate" + FILE_SEPARATOR;
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "xiaoe_autoupdate.apk";
    // 更新应用版本标记
    private static final int UPDATE_TOKEN = 0x29;
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 0x31;
    
    private Context context;
    // 下载应用的对话框
    private Dialog dialog;
    // 下载应用的进度条
    private ProgressBar progressBar;
    // 进度条的当前刻度值
    private int curProgress;
    private TextView progressPercentage;
    // 用户是否取消下载
    private boolean isCancel;

    public UpdateAppManager(Context context){
        this.context = context;
    }
    
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case UPDATE_TOKEN:
                progressBar.setProgress(curProgress);
                if (curProgress > 0)
                    progressPercentage.setText(curProgress + "%");
                break;

            case INSTALL_TOKEN:
                installApp();
                break;
            }
        }
    };

    /**
     * 显示提示更新对话框
     */
    public void showNoticeDialog(final String downloadUrl){
        if (!isValidAppLink(downloadUrl)){
            return;
        }
        new AlertDialog.Builder(context)
                .setTitle(R.string.version_update_title)
                .setCancelable(false)
                .setMessage(R.string.version_update_new_verion_message)
                .setPositiveButton(R.string.version_update_action_download, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog(downloadUrl);
                    }
                }).setNegativeButton(R.string.version_update_action_later, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
                            context.startActivity(new Intent(context, ChooseLoginTypeActivity.class));
                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    }
                },  2000);
            }
        }).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog(String downloadUrl) {
        View view = LayoutInflater.from(context).inflate(R.layout.download_progressbar, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressPercentage = (TextView) view.findViewById(R.id.progress_percentage);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.version_update_action_download_title);
        builder.setView(view);
        builder.setNegativeButton(R.string.version_update_action_cancel, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isCancel = true;
                }
            });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        downloadApp(downloadUrl);
    }

    /**
     * 下载新版本应用
     */
    public void downloadApp(final String downloadUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(downloadUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    long fileLength = conn.getContentLength();
                    in = conn.getInputStream();
                    File filePath = new File(FILE_PATH);
                    if(!filePath.exists()) {
                        filePath.mkdir();
                    }
                    out = new FileOutputStream(new File(FILE_NAME));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLength = 0l;
                    while((len = in.read(buffer)) != -1) {
                        // 用户点击“取消”按钮，下载中断
                        if(isCancel) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        readedLength += len;
                        curProgress = (int) (((float) readedLength / fileLength) * 100);
                        handler.sendEmptyMessage(UPDATE_TOKEN);
                        if(readedLength >= fileLength) {
                            dialog.dismiss();
                            // 下载完毕，通知安装
                            handler.sendEmptyMessage(INSTALL_TOKEN);
                            break;
                        }
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    
    /**
     * 安装新版本应用
     */
    public void installApp() {
        File appFile = new File(FILE_NAME);
        if(!appFile.exists()) {
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//自动跳转
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());//自动跳转
    }

    public boolean isValidAppLink(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(url).matches();
        if (!isURL) {
            if (URLUtil.isNetworkUrl(url)) {
                try {
                    new URL(url);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }
}