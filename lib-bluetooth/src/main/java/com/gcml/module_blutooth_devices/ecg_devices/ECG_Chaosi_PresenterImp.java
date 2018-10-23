package com.gcml.module_blutooth_devices.ecg_devices;

import android.os.CountDownTimer;
import android.util.Log;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 超思心电设备
 * name:A12-B
 * mac:08:7C:BE:33:30:9E
 */
public class ECG_Chaosi_PresenterImp extends BaseBluetoothPresenter {
    private static final UUID Service_UUID = UUID.fromString("0000fee9-0000-1000-8000-00805f9b34fb");
    private static final UUID Characteristic_UUID_Write = UUID.fromString("d44bc439-abfd-45a2-b575-925416129600");
    private static final UUID Characteristic_UUID_1 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129601");
    private static final UUID Characteristic_UUID_2 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129602");
    private static final UUID Characteristic_UUID_3 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129603");
    private static final UUID Characteristic_UUID_4 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129604");
    private static final UUID Characteristic_UUID_5 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129605");
    private static final UUID Characteristic_UUID_6 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129606");
    private static final UUID Characteristic_UUID_7 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129607");
    private static String TAG = "ECG_Chaosi2_PresenterImp";
    private byte[] cacheBytes;
    private List<byte[]> realtimeDatas;
    private List<Integer> pointDatas;
    private WeakHandler weakHandler;

    private byte[] one = new byte[17];
    private byte[] two = new byte[17];
    private byte[] three = new byte[17];
    private byte[] four = new byte[17];
    private byte[] oneCache = new byte[3];
    private byte[] twoCache = new byte[6];
    private byte[] threeCache = new byte[9];

    private TimeCount timeCount;
    //是不是第一次接收到数据，用于控制倒计时的开始
    private boolean isFirstReceivedData = true;
    //是不是测量结束，用于控制倒计时的结束
    private static boolean isMeasureEnd = false;

    public ECG_Chaosi_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment,discoverSetting);
        weakHandler = new WeakHandler();
        timeCount = new TimeCount(30000, 1000, fragment);
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean is) {
        Logg.e(ECG_Chaosi_PresenterImp.class, "连接成功");
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_ECG,targetName+","+address);
        realtimeDatas = new ArrayList<>();
        pointDatas = new ArrayList<>();
        //7个通道必须依次打开 否则没有数据返回
        openOne();
        openTwo();
        openThree();
        openFour();
        openFive();
        openSix();
        openSeven();
