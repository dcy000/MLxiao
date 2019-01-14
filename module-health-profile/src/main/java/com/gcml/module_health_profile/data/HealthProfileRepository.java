package com.gcml.module_health_profile.data;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_health_profile.bean.HealthProfileMenuBean;

import java.util.List;

import io.reactivex.Observable;

public class HealthProfileRepository {
    HealthProfileAPI healthProfile = RetrofitHelper.service(HealthProfileAPI.class);

    /**
     * 获取导航栏
     * @return
     */
    public Observable<List<HealthProfileMenuBean>> getMenu() {
        return healthProfile.getHealthProfileMenu().compose(RxUtils.apiResultTransformer());
    }
}
