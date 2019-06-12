package com.gcml.call;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.gcml.call.floatwindow.CallFloatViewHelper;
import com.gcml.common.AppDelegate;
import com.gcml.common.utils.display.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatAudioEffectMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatImageFormat;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;


/**
 * Created by afirez on 2018/6/1.
 */

public enum CallHelper {

    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    public static void launchFromSmall(final Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, CallActivity.class);
        context.startActivity(intent);
    }

    public static void launch(final Context context, final String account) {
        launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
    }

    public static void launch(Context context, String account, int callType, int source) {
        CallHelper.INSTANCE.setChatting(true);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, CallActivity.class);
        intent.putExtra(EXTRA_REMOTE_ACCOUNT, account);
        intent.putExtra(EXTRA_INCOMING_CALL, false);
        intent.putExtra(EXTRA_CALL_TYPE, callType);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }

    public static void launch(Context context, AVChatData config, int source) {
        CallHelper.INSTANCE.setChatting(true);
        Intent intent = new Intent();
        intent.setClass(context, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_CALL_CONFIG, config);
        intent.putExtra(EXTRA_INCOMING_CALL, true);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }


    private static final String TAG = "CallHelper";

    public static final String EXTRA_INCOMING_CALL = "extra_incoming_call";
    public static final String EXTRA_CALL_TYPE = "extra_call_type";
    public static final String EXTRA_REMOTE_ACCOUNT = "extra_peer_account";
    public static final String EXTRA_CALL_CONFIG = "extra_call_config";
    public static final String EXTRA_SOURCE = "extra_source";
    public static final int SOURCE_UNKNOWN = -1;
    public static final int SOURCE_BROADCAST = 0;
    public static final int SOURCE_INTERNAL = 1;


    CallHelper() {
        context = AppDelegate.INSTANCE.app();
    }


    public void setChatting(boolean chatting) {
        mChatting = chatting;
    }

    public boolean isChatting() {
        return mChatting;
    }

    public boolean isCallEstablished() {
        return mCallEstablished.get();
    }

    public interface OnCallStateChangeListener {
        void onCallStateChanged(CallState state);
    }

    private List<OnCallStateChangeListener> mOnCallStateChangeListeners =
            new ArrayList<>(2);

    public void addOnCallStateChangeListener(OnCallStateChangeListener listener) {
        mOnCallStateChangeListeners.add(listener);
    }

    public void removeOnCallStateChangeListener(OnCallStateChangeListener listener) {
        mOnCallStateChangeListeners.remove(listener);
    }

    public void notifyCallStateChanged(CallState state) {
        Timber.tag(TAG).d("notifyCallStateChanged state=%s", state);
        callingState = state;
        for (OnCallStateChangeListener listener : mOnCallStateChangeListeners) {
            if (listener != null) {
                listener.onCallStateChanged(state);
            }
        }
    }

    public interface OnCloseSessionListener {
        void onCloseSession();
    }

    private OnCloseSessionListener mOnCloseSessionListener;

    public void setOnCloseSessionListener(OnCloseSessionListener onCloseSessionListener) {
        mOnCloseSessionListener = onCloseSessionListener;
    }

    private Context context;

    private boolean mChatting;

    private CallState callingState;
    private AVChatData avChatData;
    private boolean isIncomingCall;
    private int callType;
    private String remoteAccount;
    private AVChatCameraCapturer videoCapturer;

    private AtomicBoolean mCallEstablished = new AtomicBoolean(false);

    private volatile FrameLayout flSmallContainer;
    private volatile FrameLayout flLargeContainer;

    private String largeAccount;

    private AVChatSurfaceViewRenderer smallRenderer;
    private AVChatSurfaceViewRenderer largeRenderer;

    private boolean isRemoteVideoOff;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public FrameLayout getFlSmallContainer() {
        return flSmallContainer;
    }

    public synchronized void setSmallContainer(FrameLayout flSmallContainer) {
        this.flSmallContainer = flSmallContainer;
        if (flSmallContainer == null && mCallEstablished.get() && smallRenderer != null) {
            ViewParent parent = smallRenderer.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(smallRenderer);
            }
            return;
        }
        initSmallSurface();
    }

    public FrameLayout getFlLargeContainer() {
        return flLargeContainer;
    }

    public synchronized void setLargeContainer(FrameLayout flLargeContainer) {
        this.flLargeContainer = flLargeContainer;
        if (flLargeContainer == null
                && mCallEstablished.get()
                && largeRenderer != null) {
            ViewParent parent = largeRenderer.getParent();
            if (parent != null
                    && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(largeRenderer);
            }
            return;
        }
        initLargeSurface();
    }

    public void setIncomingCall(boolean incomingCall) {
        isIncomingCall = incomingCall;
    }

    public boolean isIncomingCall() {
        return isIncomingCall;
    }

    public String getRemoteAccount() {
        return remoteAccount;
    }

    public boolean checkSource(Bundle bundle) {
        int source = bundle.getInt(EXTRA_SOURCE, SOURCE_UNKNOWN);
        switch (source) {
            case SOURCE_BROADCAST: // incoming call
                isIncomingCall = bundle.getBoolean(EXTRA_INCOMING_CALL, false);
                avChatData = (AVChatData) bundle.getSerializable(EXTRA_CALL_CONFIG);
                if (avChatData != null) {
                    callType = avChatData.getChatType().getValue();
                    registerCallObserver(true);
                    uiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onInComingCalling(avChatData);
                        }
                    }, 1000);
                }
                return true;
            case SOURCE_INTERNAL: // outgoing call
                isIncomingCall = bundle.getBoolean(EXTRA_INCOMING_CALL, false);
                remoteAccount = bundle.getString(EXTRA_REMOTE_ACCOUNT);
                callType = bundle.getInt(EXTRA_CALL_TYPE, -1);
                final AVChatType chatType = callType == AVChatType.VIDEO.getValue()
                        ? AVChatType.VIDEO
                        : AVChatType.AUDIO;
                registerCallObserver(true);
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call(remoteAccount, chatType);
                    }
                }, 1000);
                return true;
            default:
                notifyCallStateChanged(callingState);
                return true;
        }
    }

    public void registerCallObserver(boolean register) {
        AVChatManager.getInstance().observeAVChatState(callStateObserver, register);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, register);
        AVChatManager.getInstance().observeControlNotification(callControlObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
        AVChatManager.getInstance().observeOnlineAckNotification(onlineAckObserver, register);
        CallTimeoutObserver.getInstance().observeTimeoutNotification(timeoutObserver, register, isIncomingCall);
        CallPhoneStateObserver.getInstance().observeAutoHangUpForLocalPhone(autoHangUpForLocalPhoneObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }


    private AVChatStateObserver callStateObserver = new AbsChatStateObserver() {

        @Override
        public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {
            if (code == 200) {
//                Timber.tag(TAG).d("onJoinedChannel %s", "OK");
            } else if (code == 101) { // 连接超时
                closeSessions(CallExitCode.PEER_NO_RESPONSE);
            } else if (code == 401) { // 验证失败
                closeSessions(CallExitCode.CONFIG_ERROR);
            } else if (code == 417) { // 无效的channelId
                closeSessions(CallExitCode.INVALIDE_CHANNELID);
            } else { // 连接服务器错误，直接退出
                closeSessions(CallExitCode.CONFIG_ERROR);
            }
        }

        @Override
        public void onUserJoined(String account) {
            remoteAccount = account;
            initLargeSurface();
        }

        @Override
        public void onUserLeave(String account, int event) {
            hangUp();
        }

        @Override
        public void onCallEstablished() {
            CallHelper.this.onCallEstablished();
        }
    };

    private void initLargeSurface() {
        if (flLargeContainer == null && !mCallEstablished.get()) {
            return;
        }
        largeAccount = remoteAccount;
        AVChatManager chatManager = AVChatManager.getInstance();
        if (largeRenderer == null) {
            largeRenderer = new AVChatSurfaceViewRenderer(context);
        } else {
            ViewParent parent = largeRenderer.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(largeRenderer);
            }
        }
        flLargeContainer.addView(largeRenderer);
        largeRenderer.setZOrderMediaOverlay(false);
        chatManager.setupRemoteVideoRender(
                remoteAccount,
                largeRenderer,
                false,
                AVChatVideoScalingType.SCALE_ASPECT_BALANCED
        );
    }

    private void previewInLargeSurface() {
        if (flLargeContainer == null) {
            return;
        }
        largeAccount = CallAuthHelper.getInstance().getAccount();
        AVChatManager chatManager = AVChatManager.getInstance();
        if (largeRenderer == null) {
            largeRenderer = new AVChatSurfaceViewRenderer(context);
        } else {
            ViewParent parent = largeRenderer.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(largeRenderer);
            }
        }
        flLargeContainer.addView(largeRenderer);
        largeRenderer.setZOrderMediaOverlay(false);
        chatManager.setupLocalVideoRender(
                largeRenderer,
                false,
                AVChatVideoScalingType.SCALE_ASPECT_BALANCED
        );
    }

    /**
     * 注册/注销对方接听挂断状态（音视频模式切换通知）
     */
    private Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
            AVChatData info = avChatData;
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                    Timber.tag(TAG).d("onEvent %s", "CALLEE_ACK_BUSY");
                    CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.PEER_BUSY);
                    closeSessions(CallExitCode.PEER_BUSY);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    Timber.tag(TAG).d("onEvent %s", "CALLEE_ACK_REJECT");
                    closeSessions(CallExitCode.REJECT);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    Timber.tag(TAG).d("onEvent %s", "CALLEE_ACK_AGREE");
                    mCallEstablished.set(true);
                }
            }
        }
    };

    /**
     * 注册/注销网络通话控制消息（音视频模式切换通知）
     */
    private Observer<AVChatControlEvent> callControlObserver = new Observer<AVChatControlEvent>() {
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
                    isRemoteVideoOff = true;
                    notifyCallStateChanged(CallState.VIDEO_OFF);
                    break;
                case AVChatControlCommand.NOTIFY_VIDEO_ON:
                    isRemoteVideoOff = false;
                    notifyCallStateChanged(CallState.VIDEO_ON);
                    break;
                default:
                    Timber.tag(TAG).d("对方发来指令值：" + notification.getControlCommand());
                    break;
            }
        }
    };

    /**
     * 注册/注销网络通话对方挂断的通知
     */
    private Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent avChatHangUpInfo) {
            AVChatData info = avChatData;
            if (info != null && info.getChatId() == avChatHangUpInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                closeSessions(CallExitCode.HANGUP);
            }
        }
    };

    /**
     * 注册/注销同时在线的其他端对主叫方的响应
     */
    private Observer<AVChatOnlineAckEvent> onlineAckObserver = new Observer<AVChatOnlineAckEvent>() {
        @Override
        public void onEvent(AVChatOnlineAckEvent ackInfo) {
            AVChatData info = avChatData;
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
                    ToastUtils.showShort("通话已在" + client + "端被" + option);
                }
                closeSessions(-1);
            }
        }
    };

    private Observer<Integer> timeoutObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            CallSoundPlayer.instance().stop();
            hangUp();
        }
    };

    private Observer<Integer> autoHangUpForLocalPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            CallSoundPlayer.instance().stop();
            closeSessions(CallExitCode.PEER_BUSY);
        }
    };

    private Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                CallSoundPlayer.instance().stop();
                closeSessions(-1);
            }
        }
    };

    public void closeSessions(int exitCode) {
        if (closing) {
            return;
        }
        closing = true;
        Timber.tag(TAG).d("closeSession: code=%s",CallExitCode.getExitString(exitCode));
        CallSoundPlayer.instance().stop();
        registerCallObserver(false);
        setLargeContainer(null);
        setSmallContainer(null);
        mCallEstablished.set(false);
        stopTimer();
        mCallTimeCallback = null;
        showQuitToast(exitCode);
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mOnCloseSessionListener != null) {
                    mOnCloseSessionListener.onCloseSession();
                    mOnCloseSessionListener = null;
                }
                closing = false;
                mChatting = false;
            }
        }, 2200);
        if (callingState != null && callingState.getValue() >= CallState.OUTGOING_VIDEO_CALLING.getValue() || callingState == CallState.VIDEO) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
        }
        AVChatManager.getInstance().disableRtc();
    }

    /**
     * 给出结束的提醒
     *
     * @param code
     */
    public void showQuitToast(int code) {
        switch (code) {
            case CallExitCode.NET_CHANGE: // 网络切换
            case CallExitCode.NET_ERROR: // 网络异常
            case CallExitCode.CONFIG_ERROR: // 服务器返回数据错误
                ToastUtils.showShort(R.string.call_net_error_then_quit);
                break;
            case CallExitCode.PEER_HANGUP:
            case CallExitCode.HANGUP:
                if (mCallEstablished.get()) {
                    ToastUtils.showShort(R.string.call_call_finish);
                }
                break;
            case CallExitCode.PEER_BUSY:
                ToastUtils.showShort(R.string.call_peer_busy);
                break;
            case CallExitCode.PROTOCOL_INCOMPATIBLE_PEER_LOWER:
                ToastUtils.showShort(R.string.call_peer_protocol_low_version);
                break;
            case CallExitCode.PROTOCOL_INCOMPATIBLE_SELF_LOWER:
                ToastUtils.showShort(R.string.call_local_protocol_low_version);
                break;
            case CallExitCode.INVALIDE_CHANNELID:
                ToastUtils.showShort(R.string.call_invalid_channel_id);
                break;
            case CallExitCode.LOCAL_CALL_BUSY:
                ToastUtils.showShort(R.string.call_local_call_busy);
                break;
            default:
                break;
        }
    }

    public void onCallEstablished() {
        mCallEstablished.set(true);
        CallTimeoutObserver.getInstance().observeTimeoutNotification(
                timeoutObserver, false, isIncomingCall);
        startTimer();
        if (callType == AVChatType.VIDEO.getValue()) {
            notifyCallStateChanged(CallState.VIDEO);
            initSmallSurface();
        } else {
            notifyCallStateChanged(CallState.AUDIO);
        }
    }

    private int mSeconds = 0;

    private void startTimer() {
        mSeconds = 0;
        uiHandler.post(refreshCallTime);
    }

    private void stopTimer() {
        uiHandler.removeCallbacks(refreshCallTime);
    }

    private final Runnable refreshCallTime = new Runnable() {
        @Override
        public void run() {
            uiHandler.postDelayed(refreshCallTime, 1000);
            if (mCallTimeCallback != null) {
                mCallTimeCallback.onCallTime(mSeconds);
            }
            mSeconds++;
        }
    };

    public static String formatCallTime(int seconds) {
        final int hh = seconds / 60 / 60;
        final int mm = seconds / 60 % 60;
        final int ss = seconds % 60;
        return (hh > 9 ? "" + hh : "0" + hh) +
                (mm > 9 ? ":" + mm : ":0" + mm) +
                (ss > 9 ? ":" + ss : ":0" + ss);
    }

    public interface CallTimeCallback {
        void onCallTime(int seconds);
    }

    private CallTimeCallback mCallTimeCallback;

    public void setCallTimeCallback(CallTimeCallback callTimeCallback) {
        mCallTimeCallback = callTimeCallback;
    }

    private void initSmallSurface() {
        if (flSmallContainer == null && !mCallEstablished.get()) {
            return;
        }
        largeAccount = remoteAccount;
        AVChatManager chatManager = AVChatManager.getInstance();
        if (smallRenderer == null) {
            smallRenderer = new AVChatSurfaceViewRenderer(context);
        } else {
            ViewParent parent = smallRenderer.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(smallRenderer);
            }
        }
        flSmallContainer.addView(smallRenderer);
        chatManager.setupLocalVideoRender(
                smallRenderer,
                false,
                AVChatVideoScalingType.SCALE_ASPECT_BALANCED
        );
        smallRenderer.setZOrderOnTop(true);
