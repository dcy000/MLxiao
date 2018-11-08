package com.gcml.module_blutooth_devices.weight_devices;

import android.text.TextUtils;
import android.util.Log;

import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.google.gson.Gson;
import com.vtrump.vtble.VTDevice;
import com.vtrump.vtble.VTDeviceManager;
import com.vtrump.vtble.VTDeviceScale;
import com.vtrump.vtble.VTModelIdentifier;

import java.util.ArrayList;


/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/7 17:55
 * created by: gzq
 * description: TODO
 */
public class Weight_Simaide_PresenterImp extends BaseBluetoothPresenter {
    private static final String TAG = "Weight_Simaide_Presente";
    /**
     * 我司专属的KEY,由厂家直接提供
     */
    private static final String KEY = "TU1JA3D0ZI078UCC";
    private VTDeviceManager manager;
    private ArrayList<VTModelIdentifier> devices;

    public Weight_Simaide_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }


    @Override
    protected boolean isSelfDefined() {

        return true;
    }

    @Override
    public void checkBlueboothOpened() {
        super.checkBlueboothOpened();
        searchDevices();
    }

    @Override
    public void searchDevices() {
        devices = new ArrayList<>();
        manager = VTDeviceManager.getInstance();
        manager.setKey(KEY);
        manager.startBle(baseContext);
        manager.startScan(20, devices);

        manager.setDeviceManagerListener(deviceManagerListener);

        //停止扫描
        manager.stopScan();
    }

    private VTDeviceScale mDevice;
    //VTDeviceManager 监听器
    VTDeviceManager.VTDeviceManagerListener deviceManagerListener = new VTDeviceManager.VTDeviceManagerListener() {
        @Override
        public void onInited() {

        }

        @Override
        public void onDeviceDiscovered(VTDevice vtDevice) {
            Log.i(TAG, "onDeviceDiscovered: ");
        }

        @Override
        public void onDeviceConnected(VTDevice vtDevice) {
            Log.i(TAG, "onDeviceConnected: ");
        }

        @Override
        public void onDeviceDisconnected(VTDevice vtDevice) {
            Log.i(TAG, "onDeviceDisconnected: ");
        }

        @Override
        public void onDeviceServiceDiscovered(VTDevice vtDevice) {
            Log.i(TAG, "onDeviceServiceDiscovered: ");
        }

        @Override
        public void onDevicePaired(VTDevice vtDevice) {
            Log.i(TAG, "onDevicePaired: ");
        }

        @Override
        public void onScanStop() {
            Log.i(TAG, "onScanStop: ");
        }

        @Override
        public void onDeviceAdvDiscovered(VTDevice device) {
            Log.i(TAG, "onDeviceAdvDiscovered: ");
            //连接成功 然后 给广播称设置数据监听
            ((VTDeviceScale) device).setScaleDataListener(mDataListener);
            mDevice = (VTDeviceScale) device;
        }

        @Override
        public void onDeviceAdvDisappeared(VTDevice device) {
            //广播称断开链接
            Log.i(TAG, "onDeviceAdvDisappeared: ");
        }
    };

    //广播称数据监听器
    VTDeviceScale.VTDeviceScaleListener mDataListener = new VTDeviceScale.VTDeviceScaleListener() {
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
            Log.e(TAG, "onDataAvailable: " + response);
            if (!TextUtils.isEmpty(response)) {
                Gson gson = new Gson();
//                scaleData = gson.fromJson(response, ScaleData.class);
//                if (scaleData != null) {
//                    details = scaleData.getDetails();
//                    if (scaleData.getCode() == 200) {
//                        // 设置用户信息
//                        device.setScaleUserInfo(userObject);
//                    }
//                }
            }
        }

        @Override
        public void onRssiReceived(int rssi) {
            super.onRssiReceived(rssi);
            //蓝牙信号强度
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.releaseBleManager();
        }
        devices = null;
        manager = null;
    }
}
