package com.gcml.module_health_profile.data;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_health_profile.bean.GuardianInfo;
import com.gcml.module_health_profile.bean.HealthProfileMenuBean;
import com.gcml.module_health_profile.bean.HealthRecordBean;
import com.gcml.module_health_profile.bean.OutputMeasureBean;
import com.gcml.module_health_profile.bean.TiZhiBean;
import com.gcml.module_health_profile.bean.WarnBean;

import java.util.List;

import io.reactivex.Observable;

public class HealthProfileRepository {
    HealthProfileAPI healthProfile = RetrofitHelper.service(HealthProfileAPI.class);

    /**
     * 获取导航栏
     *
     * @return
     */
    public Observable<List<HealthProfileMenuBean>> getMenu() {
        return healthProfile.getHealthProfileMenu().compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取记录列表
     *
     * @param recordId
     * @param userId
     * @return
     */
    public Observable<List<HealthRecordBean>> getHealthRecordList(String recordId, String userId) {
        return healthProfile.getHealthRecordList(recordId, userId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 查询医生的信息
     *
     * @param doctorId
     * @return
     */
    public Observable<Doctor> getDoctorInfo(String doctorId) {
        return healthProfile.queryDoctorInfo(doctorId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 查询需要打印的健康数据
     *
     * @param rdRecordId
     * @param userHealthRecordId
     * @return
     */
    public Observable<List<OutputMeasureBean>> getHealthRecordMeasureResult(String rdRecordId, String userHealthRecordId) {
        return healthProfile.getHealthRecordMeasureResult(rdRecordId, userHealthRecordId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<GuardianInfo>> getGuardians(String userId) {
        return healthProfile.getGuardians(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<WarnBean>> getWannings(String userId) {
        return healthProfile.getWanings(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<TiZhiBean>> getConstitution(String userId) {
        return healthProfile.getConstitution(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> chat(String appId, String currentTime, String param, String token) {
        return healthProfile.chat(appId, currentTime, param, token).compose(RxUtils.apiResultTransformer());
    }
}
