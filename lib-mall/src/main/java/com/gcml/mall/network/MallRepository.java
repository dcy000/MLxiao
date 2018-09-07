package com.gcml.mall.network;

import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;

import io.reactivex.Observable;

public class MallRepository {
    private IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();

    private MallService mTaskService = mRepositoryHelper.retrofitService(MallService.class);

    public Observable<String> rechargeForApi(String eqid, String bba, String time, String bid) {
        return mTaskService.recharge(eqid, bba, time, bid).compose(RxUtils.apiResultTransformer());
    }
}
