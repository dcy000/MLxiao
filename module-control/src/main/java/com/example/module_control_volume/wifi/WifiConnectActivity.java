package com.example.module_control_volume.wifi;

import android.arch.lifecycle.LifecycleOwner;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.module_control_volume.R;
import com.example.module_control_volume.adapter.WifiConnectRecyclerAdapter;
import com.gcml.common.RoomHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.network.WiFiUtil;
import com.gcml.common.utils.thread.ThreadUtils;
import com.gcml.common.widget.dialog.InputDialog;
import com.gcml.common.wifi.WifiUtils;
import com.gcml.common.wifi.wifiConnect.ConnectionSuccessListener;
import com.gcml.common.wifi.wifiScan.ScanResultsListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/activity/wifi/connect")
public class WifiConnectActivity extends ToolbarBaseActivity implements View.OnClickListener, ConnectionSuccessListener {

    private RecyclerView mRecycler;
    private List<ScanResult> mList = new ArrayList<>();
    //    private WifiConnectRecyclerAdapter mAdapter;
    private SwitchButton mSwitch;
    private View mConnectedLayout;
    private TextView mConnectedWifiName;
    private ImageView mConnectedWifiLevel;
    private WifiManager mWifiManager;
    private boolean isFirstWifi = false;
    private Animation mRefreshAnim;
    private BaseQuickAdapter<ScanResult, BaseViewHolder> adapter;
    private ScanResult currentWifi;
    private String currentPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect_layout);

        bindView();
        bindData();
        setAdapter();
    }


    private void bindView() {
        mConnectedLayout = findViewById(R.id.rl_connected);
        mConnectedWifiName = findViewById(R.id.tv_connect_name);
        mConnectedWifiLevel = findViewById(R.id.iv_connect_levle);
        mSwitch = findViewById(R.id.switch_wifi);
        mRecycler = findViewById(R.id.rv_wifi);

        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.icon_refresh);
        mRightView.setOnClickListener(this);
        mTitleText.setText("WiFi连接");
        mRefreshAnim = AnimationUtils.loadAnimation(this, R.anim.common_wifi_refresh);
    }

    private void bindData() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean iswifiConnected = cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
        if (iswifiConnected) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您的wifi已连接,如果需要更换,请点击对应wifi名称");
        } else {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "请连接wifi,如果未找到,请点击右上角的刷新按钮");
        }
        isFirstWifi = getIntent().getBooleanExtra("is_first_wifi", false);
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
    }

    private void setAdapter() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(adapter = new BaseQuickAdapter<ScanResult, BaseViewHolder>(R.layout.item_wifi_info_layout, mList) {
            @Override
            protected void convert(BaseViewHolder helper, ScanResult item) {
                helper.setText(R.id.item_tv_wifi_name, item.SSID);
                RxUtils.rxWifiLevels(UM.getApp(), 4, item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxUtils.autoDisposeConverter(((LifecycleOwner) helper.itemView.getContext())))
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer level) throws Exception {
                                ((ImageView) helper.getView(R.id.item_iv_wifi_level)).setImageLevel(level);
                            }
                        });
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ScanResult itemResult = mList.get(position);
                currentWifi = itemResult;
                String capabilities = itemResult.capabilities;
                if (capabilities.contains("WPA2") || capabilities.contains("WPA-PSK") || capabilities.contains("WPA") || capabilities.contains("WEP")) {
                    checkWifiCache();
                } else {
                    currentPassword = "";
                    WifiUtils.withContext(WifiConnectActivity.this).connectWith(itemResult.SSID, "").onConnectionResult(WifiConnectActivity.this).start();
                }
            }
        });
        getWifiData(mWifiManager.isWifiEnabled());
    }

    private void checkWifiCache() {
        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {
                WifiEntity result = RoomHelper.db(WifiDB.class, WifiDB.class.getName()).wifiDao().queryByKey(currentWifi.BSSID);
                if (result == null) {
                    Handlers.ui().post(new Runnable() {
                        @Override
                        public void run() {
                            showInputPasswordDialog(currentWifi.SSID);
                        }
                    });
                } else {
                    WifiUtils.withContext(WifiConnectActivity.this).connectWith(result.SSID, result.password).onConnectionResult(WifiConnectActivity.this).start();
                }
            }
        });
    }

    private void showInputPasswordDialog(String wifiName) {
        new InputDialog(this)
                .builder()
                .setMsg(wifiName)
                .setMsgColor(R.color.config_color_appthema)
                .setPositiveButton("连接", new InputDialog.OnInputChangeListener() {
                    @Override
                    public void onInput(String s) {
                        currentPassword = s;
                        WifiUtils.withContext(WifiConnectActivity.this).connectWith(wifiName, s).onConnectionResult(WifiConnectActivity.this).start();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
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
                            for (ScanResult result : scanResults) {
                                if (TextUtils.isEmpty(result.SSID)) {
                                    continue;
                                }
                                if (mInfo != null && TextUtils.equals(mInfo.getBSSID(), result.BSSID)) {
                                    continue;
                                }
                                if (result.level < -60) {
                                    continue;
                                }
                                if (!mList.contains(result)) {
                                    mList.add(result);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            mRightView.clearAnimation();
                            Log.e("xxxxxxxxxx", scanResults.size() + scanResults.toArray().toString());
                        }
                    }).start();
        } else {
            mList.clear();
            adapter.notifyDataSetChanged();
            mRightView.clearAnimation();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_top_right) {
            getWifiData(mWifiManager.isWifiEnabled());

        }
    }

    @Override
    public void isSuccessful(boolean isSuccess) {
        if (isSuccess) {
            //存数据库
            if (currentWifi != null) {
                Handlers.bg().post(new Runnable() {
                    @Override
                    public void run() {
                        WifiEntity entity = new WifiEntity();
                        entity.SSID = currentWifi.SSID;
                        entity.BSSID = currentWifi.BSSID;
                        entity.frequency = currentWifi.frequency;
                        entity.password = currentPassword;
                        RoomHelper.db(WifiDB.class, WifiDB.class.getName()).wifiDao().insertWifiCache(entity);
                    }
                });

            }
            mConnectedLayout.setVisibility(View.VISIBLE);
            if (isFirstWifi) {
                if (TextUtils.isEmpty(UserSpHelper.getUserId())) {
                    Routerfit.register(AppRouter.class).skipAuthActivity();
                } else {
                    Routerfit.register(AppRouter.class).skipMainActivity();
                }
                finish();
            }
        } else {
            mConnectedLayout.setVisibility(View.GONE);
        }
        getWifiData(mWifiManager.isWifiEnabled());
    }
}
