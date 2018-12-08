package com.example.module_person.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.module_person.R;
import com.example.module_person.R2;
import com.example.module_person.adapter.PersonDetailFragmentPagerAdapter;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.IPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonDetailActivity extends ToolbarBaseActivity implements View.OnClickListener {


    List<Fragment> fragments = new ArrayList<>();
    @BindView(R2.id.vp_content)
    ViewPager vpContent;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_person_detail;
    }

    @Override
    public void initParams(Intent intentArgument) {
        registerReceiver(wifiChangedReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

    private BroadcastReceiver wifiChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = obtainWifiInfo();
            Log.e("网络强度发生变化", "onReceive: " + level);
            if (level <= 0 && level >= -50) {
                mRightView.setImageResource(R.drawable.white_wifi_3);
            } else if (level < -50 && level >= -70) {
                mRightView.setImageResource(R.drawable.white_wifi_2);
            } else if (level < -70) {
                mRightView.setImageResource(R.drawable.white_wifi_1);
            }
        }
    };

    private int obtainWifiInfo() {
        // Wifi的连接速度及信号强度：
        int strength = 0;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            // 链接信号强度，5为获取的信号强度值在5以内
            strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            // 链接速度
            int speed = info.getLinkSpeed();
            // 链接速度单位
            String units = WifiInfo.LINK_SPEED_UNITS;
            // Wifi源名称
            String ssid = info.getSSID();
        }
        return strength;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mTitleText.setText("个 人 中 心");
        mRightView.setImageResource(R.drawable.icon_wifi);
        mRightView.setOnClickListener(this);

        PersonDetailFragment detail = new PersonDetailFragment();
        PersonDetail2Fragment detail2 = new PersonDetail2Fragment();
        fragments.add(detail);
        fragments.add(detail2);

        vpContent.setAdapter(new PersonDetailFragmentPagerAdapter(getSupportFragmentManager(), fragments));
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        emitEvent("skip2WifiConnectActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiChangedReceiver);
    }
}
