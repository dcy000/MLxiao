package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.ihealth.communication.control.Bp550BTControl;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 九安血压仪
 * name:KN-550BT 110(10天试用，之后需要授权)
 * F4:5E:AB:7D:79:21
 */
@SuppressLint("LongLogTag")
public class Bloodpressure_KN550_PresenterImp extends BaseBluetoothPresenter {
    private final int callbackId;
    private final String userName = "";
    private final String clientId = "";
    private final String clientSecret = "";
    //    private IView fragment;
    private boolean isConnected = false;
    private boolean isSearching = true;
    private Bp550BTControl bp550BTControl;

    public Bloodpressure_KN550_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
//        this.fragment = fragment;
        //初始化SDK
        iHealthDevicesManager.getInstance().init(fragment.getThisContext());
        callbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
        //初始化成功后先授权
        iHealthDevicesManager.getInstance().sdkUserInAuthor(fragment.getThisContext(), userName, clientId, clientSecret, callbackId);

        int discoverType = discoverSetting.getDiscoverType();
        if (discoverType == IPresenter.DISCOVER_WITH_MAC) {
            String targetMac = discoverSetting.getTargetMac();
            if (!TextUtils.isEmpty(targetMac)) {
                iHealthDevicesManager.getInstance().addCallbackFilterForAddress(callbackId, targetMac);
            } else {
                iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(callbackId, iHealthDevicesManager.TYPE_550BT);
            }
        } else {
            iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(callbackId, iHealthDevicesManager.TYPE_550BT);
        }
        searchDevices();
    }


    @Override
    protected boolean isSelfDefined() {
        return true;
    }

    @Override
    public void searchDevices() {
        isSearching = true;
        iHealthDevicesManager.getInstance().startDiscovery(iHealthDevicesManager.DISCOVERY_BP550BT);
    }


    private com.ihealth.communication.manager.iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

        @Override
        public void onScanDevice(String mac, String deviceType, int rssi, Map manufactorData) {
            Logg.e(Bloodpressure_KN550_PresenterImp.class, "onScanDevice - mac:" + mac + " - deviceType:" + deviceType + " - rssi:" + rssi + " -manufactorData:" + manufactorData);
            iHealthDevicesManager.getInstance().connectDevice(userName, mac, iHealthDevicesManager.TYPE_550BT);
        }

        @Override
        public void onDeviceConnectionStateChange(String mac, String deviceType, int status, int errorID, Map manufactorData) {//status:0正在连接，1连接成功，2，连接断开，3连接失败，4重连
            Logg.e(Bloodpressure_KN550_PresenterImp.class, "mac:" + mac + " deviceType:" + deviceType + " status:" + status + " errorid:" + errorID + " -manufactorData:" + manufactorData);

            if (status == iHealthDevicesManager.DEVICE_STATE_CONNECTED) {//连接成功
                iHealthDevicesManager.getInstance().stopDiscovery();
                Logg.e(Bloodpressure_KN550_PresenterImp.class, "onDeviceConnectionStateChange: 连接成功");
                bp550BTControl = iHealthDevicesManager.getInstance().getBp550BTControl(mac);
                isSearching = false;
                bp550BTControl.getOfflineData();

                baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
                baseView.updateData("0", "0", "0");
                SPUtil.put(baseContext, SPUtil.SP_SAVE_BLOODPRESSURE, targetName + "," + mac);
            } else if (status == iHealthDevicesManager.DEVICE_STATE_CONNECTIONFAIL) {//连接失败
                Logg.e(Bloodpressure_KN550_PresenterImp.class, "onDeviceConnectionStateChange: 连接失败");
            } else if (status == iHealthDevicesManager.DEVICE_STATE_DISCONNECTED) {//连接断开
                Logg.e(Bloodpressure_KN550_PresenterImp.class, "连接断开");
                if (((Fragment) baseView).isAdded()) {
                    baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
                }
                if (!isDestroy) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //断开后尝试重连
                    iHealthDevicesManager.getInstance().connectDevice(userName, mac, iHealthDevicesManager.TYPE_550BT);
                }
            }
        }

        @Override
        public void onUserStatus(String username, int userStatus) {
            Logg.e(Bloodpressure_KN550_PresenterImp.class, "onUserStatus: " + username + userStatus);
        }

        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            if (action.equals("historicaldata_bp") && !TextUtils.isEmpty(message)) {
                try {
                    JSONObject object = new JSONObject(message);
                    JSONArray data = object.optJSONArray("data");
                    if (data.length() > 0) {
                        JSONObject jsonObject = data.getJSONObject(0);
                        int gaoya = jsonObject.optInt("sys");
                        int diya = jsonObject.optInt("dia");
                        int heartRate = jsonObject.optInt("heartRate");
                        Logg.e(Bloodpressure_KN550_PresenterImp.class, "onDeviceNotify: 高压：" + gaoya + ";低压：" + diya + "心率：" + heartRate);
                        baseView.updateData(gaoya + "", diya + "", heartRate + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onScanFinish() {
            Logg.e(Bloodpressure_KN550_PresenterImp.class, "onScanFinish: 搜索结束");
            if (isSearching && bp550BTControl == null) {
                searchDevices();
            }
        }

    };

    @Override
    public void onDestroy() {
        if (isSearching) {
            iHealthDevicesManager.getInstance().stopDiscovery();
        }
        if (isConnected && bp550BTControl != null) {
            bp550BTControl.disconnect();
        }
        iHealthDevicesManager.getInstance().unRegisterClientCallback(callbackId);
        iHealthDevicesManager.getInstance().destroy();
        iHealthDevicesCallback = null;
        super.onDestroy();
    }
}
