package com.gcml.module_blutooth_devices.base;

import com.gcml.common.recommend.bean.post.DetectionData;

import java.util.ArrayList;

public interface IUploadData {
    void onSuccess(ArrayList<DetectionData> data);

    void onError(ArrayList<DetectionData> data);
}