package com.example.han.referralproject.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bluetooth.BluetoothLeService;
import com.example.han.referralproject.bluetooth.Commands;
import com.example.han.referralproject.bluetooth.XueTangGattAttributes;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.XueyaUtils;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import android.support.v4.content.ContextCompat;

public class DetectActivity extends BaseActivity implements View.OnClickListener {

    private BluetoothAdapter mBluetoothAdapter;
    int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private String mDeviceAddress;
    private final static String TAG = DetectActivity.class.getSimpleName();
    private BluetoothLeService mBluetoothLeService;
    public boolean threadDisable = true;
    public boolean blueThreadDisable = true;
    public boolean workSearchThread = true;
    public String str;
    public TextView mResultTv;
    public TextView mHighPressTv, mLowPressTv, mPulseTv;
    public TextView mXueYangTv, mXueYangPulseTv;
    public TextView mSanHeYiOneTv, mSanHeYiTwoTv, mSanHeYiThreeTv;
    public View tipsLayout;
    public VideoView mVideoView;
    NDialog dialog;
    private BluetoothGatt mBluetoothGatt;

    private String detectType = Type_Xueya;
    public static final String Type_Wendu = "wendu";
    public static final String Type_Xueya = "xueya";
    public static final String Type_XueTang = "xuetang";
    public static final String Type_XueYang = "xueyang";
    public static final String Type_XinDian = "xindian";
    public static final String Type_TiZhong = "tizhong";
    public static final String Type_SanHeYi = "sanheyi";
    private boolean isGetResustFirst = true;
    private String[] mXueyaResults;
    private String[] mWenduResults;
    private String[] mXueYangResults;
    private String[] mEcgResults;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private View mOverView;
    private LocalShared mShared;
    private Thread mSearchThread;
    private boolean isYuyue = false;

