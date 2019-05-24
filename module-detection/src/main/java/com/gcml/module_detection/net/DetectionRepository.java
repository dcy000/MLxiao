package com.gcml.module_detection.net;

import com.gcml.common.RetrofitHelper;

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
//    public static Observable<List<DetectionResult>> postMeasureData(ArrayList<DetectionData> datas) {
//        String userId = UserSpHelper.getUserId();
//        return detectionService.postMeasureData(userId, ChannelUtils.getChannelMeta(), datas).compose(RxUtils.apiResultTransformer());
//    }
}