//        writeCMD(ECG_Chaosi_CMD.cmdWithSumCheck(ECG_Chaosi_CMD.Initialization));
    }

    private void openOne() {
        BluetoothClientManager.getClient().notify(targetAddress, Service_UUID, Characteristic_UUID_1, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
//                Log.e(TAG, "onNotify1: " + ByteUtils.bytes2HexString(bytes));
                //55aa045d120613092b5800010000546d4544
                String s = ByteUtils.byteToString(bytes);
                if (s.startsWith("55AA04") && s.endsWith("4544")) {//结果
                    //测量结束，把isFirstReceivedData置初，方便重连之后再测量
                    isFirstReceivedData = true;
                    isMeasureEnd = true;
                    cacheBytes = null;
                    Logg.e(ECG_Chaosi_PresenterImp.class, ByteUtils.byteToString(bytes));
                    Logg.e(ECG_Chaosi_PresenterImp.class, "心率：" + ((bytes[9] & 0xff) + ((bytes[10] & 0xff) << 8)));
                    /**
                     *
                     20=波形异常
                     21=信号差，请重新测量
                     84=波形正常
                     1＝V FIB；//室颤
                     2＝A FIB；//房颤
                     3＝ASYSTOLE；//停博
                     4＝TACHY；//心动过快  
                     5＝VTACHY；//室性心动过速
                     6＝BRADY；//心动过缓
                     7＝MISSBEAT；//漏搏
                     8＝FREQPVC； //频发室早
                     9＝RUNPVC；//连发室早
                     10＝TRIGEMIN；//三联律
                     11＝BIGEMINY；//二联律
                     12＝R On T ； //R on T （严重室早的一种）
                     13＝INTERPVC；//插入室早
                     14＝COUPLET；//成对室早
                     15＝ PVC ；//室早
                     */
                    Logg.e(ECG_Chaosi_PresenterImp.class, "心律异常标志：" + bytes[14]);

                    //开始解析所有数据
//                    resolveDatas();
//                    display();
                    return;
                }
                if (bytes.length == 20) {//20个字节的数据表示是实时测量数据
                    if (isFirstReceivedData) {
                        isFirstReceivedData = false;
                        isMeasureEnd=false;
                        timeCount.start();
                    }
//                    cacheBytes = new byte[68];
//                    System.arraycopy(bytes, 0, cacheBytes, 0, 20);
                    System.arraycopy(bytes, 0, one, 0, 17);
                    System.arraycopy(bytes, 17, oneCache, 0, 3);
                    if (!isMeasureEnd) {
                        baseView.updateData(ByteUtils.byteToString(one));
                    }
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openOne " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openTwo() {
        BluetoothClientManager.getClient().notify(targetAddress, Service_UUID, Characteristic_UUID_2, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
//                Log.e(TAG, "onNotify2: " + ByteUtils.bytes2HexString(bytes));
//                if (cacheBytes != null) {
//                    System.arraycopy(bytes, 0, cacheBytes, 20, 20);
//                }
                System.arraycopy(oneCache, 0, two, 0, 3);
                System.arraycopy(bytes, 0, two, 3, 14);
                System.arraycopy(bytes, 14, twoCache, 0, 6);
                if (!isMeasureEnd) {
                    baseView.updateData(com.inuker.bluetooth.library.utils.ByteUtils.byteToString(two));
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openTwo " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openThree() {
        BluetoothClientManager.getClient().notify(targetAddress, Service_UUID, Characteristic_UUID_3, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
//                Log.e(TAG, "onNotify3: " + ByteUtils.bytes2HexString(bytes));
//                if (cacheBytes != null) {
//                    System.arraycopy(bytes, 0, cacheBytes, 40, 20);
//                }
                System.arraycopy(twoCache, 0, three, 0, 6);
                System.arraycopy(bytes, 0, three, 6, 11);
                System.arraycopy(bytes, 11, threeCache, 0, 9);
                if (!isMeasureEnd) {
                    baseView.updateData(com.inuker.bluetooth.library.utils.ByteUtils.byteToString(three));
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openThree " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openFour() {
        BluetoothClientManager.getClient().notify(targetAddress, Service_UUID, Characteristic_UUID_4, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, final byte[] bytes) {
//                Log.e(TAG, "onNotify4: " + ByteUtils.bytes2HexString(bytes));
//                if (cacheBytes != null) {
//                    System.arraycopy(bytes, 0, cacheBytes, 60, 8);
//                    weakHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            fragment.updateData(com.inuker.bluetooth.library.utils.ByteUtils.byteToString(cacheBytes));
//                        }
//                    });
//                    realtimeDatas.add(cacheBytes);
//                }
                System.arraycopy(threeCache, 0, four, 0, 9);
                System.arraycopy(bytes, 0, four, 9, 8);
                if (!isMeasureEnd) {
                    baseView.updateData(com.inuker.bluetooth.library.utils.ByteUtils.byteToString(four));
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openFour " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openFive() {
        BluetoothClientManager.getClient().notify(targetAddress, Service_UUID, Characteristic_UUID_5, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {//有时候数据会从该通道返回
//                Log.e(TAG, "onNotify5: " + ByteUtils.bytes2HexString(bytes));
//                if (cacheBytes != null) {
//                    System.arraycopy(bytes, 0, cacheBytes, 60, 8);
//                    weakHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            fragment.updateData(com.inuker.bluetooth.library.utils.ByteUtils.byteToString(cacheBytes));
//                        }
//                    });
//                    realtimeDatas.add(cacheBytes);
//                }

                System.arraycopy(threeCache, 0, four, 0, 9);
                System.arraycopy(bytes, 0, four, 9, 8);
                if (!isMeasureEnd) {
                    baseView.updateData(com.inuker.bluetooth.library.utils.ByteUtils.byteToString(four));
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openFive " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    //
    private void openSix() {
        BluetoothClientManager.getClient().notify(targetAddress, Service_UUID, Characteristic_UUID_6, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Log.e(TAG, "onNotify6: " + ByteUtils.byteToString(bytes));
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openSix " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openSeven() {
        BluetoothClientManager.getClient().notify(targetAddress, Service_UUID, Characteristic_UUID_7, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Log.e(TAG, "onNotify7: " + ByteUtils.byteToString(bytes));
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openSeven " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void display() {
        if (pointDatas.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < pointDatas.size(); i++) {
                buffer.append(pointDatas.get(i) + "\t\t");
                if (i > 0 && i % 15 == 0) {
                    buffer.append("\n");
                }
            }
//            Log.e(TAG, "display:点数据 \n" + buffer.toString());
            Log.e(TAG, "display:点数据 \n" + pointDatas.size());
        } else {
            Log.e(TAG, "display: 点数据为null");
        }

    }

    private void resolveDatas() {
        if (realtimeDatas.size() != 0) {
            for (byte[] rel : realtimeDatas) {
                pointDatas.add((((rel[6] & 0xff) << 2) + (((rel[10] & 0xff) >> 6) & 0x3)));
                pointDatas.add(((rel[7] & 0xff) << 2) + (((rel[10] & 0xff) >> 4) & 0x3));
                pointDatas.add(((rel[8] & 0xff) << 2) + (((rel[10] & 0xff) >> 2) & 0x3));
                pointDatas.add(((rel[9] & 0xff) << 2) + ((rel[10] & 0xff) & 0x3));

                pointDatas.add(((rel[23] & 0xff) << 2) + (((rel[27] & 0xff) >> 6) & 0x3));
                pointDatas.add(((rel[24] & 0xff) << 2) + (((rel[27] & 0xff) >> 4) & 0x3));
                pointDatas.add(((rel[25] & 0xff) << 2) + (((rel[27] & 0xff) >> 2) & 0x3));
                pointDatas.add(((rel[26] & 0xff) << 2) + ((rel[27] & 0xff) & 0x3));

                pointDatas.add(((rel[40] & 0xff) << 2) + (((rel[44] & 0xff) >> 6) & 0x3));
                pointDatas.add(((rel[41] & 0xff) << 2) + (((rel[44] & 0xff) >> 4) & 0x3));
                pointDatas.add(((rel[42] & 0xff) << 2) + (((rel[44] & 0xff) >> 2) & 0x3));
                pointDatas.add(((rel[43] & 0xff) << 2) + ((rel[44] & 0xff) & 0x3));

                pointDatas.add(((rel[57] & 0xff) << 2) + (((rel[61] & 0xff) >> 6) & 0x3));
                pointDatas.add(((rel[58] & 0xff) << 2) + (((rel[61] & 0xff) >> 4) & 0x3));
                pointDatas.add(((rel[59] & 0xff) << 2) + (((rel[61] & 0xff) >> 2) & 0x3));
                pointDatas.add(((rel[60] & 0xff) << 2) + ((rel[61] & 0xff) & 0x3));
            }
        } else {
            Log.e(TAG, "resolveDatas: 蓝牙数据是空的");
        }
    }

    static class TimeCount extends CountDownTimer {
        private IView fragment;

        TimeCount(long millisInFuture, long countDownInterval, IView fragment) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.fragment = fragment;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
//            StringBuffer buffer = new StringBuffer();
//            for (int i = 0; i < points.size(); i++) {
//                buffer.append(points.get(i) + ",");
//
//            }
//            Logg.e(ECG_BoSheng_PresenterImp.class, buffer.toString());
            isMeasureEnd = true;
            fragment.updateData("tip", "测量结束");

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
//            Logg.e(ECG_BoSheng_PresenterImp.class, millisUntilFinished / 1000 + "");
            fragment.updateData("tip", "距离测量结束还有" + millisUntilFinished / 1000 + "s");
        }
    }
}