    @SuppressLint("HandlerLeak")
    Handler xueyaHandler = new Handler() {
        private byte i;

        @SuppressLint("SimpleDateFormat")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// TODO 设置显示点击查看按钮并上传数据
                    break;
                // 测量成功
                case 1:
                    break;
                // 测量失败
                case 2:
                    break;
                // 设备超时
                case 3:
                    //getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeilianjieyichangqingchongxinlianjie));
                    speak("设备超时");
                    break;
                // 充不上气
                case 4:
                    //getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.chongbushangqi));
                    speak("设备充不上气");
                    break;
                // 测量中发生错误
                case 5:
                    //getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.celiangzhongfashengcuowu));
                    speak("设备检测发生错误");
                    break;
                // 血压计低电量
                case 6:
                    //getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.xueyajididianliang));
                    speak("设备低电量");
                    break;
                case 7:
                    //getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.quxiaocaozuo));
                    break;
                // 测量中
                case 8:
                    break;
                case 9:
                    break;
                case 10:
//                    finish();
//                    new TimeManager(3000, 1000).start();
                    break;
                case 11:
//                    String erro = (String) msg.obj;
//                    if(erro.equals("错误信息：设备充不上气")){
//                        getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeiyichang00));
//                    }else if(erro.equals("错误信息：血压计电量过低")){
//                        getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeiyichang02));
//                    }else if(erro.equals("错误信息：测量中发生错误，请正确测量")){
//                        getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeiyichang01));
//                    }
                    break;
                case 12://经典蓝牙测量中数据

//                    if ((int) notifyData[0] == 32 && notifyData.length == 2) {
//                        mHighPressTv.setText(String.valueOf(notifyData[1] & 0xff));
//                        mLowPressTv.setText("0");
//                        mPulseTv.setText("0");
//                    }
//                    if ((int) notifyData[0] == 12) {
//                        mHighPressTv.setText(String.valueOf(notifyData[2] & 0xff));
//                        mLowPressTv.setText(String.valueOf(notifyData[4] & 0xff));
//                        mPulseTv.setText(String.valueOf(notifyData[8] & 0xff));
//                        if (isGetResustFirst) {
//                            String xueyaResult;
//                            if ((notifyData[2] & 0xff) <= 140) {
//                                xueyaResult = mXueyaResults[0];
//                            } else if ((notifyData[2] & 0xff) <= 160) {
//                                xueyaResult = mXueyaResults[1];
//                            } else {
//                                xueyaResult = mXueyaResults[2];
//                            }
//                            speak(String.format(getString(R.string.tips_result_xueya),
//                                    notifyData[2] & 0xff, notifyData[4] & 0xff, notifyData[8] & 0xff, xueyaResult));
//                            DataInfoBean info = new DataInfoBean();
//                            info.high_pressure = notifyData[2] & 0xff;
//                            info.low_pressure = notifyData[4] & 0xff;
//                            info.pulse = notifyData[8] & 0xff;
//                            NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
//                                @Override
//                                public void onSuccess(String response) {
//                                    //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            isGetResustFirst = false;
//                            mHandler.sendEmptyMessageDelayed(2, 30000);
//                        }
//                    }
//                    StringBuilder mBuilder = new StringBuilder();
////                        for (char item : xueyaChars){
////                            mBuilder.append(item).append("(").append((byte)item).append(")").append("    ");
////                        }
//                    for (byte item : notifyData) {
//                        mBuilder.append(item).append("    ");
//                    }

                    byte[] result2 = (byte[]) msg.obj;
                    int num = 0;
				/*numPosition = result2[5];
				if (numPosition > 255) {
					numPosition = (((byte)numPosition)&0xff)+1+0xff;
				}*/
                    num = (result2[5]&0xff)|(result2[6]<<8&0xff00);
                    Log.i("mylog", "data " + num);
                    mHighPressTv.setText(String.valueOf(num));
                    break;
                case 13://经典蓝牙测量结果
                    byte[] res = (byte[]) msg.obj;
                    int getNew = (res[5]&0xff) + 30;
                    int maibo = res[4]&0xff;int i = res[6];
                    int down = 0;
                    if(((i & 0XFF)>0)||(i & 0XFF)<256){
                        down = (i & 0XFF) + 30;
                    }else{
                        down = (((byte)i) & 0XFF)+1+0xff + 30;
                    }
                    mHighPressTv.setText(String.valueOf(getNew));
                    mLowPressTv.setText(String.valueOf(down));
                    mPulseTv.setText(String.valueOf(maibo));
                    Log.i("mylog", "gao " + getNew + " di "  + down + " pluse " + maibo);
                    String xueyaResult;
                    if (getNew <= 140) {
                        xueyaResult = mXueyaResults[0];
                    } else if (getNew <= 160) {
                        xueyaResult = mXueyaResults[1];
                    } else {
                        xueyaResult = mXueyaResults[2];
                    }
                    speak(String.format(getString(R.string.tips_result_xueya),
                            getNew, down, maibo, xueyaResult));
                    DataInfoBean info = new DataInfoBean();
                    info.high_pressure = getNew;
                    info.low_pressure = down;
                    info.pulse = maibo;
                    NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 14:
                    stopSearch();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    sendDataByte(Commands.CMD_LENGTH_TEN, Commands.CMD_CATEGORY_ZERO);
                    break;
                case 1:
                    mResultTv.setText((String) msg.obj);
                    break;
                case 2:
                    isGetResustFirst = true;//测量重置标志位
                    break;
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.i("mylog", "service connected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            if (TextUtils.isEmpty(mDeviceAddress)){
                return;
            }
            if (mBluetoothLeService.connect(mDeviceAddress)) {
                mBluetoothGatt = mBluetoothLeService.getGatt();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private boolean mConnected = false;
    private boolean xuetangAbnormal=false;//测量血糖异常标识，默认正常
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.i("mylog", "action : " + intent.getAction());
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                switch (detectType) {
                    case Type_XueTang:
                        mShared.setXuetangMac(mDeviceAddress);
                        break;
                    case Type_XueYang:
                        mShared.setXueyangMac(mDeviceAddress);
                        break;
                    case Type_Wendu:
                        mShared.setWenduMac(mDeviceAddress);
                        break;
                    case Type_SanHeYi:
                        mShared.setSanheyiMac(mDeviceAddress);
                        break;
                    case Type_XinDian:
                        mShared.setXinDianMac(mDeviceAddress);
                        break;
                    case Type_Xueya:
                        mShared.setXueyaMac(mDeviceAddress);
                        break;
                }
//                switch (detectType) {
//                    case Type_XueTang:
//                        XueTangGattAttributes.notify(mBluetoothGatt);
//                        mHandler.sendEmptyMessageDelayed(0, 1000);
//                        break;
//                    case Type_XueYang:
////                        mWriteCharacteristic.setValue(Commands.xueyangDatas);
////                        mWriteCharacteristic
////                                .setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
////                        boolean issuccess = mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
////                        Log.i("mylog", "success");
//                        break;
//                }
                Log.i("mylog", "gata connect 11111111111111111111");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i("mylog", "gata disConnect 22222222222222222");
                mConnected = false;
                //speak(R.string.tips_blue_unConnect);
                isGetResustFirst = true;
                if (mBluetoothLeService != null){
                    mBluetoothLeService.disconnect();
                    mBluetoothLeService.close();
                }
                if (mBluetoothAdapter != null) {
//                    if (detectType == Type_XinDian){
//                        dialog = new NDialog(mContext);
//                        showNormal("设备连接中，请稍后...");
//                    }
                    startSearch();
//                    mBluetoothAdapter.startDiscovery();
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i("mylog", "gata servicesConnect 3333333333333333");
                speak(R.string.tips_blue_connect);
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                switch (detectType) {
                    case Type_XueTang:
                        XueTangGattAttributes.notify(mBluetoothGatt);
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                        break;
                    case Type_XueYang:
//                        mWriteCharacteristic.setValue(Commands.xueyangDatas);
//                        mWriteCharacteristic
//                                .setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
//                        boolean issuccess = mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
//                        Log.i("mylog", "success");
                        break;

                }
                switch (detectType) {
                    case Type_XueYang:
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                while (threadDisable){
//                                    mWriteCharacteristic.setValue(Commands.xueyangDatas);
//                                    mWriteCharacteristic
//                                            .setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
//                                    mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
//                                    Log.i("mylog2", "血氧 write");
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }).start();
                        break;
                }
//                if (detectType == Type_XueTang) {
//                    mHandler.sendEmptyMessage(0);
//                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] notifyData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                //Log.i("mylog", "receive>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + notifyData.length);
                Log.i("mylog", "receive   " + bytesToHexString(notifyData));
                byte[] extraData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_NOTIFY_DATA);
                switch (detectType) {
                    case Type_Wendu:
                        if (notifyData == null || notifyData.length != 13) {
                            return;
                        }
                        int tempData = notifyData[6] & 0xff;
                        if (tempData < 44) {
                            speak(R.string.tips_error_temp);
                            return;
                        }
                        StringBuilder mTempResult = new StringBuilder();
                        mTempResult.append((tempData - 44) / 10).append(".").append((tempData - 44) % 10);
                        String wenduResult;
                        float wenduValue = 30 + Float.valueOf(mTempResult.toString());
                        if (wenduValue < 36) {
                            wenduResult = mWenduResults[3];
                        } else if (wenduValue < 38) {
                            wenduResult = mWenduResults[0];
                        } else if (wenduValue < 40) {
                            wenduResult = mWenduResults[1];
                        } else {
                            wenduResult = mWenduResults[2];
                        }
                        if (isGetResustFirst) {
                            isGetResustFirst = false;
                            mResultTv.setText(String.valueOf(wenduValue));
                            mHandler.sendEmptyMessageDelayed(2, 5000);
                            DataInfoBean info = new DataInfoBean();
                            info.temper_ature = String.valueOf(wenduValue);
                            NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        speak(String.format(getString(R.string.tips_result_wendu), String.valueOf(wenduValue), wenduResult));
                        break;
                    case Type_Xueya:
                        if (isYuyue && notifyData.length == 19){
                            mHighPressTv.setText(String.valueOf(notifyData[1] & 0xff));
                            mLowPressTv.setText(String.valueOf(notifyData[3] & 0xff));
                            mPulseTv.setText(String.valueOf(notifyData[14] & 0xff));
                            String xueyaResult;
                            if ((notifyData[1] & 0xff) <= 140) {
                                xueyaResult = mXueyaResults[0];
                            } else if ((notifyData[1] & 0xff) <= 160) {
                                xueyaResult = mXueyaResults[1];
                            } else {
                                xueyaResult = mXueyaResults[2];
                            }
                            speak(String.format(getString(R.string.tips_result_xueya),
                                    notifyData[1] & 0xff, notifyData[3] & 0xff, notifyData[14] & 0xff, xueyaResult));
                            DataInfoBean info = new DataInfoBean();
                            info.high_pressure = notifyData[1] & 0xff;
                            info.low_pressure = notifyData[3] & 0xff;
                            info.pulse = notifyData[14] & 0xff;
                            NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        if ((int) notifyData[0] == 32 && notifyData.length == 2) {
                            mHighPressTv.setText(String.valueOf(notifyData[1] & 0xff));
                            mLowPressTv.setText("0");
                            mPulseTv.setText("0");
                        }
                        if ((int) notifyData[0] == 12) {
                            mHighPressTv.setText(String.valueOf(notifyData[2] & 0xff));
                            mLowPressTv.setText(String.valueOf(notifyData[4] & 0xff));
                            mPulseTv.setText(String.valueOf(notifyData[8] & 0xff));
                            if (isGetResustFirst) {
                                isGetResustFirst = false;
                                mHandler.sendEmptyMessageDelayed(2, 30000);
                                String xueyaResult;
                                if ((notifyData[2] & 0xff) <= 140) {
                                    xueyaResult = mXueyaResults[0];
                                } else if ((notifyData[2] & 0xff) <= 160) {
                                    xueyaResult = mXueyaResults[1];
                                } else {
                                    xueyaResult = mXueyaResults[2];
                                }
                                speak(String.format(getString(R.string.tips_result_xueya),
                                        notifyData[2] & 0xff, notifyData[4] & 0xff, notifyData[8] & 0xff, xueyaResult));
                                DataInfoBean info = new DataInfoBean();
                                info.high_pressure = notifyData[2] & 0xff;
                                info.low_pressure = notifyData[4] & 0xff;
                                info.pulse = notifyData[8] & 0xff;
                                NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                                    @Override
                                    public void onSuccess(String response) {
                                        //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        break;
                    case Type_XueTang://血糖测量
                        if (notifyData == null || notifyData.length < 12) {
                            return;
                        }
                        //threadDisable = false;
                        if (isGetResustFirst) {
                            isGetResustFirst = false;
//                            float xuetangResut = ((float)((notifyData[10] << 8 + (notifyData[9] & 0xff))))/18;
                            float xuetangResut = ((float) (notifyData[10] << 8) + (float) (notifyData[9] & 0xff)) / 18;
//                            float xuetangResut = ((float) (((notifyData[9] & 0xff)))) / 18;
                            mResultTv.setText(String.format("%.1f", xuetangResut));
                            switch (time){
                                case 0://空腹
                                    if(xuetangResut<3.61){
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏低,请重新测量或联系医生"));
                                        xuetangAbnormal=true;
                                    }else if(xuetangResut<=7.0){
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值正常"));
                                    }else{
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏高,请重新测量或联系医生"));
                                        xuetangAbnormal=true;
                                    }
                                    break;
                                case 1://饭后一小时
                                    if(xuetangResut<3.61){
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏低,请重新测量或联系医生"));
                                        xuetangAbnormal=true;
                                    }else if(xuetangResut<=11.1){
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值正常"));
                                    }else{
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏高,请重新测量或联系医生"));
                                        xuetangAbnormal=true;
                                    }
                                    break;
                                case 2://饭后两小时
                                    if(xuetangResut<3.61){
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏低,请重新测量或联系医生"));
                                        xuetangAbnormal=true;
                                    }else if(xuetangResut<=7.8){
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值正常"));
                                    }else{
                                        speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏高,请重新测量或联系医生"));
                                        xuetangAbnormal=true;
                                    }
                                    break;
                            }

                            DataInfoBean info = new DataInfoBean();
                            info.blood_sugar = String.format("%.1f", xuetangResut);
                            info.sugar_time=time+"";
                            NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case Type_XueYang:
                        if (notifyData != null && notifyData.length == 12 && notifyData[5] != 0) {
                            threadDisable = false;
                            mXueYangTv.setText(String.valueOf(notifyData[5]));
                            mXueYangPulseTv.setText(String.valueOf(notifyData[6]));
                            if (isGetResustFirst) {
                                isGetResustFirst = false;
                                mHandler.sendEmptyMessageDelayed(2, 30000);
                                DataInfoBean info = new DataInfoBean();
                                info.blood_oxygen = String.format(String.valueOf(notifyData[5]));
                                info.pulse = (int) notifyData[6];
                                String xueyangResult;
                                if (notifyData[5] >= 90) {
                                    xueyangResult = mXueYangResults[0];
                                } else {
                                    xueyangResult = mXueYangResults[1];
                                }
                                speak(String.format(getString(R.string.tips_result_xueyang), info.blood_oxygen, info.pulse, xueyangResult));
                                NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                                    @Override
                                    public void onSuccess(String response) {
                                        //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        break;
                    case Type_TiZhong:
                        if (notifyData != null && notifyData.length == 14 && (notifyData[1] & 0xff) == 221) {
                            if (isGetResustFirst){
                                isGetResustFirst = false;
                                float result = ((float) (notifyData[2] << 8) + (float) (notifyData[3] & 0xff)) / 10;
                                mResultTv.setText(String.valueOf(result));
                                String  height_s=LocalShared.getInstance(DetectActivity.this).getUserHeight();
                                float height=TextUtils.isEmpty(height_s)?0:Float.parseFloat(height_s)/100;
                                if(height!=0)
                                    ((TextView)findViewById(R.id.tv_tizhi)).setText(String.format("%1$.2f",result/(height*height)));
                                speak(String.format(getString(R.string.tips_result_tizhong), String.format("%.1f", result)));
                                DataInfoBean info = new DataInfoBean();
                                info.weight = result;
                                NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                                    @Override
                                    public void onSuccess(String response) {
                                        //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        break;
                    case Type_XinDian:
                        if (notifyData == null || notifyData.length < 20 || notifyData[6] != 84) {
                            return;
                        }
                        //onDetectView.setVisibility(View.GONE);
                        ((TextView)findViewById(R.id.tv_xindian)).setText(String.format(getString(R.string.tips_result_xindian), notifyData[16] & 0xff, mEcgResults[notifyData[17]]));
                        DataInfoBean ecgInfo = new DataInfoBean();
                        ecgInfo.ecg = notifyData[17];
                        ecgInfo.heart_rate = notifyData[16] & 0xff;
                        NetworkApi.postData(ecgInfo, new NetworkManager.SuccessCallback<String>() {
                            @Override
                            public void onSuccess(String response) {
                                //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                            }
                        });
                        speak(String.format(getString(R.string.tips_result_xindian), notifyData[16] & 0xff, mEcgResults[notifyData[17]]));
                        break;
                    case Type_SanHeYi:
                        if (notifyData == null || notifyData.length < 13){
                            return;
                        }
                        if (isGetResustFirst){
                            isGetResustFirst = false;
                            int result = ((notifyData[11] & 0xff) << 8) + (notifyData[10] & 0xff);
                            int basic = (int) Math.pow(16, 3);
                            int flag = result/basic;
                            int number = result % basic;
                            double afterResult;
                            afterResult = number / Math.pow(10, 13 - flag);
                            DataInfoBean info = new DataInfoBean();
                            if (notifyData[1] == 65){
                                info.blood_sugar = String.valueOf(afterResult);
                                mSanHeYiOneTv.setText(String.valueOf(afterResult));
                                speak(String.format(getString(R.string.tips_result_xuetang), String.valueOf(afterResult), "正常"));
                            } else if (notifyData[1] == 81) {//尿酸
                                info.uric_acid = String.valueOf(afterResult);
                                mSanHeYiTwoTv.setText(String.valueOf(afterResult));
                                speak(String.format(getString(R.string.tips_result_niaosuan), String.valueOf(afterResult), "正常"));
                            } else if (notifyData[1] == 97) {//胆固醇
                                info.cholesterol = String.valueOf(afterResult);
                                mSanHeYiThreeTv.setText(String.valueOf(afterResult));
                                speak(String.format(getString(R.string.tips_result_danguchun), String.valueOf(afterResult), "正常"));
                            }
                            NetworkApi.postData(info, new NetworkManager.SuccessCallback<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                }
                            });
//                            if (notifyData[1] == 65){
//                                afterResult = number / Math.pow(10, 11 - flag);
//                            } else {
//                                afterResult = number / Math.pow(10, 13 - flag);
//                            }
//                            int result = (notifyData[18] << 8) + (notifyData[17] & 0xff);
                            //mResultTv.setText(String.valueOf(result));
                        }
                        break;
                }
            }
        }
    };

    public ImageView ivBack;

    @Override
    protected void onActivitySpeakFinish() {//语音播放完成后，如果血糖异常，则跳转到并发症页面
        if(xuetangAbnormal){
//            startActivity(new Intent(this,SymptomsActivity.class));
        }
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        BluetoothGattCharacteristic characteristic = null;
        // Loops through available GATT Services.

//        for (BluetoothGattService gattService : gattServices) {

//            HashMap<String, String> currentServiceData = new HashMap<String, String>();
//            uuid = gattService.getUuid().toString();
//            Log.i("mylog", "uuid : " + uuid);
//            currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
//            currentServiceData.put(LIST_UUID, uuid);
//            gattServiceData.add(currentServiceData);
        // get characteristic when UUID matches RX/TX UUID
//            characteristic = gattService.getCharacteristics().get(4);
//            characteristic = gattService.getCharacteristic(UUID.fromString(uuid));
//            break;
//            characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
//        }
        switch (detectType) {
            case Type_Wendu:
                List<BluetoothGattCharacteristic> characteristicsList = gattServices.get(3).getCharacteristics();
                if (characteristicsList.size() == 3){
                    characteristic = characteristicsList.get(1);//新版本耳温枪
                } else {
                    characteristic = characteristicsList.get(3);//旧版本耳温枪
                }
                break;
            case Type_Xueya:
//                if (gattServices.size() == 5) {
//                    characteristic = gattServices.get(3).getCharacteristics().get(3);
//                } else {
//                    characteristic = gattServices.get(2).getCharacteristics().get(3);
//                }

//                BluetoothGattService service = mBluetoothLeService.getGatt().getService(UUID
//                        .fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
//                if (service != null){
//                    characteristic = service
//                            .getCharacteristic(UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb"));
//                    break;
//                }

                BluetoothGattService xueyaService = mBluetoothLeService.getGatt().getService(UUID
                        .fromString("00001810-0000-1000-8000-00805f9b34fb"));
                if (xueyaService != null){
                    characteristic = xueyaService
                            .getCharacteristic(UUID.fromString("00002a35-0000-1000-8000-00805f9b34fb"));
                    Log.i("mylog", "success sssssssssssssssssssssssssssssss");
                    isYuyue = true;
                    break;
                }

                if (gattServices.size() == 5 || gattServices.size() == 10){
                    characteristic = gattServices.get(3).getCharacteristics().get(3);
                } else {
                    characteristic = gattServices.get(2).getCharacteristics().get(3);
                }
                break;
            case Type_XueYang:
                characteristic = gattServices.get(2).getCharacteristics().get(1);
                break;
            case Type_XinDian:
                characteristic = gattServices.get(0).getCharacteristics().get(0);
                break;
            case Type_TiZhong:
                //characteristic = gattServices.get(3).getCharacteristics().get(2);

//                BluetoothGattService service = mBluetoothLeService.getGatt().getService(UUID
//                        .fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
//                //read
//                BluetoothGattCharacteristic	characteristicRead = service
//                        .getCharacteristic(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
//                //notify
//                BluetoothGattCharacteristic	characteristicNotify = service
//                        .getCharacteristic(UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb"));
//                setCharacterValue(characteristicRead, characteristicNotify , 0);

                BluetoothGattService service = mBluetoothLeService.getGatt().getService(UUID
                        .fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
                characteristic = service
                        .getCharacteristic(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
                break;
            case Type_SanHeYi:
                characteristic = gattServices.get(4).getCharacteristics().get(0);
//                characteristic = gattServices.get(3).getCharacteristics().get(1);
                break;
        }
        if (characteristic == null) {
            return;
        }
        if (detectType == Type_TiZhong) {
            setCharacterValue(characteristic, characteristic , 0);
            return;
        }
        mWriteCharacteristic = characteristic;
        switch (detectType) {
            case Type_XueYang:
                mWriteCharacteristic.setValue(Commands.xueyangDatas);
                mWriteCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                break;
        }

        mBluetoothLeService.writeCharacteristic(characteristic);
        mBluetoothLeService.readCharacteristic(characteristic);
        mBluetoothLeService.setCharacteristicNotification(characteristic, true);

        //第一个坑，数据没传输过来
        List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
        if (descriptorList != null && descriptorList.size() > 0) {
            for (BluetoothGattDescriptor descriptor : descriptorList) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (isYuyue){
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                }
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
        Log.i("mylog", "chara uuid : " + characteristic.getUuid() + "\n" + "service uuid : " + gattServices.get(0).getUuid());
//        boolean isSuccess = readMessage();
//        Log.i("mylog1", "is Success " + isSuccess);
    }

    private void setCharacterValue(BluetoothGattCharacteristic characteristic,
                                   BluetoothGattCharacteristic characteristic1,int status){
        // 激活通知
        final int charaProp = characteristic.getProperties();
        int charaProp_second = -1;
        if (characteristic1!=null) {
            charaProp_second = characteristic1.getProperties();
        }
//        Log.i("mylog", "2222222222222222222");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) >0) {
                mBluetoothGatt.setCharacteristicNotification(
                        characteristic, true);
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(UUID
                                .fromString("00002902-0000-1000-8000-00805f9b34fb"));
                if (descriptor != null) {
                    descriptor
                            .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                }
                if(characteristic1==null)
                    return;
                if ((charaProp_second & BluetoothGattCharacteristic.PROPERTY_NOTIFY) >0) {
                    mBluetoothGatt.setCharacteristicNotification(
                            characteristic1, true);
                    if (descriptor != null) {
                        descriptor
                                .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    }
                }
                if (descriptor != null){
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
//                Log.i("mylog", "33333333333333333333333");
            }
        }
    }


    //防止VedioView导致内存泄露
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase)
        {
            @Override
            public Object getSystemService(String name)
            {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int resourceId = 0;
        switch (v.getId()) {
            case R.id.temperature_video:
                resourceId = R.raw.tips_wendu;
                break;
            case R.id.xueya_video:
                resourceId = R.raw.tips_xueya;
                break;
            case R.id.xuetang_video:
                resourceId = R.raw.tips_xuetang;
                break;
            case R.id.xueyang_video:
                resourceId = R.raw.tips_xueyang;
                break;
            case R.id.xindian_video:
                resourceId = R.raw.tips_xindian;
                break;
            case R.id.view_over://跳过演示视频
                if (mVideoView != null) {
                    mVideoView.setVisibility(View.GONE);
                    mOverView.setVisibility(View.GONE);
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                    }
                }
                break;
            case R.id.sanheyi_video:
                resourceId=R.raw.tips_sanheyi;
                break;
        }
        if (resourceId != 0) {
            mVideoView.setVisibility(View.VISIBLE);
            mOverView.setVisibility(View.VISIBLE);
            String uri = "android.resource://" + getPackageName() + "/" + resourceId;
            mVideoView.setVideoURI(Uri.parse(uri));
            mVideoView.start();
        }
    }

    public ImageView mImageView1;
    public ImageView mImageView2;

    public Button mButton;
    public Button mButton1;
    public Button mButton2;
    public Button mButton3;

    private int time;
    private AVLoadingIndicatorView onDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        mShared = LocalShared.getInstance(mContext);
        time=getIntent().getIntExtra("time",0);
        mToolbar.setVisibility(View.GONE);
        mButton = (Button) findViewById(R.id.history);
        mButton1 = (Button) findViewById(R.id.history1);
        mButton2 = (Button) findViewById(R.id.history2);
        mButton3 = (Button) findViewById(R.id.history3);
        setEnableListeningLoop(false);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetectActivity.this, HealthRecordActivity.class);
                startActivity(intent);
            }
        });

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetectActivity.this, HealthRecordActivity.class);
                startActivity(intent);
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetectActivity.this, HealthRecordActivity.class);
                startActivity(intent);
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetectActivity.this, HealthRecordActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.history4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectActivity.this, HealthRecordActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.history5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetectActivity.this,HealthRecordActivity.class));
            }
        });
        findViewById(R.id.history6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetectActivity.this,HealthRecordActivity.class));
            }
        });
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImageView2 = (ImageView) findViewById(R.id.icon_home);
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startSearch();

