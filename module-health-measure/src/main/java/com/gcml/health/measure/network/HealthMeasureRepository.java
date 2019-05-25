package com.gcml.health.measure.network;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.RxCacheHelper;
import com.gcml.common.data.UserSpHelper;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.recommend.bean.post.DetectionDataProvider;
import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.first_diagnosis.bean.DeviceBean;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportReceiveBean;
import com.gcml.health.measure.first_diagnosis.bean.PostDeviceBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryPostBean;
import com.gcml.health.measure.single_measure.bean.NewWeeklyOrMonthlyBean;
import com.gcml.health.measure.utils.ChannelUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 17:01
 * created by:gzq
 * description:TODO
 */
public class HealthMeasureRepository {

    private static HealthMeasureServer healthMeasureServer = RetrofitHelper.service(HealthMeasureServer.class);
    private static DetectionDataProvider detectionDataProvider = RxCacheHelper.provider(DetectionDataProvider.class);

    /**
     * 获取健康调查的题目
     *
     * @return
     */
    public static Observable<HealthInquiryBean> getHealthInquiryQuestions() {
        return healthMeasureServer.getHealthInquiryQuestions().compose(RxUtils.apiResultTransformer());
    }

    /**
     * 上传健康调查的结果
     *
     * @param userId
     * @param bean
     * @return
     */
    public static Observable<Object> postHealthInquiryAnswers(String userId, HealthInquiryPostBean bean) {
        return healthMeasureServer.postHealthInquiryAnswers(userId, bean).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取用户有的设备
     *
     * @param userId
     * @return
     */
    public static Observable<List<DeviceBean>> getUserHasedDevices(String userId) {
        return healthMeasureServer.getUserHasedDevices(userId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 上传该用户已经拥有的设备
     *
     * @param userId
     * @return
     */
    public static Observable<Object> postUserHasedDevices(String userId, List<PostDeviceBean> postDeviceBeans) {
        return healthMeasureServer.postUserHasedDevices(userId, postDeviceBeans).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取首诊问卷报告
     */
    public static Observable<FirstReportReceiveBean> getFirstReport(String userId) {
        return healthMeasureServer.getFirstReport(userId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 判断数据是否是异常数据
     *
     * @param userId
     * @param datas
     * @return
     */
    public static Observable<Object> checkIsNormalData(String userId, ArrayList<DetectionData> datas) {
        return healthMeasureServer.checkIsNormalData(userId, datas).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 新的上传数据的接口
     * 1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏,7:胆固醇，8：血尿酸，9：心电, 10：体重
     */
    public static Observable<List<DetectionResult>> postMeasureData(ArrayList<DetectionData> datas) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();

        Timber.i("上传测量数据： userID=" + userId + " noNetwork = " + noNetwork);

        if (!noNetwork) {
            //hasNetwork
            return healthMeasureServer.postMeasureData(userId, ChannelUtils.getChannelMeta(), datas).compose(RxUtils.apiResultTransformer());
        }

        // noNetwork
        Observable<List<DetectionData>> rxDetectionDataLocal =
                detectionDataProvider.detectionDataLocal(
                        Observable.empty(),
                        new DynamicKey(userId),
                        new EvictDynamicKey(false))
                        .toObservable()
                        .onErrorResumeNext(Observable.just(Collections.emptyList()));
        return rxDetectionDataLocal.map(new Function<List<DetectionData>, List<DetectionData>>() {
            @Override
            public List<DetectionData> apply(List<DetectionData> oldData) throws Exception {
                ArrayList<DetectionData> newData = new ArrayList<>();
                for (DetectionData old : oldData) {
                    if (old != null) {
                        newData.add(old);
                    }
                }
                for (DetectionData added : datas) {
                    if (added != null) {
                        newData.add(added);
                    }
                }
                return newData;
            }
        }).compose(new ObservableTransformer<List<DetectionData>, List<DetectionResult>>() {
            @Override
            public ObservableSource<List<DetectionResult>> apply(Observable<List<DetectionData>> upstream) {
                return detectionDataProvider.detectionDataLocal(upstream, new DynamicKey(userId), new EvictDynamicKey(true))
                        .toObservable()
                        .onErrorResumeNext(Observable.just(Collections.emptyList()))
                        .map(new Function<List<DetectionData>, List<DetectionResult>>() {
                            @Override
                            public List<DetectionResult> apply(List<DetectionData> detectionData) throws Exception {
                                return Collections.emptyList();
                            }
                        });
            }
        });
    }

    /**
     * 上传惯用手
     *
     * @param hand
     * @return
     */
    public static Observable<Object> postHypertensionHand(int hand) {
        return healthMeasureServer.postHypertensionHand(UserSpHelper.getUserId(), hand).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取周报告或者月报告
     *
     * @param endTimeStamp
     * @param page
     * @return
     */
    public static Observable<NewWeeklyOrMonthlyBean> getWeeklyOrMonthlyReport(long endTimeStamp, String page) {
        return healthMeasureServer.getWeeklyOrMonthlyReport(UserSpHelper.getUserId(), endTimeStamp, page).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 取消套餐
     *
     * @param setmealId
     * @return
     */
    public static Observable<Object> cancelServicePackage(String setmealId) {
        return healthMeasureServer.cancelServicePackage(setmealId);
    }


    /**
     * 获取健康档案中应该测量的设备
     * @param rdRecordId
     * @return
     */
    public static Observable<List<String>> getDevices(String rdRecordId){
        return healthMeasureServer.getDevices(rdRecordId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 健康档案测量的数据上传
     * @param userRecordId
     * @return
     */
    public static Observable<Object> postHealthRecordMeasureData(String userRecordId,ArrayList<DetectionData> detectionDatas){
        return healthMeasureServer.postHealthRecordMeasureData(userRecordId,detectionDatas);
    }
}
