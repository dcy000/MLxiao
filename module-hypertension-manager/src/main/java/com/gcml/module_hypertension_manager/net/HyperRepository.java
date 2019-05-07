package com.gcml.module_hypertension_manager.net;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_hypertension_manager.bean.DailyRecommendIntake;
import com.gcml.module_hypertension_manager.bean.DiagnoseInfoBean;
import com.gcml.module_hypertension_manager.bean.FoodMateratilDetail;
import com.gcml.module_hypertension_manager.bean.MedicineBean;
import com.gcml.module_hypertension_manager.bean.PrimaryHypertensionBean;
import com.gcml.module_hypertension_manager.bean.PrimaryHypertensionQuestionnaireBean;
import com.gcml.module_hypertension_manager.bean.SportPlan;
import com.gcml.module_hypertension_manager.bean.ThisWeekHealthPlan;
import com.gcml.module_hypertension_manager.bean.WeekDietPlan;
import com.gcml.module_hypertension_manager.zhongyi.bean.HealthManagementAnwserBean;
import com.gcml.module_hypertension_manager.zhongyi.bean.HealthManagementResultBean;
import com.gcml.module_hypertension_manager.zhongyi.bean.OlderHealthManagementBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class HyperRepository {
    private static HyperService hyperServer = RetrofitHelper.service(HyperService.class);

    public Observable<DiagnoseInfoBean.DataBean> getDiagnoseInfo(String userId) {
        return hyperServer.getDiagnoseInfo(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<DiagnoseInfoBean.DataBean> getDiagnoseInfoNew(String userId) {
        return hyperServer.getDiagnoseInfoNew(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> postOriginHypertension(String userId, String hypertensionPrimaryState) {
        return hyperServer.postOriginHypertension(userId, hypertensionPrimaryState).compose(RxUtils.apiResultTransformer());
    }

    public Observable<DailyRecommendIntake> getDailyRecommendedIntake(String userId) {
        return hyperServer.getDailyRecommendedIntake(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<ArrayList<FoodMateratilDetail>> getDailyFoodRecommendation(String userId) {
        return hyperServer.getDailyFoodRecommendation(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<MedicineBean> getMedicineProgram(String userId) {
        return hyperServer.getMedicineProgram(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<SportPlan> getSportHealthPlan(String userId) {
        return hyperServer.getSportHealthPlan(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<ThisWeekHealthPlan> getThisWeekPlan(String userId) {
        return hyperServer.getThisWeekPlan(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<WeekDietPlan> getWeekDietPlan(String userId) {
        return hyperServer.getWeekDietPlan(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<PrimaryHypertensionQuestionnaireBean.DataBean> getNormalHightQuestion() {
        return hyperServer.getNormalHightQuestion().compose(RxUtils.apiResultTransformer());
    }

    public Observable<String> postNormalHightQuestion(String userId, PrimaryHypertensionBean bean) {
        return hyperServer.postNormalHightQuestion(userId, bean).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> postTargetHypertension(String userId, String state) {
        return hyperServer.postTargetHypertension(userId, state).compose(RxUtils.apiResultTransformer());
    }

    public Observable<PrimaryHypertensionQuestionnaireBean.DataBean> getHypertensionQuestion() {
        return hyperServer.getHypertensionQuestion().compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> postHypertensionQuestion(String userId, PrimaryHypertensionBean bean) {
        return hyperServer.postHypertensionQuestion(userId, bean).compose(RxUtils.apiResultTransformer());
    }

    public Observable<PrimaryHypertensionQuestionnaireBean.DataBean> getPrimaryHypertensionQuestion() {
        return hyperServer.getPrimaryHypertensionQuestion().compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> postPrimaryHypertensionQuestion(String userId, PrimaryHypertensionBean bean) {
        return hyperServer.postPrimaryHypertensionQuestion(userId, bean).compose(RxUtils.apiResultTransformer());
    }

    public Observable<OlderHealthManagementBean.DataBean> getHealthManagementForOlder() {
        return hyperServer.getHealthManagementForOlder().compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<HealthManagementResultBean.DataBean>> postHealthManagementAnwser(HealthManagementAnwserBean bean) {
        return hyperServer.postHealthManagementAnwser(bean).compose(RxUtils.apiResultTransformer());
    }
}
