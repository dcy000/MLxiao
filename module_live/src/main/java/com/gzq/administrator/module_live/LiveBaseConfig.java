package com.gzq.administrator.module_live;

import java.io.Serializable;

/**
 * Created by gzq on 2018/4/10.
 */

public class LiveBaseConfig implements Serializable{
    public String mUrl;
    public int mCameraFacing;
    public float mFrameRate;
    public int mVideoKBitrate;
    public int mAudioKBitrate;
    public int mTargetResolution;
    public int mOrientation;
    public int mEncodeMethod;
    public boolean mAutoStart;
    public boolean mShowDebugInfo;
}
