package com.gcml.module_blutooth_devices.bloodsugar_devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.gcml.module_blutooth_devices.utils.ToastUtils;
import com.gcml.module_blutooth_devices.utils.WeakHandler;
import com.sinocare.Impl.SC_BlueToothCallBack;
import com.sinocare.Impl.SC_BlueToothSearchCallBack;
import com.sinocare.Impl.SC_CurrentDataCallBack;
import com.sinocare.domain.BloodSugarData;
import com.sinocare.domain.BlueToothInfo;
import com.sinocare.handler.SN_MainHandler;
import com.sinocare.protocols.ProtocolVersion;
import com.sinocare.status.SC_DataStatusUpdate;
import com.sinocare.status.SC_ErrorStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 三诺血糖仪(提供的jar包是过滤搜索)
 * name:BDE_WEIXIN_TTM
 * mac:18:93:D7:23:06:8F
 */
public class Bloodsugar_Sannuo_PresenterImp extends BaseBluetoothPresenter {
    //    private IView fragment;
    private SN_MainHandler snMainHandler;
    private int discoverType;
    private static TimeCount timeCount;
    private static List<BlueToothInfo> devices;
    private boolean isOnSearching = false;
    private static WeakHandler weakHandler;
    private static final int CAN_CONNECT_DEVICE = 1;
    private static final int UNFOUND_DEVICE = 2;
    private static boolean isSearchSecond = false;

