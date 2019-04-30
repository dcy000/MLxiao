package com.gcml.old.auth.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.homepage.MainActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.network.NetUitls;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;


public class PersonDetailActivity extends AppCompatActivity {
    List<Fragment> fragments = new ArrayList<>();
    private ViewPager vpContent;
    private TranslucentToolBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        initView();
        registerReceiver(wifiChangedReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                getString(R.string.person_info));
    }

    private BroadcastReceiver wifiChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = obtainWifiInfo();
            if (level <= 0 && level >= -50) {
                mToolBar.iconRight.setBackgroundResource(R.drawable.white_wifi_3);
            } else if (level < -50 && level >= -70) {
                mToolBar.iconRight.setBackgroundResource(R.drawable.white_wifi_2);
            } else if (level < -70) {
                mToolBar.iconRight.setBackgroundResource(R.drawable.white_wifi_1);
            } else {
                mToolBar.iconRight.setBackgroundResource(R.drawable.white_wifi_error);
            }
        }
    };

    private int obtainWifiInfo() {
        // Wifi的连接速度及信号强度：
        int strength = 0;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            // 链接信号强度，5为获取的信号强度值在5以内
            strength = info.getRssi();
            // 链接速度
            int speed = info.getLinkSpeed();
            // 链接速度单位
            String units = WifiInfo.LINK_SPEED_UNITS;
            // Wifi源名称
            String ssid = info.getSSID();
        }
        return strength;
    }

    private void initView() {
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setData("个 人 中 心", R.drawable.common_icon_back, "返回",
                R.drawable.icon_wifi, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        startActivity(new Intent(PersonDetailActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                    }
                });
        vpContent = findViewById(R.id.vp_content);
        PersonDetailFragment detail = new PersonDetailFragment();
//        PersonDetail2Fragment detail2 = new PersonDetail2Fragment();
        fragments.add(detail);
//        fragments.add(detail2);
        vpContent.setAdapter(new PersonDetailFragmentPagerAdapter(getSupportFragmentManager(), fragments));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetUitls.isWifiConnected()) {
            mToolBar.iconRight.setBackgroundResource(R.drawable.white_wifi_error);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiChangedReceiver);
    }
}
