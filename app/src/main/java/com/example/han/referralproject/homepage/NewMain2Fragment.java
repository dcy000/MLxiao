package com.example.han.referralproject.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.tcm.SymptomCheckActivity;
import com.example.han.referralproject.video.VideoListActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.base.RecycleBaseFragment;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_widget.EclipseImageView;
import com.gcml.old.auth.personal.PersonDetailActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 17:24
 * created by:gzq
 * description:主页第2页
 */
public class NewMain2Fragment extends RecycleBaseFragment implements View.OnClickListener {
    private EclipseImageView mIvPersonCenter;
    private EclipseImageView mIvHealthCourse;
    private EclipseImageView mEntertainmentCenter;
    private EclipseImageView mIvCommunicate;
    private EclipseImageView mIvCheckSelf;
    private EclipseImageView mIvShoppingMall;
    private EclipseImageView mIvAskDoctor;

    @Override
    protected int initLayout() {
        return R.layout.fragment_newmain2;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mIvPersonCenter = view.findViewById(R.id.iv_person_center);
        mIvPersonCenter.setOnClickListener(this);
        mIvHealthCourse = view.findViewById(R.id.iv_health_course);
        mIvHealthCourse.setOnClickListener(this);
        mEntertainmentCenter = view.findViewById(R.id.entertainment_center);
        mEntertainmentCenter.setOnClickListener(this);
        mIvCommunicate = view.findViewById(R.id.iv_communicate);
        mIvCommunicate.setOnClickListener(this);
        mIvCheckSelf = view.findViewById(R.id.iv_check_self);
        mIvCheckSelf.setOnClickListener(this);
        mIvShoppingMall = view.findViewById(R.id.iv_shopping_mall);
        mIvShoppingMall.setOnClickListener(this);
        mIvAskDoctor = view.findViewById(R.id.iv_ask_doctor);
        mIvAskDoctor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CCResult result;
        Observable<UserEntity> rxUser;
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_person_center:
                startActivity(new Intent(getContext(), PersonDetailActivity.class));
                break;
            case R.id.iv_health_course:
                //健康管理
                result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
                rxUser = result.getDataItem("data");
                rxUser.subscribeOn(Schedulers.io())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new DefaultObserver<UserEntity>() {
                            @Override
                            public void onNext(UserEntity user) {
                                if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)
                                        || TextUtils.isEmpty(user.sex)|| TextUtils.isEmpty(user.age)) {
                                    ToastUtils.showShort("请先去个人中心完善体重,身高,性别,年龄信息");
                                    MLVoiceSynthetize.startSynthesize(
                                            getActivity().getApplicationContext(),
                                            "请先去个人中心完善体重,身高,性别,年龄信息");
                                } else {
                                    Intent intent = new Intent(getActivity(), SlowDiseaseManagementActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
//                startActivity(new Intent(getActivity(), VideoListActivity.class));
//                CCHealthMeasureActions.jump2HealthIntelligentDetectionActivity();
//                CCHealthMeasureActions.jump2FirstDiagnosisActivity();
                break;
            case R.id.entertainment_center:
//                OldRouter.routeToOldHomeActivity(getActivity());
                //健康课堂
                startActivity(new Intent(getActivity(), VideoListActivity.class));
                //娱乐中心
//                CC.obtainBuilder("app.component.recreation").build().callAsync();
                break;
            case R.id.iv_communicate:
                startActivity(new Intent(getContext(), SpeechSynthesisActivity.class));
                break;
            case R.id.iv_check_self:
                result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
                rxUser = result.getDataItem("data");
                rxUser.subscribeOn(Schedulers.io())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new DefaultObserver<UserEntity>() {
                            @Override
                            public void onNext(UserEntity user) {
                                if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                                    ToastUtils.showShort("请先去个人中心完善体重和身高信息");
                                    MLVoiceSynthetize.startSynthesize(
                                            getActivity().getApplicationContext(),
                                            "请先去个人中心完善体重和身高信息");
                                } else {
                                    Intent intent = new Intent(getActivity(), SymptomCheckActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                break;
            case R.id.iv_shopping_mall:
                startActivity(new Intent(getContext(), MarketActivity.class));
//                CC.obtainBuilder("com.gcml.mall.mall").build().callAsync();
                break;
            case R.id.iv_ask_doctor:
                startActivity(new Intent(getContext(), DoctorAskGuideActivity.class));
                break;
        }
    }
}