    public Bloodsugar_Sannuo_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
//        this.fragment = fragment;
        timeCount = new TimeCount(5000, 1000);
        devices = new ArrayList<>();
        weakHandler = new WeakHandler(handlerCallback);
        fragment.getThisContext().registerReceiver(mBtReceiver, makeIntentFilter());
        //在6.0以上这里需要动态申请权限，获得权限后初始化该对象
        snMainHandler = SN_MainHandler.getBlueToothInstance(fragment.getThisContext());
        discoverType = discoverSetting.getDiscoverType();
        searchDevices();
    }

    @Override
    protected boolean isSelfDefined() {
        return true;
    }

    private final Handler.Callback handlerCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == CAN_CONNECT_DEVICE) {
                if (TextUtils.isEmpty(targetAddress)) {
                    throw new NullPointerException("连接设备为NULL");
                }
                connectDevice(targetAddress);
            } else if (msg.what == UNFOUND_DEVICE) {
                baseView.updateState("未搜索到设备");
            }
            return false;
        }
    };

    @Override
    public void checkBlueboothOpened() {

    }

    //自己实现搜索逻辑（使用设备自己提供的jar包等）
    @Override
    public void searchDevices() {
        if (discoverSetting != null) {
            discoverType = discoverSetting.getDiscoverType();
            timeCount.start();
            isOnSearching = true;
            snMainHandler.searchBlueToothDevice(new SC_BlueToothSearchCallBack<BlueToothInfo>() {
                @Override
                public void onBlueToothSeaching(BlueToothInfo blueToothInfo) {
                    devices.add(blueToothInfo);
                    Logg.e(Bloodsugar_Sannuo_PresenterImp.class, blueToothInfo.getName() + "++" + blueToothInfo.getDevice().getAddress());
                    switch (discoverType) {
                        case IPresenter.DISCOVER_WITH_MAC:
                            Logg.e(Bloodsugar_Sannuo_PresenterImp.class, "onBlueToothSeaching: Mac");
                            if (TextUtils.isEmpty(discoverSetting.getTargetMac())) {
                                ToastUtils.showShort("请设置目标mac地址");
                                return;
                            }
                            if (blueToothInfo.getDevice().getAddress().equals(discoverSetting.getTargetMac())) {
                                snMainHandler.stopSearch();
                                Logg.e(Bloodsugar_Sannuo_PresenterImp.class, "onBlueToothSeaching: 发现目标设备");
                                isOnSearching = false;
                                lockedDevice = blueToothInfo.getDevice();
                                connectDevice(targetAddress);
                            }
                            break;
                        case IPresenter.DISCOVER_WITH_NAME:
                            Logg.e(Bloodsugar_Sannuo_PresenterImp.class, "onBlueToothSeaching: Name");
                            if (TextUtils.isEmpty(discoverSetting.getTargetName())) {
                                ToastUtils.showShort("请设置目标蓝牙名称");
                                return;
                            }
                            if (blueToothInfo.getName().equals(discoverSetting.getTargetName())) {
                                snMainHandler.stopSearch();
                                Logg.e(Bloodsugar_Sannuo_PresenterImp.class, "onBlueToothSeaching: 发现目标设备");
                                isOnSearching = false;
                                lockedDevice = blueToothInfo.getDevice();
                                connectDevice(targetAddress);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

        }
    }

    @Override
    public void connectDevice(String macAddress) {

        snMainHandler.connectBlueTooth(lockedDevice, new SC_BlueToothCallBack() {
            @Override
            public void onConnectFeedBack(int i) {
                if (i == 16) {
                    if (isOnSearching) {
                        snMainHandler.stopSearch();
                    }
                    baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
                    baseView.updateData("initialization", "0.00");
                    SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, targetName + "," + lockedDevice.getAddress());
                } else {
                    Logg.e(Bloodsugar_Sannuo_PresenterImp.class, "onConnectFeedBack: 设备连接失败");
                }
            }
        }, ProtocolVersion.WL_WEIXIN_AIR);

        snMainHandler.registerReceiveBloodSugarData(new SC_CurrentDataCallBack<BloodSugarData>() {

            @Override
            public void onStatusChange(int status) {
                // TODO Auto-generated method stub
                switch (status) {
                    case SC_DataStatusUpdate.SC_BLOOD_FFLASH:
//                        fragment.updateState("请插入试条");
                        break;
                    case SC_DataStatusUpdate.SC_MC_TESTING:
                        baseView.updateState("正在分析血液，请稍后");
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
                float result = datas.getBloodSugarValue();
                Date date = datas.getCreatTime();
                float t = datas.getTemperature();
                baseView.updateData(String.format("%.2f", result));
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
                    ToastUtils.showShort("手机设备不支持低功耗蓝牙，无法连接血糖仪");
                } else if (snMainHandler.isConnected()) {//
                }
            } else if (SN_MainHandler.ACTION_SN_ERROR_STATE.equals(action)) {
                Bundle bundle = intent.getExtras();
                int errorStatus = bundle.getInt(SN_MainHandler.EXTRA_ERROR_STATUS);
                switch (errorStatus) {
                    case SC_ErrorStatus.SC_OVER_RANGED_TEMPERATURE:
                        baseView.updateState("错误码：E-2");
                        break;
                    case SC_ErrorStatus.SC_AUTH_ERROR:
                        baseView.updateState("错误：认证失败！");
                        break;
                    case SC_ErrorStatus.SC_ERROR_OPERATE:
                        baseView.updateState("错误码：E-3！");
                        break;
                    case SC_ErrorStatus.SC_ERROR_FACTORY:
                        baseView.updateState("错误码：E-6！");
                        break;
                    case SC_ErrorStatus.SC_ABLOVE_MAX_VALUE:
                        baseView.updateState("错误码：HI");
                        break;
                    case SC_ErrorStatus.SC_BELOW_LEAST_VALUE:
                        baseView.updateState("错误码：LO");
                        break;
                    case SC_ErrorStatus.SC_LOW_POWER:
                        baseView.updateState("错误码：LO");
                        break;
                    case SC_ErrorStatus.SC_UNDEFINED_ERROR:
                        baseView.updateState("未知错误！");
                        break;
                }
            } else if (SN_MainHandler.ACTION_SN_MC_STATE.equals(action)) {
                Bundle bundle = intent.getExtras();
                int MCStatus = bundle.getInt(SN_MainHandler.EXTRA_MC_STATUS);
            }
        }
    };

    static class TimeCount extends CountDownTimer {


        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (lockedDevice == null && !isSearchSecond) {
                for (BlueToothInfo blueToothInfo : devices) {
                    String name = blueToothInfo.getName();
                    if (TextUtils.equals(name, discoverSetting.getTargetName())) {
                        lockedDevice = blueToothInfo.getDevice();
                        break;
                    }
                }
                if (lockedDevice != null) {
                    weakHandler.sendEmptyMessage(CAN_CONNECT_DEVICE);
                } else {
                    timeCount.cancel();
                    timeCount = null;
                    isSearchSecond = true;
                    timeCount = new TimeCount(30000, 2000);
                    timeCount.start();
                }
            } else if (lockedDevice == null && isSearchSecond) {
                weakHandler.sendEmptyMessage(UNFOUND_DEVICE);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (isSearchSecond) {
                for (BlueToothInfo blueToothInfo : devices) {
                    String name = blueToothInfo.getName();
                    if (TextUtils.equals(name, discoverSetting.getTargetName())) {
                        lockedDevice = blueToothInfo.getDevice();
                        break;
                    }
                }
                if (lockedDevice != null) {
                    timeCount.cancel();
                    weakHandler.sendEmptyMessage(CAN_CONNECT_DEVICE);
                }
            }
        }
    }

    private IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SN_MainHandler.ACTION_SN_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(SN_MainHandler.ACTION_SN_ERROR_STATE);
        intentFilter.addAction(SN_MainHandler.ACTION_SN_MC_STATE);
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (weakHandler != null) {
            weakHandler.removeCallbacksAndMessages(null);
            weakHandler = null;
        }
        if (isOnSearching && snMainHandler != null) {
            snMainHandler.stopSearch();
        }
        lockedDevice = null;
        devices = null;
        isSearchSecond = false;

        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        if (snMainHandler != null) {
            snMainHandler.disconnectDevice();
            baseContext.unregisterReceiver(mBtReceiver);
            snMainHandler.unRegisterReceiveBloodSugarData();
        }
    }
}
