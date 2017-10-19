package com.example.han.referralproject.activity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.WifiConnectRecyclerAdapter;
import com.example.han.referralproject.util.WiFiUtil;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectActivity extends BaseActivity implements View.OnClickListener{
    private WifiConnectRecyclerAdapter mConnectAdapter;
    private List<ScanResult> mDataList = new ArrayList<>();
    private WiFiUtil mWiFiUtil;
    private Switch mSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect_layout);
        mWiFiUtil = WiFiUtil.getInstance(this);
        mSwitch = (Switch) findViewById(R.id.switch_wifi);
        mSwitch.setChecked(mWiFiUtil.isWifiOpened());
        mSwitch.setOnCheckedChangeListener(mCheckedChangeListener);
        findViewById(R.id.iv_refresh).setOnClickListener(this);
        RecyclerView mWifiRv = (RecyclerView) findViewById(R.id.rv_wifi);
        mWifiRv.setLayoutManager(new LinearLayoutManager(mContext));
        mConnectAdapter = new WifiConnectRecyclerAdapter(mContext, mDataList);
        mWifiRv.setAdapter(mConnectAdapter);
        scanWifi();
    }

    private void scanWifi() {
        mDataList.clear();
        mWiFiUtil.startScan();
        mDataList.addAll(mWiFiUtil.getWifiList());
        mConnectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_refresh:
                scanWifi();
                break;
            case R.id.tv_system:
                startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                break;
        }
    }

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mWiFiUtil.openWifi();
            } else {
                mWiFiUtil.closeWifi();
            }
        }
    };
}
