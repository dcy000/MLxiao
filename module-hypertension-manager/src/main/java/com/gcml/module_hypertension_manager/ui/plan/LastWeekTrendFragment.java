package com.gcml.module_hypertension_manager.ui.plan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.fragment.IChangToolbar;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_hypertension_manager.R;
import com.gcml.module_hypertension_manager.bean.DiagnoseInfoBean;
import com.gcml.module_hypertension_manager.net.HyperRepository;
import com.sjtu.yifei.route.Routerfit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LastWeekTrendFragment extends RecycleBaseFragment {
    private FrameLayout mLastweekTrendFl;
    private TextView mConclusion;
    private Fragment bloodpressureFragment;
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

        bloodpressureFragment = Routerfit.register(AppRouter.class).getFragmentProvider().getHealthRecordBloodpressureFragment();

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
        new HyperRepository().getDiagnoseInfo(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<DiagnoseInfoBean.DataBean>() {
                    @Override
                    public void onNext(DiagnoseInfoBean.DataBean dataBean) {
                        mConclusion.setText(dataBean.result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBloodpressureData(String start, String end) {
        Routerfit.register(AppRouter.class).getFragmentProvider().fetchDataAndRefreshUI(start, end, bloodpressureFragment);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
//            if (isAdded()) {
//                ((TreatmentPlanActivity) getActivity()).speak("请查看您一周的血压趋势");
//            }

        }


    }
}