//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();

                new AlertDialog.Builder(mContext).setMessage("是否匹配新设备").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDeviceAddress = "";
                        switch (detectType) {
                            case Type_XueTang:
                                mShared.setXuetangMac("");
                                break;
                            case Type_XueYang:
                                mShared.setXueyangMac("");
                                break;
                            case Type_Wendu:
                                mShared.setWenduMac("");
                                break;
                            case Type_SanHeYi:
                                mShared.setSanheyiMac("");
                                break;
                            case Type_XinDian:
                                mShared.setXinDianMac("");
                                break;
                            case Type_Xueya:
                                mShared.setXueyaMac("");
                                break;
                        }
                    }
                }).show();

            }
        });

        String type = getIntent().getStringExtra("type");

        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case "wendu":
                    detectType = Type_Wendu;
                    break;
                case "xueya":
                    detectType = Type_Xueya;
                    break;
                case "xuetang":
                    detectType = Type_XueTang;
                    break;
                case "xueyang":
                    detectType = Type_XueYang;
                    break;
                case "xindian":
                    detectType = Type_XinDian;
                    break;
                case "tizhong":
                    detectType = Type_TiZhong;
                    break;
                case "sanheyi":
                    detectType = Type_SanHeYi;
                    break;
            }
        }

//        tipsLayout = findViewById(R.id.rl_tips);
//        switch (detectType) {
//            case Type_Wendu:
//                mResultTv = (TextView) findViewById(R.id.tv_result);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        tipsLayout.setVisibility(View.VISIBLE);
//                        findViewById(R.id.rl_temp).setVisibility(View.VISIBLE);
//                        speak(R.string.tips_wendu_one);
//                    }
//                }, 1000);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        tipsLayout.setBackgroundResource(R.drawable.tips_wendu_two);
//                        speak(R.string.tips_wendu_two);
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                tipsLayout.setBackgroundResource(R.drawable.tips_wendu_three);
//                                speak(R.string.tips_wendu_three);
//                                mHandler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        tipsLayout.setVisibility(View.GONE);
//                                    }
//                                }, 8000);
//                            }
//                        }, 8000);
//                    }
//                }, 8000);
//                break;
//            case Type_Xueya:
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        tipsLayout.setVisibility(View.VISIBLE);
//                        findViewById(R.id.rl_xueya).setVisibility(View.VISIBLE);
//                        tipsLayout.setBackgroundResource(R.drawable.tips_xueya_one);
//                        speak(R.string.tips_xueya_one);
//                    }
//                }, 1000);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        speak(R.string.tips_xueya_two);
//                        tipsLayout.setBackgroundResource(R.drawable.tips_xueya_two);
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                tipsLayout.setVisibility(View.GONE);
//                            }
//                        }, 8000);
//                    }
//                }, 8000);
//                break;
//            case Type_XueTang:
//                mResultTv = (TextView) findViewById(R.id.tv_xuetang);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        tipsLayout.setVisibility(View.VISIBLE);
//                        findViewById(R.id.rl_xuetang).setVisibility(View.VISIBLE);
//                        tipsLayout.setBackgroundResource(R.drawable.tips_xuetang_one);
//                        speak(R.string.tips_xuetang_one);
//                    }
//                }, 1000);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        tipsLayout.setBackgroundResource(R.drawable.tips_xuetang_two);
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                tipsLayout.setVisibility(View.GONE);
//                            }
//                        }, 10000);
//                    }
//                }, 8000);

