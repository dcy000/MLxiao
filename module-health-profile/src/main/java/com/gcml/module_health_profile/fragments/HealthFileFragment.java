package com.gcml.module_health_profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.http.ApiException;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.HealthProfileActivity;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.HealthRecordBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.gcml.module_health_profile.webview.AddHealthProfileActivity;
import com.gcml.module_health_profile.webview.EditHealthProfileActivity;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthFileFragment extends RecycleBaseFragment implements View.OnClickListener {
    /**
     * 最新建档时间：2018年1月21日
     */
    private TextView mTvLastBuildTime;
    private TextView mTvLine;
    private LinearLayout mLlName;
    private LinearLayout mLlGender;
    private LinearLayout mLlCard;
    private LinearLayout mLlDisease;
    private LinearLayout mLlSignDoctor;
    /**
     * 编辑
     */
    private TextView mTvEdit;
    private String recordId;
    /**
     * 测试
     */
    private TextView mTvName;
    /**
     * 未知
     */
    private TextView mTvSex;
    /**
     * 510822************1234
     */
    private TextView mTvIdcard;
    /**
     * 510822************1234
     */
    private TextView mTvDisease;
    /**
     * 医生
     */
    private TextView mTvDoctor;
    private String doctorId;
    private String signState;
    /**
     * 去建档
     */
    private TextView mTvBuild;
    private String selfRecordId;
    private String historyRecordId;
    private boolean bindDoctor;
    private boolean bindWacher;

    public static HealthFileFragment instance(String recordId, String selfRecordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);
        bundle.putString("selfRecordId", selfRecordId);
        HealthFileFragment healthFileFragment = new HealthFileFragment();
        healthFileFragment.setArguments(bundle);
        return healthFileFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_health_file;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        recordId = bundle.getString("recordId");
        selfRecordId = bundle.getString("selfRecordId");
        mTvLastBuildTime = (TextView) view.findViewById(R.id.tv_last_build_time);
        mTvLine = (TextView) view.findViewById(R.id.tv_line);
        mLlName = (LinearLayout) view.findViewById(R.id.ll_name);
        mLlGender = (LinearLayout) view.findViewById(R.id.ll_gender);
        mLlCard = (LinearLayout) view.findViewById(R.id.ll_card);
        mLlDisease = (LinearLayout) view.findViewById(R.id.ll_disease);
        mLlSignDoctor = (LinearLayout) view.findViewById(R.id.ll_sign_doctor);
        mTvEdit = (TextView) view.findViewById(R.id.tv_edit_health_file);
        mTvEdit.setOnClickListener(this);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvSex = (TextView) view.findViewById(R.id.tv_sex);
        mTvIdcard = (TextView) view.findViewById(R.id.tv_idcard);
        mTvDisease = (TextView) view.findViewById(R.id.tv_disease);
        mTvDoctor = (TextView) view.findViewById(R.id.tv_doctor);
        mTvBuild = (TextView) view.findViewById(R.id.tvBuild);
        mTvBuild.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        bindData();
    }

    private void getData() {
        HealthProfileRepository repository = new HealthProfileRepository();
        repository.getHealthRecordList(selfRecordId, UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<HealthRecordBean>>() {
                    @Override
                    public void onNext(List<HealthRecordBean> healthRecordBeans) {
                        if (healthRecordBeans == null || healthRecordBeans.size() == 0) {
                            view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.cl_contain).setVisibility(View.GONE);
                            ((HealthProfileActivity) getActivity()).isBuildHealthRecord.postValue(false);
                        } else {
                            ((HealthProfileActivity) getActivity()).isBuildHealthRecord.postValue(true);
                            view.findViewById(R.id.empty_view).setVisibility(View.GONE);
                            view.findViewById(R.id.cl_contain).setVisibility(View.VISIBLE);
                            String modifiedTime = healthRecordBeans.get(0).getModifiedTime();
                            historyRecordId = healthRecordBeans.get(0).getRdUserRecordId();
                            if (!TextUtils.isEmpty(modifiedTime)) {
                                String[] s = modifiedTime.split("\\s+");
                                if (s.length == 2) {
                                    mTvLastBuildTime.setText("最新建档时间:" + s[0]);
                                } else {
                                    mTvLastBuildTime.setText("最新建档时间:未知");
                                }
                            } else {
                                mTvLastBuildTime.setText("最新建档时间:未知");
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            ApiException exception = (ApiException) e;
                            if (exception.code() == 9002) {
                                view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.cl_contain).setVisibility(View.GONE);
                                ((HealthProfileActivity) getActivity()).isBuildHealthRecord.postValue(false);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new com.gcml.common.utils.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        doctorId = user.doctorId;
                        signState = user.state;
                        mTvName.setText(user.name);
                        mTvSex.setText(user.sex);
                        mTvIdcard.setText(user.idCard);
                        mTvDisease.setText("暂无");


                        if ("1".equals(signState)) {
                        } else if ("0".equals(signState) && !TextUtils.isEmpty(signState)) {
                            mTvDoctor.setText("待审核");
                        } else {
                            mTvDoctor.setText("未签约");
                        }
                        if (TextUtils.isEmpty(doctorId)) {
                            mTvDoctor.setText("未签约");
                        } else {
                            repository.getDoctorInfo(doctorId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .as(RxUtils.autoDisposeConverter(HealthFileFragment.this))
                                    .subscribe(new DefaultObserver<Doctor>() {
                                        @Override
                                        public void onNext(Doctor doctor) {
                                            mTvDoctor.setText(doctor.doctername);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_edit_health_file) {
            getActivity().startActivity(new Intent(getActivity(), EditHealthProfileActivity.class)
                    .putExtra("RdCordId", selfRecordId)
                    .putExtra("HealthRecordId", historyRecordId));

        } else if (i == R.id.tvBuild) {
            if (bindDoctor) {
                getActivity().startActivity(new Intent(getActivity(), AddHealthProfileActivity.class)
                        .putExtra("RdCordId", selfRecordId)
                        .putExtra("title", "添 加 健 康 档 案"));
            } else {
                //TODO:去绑定医生
//                CC.obtainBuilder(KEY_BIND_DOCTOR).build().callAsync();
            }
        }
    }

    private void bindData() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new com.gcml.common.utils.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (user != null && user.doctorId != null) {
                            bindDoctor = true;
                        }
                        if (!TextUtils.isEmpty(user.watchCode)) {
                            bindWacher = true;
                        } else {
                            bindWacher = false;
                        }
                    }
                });
    }
}
