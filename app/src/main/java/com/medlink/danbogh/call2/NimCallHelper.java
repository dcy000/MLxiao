package com.medlink.danbogh.call2;

import android.content.Context;
import android.util.Log;

import com.example.han.referralproject.R;
import com.medlink.danbogh.utils.T;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatAudioEffectMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatImageFormat;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lenovo on 2017/10/23.
 */

public class NimCallHelper {

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
//        if (state == callingState) {
//            return;
//        }
        callingState = state;
        for (OnCallStateChangeListener listener : mOnCallStateChangeListeners) {
            if (listener != null) {
                listener.onCallStateChanged(state);
            }
        }
    }

    private static final String TAG = "NimCallHelper";

    public String mPeerAccount;
    private AVChatCameraCapturer mVideoCapturer;
    private CallState callingState;
    private AVChatData avChatData;
    public AVChatCallback<AVChatData> mInnerCallback;
    public AVChatCallback<AVChatData> mOuterCallback;
    private boolean mRtcDestroyed;
    private AtomicBoolean mCallEstablished = new AtomicBoolean(false);
    private boolean canSwitchCamera;

    public static NimCallHelper getInstance() {
        if (sInstance == null) {
            synchronized (NimCallHelper.class) {
                if (sInstance == null) {
                    sInstance = new NimCallHelper();
                }
            }
        }
        return sInstance;
    }

    private static NimCallHelper sInstance;


    private boolean isChatting = false;

    public void setCallEstablished(boolean callEstablished) {
        mCallEstablished.set(callEstablished);
    }

    public boolean isCallEstablished() {
        return mCallEstablished.get();
    }

    public String getPeerAccount() {
        return mPeerAccount;
    }

    public void setPeerAccount(String peerAccount) {
        mPeerAccount = peerAccount;
    }

    public boolean isChatting() {
        return isChatting;
    }

    public void setChatting(boolean chatting) {
        isChatting = chatting;
    }

    public AVChatData getAvChatData() {
        return avChatData;
    }

    public void setAvChatData(AVChatData avChatData) {
        this.avChatData = avChatData;
    }

    /**
     * 拨打音视频
     */
    public void call2(String peerAccount, final AVChatType chatType, AVChatCallback<AVChatData> callback) {
        CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.CONNECTING);
        mOuterCallback = callback;
        mPeerAccount = peerAccount;
        AVChatNotifyOption notifyOption = new AVChatNotifyOption();
        notifyOption.extendMessage = "extra_data";
        notifyOption.webRTCCompat = webrtcCompat;
//        默认forceKeepCalling为true，开发者如果不需要离线持续呼叫功能可以将forceKeepCalling设为false
//        notifyOption.forceKeepCalling = false;
        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }
        this.callingState = (chatType == AVChatType.VIDEO ? CallState.VIDEO : CallState.AUDIO);
        AVChatManager.getInstance().setParameters(avChatParameters);
        if (chatType == AVChatType.VIDEO) {
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().startVideoPreview();
        }
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, false);
        if (mInnerCallback == null) {
            mInnerCallback = new AVChatCallback<AVChatData>() {
                @Override
                public void onSuccess(AVChatData data) {
                    avChatData = data;
                    if (mOuterCallback != null) {
                        mOuterCallback.onSuccess(data);
                    }
//                DialogMaker.dismissProgressDialog();
                    //如果需要使用视频预览功能，在此进行设置，调用setupLocalVideoRender
                    //如果不需要视频预览功能，那么删掉下面if语句代码即可
                    if (chatType == AVChatType.VIDEO) {
//                    initLargeSurfaceView(DemoCache.getAccount());
//                    canSwitchCamera = true;
                        notifyCallStateChanged(CallState.OUTGOING_VIDEO_CALLING);
                    }
                }

                @Override
                public void onFailed(int code) {
                    Log.d(TAG, "avChat call failed code->" + code);

                    if (code == ResponseCode.RES_FORBIDDEN) {
                        T.show(R.string.avchat_no_permission);
                    } else {
                        T.show(R.string.avchat_call_failed);
                    }
                    closeRtc();
                    closeSessions(-1);
                    if (mOuterCallback != null) {
                        mOuterCallback.onFailed(code);
                    }
                }

                @Override
                public void onException(Throwable exception) {
                    Log.d(TAG, "avChat call onException->" + exception);
                    closeRtc();
                    closeSessions(-1);
                    if (mOuterCallback != null) {
                        mOuterCallback.onException(exception);
                    }
                }
            };
        }
        AVChatManager.getInstance().call2(peerAccount, chatType, notifyOption, mInnerCallback);

        if (chatType == AVChatType.AUDIO) {
            notifyCallStateChanged(CallState.OUTGOING_AUDIO_CALLING);
        } else {
            notifyCallStateChanged(CallState.OUTGOING_VIDEO_CALLING);
        }
    }

    /**
     * 来电
     */
    public void inComingCalling(AVChatData avChatData) {
        this.avChatData = avChatData;
        mPeerAccount = avChatData.getAccount();
        CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.RING);
        if (avChatData.getChatType() == AVChatType.AUDIO) {
            notifyCallStateChanged(CallState.INCOMING_AUDIO_CALLING);
        } else {
            notifyCallStateChanged(CallState.INCOMING_VIDEO_CALLING);
        }
    }


    public void closeRtc() {
        if (mRtcDestroyed) {
            return;
        }
        mRtcDestroyed = true;
        if (callingState == CallState.OUTGOING_VIDEO_CALLING || callingState == CallState.VIDEO) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
        }
        AVChatManager.getInstance().disableRtc();
