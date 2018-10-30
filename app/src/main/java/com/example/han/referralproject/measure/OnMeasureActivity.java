package com.example.han.referralproject.measure;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bluetooth.BluetoothLeService;
import com.example.han.referralproject.bluetooth.Commands;
import com.example.han.referralproject.bluetooth.XueTangGattAttributes;
import com.example.han.referralproject.measure.fragment.SanheyiFragment;
import com.example.han.referralproject.measure.fragment.TiwenFragment;
import com.example.han.referralproject.measure.fragment.TizhongFragment;
import com.example.han.referralproject.measure.fragment.XindianFragment;
import com.example.han.referralproject.measure.fragment.XuetangFragment;
import com.example.han.referralproject.measure.fragment.XueyaFragment;
import com.example.han.referralproject.measure.fragment.XueyangFragment;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.XueyaUtils;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnMeasureActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = OnMeasureActivity.class.getSimpleName();
    @BindView(R.id.container)
    FrameLayout container;
    private String type;
    private Fragment fragment;
    private LocalShared mShared;
    private static final String Type_Wendu = "wendu", Type_Xueya = "xueya",
            Type_XueTang = "xuetang", Type_XueYang = "xueyang",
            Type_XinDian = "xindian", Type_TiZhong = "tizhong",
            Type_SanHeYi = "sanheyi";
    private int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private BluetoothAdapter mBluetoothAdapter;
    public boolean threadDisable = true;
    public boolean blueThreadDisable = true;//蓝牙连接的开关
    public boolean workSearchThread = true;//搜索设备时候的开关
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private Thread mSearchThread;
    private BluetoothGatt mBluetoothGatt;
    private boolean isGetResustFirst = true;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private String[] mXueyaResults,mWenduResults,mXueYangResults,mEcgResults;
    private int time;
    private NDialog dialog;

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
                    speak("设备超时");
                    break;
                // 充不上气
                case 4:
                    speak("设备充不上气");
                    break;
                // 测量中发生错误
                case 5:
                    speak("设备检测发生错误");
                    break;
                // 血压计低电量
                case 6:
                    speak("设备低电量");
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
                case 12://经典蓝牙测量中数据
                    byte[] result2 = (byte[]) msg.obj;
                    int num = 0;
                    num = (result2[5] & 0xff) | (result2[6] << 8 & 0xff00);
