package com.gcml.module_detection.net;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_detection.bean.LatestDetecBean;
import com.gcml.common.data.PostDataCallBackBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 17:01
 * created by:gzq
 * description:TODO
 */
public class DetectionRepository {

    private static DetectionService detectionService = RetrofitHelper.service(DetectionService.class);

    /**
     * 新的上传数据的接口
     * 1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏,7:胆固醇，8：血尿酸，9：心电, 10：体重
     */
    public static Observable<List<PostDataCallBackBean>> postMeasureData(ArrayList<DetectionData> datas) {
        return detectionService.postMeasureData(UserSpHelper.getUserId(), datas).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<LatestDetecBean>> getLatestDetectionData() {
        return detectionService.getLatestDetectionData().compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取健康档案中应该测量的设备
     * @param rdRecordId
     * @return
     */
    public static Observable<List<String>> getDevices(String rdRecordId){
        return detectionService.getDevices(rdRecordId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 健康档案测量的数据上传
     * @param userRecordId
     * @return
     */
    public static Observable<Object> postHealthRecordMeasureData(String userRecordId,ArrayList<DetectionData> detectionDatas){
        return detectionService.postHealthRecordMeasureData(userRecordId,detectionDatas);
    }
}
