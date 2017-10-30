package com.medlink.danbogh.call2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.medlink.danbogh.utils.Utils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.BroadcastMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by lenovo on 2017/10/19.
 */

public class NimInitHelper {

    private static final String TAG = "NimInitHelper";

    private Context context;

    private NimInitHelper() {
    }

    public static NimInitHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final NimInitHelper INSTANCE = new NimInitHelper();
    }

    public void init(Context context, boolean register) {
        this.context = context.getApplicationContext();
        NIMClient.init(context, NimAccountHelper.getInstance().loginInfo(), options());
        if (Utils.inMainProcess(context)) {
            // 1、UI相关初始化操作
            // 2、相关Service调用
            registerImMessageFilter();
            // 注册全局云信sdk 观察者
            registerNimGlobalObserver(register);
//            doctor_18940866148
//            br_12345678912
//            NimCallActivity.launch(this, "br_12345678912");
            NimAccountHelper.getInstance().login("br_12345678912", "123456",null);
        }
    }

    private void registerImMessageFilter() {

    }

    private void registerNimGlobalObserver(boolean register) {
        // 注册网络通话来电
        registerAVChatIncomingCallObserver(register);

        // 注册云信全员广播
        registerNimBroadcastMessage(register);
    }

    /**
     * 注册音视频来电观察者
     *
     * @param register
     */
    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                String extra = data.getExtra();
                Log.e("Extra", "Extra Message->" + extra);
                if (PhoneStateObserver.getInstance().getCallState() != PhoneStateObserver.PhoneState.IDLE
                        || NimCallHelper.getInstance().isChatting()
                        || AVChatManager.getInstance().getCurrentChatId() != 0) {
                    Log.i(TAG, "reject incoming call data =" + data.toString() + " as local phone is not idle");
                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
                    return;
                }
                // 有来电
                NimCallHelper.getInstance().setChatting(true);
                NimCallHelper.getInstance().dispatchIncomingCallFromBroadCast(context,data);
            }
        }, register);
    }

    /**
     * 注册云信全服广播接收器
     *
     * @param register
     */
    private void registerNimBroadcastMessage(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeBroadcastMessage(new Observer<BroadcastMessage>() {
            @Override
            public void onEvent(BroadcastMessage broadcastMessage) {
                Toast.makeText(context, broadcastMessage.getContent(), Toast.LENGTH_SHORT).show();
            }
        }, register);
    }

    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.example.han.referralproject/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 1080 / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.person_image;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(
                    String account,
                    String sessionId,
                    SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }
}
