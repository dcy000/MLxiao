package com.gcml.common.recommend.network;

import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.bean.post.DetectionBean;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lenovo on 2018/9/21.
 */

public class RecommendRepository {
    private IRepositoryHelper repositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();
    RecommendService recommendService = repositoryHelper.retrofitService(RecommendService.class);


    public Observable<List<GoodBean>> searchGoodsByName(String goodName) {
        return recommendService.searchGoodsByName(goodName)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<GoodBean>> recommendGoodsByUser(String userId) {
        return recommendService.recommendGoodsByUser(userId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<GoodBean>> recommendGoodsByDetection(List<DetectionData> detectionBeans, String userId) {
        return recommendService.recommendGoodsByDetection(detectionBeans, userId)
                .compose(RxUtils.apiResultTransformer());
    }


}
