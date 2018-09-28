package com.example.han.referralproject.qianyue;

import com.example.han.referralproject.qianyue.bean.DoctorInfoBean;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;

import io.reactivex.Observable;

/**
 * Created by lenovo on 2018/9/28.
 */

public class QianYueRepository {
    private IRepositoryHelper repositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();
    QianYueService qianYueService = repositoryHelper.retrofitService(QianYueService.class);

    public Observable<DoctorInfoBean> getDoctorInfo(String doctorId) {
        return qianYueService.getDoctInfo(doctorId).compose(RxUtils.apiResultTransformer());
    }
}
