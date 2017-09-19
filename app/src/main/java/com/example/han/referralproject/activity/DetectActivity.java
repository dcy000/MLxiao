package com.example.han.referralproject.activity;

import android.Manifest;
import android.app.Activity;
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
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.PlayVideoActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bluetooth.BluetoothLeService;
import com.example.han.referralproject.bluetooth.Commands;
import com.example.han.referralproject.bluetooth.SampleGattAttributes;
import com.example.han.referralproject.bluetooth.XueTangGattAttributes;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.iflytek.cloud.thirdparty.V;
import com.megvii.faceppidcardui.util.ConstantData;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DetectActivity extends BaseActivity implements View.OnClickListener{

    private BluetoothAdapter mBluetoothAdapter;
    int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private String mDeviceAddress;
    private final static String TAG = DetectActivity.class.getSimpleName();
    private BluetoothLeService mBluetoothLeService;
    public boolean threadDisable = true;
    public String str;
    public TextView mResultTv;
    public TextView mHighPressTv, mLowPressTv, mPulseTv;
    NDialog dialog;
    private BluetoothGatt mBluetoothGatt;

    private String detectType = Type_XueTang;
    public static final String Type_Wendu = "wendu";
    public static final String Type_Xueya = "xueya";
    public static final String Type_XueTang = "xuetang";
    private boolean isGetResustFirst = true;
    private String[] mXueyaResults;
    private String[] mWenduResults;


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
                    isGetResustFirst = true;//温度测量重置标志位
                    break;
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                if (Type_XueTang.equals(detectType)){
                    XueTangGattAttributes.notify(mBluetoothGatt);
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                }
                Log.i("mylog", "gata connect 11111111111111111111");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i("mylog", "gata disConnect 22222222222222222");
                mConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i("mylog", "gata servicesConnect 3333333333333333");
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                if (detectType == Type_XueTang) {
                    mHandler.sendEmptyMessage(0);
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.i("mylog", "receive>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                byte[] notifyData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                switch (detectType) {
                    case Type_Wendu:
                        int tempData = notifyData[6] & 0xff;
                        if (tempData == 1) {
                            return;
                        }
                        StringBuilder mTempResult = new StringBuilder();
                        mTempResult.append("3").append((tempData - 44)/10).append(".").append((tempData - 44)%10);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = mTempResult.toString();
                        mHandler.sendMessage(msg);
                        String wenduResult;
                        float wenduValue = Float.valueOf(mTempResult.toString());
                        if (wenduValue < 38){
                            wenduResult = mWenduResults[0];
                        } else if (wenduValue < 40){
                            wenduResult = mWenduResults[1];
                        } else {
                            wenduResult = mWenduResults[2];
                        }
                        if (isGetResustFirst) {
                            isGetResustFirst = false;
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
                        speak(String.format(getString(R.string.tips_result_wendu), mTempResult.toString(), wenduResult));
                        break;
                    case Type_Xueya:
                        if ((int)notifyData[0] == 32 && notifyData.length == 2) {
                            mHighPressTv.setText(String.valueOf(notifyData[1] & 0xff));
                        }
                        if ((int)notifyData[0] == 12){
                            mHighPressTv.setText(String.valueOf(notifyData[2] & 0xff));
                            mLowPressTv.setText(String.valueOf(notifyData[4] & 0xff));
                            mPulseTv.setText(String.valueOf(notifyData[8] & 0xff));
                            if (isGetResustFirst){
                                String xueyaResult;
                                if ((notifyData[2] & 0xff) <= 140){
                                    xueyaResult = mXueyaResults[0];
                                } else if ((notifyData[2] & 0xff) <= 160){
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
                                isGetResustFirst = false;
                            }
                        }
                        StringBuilder mBuilder = new StringBuilder();
//                        for (char item : xueyaChars){
//                            mBuilder.append(item).append("(").append((byte)item).append(")").append("    ");
//                        }
                        for (byte item : notifyData){
                            mBuilder.append(item).append("    ");
                        }
                        Log.i("mylog", mBuilder.toString());
                        break;
                }
            }
        }
    };


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
            case Type_Xueya:
                characteristic = gattServices.get(3).getCharacteristics().get(3);
                break;
        }

        if (characteristic == null){
            return;
        }
        mBluetoothLeService.writeCharacteristic(characteristic);
        mBluetoothLeService.readCharacteristic(characteristic);
        mBluetoothLeService.setCharacteristicNotification(characteristic, true);
        List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
        if(descriptorList != null && descriptorList.size() > 0) {
            for(BluetoothGattDescriptor descriptor : descriptorList) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
        Log.i("mylog", "chara uuid : " + characteristic.getUuid() + "\n" + "service uuid : " + gattServices.get(3).getUuid());
//        boolean isSuccess = readMessage();
//        Log.i("mylog1", "is Success " + isSuccess);
    }

    @Override
    public void onClick(View v) {
        String url = "";
        switch (v.getId()) {
            case R.id.temperature_video:
                url = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName() + "/电子温度计.mp4";
                break;
            case R.id.xueya_video:
                url = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName() + "/血压计.mp4";
                break;
        }
        Intent intent = new Intent(mContext, PlayVideoActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public ImageView mImageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)){
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
            }
        }
        switch (detectType) {
            case Type_Wendu:
                findViewById(R.id.rl_temp).setVisibility(View.VISIBLE);
                break;
            case Type_Xueya:
                findViewById(R.id.rl_xueya).setVisibility(View.VISIBLE);
                break;
            case Type_XueTang:
                findViewById(R.id.rl_xuetang).setVisibility(View.VISIBLE);
                break;
        }
        mResultTv = (TextView) findViewById(R.id.tv_result);
        mHighPressTv = (TextView) findViewById(R.id.high_pressure);
        mLowPressTv = (TextView) findViewById(R.id.low_pressure);
        mPulseTv = (TextView) findViewById(R.id.pulse);
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

        mBluetoothAdapter.startDiscovery();
        mXueyaResults = mResources.getStringArray(R.array.result_xueya);
        mWenduResults = mResources.getStringArray(R.array.result_wendu);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (threadDisable) {
//                    if (mBluetoothLeService == null && mConnected == false) {
//                        mBluetoothAdapter.startLeScan(mLeScanCallback);
//                    } else {
//                        mHandler.sendEmptyMessage(0);
//                        threadDisable = false;
//                    }
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                    }
//
//                }
//            }
//
//        }).start();
        speak(R.string.tips_open_device);
        findViewById(R.id.temperature_video).setOnClickListener(this);
        findViewById(R.id.xueya_video).setOnClickListener(this);
        dialog = new NDialog(this);
        showNormal("设备连接中，请稍后...");

    }

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
                switch (detectType){
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
                    dialog.create(NDialog.CONFIRM).dismiss();
                    mDeviceAddress = device.getAddress();
                    Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
                    bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    if (mBluetoothAdapter != null){
                        mBluetoothAdapter.cancelDiscovery();
                    }
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
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * 向设备发送命令
     *
     * @param 包长度
     * @param 包类别
     */
    private void sendDataByte(final byte leng, final byte commandType) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (threadDisable){
//
//                }
//            }
//        }).start();
        Commands commands = new Commands();
        //byte[] sendDataByte = commands.getSystemdate(Commands.CMD_HEAD, leng, commandType);
        byte[] sendDataByte = Commands.datas;
        Log.i("mylog", " sendData : " + bytesToHexString(sendDataByte));
        XueTangGattAttributes.sendMessage(mBluetoothGatt, sendDataByte);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String bytesToHexString(byte[] src){
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
            stringBuilder.append(hv);
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadDisable = false;
        unregisterReceiver(mGattUpdateReceiver);

        unregisterReceiver(searchDevices);
        if (mBluetoothAdapter != null){
            mBluetoothAdapter.cancelDiscovery();
        }

        if (mBluetoothLeService != null) {

            unbindService(mServiceConnection);

        }
        mBluetoothLeService = null;
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
                        switch (detectType){
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
                            dialog.create(NDialog.CONFIRM).dismiss();
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

}
