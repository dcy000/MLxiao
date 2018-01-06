package com.witspring.unitbody.contract;

import com.witspring.base.BasePresenter;
import com.witspring.base.BaseView;
import com.witspring.model.ApiCallback;
import com.witspring.model.Constants;
import com.witspring.model.entity.Result;
import com.witspring.unitbody.model.BodyApi;

/**
 * 人体图Contract
 * @author Created by Goven on 17/2/20 上午11:44
 * @email gxl3999@gmail.com
 */
public interface BodyContract {

    interface View extends BaseView {
        void setBodyPartIds(Integer[] ids);
        void setCommSymptoms(String[] symptoms);
    }

    class Presenter extends BasePresenter {

        private View mView;
        private BodyApi bodyApi;

        public Presenter(View mView) {
            this.mView = mView;
            bodyApi = new BodyApi();
        }

        public void topLevelOrgans(boolean isMan) {
            mView.startLoading();
            bodyApi.topLevelOrgans(isMan ? Constants.GENDER_MAN : Constants.GENDER_WOMEN, new ApiCallback<Integer[]>() {
                @Override
                public void onResult(Result<Integer[]> result) {
                    mView.stopLoading();
                    if (result.successed()) {
                        mView.setBodyPartIds(result.getContent());
                    } else {
                        mView.warningUnknow(result);
                    }
                }
            });
        }

        public void commSymptoms(int sex, int ageMonths) {
            mView.startLoading();
            bodyApi.commonSymptoms(sex, ageMonths, new ApiCallback<String[]>() {
                @Override
                public void onResult(Result<String[]> result) {
                    mView.stopLoading();
                    if (result.successed()) {
                        mView.setCommSymptoms(result.getContent());
                    } else {
                        mView.warningUnknow(result);
                    }
                }
            });
        }

    }

}
