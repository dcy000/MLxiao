package com.example.module_control_volume.wifi;

import android.os.Bundle;
import android.view.View;

import com.example.module_control_volume.R;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.wifi.AutoNetworkUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/activity/wifi/disconnected")
public class WifiDisconnectedActivity extends ToolbarBaseActivity {

    private View tvWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_disconnected);
        mTitleText.setText("网 络 连 接");
        tvWifi = findViewById(R.id.tvWifi);
        mRightView.setVisibility(View.GONE);
        tvWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                finish();
            }
        });
        AutoNetworkUtils.rxNetworkConnectionState(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        if (!"network_none".equals(s)) {
                            finish();
                        }
                    }
                });
    }
}
