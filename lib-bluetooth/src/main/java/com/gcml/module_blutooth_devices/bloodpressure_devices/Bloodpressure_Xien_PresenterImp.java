package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.common.utils.thread.ThreadUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/22 11:02
 * created by: gzq
 * description: 西恩血压计
 * name:Dual-SPP
 * mac:88:1B:99:09:94:1F
 */
public class Bloodpressure_Xien_PresenterImp extends BaseBluetoothPresenter {
    /**
     * 连接UUID
     */
    private static final String CONNECT_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothSocket socket;
    private boolean isSuccess;
    private boolean isEnd;
    private StartDeviceTask startDeviceTask;
    public List<byte[]> listNum = new ArrayList<>();
    private WeakHandler weakHandler = new WeakHandler(new Handler.Callback() {
        @SuppressLint("MissingPermission")
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //TODO 设置显示点击查看按钮并上传数据
                    break;
                // 测量成功
                case 1:
                    break;
                // 测量失败
                case 2:
                    break;
                // 设备超时
                case 3:
                    baseView.updateState("设备连接超时");
                    break;
                // 充不上气
                case 4:
                    baseView.updateState("设备充不上气");
                    break;
                // 测量中发生错误
                case 5:
                    baseView.updateState("设备检测发生错误");
                    break;
                // 血压计低电量
                case 6:
                    baseView.updateState("设备电量不足");
                    break;
                case 7:
                    break;
                // 测量中
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    //经典蓝牙测量中数据

                    byte[] result2 = (byte[]) msg.obj;
                    int num;
                    num = (result2[5] & 0xff) | (result2[6] << 8 & 0xff00);
                    baseView.updateData(String.valueOf(num));
                    break;
                case 13:
                    //经典蓝牙测量结果
                    byte[] res = (byte[]) msg.obj;
                    int high = (res[5] & 0xff) + 30;
                    int pulse = res[4] & 0xff;
                    int i = res[6];
                    int low;
                    if (((i & 0XFF) > 0) || (i & 0XFF) < 256) {
                        low = (i & 0XFF) + 30;
                    } else {
                        low = (((byte) i) & 0XFF) + 1 + 0xff + 30;
                    }
                    baseView.updateData(high + "", low + "", pulse + "");
                    break;
                case 14:
                    if (BluetoothUtils.getBluetoothAdapter().isDiscovering()) {
                        BluetoothUtils.getBluetoothAdapter().cancelDiscovery();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public Bloodpressure_Xien_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }


    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        BluetoothDevice bluetoothDevice = device.device;
        connectDevice(bluetoothDevice);
        return false;
    }

    @SuppressLint("MissingPermission")
    private void connectDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return;
        }
        try {
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(CONNECT_UUID));
            if (!socket.isConnected()) {
                socket.connect();
            }
            if (socket.isConnected()) {
                //设备连接成功
                isSuccess = true;
                baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
                baseView.updateData("0", "0", "0");
            } else {
                isSuccess = false;
                socket.connect();
            }
            isEnd = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startMeasure() {
        if (startDeviceTask == null) {
            startDeviceTask = new StartDeviceTask();
        }
        ThreadUtils.executeByIo(startDeviceTask);
    }

    public class StartDeviceTask extends ThreadUtils.Task<Void> {

