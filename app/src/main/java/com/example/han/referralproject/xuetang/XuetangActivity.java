package com.example.han.referralproject.xuetang;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
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
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.PlayVideoActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bluetooth.BluetoothLeService;
import com.example.han.referralproject.bluetooth.SampleGattAttributes;
import com.example.han.referralproject.temperature.TemperatureActivity;
import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.library.utils.Utils;
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

public class XuetangActivity extends BaseActivity {

    //   LineChart _lineChart1;
    public ImageView mImageView;

    private BluetoothAdapter mBluetoothAdapter;
    int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private String mDeviceAddress;
    private final static String TAG = XuetangActivity.class.getSimpleName();
    private BluetoothLeService mBluetoothLeService;
    public boolean threadDisable = true;
    public String str;
    public TextView mTextView;
    NDialog dialog;
    public String str1 = null;
    public String sign;
    public boolean sign1 = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    /*if (mPb.getVisibility() == View.VISIBLE) {
                        mPb.setVisibility(View.INVISIBLE);
                    }*/
                    sendDataToBLE(DEVICE1_ON);
                    //Toast.makeText(getApplicationContext(), "连接完成，请点击测试", Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    str1 = (String) msg.obj;
                    if (str1 != null) {
                        if ("OK".equals(str1)) {
                            dialog.create(NDialog.CONFIRM).dismiss();
                            speak(R.string.tips_open_device);
                            return;
                        }
                        mTextView.setText(str1);
                      /*  if ("1".equals(strs[3]) && sign1 == true) {
                          *//*  new Thread(new Runnable() {
                        try {
                            double temp = Double.parseDouble(str);
                            mTextView.setText(str1);
                        } catch (Exception e) {
                            return;
                        }
//                        final String[] strs = str1.split(",");
//                        mTextView.setText(strs[0]);
//                        sign = strs[3];

//                        if ("1".equals(strs[3]) && sign1 == true) {
                          /*  new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        posts(strs[0]);
                                        sign1 = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            }).start();*//*


                        }*/
//                        }

                    }
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
            Log.e("---------", content);
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
            mBluetoothLeService.connect(mDeviceAddress);
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

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;


            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                str = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.i("mylog", "receiver  " + str);
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = str;
                mHandler.sendMessage(msg);
            }


        }
    };

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));


            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
            characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
        }

    }


    public ImageView mImageView1;

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    //ProgressBar mPb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xue_tang);
        //      mPb = (ProgressBar) findViewById(R.id.pb);


        mTextView = (TextView) findViewById(R.id.xue_tang);
        mImageView1 = (ImageView) findViewById(R.id.test_2);
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
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }

                }
            }

        }).start();


        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;

                    if (mBluetoothLeService != null && mConnected == true) {
                        sendDataToBLE(DEVICE1_ON);
                        Toast.makeText(getApplicationContext(), "已经开始测试！", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "请先开启设备再进行测试", Toast.LENGTH_SHORT).show();
                    }
                }


            }

        });


   /*     _lineChart1 = (LineChart) findViewById(R.id.xuetang_chart);
        CheckBox cb1 = (CheckBox) findViewById(R.id.xuetang_cb);
        CheckBox cb_fill = (CheckBox) findViewById(R.id.cb_fill_xuetang);

        setChartData(_lineChart1);*/

        mImageView = (ImageView) findViewById(R.id.xuetang_video);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayVideoActivity.class);
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName() + "/血糖.mp4");
                startActivity(intent);

            }
        });
      /*  cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HighLight highLight = _lineChart1.get_HighLight();
                highLight.setEnable(isChecked);// 启用高亮显示  默认为启用状态
                _lineChart1.invalidate();

            }
        });

        cb_fill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Lines lines = _lineChart1.getlines();
                if (lines != null && lines.getLines().size() > 0) {
                    Line line = lines.getLines().get(0);
                    line.setFilled(isChecked);
                    _lineChart1.invalidate();
                }
            }
        });*/

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

   /* private void setChartData(LineChart lineChart) {


        // 高亮
        HighLight highLight = lineChart.get_HighLight();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "日期:   " + value;
            }
        });

        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "血糖:  " + value;
            }
        });


        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("日期");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：mmol/L");
        yAxis.set_ValueAdapter(new DefaultValueAdapter(2));// 默认精度到小数点后2位,现在修改为3位精度

        // 数据
        Line line = new Line();
        line.setLineColor(Color.RED);
        List<Entry> list = new ArrayList<>();
        list.add(new Entry(1, 36.5));
        list.add(new Entry(2, 37));
        list.add(new Entry(3, 37.5));
        list.add(new Entry(4, 37.2));
        list.add(new Entry(10, 37.3));
        line.setEntries(list);
        line.setDrawLegend(true);//设置启用绘制图例
        line.setLegendTextSize((int) Utils.dp2px(10));//设置图例上的字体大小
        line.setName("血糖");


        Lines lines = new Lines();


        lines.addLine(line);


        lineChart.setLines(lines);

    }*/


    public String DEVICE1_ON = "EEEEE\n";


    @Override
    protected void onResume() {
        super.onResume();


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


    }


    void sendDataToBLE(String str) {
        Log.i("mylog", "Sending result : " + str);
        final byte[] tx = str.getBytes();
        if (mConnected) {
            if (characteristicTX == null || mBluetoothLeService == null){
                return;
            }
            characteristicTX.setValue(tx);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
        }
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
                        //    Log.e("===============", device.getName());

                        if ("Med_link".equals(device.getName())) {
                            mDeviceAddress = device.getAddress();
                            Intent gattServiceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
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
