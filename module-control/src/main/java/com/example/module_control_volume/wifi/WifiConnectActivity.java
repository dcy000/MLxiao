package com.example.module_control_volume.wifi;

import android.arch.lifecycle.LifecycleOwner;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.module_control_volume.R;
import com.gcml.common.RoomHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.network.WiFiUtil;
import com.gcml.common.widget.dialog.InputDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.common.wifi.AutoNetworkUtils;
import com.gcml.common.wifi.WifiUtils;
import com.gcml.common.wifi.wifiScan.ScanResultsListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/app/activity/wifi/connect")
public class WifiConnectActivity extends ToolbarBaseActivity implements View.OnClickListener {

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
    private boolean currentConnected;
    private boolean isSearchingWifi;
    //wifi连接超时时间
    private static final int WIFI_CONNECT_TIMEOUT = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect_layout);

        AutoNetworkUtils.showWifiDisconnectedPage = false;

        bindView();
        bindData();
        setAdapter();
    }

    @Override
    protected void backMainActivity() {

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

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);
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
                helper.getView(R.id.wifi_detail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Routerfit.register(AppRouter.class).skipWifiDetailActivity(item);
                        showWifiDetailDialog(item);
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
                    showLoading(currentConnected ? "正在切换WiFi" : "正在连接WiFi");
                    resetConnectAndTimeCountDown();
                    WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(itemResult.SSID, "", WiFiUtil.Data.WIFI_CIPHER_NOPASS);
                }
            }
        });
        getWifiData(mWifiManager.isWifiEnabled());
    }

    private void showWifiDetailDialog(ScanResult item) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_wifi_detail_page)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.title, item.SSID);
                        holder.setText(R.id.wifi_level, item.level + "");
                        String capabilities = item.capabilities;
                        StringBuffer buffer = new StringBuffer(" ");
                        if (capabilities.contains("WPA-PSK")) {
                            buffer.append("WPA-PSK/");
                        }
                        if (capabilities.contains("WPA2-PSK")) {
                            buffer.append("WPA2-PSK/");
                        }
                        holder.setText(R.id.wifi_capabilities, buffer.subSequence(0, buffer.length() - 1).toString());
                        holder.getView(R.id.btn_foget_password).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Handlers.bg().post(() -> RoomHelper.db(WifiDB.class, WifiDB.class.getName()).wifiDao().deleteByKey(item.BSSID));
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(400)
                .show(getSupportFragmentManager());
    }

    private Disposable wifiTimeout;

    private void resetConnectAndTimeCountDown() {
        //开始连接倒计时
        RxUtils.rxCountDown(1, WIFI_CONNECT_TIMEOUT)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        wifiTimeout = disposable;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!currentConnected) {
                            dismissLoading();
                            showInputPasswordDialog();
                        }
                    }
                });
        currentConnected = false;
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
                            showInputPasswordDialog();
                        }
                    });
                } else {
                    Handlers.ui().post(new Runnable() {
                        @Override
                        public void run() {
                            showLoading(currentConnected ? "正在切换WiFi" : "正在连接WiFi");
                            resetConnectAndTimeCountDown();
                            String capabilities = result.capabilities;
                            String wifiName = result.SSID;
                            String password = result.password;
                            if (capabilities.contains("WPA2")) {
                                WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WPA2);
                            } else if (capabilities.contains("WPA-PSK")) {
                                WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WPA2);
                            } else if (capabilities.contains("WPA")) {
                                WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WPA);
                            } else if (capabilities.contains("WEP")) {
                                WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WEP);
                            }
                        }
                    });
                }
            }
        });
    }

    private void showInputPasswordDialog() {
        String wifiName = currentWifi.SSID;
        String capabilities = currentWifi.capabilities;
        new InputDialog(this)
                .builder()
                .setMsg(wifiName)
                .setMsgColor(R.color.config_color_appthema)
                .setPositiveButton("连接", new InputDialog.OnInputChangeListener() {
                    @Override
                    public void onInput(String password) {
                        currentPassword = password;
                        showLoading(currentConnected ? "正在切换WiFi" : "正在连接WiFi");
                        resetConnectAndTimeCountDown();
                        if (capabilities.contains("WPA2")) {
                            WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WPA2);
                        } else if (capabilities.contains("WPA-PSK")) {
                            WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WPA2);
                        } else if (capabilities.contains("WPA")) {
                            WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WPA);
                        } else if (capabilities.contains("WEP")) {
                            WiFiUtil.getInstance(WifiConnectActivity.this).addWiFiNetwork(wifiName, password, WiFiUtil.Data.WIFI_CIPHER_WEP);
                        }
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
                currentConnected = true;
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
            if (!isSearchingWifi) {
                isSearchingWifi = true;
                WifiUtils.withContext(getApplicationContext())
                        .scanWifi(new ScanResultsListener() {
                            @Override
                            public void onScanResults(@NonNull List<ScanResult> scanResults) {
                                isSearchingWifi = false;
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
                                    Timber.e(result.toString());
                                    if (!mList.contains(result)) {
                                        mList.add(result);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                mRightView.clearAnimation();
                            }
                        }).start();
            }
        } else {
            currentConnected = false;
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

    private void saveRoom() {
        if (currentWifi != null) {
            Handlers.bg().post(new Runnable() {
                @Override
                public void run() {
                    WifiEntity entity = new WifiEntity();
                    entity.SSID = currentWifi.SSID;
                    entity.BSSID = currentWifi.BSSID;
                    entity.frequency = currentWifi.frequency;
                    entity.password = currentPassword;
                    entity.capabilities = currentWifi.capabilities;
                    RoomHelper.db(WifiDB.class, WifiDB.class.getName()).wifiDao().insertWifiCache(entity);
                }
            });

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
                    if (networkInfo != null && networkInfo.isConnected()) {
                        dismissLoading();
                        currentConnected = true;
                        if (wifiTimeout != null && wifiTimeout.isDisposed()) {
                            wifiTimeout.dispose();
                        }
                        saveRoom();
                        if (isFirstWifi) {
                            Routerfit.register(AppRouter.class).skipAuthActivity();
                            finish();
                        }
                    }
                    getWifiData(mWifiManager.isWifiEnabled());
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        AutoNetworkUtils.showWifiDisconnectedPage = true;
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
    }
}
