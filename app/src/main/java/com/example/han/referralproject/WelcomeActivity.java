package com.example.han.referralproject;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;

import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.common.recommend.bean.get.VersionInfoBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.network.NetUitls;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/app/welcome/activity")
public class WelcomeActivity extends AppCompatActivity {


    private Chronometer ch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
    }

    private void initContentView() {
        setContentView(R.layout.activity_welcome);
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
            finish();
            return;
        } else {
            checkVersion();
        }
    }

    private void checkVersion() {
        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                try {
                    if (response != null && response.vid > getPackageManager().getPackageInfo(WelcomeActivity.this.getPackageName(), 0).versionCode) {
                        Routerfit.register(AppRouter.class).getAppUpdateProvider().showDialog(WelcomeActivity.this, response.url);
                    } else {
                        ch = findViewById(R.id.chronometer);
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
                                    Routerfit.register(AppRouter.class).skipAuthActivity();
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
                ch = findViewById(R.id.chronometer);
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
                            Routerfit.register(AppRouter.class).skipAuthActivity();
                            finish();
                        }
                    }
                });
            }
        });
    }
}
