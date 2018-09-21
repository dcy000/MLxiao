package com.gcml.health.measure.network;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DeviceBean;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportReceiveBean;
import com.gcml.health.measure.first_diagnosis.bean.PostDeviceBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryPostBean;

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
public class HealthMeasureRepository {
    private static IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();
    private static HealthMeasureServer healthMeasureServer = mRepositoryHelper.retrofitService(HealthMeasureServer.class);

    /**
     * 获取健康调查的题目
     * @return
     */
    public static Observable<HealthInquiryBean> getHealthInquiryQuestions(){
       return healthMeasureServer.getHealthInquiryQuestions().compose(RxUtils.apiResultTransformer());
    }

    /**
     * 上传健康调查的结果
     * @param userId
     * @param bean
     * @return
     */
    public static Observable<Object> postHealthInquiryAnswers(String userId, HealthInquiryPostBean bean){
        return healthMeasureServer.postHealthInquiryAnswers(userId,bean).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取用户有的设备
     * @param userId
     * @return
     */
    public static Observable<List<DeviceBean>>  getUserHasedDevices(String userId){
        return healthMeasureServer.getUserHasedDevices(userId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 上传该用户已经拥有的设备
     * @param userId
     * @return
     */
    public static Observable<Object> postUserHasedDevices(String userId, List<PostDeviceBean> postDeviceBeans){
        return healthMeasureServer.postUserHasedDevices(userId,postDeviceBeans).compose(RxUtils.apiResultTransformer());
    }
    /**
     * 获取首诊问卷报告
     */
    public static Observable<FirstReportReceiveBean> getFirstReport(String userId){
        return healthMeasureServer.getFirstReport(userId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 判断数据是否是异常数据
     * @param userId
     * @param datas
     * @return
     */
    public static Observable<Object> checkIsNormalData(String userId, ArrayList<DetectionData> datas){
        return healthMeasureServer.checkIsNormalData(userId,datas).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 新的上传数据的接口
     */
    public static Observable<Object> postMeasureData(ArrayList<DetectionData> datas){
        return healthMeasureServer.postMeasureData(UserSpHelper.getUserId(),datas).compose(RxUtils.apiResultTransformer());
    }
}
