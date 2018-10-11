package com.example.han.referralproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.WifiConnectRecyclerAdapter;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.homepage.MainActivity;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.wifi.WifiUtils;
import com.gcml.common.wifi.wifiScan.ScanResultsListener;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WifiConnectActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecycler;
    private List<ScanResult> mList = new ArrayList<>();
    private WifiConnectRecyclerAdapter mAdapter;
    private SwitchButton mSwitch;
    private View mConnectedLayout;
    private TextView mConnectedWifiName;
    private ImageView mConnectedWifiLevel;
    private WifiManager mWifiManager;
    private boolean isFirstWifi = false;
    private MediaPlayer mediaPlayer;
    private Animation mRefreshAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect_layout);

        bindView();
        bindData();
    }

    private void bindView() {
        mConnectedLayout = findViewById(R.id.rl_connected);
        mConnectedWifiName = findViewById(R.id.tv_connect_name);
        mConnectedWifiLevel = findViewById(R.id.iv_connect_levle);
        mSwitch = findViewById(R.id.switch_wifi);
        mRecycler = findViewById(R.id.rv_wifi);
    }

    private void bindData() {
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.icon_refresh);
        mRightView.setOnClickListener(this);
        mTitleText.setText("WiFi连接");
        mRefreshAnim =  AnimationUtils.loadAnimation(this, R.anim.common_wifi_refresh);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean iswifiConnected=cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
        if (iswifiConnected) {
            speak("主人,您的wifi已连接,如果需要更换,请点击对应wifi名称");
        } else {
            speak("主人,请连接wifi,如果未找到,请点击右上角的刷新按钮");
        }
        isFirstWifi= getIntent().getBooleanExtra("is_first_wifi", false);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mSwitch.setChecked(mWifiManager.isWifiEnabled());
        mSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    WifiUtils.withContext(getApplicationContext()).enableWifi();
                    getWifiData(true);
                } else {
                    WifiUtils.withContext(getApplicationContext()).disableWifi();
                    getWifiData(false);
                }
            }
        });

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WifiConnectRecyclerAdapter(mContext, mList);
        mRecycler.setAdapter(mAdapter);
        getWifiData(mWifiManager.isWifiEnabled());

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);
    }

    private void getWifiData(boolean isEbable) {
        mRightView.startAnimation(mRefreshAnim);
        if (isEbable) {
            WifiInfo mInfo = mWifiManager.getConnectionInfo();
            if (mInfo != null) {
                mConnectedWifiName.setText(mInfo.getSSID());
                RxUtils.rxWifiLevel(getApplication(), 4)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer level) throws Exception {
                                mConnectedWifiLevel.setImageLevel(level);
                            }
                        });
            }
            WifiUtils.withContext(getApplicationContext())
                    .scanWifi(new ScanResultsListener() {
                        @Override
                        public void onScanResults(@NonNull List<ScanResult> scanResults) {
                            mList.clear();
                            mList.addAll(scanResults);
                            for (int i = 0; i < mList.size(); i++) {
                                if (mInfo.getBSSID() != null && mList.get(i).BSSID.equals(mInfo.getBSSID())) {
                                    mList.remove(i);
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                            mRightView.clearAnimation();
                            Log.e("xxxxxxxxxx", scanResults.size() + scanResults.toArray().toString());
                        }
                    }).start();
        } else {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mRightView.clearAnimation();
        }
    }

    private void checkResult(boolean isSuccess) {
        if (isSuccess)
            Toast.makeText(WifiConnectActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(WifiConnectActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_top_right:
                getWifiData(mWifiManager.isWifiEnabled());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

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
                            mConnectedLayout.setVisibility(View.VISIBLE);
                        } else {
                            mConnectedLayout.setVisibility(View.GONE);
                        }
                    }
                    break;
                // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()){
                        if (isFirstWifi){
                            if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
                                CC.obtainBuilder("com.gcml.auth")
                                        .build()
                                        .callAsync();
                            } else {
                                startActivity(new Intent(mContext, MainActivity.class));
                            }
                            finish();
                        }
                    }
                    getWifiData(mWifiManager.isWifiEnabled());

//                    NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//                    if (info != null) {
//                        //如果当前的网络连接成功并且网络连接可用
//                        if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
//                            if (info.getType() == ConnectivityManager.TYPE_WIFI
//                                    || info.getType() == ConnectivityManager.TYPE_MOBILE) {
//                                //Logg.i("TAG", getConnectionType(info.getType()) + "连上");
//                            }
//                        } else {
//                            //Logg.i("TAG", getConnectionType(info.getType()) + "断开");
//                        }
//                    }
                    break;

            }
        }
    };
}
