package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.creative.ecg.StatusMsg;
import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCHealthRecordActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.ecg.BackGround;
import com.gcml.health.measure.ecg.DrawThreadPC80B;
import com.gcml.health.measure.ecg.ECGConnectActivity;
import com.gcml.health.measure.ecg.ReceiveService;
import com.gcml.health.measure.ecg.StaticReceive;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 14:14
 * created by:gzq
 * description:TODO
 */
public class HealthECGDetectionFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private Context context;
    /**
     * 心电测量
     */
    private TextView mMainPc80BTitleTitle;
    private TextView mMainPc80BTitleGain;
    /**
     * HR=--
     */
    private TextView mMainPc80BTitleHr;
    private ImageView mMainPc80BTitleBattery;
    private ImageView mMainPc80BTitleSmooth;
    private ImageView mMainPc80BTitlePulse;
    private ImageView mIconBack;
    private BackGround mMainPc80BViewBg;
    private TextView mMainPc80BMSG;
    private DrawThreadPC80B mMainPc80BViewDraw;
    /**
     * 消息 电池电量为0  battery 0 level
     */
    private static final int BATTERY_ZERO = 0x302;
    /**
     * 取消搏动标记  cancel pulse flag
     */
    public static final int RECEIVEMSG_PULSE_OFF = 0x115;
    private static final int MSG_NO_EXIST_ECGFILE = 46;

    /**
     * 历史记录
     */
    private TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    private TextView mBtnVideoDemo;
    /**
     * 下一步
     */
    private TextView mTvNext;
    /**
     * 当前数据传输模式
     * transmission mode now
     */
    private int nTransMode = 0;
    /**
     * 心电测量结果
     * ECG measure result
     */
    private String[] measureResult;
    private int mEcg;
    private int mHeartRate;
    /**
     * 绘图线程
     */
    private Thread drawThread;
    private boolean isJump2Next = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    protected int initLayout() {
        return R.layout.health_measure_ecg_pc80b;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mMainPc80BTitleTitle = (TextView) view.findViewById(R.id.main_pc80B_title_title);
        mMainPc80BTitleGain = (TextView) view.findViewById(R.id.main_pc80B_title_gain);
        mMainPc80BTitleHr = (TextView) view.findViewById(R.id.main_pc80B_title_hr);
        mMainPc80BTitleBattery = (ImageView) view.findViewById(R.id.main_pc80B_title_battery);
        mMainPc80BTitleSmooth = (ImageView) view.findViewById(R.id.main_pc80B_title_smooth);
        mMainPc80BTitlePulse = (ImageView) view.findViewById(R.id.main_pc80B_title_pulse);
        mIconBack = (ImageView) view.findViewById(R.id.icon_back);
        mIconBack.setOnClickListener(this);
        mMainPc80BViewBg = (BackGround) view.findViewById(R.id.main_pc80B_view_bg);
        mMainPc80BMSG = (TextView) view.findViewById(R.id.main_pc80B_MSG);
        mMainPc80BViewDraw = (DrawThreadPC80B) view.findViewById(R.id.main_pc80B_view_draw);
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvNext = (TextView) view.findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);
        setBtnClickableState(false);
        MLVoiceSynthetize.startSynthesize(context, "主人，请打开设备开关，准备测量", false);
        initOther();
    }

    private void initOther() {
        measureResult = getResources().getStringArray(R.array.ecg_measureres);
        mMainPc80BViewDraw.setmHandler(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ReceiveService.ACTION_BLU_DISCONNECT);
        context.registerReceiver(receiver, filter);
        context.startService(new Intent(context, ReceiveService.class));
        showConnectAnimation();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ReceiveService.ACTION_BLU_DISCONNECT)) {
                Toast.makeText(context, "设备已断开", Toast.LENGTH_SHORT).show();
                if (dealVoiceAndJump != null) {
                    dealVoiceAndJump.updateVoice("设备已断开");
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        isJump2Next = false;
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (drawThread == null) {
            drawThread = new Thread(mMainPc80BViewDraw, "DrawPC80BThread");
            drawThread.start();
        }
        if (mMainPc80BViewDraw.isPause()) {
            mMainPc80BViewDraw.Continue();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (drawThread != null && !mMainPc80BViewDraw.isPause()) {
            mMainPc80BViewDraw.Pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
        if (!mMainPc80BViewDraw.isStop()) {
            //drawRunable.Continue();
            mMainPc80BViewDraw.Stop();
        }
        drawThread = null;
        context.stopService(new Intent(context, ReceiveService.class));
        context.unregisterReceiver(receiver);
    }


    private void showConnectAnimation() {
        Intent i = new Intent(context, ECGConnectActivity.class);
        i.putExtra("device", 3);
        startActivityForResult(i, 0x100);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.icon_back) {
        } else if (i == R.id.btn_health_history) {
            CCHealthRecordActions.jump2HealthRecordActivity(7);

        } else if (i == R.id.btn_video_demo) {
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.tips_xindian);
            jump2MeasureVideoPlayActivity(uri, "心电测量演示视频");

        } else if (i == R.id.tv_next) {
            if (fragmentChanged != null) {
                isJump2Next = true;
                fragmentChanged.onFragmentChanged(this, null);
            }
        }
    }

    private void setBtnClickableState(boolean enableClick) {
        if (enableClick) {
            mTvNext.setClickable(true);
            mTvNext.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
        } else {
            mTvNext.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
            mTvNext.setClickable(false);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x100) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.updateVoice("设备已连接");
            }
            StaticReceive.setmHandler(mHandler);
        }
    }

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
                    if (mMainPc80BTitleBattery.isShown()) {
                        mMainPc80BTitleBattery.setVisibility(View.INVISIBLE);
                    } else {
                        mMainPc80BTitleBattery.setVisibility(View.VISIBLE);
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
                            if (mMainPc80BViewDraw.isPause()) {
                                mMainPc80BViewDraw.Continue();
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
                            if (mMainPc80BViewDraw.isPause()) {
                                mMainPc80BViewDraw.Continue();
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
                            mMainPc80BViewDraw.cleanWaveData();
                            mMainPc80BViewDraw.Pause();
                            setGain(0);
                            setHR(data.getInt("nHR"));
                            setSmooth(false);

                            mEcg = data.getInt("nResult");
                            mHeartRate = data.getInt("nHR");
                            uploadEcg(mEcg, mHeartRate);
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
                    context.sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
                }
                break;
                case MSG_NO_EXIST_ECGFILE: {
                    Toast.makeText(context, "there is not ecg file", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
            }
        }

    };

    @SuppressLint("CheckResult")
    private void uploadEcg(final int ecg, final int heartRate) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData ecgData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        ecgData.setDetectionType("2");
        ecgData.setEcg(String.valueOf(ecg));
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
                        setBtnClickableState(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("数据上传失败:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 设置滤波模式
     *
     * @param isVisible
     */
    private void setSmooth(boolean isVisible) {
        setImgVisible(mMainPc80BTitleSmooth, isVisible);
    }

    private void setImgVisible(ImageView img, boolean isVisible) {
        if (isVisible) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置搏动标记
     * set pulse flag
     */
    private void showPulse(boolean isShow) {
        if (isShow) {
            mMainPc80BTitlePulse.setVisibility(View.VISIBLE);
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
            mMainPc80BTitlePulse.setVisibility(View.INVISIBLE);
        }
    }

    private void setMSG(String msg) {
        setTVtext(mMainPc80BMSG, msg);
    }

    private void setGain(int gain) {
        System.out.println("setGain=" + gain);
        gain = gain <= 0 ? 2 : gain;
        setTVtext(mMainPc80BTitleGain, "x" + gain);
        mMainPc80BViewDraw.setGain(gain);
        mMainPc80BViewBg.setGain(gain);
    }

    private void setHR(int hr) {
        if (hr != 0) {
            setTVtext(mMainPc80BTitleHr, "HR=" + hr);
        } else {
            setTVtext(mMainPc80BTitleHr, "HR=--");
        }
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

    private void setBattery(int battery) {
        //setImgResource(img_Battery, batteryRes[battery]);
        if (battery == 0) {
            if (!mHandler.hasMessages(BATTERY_ZERO)) {
                mHandler.sendEmptyMessage(BATTERY_ZERO);
            }
        } else {
            mMainPc80BTitleBattery.setVisibility(View.VISIBLE);
            mHandler.removeMessages(BATTERY_ZERO);
        }
    }

}
