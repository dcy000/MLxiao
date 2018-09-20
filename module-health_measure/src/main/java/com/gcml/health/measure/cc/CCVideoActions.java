package com.gcml.health.measure.cc;


/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/25 15:53
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


}
