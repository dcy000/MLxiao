package com.gcml.module_health_profile.data;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserSpHelper;
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
    //TODO:目前直接使用的是医疗演示版的功能，后期如果需要做这个模块，还需要再梳理
    String userId = UserSpHelper.getUserId();

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
     * @return
     */
    public Observable<List<HealthRecordBean>> getHealthRecordList(String recordId) {
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

    public Observable<List<GuardianInfo>> getGuardians() {
        return healthProfile.getGuardians(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<WarnBean>> getWannings() {
        return healthProfile.getWanings(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<TiZhiBean>> getConstitution() {
        return healthProfile.getConstitution(userId).compose(RxUtils.apiResultTransformer());
    }

}
