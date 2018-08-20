package com.gcml.module_blutooth_devices.others;

import android.support.annotation.Nullable;
import android.util.Log;

import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.lib_utils.thread.ThreadUtils;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/18 13:59
 * created by:gzq
 * description:
 * 呼吸家肺部检查设备
 * name:B810229665
 * MAC:00:15:87:21:11:D9
 */
public class BreathHome_PresenterImp extends BaseBluetoothPresenter {
    private static final String TAG = "BreathHome_PresenterImp";
    //主服务
    private static final String targetServiceUUid = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000ffe1-0000-1000-8000-00805f9b34fb";
    //蓝牙设备发送的请求，其中包含蓝牙版本，渠道号等关键信息
    private byte[] deviceRequest = new byte[44];
    private BreathHomeRequestConnectBean requestConnectBean;
    private int num = 1;
    private boolean isCollected = false;
    private boolean isWriteRequestDataSuccess = false;
    private String deviceName = "B810229665";

    public BreathHome_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
        requestConnectBean = new BreathHomeRequestConnectBean();
    }

    @Override
    protected void connectSuccessed(final String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        Log.e(TAG, "connectSuccessed: ");
        BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        Log.e(TAG, "onNotify: " + value.length + "======》》》" + new String(value));
                        if (!isCollected) {
                            if (num == 1) {
                                System.arraycopy(value, 0, deviceRequest, 0, 20);
                                num++;
                            } else if (num == 2) {
                                System.arraycopy(value, 0, deviceRequest, 20, 20);
                                num++;
                            } else if (num == 3) {
                                System.arraycopy(value, 0, deviceRequest, 40, 4);
                                isCollected = true;
                                num = 1;
                            }
                        } else {
                            if (!isWriteRequestDataSuccess) {
                                isWriteRequestDataSuccess = true;
                                String s_requestData = new String(deviceRequest);
                                String[] split = s_requestData.split(",");
                                if (split.length == 7) {
                                    requestConnectBean.setActionHead(split[0]);
                                    requestConnectBean.setDeviceType(split[1]);
                                    requestConnectBean.setImei(split[2]);
                                    requestConnectBean.setBluetoothVersion(split[3]);
                                    requestConnectBean.setChannelNum(split[4]);
                                    requestConnectBean.setMsgLength(split[5]);
                                    requestConnectBean.setCheckCode(split[6]);
                                    Log.e(TAG, "onNotify: " + requestConnectBean.toString());
                                    writeRequestConnectData(address, requestConnectBean);
                                } else {
                                    Log.e(TAG, "doInBackground: 解析数据失败");
                                }
                            }
                        }

                    }

                    @Override
                    public void onResponse(int code) {
                        Log.e(TAG, "onResponse: " + ((code == 0) ? "成功" : "失败"));
                    }
                });


    }

    private String sex = "0";
    private String age = "25";
    private String height = "170";
    private String weight = "65";
    private String time = TimeUtils.getCurTimeString();

    private void writeRequestConnectData(String address, BreathHomeRequestConnectBean requestConnectBean) {
        StringBuffer requestConnect = new StringBuffer();
        //请求头+终端类型+IMEI+协议版本+渠道号+消息长度（20）+处理结果（成功1失败0）+性别（男0女1）+年龄（>4）
        // +身高（cm）+体重（kg）+更细标志位（不更新0，更新1）+当前时间（xxxx-xx-xx xx:xx:xx）+检验码（不校验0）+da
        requestConnect.append(
                requestConnectBean.getActionHead() + ","
                        + requestConnectBean.getDeviceType() + ","
                        + deviceName + ","
                        + requestConnectBean.getBluetoothVersion() + ","
                        + requestConnectBean.getChannelNum() + ","
                        + "20,"
                        + "1,"
                        + "234,"
                        + "1.23,"
                        + "1.24,"
                        + "0.01,"
                        + "-0.01,"
                        + "0.01,"
                        + sex + ","
                        + age + ","
                        + height + ","
                        + weight + ","
                        + "0,"
                        + time + ","
                        + "0,"
                        + "da");
        Log.e(TAG, "writeRequestConnectData:最终传参： " + requestConnect.toString());
        BluetoothClientManager.getClient().write(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), requestConnect.toString().getBytes(),
                new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        Log.e(TAG, "写入数据onResponse: " + (code == 0 ? "成功" : "失败"));
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestConnectBean = null;
    }
}
