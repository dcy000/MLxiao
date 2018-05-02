package com.example.han.referralproject.xueya;

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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bluetooth.BluetoothLeService;
import com.example.han.referralproject.bluetooth.SampleGattAttributes;
import com.linheimx.app.library.charts.LineChart;
import com.megvii.faceppidcardui.util.ConstantData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XueyaActivity extends BaseActivity {


    private final static int LINE_NUM = 2;

    LineChart _lineChart;
    LineChart _lineChart1;
    public ImageView mImageView;
    public TextView mTextView;
    public TextView mTextView1;
    public TextView mTextView2;
    public String sign;


    private BluetoothAdapter mBluetoothAdapter;
    int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private String mDeviceAddress;
    private final static String TAG = XueyaActivity.class.getSimpleName();
    private BluetoothLeService mBluetoothLeService;
    public boolean threadDisable = true;
    public String str;
    //   ProgressBar mPb;
    public String str1 = null;
    SharedPreferences sharedPreferences;
    public String mAuthid;
    NDialog dialog;
    public boolean sign1 = true;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                  /*  if (mPb.getVisibility() == View.VISIBLE) {
                        mPb.setVisibility(View.INVISIBLE);
                    }*/
                    sendDataToBLE(DEVICE1_ON);
//                    Toast.makeText(getApplicationContext(), "连接完成，请点击测试", Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    str1 = (String) msg.obj;
                    if (str1 != null) {
                       /* if ("OK".equals(str)) {
                            dialog.create(NDialog.CONFIRM).dismiss();
                            speak(R.string.tips_open_device);
                            return;
                        }*/
                        final String[] strs = str1.split(",");
                        mTextView.setText(strs[0]);
                        mTextView1.setText(strs[1]);
                        mTextView2.setText(strs[2]);
                        sign = strs[3];

                        if ("1".equals(strs[3]) && sign1 == true) {
                           /* new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        posts(strs[0], strs[1], strs[2]);
                                        sign1 = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();*/

                        }
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
    public ImageView ivBack;


    private void posts(String high, String low, String pulse) throws Exception {
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
        pw.print("data=" + mAuthid + "," + high + "," + low + "," + pulse);
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
    public String DEVICE1_ON = "AAAAA\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xueya);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sharedPreferences = getSharedPreferences(ConstantData.SHARED_FILE_NAME4, Context.MODE_PRIVATE);

        mAuthid = sharedPreferences.getString("mAuthid", "");

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


       /* new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadDisable) {
                    if (mBluetoothLeService == null && mConnected == false) {
                        mPb.setVisibility(View.VISIBLE);
                    } else {
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                        mPb.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "请开启设备进行连接", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }

                }
            }

        }).start();*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadDisable) {
                    if (mBluetoothLeService == null && mConnected == false) {
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    } else {
                        //    mHandler.sendEmptyMessage(0);
                        threadDisable = false;
                        dialog.create(NDialog.CONFIRM).dismiss();
                        speak(R.string.tips_open_device);
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }

                }
            }

        }).start();


        //   _lineChart = (LineChart) findViewById(R.id.chart);

      /*  _lineChart1 = (LineChart) findViewById(R.id.chart1);
        CheckBox cb1 = (CheckBox) findViewById(R.id.cb_cb1);
        CheckBox cb_fill = (CheckBox) findViewById(R.id.cb_fill);*/

        //    setChartData(_lineChart1);


       /* cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


//        setChartData(_lineChart, LINE_NUM);

   /*     CheckBox cb = (CheckBox) findViewById(R.id.cb_cb);

        // 2. 点击折线上的点 ，回调
        cb.setChecked(true);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                HighLight highLight = _lineChart.get_HighLight();
                highLight.setEnable(isChecked);// 启用高亮显示  默认为启用状态
                _lineChart.invalidate();

                Lines lines = _lineChart.getlines();

            }
        });*/

      /*  dialog = new NDialog(this);
        showNormal("设备连接中，请稍后...");*/

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
    protected void onStart() {
        super.onStart();

        mTextView = (TextView) findViewById(R.id.high_pressure);
        mTextView1 = (TextView) findViewById(R.id.low_pressure);
        mTextView2 = (TextView) findViewById(R.id.pulse);

        //mPb = (ProgressBar) findViewById(R.id.pb);

        mImageView1 = (ImageView) findViewById(R.id.test_1);
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothLeService != null && mConnected == true) {
                    sendDataToBLE(DEVICE1_ON);
                    Toast.makeText(getApplicationContext(), "已开始测试！", Toast.LENGTH_SHORT).show();
                } else {
                    finish();

                    Toast.makeText(getApplicationContext(), "设备断开，请重新连接", Toast.LENGTH_SHORT).show();

                }
            }
        });


       /* headImg = (ImageView) findViewById(R.id.xueya_video);
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayVideoActivity.class);
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName() + "/血压计.mp4");
                startActivity(intent);

            }
        });*/

    }

    /* private void setChartData(LineChart lineChart, int lineCount) {

        // 高亮
        HighLight highLight = lineChart.get_HighLight();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态，每条折线图想要获取点击回调，highlight需要启用
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "日期: " + value;
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "血压: " + Math.round(value);
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("日期");

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：mmol/L");

        Lines lines = new Lines();


        // 线的颜色
        int color = Color.parseColor("#FF0000");
        //#228B22
        Line line = createLine(1, color);
        lines.addLine(line);


        // 线的颜色
        int color1 = Color.parseColor("#228B22");
        //#228B22
        Line line1 = createLine(2, color1);
        lines.addLine(line1);


        lineChart.setLines(lines);
    }*/

   /* private Line createLine(int order, int color) {

        final Line line = new Line();
        List<Entry> list = new ArrayList<>();

        if (order == 1) {

            double x = 3.15;
            double y = 140;
            list.add(new Entry(x, y));

            double x1 = 3.16;
            double y1 = 143;
            list.add(new Entry(x1, y1));

            double x2 = 3.17;
            double y2 = 138;
            list.add(new Entry(x2, y2));

            double x3 = 3.18;
            double y3 = 145;
            list.add(new Entry(x3, y3));

            double x4 = 3.20;
            double y4 = 148;
            list.add(new Entry(x4, y4));

        } else if (order == 2) {

            double x = 3.15;
            double y = 80;
            list.add(new Entry(x, y));

            double x1 = 3.16;
            double y1 = 88;
            list.add(new Entry(x1, y1));

            double x2 = 3.17;
            double y2 = 85;
            list.add(new Entry(x2, y2));

            double x3 = 3.18;
            double y3 = 83;
            list.add(new Entry(x3, y3));

            double x4 = 3.20;
            double y4 = 82;
            list.add(new Entry(x4, y4));

        }


        line.setEntries(list);
        line.setDrawLegend(true);//设置启用绘制图例
        line.setLegendTextSize((int) Utils.dp2px(10));//设置图例上的字体大小
        if (order == 1) {
            line.setName("高压");
        } else {
            line.setName("低压");

        }
        line.setLineColor(color);

        return line;
    }*/

   /* private void setChartData(LineChart lineChart) {


        // 高亮
        HighLight highLight = lineChart.get_HighLight();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "日期: " + value;
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "脉搏: " + value;
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("日期");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：次/分");
        yAxis.set_ValueAdapter(new DefaultValueAdapter(2));// 默认精度到小数点后2位,现在修改为3位精度

        // 数据
        Line line = new Line();
        line.setLineColor(Color.RED);
        List<Entry> list = new ArrayList<>();
        list.add(new Entry(1, 5));
        list.add(new Entry(2, 4));
        list.add(new Entry(3, 2));
        list.add(new Entry(4, 3));
        list.add(new Entry(10, 8));
        line.setEntries(list);
        line.setDrawLegend(true);//设置启用绘制图例
        line.setLegendTextSize((int) Utils.dp2px(10));//设置图例上的字体大小
        line.setName("脉搏");


        Lines lines = new Lines();


        lines.addLine(line);


        lineChart.setLines(lines);

    }*/


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device != null) {
                        //   Log.e("===============", device.getName());

                        if ("Med_link".equals(device.getName())) {
                            mDeviceAddress = device.getAddress();
                            Intent gattServiceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
                            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                            //    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        }
                    }
                }
            });
        }
    };

    void sendDataToBLE(String str) {
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
    protected void onResume() {
        super.onResume();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mGattUpdateReceiver);

        if (mBluetoothLeService != null) {

            unbindService(mServiceConnection);

        }
        mBluetoothLeService = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadDisable = false;
        if (mBluetoothAdapter != null){
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
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


}
