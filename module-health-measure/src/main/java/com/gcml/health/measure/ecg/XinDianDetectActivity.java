package com.gcml.health.measure.ecg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.creative.ecg.StatusMsg;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.cc.CCHealthRecordActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.single_measure.MeasureChooseDeviceActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * draw by view
 */
public class XinDianDetectActivity extends ToolbarBaseActivity implements View.OnClickListener {

    /**
     * PC80B绘图的View
     */
    private DrawThreadPC80B drawRunable;

    private BackGround drawBG;

    /**
     * 绘图线程
     */
    private Thread drawThread;

    private TextView tv_MSG;
    private Button btn_Conn, btn_Replay;

    /**
     * 心电测量结果
     * ECG measure result
     */
    private String[] measureResult;
    private int mEcg;
    private int mHeartRate;
    private TextView tvNext;
    private ImageView ivBack;
    /**
     * 历史记录
     */
    private TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    private TextView mBtnVideoDemo;
    private Uri uri;

    public static void startActivity(Context context, String fromWhere, boolean isSkip) {
        Intent intent = new Intent(context, XinDianDetectActivity.class);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("fromWhere", fromWhere);
        intent.putExtra(MeasureChooseDeviceActivity.IS_FACE_SKIP, isSkip);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, String fromWhere, int requestCode) {
        context.startActivityForResult(new Intent(context, XinDianDetectActivity.class)
                .putExtra("fromWhere", fromWhere), requestCode);
    }

    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前activity常亮 必须放在setContentView之前
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.health_measure_ecg_pc80b);
        initView();
        startService(new Intent(this, ReceiveService.class));
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("心 电 测 量");
        init();
        String fromWhere = getIntent().getStringExtra("fromWhere");
        if (HealthIntelligentDetectionActivity.class.getSimpleName().equals(fromWhere)) {
            tvNext.setText("下一步");
        } else if (MeasureChooseDeviceActivity.class.getSimpleName().equals(fromWhere)) {
            tvNext.setText("完成");
            tvNext.setVisibility(View.GONE);
        }
        showConnectAnimation();
    }

    private void showConnectAnimation() {
        Intent i = new Intent(this, ECGConnectActivity.class);
        i.putExtra("device", 3);
        startActivityForResult(i, 0x100);
    }


    private void init() {
        measureResult = getResources().getStringArray(R.array.ecg_measureres);
        ivBack = findViewById(R.id.icon_back);
        ivBack.setOnClickListener(this);
        tv_MSG = findViewById(R.id.main_pc80B_MSG);
        tvNext = findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        drawRunable = findViewById(R.id.main_pc80B_view_draw);
        drawBG = findViewById(R.id.main_pc80B_view_bg);
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

                            mEcg = data.getInt("nResult");
                            mHeartRate = data.getInt("nHR");
                            //TODO:播报语音和上传数据
                            if (!getIntent().getBooleanExtra(MeasureChooseDeviceActivity.IS_FACE_SKIP, false)) {
                                uploadEcg(mEcg, mHeartRate);
                            }
                            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "您好，您的心率为" + mHeartRate + "," + measureResult[mEcg]);

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
                        default:
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

    @SuppressLint("CheckResult")
    private void uploadEcg(final int ecg, final int heartRate) {
        Timber.e("上传心电数据：" + ecg + "心跳：" + heartRate);
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData ecgData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        ecgData.setDetectionType("2");
        ecgData.setEcg(measureResult[ecg]);
        ecgData.setHeartRate(heartRate);
        datas.add(ecgData);

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> o) {
                        ToastUtils.showShort("数据上传成功");
                        Intent intent = new Intent();
                        intent.putExtra("ecg", ecg);
                        intent.putExtra("heartRate", heartRate);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("数据上传失败:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
//            @Override
//            public void onSuccess(String callbackString) {
//                ToastUtils.showShort("数据上传成功");
//                Intent intent = new Intent();
//                intent.putExtra("ecg", ecg);
//                intent.putExtra("heartRate", heartRate);
//            }
//
//            @Override
//            public void onError() {
//                ToastUtils.showLong("数据上传失败");
//            }
//        });
    }

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
//            img_Pulse.setVisibility(View.VISIBLE);
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
//            img_Pulse.setVisibility(View.INVISIBLE);
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
        if (battery == 0) {
            if (!mHandler.hasMessages(BATTERY_ZERO)) {
                mHandler.sendEmptyMessage(BATTERY_ZERO);
            }
        } else {
//            img_Battery.setVisibility(View.VISIBLE);
            mHandler.removeMessages(BATTERY_ZERO);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.icon_back) {
            finish();

        } else if (i == R.id.tv_next) {
//            uploadEcg(mEcg, mHeartRate);

        } else if (i == R.id.btn_health_history) {
            CCHealthRecordActions.jump2HealthRecordActivity(7);

        } else if (i == R.id.btn_video_demo) {
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
            jump2MeasureVideoPlayActivity(uri, "心电测量演示视频");
        }

    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        CC.obtainBuilder(CCVideoActions.MODULE_NAME)
                .setActionName(CCVideoActions.SendActionNames.TO_MEASUREACTIVITY)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URI, uri)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URL, null)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_TITLE, title)
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String resultAction = result.getDataItem(CCVideoActions.ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                switch (resultAction) {
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                        showConnectAnimation();
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        showConnectAnimation();
                        break;
                    default:
                }
            }
        });
    }

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
//        setImgVisible(img_Smooth, isVisible);
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
//        setTVtext(tv_Gain, "x" + gain);
        drawRunable.setGain(gain);
        drawBG.setGain(gain);
    }

    private void setHR(int hr) {
//        if (hr != 0) {
//            setTVtext(tv_HR, "HR=" + hr);
//        } else {
//            setTVtext(tv_HR, "HR=--");
//        }
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

    private static final int MSG_NO_EXIST_ECGFILE = 46;
    public static List<Integer> mECGReplayBuffer;

    private void initView() {
        mBtnHealthHistory = (TextView) findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
    }
}
