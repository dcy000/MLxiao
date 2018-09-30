package com.example.han.referralproject.market.network;

import com.example.han.referralproject.market.network.bean.GoodsTypeBean;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lenovo on 2018/9/29.
 */

public class GoodsRepository {
    IRepositoryHelper repositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();
    GoodsService goodsService = repositoryHelper.retrofitService(GoodsService.class);

    public Observable<List<GoodsTypeBean>> getGoodsTypes() {
        return goodsService.getGoodsTypes().compose(RxUtils.apiResultTransformer());
    }
}
