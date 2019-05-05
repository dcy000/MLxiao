package com.gcml.module_health_record.fragments;

import android.support.v4.app.Fragment;

import com.gcml.common.service.IHealthRecordBloodpressureFragmentProvider;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.network.HealthRecordRepository;
import com.sjtu.yifei.annotation.Route;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/health/record/blood/pressure/fragment/provider")
public class HealthRecordBloodpressureFragmentProviderImp implements IHealthRecordBloodpressureFragmentProvider {
    @Override
    public Fragment getHealthRecordBloodpressureFragment() {
        return new HealthRecordBloodpressureFragment();
    }

    @Override
    public void fetchDataAndRefreshUI(String start, String end, Fragment fragment) {
        HealthRecordRepository.getBloodpressureHistory(start, end, "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(fragment))
                .subscribeWith(new DefaultObserver<List<BloodPressureHistory>>() {
                    @Override
                    public void onNext(List<BloodPressureHistory> bloodPressureHistories) {
                        ((HealthRecordBloodpressureFragment) fragment).refreshData(bloodPressureHistories, "2");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((HealthRecordBloodpressureFragment) fragment).refreshErrorData(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
