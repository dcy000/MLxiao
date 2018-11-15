package com.example.han.referralproject.measure;

import android.arch.lifecycle.LifecycleOwner;
import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.BreathHomeRequestConnectBean;
import com.example.han.referralproject.bean.BreathHomeResultBean;
import com.example.han.referralproject.util.BreathHomeUtils;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.google.gson.Gson;
import com.gzq.lib_bluetooth.BaseBluetooth;
import com.gzq.lib_bluetooth.BluetoothStore;
import com.gzq.lib_bluetooth.BluetoothType;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BreathHomePresenter extends BaseBluetooth {
    //主服务
    private static final String targetServiceUUid = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private final BreathHomeRequestConnectBean requestConnectBean;
    private final BreathHomeResultBean resultBean;
    private final BreathHomeActivity context;
    private int sex = 0;
    private int age = 25;
    private int height = 170;
    private int weight = 65;
    private static final String TAG = "BreathHomePresenter";

    //蓝牙设备发送的请求，其中包含蓝牙版本，渠道号等关键信息
    private byte[] deviceRequest = new byte[44];
    private int num = 1;
    private boolean isCollected = false;
    private boolean isWriteRequestDataSuccess = false;
    private boolean isRealConnectSuccess = false;
    private StringBuffer resultBuffer = new StringBuffer();
    private String time;
    private String deviceName = "B810229665";

    public BreathHomePresenter(LifecycleOwner owner, String bluetoothName, String bluetoothAddress, int sex, int age, int height, int weight) {
        super(owner);
        context = ((BreathHomeActivity) owner);
        requestConnectBean = new BreathHomeRequestConnectBean();
        resultBean = new BreathHomeResultBean();
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (!TextUtils.isEmpty(bluetoothAddress)) {
            connect(bluetoothAddress);
        } else {
            start(BluetoothType.BLUETOOTH_TYPE_BLE, bluetoothAddress, bluetoothName);
        }

    }

    @Override
    protected void noneFind() {

    }

    @Override
    protected void connectSuccessed(final String address) {
        LocalShared.getInstance(context).setBreathHomeMac(deviceName + "," + address);

        BluetoothStore.getInstance().getClient().notify(address, UUID.fromString(targetServiceUUid),
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
                                            resultBean.setFev1(split[14]);
                                            resultBean.setFvc(split[15]);
                                            resultBean.setMef75(split[16]);
                                            resultBean.setMef50(split[17]);
                                            resultBean.setMef25(split[18]);
                                            resultBean.setMmef(split[19]);
                                            resultBean.setReserve(split[20]);
                                            resultBean.setCurve(split[21]);
                                            resultBean.setCheckCode(split[22]);
                                            resultBean.setPercentPEF(BreathHomeUtils.percentPEF(sex, age, height, weight, Float.parseFloat(split[13])));
                                            resultBean.setPercentFEV1(BreathHomeUtils.percentFEV1(sex, age, height, weight, Float.parseFloat(split[14])));
                                            resultBean.setPercentFEV1_FVC(BreathHomeUtils.percentFEV1_FVC(sex, age, height, weight, Float.parseFloat(split[14]), Float.parseFloat(split[15])));
                                            String result = new Gson().toJson(resultBean);
                                            JSONObject object = null;
                                            try {
                                                object = new JSONObject(result);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            context.updateData(object.toString());
                                            resultBuffer.setLength(0);
                                            writeReceiveResultCallBackData(address, resultBean);
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

    //收到结果数据之后回传信息
    private void writeReceiveResultCallBackData(String address, BreathHomeResultBean resultBean) {
        //睡200ms
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuffer callback = new StringBuffer();
        callback.append(
                resultBean.getActionHead() + ","
                        + resultBean.getDeviceType() + ","
                        + resultBean.getImei() + ","
                        + resultBean.getBluetoothVersion() + ","
                        + resultBean.getChannelNum() + ","
                        + "11,"
                        + "1,"
                        + BreathHomeUtils.checkPEF(sex, age, height, weight, Float.parseFloat(resultBean.getPef())) + ","//pef危险程度:-1 无效 0 正常1 警告2 危险
                        + BreathHomeUtils.checkFEV1(sex, age, height, weight, Float.parseFloat(resultBean.getFev1())) + ","//fvc危险程度  -1 无效0 正常1 轻2 中3 重
                        + BreathHomeUtils.checkFEV1_FVC(sex, age, height, weight, Float.parseFloat(resultBean.getFev1()), Float.parseFloat(resultBean.getFvc())) + ","//fev1危险程度 -1 无效0 正常1 轻2 中3 重4 极重
                        + "0,"
                        + "da"
        );
        Log.e(TAG, "writeRequestConnectData:最终传参： " + resultBean.toString());
        byte[] bytes = callback.toString().getBytes();
        List<byte[]> bytes1 = decomposeData(bytes);
        readyWrite(address, bytes1, true);
    }

    private void writeRequestConnectData(String address, BreathHomeRequestConnectBean requestConnectBean) {
        StringBuffer requestConnect = new StringBuffer();
        //请求头+终端类型+IMEI+协议版本+渠道号+消息长度（20）+处理结果（成功1失败0）+性别（男0女1）+年龄（>4）
        // +身高（cm）+体重（kg）+更细标志位（不更新0，更新1）+当前时间（xxxx-xx-xx xx:xx:xx）+检验码（不校验0）+da
        Log.e("PEF默认值计算：", ((int) BreathHomeUtils.drv_pred_pef(sex, age, height, weight)) + "");
        requestConnect.append(
                requestConnectBean.getActionHead() + ","
                        + requestConnectBean.getDeviceType() + ","
                        + deviceName + ","
                        + requestConnectBean.getBluetoothVersion() + ","
                        + requestConnectBean.getChannelNum() + ","
                        + "20,"
                        + "1,"
                        + ((int) BreathHomeUtils.drv_pred_pef(sex, age, height, weight)) + ","//默认234
                        + BreathHomeUtils.drv_pred_fev1(sex, age, height, weight) + ","//默认1.23
                        + BreathHomeUtils.drv_pred_fvc(sex, age, height, weight) + ","//默认值1.24
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
        readyWrite(address, decomposeData, false);
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
    private void readyWrite(final String address, final List<byte[]> list, final boolean isWriteResultCall) {
        if (writePosition < list.size()) {
            BluetoothStore.getInstance().getClient().write(address, UUID.fromString(targetServiceUUid),
                    UUID.fromString(targetCharacteristicUUid), list.get(writePosition),
                    new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            writePosition++;
                            //如果某一次写入数据出错了,则不再写入
                            if (code == 0) {
                                readyWrite(address, list, isWriteResultCall);
                            } else {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastTool.showShort("连接设备失败");
                                        MLVoiceSynthetize.startSynthesize(MyApplication.getInstance(), "连接设备失败", false);
                                    }
                                });

                            }
                        }
                    });
        } else {
            //数据写完之后初始化状态
            writePosition = 0;
            if (!isWriteResultCall) {
                isRealConnectSuccess = true;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastTool.showShort("设备已连接");
                        MLVoiceSynthetize.startSynthesize(MyApplication.getInstance(), "设备已连接", false);
                    }
                });
            }

        }

    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected() {

    }
}
