package com.witspring.unitdisease.contract;

import com.witspring.base.BasePresenter;
import com.witspring.base.BaseView;
import com.witspring.model.ApiCallback;
import com.witspring.model.entity.Medicine;
import com.witspring.model.entity.Result;
import com.witspring.unitdisease.model.DiseaseApi;

import java.util.ArrayList;

/**
 * @author Created by wu_zf on 1/3/2018.
 * @email :wuzf1234@gmail.com
 */

public interface MedicineContract {
    interface View extends BaseView {
        void showMedicines(ArrayList<Medicine> medicines);
        void showMedicineDetail(Medicine medicine);
        void showNoData();
    }

    class Presenter extends BasePresenter {
        private MedicineContract.View mView;
        private DiseaseApi diseaseApi;

        public Presenter(MedicineContract.View mView) {
            this.mView = mView;
            diseaseApi = new DiseaseApi();
        }

        public void getMedicines(int diseaseId, String keyword, int sex, int ageMonth) {
            diseaseApi.getMedicines(diseaseId,keyword,sex,ageMonth,new ApiCallback<ArrayList<Medicine>>(){
//                mView.startLoading();
                @Override
                public void onResult(Result<ArrayList<Medicine>> result) {
//                    mView.stopLoading();
                    if (result.successed()) {
                        mView.showMedicines(result.getContent());
                    } else {
                        mView.showNoData();
                    }
                }
            });
        }

        public void getMedicineDetailById(long diseaseId, int sex, int ageMonth) {
            diseaseApi.getDrugDetilById(diseaseId,sex,ageMonth,new ApiCallback<Medicine>(){
//                mView.startLoading();
                @Override
                public void onResult(Result<Medicine> result) {
//                    mView.stopLoading();
                    if (result.successed()) {
                        mView.showMedicineDetail(result.getContent());
                    } else {
                        mView.showNoData();
                    }
                }
            });
        }
    }
}
