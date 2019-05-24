package com.gcml.common.service;

import com.gcml.common.face.VertifyFaceProviderImp;

public interface IVertifyFaceProvider {
    void checkUserEntityAndVertifyFace(boolean isShowSkipButton, boolean isVertify, boolean isHidden, VertifyFaceProviderImp.VertifyFaceResult result);
    void onlyVertifyFace(boolean isShowSkipButton, boolean isVertify, boolean isHidden, VertifyFaceProviderImp.VertifyFaceResult result);
}
