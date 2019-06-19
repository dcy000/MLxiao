package com.gcml.call;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.gcml.common.utils.display.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.BroadcastMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

import timber.log.Timber;

/**
 * Created by lenovo on 2017/10/19.
 */

public class CallInitHelper {

    private static final String TAG = "CallHelper";

    private Context context;

    public Context getContext() {
        return context;
    }

    private CallInitHelper() {
    }

    public static CallInitHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final CallInitHelper INSTANCE = new CallInitHelper();
    }

    public void init(Context context, boolean register) {
        this.context = context.getApplicationContext();
        NIMClient.init(context, CallAccountHelper.INSTANCE.loginInfo(), options());
        if (NIMUtil.isMainProcess(context)) {
            // 1、UI相关初始化操作
            // 2、相关Service调用
            registerImMessageFilter();
            // 注册全局云信sdk 观察者
            registerNimGlobalObserver(register);
//            doctor_18940866148
//            br_12345678912
//            br_12345678913
//            br_12345678914
//            NimCallActivity.outgoingCall(this, "br_12345678912");

            NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
                @Override
                public void onEvent(StatusCode code) {
                    String name = code.name();
                    Log.w("AccountStatus", name);
//                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                    if (code.wontAutoLogin()) {
                        kickOut(code);
                    } else {
                        if (code == StatusCode.NET_BROKEN) {

                        } else if (code == StatusCode.UNLOGIN) {

                        } else if (code == StatusCode.CONNECTING) {

                        } else if (code == StatusCode.LOGINING) {

                        } else {

                        }
                    }
                }
            }, register);
        }
    }

    private void kickOut(StatusCode code) {
        CallAccountHelper.INSTANCE.setToken("");

        if (code == StatusCode.PWD_ERROR) {
            ToastUtils.showShort("请重新登录");
        } else {
            ToastUtils.showShort("你的账号在其他终端登录，请确认是否是本人操作");
        }
        // tologin
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
                Timber.i("onEvent IncomingCall: data=%s", data);
                if (CallPhoneStateObserver.INSTANCE.getCallState() != CallPhoneStateObserver.PhoneState.IDLE
                        || CallHelper.INSTANCE.isChatting()
                        || AVChatManager.getInstance().getCurrentChatId() != 0) {
                    Timber.i("reject incoming call data = %s as local phone is not idle", data);
                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
                    return;
                }
                // 有来电
                CallHelper.INSTANCE.setChatting(true);

                CallHelper.INSTANCE.dispatchIncomingCallFromBroadCast(context, data);
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
                ToastUtils.showShort(broadcastMessage.getContent());
            }
        }, register);
    }

    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
//        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
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
            public String getDisplayNameForMessageNotifier(
                    String account,
                    String sessionId,
                    SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {
//                R.drawable.call_user_placeholder
                return null;
            }
        };
        return options;
    }

    public static boolean inMainProcess(Context context) {
        if (context == null) {
            return false;
        }
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }

    public static String getProcessName(Context context) {
        String processName = null;
        ActivityManager manager = ((ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
