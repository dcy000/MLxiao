package com.gcml.module_hypertension_manager.net;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_hypertension_manager.bean.DailyRecommendIntake;
import com.gcml.module_hypertension_manager.bean.DiagnoseInfoBean;
import com.gcml.module_hypertension_manager.bean.FoodMateratilDetail;

import java.util.ArrayList;

import io.reactivex.Observable;

public class HyperRepository {
    private static HyperService hyperServer = RetrofitHelper.service(HyperService.class);
    public Observable<DiagnoseInfoBean.DataBean> getDiagnoseInfo(String userId){
        return hyperServer.getDiagnoseInfo(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<DiagnoseInfoBean.DataBean> getDiagnoseInfoNew(String userId){
        return hyperServer.getDiagnoseInfoNew(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> postOriginHypertension(String userId,String hypertensionPrimaryState){
        return hyperServer.postOriginHypertension(userId,hypertensionPrimaryState).compose(RxUtils.apiResultTransformer());
    }

    public Observable<DailyRecommendIntake> getDailyRecommendedIntake(String userId){
        return hyperServer.getDailyRecommendedIntake(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<ArrayList<FoodMateratilDetail>> getDailyFoodRecommendation(String userId){
        return hyperServer.getDailyFoodRecommendation(userId).compose(RxUtils.apiResultTransformer());
    }
}
