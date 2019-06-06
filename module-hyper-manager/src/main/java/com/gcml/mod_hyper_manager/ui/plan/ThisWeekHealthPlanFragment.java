package com.gcml.mod_hyper_manager.ui.plan;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.fragment.IChangToolbar;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.mod_hyper_manager.R;
import com.gcml.mod_hyper_manager.bean.ThisWeekHealthPlan;
import com.gcml.mod_hyper_manager.net.HyperRepository;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/15.
 */

public class ThisWeekHealthPlanFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView mTvDetectionFrequency;
    private TextView mTvXueyaDetectionFrequency;
    private TextView mTvXuetangDetectionFrequency;
    private TextView mWeight;
    private TextView mTvXueyaTitle;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    private TextView mTvXuetangTitle;
    private TextView mTvXuetangEmpty;
    private TextView mTvXuetangOne;
    private TextView mTvXuetangTwo;
    private IChangToolbar iChangToolbar;
    private String TAG = "ThisWeekHealthPlanFragment";
    private TextView mTvSetAlarm;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    private ThisWeekHealthPlan data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_detection_plan, container, false);
            getData();
            initView(view);
        }
        return view;
    }

    private void getData() {
        Log.e(TAG, "getDataCache: ");
        new HyperRepository()
                .getThisWeekPlan(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<ThisWeekHealthPlan>() {
                    @Override
                    public void onNext(ThisWeekHealthPlan thisWeekHealthPlan) {
                        detalData(thisWeekHealthPlan);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void detalData(ThisWeekHealthPlan data) {
        if (data == null) {
            return;
        }
        this.data = data;
        String hypertensionFrequency = data.getHypertensionFrequency();
        if (!TextUtils.isEmpty(hypertensionFrequency)) {
            mTvXueyaDetectionFrequency.setText("血压：" + hypertensionFrequency);
        }
        String diabetesFrequency = data.getDiabetesFrequency();
        if (!TextUtils.isEmpty(diabetesFrequency)) {
            mTvXuetangDetectionFrequency.setText("血糖：" + diabetesFrequency);
        }
        double weightTarget = data.getWeightTarget();
        mWeight.setText(String.format("%.2f", weightTarget));
        int highPressureTarget = data.getHighPressureTarget();
        mTvGaoya.setText(highPressureTarget + "");
        int lowPressureTarget = data.getLowPressureTarget();
        mTvDiya.setText(lowPressureTarget + "");
        double bloodSugarTarget = data.getBloodSugarTarget();
        mTvXuetangEmpty.setText(String.format("%.2f", bloodSugarTarget));
        double bloodSugarOneTarget = data.getBloodSugarOneTarget();
        double bloodSugarTwoTarget = data.getBloodSugarTwoTarget();
        mTvXuetangOne.setText(String.format("%.2f", bloodSugarTwoTarget));
        mTvXuetangTwo.setText(String.format("%.2f", bloodSugarTwoTarget));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e(TAG, "setUserVisibleHint: ");
//            if (data != null) {
//                ((TreatmentPlanActivity) getActivity()).speak("小易为您制定了本周的健康计划。血压" + data.getHypertensionFrequency() + ",血糖" +
//                        data.getDiabetesFrequency() + ",本周体重目标" + String.format("%.2f", data.getWeightTarget()) + "千克，高压目标"
//                        + data.getHighPressureTarget() + ",低压" + data.getLowPressureTarget() + ",血糖目标" + String.format("%.2f", data.getBloodSugarTarget()));
//            }
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }

    private void initView(View view) {
        mTvDetectionFrequency = view.findViewById(R.id.tv_detection_frequency);
        mTvXueyaDetectionFrequency = view.findViewById(R.id.tv_xueya_detection_frequency);
        mTvXuetangDetectionFrequency = view.findViewById(R.id.tv_xuetang_detection_frequency);
        mWeight = view.findViewById(R.id.weight);
        mTvXueyaTitle = view.findViewById(R.id.tv_xueya_title);
        mTvGaoya = view.findViewById(R.id.tv_gaoya);
        mTvDiya = view.findViewById(R.id.tv_diya);
        mTvXuetangTitle = view.findViewById(R.id.tv_xuetang_title);
        mTvXuetangEmpty = view.findViewById(R.id.tv_xuetang_empty);
        mTvXuetangOne = view.findViewById(R.id.tv_xuetang_one);
        mTvXuetangTwo = view.findViewById(R.id.tv_xuetang_two);
        mTvSetAlarm = view.findViewById(R.id.tv_set_alarm);
        mTvSetAlarm.setOnClickListener(this);

        mWeight.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangEmpty.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangOne.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangTwo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_set_alarm) {
            Routerfit.register(AppRouter.class).skipAlarmDetail2Activity(-1L);
        }
    }
}
