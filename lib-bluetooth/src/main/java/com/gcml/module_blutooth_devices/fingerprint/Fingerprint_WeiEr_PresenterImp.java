package com.gcml.module_blutooth_devices.fingerprint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.display.ImageUtils;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.wellcom.verify.FT312UART;
import com.wellcom.verify.GfpAlgorithmApp;
import com.wellcom.verify.GfpInterface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * 精驰指纹仪
 * name:zjwellcom
 * mac:88:1B:99:0D:77:71
 */
public class Fingerprint_WeiEr_PresenterImp implements LifecycleObserver{
    /**
     * 第一次连接设备
     */
    private static final String ACTION_FIRST_CONNECT = "first_connect";
    /**
     * 录入指纹
     */
    private static final String ACTION_COLLECT_FINGERPRINT = "collect_fingerprint";
    /**
     * 验证指纹
     */
    private static final String ACTION_VALIDATE_FINGERPRINT = "validate_fingerprint";
    /**
     * 超时时间
     */
    private static final int TIMEOUT = 10;
    private static final int RESOLVE_ACTION = 200;
    private static final int BMP_HEADER_LEN = 1078;
    private static final int FPC1020_TOTAL_PIXELS = 36864;
    private static final int FPC1011_TOTAL_PIXELS = 30400;
    private GfpInterface gfpInterface;
    private MyHandler myHandler;
    private static String action;
    private static boolean isConnected = false;
    private static long l;
    private List<byte[]> bytes;
    private SupportActivity activity;
    private IBluetoothView baseView;
    static class MyHandler extends Handler {
        private final ArrayList<byte[]> feature_group;
        private int intBTStatus;
        private GfpInterface gfpInterface;
        //三特征合成的最终模板
        private byte[] resultTemplet = new byte[256];
        //当前提取到的特征数组
        private byte[] currentTemplet = new byte[256];
        private IBluetoothView baseView;
        private String bluetoothName;
        private String bluetoothMac;
        private List<byte[]> bytes;
        private int i = 0;

        public MyHandler(IBluetoothView baseView, String name, String mac) {
            feature_group = new ArrayList<>();
            this.baseView = baseView;
            this.bluetoothName = name;
            this.bluetoothMac = mac;
        }

        public void setGfpInterface(GfpInterface gfpInterface) {
            this.gfpInterface = gfpInterface;
        }

        public void setBytes(List<byte[]> bbytes) {
            this.bytes = bbytes;
        }

        public int getIntBTStatus() {
            return intBTStatus;
        }