        @Nullable
        @Override
        public Void doInBackground() throws Throwable {
            // 发送启动命令
            OutputStream os;
            try {
                os = socket.getOutputStream();
                byte[] startDevice = {(byte) 0xff, (byte) 0xff, 0x05, 0x01, (byte) 0xfa};
                os.write(startDevice, 0, startDevice.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is;
            try {
                is = socket.getInputStream();
                // 定义一个数据，来接收数据
                byte[] buffer = new byte[255];
                // 定义int型变量，代表字节数
                int bytes;
                if (is == null) {
                    return null;
                }
                // 执行循环
                while (!isEnd) {
                    try {
                        if (socket != null) {
                            // 如果读取的字节数大于0，表示有数据存在
                            bytes = is.read(buffer);
                            if (bytes > 0 && bytes == 73) {
                                // 定义新的字符数据，长度为读取的数据的长度
                                byte[] byte_data = new byte[bytes];
                                // 通过循环将buffer里面的数据复制到byte_data数组中
                                for (int i = 0; i < bytes; i++) {
                                    byte_data[i] = buffer[i];
                                }
                                // 将byte_data数组转换成字符串
                                listNum.add(byte_data);
                                if (listNum != null) {
                                    byte[] result = listNum.get(0);
                                    if (result[2] == 73 && result[3] == 3) {
                                        // 测量返回成功关闭流
                                        try {
                                            is.close();
                                        } catch (IOException ae) {
                                            ae.printStackTrace();
                                        }
                                        isEnd = true;
                                        socket.close();
                                        Message msg = Message.obtain();
                                        msg.what = 13;
                                        msg.obj = result;
                                        weakHandler.sendMessage(msg);
                                        break;
                                    }
                                }
                            } else if (bytes > 0 && bytes == 6) {
                                // 定义新的字符数据，长度为读取的数据的长度
                                byte[] byte_data = new byte[bytes];
                                // 通过循环将buffer里面的数据复制到byte_data数组中
                                for (int i = 0; i < bytes; i++) {
                                    byte_data[i] = buffer[i];
                                }
                                if (byte_data[2] == 6 && byte_data[3] == 7) {
                                    try {
                                        is.close();
                                    } catch (IOException ae) {
                                        ae.printStackTrace();
                                    }
                                    isEnd = true;
                                    socket.close();
                                    if (byte_data[4] == 0) {
                                        weakHandler.sendEmptyMessage(4);
                                    } else if (byte_data[4] == 1) {
                                        weakHandler.sendEmptyMessage(5);
                                    } else if (byte_data[4] == 2) {
                                        weakHandler.sendEmptyMessage(6);
                                    } else {
                                        weakHandler.sendEmptyMessage(2);
                                    }
                                    break;
                                }
                            } else if (bytes > 0 && bytes == 10) {
                                // 定义新的字符数据，长度为读取的数据的长度
                                byte[] byte_data = new byte[bytes];
                                // 通过循环将buffer里面的数据复制到byte_data数组中
                                for (int i = 0; i < bytes; i++) {
                                    byte_data[i] = buffer[i];
                                }

                                Message msg = Message.obtain();
                                msg.what = 12;
                                msg.obj = byte_data;
                                weakHandler.sendMessage(msg);

                            } else if (bytes > 0 && bytes == 5) {
                                // 定义新的字符数据，长度为读取的数据的长度
                                byte[] byte_data = new byte[bytes];
                                // 通过循环将buffer里面的数据复制到byte_data数组中
                                for (int i = 0; i < bytes; i++) {
                                    byte_data[i] = buffer[i];
                                }
                                if (byte_data[3] == 4) {
                                    try {
                                        is.close();
                                    } catch (IOException ae) {
                                        ae.printStackTrace();
                                    }
                                    isEnd = true;
                                    socket.close();
                                    weakHandler.sendEmptyMessage(7);
                                }
                            }

                        }
                    } catch (IOException a) {
                        try {
                            is.close();
                        } catch (IOException ae) {
                            ae.printStackTrace();
                        }
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onSuccess(@Nullable Void result) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onFail(Throwable t) {

        }
    }

    @Override
    public void connectDevice(String macAddress) {

        BluetoothDevice remoteDevice = BluetoothUtils.getRemoteDevice(macAddress);
        connectDevice(remoteDevice);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isEnd = true;
        if (weakHandler != null) {
            weakHandler.removeCallbacksAndMessages(null);
            weakHandler = null;
        }
        if (listNum != null) {
            listNum.clear();
            listNum = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (startDeviceTask != null) {
            ThreadUtils.cancel(startDeviceTask);
            startDeviceTask = null;
        }
    }
}
