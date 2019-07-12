package com.example.han.referralproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.network.NetUitls;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/app/welcome/activity")
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init();
    }

    private void init() {
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
        } else {
            Routerfit.register(AppRouter.class).skipAuthActivity();
        }
        finish();
    }

}
