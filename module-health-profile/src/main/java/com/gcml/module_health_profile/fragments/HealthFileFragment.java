package com.gcml.module_health_profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.HealthRecordBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.gcml.module_health_profile.webview.EditHealthFileActivity;

import java.util.List;

import io.reactivex.Observable;
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

    public static HealthFileFragment instance(String recordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);
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
        getData();
    }

    private void getData() {
        HealthProfileRepository repository = new HealthProfileRepository();
        repository.getHealthRecordList(recordId, UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<HealthRecordBean>>() {
                    @Override
                    public void onNext(List<HealthRecordBean> healthRecordBeans) {
                        if (healthRecordBeans == null || healthRecordBeans.size() == 0) {
                            mTvLastBuildTime.setText("您还未建立档案");
                        } else {
                            String createdTime = healthRecordBeans.get(0).getCreatedTime();
                            if (!TextUtils.isEmpty(createdTime)) {
                                String[] s = createdTime.split(" ");
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Observable<UserEntity> rxUsers = CC.obtainBuilder("com.gcml.auth.getUser")
                .build()
                .call()
                .getDataItem("data");
        rxUsers.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new com.gcml.common.utils.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        doctorId = user.doctorId;
                        signState = user.state;
                        mTvName.setText(user.name);
                        mTvSex.setText(user.sex);
                        mTvIdcard.setText(user.idCard);
                        mTvDisease.setText("暂无");
                    }
                });
        if ("1".equals(signState)) {
//            mTvDoctor.setText("已签约");
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
                    .as(RxUtils.autoDisposeConverter(this))
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_edit_health_file) {
            getActivity().startActivity(new Intent(getActivity(), EditHealthFileActivity.class));
        } else {
        }
    }

}
