package com.example.han.referralproject.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_widget.EclipseImageView;
import com.gcml.old.auth.personal.PersonDetailActivity;
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
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_person_center:
                startActivity(new Intent(getContext(), PersonDetailActivity.class));
                break;
            case R.id.iv_health_course:
                //健康管理
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
                break;
            case R.id.entertainment_center:
                Routerfit.register(AppRouter.class).skipAlarmList2Activity();
                //娱乐中心
//                CC.obtainBuilder("app.component.recreation").build().callAsync();
                break;
            case R.id.iv_communicate:
                Routerfit.register(AppRouter.class).skipSpeechSynthesisActivity();
                break;
            case R.id.iv_check_self:
//                result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
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
                break;
            case R.id.iv_shopping_mall:
//                startActivity(new Intent(getContext(), MarketActivity.class));
                Routerfit.register(AppRouter.class).skipMarketActivity();
//                CC.obtainBuilder("com.gcml.mall.mall").build().callAsync();
                break;
            case R.id.iv_ask_doctor:
//                startActivity(new Intent(getContext(), DoctorAskGuideActivity.class));
                //健康课堂
                Routerfit.register(AppRouter.class).skipVideoListActivity(0);
                break;
            case R.id.iv_medical_tip:
                if (UserSpHelper.isNoNetwork()) {
                    Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
                    return;
                }

                Routerfit.register(AppRouter.class)
                        .getUserProvider()
                        .getUserEntity()
                        .subscribeOn(Schedulers.io())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new DefaultObserver<UserEntity>() {
                            @Override
                            public void onNext(UserEntity userEntity) {
                                if (TextUtils.isEmpty(userEntity.sex) || TextUtils.isEmpty(userEntity.birthday)) {
                                    ToastUtils.showShort("请先去个人中心完善性别和年龄信息");
                                    MLVoiceSynthetize.startSynthesize(
                                            getActivity().getApplicationContext(),
                                            "请先去个人中心完善性别和年龄信息");
                                } else {
                                    Routerfit.register(AppRouter.class)
                                            .getFaceProvider()
                                            .getFaceId(userEntity.id)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new io.reactivex.observers.DefaultObserver<String>() {
                                                @Override
                                                public void onNext(String faceId) {
                                                    Routerfit.register(AppRouter.class).skipFaceBdSignInActivity(true, true, faceId, true, new ActivityCallback() {
                                                        @Override
                                                        public void onActivityResult(int result, Object data) {
                                                            if (result == Activity.RESULT_OK) {
                                                                String sResult = data.toString();
                                                                if (TextUtils.isEmpty(sResult))
                                                                    return;
                                                                if (sResult.equals("success") || sResult.equals("skip")) {
                                                                    Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
                                                                } else if (sResult.equals("failed")) {
                                                                    ToastUtils.showShort("人脸验证失败");
                                                                }

                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    ToastUtils.showShort("请先注册人脸！");
                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });
//                                    CC.obtainBuilder("com.gcml.auth.face2.signin")
//                                            .addParam("skip", true)
//                                            .addParam("currentUser", false)
//                                            .addParam("hidden", true)
//                                            .build()
//                                            .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                                                @Override
//                                                public void onResult(CC cc, CCResult result) {
//                                                    boolean skip = "skip".equals(result.getErrorMessage());
//                                                    if (result.isSuccess() || skip) {
//                                                        startActivity(new Intent(getActivity(), HealthRecordActivity.class));
//                                                    } else {
//                                                        ToastUtils.showShort(result.getErrorMessage());
//                                                    }
//                                                }
//                                            });
                                }
                            }
                        });
                break;
            case R.id.iv_check_health:
                Routerfit.register(AppRouter.class).getBodyTestProvider().gotoPage(getActivity());
                break;
        }
    }
}
