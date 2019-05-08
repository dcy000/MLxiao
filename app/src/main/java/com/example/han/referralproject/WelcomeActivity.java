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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
    }

    private void initContentView() {
        setContentView(R.layout.activity_welcome);
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
        } else {
            Routerfit.register(AppRouter.class).skipAuthActivity();
        }
        finish();
    }
}
