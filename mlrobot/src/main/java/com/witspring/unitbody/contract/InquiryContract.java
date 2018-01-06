package com.witspring.unitbody.contract;

import com.witspring.base.BasePresenter;
import com.witspring.base.BaseView;
import com.witspring.model.ApiCallback;
import com.witspring.model.entity.Result;
import com.witspring.unitbody.model.InquiryApi;

/**
 * 对话式Contract
 * @author Created by Goven on 17/2/20 上午11:44
 * @email gxl3999@gmail.com
 */
public interface InquiryContract {

    interface View extends BaseView {
        void chatInit(String chatContent);
        void chatNextStep(String chatNextStepContent);
        void chatResultBackNow(String chatResultBackNowContent);
    }

    class Presenter extends BasePresenter {

        private View mView;
        private InquiryApi inquiryApi;

        public Presenter(View mView) {
            this.mView = mView;
            inquiryApi = new InquiryApi();
        }

        public void chatInit(String status, int sex,int ageMonth, String phrase) {
            mView.startLoading();
            inquiryApi.chatInit(status, sex,ageMonth,phrase, new ApiCallback<String>() {
                @Override
                public void onResult(Result<String> result) {
                    mView.stopLoading();
                    //这里的statue很多情况，不采用200作为唯一标识
                    mView.chatInit(result.getContent());
                }
            });
        }

        public void chatNextStep(String status,String sessionId,String location,String answer) {
            mView.startLoading();
            inquiryApi.chatNextStep(status, sessionId,location,answer, new ApiCallback<String>() {
                @Override
                public void onResult(Result<String> result) {
                    mView.stopLoading();
                    //这里的statue很多情况，不采用200作为唯一标识
                    mView.chatNextStep(result.getContent());
                }
            });
        }

        public void chatResultBackNow(String status,String sessionId) {
            mView.startLoading();
            inquiryApi.chatResultBackNow(status, sessionId, new ApiCallback<String>() {
                @Override
                public void onResult(Result<String> result) {
                    mView.stopLoading();
                    mView.chatNextStep(result.getContent());
                }
            });
        }

    }

}
