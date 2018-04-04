package com.gzq.administrator.idcardreader;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.OnClientCallback;
import com.kaer.sdk.bt.BtReadClient;
import com.kaer.sdk.bt.OnBluetoothListener;
import com.kaer.sdk.utils.CardCode;
import com.kaer.sdk.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice targetDevice;
    private boolean isSearching = false;//标识当前是否正在进行设备的搜索
    private BtReadClient mBtReadClient;
    private ReadIdCardTask readIdCardTask;
    private volatile boolean isLoop=true;
    private boolean isFirstConnectBlue=true;//防止开启多个读卡子线程
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 500) {
//                postResult((IDCardItem) msg.obj);
                //print("蓝牙已断开!");
//                btnConnect.setText("连接");
            } else if (msg.what == 600) {
                if (msg.arg1 == 101) {
//                    intensityTv.setText("蓝牙未连接");
                } else if (msg.arg1 != 100 && msg.arg1 != 102) {
//                    intensityTv.setText("信号强度:" + msg.arg1);
                }
            } else if (msg.what == 700) {
                if (msg.arg1 == 1) {
                    MLVoiceSynthetize.startSynthesize(MainActivity.this, "蓝牙连接成功", false);
                    if (isFirstConnectBlue){
                        isFirstConnectBlue=false;
                        //开启一个异步线程读取卡信息
                        readIdCardTask = new ReadIdCardTask();
                        readIdCardTask.execute();
                    }
                } else {
                    MLVoiceSynthetize.startSynthesize(MainActivity.this, "蓝牙连接断开", false);
                }
            } else if (msg.what == 800) {
//                timeTv.setText(msg.obj.toString());
            } else if (msg.what == 900) {
//                print(getEInfoByCode(msg.arg1));

            } else if (msg.what == 1000) {
//                print(msg.obj.toString());

            }
        }
    };
    private TextView mMessage;
    private ImageView mImg;

    private void initView() {
        mMessage = (TextView) findViewById(R.id.message);
        mImg = (ImageView) findViewById(R.id.img);
    }

    private class ReadIdCardTask extends AsyncTask<Void, Void, IDCardItem> {

        @Override
        protected IDCardItem doInBackground(Void... voids) {
            IDCardItem item = null;
            if (mBtReadClient != null) {
                while (isLoop) {
                    connectDevice();
                    Log.e("开始读卡","ddddd");
                    item = mBtReadClient.readCert(0);
                    if (item != null && item.retCode == 1) {
                        isLoop = false;
                        break;
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MLVoiceSynthetize.startSynthesize(MainActivity.this,"主人，未识别到身份证，请将您的二代身份证放置到读卡器上",false);
                            }
                        });
                        Log.e("未读到卡","1111");
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("未读到卡","22222");
                        disConnectDevice();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("未读到卡","33333");

                    }
                }
            }
            return item;
        }

        @Override
        protected void onPostExecute(IDCardItem idCardItem) {
            if (idCardItem != null) {
                if (idCardItem.retCode == 1) {
                    Log.e("读卡成功","ggg");
                    updateView(idCardItem);
                } else if (idCardItem.retCode == 128) {

                }
            } else {
                Log.e("读卡失败","ffff");
                MLVoiceSynthetize.startSynthesize(MainActivity.this, "读卡失败，请重启读卡器", false);
            }
        }
    }

    private void updateView(IDCardItem item) {
        StringBuilder sb = new StringBuilder();
        sb.append("姓名:" + item.partyName + "\n");
        sb.append("性别:" + item.gender + "\n");
        sb.append("民族:" + item.nation + "\n");
        sb.append("出生:" + item.bornDay + "\n");
        sb.append("住址:" + item.certAddress + "\n");
        sb.append("证件号:" + item.certNumber + "\n");
        sb.append("签发机关:" + item.certOrg + "\n");
        String effDate = item.effDate;
        String expDate = item.expDate;
        sb.append("有效期限:" + effDate.substring(0, 4) + "." + effDate.substring(4, 6) + "." + effDate.substring(6, 8)
                + "-" + expDate.substring(0, 4) + "." + expDate.substring(4, 6) + "." + expDate.substring(6, 8) + "\n");
        if (!TextUtils.isEmpty(item.uuid))
            sb.append("唯一标识:" + item.uuid + "\n");
        if (!TextUtils.isEmpty(item.dn))
            sb.append("dn:" + item.dn + "\n");
        if (!TextUtils.isEmpty(item.timeTag))
            sb.append("timeTag:" + item.timeTag + "\n");
        if (!TextUtils.isEmpty(item.nfcSignature))
            sb.append("nfcSignature:" + item.nfcSignature + "\n");
        mMessage.setText(sb);
        if (item.picBitmap != null) {
            mImg.setImageBitmap(scale(item.picBitmap));
        }
    }

    protected Bitmap scale(Bitmap bitmap) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        int width = displaysMetrics.widthPixels;
        Matrix matrix = new Matrix();
        float scale = width / (3.0f * bitmap.getWidth());
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility.createUtility(this, builder.toString());
        mBtReadClient = BtReadClient.getInstance();
        mBtReadClient.setBluetoothListener(bluetoothListener);
