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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.PlayVideoActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bluetooth.BluetoothLeService;
import com.example.han.referralproject.bluetooth.Commands;
import com.example.han.referralproject.bluetooth.SampleGattAttributes;
import com.example.han.referralproject.bluetooth.XueTangGattAttributes;
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

public class DetectActivity extends BaseActivity {

    public ImageView mImageView;

    private BluetoothAdapter mBluetoothAdapter;
    int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private String mDeviceAddress;
    private final static String TAG = DetectActivity.class.getSimpleName();
    private BluetoothLeService mBluetoothLeService;
    public boolean threadDisable = true;
    public String str;
    public TextView mTextView;
    NDialog dialog;
    private BluetoothGatt mBluetoothGatt;

    private String detectType = Type_Xueya;
    public static final String Type_Wendu = "wendu";
    public static final String Type_Xueya = "xueya";
    public static final String Type_XueTang = "xuetang";


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    sendDataByte(Commands.CMD_LENGTH_TEN, Commands.CMD_CATEGORY_ZERO);
                    break;
                case 1:
                    mTextView.setText((String) msg.obj);
//                    if (str1 != null) {
//                        if ("OK".equals(str)){
//                            dialog.create(NDialog.CONFIRM).dismiss();
//                            speak(R.string.tips_open_device);
//                            return;
//                        }
//                        mTextView.setText(str1);
//                    }
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "检测完成", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "请重新检测", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void posts(String bloodSuger) throws Exception {
        // 创建URL对象
        URL url = new URL(ConstantData.BASE_URL + "/referralProject/AddInfoServlet");
        // 获取该URL与服务器的连接对象
        URLConnection conn = url.openConnection();
        // 设置头信息，请求头信息了解
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");

        // 设置可以操作连接的输入输出流
        conn.setDoOutput(true);// 默认为false，允许使用输出流
        conn.setDoInput(true);// 默认为true，允许使用输入流


        // 传参数
        PrintWriter pw = new PrintWriter(conn.getOutputStream());
        pw.print("data=" + bloodSuger);
        pw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String lineContent = null;
        String content = null;

        while ((lineContent = br.readLine()) != null) {
            content = lineContent;
        }

        if (content.equals("0"))
            mHandler.sendEmptyMessage(2);
        else {
            mHandler.sendEmptyMessage(3);
        }
        pw.close();
        br.close();

    }

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
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                String data=intent.getStringExtra("devicename_status");
                Log.i("mylog", "receiver    ");
                byte[] notify = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_NOTIFY_DATA);

                String str = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                byte[] data = str.getBytes();
                switch (detectType) {
                    case Type_Wendu:
                        int tempData = (int)str.toCharArray()[6];
                        StringBuilder mTempResult = new StringBuilder();
                        mTempResult.append("3").append((tempData - 44)/10).append(".").append((tempData - 44)%10);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = mTempResult.toString();
                        mHandler.sendMessage(msg);
                        break;
                    case Type_Xueya:

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


    public ImageView mImageView1;

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    //ProgressBar mPb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xue_tang);
        mTextView = (TextView) findViewById(R.id.xue_tang);
        mImageView1 = (ImageView) findViewById(R.id.test_2);
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(0);
            }
        });
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadDisable) {
                    if (mBluetoothLeService == null && mConnected == false) {
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    } else {
                        mHandler.sendEmptyMessage(0);
                        threadDisable = false;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                }
            }

        }).start();

        mImageView = (ImageView) findViewById(R.id.xuetang_video);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayVideoActivity.class);
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName() + "/血糖.mp4");
                startActivity(intent);

            }
        });
        dialog = new NDialog(this);
        showNormal("设备连接中，请稍后...");

    }

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
        Commands commands = new Commands();
        byte[] sendDataByte = commands.getSystemdate(Commands.CMD_HEAD, leng, commandType);
        Log.i("mylog", " sendData : " + bytesToHexString(sendDataByte));
        XueTangGattAttributes.sendMessage(mBluetoothGatt, sendDataByte);
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
        unregisterReceiver(mGattUpdateReceiver);

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