//        smallRenderer.setZOrderMediaOverlay(true);
    }

    public void call(
            String remoteAccount,
            final AVChatType chatType) {
        CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.CONNECTING);
//        mOuterCallback = callback;
        this.remoteAccount = remoteAccount;
        AVChatManager chatManager = AVChatManager.getInstance();
        closing = false;
        chatManager.enableRtc();
        if (chatType == AVChatType.VIDEO) {
            chatManager.enableVideo();
            previewInLargeSurface();
            videoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            chatManager.setupVideoCapturer(videoCapturer);
//            chatManager.setupLocalVideoRender() onCallEstablished
        }

        // setParameter
        initCallParams();
        chatManager.setParameters(avChatParameters);
        if (chatType == AVChatType.VIDEO) {
            chatManager.startVideoPreview();
        }

        if (chatType == AVChatType.AUDIO) {
            notifyCallStateChanged(CallState.OUTGOING_AUDIO_CALLING);
        } else {
            notifyCallStateChanged(CallState.OUTGOING_VIDEO_CALLING);
        }
        AVChatNotifyOption notifyOption = new AVChatNotifyOption();
        notifyOption.extendMessage = "extra_data";
        notifyOption.webRTCCompat = webrtcCompat;
        Timber.tag(TAG).d("call2: remoteAccount=%s chatType=%s notifyOption=%s", remoteAccount, chatType, notifyOption);
        chatManager.call2(remoteAccount, chatType, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                Timber.tag(TAG).d("call2 -> onSuccess: data=%s", data);
                avChatData = data;
                notifyCallStateChanged(CallState.CONNECT_SUCCESS);
            }

            @Override
            public void onFailed(int code) {
                Timber.tag(TAG).d("call2 -> onFailed: code=%s", code);
                if (code == ResponseCode.RES_FORBIDDEN) {
                    ToastUtils.showShort(R.string.call_no_permission);
                } else {
                    ToastUtils.showShort(R.string.call_call_failed);
                }
//                notifyCallStateChanged(CallState.CONNECT_FAILED);
                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
                Timber.tag(TAG).d("call2 -> onException: exception=%s", exception);
                ToastUtils.showShort(R.string.call_call_failed);
//                notifyCallStateChanged(CallState.CONNECT_FAILED);
                closeSessions(-1);
            }
        });

    }

    /**
     * 来电
     */
    public void onInComingCalling(AVChatData avChatData) {
        this.avChatData = avChatData;
        remoteAccount = avChatData.getAccount();
        CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.RING);
        if (avChatData.getChatType() == AVChatType.AUDIO) {
            notifyCallStateChanged(CallState.INCOMING_AUDIO_CALLING);
        } else {
            notifyCallStateChanged(CallState.INCOMING_VIDEO_CALLING);
        }
    }

    public void refuse() {
        switch (callingState) {
            case INCOMING_AUDIO_CALLING:
            case AUDIO_CONNECTING:
                rejectInComingCall();
                break;
            case INCOMING_AUDIO_TO_VIDEO:
//                rejectAudioToVideo();
                break;
            case INCOMING_VIDEO_CALLING:
            case VIDEO_CONNECTING: // 连接中点击拒绝
                rejectInComingCall();
                break;
            default:
                break;
        }
    }

    private void rejectInComingCall() {
        CallSoundPlayer.instance().stop();
        notifyCallStateChanged(CallState.INCOMING_VIDEO_REFUSING);
        long chatId = avChatData.getChatId();
        Timber.tag(TAG).d("hangUp2: chatId=%s", chatId);
        AVChatManager.getInstance().hangUp2(chatId, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.tag(TAG).d("hangUp2 -> onSuccess:");
                closeSessions(-1);
            }

            @Override
            public void onFailed(int code) {
                Timber.tag(TAG).d("hangUp2 -> onFailed: code=%s", code);
                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
                Timber.tag(TAG).d("hangUp2 -> onException: exception=%s", exception);
                closeSessions(-1);
            }
        });
    }

    public void receive() {
        switch (callingState) {
            case INCOMING_AUDIO_CALLING:
                receiveInComingCall();
                break;
            case AUDIO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_VIDEO_CALLING:
                receiveInComingCall();
                break;
            case VIDEO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_AUDIO_TO_VIDEO:
//                receiveAudioToVideo();
            default:
                break;
        }
    }

    private void receiveInComingCall() {
        CallSoundPlayer.instance().stop();
        if (callingState == CallState.INCOMING_AUDIO_CALLING) {
            notifyCallStateChanged(CallState.AUDIO_CONNECTING);
        } else {
            notifyCallStateChanged(CallState.VIDEO_CONNECTING);
        }
        AVChatManager chatManager = AVChatManager.getInstance();
        closing = false;
        chatManager.enableRtc();
        videoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
        chatManager.setupVideoCapturer(videoCapturer);
        initCallParams();
        chatManager.setParameters(avChatParameters);
        if (callingState.getValue() >= CallState.VIDEO_CONNECTING.getValue()) {
            chatManager.enableVideo();
            chatManager.startVideoPreview();
        }
        notifyCallStateChanged(CallState.INCOMING_VIDEO_RECEIVING);
        long chatId = avChatData.getChatId();
        Timber.tag(TAG).d("accept2: chatId=%s", chatId);
        chatManager.accept2(chatId, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.tag(TAG).d("accept2 -> onSuccess:");
                mCallEstablished.set(true);
                notifyCallStateChanged(CallState.INCOMING_VIDEO_RECEIVE_SUCCESS);
            }

            @Override
            public void onFailed(int code) {
                Timber.tag(TAG).d("accept2 -> onFailed: code=%s", code);
                if (code == -1) {
                    ToastUtils.showShort("本地音视频启动失败");
                } else {
                    ToastUtils.showShort("建立连接失败");
                }
                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
                Timber.tag(TAG).d("accept2 -> onException: exception=%s", exception);
                closeSessions(-1);
            }
        });
    }

    public void hangUp() {
        if (mCallEstablished.get()) {
            hangUp(CallExitCode.HANGUP);
        } else {
            hangUp(CallExitCode.CANCEL);
        }
    }

    private volatile boolean closing;

    private void hangUp(final int code) {
        if ((code == CallExitCode.HANGUP
                || code == CallExitCode.PEER_NO_RESPONSE
                || code == CallExitCode.CANCEL) && avChatData != null) {
            long chatId = avChatData.getChatId();
            Timber.tag(TAG).d("hangUp2: chatId=%s", chatId);
            AVChatManager.getInstance().hangUp2(chatId, new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Timber.tag(TAG).d("hangUp2 -> onSuccess: ");
                    closeSessions(code);
                }

                @Override
                public void onFailed(int errorCode) {
                    Timber.tag(TAG).d("hangUp2 -> onFailed: errorCode=%s", errorCode);
                    closeSessions(code);
                }

                @Override
                public void onException(Throwable exception) {
                    Timber.tag(TAG).d("hangUp2 -> onException: exception=%s", exception);
                    closeSessions(code);
                }
            });
        }
    }

    public void switchCamera() {
        if (videoCapturer != null) {
            videoCapturer.switchCamera();
        }
    }

    private boolean needRestoreLocalVideo = false;
    private boolean needRestoreLocalAudio = false;

    //恢复视频和语音发送
    public void resume() {
        if (needRestoreLocalVideo) {
            AVChatManager.getInstance().muteLocalVideo(false);
            needRestoreLocalVideo = false;
        }

        if (needRestoreLocalAudio) {
            AVChatManager.getInstance().muteLocalAudio(false);
            needRestoreLocalAudio = false;
        }

    }

    //关闭视频和语音发送.
    public void pause() {
        if (!AVChatManager.getInstance().isLocalVideoMuted()) {
            AVChatManager.getInstance().muteLocalVideo(true);
            needRestoreLocalVideo = true;
        }

        if (!AVChatManager.getInstance().isLocalAudioMuted()) {
            AVChatManager.getInstance().muteLocalAudio(true);
            needRestoreLocalAudio = true;
        }
    }

    private CallFloatViewHelper callFloatViewHelper;

    public void enterFloatWindow() {
        if (callFloatViewHelper == null) {
            callFloatViewHelper = new CallFloatViewHelper(
                    context,
                    onSurfaceContainerPreparedListener
            );
        }
        callFloatViewHelper.setFullScreenOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFloatWindow();
                CallHelper.launchFromSmall(context);
            }
        });
        callFloatViewHelper.setCloseOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addOnCallStateChangeListener(callFloatViewHelper);
        setCallTimeCallback(callFloatViewHelper);
        setOnCloseSessionListener(callFloatViewHelper);
        callFloatViewHelper.show();
    }

    private CallFloatViewHelper.OnSurfaceContainerPreparedListener onSurfaceContainerPreparedListener
            = new CallFloatViewHelper.OnSurfaceContainerPreparedListener() {
        @Override
        public void onSurfaceContainerPrepared(FrameLayout smallContainer, FrameLayout largeContainer) {
            setSmallContainer(smallContainer);
            setLargeContainer(largeContainer);
        }
    };

    public void exitFloatWindow() {
        if (callFloatViewHelper != null) {
            removeOnCallStateChangeListener(callFloatViewHelper);
            setCallTimeCallback(null);
            setOnCloseSessionListener(null);
            setSmallContainer(null);
            setLargeContainer(null);
            callFloatViewHelper.dismiss();
            callFloatViewHelper = null;
        }
    }

    public void dispatchIncomingCallFromBroadCast(Context context, AVChatData avChatData) {
        this.avChatData = avChatData;
        isIncomingCall = true;
        CallHelper.launch(context, avChatData, CallHelper.SOURCE_BROADCAST);
    }

    /**
     * 1, autoCallProximity: 语音通话时使用, 距离感应自动黑屏
     * 2, videoCropRatio: 制定固定的画面裁剪比例，发送端有效
     * 3, videoAutoRotate: 结合自己设备角度和对方设备角度自动旋转画面
     * 4, serverRecordAudio: 需要服务器录制语音, 同时需要 APP KEY 下面开通了服务器录制功能
     * 5, serverRecordVideo: 需要服务器录制视频, 同时需要 APP KEY 下面开通了服务器录制功能
     * 6, defaultFrontCamera: 默认是否使用前置摄像头
     * 7, videoQuality: 视频质量调整, 最高建议使用480P
     * 8, videoFpsReported: 是否开启视频绘制帧率汇报
     * 9, deviceDefaultRotation: 99.99%情况下你不需要设置这个参数, 当设备固定在水平方向时,并且设备不会移动, 这时是无法确定设备角度的,可以设置一个默认角度
     * 10, deviceRotationOffset: 99.99%情况下你不需要设置这个参数, 当你的设备传感器获取的角度永远偏移固定值时设置,用于修正旋转角度
     * 11, videoMaxBitrate: 视频最大码率设置, 100K ~ 5M. 如果没有特殊需求不要去设置,会影响SDK内部的调节机制
     * 12, audioEffectAecMode: 语音处理选择, 默认使用平台内置,当你发现平台内置不好用时可以设置到SDK内置
     * 13, audioEffectNsMode: 语音处理选择, 默认使用平台内置,当你发现平台内置不好用时可以设置到SDK内置
     * 14, videoHwEncoderMode: 视频编码类型, 默认情况下不用设置.
     * 15, videoHwDecoderMode: 视频解码类型, 默认情况下不用设置.
     * 16, audioHighQuality: 高清语音，采用更高的采样率来传输语音
     * 17, audioDtx: 非连续发送，当监测到人声非活跃状态时减少数据包的发送
     */
    public void initCallParams() {
        if (avChatParameters == null) {
            avChatParameters = new AVChatParameters();
        }
        avChatParameters.setBoolean(AVChatParameters.KEY_AUDIO_CALL_PROXIMITY, autoCallProximity);
        avChatParameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, videoCropRatio);
        avChatParameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, videoAutoRotate);
        avChatParameters.setBoolean(AVChatParameters.KEY_SERVER_AUDIO_RECORD, serverRecordAudio);
        avChatParameters.setBoolean(AVChatParameters.KEY_SERVER_VIDEO_RECORD, serverRecordVideo);
        avChatParameters.setBoolean(AVChatParameters.KEY_VIDEO_DEFAULT_FRONT_CAMERA, defaultFrontCamera);
        avChatParameters.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, videoQuality);
        avChatParameters.setBoolean(AVChatParameters.KEY_VIDEO_FPS_REPORTED, videoFpsReported);
        avChatParameters.setInteger(AVChatParameters.KEY_DEVICE_DEFAULT_ROTATION, deviceDefaultRotation);
        avChatParameters.setInteger(AVChatParameters.KEY_DEVICE_ROTATION_FIXED_OFFSET, deviceRotationOffset);

        if (videoMaxBitrate > 0) {
            avChatParameters.setInteger(AVChatParameters.KEY_VIDEO_MAX_BITRATE, videoMaxBitrate * 1024);
        }
        switch (audioEffectAecMode) {
            case 0:
                avChatParameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_ACOUSTIC_ECHO_CANCELER, AVChatAudioEffectMode.DISABLE);
                break;
            case 1:
                avChatParameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_ACOUSTIC_ECHO_CANCELER, AVChatAudioEffectMode.SDK_BUILTIN);
                break;
            case 2:
                avChatParameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_ACOUSTIC_ECHO_CANCELER, AVChatAudioEffectMode.PLATFORM_BUILTIN);
                break;
        }
        switch (audioEffectNsMode) {
            case 0:
                avChatParameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_NOISE_SUPPRESSOR, AVChatAudioEffectMode.DISABLE);
                break;
            case 1:
                avChatParameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_NOISE_SUPPRESSOR, AVChatAudioEffectMode.SDK_BUILTIN);
                break;
            case 2:
                avChatParameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_NOISE_SUPPRESSOR, AVChatAudioEffectMode.PLATFORM_BUILTIN);
                break;
        }
        switch (videoHwEncoderMode) {
            case 0:
                avChatParameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_AUTO);
                break;
            case 1:
                avChatParameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
                break;
            case 2:
                avChatParameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_HARDWARE);
                break;
        }
        switch (videoHwDecoderMode) {
            case 0:
                avChatParameters.setString(AVChatParameters.KEY_VIDEO_DECODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_AUTO);
                break;
            case 1:
                avChatParameters.setString(AVChatParameters.KEY_VIDEO_DECODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
                break;
            case 2:
                avChatParameters.setString(AVChatParameters.KEY_VIDEO_DECODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_HARDWARE);
                break;
        }
        avChatParameters.setBoolean(AVChatParameters.KEY_AUDIO_HIGH_QUALITY, audioHighQuality);
        avChatParameters.setBoolean(AVChatParameters.KEY_AUDIO_DTX_ENABLE, audioDtx);

        //观众角色,多人模式下使用. IM Demo没有多人通话, 全部设置为AVChatUserRole.NORMAL.
        avChatParameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);

        //采用I420图像格式
        avChatParameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_FILTER_FORMAT, AVChatImageFormat.I420);
    }

    private AVChatParameters avChatParameters;

    private int videoCropRatio = 0;
    private boolean videoAutoRotate = true;
    private int videoQuality = 0;
    private boolean serverRecordAudio = false;
    private boolean serverRecordVideo = false;
    private boolean defaultFrontCamera = true;
    private boolean autoCallProximity = true;
    private int videoHwEncoderMode = 0;
    private int videoHwDecoderMode = 0;
    private boolean videoFpsReported = true;
    private int audioEffectAecMode = 2;
    private int audioEffectNsMode = 2;
    private int videoMaxBitrate = 0;
    private int deviceDefaultRotation = 0;
    private int deviceRotationOffset = 0;
    private boolean audioHighQuality = false;
    private boolean audioDtx = true;
    private boolean webrtcCompat = true;
}
