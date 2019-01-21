package com.gcml.module_health_profile.data;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_health_profile.bean.HealthProfileMenuBean;
import com.gcml.module_health_profile.bean.HealthRecordBean;

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
}
