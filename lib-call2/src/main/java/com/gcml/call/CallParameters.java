package com.gcml.call;

import com.netease.nimlib.sdk.avchat.constant.AVChatAudioEffectMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.model.AVChatImageFormat;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;

public enum CallParameters {
    INSTANCE;

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
    public AVChatParameters provideParameter() {
        AVChatParameters parameters = new AVChatParameters();
        parameters.setBoolean(AVChatParameters.KEY_AUDIO_CALL_PROXIMITY, autoCallProximity);
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, videoCropRatio);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, videoAutoRotate);
        parameters.setBoolean(AVChatParameters.KEY_SERVER_AUDIO_RECORD, serverRecordAudio);
        parameters.setBoolean(AVChatParameters.KEY_SERVER_VIDEO_RECORD, serverRecordVideo);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_DEFAULT_FRONT_CAMERA, defaultFrontCamera);
        parameters.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, videoQuality);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_FPS_REPORTED, videoFpsReported);
        parameters.setInteger(AVChatParameters.KEY_DEVICE_DEFAULT_ROTATION, deviceDefaultRotation);
        parameters.setInteger(AVChatParameters.KEY_DEVICE_ROTATION_FIXED_OFFSET, deviceRotationOffset);

        if (videoMaxBitrate > 0) {
            parameters.setInteger(AVChatParameters.KEY_VIDEO_MAX_BITRATE, videoMaxBitrate * 1024);
        }
        switch (audioEffectAecMode) {
            case 0:
                parameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_ACOUSTIC_ECHO_CANCELER, AVChatAudioEffectMode.DISABLE);
                break;
            case 1:
                parameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_ACOUSTIC_ECHO_CANCELER, AVChatAudioEffectMode.SDK_BUILTIN);
                break;
            case 2:
                parameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_ACOUSTIC_ECHO_CANCELER, AVChatAudioEffectMode.PLATFORM_BUILTIN);
                break;
        }
        switch (audioEffectNsMode) {
            case 0:
                parameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_NOISE_SUPPRESSOR, AVChatAudioEffectMode.DISABLE);
                break;
            case 1:
                parameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_NOISE_SUPPRESSOR, AVChatAudioEffectMode.SDK_BUILTIN);
                break;
            case 2:
                parameters.setString(AVChatParameters.KEY_AUDIO_EFFECT_NOISE_SUPPRESSOR, AVChatAudioEffectMode.PLATFORM_BUILTIN);
                break;
        }
        switch (videoHwEncoderMode) {
            case 0:
                parameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_AUTO);
                break;
            case 1:
                parameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
                break;
            case 2:
                parameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_HARDWARE);
                break;
        }
        switch (videoHwDecoderMode) {
            case 0:
                parameters.setString(AVChatParameters.KEY_VIDEO_DECODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_AUTO);
                break;
            case 1:
                parameters.setString(AVChatParameters.KEY_VIDEO_DECODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
                break;
            case 2:
                parameters.setString(AVChatParameters.KEY_VIDEO_DECODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_HARDWARE);
                break;
        }
        parameters.setBoolean(AVChatParameters.KEY_AUDIO_HIGH_QUALITY, audioHighQuality);
        parameters.setBoolean(AVChatParameters.KEY_AUDIO_DTX_ENABLE, audioDtx);

        //观众角色,多人模式下使用. IM Demo没有多人通话, 全部设置为AVChatUserRole.NORMAL.
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);

        //采用I420图像格式
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_FILTER_FORMAT, AVChatImageFormat.I420);
        return parameters;
    }

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
