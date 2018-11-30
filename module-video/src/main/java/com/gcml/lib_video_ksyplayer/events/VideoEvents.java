package com.gcml.lib_video_ksyplayer.events;

import android.net.Uri;

import com.gcml.lib_video_ksyplayer.MeasureVideoPlayActivity;
import com.gcml.lib_video_ksyplayer.R;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.ToastUtils;

public class VideoEvents implements IEvents {
    @Override
    public void onEvent(String tag, Object... objects) {
        Uri uri;
        switch (tag) {
            case "Video>Temperature":
                //体温计演示视频
                uri = Uri.parse("android.resource://" + Box.getApp().getPackageName() + "/" + R.raw.tips_wendu);
                MeasureVideoPlayActivity.startActivity(ActivityUtils.currentActivity(), uri, null, "耳温测量演示视频");
                break;
            case "Video>BloodPressure":
                //血压演示视频
                uri = Uri.parse("android.resource://" + Box.getApp().getPackageName() + "/" + R.raw.tips_xueya);
                MeasureVideoPlayActivity.startActivity(ActivityUtils.currentActivity(), uri, null, "血压测量演示视频");
                break;
            case "Video>Weight":
                ToastUtils.showShort("暂无演示视频");
                break;
            case "Video>ThreeInOne":
                uri = Uri.parse("android.resource://" + Box.getApp().getPackageName() + "/" + R.raw.tips_sanheyi);
                MeasureVideoPlayActivity.startActivity(ActivityUtils.currentActivity(), uri, null, "三合一测量演示视频");
                break;
            case "Video>Ecg":
                uri = Uri.parse("android.resource://" + Box.getApp().getPackageName() + "/" + R.raw.tips_xindian);
                MeasureVideoPlayActivity.startActivity(ActivityUtils.currentActivity(), uri, null, "心电测量演示视频");
                break;
            case "Video>BloodSugar":
                uri = Uri.parse("android.resource://" + Box.getApp().getPackageName() + "/" + R.raw.tips_xuetang);
                MeasureVideoPlayActivity.startActivity(ActivityUtils.currentActivity(), uri, null, "血糖测量演示视频");
                break;
            case "Video>Bloodoxygen":
                uri = Uri.parse("android.resource://" + Box.getApp().getPackageName() + "/" + R.raw.tips_xueyang);
                MeasureVideoPlayActivity.startActivity(ActivityUtils.currentActivity(), uri, null, "血氧测量演示视频");
                break;
        }
    }
}
