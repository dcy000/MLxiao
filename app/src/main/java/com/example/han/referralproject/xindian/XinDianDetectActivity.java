package com.example.han.referralproject.xindian;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.ecg.StatusMsg;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import java.io.File;
import java.util.List;

/**
 * draw by view
 */
public class XinDianDetectActivity extends BaseActivity implements View.OnClickListener {

    /**
     * PC80B绘图的View
     */
    private DrawThreadPC80B drawRunable;

    private BackGround drawBG;

    /**
     * 绘图线程
     */
    private Thread drawThread;

    private TextView tv_Gain, tv_HR, tv_MSG;
    private ImageView img_Battery, img_Smooth, img_Pulse;
    private Button btn_Conn, btn_Replay;

    /**
     * 心电测量结果
     * ECG measure result
     */
    private String[] measureResult;

    /**
     * 电量等级
     * battery level
     */
//	private int batteryRes[] = { R.drawable.battery_0, R.drawable.battery_1,
//			R.drawable.battery_2, R.drawable.battery_3 };
    private boolean isDetect;
    private String detectCategory;
    private View mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isDetect = intent.getBooleanExtra("isDetect", false);
		detectCategory = intent.getStringExtra("detectCategory");
        // 设置当前activity常亮 必须放在setContentView之前
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main_context_pc80b);
        startService(new Intent(this, ReceiveService.class));
        findViewById(R.id.icon_back).setOnClickListener(this);
        init();

        final Intent i = new Intent(this, ConnectActivity.class);
        i.putExtra("device", 3);
        startActivityForResult(i, 0x100);

        android6_RequestLocation();
        if (isDetect) {
            mNavView = getLayoutInflater().inflate(R.layout.detect_activity_nav, (FrameLayout) findViewById(android.R.id.content));
            mNavView.findViewById(R.id.detect_tv_result_last).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
					if ("detectHealth".equals(detectCategory)) {
						Intent intent1 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
						intent1.putExtras(getIntent());
						intent1.putExtra("type", "xueya");
						intent1.putExtra("ecg", "0.0");
						startActivity(intent1);
						finish();
						return;
					}
					if ("detectPressure".equals(detectCategory)) {
						Intent intent1 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
						intent1.putExtras(getIntent());
						intent1.putExtra("type", "xueya");
						intent1.putExtra("ecg", "0.0");
						startActivity(intent1);
						finish();
						return;
					}

                    if ("detectSugar".equals(detectCategory)) {
                        Intent intent1 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
                        intent1.putExtras(getIntent());
                        intent1.putExtra("type", "xueya");
                        intent1.putExtra("ecg", "0.0");
                        startActivity(intent1);
                        finish();
                        return;
                    }

                    Intent intent3 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
                    intent3.putExtras(getIntent());
                    intent3.putExtra("ecg", "0.0");
                    intent3.putExtra("type", "wendu");
                    intent3.putExtra("isDetect", true);
                    startActivity(intent3);
                    finish();
                }
            });
            mNavView.findViewById(R.id.detect_tv_result_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
					if ("detectHealth".equals(detectCategory)) {
						Intent intent1 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
						intent1.putExtras(getIntent());
						intent1.putExtra("type", "tizhong");
						String ecg = tv_MSG == null ? "0.0" : tv_MSG.getText().toString();
						intent1.putExtra("ecg", ecg);
						startActivity(intent1);
						finish();
						return;
					}

					if ("detectPressure".equals(detectCategory)) {
						Intent intent1 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
						intent1.putExtras(getIntent());
						intent1.putExtra("type", "tizhong");
						String ecg = tv_MSG == null ? "0.0" : tv_MSG.getText().toString();
						intent1.putExtra("ecg", ecg);
						startActivity(intent1);
						finish();
						return;
					}
					if ("detectSugar".equals(detectCategory)) {
						Intent intent1 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
						intent1.putExtras(getIntent());
						intent1.putExtra("type", "xuetang");
						String ecg = tv_MSG == null ? "0.0" : tv_MSG.getText().toString();
						intent1.putExtra("ecg", ecg);
						startActivity(intent1);
						finish();
						return;
					}

                    Intent intent3 = new Intent(XinDianDetectActivity.this, DetectActivity.class);
                    intent3.putExtras(getIntent());
                    intent3.putExtra("type", "tizhong");
                    String ecg = tv_MSG == null ? "0.0" : tv_MSG.getText().toString();
                    intent3.putExtra("ecg", ecg);
                    intent3.putExtra("isDetect", true);
                    startActivity(intent3);
                    finish();
                }
            });
        }
    }

    @Override
    protected void backLastActivity() {
        if (isDetect || mNavView != null) {
            mNavView.findViewById(R.id.detect_tv_result_last).performClick();
            return;
        }
        super.backLastActivity();
    }

    private void init() {
        measureResult = getResources().getStringArray(R.array.ecg_measureres);
        tv_MSG = (TextView) findViewById(R.id.main_pc80B_MSG);
        tv_Gain = (TextView) findViewById(R.id.main_pc80B_title_gain);
        tv_HR = (TextView) findViewById(R.id.main_pc80B_title_hr);

        img_Battery = (ImageView) findViewById(R.id.main_pc80B_title_battery);
        img_Smooth = (ImageView) findViewById(R.id.main_pc80B_title_smooth);
        img_Pulse = (ImageView) findViewById(R.id.main_pc80B_title_pulse);

//		btn_Conn = (Button) findViewById(R.id.btn_connect);
//		btn_Replay = (Button) findViewById(R.id.btn_replay);
//		btn_Conn.setOnClickListener(this);
//		btn_Replay.setOnClickListener(this);

        drawRunable = (DrawThreadPC80B) findViewById(R.id.main_pc80B_view_draw);
        drawBG = (BackGround) findViewById(R.id.main_pc80B_view_bg);
        drawRunable.setmHandler(mHandler);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ReceiveService.ACTION_BLU_DISCONNECT);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ReceiveService.ACTION_BLU_DISCONNECT)) {
                Toast.makeText(XinDianDetectActivity.this, R.string.connect_connect_off,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticReceive.MSG_DATA_BATTERY: {
                    setBattery(msg.arg1);
                }
                break;
                case BATTERY_ZERO: {// 电池电量为0时的消息  battery 0 level
                    if (img_Battery.isShown()) {
                        img_Battery.setVisibility(View.INVISIBLE);
                    } else {
                        img_Battery.setVisibility(View.VISIBLE);
                    }
                    mHandler.sendEmptyMessageDelayed(BATTERY_ZERO, 500);
                }
                break;
                case StaticReceive.MSG_DATA_ECG_STATUS_CH: {
                    switch (msg.arg1) {
                        case StatusMsg.FILE_TRANSMIT_START: {// 接收文件 receive file
                            setMSG(getResources().getString(
                                    R.string.measure_ecg_file_ing));
                        }
                        break;
                        case StatusMsg.FILE_TRANSMIT_SUCCESS: {
                            setMSG(getResources().getString(
                                    R.string.measure_ecg_file_end));
                        }
                        break;
                        case StatusMsg.FILE_TRANSMIT_ERROR: {
                            setMSG(getResources().getString(
                                    R.string.measure_ecg_time_err));
                        }
                        break;
                        case StaticReceive.MSG_DATA_TIMEOUT: {
                            setMSG(getResources().getString(
                                    R.string.measure_ecg_time_out));
                        }
                        break;
                        case 4: {// 准备阶段波形   ready wave
                            if (drawRunable.isPause()) {
                                drawRunable.Continue();
                            }
                            Bundle data = msg.getData();
                            if (data.getBoolean("bLeadoff")) {
                                setMSG(getResources().getString(
                                        R.string.measure_lead_off));
                            } else {
                                setMSG(" ");
                            }
                            setGain(data.getInt("nGain"));
                        }
                        break;
                        case 5: {// 实时测量波形    measure wave real time
                            if (drawRunable.isPause()) {
                                drawRunable.Continue();
                            }
                            Bundle data = msg.getData();
                            if (data.getBoolean("bLeadoff")) {
                                setMSG(getResources().getString(
                                        R.string.measure_lead_off));
                            } else {
                                setMSG(" ");
                            }
                            data.getInt("nTransMode");
                            setHR(data.getInt("nHR"));
                            setGain(data.getInt("nGain"));
                        }
                        break;
                        case 6: {// 测量结果   measure result
                            Bundle data = msg.getData();
                            nTransMode = data.getInt("nTransMode");
                            String time = data.getString("time");
                            if (nTransMode == StatusMsg.TRANSMIT_MODE_QUICK
                                    && time != null) {
                                setMSG(measureResult[data.getInt("nResult")]);
                            } else {
                                setMSG("");
                            }
                            drawRunable.cleanWaveData();
                            drawRunable.Pause();
                            setGain(0);
                            setHR(data.getInt("nHR"));
                            setSmooth(false);

                            DataInfoBean ecgInfo = new DataInfoBean();
                            ecgInfo.ecg = data.getInt("nResult");
                            ecgInfo.heart_rate = data.getInt("nHR");
                            NetworkApi.postData(ecgInfo, new NetworkManager.SuccessCallback<MeasureResult>() {
                                @Override
                                public void onSuccess(MeasureResult response) {
                                    //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                }
                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {

                                }
                            });
                            speak(String.format(getString(R.string.tips_result_xindian), ecgInfo.heart_rate, measureResult[ecgInfo.ecg]));

                        }
                        break;
                        case 7: {// 传输设置    setting data transmission mode
                            int nSmoothingMode = msg.arg2;// 滤波模式     filter mode
                            nTransMode = (Integer) msg.obj;// 传输模式   transmission mode
                            if (nTransMode == StatusMsg.TRANSMIT_MODE_FILE) {
                                setMSG(getResources().getString(
                                        R.string.measure_ecg_file_ing));
                            } else if (nTransMode == StatusMsg.TRANSMIT_MODE_CONTINUOUS) {
                                setMSG("");
                                setSmooth(nSmoothingMode == StatusMsg.SMOOTHMODE_ENHANCE);
                            }
                        }
                        break;
                    }
                }
                break;
                case StaticReceive.MSG_DATA_PULSE: {
                    showPulse(true);
                }
                break;
                case RECEIVEMSG_PULSE_OFF: {
                    showPulse(false);
                }
                break;
                case StaticReceive.MSG_TERMINAL_OFFLINE: {
                    sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
                }
                break;
                case MSG_NO_EXIST_ECGFILE: {
                    Toast.makeText(XinDianDetectActivity.this, "there is not ecg file", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
            }
        }

    };

    /**
     * 取消搏动标记  cancel pulse flag
     */
    public static final int RECEIVEMSG_PULSE_OFF = 0x115;

    /**
     * 设置搏动标记
     * set pulse flag
     */
    private void showPulse(boolean isShow) {
        if (isShow) {
            img_Pulse.setVisibility(View.VISIBLE);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(RECEIVEMSG_PULSE_OFF);
                }
            }.start();
        } else {
            img_Pulse.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 当前数据传输模式
     * transmission mode now
     */
    private int nTransMode = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (drawThread == null) {
            drawThread = new Thread(drawRunable, "DrawPC80BThread");
            drawThread.start();
        }
        if (drawRunable.isPause()) {
            drawRunable.Continue();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (drawThread != null && !drawRunable.isPause()) {
            drawRunable.Pause();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
        if (!drawRunable.isStop()) {
            //drawRunable.Continue();
            drawRunable.Stop();
        }
        drawThread = null;
        stopService(new Intent(this, ReceiveService.class));
        unregisterReceiver(receiver);
    }

    /**
     * 消息 电池电量为0  battery 0 level
     */
    private static final int BATTERY_ZERO = 0x302;

    private void setBattery(int battery) {
        //setImgResource(img_Battery, batteryRes[battery]);
        if (battery == 0) {
            if (!mHandler.hasMessages(BATTERY_ZERO)) {
                mHandler.sendEmptyMessage(BATTERY_ZERO);
            }
        } else {
            img_Battery.setVisibility(View.VISIBLE);
            mHandler.removeMessages(BATTERY_ZERO);
        }
    }

    private boolean isConn;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_back:
                if (isDetect && mNavView != null) {
                    mNavView.findViewById(R.id.detect_tv_result_last).performClick();
                    return;
                }
                finish();
                break;
        }
//		if(v.getId() == R.id.btn_connect){
//			isConn = !isConn;
//			if (isConn) {// connect
//				btn_Conn.setText(getString(R.string.main_bt_disconnect));
//				Intent i = new Intent(this, ConnectActivity.class);
//				i.putExtra("device", 3);
//				startActivityForResult(i, 0x100);
//			} else {// disConnect
//				btn_Conn.setText(getString(R.string.connect_connect));
//				sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
//			}
//		}else if(v.getId() == R.id.btn_replay){
//			replay();
//		}

    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.action_connect) {
//			if (MyBluetooth.isConnected) {//断开
//				item.setTitle(getString(R.string.connect_connect));
//				sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
//			} else {//连接
//				item.setTitle(getString(R.string.main_bt_disconnect));
//				Intent i = new Intent(this, ConnectActivity.class);
//				i.putExtra("device", 3);
//				startActivityForResult(i, 0x100);
//			}
//		}else if(id == R.id.action_replay){
//			replay();
//		}
//		return super.onOptionsItemSelected(item);
//	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x100) {
            StaticReceive.setmHandler(mHandler);
        }
    }

    /**
     * 设置滤波模式
     *
     * @param isVisible
     */
    private void setSmooth(boolean isVisible) {
        setImgVisible(img_Smooth, isVisible);
    }

    /**
     * 设置图片
     *
     * @param img
     * @param res
     */
    private void setImgResource(ImageView img, int res) {
        if (!img.isShown()) {
            img.setVisibility(View.VISIBLE);
        }
        img.setImageResource(res);
    }

    private void setImgVisible(ImageView img, boolean isVisible) {
        if (isVisible) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.INVISIBLE);
        }
    }

    private void setGain(int gain) {
        System.out.println("setGain=" + gain);
        gain = gain <= 0 ? 2 : gain;
        setTVtext(tv_Gain, "x" + gain);
        drawRunable.setGain(gain);
        drawBG.setGain(gain);
    }

    private void setHR(int hr) {
        if (hr != 0)
            setTVtext(tv_HR, "HR=" + hr);
        else
            setTVtext(tv_HR, "HR=--");
    }

    private void setMSG(String msg) {
        setTVtext(tv_MSG, msg);
    }

    /**
     * 设置TextView显示的内容
     */
    private void setTVtext(TextView tv, String msg) {
        if (tv != null) {
            if (!tv.isShown()) {
                tv.setVisibility(View.VISIBLE);
            }
            if (msg != null && !msg.equals("")) {
                if (msg.equals("0")) {
                    tv.setText(getResources().getString(
                            R.string.const_data_nodata));
                } else {
                    tv.setText(msg);
                }
            }
        }
    }

    /**
     * android6.0 蓝牙检测
     * android 6.0 access bluetooth
     */
    private static final int REQUEST_FINE_LOCATION = 0;

    private void android6_RequestLocation() {
//		if (Build.VERSION.SDK_INT >= 23) {
//			int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
//				//判断是否需要 向用户解释，为什么要申请该权限
//				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
//					Toast.makeText(this,"need to open location info for discovering bluetooth device in android6.0 system，otherwise dont find！", Toast.LENGTH_LONG).show();
//
//				//请求权限
//				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_FINE_LOCATION);
//				return;
//			}
//		}
    }


    private static final int MSG_NO_EXIST_ECGFILE = 46;
    public static List<Integer> mECGReplayBuffer;

    private void replay() {
        new Thread() {
            public void run() {
                if (drawRunable.isPause()) {
                    drawRunable.Continue();
                }

                String path = StaticReceive.filePath + "/" + StaticReceive.fileName;
                File file = new File(path);
                if (!file.exists()) {
                    mHandler.sendEmptyMessage(MSG_NO_EXIST_ECGFILE);
                    return;
                }
                mECGReplayBuffer = MyUtil.readFile(path);

            }

            ;
        }.start();
    }

}
