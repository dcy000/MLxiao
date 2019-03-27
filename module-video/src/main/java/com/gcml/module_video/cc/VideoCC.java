package com.gcml.module_video.cc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_video.measure.MeasureVideoPlayActivity;
import com.gcml.module_video.normal.NormalVideoPlayActivity;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/25 15:10
 * created by:gzq
 * description:视频模块港口
 */
public class VideoCC implements IComponent {
    interface ReceiveActionNames {
        /**
         * 跳转到MeasureVideoPlayActivity
         */
        String TO_MEASUREACTIVITY = "TO_MeasureVideoPlayActivity";
        /**
         * 跳转到NormalVideoPlayActivity
         */
        String TO_NORMALVIDEOPLAYACTIVITY = "To_NormalVideoPlayActivity";
    }

    interface ReceiveKeys {
        /**
         * 播放链接的key
         */
        String KEY_EXTRA_URI = "key_uri";
        /**
         * 播放视频路径的key
         */
        String KEY_EXTRA_URL = "key_url";
        /**
         * 播放视频标题的key
         */
        String KEY_EXTRA_TITLE = "key_title";
    }

    @Override
    public String getName() {
        return "video";
    }

    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        String actionName = cc.getActionName();
        Uri uri = cc.getParamItem(ReceiveKeys.KEY_EXTRA_URI);
        String url = cc.getParamItem(ReceiveKeys.KEY_EXTRA_URL);
        String title = cc.getParamItem(ReceiveKeys.KEY_EXTRA_TITLE);
        switch (actionName) {
            case ReceiveActionNames.TO_MEASUREACTIVITY:
                //要求异步启动
//                MeasureVideoPlayActivity.startActivity(context, uri, url, title);
                CCResultActions.onCCResultAction(MeasureVideoPlayActivity.SendResultActionNames.PRESSED_BUTTON_SKIP);
                return true;
            case ReceiveActionNames.TO_NORMALVIDEOPLAYACTIVITY:
                NormalVideoPlayActivity.startActivity(context, uri, url, title);
                return true;
            default:
                break;
        }
        return false;
    }
}
