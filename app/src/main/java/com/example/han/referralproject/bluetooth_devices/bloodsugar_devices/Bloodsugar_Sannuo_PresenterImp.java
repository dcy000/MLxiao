package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.util.ToastTool;
import com.inuker.bluetooth.library.search.SearchResult;
import com.sinocare.Impl.SC_BlueToothCallBack;
import com.sinocare.Impl.SC_BlueToothSearchCallBack;
import com.sinocare.Impl.SC_CurrentDataCallBack;
import com.sinocare.domain.BloodSugarData;
import com.sinocare.domain.BlueToothInfo;
import com.sinocare.handler.SN_MainHandler;
import com.sinocare.protocols.ProtocolVersion;
import com.sinocare.status.SC_DataStatusUpdate;
import com.sinocare.status.SC_ErrorStatus;

import java.util.Date;

public class Bloodsugar_Sannuo_PresenterImp extends BaseBluetoothPresenter {
    private Bloodsugar_Sannuo_Fragment fragment;
    private final String TAG = Bloodsugar_Sannuo_PresenterImp.this.getClass().getSimpleName();
    private SN_MainHandler snMainHandler = null;

    public Bloodsugar_Sannuo_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Bloodsugar_Sannuo_Fragment) fragment;
        this.fragment.getActivity().registerReceiver(mBtReceiver, makeIntentFilter());
        //在6.0以上这里需要动态申请权限，获得权限后初始化该对象
        snMainHandler = SN_MainHandler.getBlueToothInstance(((Bloodsugar_Sannuo_Fragment) fragment).getContext());
    }

    //自己实现搜索逻辑（使用设备自己提供的jar包等）
    @Override
    public void searchDevices() {
        if (discoverSetting != null) {
            final int discoverType = discoverSetting.getDiscoverType();
            snMainHandler.searchBlueToothDevice(new SC_BlueToothSearchCallBack<BlueToothInfo>() {
                @Override
                public void onBlueToothSeaching(BlueToothInfo blueToothInfo) {
                    if (blueToothInfo == null) {
                        return;
                    }
                    switch (discoverType) {
                        case IPresenter.DISCOVER_WITH_MAC:
                            if (TextUtils.isEmpty(discoverSetting.getTargetMac())) {
                                ToastTool.showShort("请设置目标mac地址");
                                return;
                            }
                            if (blueToothInfo.getDevice().getAddress().equals(discoverSetting.getTargetMac())) {
                                snMainHandler.stopSearch();
                                SearchResult searchResult = new SearchResult(blueToothInfo.getDevice(), blueToothInfo.getRssi(), null);
                                lockedDevice.setSearchResult(searchResult);
                                lockedDevice.setCurrentState(IPresenter.DEVICE_INITIAL);
                                stateChanged(DEVICE_FOUNDED);
                            }
                            break;
                        case IPresenter.DISCOVER_WITH_NAME:
                            if (TextUtils.isEmpty(discoverSetting.getTargetName())) {
                                ToastTool.showShort("请设置目标蓝牙名称");
                                return;
                            }
                            if (blueToothInfo.getDevice().getName().equals(discoverSetting.getTargetName())) {
                                snMainHandler.stopSearch();
                                SearchResult searchResult = new SearchResult(blueToothInfo.getDevice(), blueToothInfo.getRssi(), null);
                                lockedDevice.setSearchResult(searchResult);
                                lockedDevice.setCurrentState(IPresenter.DEVICE_INITIAL);
                                stateChanged(DEVICE_FOUNDED);
                            }
                            break;
                        case IPresenter.DISCOVER_WITH_ALL:
                            break;
                    }
                }
            });

        }

    }

    @Override
    public void connectDevice() {
        if (lockedDevice != null) {
            ToastTool.showShort("尝试连接的设备不存在");
            return;
        }
        snMainHandler.connectBlueTooth(lockedDevice.getSearchResult().device, new SC_BlueToothCallBack() {
            @Override
            public void onConnectFeedBack(int i) {
                if (i == 16) {
                    Log.d(TAG, "onConnectFeedBack: 设备连接成功");

                } else {
                    Log.d(TAG, "onConnectFeedBack: 设备连接失败");
                }
            }
        }, ProtocolVersion.WL_WEIXIN_AIR);

        snMainHandler.registerReceiveBloodSugarData(new SC_CurrentDataCallBack<BloodSugarData>() {

            @Override
            public void onStatusChange(int status) {
                // TODO Auto-generated method stub
                switch (status) {
                    case SC_DataStatusUpdate.SC_BLOOD_FFLASH:
                        fragment.updateState("请插入试条");
                        break;
                    case SC_DataStatusUpdate.SC_MC_TESTING:
                        fragment.updateState("正在分析血液，请稍后");
                        break;
                    case SC_DataStatusUpdate.SC_MC_SHUTTINGDOWN:
//                        list.add(new DeviceListItem("正在关机！", false));
                        break;
                    case SC_DataStatusUpdate.SC_MC_SHUTDOWN:
//                        list.add(new DeviceListItem("已关机！", false));
                        break;
                }
            }

            @Override
            public void onReceiveSyncData(BloodSugarData datas) {

            }

            @Override
            public void onReceiveSucess(BloodSugarData datas) {
                // TODO Auto-generated method stub
                float result= datas.getBloodSugarValue();
                Date date = datas.getCreatTime();
                float t = datas.getTemperature();
                fragment.updateData(String.format("%.2f",result));
//                list.add(new DeviceListItem("测试结果：" + result + "mmol/l," + "时间："
//                        + date.toLocaleString() + "当前温度：" + t + "°", false));
            }
        });
    }

    //广播监听SDK ACTION
    private final BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SN_MainHandler.ACTION_SN_CONNECTION_STATE_CHANGED.equals(action)) {
                if (snMainHandler.isUnSupport()) {
                    ToastTool.showShort("手机设备不支持低功耗蓝牙，无法连接血糖仪");
                } else if (snMainHandler.isConnected()) {//
                    stateChanged(DEVICE_CONNECTED);
                } else if (snMainHandler.isIdleState() || snMainHandler.isDisconnecting()) {
                    stateChanged(DEVICE_CONNECTED);
                }
            } else if (SN_MainHandler.ACTION_SN_ERROR_STATE.equals(action)) {
                Bundle bundle = intent.getExtras();
                int errorStatus = bundle.getInt(SN_MainHandler.EXTRA_ERROR_STATUS);
                switch (errorStatus) {
                    case SC_ErrorStatus.SC_OVER_RANGED_TEMPERATURE:
                        fragment.updateState("错误码：E-2");
                        break;
                    case SC_ErrorStatus.SC_AUTH_ERROR:
                        fragment.updateState("错误：认证失败！");
                        break;
                    case SC_ErrorStatus.SC_ERROR_OPERATE:
                        fragment.updateState("错误码：E-3！");
                        break;
                    case SC_ErrorStatus.SC_ERROR_FACTORY:
                        fragment.updateState("错误码：E-6！");
                        break;
                    case SC_ErrorStatus.SC_ABLOVE_MAX_VALUE:
                        fragment.updateState("错误码：HI");
                        break;
                    case SC_ErrorStatus.SC_BELOW_LEAST_VALUE:
                        fragment.updateState("错误码：LO");
                        break;
                    case SC_ErrorStatus.SC_LOW_POWER:
                        fragment.updateState("错误码：LO");
                        break;
                    case SC_ErrorStatus.SC_UNDEFINED_ERROR:
                        fragment.updateState("未知错误！");
                        break;
                }
            } else if (SN_MainHandler.ACTION_SN_MC_STATE.equals(action)) {
                Bundle bundle = intent.getExtras();
                int MCStatus = bundle.getInt(SN_MainHandler.EXTRA_MC_STATUS);
            }
        }
    };

    private IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SN_MainHandler.ACTION_SN_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(SN_MainHandler.ACTION_SN_ERROR_STATE);
        intentFilter.addAction(SN_MainHandler.ACTION_SN_MC_STATE);
        return intentFilter;
    }

    @Override
    public void stateChanged(int state) {
        switch (state) {
            case DEVICE_FOUNDED://发现目标设备
                connectDevice();
                break;
            case DEVICE_UNFOUNDED://未发现目标设备
                fragment.updateState("未发现目标设备");
                break;
            case DEVICE_CONNECTED://设备连接成功
                lockedDevice.setCurrentState(DEVICE_CONNECTED);
                fragment.updateState("设备已连接");
                break;
            case DEVICE_CONNECT_FAIL://设备连接失败
                lockedDevice.setCurrentState(DEVICE_CONNECT_FAIL);
                fragment.updateState("设备连接失败");
                break;
            case DEVICE_DISCONNECTED://设备连接断开
                lockedDevice.setCurrentState(DEVICE_INITIAL);
                fragment.updateState("设备已断开");
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (snMainHandler != null) {
            snMainHandler.disconnectDevice();
            this.fragment.getActivity().unregisterReceiver(mBtReceiver);
        }
    }
}
