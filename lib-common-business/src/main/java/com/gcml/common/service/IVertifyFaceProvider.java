package com.gcml.common.service;

import com.gcml.common.face2.VertifyFace2ProviderImp;

public interface IVertifyFaceProvider {
    void checkUserEntityAndVertifyFace(boolean isShowSkipButton, boolean isVertify, boolean isHidden, VertifyFace2ProviderImp.VertifyFaceResult result);
    void onlyVertifyFace(boolean isShowSkipButton, boolean isVertify, boolean isHidden, VertifyFace2ProviderImp.VertifyFaceResult result);
}
