package com.medlink.danbogh.call2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.medlink.danbogh.utils.Utils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by lenovo on 2017/10/19.
 */

public class NimInitHelper {

    private Context context;

    private NimInitHelper() {}

    public static NimInitHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final NimInitHelper INSTANCE = new NimInitHelper();
    }

    public void init(Context context, boolean register) {
        context = context.getApplicationContext();
        NIMClient.init(context, NimAccountHelper.getInstance().loginInfo(), options());
        if (Utils.inMainProcess(context)) {
            // 1、UI相关初始化操作
            // 2、相关Service调用
        }
        registerImMessageFilter();
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
        options.thumbnailSize = 1200 / 2;

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

    private void registerImMessageFilter() {

    }
}
