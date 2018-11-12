package com.example.han.referralproject.single_measure;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.ecg.StatusMsg;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.single_measure.ecg.BackGround;
import com.example.han.referralproject.single_measure.ecg.DrawThreadPC80B;
import com.example.han.referralproject.single_measure.ecg.ECGBluetooth;
import com.example.han.referralproject.single_measure.ecg.ReceiveService;
import com.example.han.referralproject.single_measure.ecg.StaticReceive;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.utils.ToastUtils;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 14:14
 * created by:gzq
 * description:TODO
 */
public class SelfECGDetectionFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private Context context;
    private BackGround mMainPc80BViewBg;
    private TextView mMainPc80BMSG;
    private DrawThreadPC80B mMainPc80BViewDraw;
    private static final int BATTERY_ZERO = 0x302;
    public static final int RECEIVEMSG_PULSE_OFF = 0x115;
    private static final int MSG_NO_EXIST_ECGFILE = 46;
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvNext;
    private int nTransMode = 0;
    private String[] measureResult;
    private int mEcg;
    private int mHeartRate;
    private Thread drawThread;
    private boolean isServiceBind = false;
    private boolean isRegistReceiver = false;

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

        mMainPc80BViewBg = (BackGround) view.findViewById(R.id.main_pc80B_view_bg);
        mMainPc80BMSG = (TextView) view.findViewById(R.id.main_pc80B_MSG);
        mMainPc80BViewDraw = (DrawThreadPC80B) view.findViewById(R.id.main_pc80B_view_draw);
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvNext = (TextView) view.findViewById(R.id.tv_next);
        mTvNext.setVisibility(View.GONE);
        mTvNext.setOnClickListener(this);
        view.findViewById(R.id.tv_change_device).setOnClickListener(this);
        setBtnClickableState(false);
        MLVoiceSynthetize.startSynthesize(context, "主人，请打开设备开关，准备测量", false);
        initOther();
    }


    public void startDiscovery() {
        context.sendBroadcast(new Intent(ReceiveService.BLU_ACTION_STARTDISCOVERY)
                .putExtra("device", 3));
    }

    public void initOther() {
        measureResult = getResources().getStringArray(R.array.ecg_measureres);
        mMainPc80BViewDraw.setmHandler(mHandler);
        IntentFilter filter = new IntentFilter(ReceiveService.BLU_ACTION_STATE_CHANGE);
        filter.addAction(ReceiveService.ACTION_BLUETOOH_OFF);
        filter.addAction(ReceiveService.ACTION_BLU_DISCONNECT);
        if (!isRegistReceiver) {
            isRegistReceiver = true;
            context.registerReceiver(connectReceiver, filter);
        }
        context.startService(new Intent(context, ReceiveService.class));
        context.bindService(new Intent(context, ReceiveService.class), serviceConnect, Service.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceBind = true;
            startDiscovery();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private BroadcastReceiver connectReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ReceiveService.BLU_ACTION_STATE_CHANGE)) {
                String state = intent.getExtras().getString("arg1");
                if (state.equals("OPENING")) {
//                    Timber.i("opening the bluetooth(ecg)");
                } else if (state.equals("OPENINGFILE")) {
//                    Timber.i("opening the bluetooth failed");
                } else if (state.equals("DISCOVERYING")) {
//                    Timber.i("searching the bluetooth devices");
                } else if (state.equals("CONNECTING")) {
//                    Timber.i("connecting the bluetooth device");
                } else if (state.equals("CONNECTED")) {
                    if (dealVoiceAndJump != null) {
                        dealVoiceAndJump.updateVoice("设备已连接");
                    }
                    StaticReceive.setmHandler(mHandler);

                } else if (state.equals("CONNECTFILE") || state.equals("DISCOVERYED")) {
                    if (ECGBluetooth.bluStatus == ECGBluetooth.BLU_STATUS_NORMAL) {
                        String message = "连接失败，点击右上角按钮重连";
                        ToastUtils.showShort(message);
                        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), message);
                    }
                }
            } else if (action.equals(ReceiveService.ACTION_BLUETOOH_OFF)) {
//                Timber.i("bluetooth is closed");
                context.sendBroadcast(new Intent(
                        ReceiveService.BLU_ACTION_STOPDISCOVERY));
                context.sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
            } else if (action.equals(ReceiveService.ACTION_BLU_DISCONNECT)) {
                Toast.makeText(context, "设备已断开", Toast.LENGTH_SHORT).show();
                if (dealVoiceAndJump != null) {
                    dealVoiceAndJump.updateVoice("设备已断开");
                }
            }
        }
    };

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
    public void onStop() {
        super.onStop();
        context.sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
        if (!mMainPc80BViewDraw.isStop()) {
            mMainPc80BViewDraw.Stop();
        }
        drawThread = null;
        context.stopService(new Intent(context, ReceiveService.class));
        if (serviceConnect != null && isServiceBind) {
            context.unbindService(serviceConnect);
        }
        if (isRegistReceiver) {
            isRegistReceiver = false;
            context.unregisterReceiver(connectReceiver);
        }
        context.sendBroadcast(new Intent(ReceiveService.BLU_ACTION_STOPDISCOVERY));

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_next) {
            if (fragmentChanged != null) {
                fragmentChanged.onFragmentChanged(this, null);
            }
        }
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_ECG);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_ECG);
            }
            clickHealthHistory(v);
        } else if (i == R.id.tv_change_device) {
            if (fragmentChanged != null) {
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
                case BATTERY_ZERO: {
                    // 电池电量为0时的消息  battery 0 level
                    mHandler.sendEmptyMessageDelayed(BATTERY_ZERO, 500);
                }
                break;
                case StaticReceive.MSG_DATA_ECG_STATUS_CH: {
                    switch (msg.arg1) {
                        case StatusMsg.FILE_TRANSMIT_START: {
                            // 接收文件 receive file
                            setMSG(getResources().getString(R.string.measure_ecg_file_ing));
                        }
                        break;
                        case StatusMsg.FILE_TRANSMIT_SUCCESS: {
                            setMSG(getResources().getString(R.string.measure_ecg_file_end));
                        }
                        break;
                        case StatusMsg.FILE_TRANSMIT_ERROR: {
                            setMSG(getResources().getString(R.string.measure_ecg_time_err));
                        }
                        break;
                        case StaticReceive.MSG_DATA_TIMEOUT: {
                            setMSG(getResources().getString(R.string.measure_ecg_time_out));
                        }
                        break;
                        case 4: {
                            // 准备阶段波形   ready wave
                            if (mMainPc80BViewDraw.isPause()) {
                                mMainPc80BViewDraw.Continue();
                            }
                            Bundle data = msg.getData();
                            if (data.getBoolean("bLeadoff")) {
                                setMSG(getResources().getString(R.string.measure_lead_off));
                            } else {
                                setMSG(" ");
                            }
                        }
                        break;
                        case 5: {
                            // 实时测量波形    measure wave real time
                            if (mMainPc80BViewDraw.isPause()) {
                                mMainPc80BViewDraw.Continue();
                            }
                            Bundle data = msg.getData();
                            if (data.getBoolean("bLeadoff")) {
                                setMSG(getResources().getString(R.string.measure_lead_off));
                            } else {
                                setMSG(" ");
                            }
                            data.getInt("nTransMode");
                        }
                        break;
                        case 6: {
                            // 测量结果   measure result
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
                            mEcg = data.getInt("nResult");
                            mHeartRate = data.getInt("nHR");
                            uploadEcg(mEcg, mHeartRate);
                        }
                        break;
                        case 7: {
                            // 传输设置    setting data transmission mode
                            // 滤波模式     filter mode
                            int nSmoothingMode = msg.arg2;
                            // 传输模式   transmission mode
                            nTransMode = (Integer) msg.obj;
                            if (nTransMode == StatusMsg.TRANSMIT_MODE_FILE) {
                                setMSG(getResources().getString(R.string.measure_ecg_file_ing));
                            } else if (nTransMode == StatusMsg.TRANSMIT_MODE_CONTINUOUS) {
                                setMSG("");
                            }
                        }
                        break;
                        default:
                            break;
                    }
                }
                break;
                case StaticReceive.MSG_DATA_PULSE:
                    break;
                case RECEIVEMSG_PULSE_OFF:
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
       /* ArrayList<DetectionData> datas = new ArrayList<>();
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
                });*/

        DataInfoBean ecgInfo = new DataInfoBean();
        ecgInfo.ecg =ecg ;
        ecgInfo.heart_rate = heartRate;

        NetworkApi.postData(ecgInfo, response -> {
            T.show("");
        }, message -> {

        });
    }

    private void setMSG(String msg) {
        if (mMainPc80BMSG != null) {
            if (!mMainPc80BMSG.isShown()) {
                mMainPc80BMSG.setVisibility(View.VISIBLE);
            }
            if (msg != null && !msg.equals("")) {
                if (msg.equals("0")) {
                    mMainPc80BMSG.setText(getResources().getString(
                            R.string.const_data_nodata));
                } else {
                    mMainPc80BMSG.setText(msg);
                }
            }
        }
    }
    private void setBattery(int battery) {
        if (battery == 0) {
            if (!mHandler.hasMessages(BATTERY_ZERO)) {
                mHandler.sendEmptyMessage(BATTERY_ZERO);
            }
        } else {
            mHandler.removeMessages(BATTERY_ZERO);
        }
    }

}
