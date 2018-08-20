package com.gcml.module_blutooth_devices.others;

import android.support.annotation.Nullable;
import android.util.Log;

import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.lib_utils.thread.ThreadUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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
    private BreathHomeResultBean resultBean;
    private int num = 1;
    private boolean isCollected = false;
    private boolean isWriteRequestDataSuccess = false;
    private boolean isRealConnectSuccess = false;
    private StringBuffer resultBuffer = new StringBuffer();
    private String deviceName = "B810229665";
    private String sex = "0";
    private String age = "25";
    private String height = "170";
    private String weight = "65";
    private String time = TimeUtils.getCurTimeString();

    public BreathHome_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting, String sex, String age, String height, String weight) {
        super(fragment, discoverSetting);
        requestConnectBean = new BreathHomeRequestConnectBean();
        resultBean = new BreathHomeResultBean();
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    @Override
    protected void connectSuccessed(final String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        Log.e(TAG, "onNotify: " + value.length + "======》》》" + new String(value));
                        //如果isCollected=false,说明是收到的连接请求；如果isCollected=true,则是测量数据
                        //警告：需要注意的是如果用户在没有连接的情况下测量过，name可能第一次同步的是历史测量数据
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
                                    Log.e(TAG, "解析请求数据失败");
                                }
                            } else {//接收测量数据
                                if (isRealConnectSuccess) {
                                    String sResult = new String(value);
                                    resultBuffer.append(sResult);
                                    //最后一包数据少于20字节，说明所有数据接收完了
                                    if (value.length < 20) {
                                        Log.e(TAG, "onNotify: 最后的结果字段：" + resultBuffer.toString());
                                        String[] split = resultBuffer.toString().split(",");
                                        if (split.length == 23) {
                                            resultBean.setActionHead(split[0]);
                                            resultBean.setDeviceType(split[1]);
                                            resultBean.setImei(split[2]);
                                            resultBean.setBluetoothVersion(split[3]);
                                            resultBean.setChannelNum(split[4]);
                                            resultBean.setMsgLength(split[5]);
                                            resultBean.setB1Save(split[6]);
                                            resultBean.setSendN(split[7]);
                                            resultBean.setB1SaveTime(split[8]);
                                            resultBean.setSex(split[9]);
                                            resultBean.setAge(split[10]);
                                            resultBean.setHeight(split[11]);
                                            resultBean.setWeight(split[12]);
                                            resultBean.setPef(split[13]);
                                            resultBean.setPev1(split[14]);
                                            resultBean.setFvc(split[15]);
                                            resultBean.setMef75(split[16]);
                                            resultBean.setMef50(split[17]);
                                            resultBean.setMef25(split[18]);
                                            resultBean.setMmef(split[19]);
                                            resultBean.setReserve(split[20]);
                                            resultBean.setCurve(split[21]);
                                            resultBean.setCheckCode(split[22]);
                                            String result = new Gson().toJson(resultBean);
                                            JSONObject object = null;
                                            try {
                                                object = new JSONObject(result);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            baseView.updateData(object.toString());
                                            resultBuffer.setLength(0);
                                        } else {
                                            Log.e(TAG, "解析结果数据失败");
                                            resultBuffer.setLength(0);
                                        }

                                    }
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
        byte[] connectBytes = requestConnect.toString().getBytes();
        List<byte[]> decomposeData = decomposeData(connectBytes);
        readWrite(address, decomposeData);
    }

    /**
     * 数据分包
     *
     * @param datas
     * @return
     */
    private List<byte[]> decomposeData(byte[] datas) {
        List<byte[]> bytesList = new ArrayList<>();
        //默认剩余全部
        byte[] surplus = datas;
        //将数据分包，每20个字节一包
        while (surplus != null) {
            byte[] cacheByte;
            byte[] newByte;
            if (surplus.length >= 20) {
                //先复制前20个字节到新数组，然后将剩余的复制到缓存数组，最后将缓存数组重新赋值给剩余数组
                cacheByte = new byte[surplus.length - 1];
                newByte = new byte[20];
                System.arraycopy(surplus, 0, newByte, 0, 20);
                System.arraycopy(surplus, 20, cacheByte, 0, surplus.length - 20);
                surplus = cacheByte;
            } else {
                //如果是剩余数组不足20个字节，直接将剩余数组作为新数组加入集合即可
                newByte = surplus;
                surplus = null;
            }
            bytesList.add(newByte);
        }
        return bytesList;
    }

    private int writePosition = 0;

    /**
     * 分包写入数据
     *
     * @param address
     * @param list
     */
    private void readWrite(final String address, final List<byte[]> list) {
        if (writePosition < list.size()) {
            BluetoothClientManager.getClient().write(address, UUID.fromString(targetServiceUUid),
                    UUID.fromString(targetCharacteristicUUid), list.get(writePosition),
                    new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            writePosition++;
                            //如果某一次写入数据出错了,则不再写入
                            if (code == 0) {
                                readWrite(address, list);
                            } else {
                                baseView.updateState("连接设备失败");
                            }
                        }
                    });
        } else {
            baseView.updateState("请开始吹气");
            isRealConnectSuccess = true;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestConnectBean = null;
    }
}
