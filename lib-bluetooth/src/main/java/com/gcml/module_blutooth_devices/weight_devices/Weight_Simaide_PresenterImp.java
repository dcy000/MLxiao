package com.gcml.module_blutooth_devices.weight_devices;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
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
 * name:dr01
 * mac:ED:67:27:64:6A:20
 */
public class Weight_Simaide_PresenterImp extends BaseBluetoothPresenter {
    private static final String TAG = "Weight_Simaide";
    /**
     * 我司专属的KEY,由厂家直接提供
     */
    private static final String KEY = "TU1JA3D0ZI078UCC";
    private VTDeviceManager manager;
    private VTDeviceScale device;


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


        manager = VTDeviceManager.getInstance();
        manager.setKey(KEY);
        manager.startBle(baseContext);

        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                manager.setDeviceManagerListener(deviceManagerListener);
            }
        },1000);

        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchDevices();
            }
        }, 1000);
    }

    @Override
    public void searchDevices() {

        ArrayList<VTModelIdentifier> list = new ArrayList<>();
        list.add(new VTModelIdentifier(
                VTModelIdentifier.VT_PROTOCOL_VERSION_STANDARD,
                VTModelIdentifier.VT_DEVICE_TYPE_VSCALE,
                VTModelIdentifier.VT_VSCALE_FAT6,
                (byte) -1));
        manager.startScan(30, list);

    }


    //VTDeviceManager 监听器
    VTDeviceManager.VTDeviceManagerListener deviceManagerListener = new VTDeviceManager.VTDeviceManagerListener() {
        @Override
        public void onInited() {
            Log.e(TAG, "onInited: ");
        }

        @Override
        public void onDeviceDiscovered(VTDevice vtDevice) {
            Log.e(TAG, "onDeviceDiscovered: ");
        }

        @Override
        public void onDeviceConnected(VTDevice vtDevice) {
            Log.e(TAG, "onDeviceConnected: ");
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected));
            baseView.updateData("initialization", "0.00");
            BluetoothDevice btDevice = vtDevice.getBtDevice();
            SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, btDevice.getName() + "," + btDevice.getAddress());
        }

        @Override
        public void onDeviceDisconnected(VTDevice vtDevice) {
            Log.e(TAG, "onDeviceDisconnected: ");
        }

        @Override
        public void onDeviceServiceDiscovered(VTDevice vtDevice) {
            Log.e(TAG, "onDeviceServiceDiscovered: ");
            Weight_Simaide_PresenterImp.this.device = (VTDeviceScale) device;
            Weight_Simaide_PresenterImp.this.device.setScaleDataListener(mDataListener);
        }

        @Override
        public void onDevicePaired(VTDevice vtDevice) {
            Log.e(TAG, "onDevicePaired: ");
        }

        @Override
        public void onScanStop() {
            Log.e(TAG, "onScanStop: ");
        }

        @Override
        public void onDeviceAdvDiscovered(VTDevice device) {
            Log.e(TAG, "onDeviceAdvDiscovered: ");
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected));
            baseView.updateData("initialization", "0.00");
            BluetoothDevice btDevice = device.getBtDevice();
            SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, btDevice.getName() + "," + btDevice.getAddress());
            //连接成功 然后 给广播称设置数据监听
            Weight_Simaide_PresenterImp.this.device = (VTDeviceScale) device;
            Weight_Simaide_PresenterImp.this.device.setScaleDataListener(mDataListener);
        }

        @Override
        public void onDeviceAdvDisappeared(VTDevice device) {
            //广播称断开链接
            Log.e(TAG, "onDeviceAdvDisappeared: ");
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
            Log.e(TAG, "onDataAvailable: " + response);
            if (!TextUtils.isEmpty(response)) {
                SimaideBodyInfo scaleInfo = new Gson().fromJson(response, SimaideBodyInfo.class);
                if (scaleInfo.getCode() == 200) {
                    float weight = scaleInfo.getDetails().getWeight();
                    baseView.updateData(String.format("%.2f", weight));
                }
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
