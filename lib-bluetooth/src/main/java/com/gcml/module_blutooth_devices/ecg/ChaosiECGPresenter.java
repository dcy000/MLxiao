package com.gcml.module_blutooth_devices.ecg;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.CountDownTimer;
import android.support.v4.app.SupportActivity;
import android.util.Log;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.UUID;

public class ChaosiECGPresenter implements LifecycleObserver {
    private SupportActivity activity;
    private IBluetoothView baseView;
    private String name;
    private String address;

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
    DetectionData detectionData = new DetectionData();

    public ChaosiECGPresenter(SupportActivity activity, IBluetoothView baseView, String name, String address) {
        this.activity = activity;
        this.baseView = baseView;
        this.name = name;
        this.address = address;
        timeCount = new TimeCount(30000, 1000, baseView, detectionData);
        bluetoothNotify();
    }

    private void bluetoothNotify() {
        //7个通道必须依次打开 否则没有数据返回
        openOne();
        openTwo();
        openThree();
        openFour();
        openFive();
        openSix();
        openSeven();
    }

    private void openOne() {
        BluetoothStore.getClient().notify(address, Service_UUID, Characteristic_UUID_1, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
//                Log.e(TAG, "onNotify1: " + ByteUtils.bytes2HexString(bytes));
                //55aa045d120613092b5800010000546d4544
                String s = ByteUtils.byteToString(bytes);
                if (s.startsWith("55AA04") && s.endsWith("4544")) {//结果
                    //测量结束，把isFirstReceivedData置初，方便重连之后再测量
                    isFirstReceivedData = true;
                    isMeasureEnd = true;
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
                    return;
                }
                if (bytes.length == 20) {//20个字节的数据表示是实时测量数据
                    if (isFirstReceivedData) {
                        isFirstReceivedData = false;
                        isMeasureEnd = false;
                        timeCount.start();
                    }
                    System.arraycopy(bytes, 0, one, 0, 17);
                    System.arraycopy(bytes, 17, oneCache, 0, 3);
                    if (!isMeasureEnd) {
                        detectionData.setInit(false);
                        detectionData.setEcgData(one);
                        baseView.updateData(detectionData);
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
        BluetoothStore.getClient().notify(address, Service_UUID, Characteristic_UUID_2, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                System.arraycopy(oneCache, 0, two, 0, 3);
                System.arraycopy(bytes, 0, two, 3, 14);
                System.arraycopy(bytes, 14, twoCache, 0, 6);
                if (!isMeasureEnd) {
                    detectionData.setInit(false);
                    detectionData.setEcgData(two);
                    baseView.updateData(detectionData);
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openTwo " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openThree() {
        BluetoothStore.getClient().notify(address, Service_UUID, Characteristic_UUID_3, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                System.arraycopy(twoCache, 0, three, 0, 6);
                System.arraycopy(bytes, 0, three, 6, 11);
                System.arraycopy(bytes, 11, threeCache, 0, 9);
                if (!isMeasureEnd) {
                    detectionData.setInit(false);
                    detectionData.setEcgData(three);
                    baseView.updateData(detectionData);
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openThree " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openFour() {
        BluetoothStore.getClient().notify(address, Service_UUID, Characteristic_UUID_4, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, final byte[] bytes) {
                System.arraycopy(threeCache, 0, four, 0, 9);
                System.arraycopy(bytes, 0, four, 9, 8);
                if (!isMeasureEnd) {
                    detectionData.setInit(false);
                    detectionData.setEcgData(four);
                    baseView.updateData(detectionData);
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openFour " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openFive() {
        BluetoothStore.getClient().notify(address, Service_UUID, Characteristic_UUID_5, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {//有时候数据会从该通道返回
                System.arraycopy(threeCache, 0, four, 0, 9);
                System.arraycopy(bytes, 0, four, 9, 8);
                if (!isMeasureEnd) {
                    detectionData.setInit(false);
                    detectionData.setEcgData(four);
                    baseView.updateData(detectionData);
                }
            }

            @Override
            public void onResponse(int i) {
                Log.e(TAG, "openFive " + (i == 0 ? "成功" : "失败"));
            }
        });
    }

    private void openSix() {
        BluetoothStore.getClient().notify(address, Service_UUID, Characteristic_UUID_6, new BleNotifyResponse() {
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
        BluetoothStore.getClient().notify(address, Service_UUID, Characteristic_UUID_7, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
            }

            @Override
            public void onResponse(int i) {
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {

    }

    static class TimeCount extends CountDownTimer {
        private IBluetoothView fragment;
        private DetectionData detection;

        TimeCount(long millisInFuture, long countDownInterval, IBluetoothView fragment, DetectionData detectionData) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.fragment = fragment;
            this.detection = detectionData;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isMeasureEnd = true;
            detection.setInit(false);
            detection.setEcgData(null);
            detection.setEcgTips("测量结束");
            fragment.updateData(detection);

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            detection.setInit(false);
            detection.setEcgData(null);
            detection.setEcgTips("距离测量结束还有" + millisUntilFinished / 1000 + "s");
            fragment.updateData(detection);
        }
    }
}
