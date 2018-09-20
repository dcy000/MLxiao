package com.example.han.referralproject.health_manager_program;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.hypertensionmanagement.bean.DiagnoseInfoBean;
import com.example.han.referralproject.intelligent_diagnosis.IChangToolbar;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.base.RecycleBaseFragment;
import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.fragments.HealthRecordBloodpressureFragment;
import com.gcml.module_health_record.network.HealthRecordNetworkApi;
import com.gcml.module_health_record.network.HealthRecordRepository;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LastWeekTrendFragment extends RecycleBaseFragment {
    private FrameLayout mLastweekTrendFl;
    private TextView mConclusion;
    private HealthRecordBloodpressureFragment bloodpressureFragment;
    private IChangToolbar iChangToolbar;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_lastweek_trend;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mLastweekTrendFl = view.findViewById(R.id.lastweek_trend_fl);
        mConclusion = view.findViewById(R.id.conclusion);

        bloodpressureFragment = new HealthRecordBloodpressureFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lastweek_trend_fl, bloodpressureFragment).commit();

        Calendar calendar = Calendar.getInstance();
        int selectEndYear = calendar.get(Calendar.YEAR);
        int selectEndMonth = calendar.get(Calendar.MONTH) + 1;
        int selectEndDay = calendar.get(Calendar.DATE);
        String startMillisecond = TimeUtils.string2Milliseconds(selectEndYear + "-" + selectEndMonth + "-" +
                selectEndDay, new SimpleDateFormat("yyyy-MM-dd")) + "";

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);

        Date weekAgoDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(weekAgoDate);
        String[] date = result.split("-");
        int selectStartYear = Integer.parseInt(date[0]);
        int selectStartMonth = Integer.parseInt(date[1]);
        int selectStartDay = Integer.parseInt(date[2]);
        String endMillisecond = TimeUtils.string2Milliseconds(selectStartYear + "-" + selectStartMonth + "-" +
                selectStartDay, new SimpleDateFormat("yyyy-MM-dd")) + "";
        getBloodpressureData(endMillisecond, startMillisecond);
        getResult();
    }

    private void getResult() {
        NetworkApi.getDiagnoseInfo(UserSpHelper.getUserId(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                DiagnoseInfoBean bean = new Gson().fromJson(response.body(), DiagnoseInfoBean.class);
                if (bean != null && bean.tag && bean.data != null) {
                    mConclusion.setText(bean.data.result);
                }
            }

            @Override
            public void onError(Response<String> response) {
                Timber.e(response.body());
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getBloodpressureData(String start, String end) {
//        HealthRecordNetworkApi.getBloodpressureHistory(start, end, "2",
//                response -> bloodpressureFragment.refreshData(response, "2"),
//                message -> bloodpressureFragment.refreshErrorData(message));

        HealthRecordRepository.getBloodpressureHistory(start,end,"2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<BloodPressureHistory>>() {
                    @Override
                    public void onNext(List<BloodPressureHistory> bloodPressureHistories) {
                        bloodpressureFragment.refreshData(bloodPressureHistories, "2");
                    }

                    @Override
                    public void onError(Throwable e) {
                        bloodpressureFragment.refreshErrorData(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
//            if (isAdded()) {
//                ((TreatmentPlanActivity) getActivity()).speak("主人，请查看您一周的血压趋势");
//            }

        }


    }
}
