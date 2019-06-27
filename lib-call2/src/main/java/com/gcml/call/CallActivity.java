package com.gcml.call;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;

public class CallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity_call);
        initView();
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        CallHelper.INSTANCE.setSmallContainer(flSmallContainer);
        CallHelper.INSTANCE.setLargeContainer(flLargeContainer);
        CallHelper.INSTANCE.addOnCallStateChangeListener(mCallListener);
        CallHelper.INSTANCE.setCallTimeCallback(mCallTimeCallback);
        CallHelper.INSTANCE.setOnCloseSessionListener(onCloseSessionListener);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CallHelper.INSTANCE.parse(extras);
        }
    }

    private CallHelper.OnCloseSessionListener onCloseSessionListener = new CallHelper.OnCloseSessionListener() {
        @Override
        public void onCloseSession() {
            CallHelper.INSTANCE.removeOnCallStateChangeListener(mCallListener);
            CallHelper.INSTANCE.setCallTimeCallback(null);
            CallHelper.INSTANCE.setOnCloseSessionListener(null);
            CallHelper.INSTANCE.setSmallContainer(null);
            CallHelper.INSTANCE.setLargeContainer(null);
            finish();
        }
    };

    private CallHelper.CallTimeCallback mCallTimeCallback = new CallHelper.CallTimeCallback() {
        @Override
        public void onCallTime(int seconds) {
            tvCallTime.setText(CallHelper.formatCallTime(seconds));
        }
    };

    private boolean canSwitchCamera;

    public CallHelper.OnCallStateChangeListener mCallListener = new CallHelper.OnCallStateChangeListener() {
        @Override
        public void onCallStateChanged(CallState state) {
            if (clRoot == null) {
                return;
            }
            if (CallState.isVideoMode(state))
                switch (state) {
                    case OUTGOING_VIDEO_CALLING:
                        ivLargeCover.setVisibility(View.GONE);
                        showPeerProfile(true);
                        showStatus(R.string.call_wait_recieve);
                        showTime(false);
                        showReceive(false);
                        showFloatWindow(false);
                        showSwitchCamera(false);
                        showMuteAudio(false);
                        showMuteVideo(false);
                        showHangUp(true);
                        break;
                    case INCOMING_VIDEO_CALLING:
                        ivLargeCover.setVisibility(View.VISIBLE);
                        showPeerProfile(true);
                        showStatus(R.string.call_video_call_request);
                        showTime(false);
                        showFloatWindow(false);
                        showSwitchCamera(false);
                        showMuteAudio(false);
                        showMuteVideo(false);
                        showReceive(true);
                        showHangUp(true);
                        break;
                    case VIDEO_CONNECTING:
                        showPeerProfile(true);
                        showStatus(R.string.call_connecting);
                        showTime(false);
                        showReceive(false);
                        showFloatWindow(false);
                        showSwitchCamera(false);
                        showMuteAudio(false);
                        showMuteVideo(false);
                        showHangUp(true);
                        break;
                    case VIDEO:
                        ivLargeCover.setVisibility(View.GONE);
                        showPeerProfile(false);
                        hideStatus();
                        showTime(true);
                        showFloatWindow(false);
                        showSwitchCamera(false);
                        showMuteAudio(false);
                        showMuteVideo(false);
                        showReceive(false);
                        showHangUp(true);
                        break;
                    case OUTGOING_AUDIO_TO_VIDEO:
                        break;
                    default:
                        break;
                }
        }
    };

    private void showStatus(@StringRes int res) {
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(res);
    }

    private void hideStatus() {
        tvStatus.setVisibility(View.GONE);
    }

    private void showHangUp(boolean show) {
        llHangup.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            boolean established = CallHelper.INSTANCE.isCallEstablished();
            String text = established
                    ? "挂断"
                    : CallHelper.INSTANCE.isIncomingCall() ? "拒绝" : "取消";
            tvHangup.setText(text);
        }
    }

    private void showMuteVideo(boolean show) {
        show = show && CallHelper.INSTANCE.isCallEstablished();
        llMuteVideo.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            boolean muted = AVChatManager.getInstance().isLocalVideoMuted();
            muteVideo(muted);
        }
    }

    private void showMuteAudio(boolean show) {
        show = show && CallHelper.INSTANCE.isCallEstablished();
        llMuteAudio.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            boolean muted = AVChatManager.getInstance().isLocalAudioMuted();
            muteAudio(muted);
        }
    }

    private void showTime(boolean show) {
        tvCallTime.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showFloatWindow(boolean show) {
        llFloatWindow.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showSwitchCamera(boolean enable) {
        canSwitchCamera = enable && AVChatCameraCapturer.hasMultipleCameras();
        llSwitchCamera.setVisibility(canSwitchCamera ? View.VISIBLE : View.GONE);
    }

    private void showReceive(boolean show) {
        llReceive.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showPeerProfile(boolean show) {
        //show avatar
        ivPeerAvatar.setVisibility(show ? View.VISIBLE : View.GONE);
        tvPeerNickname.setVisibility(show ? View.VISIBLE : View.GONE);
        String peerAccount = CallHelper.INSTANCE.getRemoteAccount();
        tvPeerNickname.setText(peerAccount);
    }

    @Override
    public void onBackPressed() {
        //no back pressed
    }

    ImageView ivLargeCover;
    ImageView ivSmallCover;
    FrameLayout flSmallContainer;
    FrameLayout flLargeContainer;
    ImageView ivPeerAvatar;
    TextView tvPeerNickname;
    TextView tvStatus;
    TextView tvCallTime;
    LinearLayout llFloatWindow;
    ImageView ivFloatWindow;
    TextView tvFloatWindow;
    LinearLayout llSwitchCamera;
    ImageView ivSwitchCamera;
    TextView tvSwitchCamera;
    LinearLayout llMuteVideo;
    ImageView ivMuteVideo;
    TextView tvMuteVideo;
    LinearLayout llMuteAudio;
    ImageView ivMuteAudio;
    TextView tvMuteAudio;
    LinearLayout llHangup;
    ImageView ivHangup;
    TextView tvHangup;
    LinearLayout llReceive;
    ImageView ivReceive;
    TextView tvReceive;
    ConstraintLayout clRoot;

    public void initView() {
        clRoot = (ConstraintLayout) findViewById(R.id.cl_root);
        ivLargeCover = (ImageView) findViewById(R.id.iv_large_cover);
        ivSmallCover = (ImageView) findViewById(R.id.iv_small_cover);
        flSmallContainer = (FrameLayout) findViewById(R.id.fl_small_container);
        flLargeContainer = (FrameLayout) findViewById(R.id.fl_large_container);
        ivPeerAvatar = (ImageView) findViewById(R.id.iv_peer_avatar);
        tvPeerNickname = (TextView) findViewById(R.id.tv_peer_nickname);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvCallTime = (TextView) findViewById(R.id.tv_time);
        llFloatWindow = (LinearLayout) findViewById(R.id.ll_float_window);
        ivFloatWindow = (ImageView) findViewById(R.id.iv_float_window);
        tvFloatWindow = (TextView) findViewById(R.id.tv_float_window);
        llSwitchCamera = (LinearLayout) findViewById(R.id.ll_switch_camera);
        ivSwitchCamera = (ImageView) findViewById(R.id.iv_switch_camera);
        tvSwitchCamera = (TextView) findViewById(R.id.tv_switch_camera);
        llMuteVideo = (LinearLayout) findViewById(R.id.ll_mute_video);
        ivMuteVideo = (ImageView) findViewById(R.id.iv_mute_video);
        tvMuteVideo = (TextView) findViewById(R.id.tv_mute_video);
        llMuteAudio = (LinearLayout) findViewById(R.id.ll_mute_audio);
        ivMuteAudio = (ImageView) findViewById(R.id.iv_mute_audio);
        tvMuteAudio = (TextView) findViewById(R.id.tv_mute_audio);
        llHangup = (LinearLayout) findViewById(R.id.ll_hangup);
        ivHangup = (ImageView) findViewById(R.id.iv_hangup);
        tvHangup = (TextView) findViewById(R.id.tv_hangup);
        llReceive = (LinearLayout) findViewById(R.id.ll_receive);
        ivReceive = (ImageView) findViewById(R.id.iv_receive);
        tvReceive = (TextView) findViewById(R.id.tv_receive);

        llFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CallHelper.INSTANCE.enterFloatWindow();
            }
        });

        llSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLlSwitchCameraClicked();
            }
        });
        llMuteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLlMuteVideoClicked();
            }
        });
        llMuteAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLlMuteAudioClicked();
            }
        });
        llHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLlHangUpClicked();
            }
        });
        llReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLlReceiveClicked();
            }
        });
    }

    private void onLlReceiveClicked() {
        CallHelper.INSTANCE.receive();
    }

    private void onLlHangUpClicked() {
        CallHelper.INSTANCE.hangUp();
    }

    private void onLlMuteAudioClicked() {
        boolean muted = !AVChatManager.getInstance().isLocalAudioMuted();
        muteAudio(muted);
    }

    private void muteAudio(boolean muted) {
        boolean established = CallHelper.INSTANCE.isCallEstablished();
        if (established) { // 连接已经建立
            AVChatManager.getInstance().muteLocalAudio(muted);
            ivMuteAudio.setSelected(muted);
            tvMuteAudio.setText(muted ? "取消静音" : "静音");
        }
    }

    private void onLlMuteVideoClicked() {
        boolean muted = !AVChatManager.getInstance().isLocalVideoMuted();
        muteVideo(muted);
    }

    private void muteVideo(boolean muted) {
        boolean established = CallHelper.INSTANCE.isCallEstablished();
        if (established) { // 连接已经建立
            AVChatManager.getInstance().muteLocalVideo(muted);
            ivMuteVideo.setSelected(muted);
            tvMuteVideo.setText(muted ? "打开视频" : "关闭视频");
        }
    }

    private void onLlSwitchCameraClicked() {
        CallHelper.INSTANCE.switchCamera();
    }
}
