package com.witspring.unitdisease.contract;

import com.witspring.base.BasePresenter;
import com.witspring.base.BaseView;
import com.witspring.model.ApiCallback;
import com.witspring.model.entity.Disease;
import com.witspring.model.entity.Result;
import com.witspring.unitdisease.model.DiseaseApi;

/**
 * @author Created by wu_zf on 1/2/2018.
 * @email :wuzf1234@gmail.com
 */

public interface DiseaseContract {

    interface View extends BaseView {
        void showDiseaseInfo(Disease disease);
        void showNoData();
    }

    class Presenter extends BasePresenter {
        private View mView;
        private DiseaseApi diseaseApi;

        public Presenter(View mView) {
            this.mView = mView;
            diseaseApi = new DiseaseApi();
        }

        public void getDiseaseInfoByName(String diseaseName, String word, int sex, int ageMonth) {
//            mView.startLoading();
            diseaseApi.getDiseaseInfoByName(diseaseName,word,sex,ageMonth, new ApiCallback<Disease>(){
                @Override
                public void onResult(Result<Disease> result) {
//                    mView.stopLoading();
                    if (result.successed() && result.getContent() != null) {
                        mView.showDiseaseInfo(result.getContent());
                    } else {
                        mView.showNoData();
                    }
                }
            });
        }
    }
}