//                findViewById(R.id.rl_xuetang).setVisibility(View.VISIBLE);
//                dialog = new NDialog(th、is);
//                showNormal("设备连接中，请稍后...");
        int resourceId = 0;
        switch (detectType) {
            case Type_Wendu:
                mResultTv = (TextView) findViewById(R.id.tv_result);
                findViewById(R.id.rl_temp).setVisibility(View.VISIBLE);
                resourceId = R.raw.tips_wendu;
                break;
            case Type_Xueya:
                findViewById(R.id.rl_xueya).setVisibility(View.VISIBLE);
                resourceId = R.raw.tips_xueya;
                break;
            case Type_XueTang:
                mResultTv = (TextView) findViewById(R.id.tv_xuetang);
                findViewById(R.id.rl_xuetang).setVisibility(View.VISIBLE);
                resourceId = R.raw.tips_xuetang;
                break;
            case Type_XueYang:
                findViewById(R.id.rl_xueyang).setVisibility(View.VISIBLE);
                resourceId = R.raw.tips_xueyang;
//                dialog = new NDialog(this);
//                showNormal("设备连接中，请稍后...");
                break;
            case Type_XinDian:
                findViewById(R.id.rl_xindian).setVisibility(View.VISIBLE);
                resourceId = R.raw.tips_xindian;
//                dialog = new NDialog(this);
//                showNormal("设备连接中，请稍后...");
                break;
            case Type_TiZhong:
                mResultTv = (TextView) findViewById(R.id.tv_tizhong);
                findViewById(R.id.rl_tizhong).setVisibility(View.VISIBLE);
                dialog = new NDialog(this);
                //showNormal("设备连接中，请稍后...");
                break;
            case Type_SanHeYi:
                findViewById(R.id.rl_sanheyi).setVisibility(View.VISIBLE);
                resourceId = R.raw.tips_sanheyi;
                break;
        }
        mVideoView = (VideoView) findViewById(R.id.vv_tips);
        mOverView = findViewById(R.id.view_over);
        mOverView.setOnClickListener(this);
        if (resourceId != 0) {
            String uri = "android.resource://" + getPackageName() + "/" + resourceId;
            mVideoView.setVideoURI(Uri.parse(uri));
            mVideoView.start();
            mVideoView.setOnCompletionListener(mCompletionListener);
        } else {
            mVideoView.setVisibility(View.GONE);
            mOverView.setVisibility(View.GONE);
        }
        mHighPressTv = (TextView) findViewById(R.id.high_pressure);
        mLowPressTv = (TextView) findViewById(R.id.low_pressure);
        mSanHeYiOneTv = (TextView) findViewById(R.id.tv_san_one);
        mSanHeYiTwoTv = (TextView) findViewById(R.id.tv_san_two);
        mSanHeYiThreeTv = (TextView) findViewById(R.id.tv_san_three);
        mPulseTv = (TextView) findViewById(R.id.pulse);
        mXueYangTv = (TextView) findViewById(R.id.tv_xue_yang);
        mXueYangPulseTv = (TextView) findViewById(R.id.tv_xueyang_pulse);
        onDetect= (AVLoadingIndicatorView) findViewById(R.id.onDetect);
        if (detectType == Type_XinDian){
            onDetect.show();
//            showAnimation();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //mBluetoothAdapter.startDiscovery();

        mXueyaResults = mResources.getStringArray(R.array.result_xueya);
        mWenduResults = mResources.getStringArray(R.array.result_wendu);
        mXueYangResults = mResources.getStringArray(R.array.result_xueyang);
        mEcgResults = mResources.getStringArray(R.array.ecg_measureres);

        //speak(R.string.tips_open_device);
        findViewById(R.id.temperature_video).setOnClickListener(this);
        findViewById(R.id.xueya_video).setOnClickListener(this);
        findViewById(R.id.xuetang_video).setOnClickListener(this);
        findViewById(R.id.xueyang_video).setOnClickListener(this);
        findViewById(R.id.xindian_video).setOnClickListener(this);
        findViewById(R.id.sanheyi_video).setOnClickListener(this);
        //选择血糖测量的时间
        setXuetangSelectTime();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        registerBltReceiver();
        blueThreadDisable = true;
        startSearch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        threadDisable = false;
        unregisterReceiver(mGattUpdateReceiver);
        unregisterReceiver(searchDevices);
        stopSearch();
        blueThreadDisable = false;
        if (mBluetoothLeService != null) {
            unbindService(mServiceConnection);
        }
        mBluetoothLeService = null;
        XueyaUtils.stopThread();
        if (mBluetoothGatt != null){
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
    }

    private int seletTimeType=0;
    private void setXuetangSelectTime() {
        //空腹
        findViewById(R.id.tv_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletTimeType=0;
                findViewById(R.id.ll_selectTime).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.tv_an_hour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletTimeType=1;
                findViewById(R.id.ll_selectTime).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.tv_two_hour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletTimeType=2;
                findViewById(R.id.ll_selectTime).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.tv_three_hour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletTimeType=3;
                findViewById(R.id.ll_selectTime).setVisibility(View.GONE);
            }
        });
    }

    private void startSearch() {
//        if (mBluetoothLeService == null) {
//            Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
//            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//            switch (detectType){
//                case Type_Xueya:
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mBluetoothLeService.connect("98:7B:F3:67:80:14")) {
//                                mBluetoothGatt = mBluetoothLeService.getGatt();
//                            }
//                        }
//                    }, 1000);
//                    break;
//                case Type_Wendu:
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mBluetoothLeService.connect("8B:00:00:00:00:00")) {
//                                mBluetoothGatt = mBluetoothLeService.getGatt();
//                            }
//                        }
//                    }, 1000);
//                    break;
//            }
//        } else {
//            switch (detectType){
//                case Type_Xueya:
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mBluetoothLeService.connect("98:7B:F3:67:80:14")) {
//                                mBluetoothGatt = mBluetoothLeService.getGatt();
//                            }
//                        }
//                    }, 1000);
//                    break;
//                case Type_Wendu:
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mBluetoothLeService.connect("8B:00:00:00:00:00")) {
//                                mBluetoothGatt = mBluetoothLeService.getGatt();
//                            }
//                        }
//                    }, 1000);
//                    break;
//            }
//        }

//        if (detectType == Type_SanHeYi){
//            if (dialog == null){
//                dialog = new NDialog(this);
//            }
//            showNormal("设备连接中，请稍后...");
//        }
        switch (detectType) {
            case Type_Wendu:
                mDeviceAddress = mShared.getWenduMac();
                break;
            case Type_XinDian:
                mDeviceAddress = mShared.getXinDianMac();
                break;
            case Type_Xueya:
                mDeviceAddress = mShared.getXueyaMac();
                break;
            case Type_XueTang:
                mDeviceAddress = mShared.getXuetangMac();
                break;
            case Type_SanHeYi:
                mDeviceAddress = mShared.getSanheyiMac();
                break;
            case Type_XueYang:
                mDeviceAddress = mShared.getXueyangMac();
                break;
        }
        if (mBluetoothLeService == null) {
            Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
        workSearchThread = true;
        if (mSearchThread == null){
            mSearchThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (blueThreadDisable){
//                        Log.i("mylog", "workSearchThread : " + workSearchThread + "   blueThreadDisable " + blueThreadDisable);
                        if (!workSearchThread){
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }
//                        Log.i("mylog", "start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        if (TextUtils.isEmpty(mDeviceAddress)){
                            if (!mBluetoothAdapter.isDiscovering()){
                                boolean flag = mBluetoothAdapter.startDiscovery();
//                                Log.i("mylog", "flag : " + flag);
                            }
                        } else {
//                            Log.i("mylog", "address : " + mDeviceAddress);
                            if (mBluetoothLeService != null && mBluetoothLeService.connect(mDeviceAddress)) {
                                mBluetoothGatt = mBluetoothLeService.getGatt();
                                stopSearch();
                            }
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mSearchThread.start();
        }
    }

    private void stopSearch() {
        workSearchThread = false;
        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mVideoView.setVisibility(View.GONE);
            mOverView.setVisibility(View.GONE);
        }
    };

    public void registerBltReceiver() {
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        registerReceiver(searchDevices, intent);
    }

    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        //接收
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();
            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.i("mylog", keyName + ">>>" + String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device;
            // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = "FSRKB-EWQ01";
                switch (detectType) {
                    case Type_Wendu:
                        deviceName = "FSRKB-EWQ01";
                        break;
                    case Type_Xueya:
                        deviceName = "eBlood-Pressure";
//                        deviceName = "Dual-SPP";
                        break;
                    case Type_XueTang:
                        deviceName = "Bioland-BGM";
                        break;
                    case Type_XueYang:
                        deviceName = "POD";
                        break;
                    case Type_XinDian:
                        deviceName = "PC80B";
                        break;
                    case Type_TiZhong:
                        deviceName = "000FatScale01";
                        break;
                    case Type_SanHeYi:
                        deviceName = "BeneCheck";
//                        deviceName = "BeneCheck-1544";
//                        deviceName = "BeneCheck GL-0F8B73";
//                        deviceName = "BeneCheck TC-B DONGLE";
                        break;
                }

                if (detectType == Type_Xueya && "Dual-SPP".equals(device.getName())){
                    try {
                        stopSearch();
                        XueyaUtils.connect(device, xueyaHandler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

                if (detectType == Type_Xueya && "Yuwell BP-YE680A".equals(device.getName())){
                    mDeviceAddress = device.getAddress();
                    if (mBluetoothLeService == null) {
                        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
                        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    } else {
                        if (mBluetoothLeService.connect(mDeviceAddress)) {
                            mBluetoothGatt = mBluetoothLeService.getGatt();
                        }
                    }
                    stopSearch();
                    return;
                }

//                if (deviceName.equals(device.getName())) {
                if (!TextUtils.isEmpty(device.getName()) && device.getName().startsWith(deviceName)) {
                    if (dialog != null) {
                        dialog.create(NDialog.CONFIRM).dismiss();
                    }
                    mDeviceAddress = device.getAddress();
                    if (mBluetoothLeService == null) {
                        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
                        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    } else {
                        if (mBluetoothLeService.connect(mDeviceAddress)) {
                            mBluetoothGatt = mBluetoothLeService.getGatt();
                        }
                    }
                    stopSearch();
                }
            }
            //状态改变时
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        Log.d("BlueToothTestActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束
                        Log.d("BlueToothTestActivity", "完成配对");
                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        Log.d("BlueToothTestActivity", "取消配对");
                    default:
                        break;
                }
            }
        }
    };

    public void showNormal(String message) {
        dialog.setMessageCenter(true)
                .setMessage(message)
                .setMessageSize(25)
                .setCancleable(false)
                .setNegativeTextColor(Color.parseColor("#0000FF"))
                .setButtonCenter(false)
                .setButtonSize(25)
                .setOnConfirmListener(new NDialog.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        finish();
                    }
                }).create(NDialog.CONFIRM).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        speak(getString(R.string.now_eating_state));
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    /**
     * 向设备发送命令
     *
     * @param
     * @param
     */
    private void sendDataByte(final byte leng, final byte commandType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadDisable) {
                    Commands commands = new Commands();
                    //byte[] sendDataByte = commands.getSystemdate(Commands.CMD_HEAD, leng, commandType);
                    byte[] sendDataByte = Commands.datas;
                    //Log.i("mylog", "sendData");
                    XueTangGattAttributes.sendMessage(mBluetoothGatt, sendDataByte);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device != null) {
//                        if ("Bioland-BGM".equals(device.getName())) {
                        String deviceName = "FSRKB-EWQ01";
                        switch (detectType) {
                            case Type_Wendu:
                                deviceName = "FSRKB-EWQ01";
                                break;
                            case Type_Xueya:
                                deviceName = "eBlood-Pressure";
                                break;
                            case Type_XueTang:
                                deviceName = "Bioland-BGM";
                        }

                        if (deviceName.equals(device.getName())) {
                            //dialog.create(NDialog.CONFIRM).dismiss();
                            mDeviceAddress = device.getAddress();
                            Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
                            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        }
                    }
                }
            });
        }
    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /**
     * 心电测量动画
     */
//    private void showAnimation(){
//        Animation animation = AnimationUtils.loadAnimation(this,R.anim.heart_test);
//        onDetect.startAnimation(animation);
//    }
}
