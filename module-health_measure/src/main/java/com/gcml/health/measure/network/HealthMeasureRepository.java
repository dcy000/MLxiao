package com.gcml.health.measure.network;

import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.repository.http.ApiResult;
import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryPostBean;

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
}
