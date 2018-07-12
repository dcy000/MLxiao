package com.gcml.module_blutooth_devices.weight_devices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.utils.SPUtil;

import java.math.BigDecimal;
import java.util.List;

import senssun.blelib.device.scale.cloudblelib.BleCloudProtocolUtils;
import senssun.blelib.model.BleDevice;
import senssun.blelib.scan.BleScan;

/**
 * 香山体脂称
 * name:SENSSUN CLOUD
 * mac:18:7A:93:4F:43:11(白色云联版)(这一款必须拖鞋测量才有数据回来)
 * mac:18:7A:93:50:10:5C(黑色云联版)
 * 云联版的意思是可以连接对象提供的健康分析平台
 */
@SuppressLint("LongLogTag")
public class Weight_Xiangshan_EF895i_PresenterImp extends BaseBluetoothPresenter {
    private final BleScan bleScan;
    private final BleCloudProtocolUtils bleCloudProtocolUtils;
    private boolean isConnected = false;
    private boolean isDestroy = false;

    public Weight_Xiangshan_EF895i_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
        bleCloudProtocolUtils = BleCloudProtocolUtils.getInstance(baseContext);
        bleScan = new BleScan();
        bleScan.Create(baseContext);
        setListener();
        if (discoverType == IPresenter.DISCOVER_WITH_MAC && !TextUtils.isEmpty(targetAddress)) {
            Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "物理地址连接:" + targetAddress);
            connectDevice(targetAddress);
        } else {
            Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "搜索设备:");
            searchDevices();
        }
    }

    private void setListener() {
        bleCloudProtocolUtils.setOnConnectState(new BleCloudProtocolUtils.OnConnectState() {
            @Override
            public void OnState(boolean b) {
                if (b) {
                    isConnected = true;
                    Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "OnState: 连接成功");
                    baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
                    baseView.updateData("0.00");
                    SPUtil.put(baseContext, SPUtil.SP_SAVE_WEIGHT, targetName + "," + targetAddress);
                } else {
                    if (!isDestroy) {
                        connectDevice(targetAddress);
                    }
                    isConnected = false;
                    Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "OnState: 断开连接");
                    if (((Fragment) baseView).isAdded()) {
                        baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
                    }

                    if (!isDestroy) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        connectDevice(targetAddress);
                    }
                }
            }
        });


        bleCloudProtocolUtils.setOnDisplayDATA(new BleCloudProtocolUtils.OnDisplayDATA() {
            @Override
            public void OnDATA(String data) {
                String[] strdata = data.split("-");
                switch (strdata[5]) {
                    case "03":
                        switch (strdata[6]) {
                            case "80":
                                if (strdata[12].equals("A0")) {
                                    String tmpNum = strdata[7] + strdata[8];
                                    Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "OnDATA: 不稳定体重" + String.format("%.1f", Integer.valueOf(tmpNum, 16) / 10f) + "KG");
                                    baseView.updateData(String.format("%.2f", Integer.valueOf(tmpNum, 16) / 10f));
                                } else {
                                    String tmpNum = strdata[7] + strdata[8];
                                    Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "OnDATA:稳定体重 " + String.format("%.1f", Integer.valueOf(tmpNum, 16) / 10f) + "KG");
                                    baseView.updateData(String.format("%.2f", Integer.valueOf(tmpNum, 16) / 10f));
                                }
                                break;
                            case "82":
                                switch (strdata[7]) {
                                    case "00": {
                                        Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "OnDATA: " + new BigDecimal(Integer.valueOf(strdata[10] + strdata[11], 16) / 10f).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                                        float eigenvalue = new BigDecimal(Integer.valueOf(strdata[14] + strdata[15], 16)).floatValue();
                                        float weight = new BigDecimal(Integer.valueOf(strdata[10] + strdata[11], 16) / 10f).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                                        //TODO:请求香山的接口 获取更加详细的健康分析数据
                                    }
                                    break;
                                }

                        }
                        break;
                }
            }
        });
    }

    @Override
    protected boolean isSelfDefined() {
        return true;
    }

    @Override
    public void connectDevice(String macAddress) {
        bleCloudProtocolUtils.Connect(macAddress);
    }

    @Override
    public void searchDevices() {
        bleScan.setOnScanListening(new BleScan.OnScanListening() {

            @Override
            public void OnListening(BleDevice bleDevice) {
                Logg.e(Weight_Xiangshan_EF895i_PresenterImp.class, "OnListening: " + bleDevice.getDeviceType().toString() + "---" + bleDevice.getBluetoothDevice().getAddress());
                if (bleDevice.getBluetoothDevice().getAddress().equals(targetAddress)) {
                    bleScan.scanLeStopDevice();
                    connectDevice(targetAddress);
                }
            }
        });
        //扫描15秒未搜索到会自动结束扫描
        bleScan.ScanLeStartDevice(15 * 1000);
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        if (bleScan != null) {
            if (bleScan.ismScanning()) {
                bleScan.scanLeStopDevice();
            }
        }
        if (isConnected) {
            bleCloudProtocolUtils.Disconnect();
        }
//        if (bleCloudProtocolUtils != null && context != null) {
//            bleCloudProtocolUtils.stopSDK(context);
//        }
        super.onDestroy();
    }
}
