package com.example.han.referralproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.WifiConnectRecyclerAdapter;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.util.WiFiUtil;
import com.medlink.danbogh.signin.SignInActivity;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectActivity extends BaseActivity implements View.OnClickListener{
    private WifiConnectRecyclerAdapter mConnectAdapter;
    private List<ScanResult> mDataList = new ArrayList<>();
    private WiFiUtil mWiFiUtil;
    private Switch mSwitch;
    private View mConnectedLayout;
    private TextView mConnectedWifiName;
    private WifiManager mWifiManager;
    private Handler mHandler = new Handler();
    private boolean isFirstWifi = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect_layout);
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.icon_refresh);
        mTitleText.setText("WiFi连接");
        mRightView.setOnClickListener(this);
        isFirstWifi= getIntent().getBooleanExtra("is_first_wifi", false);
        mWiFiUtil = WiFiUtil.getInstance(this);
        mWiFiUtil.openWifi();
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mConnectedLayout = findViewById(R.id.rl_connected);
        mConnectedWifiName = (TextView) findViewById(R.id.tv_connect_name);
        mSwitch = (Switch) findViewById(R.id.switch_wifi);
        mSwitch.setChecked(mWiFiUtil.isWifiOpened());
        mSwitch.setOnCheckedChangeListener(mCheckedChangeListener);
        RecyclerView mWifiRv = (RecyclerView) findViewById(R.id.rv_wifi);
        mWifiRv.setLayoutManager(new LinearLayoutManager(mContext));
        mConnectAdapter = new WifiConnectRecyclerAdapter(mContext, mDataList);
        mWifiRv.setAdapter(mConnectAdapter);
        scanWifi();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);
    }

    private void scanWifi() {
        mDataList.clear();
        mWiFiUtil.startScan();
        WifiInfo mInfo = mWifiManager.getConnectionInfo();
        if (mInfo != null) {
            mConnectedLayout.setVisibility(View.VISIBLE);
            mConnectedWifiName.setText(mInfo.getSSID());
            for (ScanResult itemResult : mWiFiUtil.getWifiList()) {
                if (!itemResult.BSSID.equals(mInfo.getBSSID())){
                    mDataList.add(itemResult);
                }
            }
        } else {
            mConnectedLayout.setVisibility(View.GONE);
            mDataList.addAll(mWiFiUtil.getWifiList());
        }
        mConnectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_top_right:
                scanWifi();
                break;
            case R.id.tv_system:
                startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                break;
            case R.id.view_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
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

    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("mylog", intent.getAction());
            switch (intent.getAction()) {
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    Log.e("TAG", "wifiState:" + wifiState);
                    switch (wifiState) {
                        case WifiManager.WIFI_STATE_DISABLED:
                            break;
                        case WifiManager.WIFI_STATE_DISABLING:
                            break;
                    }
                    break;
                // 监听wifi的连接状态即是否连上了一个有效无线路由
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    Parcelable parcelableExtra = intent
                            .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (null != parcelableExtra) {
                        // 获取联网状态的NetWorkInfo对象
                        NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                        //获取的State对象则代表着连接成功与否等状态
                        NetworkInfo.State state = networkInfo.getState();
                        //判断网络是否已经连接
                        boolean isConnected = state == NetworkInfo.State.CONNECTED;
                        Log.e("TAG", "isConnected:" + isConnected);
                        if (isConnected) {
                        } else {

                        }
                    }
                    break;
                // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()){
                        //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                        if (isFirstWifi){
                            if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
                                startActivity(new Intent(mContext, SignInActivity.class));
                            } else {
                                startActivity(new Intent(mContext, MainActivity.class));
                            }
                            finish();
                        } else {
                            scanWifi();
                        }
                    }
//                    if (info != null) {
//                        //如果当前的网络连接成功并且网络连接可用
//                        if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
//                            if (info.getType() == ConnectivityManager.TYPE_WIFI
//                                    || info.getType() == ConnectivityManager.TYPE_MOBILE) {
//                                //Log.i("TAG", getConnectionType(info.getType()) + "连上");
//                            }
//                        } else {
//                            //Log.i("TAG", getConnectionType(info.getType()) + "断开");
//                        }
//                    }
                    break;

            }
        }
    };
}
