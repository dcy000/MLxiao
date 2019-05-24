package com.example.han.referralproject.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.face.VertifyFaceProviderImp;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_widget.EclipseImageView;
import com.iflytek.synthetize.MLVoiceSynthetize;

import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private EclipseImageView mIvMedicalTip;
    private EclipseImageView mIvCheckHealth;

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
        mIvMedicalTip = view.findViewById(R.id.iv_medical_tip);
        mIvMedicalTip.setOnClickListener(this);
        mIvCheckHealth = view.findViewById(R.id.iv_check_health);
        mIvCheckHealth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.iv_person_center
                && v.getId() != R.id.iv_medical_tip
                && UserSpHelper.isNoNetwork()) {
            ToastUtils.showShort("请使用有网模式登录");
            return;
        }
        int i = v.getId();
        if (i == R.id.iv_person_center) {
            Routerfit.register(AppRouter.class).skipPersonDetailActivity();

        } else if (i == R.id.iv_health_course) {//健康管理
            Routerfit.register(AppRouter.class)
                    .getUserProvider()
                    .getUserEntity()
                    .subscribeOn(Schedulers.io())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity user) {
                            if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)
                                    || TextUtils.isEmpty(user.sex) || TextUtils.isEmpty(user.birthday)) {
                                ToastUtils.showShort("请先去个人中心完善体重,身高,性别,年龄信息");
                                MLVoiceSynthetize.startSynthesize(
                                        getActivity().getApplicationContext(),
                                        "请先去个人中心完善体重,身高,性别,年龄信息");
                            } else {
                                Routerfit.register(AppRouter.class).skipSlowDiseaseManagementActivity();
                            }
                        }
                    });

        } else if (i == R.id.entertainment_center) {
            Routerfit.register(AppRouter.class).skipAlarmList2Activity();
            //娱乐中心
//                CC.obtainBuilder("app.component.recreation").build().callAsync();

        } else if (i == R.id.iv_communicate) {
            Routerfit.register(AppRouter.class).skipSpeechSynthesisActivity();

        } else if (i == R.id.iv_check_self) {//                result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
//                rxUser = result.getDataItem("data");
//                rxUser.subscribeOn(Schedulers.io())
//                        .as(RxUtils.autoDisposeConverter(this))
//                        .subscribe(new DefaultObserver<UserEntity>() {
//                            @Override
//                            public void onNext(UserEntity user) {
//                                if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
//                                    ToastUtils.showShort("请先去个人中心完善体重和身高信息");
//                                    MLVoiceSynthetize.startSynthesize(
//                                            getActivity().getApplicationContext(),
//                                            "请先去个人中心完善体重和身高信息");
//                                } else {
//                                    Intent intent = new Intent(getActivity(), SymptomCheckActivity.class);
//                                    startActivity(intent);
//                                }
//                            }
//                        });

//                CC.obtainBuilder("app.component.recreation").build().callAsync();
            Routerfit.register(AppRouter.class).skipRecreationEntranceActivity();

        } else if (i == R.id.iv_shopping_mall) {//                startActivity(new Intent(getContext(), MarketActivity.class));
            Routerfit.register(AppRouter.class).skipMarketActivity();
//                CC.obtainBuilder("com.gcml.mall.mall").build().callAsync();

        } else if (i == R.id.iv_ask_doctor) {//                startActivity(new Intent(getContext(), DoctorAskGuideActivity.class));
            //健康课堂
            Routerfit.register(AppRouter.class).skipVideoListActivity(0);

        } else if (i == R.id.iv_medical_tip) {
            if (UserSpHelper.isNoNetwork()) {
                Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
                return;
            }
            Routerfit.register(AppRouter.class)
                    .getVertifyFaceProvider()
                    .checkUserEntityAndVertifyFace(true, true, true, new VertifyFaceProviderImp.VertifyFaceResult() {
                        @Override
                        public void success() {
                            Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
                        }

                        @Override
                        public void failed(String msg) {
                            ToastUtils.showShort(msg);
                        }
                    });
        } else if (i == R.id.iv_check_health) {
            Routerfit.register(AppRouter.class).getBodyTestProvider().gotoPage(getActivity());

        } else {
        }
    }
}
