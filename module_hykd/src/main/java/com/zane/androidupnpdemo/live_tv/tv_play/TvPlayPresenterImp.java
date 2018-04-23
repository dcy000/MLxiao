package com.zane.androidupnpdemo.live_tv.tv_play;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.gzq.administrator.lib_common.utils.ToastTool;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.iflytek.wake.MLVoiceWake;
import com.iflytek.wake.MLWakeuperListener;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.connect_tv.Intents;
import com.zane.androidupnpdemo.connect_tv.control.ClingPlayControl;
import com.zane.androidupnpdemo.connect_tv.control.callback.ControlCallback;
import com.zane.androidupnpdemo.connect_tv.entity.ClingDevice;
import com.zane.androidupnpdemo.connect_tv.entity.ClingDeviceList;
import com.zane.androidupnpdemo.connect_tv.entity.DLANPlayState;
import com.zane.androidupnpdemo.connect_tv.entity.IDevice;
import com.zane.androidupnpdemo.connect_tv.entity.IResponse;
import com.zane.androidupnpdemo.connect_tv.listener.BrowseRegistryListener;
import com.zane.androidupnpdemo.connect_tv.listener.DeviceListChangedListener;
import com.zane.androidupnpdemo.connect_tv.service.ClingUpnpService;
import com.zane.androidupnpdemo.connect_tv.service.manager.ClingManager;
import com.zane.androidupnpdemo.connect_tv.service.manager.DeviceManager;
import com.zane.androidupnpdemo.connect_tv.ui.TVConnectMainActivity;
import com.zane.androidupnpdemo.connect_tv.util.Utils;
import com.zane.androidupnpdemo.live_tv.FloatingPlayer;
import com.zane.androidupnpdemo.live_tv.LiveBean;
import com.zane.androidupnpdemo.utils.PinyinHelper;

import org.fourthline.cling.model.meta.Device;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

