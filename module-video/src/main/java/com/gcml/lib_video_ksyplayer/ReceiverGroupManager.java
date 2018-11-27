package com.gcml.lib_video_ksyplayer;

import android.content.Context;

import com.gcml.lib_video_ksyplayer.default_cover.CompleteCover;
import com.gcml.lib_video_ksyplayer.default_cover.ControllerCover;
import com.gcml.lib_video_ksyplayer.default_cover.ErrorCover;
import com.gcml.lib_video_ksyplayer.default_cover.GestureCover;
import com.gcml.lib_video_ksyplayer.default_cover.LoadingCover;
import com.kk.taurus.playerbase.receiver.GroupValue;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;

import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_COMPLETE_COVER;
import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_CONTROLLER_COVER;
import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_ERROR_COVER;
import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_GESTURE_COVER;
import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_LOADING_COVER;


/**
 * Created by Taurus on 2018/4/18.
 */

public class ReceiverGroupManager {

    private static ReceiverGroupManager i;

    private ReceiverGroupManager() {
    }

    public static ReceiverGroupManager get() {
        if (null == i) {
            synchronized (ReceiverGroupManager.class) {
                if (null == i) {
                    i = new ReceiverGroupManager();
                }
            }
        }
        return i;
    }

    /**
     * 测量演示视频
     *
     * @param context
     * @param groupValue
     * @return
     */
    public ReceiverGroup getMeasureVideoReceiverGroup(Context context, GroupValue groupValue) {
        ReceiverGroup receiverGroup = new ReceiverGroup(groupValue);
        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context,true));
        receiverGroup.addReceiver(KEY_GESTURE_COVER, new GestureCover(context));
//        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context,true));
        return receiverGroup;
    }

    /**
     * 普通视频播放
     *
     * @param context
     * @param groupValue
     * @return
     */
    public ReceiverGroup getNormalVideoReceiverGroup(Context context, GroupValue groupValue) {
        ReceiverGroup receiverGroup = new ReceiverGroup(groupValue);
        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context,false));
        receiverGroup.addReceiver(KEY_GESTURE_COVER, new GestureCover(context));
        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context,false));
        return receiverGroup;
    }
}
