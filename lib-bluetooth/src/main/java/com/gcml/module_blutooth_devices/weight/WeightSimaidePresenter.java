package com.gcml.module_blutooth_devices.weight;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.google.gson.Gson;
import com.vtrump.vtble.VTDevice;
import com.vtrump.vtble.VTDeviceManager;
import com.vtrump.vtble.VTDeviceScale;
import com.vtrump.vtble.VTModelIdentifier;

import java.util.ArrayList;

public class WeightSimaidePresenter implements LifecycleObserver {
    private SupportActivity activity;
    private IBluetoothView baseView;
    private String name;
    private String address;
    /**
     * 我司专属的KEY,由厂家直接提供
     */
    private static final String KEY = "TU1JA3D0ZI078UCC";
    private VTDeviceManager manager;
    private VTDeviceScale device;

    @SuppressLint("RestrictedApi")
    public WeightSimaidePresenter(SupportActivity activity, IBluetoothView baseView, String name, String address) {
        this.activity = activity;
        this.baseView = baseView;
        this.name = name;
        this.address = address;
        this.activity.getLifecycle().addObserver(this);
        search();
    }

    private void search() {
        manager = VTDeviceManager.getInstance();
        manager.setKey(KEY);
        manager.startBle(activity);
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                manager.setDeviceManagerListener(deviceManagerListener);
            }
        }, 1000);

        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<VTModelIdentifier> list = new ArrayList<>();
                list.add(new VTModelIdentifier(
                        VTModelIdentifier.VT_PROTOCOL_VERSION_STANDARD,
                        VTModelIdentifier.VT_DEVICE_TYPE_VSCALE,
                        VTModelIdentifier.VT_VSCALE_FAT6,
                        (byte) -1));
                manager.startScan(30, list);
            }
        }, 1000);
    }

    //VTDeviceManager 监听器
    VTDeviceManager.VTDeviceManagerListener deviceManagerListener = new VTDeviceManager.VTDeviceManagerListener() {
        @Override
        public void onInited() {
        }

        @Override
        public void onDeviceDiscovered(VTDevice vtDevice) {
        }

        @Override
        public void onDeviceConnected(VTDevice vtDevice) {
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected));
            baseView.updateData("initialization", "0.00");
            BluetoothDevice btDevice = vtDevice.getBtDevice();
            SPUtil.put(BluetoothConstants.SP.SP_SAVE_WEIGHT, btDevice.getName() + "," + btDevice.getAddress());
        }

        @Override
        public void onDeviceDisconnected(VTDevice vtDevice) {
        }

        @Override
        public void onDeviceServiceDiscovered(VTDevice vtDevice) {
            WeightSimaidePresenter.this.device = (VTDeviceScale) vtDevice;
            WeightSimaidePresenter.this.device.setScaleDataListener(mDataListener);
        }

        @Override
        public void onDevicePaired(VTDevice vtDevice) {
        }

        @Override
        public void onScanStop() {
        }

        @Override
        public void onDeviceAdvDiscovered(VTDevice device) {
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected));
            baseView.updateData("initialization", "0.00");
            BluetoothDevice btDevice = device.getBtDevice();
            SPUtil.put(BluetoothConstants.SP.SP_SAVE_WEIGHT, btDevice.getName() + "," + btDevice.getAddress());
            //连接成功 然后 给广播称设置数据监听
            WeightSimaidePresenter.this.device = (VTDeviceScale) device;
            WeightSimaidePresenter.this.device.setScaleDataListener(mDataListener);
        }

        @Override
        public void onDeviceAdvDisappeared(VTDevice device) {
            //广播称断开链接
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_disconnected));
        }
    };

    //广播称数据监听器
    private VTDeviceScale.VTDeviceScaleListener mDataListener = new VTDeviceScale.VTDeviceScaleListener() {
        /**
         * 这个方法 onDataAvailable() 会有两个地方调用
         *
         * 一、是动态体重数据时会调用 即scaleData.getCode()==200
         * 这时应该把个人信息 userJson (一个 JSONObject 对象) 传给 device，
         * 即device.setScaleUserInfo(userJson);【这个是必须的 否则不会返回体质数据】
         * userJson = {"height":170,"age":27,"gender":0}
         * height : 身高/cm
         * age : 年龄/岁
         * gender :性别/ 0-男, 1-女, 2-男运动员, 3-女运动员
         *
         * 然后会再次调用此方法 返回体质数据
         *
         * @param response json字符串 格式:{'code': 0, 'msg': 'success', 'details': data}
         *
         */
        @Override
        public void onDataAvailable(String response) {
            if (!TextUtils.isEmpty(response)) {
                SimaideBodyInfo scaleInfo = new Gson().fromJson(response, SimaideBodyInfo.class);
                if (scaleInfo.getCode() == 200) {
                    float weight = scaleInfo.getDetails().getWeight();
                    baseView.updateData("result", "result", String.format("%.2f", weight));
                }
            }
        }

        @Override
        public void onRssiReceived(int rssi) {
            super.onRssiReceived(rssi);
            //蓝牙信号强度
        }
    };

    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (manager != null) {
            manager.releaseBleManager();
        }
        manager = null;
        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
    }
}
