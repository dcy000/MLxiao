package com.ml.call;

import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;

public class CallActivity extends AppCompatActivity {

    private static final String TAG = "CallActivity";

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
        CallHelper.getInstance().setSmallContainer(flSmallContainer);
        CallHelper.getInstance().setLargeContainer(flLargeContainer);
        CallHelper.getInstance().addOnCallStateChangeListener(mCallListener);
        CallHelper.getInstance().setCallTimeCallback(mCallTimeCallback);
        CallHelper.getInstance().setOnCloseSessionListener(onCloseSessionListener);
        Bundle extras = getIntent().getExtras();
        extras = extras == null ? new Bundle() : extras;
        CallHelper.getInstance().checkSource(extras);
    }

    private CallHelper.OnCloseSessionListener onCloseSessionListener = new CallHelper.OnCloseSessionListener() {
        @Override
        public void onCloseSession() {
            CallHelper.getInstance().removeOnCallStateChangeListener(mCallListener);
            CallHelper.getInstance().setCallTimeCallback(null);
            CallHelper.getInstance().setOnCloseSessionListener(null);
            CallHelper.getInstance().setSmallContainer(null);
            CallHelper.getInstance().setLargeContainer(null);
            finish();
        }
    };

    private CallHelper.CallTimeCallback mCallTimeCallback = new CallHelper.CallTimeCallback() {
        @Override
        public void onCallTime(int seconds) {
            tvCallTime.setText(CallHelper.formatCallTime(seconds));
        }
    };

    private boolean shouldEnableToggle = false;
    private boolean canSwitchCamera;

    public CallHelper.OnCallStateChangeListener mCallListener = new CallHelper.OnCallStateChangeListener() {
        @Override
        public void onCallStateChanged(CallState state) {
            Log.d(TAG, "onCallStateChanged: ");
            if (clRoot == null) {
                return;
            }
            if (CallState.isVideoMode(state))
                switch (state) {
                    case OUTGOING_VIDEO_CALLING:
                        showProfile(true);
                        showStatus(R.string.call_wait_recieve);
                        showTime(false);
                        showRefuseReceive(false);
                        shouldEnableToggle = true;
                        enableCameraToggle();
                        showBottomPanel(true);
                        break;
                    case INCOMING_VIDEO_CALLING:
                        showProfile(true);
                        showStatus(R.string.call_video_call_request);
                        showTime(false);
                        showRefuseReceive(true);
                        showBottomPanel(false);
                        break;
                    case VIDEO_CONNECTING:
                        showStatus(R.string.call_connecting);
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
        String peerAccount = CallHelper.getInstance().getRemoteAccount();
        tvNickname.setText(peerAccount);
    }

    @Override
    public void onBackPressed() {
        //no back pressed
    }

    //    @BindView(R.id.iv_call_small_cover)
    ImageView ivSmallCover;
    //    @BindView(R.id.fl_call_small_container)
    FrameLayout flSmallContainer;
    //    @BindView(R.id.fl_call_large_container)
    FrameLayout flLargeContainer;
    //    @BindView(R.id.tv_call_time)
    TextView tvCallTime;
    //    @BindView(R.id.iv_call_peer_avatar)
    ImageView ivPeerAvatar;
    //    @BindView(R.id.tv_call_nickname)
    TextView tvNickname;
    //    @BindView(R.id.tv_call_status)
    TextView tvStatus;
    //    @BindView(R.id.ic_call_switch_camera)
    ImageView ivSwitchCamera;
    //    @BindView(R.id.iv_call_toggle_camera)
    ImageView ivToggleCamera;
    //    @BindView(R.id.iv_call_toggle_mute)
    ImageView ivToggleMute;
    //    @BindView(R.id.iv_call_hang_up)
    ImageView ivHangUp;
    //    @BindView(R.id.tv_call_receive)
    TextView tvReceive;
    //    @BindView(R.id.tv_call_refuse)
    TextView tvRefuse;
    //    @BindView(R.id.cl_call_root)
    ConstraintLayout clRoot;

    public void initView() {
        ivSmallCover = (ImageView) findViewById(R.id.call_iv_call_small_cover);
        flSmallContainer = (FrameLayout) findViewById(R.id.call_fl_call_small_container);
        flLargeContainer = (FrameLayout) findViewById(R.id.call_fl_call_large_container);
        tvCallTime = (TextView) findViewById(R.id.call_tv_call_time);
        ivPeerAvatar = (ImageView) findViewById(R.id.call_iv_call_peer_avatar);
        tvNickname = (TextView) findViewById(R.id.call_tv_call_nickname);
        tvStatus = (TextView) findViewById(R.id.call_tv_call_status);
        ivSwitchCamera = (ImageView) findViewById(R.id.call_ic_switch_camera);
        ivSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIvSwitchCameraClicked();
            }
        });
        ivToggleCamera = (ImageView) findViewById(R.id.call_iv_toggle_camera);
        ivToggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIvToggleCameraClicked();
            }
        });
        ivToggleMute = (ImageView) findViewById(R.id.call_iv_toggle_mute);
        ivToggleMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIvToggleMuteClicked();
            }
        });
        ivHangUp = (ImageView) findViewById(R.id.call_iv_hang_up);
        ivHangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIvHangUpClicked();
            }
        });
        tvReceive = (TextView) findViewById(R.id.call_tv_receive);
        tvReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvReceiveClicked();
            }
        });
        tvRefuse = (TextView) findViewById(R.id.call_tv_refuse);
        tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvRefuseClicked();
            }
        });
        clRoot = (ConstraintLayout) findViewById(R.id.call_cl_call_root);
    }

    private void onTvRefuseClicked() {
        CallHelper.getInstance().refuse();
    }

    private void onTvReceiveClicked() {
        CallHelper.getInstance().receive();
    }

    private void onIvHangUpClicked() {
        CallHelper.getInstance().hangUp();
    }

    private void onIvToggleMuteClicked() {
        boolean established = CallHelper.getInstance().isCallEstablished();
        if (established) { // 连接已经建立
            boolean muted = AVChatManager.getInstance().isLocalAudioMuted();
            AVChatManager.getInstance().muteLocalAudio(!muted);
            ivToggleMute.setSelected(!muted);
        }
    }

    private void onIvToggleCameraClicked() {
        CallHelper.getInstance().removeOnCallStateChangeListener(mCallListener);
        CallHelper.getInstance().setCallTimeCallback(null);
        CallHelper.getInstance().setOnCloseSessionListener(null);
        CallHelper.getInstance().setSmallContainer(null);
        CallHelper.getInstance().setLargeContainer(null);
        CallHelper.getInstance().enterFloatWindow();
        finish();

//        boolean selected = ivToggleCamera.isSelected();
//        AVChatManager.getInstance().muteLocalVideo(!selected);
//        ivToggleCamera.setSelected(!selected);
//        ivSmallCover.setVisibility(!selected ? View.GONE : View.VISIBLE);

    }

    private void onIvSwitchCameraClicked() {
        CallHelper.getInstance().switchCamera();
    }
}
