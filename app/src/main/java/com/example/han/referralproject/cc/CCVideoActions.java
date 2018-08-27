package com.example.han.referralproject.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/25 18:35
 * created by:gzq
 * description:TODO
 */
public class CCVideoActions {
    public static final String MODULE_NAME = "video";

    public interface SendActionNames {
        /**
         * 跳转到MeasureVideoPlayActivity
         */
        String TO_MEASUREACTIVITY = "TO_MeasureVideoPlayActivity";
        /**
         * 跳转到NormalVideoPlayActivity
         */
        String TO_NORMALVIDEOPLAYACTIVITY="To_NormalVideoPlayActivity";
    }

    public interface SendKeys {
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

    public interface ReceiveResultActionNames {
        /**
         * 点击了返回按钮
         */
        String PRESSED_BUTTON_BACK = "pressed_button_back";
        /**
         * 点击了跳过按钮
         */
        String PRESSED_BUTTON_SKIP = "pressed_button_skip";
        /**
         * 视频播放结束
         */
        String VIDEO_PLAY_END = "video_play_end";
    }

    public interface ReceiveResultKeys {
        /**
         * CC结果返回key
         */
        String KEY_EXTRA_CC_CALLBACK = "key_cc_callback";
    }
    public static void jump2NormalVideoPlayActivity(String url,String title){
        CC.obtainBuilder(CCVideoActions.MODULE_NAME)
                .setActionName(CCVideoActions.SendActionNames.TO_NORMALVIDEOPLAYACTIVITY)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URI, null)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URL, url)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_TITLE, title)
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String resultAction = result.getDataItem(CCVideoActions.ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                switch (resultAction) {
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        break;
                    default:
                }
            }
        });
    }
}
