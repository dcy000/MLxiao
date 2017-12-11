package com.example.han.referralproject;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.LoginActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.music.AppCache;
import com.example.han.referralproject.music.EventCallback;
import com.example.han.referralproject.music.PermissionReq;
import com.example.han.referralproject.music.PlayService;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.WiFiUtil;
import com.medlink.danbogh.signin.SignInActivity;

public class WelcomeActivity extends BaseActivity {

    Chronometer ch;

    private ServiceConnection mPlayServiceConnection;

    protected Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkService();
        if (!WiFiUtil.getInstance(this).isNetworkEnabled(this)){
            Intent mIntent = new Intent(mContext, WifiConnectActivity.class);
            mIntent.putExtra("is_first_wifi", true);
            startActivity(mIntent);
            finish();
        }

        //new UpdateAppManager(mContext).showNoticeDialog("http://7xt9gr.com1.z0.glb.clouddn.com/app-release.apk");
        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                try {
                    if (response != null && response.vid > getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode) {
                        new UpdateAppManager(mContext).showNoticeDialog(response.url);
                    } else {
                        ch = (Chronometer) findViewById(R.id.chronometer);
                        //设置开始计时时间
                        ch.setBase(SystemClock.elapsedRealtime());
                        //启动计时器
                        ch.start();
                        //为计时器绑定监听事件
                        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(Chronometer ch) {
                                // 如果从开始计时到现在超过了60s
                                if (SystemClock.elapsedRealtime() - ch.getBase() > 2 * 1000) {
                                    ch.stop();
                                    if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ch = (Chronometer) findViewById(R.id.chronometer);
                //设置开始计时时间
                ch.setBase(SystemClock.elapsedRealtime());
                //启动计时器
                ch.start();
                //为计时器绑定监听事件
                ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer ch) {
                        // 如果从开始计时到现在超过了60s
                        if (SystemClock.elapsedRealtime() - ch.getBase() > 2 * 1000) {
                            ch.stop();
                            if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void checkService() {
        if (AppCache.getPlayService() == null) {
            startService();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }
            }, 500);
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            AppCache.setPlayService(playService);
            PermissionReq.with(WelcomeActivity.this).permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .result(new PermissionReq.Result() {
                        @Override
                        public void onGranted() {
                         //   scanMusic(playService);

                            Log.e("==========", "PlayServiceConnection");

                        }

                        @Override
                        public void onDenied() {
                            ToastUtils.show(getString(R.string.no_permission, "存储空间", "扫描本地歌曲"));
                            finish();
                            playService.quit();
                        }
                    })
                    .request();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    private void scanMusic(final PlayService playService) {
        playService.updateMusicList(new EventCallback<Void>() {
            @Override
            public void onEvent(Void aVoid) {

            }
        });
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection);
        }
        super.onDestroy();
    }

    private void startService() {
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
        Log.e("==========", "startService");
    }


}
