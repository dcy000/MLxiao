package com.medlink.danbogh.call2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.AppraiseActivity;
import com.example.han.referralproject.yisuotang.bean.WalletResultBean;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NimCallActivity extends AppCompatActivity {

    private static final String TAG = "NimCallActivity";

    public static final String EXTRA_INCOMING_CALL = "extra_incoming_call";
    public static final String EXTRA_SOURCE = "extra_source";
    public static final String EXTRA_PEER_ACCOUNT = "extra_peer_account";
    public static final String EXTRA_CALL_CONFIG = "extra_call_config";
    public static final String EXTRA_CALL_TYPE = "extra_call_type";
    public static final int SOURCE_UNKNOWN = -1;
    public static final int SOURCE_BROADCAST = 0;
    public static final int SOURCE_INTERNAL = 1;
    private Handler mHandler = new Handler();

    public static void launchNoCheck(final Context context, final String account) {
        launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
    }

    public interface TimeOutListener {
        void ontimeOut();
    }

    private static TimeOutListener listener;


    public static void launch(final Context context, final String account, TimeOutListener listener) {
        NimCallActivity.listener = listener;
        final String deviceId = com.example.han.referralproject.util.Utils.getDeviceId();
//        NetworkApi.Person_Amount(deviceId, new NetworkManager.SuccessCallback<RobotAmount>() {
//                    @Override
//                    public void onSuccess(RobotAmount response) {
//                        final String amount = response.getAmount();
//                        if (Float.parseFloat(amount) > 0) {
//                            //有余额
//                            launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
//                        } else {
//                            T.show("余额不足，请充值后再试");
//                        }
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//                        T.show("服务器繁忙，请稍后再试");
//                    }
//                });
/**
 * 云联商城的钱包余额接口
 */
        NetworkApi.getYSTWallet(MyApplication.getInstance().userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response != null) {
                    String resultJson = response.body();
                    WalletResultBean resultBean = new Gson().fromJson(resultJson, WalletResultBean.class);
                    if (resultBean != null) {
                        if (resultBean.tag) {
                            if (resultBean.data != null) {
                                String mywallet = resultBean.data.mywallet;
                                if (mywallet != null) {
                                    if (Float.parseFloat(mywallet) >= 0) {
                                        launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
                                    } else {
                                        T.show("余额不足，请充值后再试");
                                    }

                                }
                            }

                        } else {
                            T.show("服务器繁忙，请稍后再试");
                        }
                    }
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                T.show("网络繁忙");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });


    }

    public static void launch(final Context context, final String account) {
        final String deviceId = com.example.han.referralproject.util.Utils.getDeviceId();
//        NetworkApi.Person_Amount(deviceId, new NetworkManager.SuccessCallback<RobotAmount>() {
//                    @Override
//                    public void onSuccess(RobotAmount response) {
//                        final String amount = response.getAmount();
//                        if (Float.parseFloat(amount) > 0) {
//                            //有余额
//                            launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
//                        } else {
//                            T.show("余额不足，请充值后再试");
//                        }
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//                        T.show("服务器繁忙，请稍后再试");
//                    }
//                });
/**
 * 云联商城的钱包余额接口
 */
        NetworkApi.getYSTWallet(MyApplication.getInstance().userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response != null) {
                    String resultJson = response.body();
                    WalletResultBean resultBean = new Gson().fromJson(resultJson, WalletResultBean.class);
                    if (resultBean != null) {
                        if (resultBean.tag) {
                            if (resultBean.data != null) {
                                String mywallet = resultBean.data.mywallet;
                                if (mywallet != null) {
                                    if (Float.parseFloat(mywallet) >= 0) {
                                        launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
                                    } else {
                                        T.show("余额不足，请充值后再试");
                                    }

                                }
                            }

                        } else {
                            T.show("服务器繁忙，请稍后再试");
                        }
                    }
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                T.show("网络繁忙");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });


    }

    public static void launch(Context context, String account, int callType, int source) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, NimCallActivity.class);
        intent.putExtra(EXTRA_PEER_ACCOUNT, account);
        intent.putExtra(EXTRA_INCOMING_CALL, false);
        intent.putExtra(EXTRA_CALL_TYPE, callType);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }

    public static void launch(Context context, AVChatData config, int source) {
        Intent intent = new Intent();
        intent.setClass(context, NimCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_CALL_CONFIG, config);
        intent.putExtra(EXTRA_INCOMING_CALL, true);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }

    @BindView(R.id.iv_call_small_cover)
    ImageView ivSmallCover;
    @BindView(R.id.fl_call_small_container)
    FrameLayout flSmallContainer;
    @BindView(R.id.fl_call_large_container)
    FrameLayout flLargeContainer;
    @BindView(R.id.tv_call_time)
    TextView tvCallTime;
    @BindView(R.id.iv_call_peer_avatar)
    ImageView ivPeerAvatar;
    @BindView(R.id.tv_call_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_call_status)
    TextView tvStatus;
    @BindView(R.id.ic_call_switch_camera)
    ImageView ivSwitchCamera;
    @BindView(R.id.iv_call_toggle_camera)
    ImageView ivToggleCamera;
    @BindView(R.id.iv_call_toggle_mute)
    ImageView ivToggleMute;
    @BindView(R.id.iv_call_hang_up)
    ImageView ivHangUp;
    @BindView(R.id.tv_call_receive)
    TextView tvReceive;
    @BindView(R.id.tv_call_refuse)
    TextView tvRefuse;
    @BindView(R.id.cl_call_root)
    ConstraintLayout clRoot;
    @BindView(R.id.iv_finish)
    ImageView backView;

    private boolean mIsIncomingCall;
    public AVChatData mCallData;
    public int mCallType;
    public String mPeerAccount;
    public Unbinder mUnbinder;
    public AVChatSurfaceViewRenderer mSmallRenderer;
    public AVChatSurfaceViewRenderer mLargeRenderer;

    private boolean shouldEnableToggle = false;


    public NimCallHelper.OnCallStateChangeListener mCallListener = new NimCallHelper.OnCallStateChangeListener() {
        @Override
        public void onCallStateChanged(CallState state) {
            if (clRoot == null) {
                return;
            }
            if (CallState.isVideoMode(state))
                switch (state) {
                    case OUTGOING_VIDEO_CALLING:
                        showProfile(true);
                        showStatus(R.string.avchat_wait_recieve);
                        showTime(false);
                        showRefuseReceive(false);
                        shouldEnableToggle = true;
                        enableCameraToggle();
                        showBottomPanel(true);
                        break;
                    case INCOMING_VIDEO_CALLING:
                        showProfile(true);
                        showStatus(R.string.avchat_video_call_request);
                        showTime(false);
                        showRefuseReceive(true);
                        showBottomPanel(false);
                        break;
                    case VIDEO_CONNECTING:
                        showStatus(R.string.avchat_connecting);
                        shouldEnableToggle = true;
                        break;
                    case VIDEO:
                        canSwitchCamera = true;
                        showProfile(false);
                        hideStatus();
                        showTime(true);
                        showRefuseReceive(false);
                        showBottomPanel(true);
                        break;
                    case OUTGOING_AUDIO_TO_VIDEO:
                        break;
                    default:
                        break;
                }
        }
    };
    public String mLargeAccount;
    public String mSmallAccount;
    private boolean isClosedCamera;
    private boolean isLocalVideoOff;
    private boolean localPreviewInSmallSize;
    private boolean canSwitchCamera;
    private boolean isPeerVideoOff;

    private void showStatus(@StringRes int res) {
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(res);
    }

    private void hideStatus() {
        tvStatus.setVisibility(View.GONE);
    }

    private void showBottomPanel(boolean show) {
        ivSwitchCamera.setVisibility(show ? View.VISIBLE : View.GONE);
        ivToggleCamera.setVisibility(show ? View.VISIBLE : View.GONE);
        ivToggleMute.setVisibility(show ? View.VISIBLE : View.GONE);
        ivHangUp.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showTime(boolean show) {
        tvCallTime.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void enableCameraToggle() {
        if (shouldEnableToggle) {
            if (canSwitchCamera && AVChatCameraCapturer.hasMultipleCameras())
                ivSwitchCamera.setEnabled(true);
        }
    }

    private void showRefuseReceive(boolean show) {
        tvRefuse.setVisibility(show ? View.VISIBLE : View.GONE);
        tvReceive.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    private void showProfile(boolean show) {
        //show avatar
        ivPeerAvatar.setVisibility(show ? View.VISIBLE : View.GONE);
        tvNickname.setVisibility(show ? View.VISIBLE : View.GONE);
        String peerAccount = NimCallHelper.getInstance().getPeerAccount();
        tvNickname.setText(peerAccount);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!validSource()) {
            finish();
            return;
        }

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        setContentView(R.layout.activity_nim_call);
        mUnbinder = ButterKnife.bind(this);
        mIsIncomingCall = getIntent().getBooleanExtra(EXTRA_INCOMING_CALL, false);
        NimCallHelper.getInstance().initCallParams();
        NimCallHelper.getInstance().setChatting(true);

        //init
        mSmallRenderer = new AVChatSurfaceViewRenderer(this);
        mLargeRenderer = new AVChatSurfaceViewRenderer(this);
        NimCallHelper.getInstance().addOnCallStateChangeListener(mCallListener);

        registerNimCallObserver(true);
        if (mIsIncomingCall) {
            incomingCalling();
        } else {
            outgoingCalling();
        }
        isCallEstablished = false;
        NimCallHelper.getInstance().setOnCloseSessionListener(new NimCallHelper.OnCloseSessionListener() {
            @Override
            public void onCloseSession() {
//                closeSession();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeSession();
                    }
                }, 1000);
            }
        });
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
    }

    private void outgoingCalling() {
        final AVChatType chatType = mCallType == AVChatType.VIDEO.getValue() ? AVChatType.VIDEO : AVChatType.AUDIO;
        final String account = NimAccountHelper.getInstance().getAccount();

        NimCallHelper.getInstance().call2(mPeerAccount, chatType, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                Log.i("mylog444444444444", "success avChatData : " + avChatData);
                if (chatType == AVChatType.VIDEO) {
                    initLargeSurfaceView(account);
                    canSwitchCamera = true;
                }
            }

            @Override
            public void onFailed(int code) {
                Log.i("mylog444444444444", "failed code : " + code);
            }

            @Override
            public void onException(Throwable exception) {
                Log.i("mylog444444444444", "exception : " + exception);
            }
        });
    }

    private void initSmallSurfaceView(String account) {
        mSmallAccount = account;
        if (account.equals(NimAccountHelper.getInstance().getAccount())) {
            AVChatManager.getInstance().setupLocalVideoRender(mSmallRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(account, mSmallRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        ViewParent parent = mSmallRenderer.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mSmallRenderer);
        }
        flSmallContainer.addView(mSmallRenderer, 0);
        mSmallRenderer.setZOrderMediaOverlay(true);
    }

    private void initLargeSurfaceView(String account) {
        if (flLargeContainer == null) {
            return;
        }
        mLargeAccount = account;
        if (account.equals(NimAccountHelper.getInstance().getAccount())) {
            AVChatManager.getInstance().setupLocalVideoRender(
                    mLargeRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(
                    account, mLargeRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        ViewParent parent = mLargeRenderer.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mLargeRenderer);
        }
        flLargeContainer.addView(mLargeRenderer, 0);
        mLargeRenderer.setZOrderMediaOverlay(false);
    }


    private void incomingCalling() {
        NimCallHelper.getInstance().inComingCalling(mCallData);
    }

    /**
     * 判断来电还是去电
     *
     * @return
     */
    private boolean validSource() {
        Intent intent = getIntent();
        int source = intent.getIntExtra(EXTRA_SOURCE, SOURCE_UNKNOWN);
        switch (source) {
            case SOURCE_BROADCAST: // incoming call
                mCallData = (AVChatData) intent.getSerializableExtra(EXTRA_CALL_CONFIG);
                mCallType = mCallData.getChatType().getValue();
                return true;
            case SOURCE_INTERNAL: // outgoing call
                mPeerAccount = intent.getStringExtra(EXTRA_PEER_ACCOUNT);
                mCallType = intent.getIntExtra(EXTRA_CALL_TYPE, -1);
                return mCallType == AVChatType.VIDEO.getValue()
                        || mCallType == AVChatType.AUDIO.getValue();
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        NimCallHelper.getInstance().setChatting(false);
        registerNimCallObserver(false);
        NimCallHelper.getInstance().destroy();
        stopTimer();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NimCallHelper.getInstance().resumeVideo();
    }

    @Override
    protected void onPause() {
        NimCallHelper.getInstance().pauseVideo();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //no back pressed
    }

    private void registerNimCallObserver(boolean register) {
        AVChatManager.getInstance().observeAVChatState(callStateObserver, register);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, register);
        AVChatManager.getInstance().observeControlNotification(callControlObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
        AVChatManager.getInstance().observeOnlineAckNotification(onlineAckObserver, register);
        CallTimeoutObserver.getInstance().observeTimeoutNotification(timeoutObserver, register, mIsIncomingCall);
        PhoneStateObserver.getInstance().observeAutoHangUpForLocalPhone(autoHangUpForLocalPhoneObserver, register);
    }

    private boolean isCallEstablished;

    private AVChatStateObserver callStateObserver = new AVChatStateObserver() {
        @Override
        public void onTakeSnapshotResult(String account, boolean success, String file) {

        }

        @Override
        public void onAVRecordingCompletion(String account, String filePath) {

        }

        @Override
        public void onAudioRecordingCompletion(String filePath) {

        }

        @Override
        public void onLowStorageSpaceWarning(long availableSize) {

        }

        @Override
        public void onAudioMixingEvent(int event) {

        }

        @Override
        public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {
            Log.i(TAG, "result code->" + code);
            if (code == 200) {
                Log.d(TAG, "onConnectServer success");
            } else if (code == 101) { // 连接超时
                NimCallHelper.getInstance().closeSessions(CallExitCode.PEER_NO_RESPONSE);
            } else if (code == 401) { // 验证失败
                NimCallHelper.getInstance().closeSessions(CallExitCode.CONFIG_ERROR);
            } else if (code == 417) { // 无效的channelId
                NimCallHelper.getInstance().closeSessions(CallExitCode.INVALIDE_CHANNELID);
            } else { // 连接服务器错误，直接退出
                NimCallHelper.getInstance().closeSessions(CallExitCode.CONFIG_ERROR);
            }
        }

        @Override
        public void onUserJoined(String account) {
            Log.d(TAG, "onUserJoined -> " + account);
            NimCallHelper.getInstance().setPeerAccount(account);
            initLargeSurfaceView(NimCallHelper.getInstance().getPeerAccount());
        }

        @Override
        public void onUserLeave(String account, int event) {
            Log.d(TAG, "onUserLeave -> " + account);
            onIvHangUpClicked();
            NimCallHelper.getInstance().closeSessions(CallExitCode.HANGUP);
        }

        @Override
        public void onLeaveChannel() {

        }

        @Override
        public void onProtocolIncompatible(int status) {

        }

        @Override
        public void onDisconnectServer() {

        }

        @Override
        public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {

        }

        @Override
        public void onCallEstablished() {
            CallTimeoutObserver.getInstance().observeTimeoutNotification(
                    timeoutObserver, false, mIsIncomingCall);
            startTimer();

            if (mCallType == AVChatType.VIDEO.getValue()) {
                NimCallHelper.getInstance().notifyCallStateChanged(CallState.VIDEO);
                initSmallSurfaceView(NimAccountHelper.getInstance().getAccount());
            } else {
                NimCallHelper.getInstance().notifyCallStateChanged(CallState.AUDIO);
            }
            isCallEstablished = true;
        }

        @Override
        public void onDeviceEvent(int code, String desc) {

        }

        @Override
        public void onConnectionTypeChanged(int netType) {

        }

        @Override
        public void onFirstVideoFrameAvailable(String account) {

        }

        @Override
        public void onFirstVideoFrameRendered(String user) {

        }

        @Override
        public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {

        }

        @Override
        public void onVideoFpsReported(String account, int fps) {

        }

        @Override
        public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
            return false;
        }

        @Override
        public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
            return true;
        }

        @Override
        public void onAudioDeviceChanged(int device) {

        }

        @Override
        public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

        }

        @Override
        public void onSessionStats(AVChatSessionStats sessionStats) {

        }

        @Override
        public void onLiveEvent(int event) {

        }
    };

    /**
     * 注册/注销网络通话被叫方的响应（接听、拒绝、忙）
     */
    Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
            AVChatData info = NimCallHelper.getInstance().getAvChatData();
            Log.i("mylog555555555555", "call ack : " + info);
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                    CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.PEER_BUSY);
                    NimCallHelper.getInstance().closeSessions(CallExitCode.PEER_BUSY);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    NimCallHelper.getInstance().closeRtc();
                    NimCallHelper.getInstance().closeSessions(CallExitCode.REJECT);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    NimCallHelper.getInstance().setCallEstablished(true);
                    canSwitchCamera = true;
                }
            }
            Log.i("mylog555555555555", "call ack : " + info);
        }
    };

    /**
     * 注册/注销网络通话控制消息（音视频模式切换通知）
     */
    Observer<AVChatControlEvent> callControlObserver = new Observer<AVChatControlEvent>() {
        @Override
        public void onEvent(AVChatControlEvent notification) {
            if (AVChatManager.getInstance().getCurrentChatId() != notification.getChatId()) {
                return;
            }
            switch (notification.getControlCommand()) {
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO:
//                avChatUI.incomingAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE:
//                onAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_REJECT:
//                avChatUI.onCallStateChange(CallStateEnum.AUDIO);
//                Toast.makeText(AVChatActivity.this, R.string.avchat_switch_video_reject, Toast.LENGTH_SHORT).show();
//                break;
//            case AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO:
//                onVideoToAudio();
//                break;
                case AVChatControlCommand.NOTIFY_VIDEO_OFF:
                    isPeerVideoOff = true;
                    if (!localPreviewInSmallSize) {
                        ivSmallCover.setVisibility(View.VISIBLE);
                    }
                    break;
                case AVChatControlCommand.NOTIFY_VIDEO_ON:
                    isPeerVideoOff = false;
                    if (!localPreviewInSmallSize) {
                        ivSmallCover.setVisibility(View.GONE);
                    }
                    break;
                default:
                    Log.i(TAG, "对方发来指令值：" + notification.getControlCommand());
                    break;
            }
        }
    };

    /**
     * 注册/注销网络通话对方挂断的通知
     */
    Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent avChatHangUpInfo) {
            AVChatData info = NimCallHelper.getInstance().getAvChatData();
            Log.i("mylog555555555555", "hang up info : " + info);
            if (info != null && info.getChatId() == avChatHangUpInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                NimCallHelper.getInstance().closeRtc();
                NimCallHelper.getInstance().closeSessions(CallExitCode.HANGUP);
            }
            Log.i("mylog555555555555", "after hang up info : " + info);
        }
    };

    /**
     * 注册/注销同时在线的其他端对主叫方的响应
     */
    Observer<AVChatOnlineAckEvent> onlineAckObserver = new Observer<AVChatOnlineAckEvent>() {
        @Override
        public void onEvent(AVChatOnlineAckEvent ackInfo) {
            AVChatData info = NimCallHelper.getInstance().getAvChatData();
            Log.i("mylog555555555555", "online ack : " + info);
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                String client = null;
                switch (ackInfo.getClientType()) {
                    case ClientType.Web:
                        client = "Web";
                        break;
                    case ClientType.Windows:
                        client = "Windows";
                        break;
                    case ClientType.Android:
                        client = "Android";
                        break;
                    case ClientType.iOS:
                        client = "iOS";
                        break;
                    case ClientType.MAC:
                        client = "Mac";
                        break;
                    default:
                        break;
                }
                if (client != null) {
                    String option = ackInfo.getEvent() == AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ? "接听！" : "拒绝！";
                    T.show("通话已在" + client + "端被" + option);
                }
                NimCallHelper.getInstance().closeSessions(-1);
            }
            Log.i("mylog555555555555", "online ack : " + info);
        }
    };

    Observer<Integer> timeoutObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            NimCallHelper.getInstance().hangUp();
            CallSoundPlayer.instance().stop();
            if (listener != null) {
                listener.ontimeOut();
            }
        }
    };

    Observer<Integer> autoHangUpForLocalPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            CallSoundPlayer.instance().stop();
            NimCallHelper.getInstance().closeSessions(CallExitCode.PEER_BUSY);
        }
    };

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                CallSoundPlayer.instance().stop();
                finish();
            }
        }
    };

    private int mSeconds = 0;

    private void startTimer() {
        mSeconds = 0;
        Handlers.runOnUiThread(refreshCallTime);
    }

    private void stopTimer() {
        Handlers.ui().removeCallbacks(refreshCallTime);
        if (mSeconds > 0) {
            final int minutes = mSeconds / 60 + 1;
            if (minutes >= 0) {
                final String bid = MyApplication.getInstance().userId;

                if ((!TextUtils.isEmpty(mPeerAccount)
                        && !mPeerAccount.startsWith("docter_"))
                        || (mCallData != null
                        && !TextUtils.isEmpty(mCallData.getAccount())
                        && !mCallData.getAccount().startsWith("docter_"))) {
                    return;
                }

                NetworkApi.DoctorInfo(bid, new NetworkManager.SuccessCallback<Doctor>() {
                    @Override
                    public void onSuccess(Doctor response) {
                        int docterid = response.docterid;
                        NetworkApi.charge(minutes, docterid, bid,
                                new NetworkManager.SuccessCallback<String>() {
                                    @Override
                                    public void onSuccess(String response) {
                                        T.show(minutes + "分钟");
                                        if (TextUtils.isEmpty(response)) {
                                            return;
                                        }
                                        try {
                                            JSONObject mResult = new JSONObject(response);
                                            Intent intent = new Intent(NimCallActivity.this, AppraiseActivity.class);
                                            intent.putExtra("doid", mResult.getInt("daid"));
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new NetworkManager.FailedCallback() {
                                    @Override
                                    public void onFailed(String message) {
                                        T.show(minutes + "分钟, 失败");
                                    }
                                });
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        T.show("请签约医生");
                    }
                });
            }
        }
    }

    private final Runnable refreshCallTime = new Runnable() {
        @Override
        public void run() {
            Handlers.ui().postDelayed(refreshCallTime, 1000);
            if (!tvCallTime.isShown()) {
                tvCallTime.setVisibility(View.VISIBLE);
            }
            tvCallTime.setText(Utils.formatCallTime(mSeconds));
            mSeconds++;
        }
    };

    @OnClick(R.id.ic_call_switch_camera)
    public void onIvSwitchCameraClicked() {
        NimCallHelper.getInstance().switchCamera();
    }

    @OnClick(R.id.iv_call_toggle_camera)
    public void onIvToggleCameraClicked() {
        boolean selected = ivToggleCamera.isSelected();
        AVChatManager.getInstance().muteLocalVideo(!selected);
        ivToggleCamera.setSelected(!selected);
        ivSmallCover.setVisibility(!selected ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.iv_call_toggle_mute)
    public void onIvToggleMuteClicked() {
        boolean established = NimCallHelper.getInstance().isCallEstablished();
        if (established) { // 连接已经建立
            boolean muted = AVChatManager.getInstance().isLocalAudioMuted();
            AVChatManager.getInstance().muteLocalAudio(!muted);
            ivToggleMute.setSelected(!muted);
        }
    }

    @OnClick(R.id.iv_finish)
    public void onBackClicked() {
        Toast.makeText(NimCallActivity.this, "正在停止通话", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeSession();
            }
        }, 1000);
        NimCallHelper.getInstance().hangUp();
    }

    private boolean isClosed = false;

    @OnClick(R.id.iv_call_hang_up)
    public void onIvHangUpClicked() {
        if (isClosed) {
            //findViewById(R.id.iv_call_hang_up).setVisibility(View.GONE);
            return;
        }
        isClosed = true;
        Toast.makeText(NimCallActivity.this, "正在停止通话", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NimCallHelper.getInstance().hangUp();
//                isClosed = true;
//                closeSession();
            }
        }, 2000);

    }

    @OnClick(R.id.tv_call_receive)
    public void onTvReceiveClicked() {
        NimCallHelper.getInstance().receive();
    }

    @OnClick(R.id.tv_call_refuse)
    public void onTvRefuseClicked() {
        NimCallHelper.getInstance().refuse();
    }


    public void closeSession() {
        if (ivSwitchCamera != null) {
            ivSwitchCamera.setEnabled(false);
        }
        if (ivToggleCamera != null) {
            ivToggleCamera.setEnabled(false);
        }
        if (ivToggleMute != null) {
            ivToggleMute.setEnabled(false);
        }
        if (tvRefuse != null) {
            tvRefuse.setEnabled(false);
        }
        if (tvReceive != null) {
            tvReceive.setEnabled(false);
        }
        if (ivHangUp != null) {
            ivHangUp.setEnabled(false);
        }
        if (mLargeRenderer != null && mLargeRenderer.getParent() != null) {
            ((ViewGroup) mLargeRenderer.getParent()).removeView(mLargeRenderer);
        }
        if (mSmallRenderer != null && mSmallRenderer.getParent() != null) {
            ((ViewGroup) mSmallRenderer.getParent()).removeView(mSmallRenderer);
        }
        mLargeRenderer = null;
        mSmallRenderer = null;
        finish();
    }
}