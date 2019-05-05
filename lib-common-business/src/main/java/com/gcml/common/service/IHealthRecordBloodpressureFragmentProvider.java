package com.gcml.common.service;

import android.support.v4.app.Fragment;

public interface IHealthRecordBloodpressureFragmentProvider {
    Fragment getHealthRecordBloodpressureFragment();
    void fetchDataAndRefreshUI(String start, String end,Fragment fragment);
}