//                    mHighPressTv.setText(String.valueOf(num));
                    measureListener.onSuccess(num+"",null,null,null);
                    break;
                case 13://经典蓝牙测量结果
                    byte[] res = (byte[]) msg.obj;
                    int getNew = (res[5] & 0xff) + 30;
                    int maibo = res[4] & 0xff;
                    int i = res[6];
                    int down = 0;
                    if (((i & 0XFF) > 0) || (i & 0XFF) < 256) {
                        down = (i & 0XFF) + 30;
                    } else {
                        down = (((byte) i) & 0XFF) + 1 + 0xff + 30;
                    }
                    measureListener.onSuccess(getNew+"",down+"",maibo+"",null);
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
                    NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                        @Override
                        public void onSuccess(MeasureResult response) {
                            //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).showShort();
                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {

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
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    sendDataByte(Commands.CMD_LENGTH_TEN, Commands.CMD_CATEGORY_ZERO);
                    break;
                case 1:
                    measureListener.onSuccess((String)msg.obj,null,null,null);
                    break;
                case 2:
                    isGetResustFirst = true;//测量重置标志位
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_measure);
        ButterKnife.bind(this);

        initBase();//初始化基本变量
        initFragment();//初始fragment
        initBluetooth();
    }

    private void initBase() {
        type = getIntent().getStringExtra("type");
        time=getIntent().getIntExtra("time",0);
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.icon_refresh);
        mShared = LocalShared.getInstance(mContext);
        mXueyaResults = mResources.getStringArray(R.array.result_xueya);
        mWenduResults = mResources.getStringArray(R.array.result_wendu);
        mXueYangResults = mResources.getStringArray(R.array.result_xueyang);
        mEcgResults = mResources.getStringArray(R.array.ecg_measureres);

        mRightView.setOnClickListener(this);
    }

    private void initBluetooth() {
        /**
         * 先获取相关权限
         */
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

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        registerBltReceiver();
        startSearch();
    }

    public void registerBltReceiver() {
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        registerReceiver(searchDevices, intent);
    }

    /**
     * 搜索设备的回调
     */
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        @Override
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
                switch (type) {
                    case Type_Wendu:
                        deviceName = "FSRKB-EWQ01";
                        break;
                    case Type_Xueya:
                        deviceName = "eBlood-Pressure";
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
                        break;
                }

                if (type == Type_Xueya && "Dual-SPP".equals(device.getName())) {
                    try {
                        stopSearch();
                        XueyaUtils.connect(device, xueyaHandler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (!TextUtils.isEmpty(device.getName()) && device.getName().startsWith(deviceName)) {
                    if (dialog != null) {
                        dialog.create(NDialog.CONFIRM).dismiss();
                    }
                    mDeviceAddress = device.getAddress();
                    if (mBluetoothLeService == null) {//绑定蓝牙服务
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
    private boolean mConnected = false;
    /**
     * 连接蓝牙设备
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothLeService.ACTION_GATT_CONNECTED://设备连接成功
                    mConnected = true;
                    switch (type) {
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
                        case Type_TiZhong:
                            mShared.setTizhongMac(mDeviceAddress);
                            break;
                    }
                    break;
                case BluetoothLeService.ACTION_GATT_DISCONNECTED://设备断开连接
                    mConnected = false;
                    isGetResustFirst = true;
                    if (mBluetoothLeService != null) {
                        mBluetoothLeService.disconnect();
                        mBluetoothLeService.close();
                    }
                    if (mBluetoothAdapter != null) {
                        startSearch();
                    }
                    break;
                case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED://发现蓝牙设备
                    speak(R.string.tips_blue_connect);
                    displayGattServices(mBluetoothLeService.getSupportedGattServices());
                    switch (type) {
                        case Type_XueTang:
                            XueTangGattAttributes.notify(mBluetoothGatt);
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                            break;
                        case Type_XueYang:

                            break;

                    }
                    break;
                case BluetoothLeService.ACTION_DATA_AVAILABLE://有数据的变化
                    onDataChange(intent);
                    break;
            }
        }
    };

    private void onDataChange(Intent intent) {
        byte[] notifyData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
        switch (type) {
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
                    measureListener.onSuccess(wenduValue+"",null,null,null);
                    mHandler.sendEmptyMessageDelayed(2, 5000);
                    DataInfoBean info = new DataInfoBean();
                    info.temper_ature = String.valueOf(wenduValue);
                    NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                        @Override
                        public void onSuccess(MeasureResult response) {
                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {

                        }
                    });
                }
                speak(String.format(getString(R.string.tips_result_wendu), String.valueOf(wenduValue), wenduResult));
                break;
            case Type_Xueya:
                if ((int) notifyData[0] == 32 && notifyData.length == 2) {
                    measureListener.onSuccess(String.valueOf(notifyData[1] & 0xff),"0","0",null);
                }
                if ((int) notifyData[0] == 12) {
                    measureListener.onSuccess(String.valueOf(notifyData[2] & 0xff),String.valueOf(notifyData[4] & 0xff),String.valueOf(notifyData[8] & 0xff),null);
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
                        NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                            @Override
                            public void onSuccess(MeasureResult response) {
                                //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).showShort();
                            }
                        }, new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {

                            }
                        });
                    }
                }
                StringBuilder mBuilder = new StringBuilder();
                for (byte item : notifyData) {
                    mBuilder.append(item).append("    ");
                }
                break;
            case Type_XueTang://血糖测量
                if (notifyData == null || notifyData.length < 12) {
                    return;
                }
                if (isGetResustFirst) {
                    isGetResustFirst = false;
                    float xuetangResut = ((float) (notifyData[10] << 8) + (float) (notifyData[9] & 0xff)) / 18;
                    measureListener.onSuccess(String.format("%.1f", xuetangResut),null,null,null);
                    switch (time){
                        case 0://空腹
                            if(xuetangResut<3.61){
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏低,请重新测量或联系顾问"));
                            }else if(xuetangResut<=7.0){
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值正常"));
                            }else{
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏高,请重新测量或联系顾问"));
                            }
                            break;
                        case 1://饭后一小时
                            if(xuetangResut<3.61){
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏低,请重新测量或联系顾问"));
                            }else if(xuetangResut<=11.1){
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值正常"));
                            }else{
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏高,请重新测量或联系顾问"));
                            }
                            break;
                        case 2://饭后两小时
                            if(xuetangResut<3.61){
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏低,请重新测量或联系顾问"));
                            }else if(xuetangResut<=7.8){
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值正常"));
                            }else{
                                speak(String.format(getString(R.string.tips_result_xuetang), String.format("%.1f", xuetangResut),"血糖值偏高,请重新测量或联系顾问"));
                            }
                            break;
                    }

                    DataInfoBean info = new DataInfoBean();
                    info.blood_sugar = String.format("%.1f", xuetangResut);
                    info.sugar_time=time+"";
                    NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                        @Override
                        public void onSuccess(MeasureResult response) {
                            //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).showShort();
                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {

                        }
                    });
                }
                break;
            case Type_XueYang:
                if (notifyData != null && notifyData.length == 12 && notifyData[5] != 0) {
                    threadDisable = false;
                    measureListener.onSuccess(String.valueOf(notifyData[5]),String.valueOf(notifyData[6]),null,null);
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
                        NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                            @Override
                            public void onSuccess(MeasureResult response) {
                                //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).showShort();
                            }
                        }, new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {

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
                        measureListener.onSuccess(String.valueOf(result),null,null,null);
                        String  height_s=LocalShared.getInstance(OnMeasureActivity.this).getUserHeight();
                        float height=TextUtils.isEmpty(height_s)?0:Float.parseFloat(height_s)/100;
                        if(height!=0)
                            ((TextView)findViewById(R.id.tv_tizhi)).setText(String.format("%1$.2f",result/(height*height)));
                        speak(String.format(getString(R.string.tips_result_tizhong), String.format("%.1f", result)));
                        DataInfoBean info = new DataInfoBean();
                        info.weight = result;
                        NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                            @Override
                            public void onSuccess(MeasureResult response) {
                                //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).showShort();
                            }
                        }, new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {

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
                NetworkApi.postData(ecgInfo, new NetworkManager.SuccessCallback<MeasureResult>() {
                    @Override
                    public void onSuccess(MeasureResult response) {
                        //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).showShort();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {

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
                        measureListener.onSuccess(String.valueOf(afterResult),null,null,null);
                        speak(String.format(getString(R.string.tips_result_xuetang), String.valueOf(afterResult), "正常"));
                    } else if (notifyData[1] == 81) {//尿酸
                        info.uric_acid = String.valueOf(afterResult);
                        measureListener.onSuccess(String.valueOf(afterResult),null,null,null);
                        speak(String.format(getString(R.string.tips_result_niaosuan), String.valueOf(afterResult), "正常"));
                    } else if (notifyData[1] == 97) {//胆固醇
                        info.cholesterol = String.valueOf(afterResult);
                        measureListener.onSuccess(String.valueOf(afterResult),null,null,null);
                        speak(String.format(getString(R.string.tips_result_danguchun), String.valueOf(afterResult), "正常"));
                    }
                    NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                        @Override
                        public void onSuccess(MeasureResult response) {
                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {

                        }
                    });
                }
                break;
        }
    }

    private void startSearch() {
        switch (type) {
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
            case Type_TiZhong:
                mDeviceAddress = mShared.getTizhongMac();
                dialog = new NDialog(this);
                break;
        }
        if (mBluetoothLeService == null) {
            Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
        workSearchThread = true;
        if (mSearchThread == null) {
            mSearchThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (blueThreadDisable) {
                        if (!workSearchThread) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }
                        if (TextUtils.isEmpty(mDeviceAddress)) {
                            if (!mBluetoothAdapter.isDiscovering()) {
                                boolean flag = mBluetoothAdapter.startDiscovery();
                            }
                        } else {
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
                    byte[] sendDataByte = Commands.datas;
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


    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        BluetoothGattCharacteristic characteristic = null;
        switch (type) {
            case Type_Wendu:
                List<BluetoothGattCharacteristic> characteristicsList = gattServices.get(3).getCharacteristics();
                if (characteristicsList.size() == 3) {
                    characteristic = characteristicsList.get(1);//新版本耳温枪
                } else {
                    characteristic = characteristicsList.get(3);//旧版本耳温枪
                }
                break;
            case Type_Xueya:
                if (gattServices.size() == 5 || gattServices.size() == 10) {
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
                BluetoothGattService service = mBluetoothLeService.getGatt().getService(UUID
                        .fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
                characteristic = service
                        .getCharacteristic(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
                break;
            case Type_SanHeYi:
                characteristic = gattServices.get(4).getCharacteristics().get(0);
                break;
        }
        if (characteristic == null) {
            return;
        }
        if (type == Type_TiZhong) {
            setCharacterValue(characteristic, characteristic, 0);
            return;
        }
        mWriteCharacteristic = characteristic;
        switch (type) {
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
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
    }

    private void setCharacterValue(BluetoothGattCharacteristic characteristic,
                                   BluetoothGattCharacteristic characteristic1, int status) {
        // 激活通知
        final int charaProp = characteristic.getProperties();
        int charaProp_second = -1;
        if (characteristic1 != null) {
            charaProp_second = characteristic1.getProperties();
        }
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mBluetoothGatt.setCharacteristicNotification(
                        characteristic, true);
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(UUID
                                .fromString("00002902-0000-1000-8000-00805f9b34fb"));
                if (descriptor != null) {
                    descriptor
                            .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                }
                if (characteristic1 == null)
                    return;
                if ((charaProp_second & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    mBluetoothGatt.setCharacteristicNotification(
                            characteristic1, true);
                    if (descriptor != null) {
                        descriptor
                                .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    }
                }
                if (descriptor != null) {
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.i("mylog", "service connected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            if (TextUtils.isEmpty(mDeviceAddress)) {
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

    private void initFragment() {
        switch (type) {
            case "wendu":
                fragment = new TiwenFragment();
                mTitleText.setText("体温测量");
                break;
            case "xueya":
                fragment = new XueyaFragment();
                mTitleText.setText("血压测量");
                break;
            case "xuetang":
                fragment = new XuetangFragment();
                mTitleText.setText("血糖测量");
                break;
            case "xueyang":
                fragment = new XueyangFragment();
                mTitleText.setText("血氧测量");
                break;
            case "xindian":
                fragment = new XindianFragment();
                mTitleText.setText("心电测量");
                break;
            case "tizhong":
                fragment = new TizhongFragment();
                mTitleText.setText("体重测量");
                break;
            case "sanheyi":
                fragment = new SanheyiFragment();
                mTitleText.setText("三合一测量");
                break;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_right://刷新蓝牙
                new AlertDialog.Builder(mContext).setMessage("是否匹配新设备").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDeviceAddress = "";
                        switch (type) {
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
                            case Type_TiZhong:
                                mShared.setTizhongMac("");
                                break;
                        }
                    }
                }).show();
                break;
        }
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

    private MeasureListener measureListener;

    public void setOnMeasureListener(MeasureListener measureListener) {
        this.measureListener = measureListener;
    }
}