import static com.zane.androidupnpdemo.live_tv.MediaHelper.getCurrentSpan;
import static com.zane.androidupnpdemo.live_tv.MediaHelper.getFocusX;
import static com.zane.androidupnpdemo.live_tv.MediaHelper.getFocusY;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvPlayPresenterImp implements ITvPlayPresenter {
    private static final int LOOP_MONITOR_LISTENER = 100;
    private KSYTextureView ksyTextureView;
    private ITvPlayView tvPlayActivity;
    /**
     * 视频设置
     */
    private Boolean mTouching;
    private float centerPointX;
    private float centerPointY;
    private float lastMoveX = -1;
    private float lastMoveY = -1;
    private float movedDeltaX;
    private float movedDeltaY;
    private float totalRatio;
    private float deltaRatio;
    private double lastSpan;
    private TimeCount timeCount;
    private MonitorSpeakingTimeCount timeCountMonitorSpeaking;
    private List<LiveBean> tvs;
    private boolean isMonitorUserSpeaking = false;
    private MyHandler myHandler;
    private static final String TAG = TvPlayPresenterImp.class.getSimpleName();
    /** 连接设备状态: 播放状态 */
    public static final int PLAY_ACTION = 0xa1;
    /** 连接设备状态: 暂停状态 */
    public static final int PAUSE_ACTION = 0xa2;
    /** 连接设备状态: 停止状态 */
    public static final int STOP_ACTION = 0xa3;
    /** 连接设备状态: 转菊花状态 */
    public static final int TRANSITIONING_ACTION = 0xa4;
    /** 获取进度 */
    public static final int GET_POSITION_INFO_ACTION = 0xa5;
    /** 投放失败 */
    public static final int ERROR_ACTION = 0xa5;
    private BroadcastReceiver mTransportStateBroadcastReceiver;
    /**
     * 投屏控制器
     */
    private ClingPlayControl mClingPlayControl = new ClingPlayControl();
    private Handler mHandler = new InnerHandler();
    private final class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAY_ACTION:
                    Log.i(TAG, "Execute PLAY_ACTION");
                    Toast.makeText((TvPlayActivity)tvPlayActivity, "播放", Toast.LENGTH_SHORT).show();
                    mClingPlayControl.setCurrentState(DLANPlayState.PLAY);

                    break;
                case PAUSE_ACTION:
                    Log.i(TAG, "Execute PAUSE_ACTION");
                    Toast.makeText((TvPlayActivity)tvPlayActivity, "暂停", Toast.LENGTH_SHORT).show();
                    mClingPlayControl.setCurrentState(DLANPlayState.PAUSE);

                    break;
                case STOP_ACTION:
                    Log.i(TAG, "Execute STOP_ACTION");
                    mClingPlayControl.setCurrentState(DLANPlayState.STOP);
                    Toast.makeText((TvPlayActivity)tvPlayActivity, "停止", Toast.LENGTH_SHORT).show();
                    break;
                case TRANSITIONING_ACTION:
                    Log.i(TAG, "Execute TRANSITIONING_ACTION");
                    Toast.makeText((TvPlayActivity)tvPlayActivity, "正在连接", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_ACTION:
                    Log.e(TAG, "Execute ERROR_ACTION");
                    Toast.makeText((TvPlayActivity)tvPlayActivity, "投放失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    public TvPlayPresenterImp(ITvPlayView tvPlayActivity, List<LiveBean> tvs) {
        this.tvPlayActivity = tvPlayActivity;
        this.tvs = tvs;
        FloatingPlayer.getInstance().init((Context) tvPlayActivity);
        ksyTextureView = FloatingPlayer.getInstance().getKSYTextureView();
        timeCount = new TimeCount(5000, 1000);
        timeCountMonitorSpeaking = new MonitorSpeakingTimeCount(5000, 1000);
        myHandler = new MyHandler((Activity) tvPlayActivity);
        startListenWakeup();
        bindServices();
        initConnectTvListener();
        registerReceivers();
    }
    private void registerReceivers() {
        //Register play status broadcast
        mTransportStateBroadcastReceiver = new TransportStateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intents.ACTION_PLAYING);
        filter.addAction(Intents.ACTION_PAUSED_PLAYBACK);
        filter.addAction(Intents.ACTION_STOPPED);
        filter.addAction(Intents.ACTION_TRANSITIONING);
        ((TvPlayActivity)tvPlayActivity).registerReceiver(mTransportStateBroadcastReceiver, filter);
    }
    /**
     * 接收状态改变信息
     */
    private class TransportStateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "Receive playback intent:" + action);
            if (Intents.ACTION_PLAYING.equals(action)) {
                mHandler.sendEmptyMessage(PLAY_ACTION);

            } else if (Intents.ACTION_PAUSED_PLAYBACK.equals(action)) {
                mHandler.sendEmptyMessage(PAUSE_ACTION);

            } else if (Intents.ACTION_STOPPED.equals(action)) {
                mHandler.sendEmptyMessage(STOP_ACTION);

            } else if (Intents.ACTION_TRANSITIONING.equals(action)) {
                mHandler.sendEmptyMessage(TRANSITIONING_ACTION);
            }
        }
    }
    private void initConnectTvListener() {

        // 设置发现设备监听
        mBrowseRegistryListener.setOnDeviceListChangedListener(new DeviceListChangedListener() {
            @Override
            public void onDeviceAdded(final IDevice device) {
                tvPlayActivity.findNewDevice(device);
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        mDevicesAdapter.add((ClingDevice) device);
//                    }
//                });
            }

            @Override
            public void onDeviceRemoved(final IDevice device) {
                tvPlayActivity.removeDevice(device);
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        mDevicesAdapter.remove((ClingDevice) device);
//                    }
//                });
            }
        });
    }
    private void bindServices() {
        Intent upnpServiceIntent = new Intent((TvPlayActivity)tvPlayActivity, ClingUpnpService.class);
        ((TvPlayActivity) tvPlayActivity).bindService(upnpServiceIntent, mUpnpServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection mUpnpServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "mUpnpServiceConnection onServiceConnected");

            ClingUpnpService.LocalBinder binder = (ClingUpnpService.LocalBinder) service;
            ClingUpnpService beyondUpnpService = binder.getService();

            ClingManager clingUpnpServiceManager = ClingManager.getInstance();
            clingUpnpServiceManager.setUpnpService(beyondUpnpService);
            clingUpnpServiceManager.setDeviceManager(new DeviceManager());

            clingUpnpServiceManager.getRegistry().addListener(mBrowseRegistryListener);
            //Search on service created.
            clingUpnpServiceManager.searchDevices();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "mUpnpServiceConnection onServiceDisconnected");

            ClingManager.getInstance().setUpnpService(null);
        }
    };
    /** 用于监听发现设备 */
    private BrowseRegistryListener mBrowseRegistryListener =new BrowseRegistryListener();
    private void startListenWakeup() {
        MLVoiceWake.initGlobalContext((TvPlayActivity) tvPlayActivity);
        MLVoiceWake.startWakeUp(new MLWakeuperListener() {
            @Override
            public void onMLError(int errorCode) {

            }

            @Override
            public void onMLResult() {
                onBehindWakeuped();
            }
        });

    }

    private SynthesizerListener speakFinishListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            isMonitorUserSpeaking = true;
            timeCountMonitorSpeaking.start();
            //说完之后开始识别
            myHandler.sendEmptyMessage(LOOP_MONITOR_LISTENER);
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private class MyHandler extends Handler {
        private WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOOP_MONITOR_LISTENER:
                    Log.e(TAG, "handleMessage: 循环监听说话");
                    startListenSpeak();
                    break;
            }
        }
    }

    private void startListenSpeak() {
        MLVoiceRecognize.startRecognize(((TvPlayActivity) tvPlayActivity).getApplicationContext(), new MLRecognizerListener() {
            @Override
            public void onMLVolumeChanged(int i, byte[] bytes) {
                Log.e(TAG, "onMLVolumeChanged: ");
            }

            @Override
            public void onMLBeginOfSpeech() {
                Log.e(TAG, "onMLBeginOfSpeech: ");
                tvPlayActivity.showVoiceView();
            }

            @Override
            public void onMLEndOfSpeech() {
                Log.e(TAG, "onMLEndOfSpeech: ");
                if (isMonitorUserSpeaking) {
                    myHandler.sendEmptyMessage(LOOP_MONITOR_LISTENER);
                }
                tvPlayActivity.hideVoiceView();
            }

            @Override
            public void onMLResult(String result) {
                isMonitorUserSpeaking = false;
                Log.e(TAG, "onMLResult: -----------" + result);
                ToastTool.showShort(result);
                String inSpell = PinyinHelper.getPinYin(result);
                if (inSpell.matches("(.*)fanhui(.*)")) {
                    tvPlayActivity.closeTv();
                }
                if (inSpell.matches("(.*)yitai|cctvyi(.*)")) {//CCTV1
                    ksyTextureView.reload(tvs.get(0).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }

                if (inSpell.matches("(.*)ertai|cctver|cctvcai(jin|jing)|cai(jin|jing)(.*)")) {//CCTV2
                    ksyTextureView.reload(tvs.get(1).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(san|shan)tai|cctv(san|shan)(.*)")) {//CCTV3
                    ksyTextureView.reload(tvs.get(2).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }

                if (inSpell.matches("(.*)sitai|cctvsi(.*)")) {//CCTV4
                    ksyTextureView.reload(tvs.get(3).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }

                if (inSpell.matches("(.*)qitai|cctvqi(.*)")) {//CCTV7
                    ksyTextureView.reload(tvs.get(4).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)jiu|cctvjiu(.*)")) {//CCTV9
                    ksyTextureView.reload(tvs.get(5).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)shitai|cctvshi(.*)")) {//CCTV10
                    ksyTextureView.reload(tvs.get(6).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(si|shi)yitai|cctv(si|shi)yi(.*)")) {//CCTV11
                    ksyTextureView.reload(tvs.get(7).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(si|shi)ertai|cctv(si|shi)er(.*)")) {//CCTV12
                    ksyTextureView.reload(tvs.get(8).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(si|shi)wutai|cctv(si|shi)wu(.*)")) {//CCTV15
                    ksyTextureView.reload(tvs.get(9).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)wenhua(jin|jing)(pin|ping)(.*)")) {//CCTV-文化精品
                    ksyTextureView.reload(tvs.get(10).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(shao|sao)er(.*)")) {//CCTV-少儿
                    ksyTextureView.reload(tvs.get(11).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(zu|zhu)qiu(.*)")) {//CCTV-风云足球
                    ksyTextureView.reload(tvs.get(12).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                //====================================
                if (inSpell.matches("(.*)di(li|ni)(.*)")) {//CCTV-世界地理
                    ksyTextureView.reload(tvs.get(13).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)jun(si|shi)(.*)")) {//CCTV-国防军事
                    ksyTextureView.reload(tvs.get(14).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)tiyue|cctvwu|wutai(.*)")) {//CCTV-体育
                    ksyTextureView.reload(tvs.get(15).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)fengyunju(chang|cang)(.*)")) {//CCTV-风云剧场
                    ksyTextureView.reload(tvs.get(16).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(yin|ying)yue(.*)")) {//CCTV-风云音乐
                    ksyTextureView.reload(tvs.get(17).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)huaijiu(.*)")) {//CCTV-怀旧剧场
                    ksyTextureView.reload(tvs.get(18).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(lao|nao)gu(si|shi)(.*)")) {//CCTV-老故事
                    ksyTextureView.reload(tvs.get(19).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)gaoerfu|wangqiu(.*)")) {//CCTV-高尔夫·网球
                    ksyTextureView.reload(tvs.get(20).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(fu|hu)(nan|lan)(.*)")) {//湖南
                    ksyTextureView.reload(tvs.get(21).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)jiang(su|shu)(.*)")) {//江苏
                    ksyTextureView.reload(tvs.get(22).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(zhe|ze)jiang(.*)")) {//浙江
                    ksyTextureView.reload(tvs.get(23).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(shang|sang)hai|dongfang(.*)")) {//上海
                    ksyTextureView.reload(tvs.get(24).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)yun(nan|lan)(.*)")) {//云南
                    ksyTextureView.reload(tvs.get(25).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)dong(nan|lan)(.*)")) {//东南
                    ksyTextureView.reload(tvs.get(26).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(liao|niao)(ning|ling|nin|lin)(.*)")) {//辽宁
                    ksyTextureView.reload(tvs.get(27).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(chong|cong)(qin|qing)(.*)")) {//重庆
                    ksyTextureView.reload(tvs.get(28).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)hei(long|nong)jiang(.*)")) {//黑龙江
                    ksyTextureView.reload(tvs.get(29).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)gui(zhou|zou)(.*)")) {//贵州
                    ksyTextureView.reload(tvs.get(30).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)jiangxi(.*)")) {//江西
                    ksyTextureView.reload(tvs.get(31).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)guangxi(.*)")) {//广西
                    ksyTextureView.reload(tvs.get(32).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)guangdong(.*)")) {//广东
                    ksyTextureView.reload(tvs.get(33).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)ji(lin|ling)(.*)")) {//吉林
                    ksyTextureView.reload(tvs.get(34).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(shan|san)dong(.*)")) {//山东
                    ksyTextureView.reload(tvs.get(35).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(si|shi)(chuan|cuan)(.*)")) {//四川
                    ksyTextureView.reload(tvs.get(36).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)xi(zang|zhang)(.*)")) {//西藏
                    ksyTextureView.reload(tvs.get(37).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(nei|lei)(men|meng)gu(.*)")) {//内蒙古
                    ksyTextureView.reload(tvs.get(38).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(xin|xing)jiang(.*)")) {//新疆
                    ksyTextureView.reload(tvs.get(39).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(shen|sen|seng|sheng)(zhen|zheng|zen|zeng)(.*)")) {//深圳
                    ksyTextureView.reload(tvs.get(40).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)bei(jin|jing)(.*)")) {//北京纪实
                    ksyTextureView.reload(tvs.get(41).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)yidong(.*)")) {//重庆移动
                    ksyTextureView.reload(tvs.get(42).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(long|nong)cun(.*)")) {//重庆公共农村
                    ksyTextureView.reload(tvs.get(43).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)dao(si|shi)(.*)")) {//重庆导视
                    ksyTextureView.reload(tvs.get(44).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)dong(zuo|zhuo)(.*)")) {//CHC-动作电影
                    ksyTextureView.reload(tvs.get(45).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)huanxiao(.*)")) {//欢笑剧场
                    ksyTextureView.reload(tvs.get(46).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }

                if (inSpell.matches("(.*)(shi|si)(shang|sang)(.*)")) {//生活时尚
                    ksyTextureView.reload(tvs.get(47).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)jiating(yin|ying)yuan(.*)")) {//CHC-家庭影院

                    ksyTextureView.reload(tvs.get(48).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)gouwu(.*)")) {//家有购物
                    ksyTextureView.reload(tvs.get(49).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)rexiao|(jin|jing)(pin|ping)(.*)")) {//热销精选
                    ksyTextureView.reload(tvs.get(50).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(shou|sou)cang(.*)")) {//天下收藏
                    ksyTextureView.reload(tvs.get(51).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)jia(zhen|zheng|zen|zeng)(.*)")) {//家政
                    ksyTextureView.reload(tvs.get(52).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)gaoerfu(.*)")) {//高尔夫
                    ksyTextureView.reload(tvs.get(53).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)wanluo|qipai(.*)")) {//网络棋牌
                    ksyTextureView.reload(tvs.get(54).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)katong|(jin|jing)(yin|ying)(.*)")) {//金鹰卡通
                    ksyTextureView.reload(tvs.get(55).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)dongman(.*)")) {//新动漫
                    ksyTextureView.reload(tvs.get(56).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(li|ni)cai(.*)")) {//家庭理财
                    ksyTextureView.reload(tvs.get(57).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }
                if (inSpell.matches("(.*)(cai|chai)fu(.*)")) {//财富天下
                    ksyTextureView.reload(tvs.get(58).getTvUrl(), true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
                    return;
                }

                if (!ksyTextureView.isPlaying()) {
                    ksyTextureView.start();
                }
            }

            @Override
            public void onMLError(SpeechError error) {
                Log.e(TAG, "onMLError: " + error.getErrorCode() + "====" + error.getErrorDescription());
                if (error.getErrorCode() == 10118) {//没有听到讲话
                    if (!ksyTextureView.isPlaying()) {
                        ksyTextureView.start();
                    }
                    myHandler.sendEmptyMessage(LOOP_MONITOR_LISTENER);
                }
            }
        });
    }

    @Override
    public void startPlay(String url) {
        if (tvPlayActivity != null) {
            timeCount.start();
            tvPlayActivity.showLoadingDialog();
            tvPlayActivity.hideStatusBar();
            tvPlayActivity.addVideoView(ksyTextureView);
            ksyTextureView.setOnTouchListener(mTouchListener);
            ksyTextureView.setOnPreparedListener(mOnPreparedListener);
            ksyTextureView.setOnErrorListener(mOnErrorListener);
            ksyTextureView.setOnInfoListener(mOnInfoListener);
            ksyTextureView.setOnCompletionListener(mOnCompletionListener);
            ksyTextureView.setVolume(1.0f, 1.0f);
            ksyTextureView.setBufferTimeMax(3);
            ksyTextureView.setBufferSize(15);
            ksyTextureView.setTimeout(5, 30);

            try {
                ksyTextureView.setDataSource(url);
                ksyTextureView.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        if (ksyTextureView != null) {
            ksyTextureView.runInForeground();
            if (!ksyTextureView.isPlaying())
                ksyTextureView.start();
        }
    }

    @Override
    public void onPause() {
        if (ksyTextureView != null)
            ksyTextureView.runInBackground(false);
    }

    @Override
    public void playLast(String url) {
        ksyTextureView.reload(url, true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
    }

    @Override
    public void playNext(String url) {
        ksyTextureView.reload(url, true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_ACCURATE);
    }

    //事件监听
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            KSYTextureView mVideoView = FloatingPlayer.getInstance().getKSYTextureView();

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mTouching = false;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mTouching = true;
                    if (event.getPointerCount() == 2) {
                        lastSpan = getCurrentSpan(event);
                        centerPointX = getFocusX(event);
                        centerPointY = getFocusY(event);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1) {
                        float posX = event.getX();
                        float posY = event.getY();
                        if (lastMoveX == -1 && lastMoveX == -1) {
                            lastMoveX = posX;
                            lastMoveY = posY;
                        }
                        movedDeltaX = posX - lastMoveX;
                        movedDeltaY = posY - lastMoveY;

                        if (Math.abs(movedDeltaX) > 5 || Math.abs(movedDeltaY) > 5) {
                            //判断调节音量和亮度 还是缩放画面
                            if (ksyTextureView != null) {
                                ksyTextureView.moveVideo(movedDeltaX, movedDeltaY);
                            }
                            mTouching = true;
                        }
                        lastMoveX = posX;
                        lastMoveY = posY;

                    } else if (event.getPointerCount() == 2) {
                        double spans = getCurrentSpan(event);
                        if (spans > 5) {
                            deltaRatio = (float) (spans / lastSpan);
                            totalRatio = ksyTextureView.getVideoScaleRatio() * deltaRatio;
                            if (ksyTextureView != null) {
                                ksyTextureView.setVideoScaleRatio(totalRatio, centerPointX, centerPointY);
                            }
                            lastSpan = spans;
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    if (event.getPointerCount() == 2) {
                        lastMoveX = -1;
                        lastMoveY = -1;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    lastMoveX = -1;
                    lastMoveY = -1;
                    if (!mTouching) {
                        if (tvPlayActivity.getControlBarVisibility() == View.VISIBLE) {
                            tvPlayActivity.hideControlBar();
                            timeCount.cancel();
                        } else {
                            tvPlayActivity.showControlBar();
                            timeCount.start();
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.d("播放异常", "onError: " + i);
            Toast.makeText((Context) tvPlayActivity, "网络连接不稳定", Toast.LENGTH_SHORT).show();
            videoPlayEnd();
            tvPlayActivity.closeTv();
            return false;
        }
    };
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            tvPlayActivity.hideLoadingDialog();
            ksyTextureView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyTextureView.start();
            timeCount.start();
        }
    };

    private IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            switch (i) {
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            tvPlayActivity.closeTv();
            Toast.makeText((Context) tvPlayActivity, "播放结束", Toast.LENGTH_SHORT).show();
        }
    };

    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (tvPlayActivity.getControlBarVisibility() == View.VISIBLE) {
                tvPlayActivity.hideControlBar();
                timeCount.cancel();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

        }
    }

    private class MonitorSpeakingTimeCount extends CountDownTimer {
        MonitorSpeakingTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isMonitorUserSpeaking = false;
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

        }
    }

    private void videoPlayEnd() {
        if (ksyTextureView != null) {
            FloatingPlayer.getInstance().destroy();
        }
    }

    @Override
    public void onDestroy() {
        videoPlayEnd();
        timeCount.cancel();

        mHandler.removeCallbacksAndMessages(null);
        // Unbind UPnP service
        ((TvPlayActivity)tvPlayActivity).unbindService(mUpnpServiceConnection);
        // Unbind System service
        //        unbindService(mSystemServiceConnection);
        // UnRegister Receiver
        ((TvPlayActivity)tvPlayActivity).unregisterReceiver(mTransportStateBroadcastReceiver);

        ClingManager.getInstance().destroy();
        ClingDeviceList.getInstance().destroy();
        tvPlayActivity = null;
    }

    @Override
    public int getOnPlayingPosition() {
        for (int i=0;i<tvs.size();i++){
            if (tvs.get(i).getTvUrl().equals(ksyTextureView.getDataSource())) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onBehindWakeuped() {
        if (ksyTextureView.isPlaying()) {
            ksyTextureView.pause();
        }
        MLVoiceSynthetize.startSynthesize((TvPlayActivity) tvPlayActivity, "主人,您想看哪个电视台？", speakFinishListener, false);
    }

    @Override
    public void refreshDevices() {
        Collection<ClingDevice> devices = ClingManager.getInstance().getDmrDevices();
        ClingDeviceList.getInstance().setClingDeviceList(devices);
        if (devices != null){
            tvPlayActivity.refreshDeices(devices);
        }
    }

    public KSYTextureView getKsyTextureView() {
        return ksyTextureView;
    }

}