//        DialogMaker.dismissProgressDialog();
        CallSoundPlayer.instance().stop();
    }

    /**
     * 拒绝操作，根据当前状态来选择合适的操作
     */
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
        /**
         * 接收方拒绝通话
         * AVChatCallback 回调函数
         */
        AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "reject onSuccess-");
            }

            @Override
            public void onFailed(int code) {
                Log.d(TAG, "reject onFailed->" + code);
            }

            @Override
            public void onException(Throwable exception) {
                Log.d(TAG, "reject onException");
            }
        });
        closeSessions(CallExitCode.REJECT);
        CallSoundPlayer.instance().stop();
    }

    public void receive() {
        switch (callingState) {
            case INCOMING_AUDIO_CALLING:
                receiveInComingCall();
                notifyCallStateChanged(CallState.AUDIO_CONNECTING);
                break;
            case AUDIO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_VIDEO_CALLING:
                receiveInComingCall();
                notifyCallStateChanged(CallState.VIDEO_CONNECTING);
                break;
            case VIDEO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_AUDIO_TO_VIDEO:
//                receiveAudioToVideo();
            default:
                break;
        }
    }

    /**
     * 接听来电
     */
    private void receiveInComingCall() {
        //接听，告知服务器，以便通知其他端

        if (callingState == CallState.INCOMING_AUDIO_CALLING) {
            notifyCallStateChanged(CallState.AUDIO_CONNECTING);
        } else {
            notifyCallStateChanged(CallState.VIDEO_CONNECTING);
        }

        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }
        AVChatManager.getInstance().setParameters(avChatParameters);
        if (callingState == CallState.VIDEO_CONNECTING) {
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().startVideoPreview();
        }

        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, false);
        AVChatManager.getInstance().accept2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "accept success");

                mCallEstablished.set(true);
                canSwitchCamera = true;
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    T.show("本地音视频启动失败");
                } else {
                    T.show("建立连接失败");
                }
                Log.e(TAG, "accept onFailed->" + code);
                handleAcceptFailed();
            }

            @Override
            public void onException(Throwable exception) {
                Log.d(TAG, "accept exception->" + exception);
                handleAcceptFailed();
            }
        });
        CallSoundPlayer.instance().stop();
    }

    private void handleAcceptFailed() {
        if (mRtcDestroyed) {
            return;
        }
        mRtcDestroyed = true;
        if (callingState == CallState.VIDEO_CONNECTING) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
        }
        AVChatManager.getInstance().disableRtc();
        closeSessions(CallExitCode.CANCEL);
    }

    /**
     * 点击挂断或取消
     */
    public void hangUp() {
        if (mCallEstablished.get()) {
            hangUp(CallExitCode.HANGUP);
        } else {
            hangUp(CallExitCode.CANCEL);
        }
    }

    /**
     * 挂断
     *
     * @param code 音视频类型
     */
    private void hangUp(final int code) {
        if (mRtcDestroyed) {
            return;
        }
        mRtcDestroyed = true;
        if (callingState == CallState.OUTGOING_VIDEO_CALLING || callingState == CallState.VIDEO) {
            AVChatManager.getInstance().stopVideoPreview();
        }
        if ((code == CallExitCode.HANGUP
                || code == CallExitCode.PEER_NO_RESPONSE
                || code == CallExitCode.CANCEL) && avChatData != null) {
            AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }

                @Override
                public void onFailed(int code) {
                    Log.d(TAG, "hangup onFailed->" + code);
                }

                @Override
                public void onException(Throwable exception) {
                    Log.d(TAG, "hangup onException->" + exception);
                }
            });
        }
        AVChatManager.getInstance().disableRtc();
        closeSessions(code);
        CallSoundPlayer.instance().stop();
    }

    public void switchCamera() {
        if (mVideoCapturer != null) {
            mVideoCapturer.switchCamera();
        }
    }


    private boolean needRestoreLocalVideo = false;
    private boolean needRestoreLocalAudio = false;

    //恢复视频和语音发送
    public void resumeVideo() {
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
    public void pauseVideo() {

        if (!AVChatManager.getInstance().isLocalVideoMuted()) {
            AVChatManager.getInstance().muteLocalVideo(true);
            needRestoreLocalVideo = true;
        }

        if (!AVChatManager.getInstance().isLocalAudioMuted()) {
            AVChatManager.getInstance().muteLocalAudio(true);
            needRestoreLocalAudio = true;
        }
    }


    public void closeSessions(int exitCode) {
        //not  user  hang up active  and warning tone is playing,so wait its end
        Log.i(TAG, "close session -> " + CallExitCode.getExitString(exitCode));
        showQuitToast(exitCode);
        mCallEstablished.set(false);
        canSwitchCamera = false;
//        try {
//            Thread.sleep(1800);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (mOnCloseSessionListener != null) {
            mOnCloseSessionListener.onCloseSession();
            mOnCloseSessionListener = null;
        }

//        if (exitCode == CallExitCode.HANGUP || exitCode == CallExitCode.PEER_HANGUP) {
//            Context context = MyApplication.getInstance().getApplicationContext();
//            Intent intent = new Intent(context, AppraiseActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
    }

    public void destroy() {
        sInstance = null;
    }

    private OnCloseSessionListener mOnCloseSessionListener;

    public void setOnCloseSessionListener(OnCloseSessionListener onCloseSessionListener) {
        mOnCloseSessionListener = onCloseSessionListener;
    }

    public interface OnCloseSessionListener {
        void onCloseSession();
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
                T.show(R.string.avchat_net_error_then_quit);
                break;
            case CallExitCode.PEER_HANGUP:
            case CallExitCode.HANGUP:
                if (mCallEstablished.get()) {
                    T.show(R.string.avchat_call_finish);
                }
                break;
            case CallExitCode.PEER_BUSY:
                T.show(R.string.avchat_peer_busy);
                break;
            case CallExitCode.PROTOCOL_INCOMPATIBLE_PEER_LOWER:
                T.show(R.string.avchat_peer_protocol_low_version);
                break;
            case CallExitCode.PROTOCOL_INCOMPATIBLE_SELF_LOWER:
                T.show(R.string.avchat_local_protocol_low_version);
                break;
            case CallExitCode.INVALIDE_CHANNELID:
                T.show(R.string.avchat_invalid_channel_id);
                break;
            case CallExitCode.LOCAL_CALL_BUSY:
                T.show(R.string.avchat_local_call_busy);
                break;
            default:
                break;
        }
    }

    public void dispatchIncomingCallFromBroadCast(Context context, AVChatData avChatData) {
        NimCallActivity.launch(context, avChatData, NimCallActivity.SOURCE_BROADCAST);
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
    private boolean defaultFrontCamera = false;
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