        public byte[] getResultTemplet() {
            return resultTemplet;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0xA0:
                    int intGetError = msg.getData().getInt("FPIGetError");
                    if (intGetError < 0) {
                        /**
                         *
                         private final int STATUS_OK = 0: //状态正常
                         private final int DATA_CHECK_ERR = -1; //数据校验错误
                         private final int DATA_NOT_END_ERR = -2; //数据没有结束标志
                         private final int CMD_ERR_OR_BUSY = -3; //命令返回错误或者传输忙
                         private final int BT_RECONNECT_OR_BUSY = -4; //蓝牙断开重连或者传输忙
                         private final int DATA_LENGTH_ERR = -5; //数据长度错误
                         private final int ALGORITHM_VERIFY_ERR = -6; //算法鉴权失败
                         private final int GEN_TEMPLATE_ERR = -7; //指纹图像合成模板失败
                         private final int GET_FEATURE_ERR = -8; //指纹图像提取特征失败
                         */
                    }
                    break;
                case 0xA1://实时侦查蓝牙的状态
                    intBTStatus = msg.getData().getInt("FPIBTStatus");
                    Log.e("蓝牙实时状态", "handleMessage: " + intBTStatus);
                    if (intBTStatus == 3) {//成功连接蓝牙设备
                        isConnected = true;
                        baseView.updateState("连接成功");
                        SPUtil.put(BluetoothConstants.SP.SP_SAVE_FINGERPRINT, bluetoothName + "," + bluetoothMac);
                        if (!TextUtils.isEmpty(action)) {
                            sendEmptyMessage(RESOLVE_ACTION);
                        }
                    } else if (intBTStatus == 2) {   //蓝牙正在连接
                        baseView.updateState("正在连接");
                    } else if (intBTStatus == 1) {   //蓝牙进行配对
                        baseView.updateState("正在配对");
                    } else if (intBTStatus == 0) {   //蓝牙连接断开
                        baseView.updateState("连接断开");

                    }
                    break;
                case 0xA4:  //提取指纹特征
                    break;
                case 0xA5: //由图像合成指纹模板
                    break;
                case 0xB0://获取设备的版本信息
                    break;
                case 0xB1://获取设备的产品序列号
                    break;
                case 0xB2://设置产品的序列号
                    break;
                case 0xB3://获取指纹图像
                    // strFpImage = msg.getData().getString("FPIGetImage");
                    byte[] byteFpImage = (byte[]) msg.obj;
                    int lenIMG = msg.arg1;
                    Bitmap bitmap = bitmapShow(gfpInterface, byteFpImage, lenIMG);
                    //图像采集完了，再采集模板
                    gfpInterface.fpiGetDevTPT(TIMEOUT, 0);//0xB5
                    baseView.updateData("image", ImageUtils.convertIconToString(bitmap));
                    break;
                case 0xB4: //获取指纹特征，然后可用于匹配
                    byte[] feature = (byte[]) msg.obj;
                    int lengthFTR = msg.arg1;

                    if (lengthFTR == 256) { // 获取指纹特征成功
                        System.arraycopy(feature, 0, currentTemplet, 0, 256);
                        baseView.updateData("validate", ByteUtils.byteToString(currentTemplet));
                        for (byte[] byt : bytes) {
                            i++;
                            int result = gfpInterface.sysOneMatch(byt, currentTemplet);
                            if (result > 0) {
                                baseView.updateState("验证成功");
                                break;
                            } else {
                                baseView.updateState("验证失败:" + i);
                                SystemClock.sleep(10);
                            }
                        }
                        i = 0;
                        baseView.updateState("耗费时间：" + (SystemClock.currentThreadTimeMillis() - l));
                    } else { // 获取指纹特征失败

                    }
                    break;
                case 0xB5://获得指纹模板(指纹录入)
                    byte[] byteTemplate = (byte[]) msg.obj;
                    int lenTPT = msg.arg1;
                    if (lenTPT == 256) { // 获取指纹模板成功
                        System.arraycopy(byteTemplate, 0, resultTemplet,
                                0, 256);
                        baseView.updateState("录入指纹成功");
                        Timber.e(ByteUtils.byteToString(resultTemplet));
                        baseView.updateData("input", ByteUtils.byteToString(resultTemplet));
                    } else { // 获取指纹模板失败
                    }
                    break;
                case 0xB6://指纹模板和特征在设备中进行比对(1:1)

                    break;
                case 0xB7://在终端进行搜索比对（1：n）
                    break;
                case 0xB8://检测手指状态
                    int intFpStatus = msg.getData().getInt("FPICheckFinger");
                    break;
                case 0xB9: // EncryptTest --Reserved
                    break;
                case 0xBA: // DecryptTest --Reserved String.valueOf(i),
                    // Integer.toString(i)
                    break;
                case 0xBB: //获取设备中存储的模板数量
                    break;
                case 0xBC: // 删除设备中的指纹模板
                    break;
                case 0xBD://修改蓝牙名称
                    int intSetBtName = msg.getData().getInt("FPISetBtName");
                    break;
                case 0xBE://修改进入休眠的时间
                    int intSetSleepTime = msg.getData().getInt("FPISetSleepTime");
                    break;

                case 0xD0:
                    break;
                case 0xD1:
                    break;
                case 0xD2:
                    break;
                case 0xE0:
                    break;
                case RESOLVE_ACTION:
                    switch (action) {
                        case ACTION_COLLECT_FINGERPRINT://指纹录入
                            //先发一条命令获取指纹图像
                            //再发录入指纹的命令
                            gfpInterface.fpiGetImage(TIMEOUT);
                            baseView.updateState("请按下手指");
                            break;
                        case ACTION_VALIDATE_FINGERPRINT://指纹验证
                            gfpInterface.fpiGetDevFTR(TIMEOUT);//0xB4
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private static Bitmap bitmapShow(GfpInterface gfpInterface, byte[] arryData, int fpDataLen) {
        if (!((fpDataLen == FPC1020_TOTAL_PIXELS) || (fpDataLen == FPC1011_TOTAL_PIXELS))) {
            return null;
        }
        byte bmpData[] = new byte[fpDataLen + BMP_HEADER_LEN];
        byte[] byteBmpArry;
        if (fpDataLen == FPC1020_TOTAL_PIXELS) {
            byteBmpArry = gfpInterface.CGfpArithApp.CDataTrans.hexStringToBytes(gfpInterface.CGfpArithApp.mGfpVerifyIntf.FPGetBmpHeader(3));//
        } else {
            byteBmpArry = gfpInterface.CGfpArithApp.CDataTrans.hexStringToBytes(gfpInterface.CGfpArithApp.mGfpVerifyIntf.FPGetBmpHeader(0));//
        }

        System.arraycopy(byteBmpArry, 0, bmpData, 0, BMP_HEADER_LEN);  //copy bitmap header
        System.arraycopy(arryData, 0, bmpData, BMP_HEADER_LEN, fpDataLen); //copy sensor data
        Bitmap mFPBitmap = BitmapFactory.decodeByteArray(bmpData, 0, bmpData.length);
        return mFPBitmap;
    }

    @SuppressLint("RestrictedApi")
    public Fingerprint_WeiEr_PresenterImp(IBluetoothView baseView , List<byte[]> bytes) {
        this.baseView=baseView;
        if (baseView instanceof Activity) {
            activity = ((SupportActivity) baseView);
        } else if (baseView instanceof Fragment) {
            activity = ((Fragment) baseView).getActivity();
        }
        this.activity.getLifecycle().addObserver(this);

        this.bytes = bytes;
        myHandler = new MyHandler(baseView, "zjwellcom", "zjwellcom");
        gfpInterface = new GfpInterface(activity, myHandler);
        myHandler.setGfpInterface(gfpInterface);
        myHandler.setBytes(bytes);
        connectDevice("zjwellcom");
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        gfpInterface.sysOnResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }

        if (gfpInterface != null) {
            if (gfpInterface.sysCheckBTConnected()) {
                gfpInterface.fpiDisconnectBT();
            } else {
                gfpInterface.sysExit();
            }
            //利用反射将jar包中未注销的广播进行注销
            try {
                Field field_GfpArithApp = GfpInterface.class.getDeclaredField("CGfpArithApp");
                field_GfpArithApp.setAccessible(true);
                Object obj_GfpAlgorithmApp = field_GfpArithApp.get(gfpInterface);
                Field field_uartInterface = GfpAlgorithmApp.class.getDeclaredField("uartInterface");
                field_uartInterface.setAccessible(true);
                Object obj_uartInterface = field_uartInterface.get(obj_GfpAlgorithmApp);
                Field fieled_mUsbReceiver = FT312UART.class.getDeclaredField("mUsbReceiver");
                Object obj_mUsbReceiver = fieled_mUsbReceiver.get(obj_uartInterface);
                activity.unregisterReceiver((BroadcastReceiver) obj_mUsbReceiver);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        isConnected = false;
        action = null;
    }

    public void collectFingers() {
        action = ACTION_COLLECT_FINGERPRINT;
        if (myHandler.getIntBTStatus() != 3) {
            baseView.updateState("设备未连接");
            gfpInterface.fpiConnectBT("zjwellcom");
        } else {
            myHandler.sendEmptyMessage(RESOLVE_ACTION);
        }
    }

    public void connectDevice(final String macAddress) {
        if (!isConnected) {
            gfpInterface.fpiOpenBT();
            SystemClock.sleep(100);
            gfpInterface.fpiConnectBT(macAddress);
        }
    }
    public void  addByte(byte[] bytes){
        this.bytes.add(bytes);
        myHandler.setBytes(this.bytes);
    }
    public void validateFinger() {
        l = SystemClock.currentThreadTimeMillis();
        action = ACTION_VALIDATE_FINGERPRINT;
        if (myHandler.getIntBTStatus() != 3) {
            baseView.updateState("设备未连接");
            gfpInterface.fpiConnectBT("zjwellcom");
        } else {
            myHandler.sendEmptyMessage(RESOLVE_ACTION);
        }
    }
}