//        MLVoiceSynthetize.startSynthesize(this, "测试", false);

        checkConnectedDevice();
        registBroadcastReceiver();
    }

    private void checkConnectedDevice() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        Log.e("已经配对的设备", devices.toString());
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                if (bluetoothDevice.getName().toUpperCase().startsWith("KT8000")) {
                    targetDevice = bluetoothDevice;
                    break;
                }
            }
        }
        //如果没有在已经配对的设备中找到目标设备则开始搜索
        if (targetDevice == null) {
            searchNewDevices();
        } else {
            connectDevice();
        }

    }

    //连接设备
    private void connectDevice() {
        if (mBtReadClient.getBtState() == 0) {
            mBtReadClient.connectBt(targetDevice.getAddress());
        }
    }

    private void disConnectDevice() {
        if (mBtReadClient.getBtState() == 2) {
            mBtReadClient.disconnectBt();
            mBtReadClient.disconnect();
        }
    }

    private void searchNewDevices() {
        if (isSearching) {
            isSearching = false;
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        } else {
            isSearching = true;
            bluetoothAdapter.startDiscovery();
        }
    }

    private void stopSearchNewDevices() {
        if (isSearching) {
            isSearching = false;
            bluetoothAdapter.cancelDiscovery();
        }
    }

    private void registBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregistBroadcastReceiver() {
        this.unregisterReceiver(broadcastReceiver);
        disConnectDevice();
    }

    // 接收设备的接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println(action);
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (device.getName().toUpperCase().startsWith("KT8000")) {
                        stopSearchNewDevices();
                        targetDevice = device;
                    }
                }
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (targetDevice != null) {//开始配对
                    try {
                        Method createBond = BluetoothDevice.class
                                .getMethod("createBond");
                        createBond.invoke(targetDevice);
                        //	loading.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "无法执行配对",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BOND_BONDING", "正在配对");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BOND_BONDED", "完成配对");
                        //		loading.dismiss();
                        Toast.makeText(MainActivity.this, "配对完成",
                                Toast.LENGTH_SHORT).show();
                        connectDevice();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BOND_NONE", "取消配对");
                        Toast.makeText(MainActivity.this, "配对已取消",
                                Toast.LENGTH_SHORT).show();

                        //	loading.dismiss();
                    default:
                        break;
                }
            } else if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                BluetoothDevice mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    //3.调用setPin方法进行配对...
                    //    boolean ret = ClsUtils.setPin(mBluetoothDevice.getClass(), mBluetoothDevice, "1234");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private OnBluetoothListener bluetoothListener = new OnBluetoothListener() {
        @Override
        public void connectResult(boolean result) {
            int what = result ? 1 : 0;
            mHandler.obtainMessage(700, what, what).sendToTarget();
        }

        @Override
        public void connectionLost() {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistBroadcastReceiver();
        readIdCardTask.cancel(true);
    }
}
