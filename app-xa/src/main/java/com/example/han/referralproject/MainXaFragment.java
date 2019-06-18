package com.example.han.referralproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.healthmanage.HealthManageActivity;
import com.gcml.common.LazyFragment;
import com.gcml.common.constant.EUserInfo;
import com.gcml.common.constant.Global;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.CheckUserInfoProviderImp;
import com.gcml.common.service.ICallProvider;
import com.gcml.common.service.IHuiQuanBodyTestProvider;
import com.gcml.common.service.IUserEntityProvider;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.lib_widget.EclipseImageView;
import com.sjtu.yifei.route.Routerfit;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainXaFragment extends LazyFragment implements View.OnClickListener {
    private LinearLayout mLlPernal;
    private EclipseImageView mEiHealthCheckup;
    private EclipseImageView mEiInfomationCollection;
    private EclipseImageView mEiDoctorService, eiRecommend, eiReservation, eiMedicalHome, eiRecommendNew;
    private EclipseImageView mEiQuit;
    private EclipseImageView ivDoctorCall;
    private EclipseImageView ivDoctorFamily;
    private EclipseImageView eiHealthEdu;
    private EclipseImageView mEiEntertainment;
    private View view;
    private CircleImageView mCvHead;
    private TextView mTvUserName;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_xa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    protected void initView(View view, Bundle bundle) {
        mLlPernal = (LinearLayout) view.findViewById(R.id.ll_pernal);
        mLlPernal.setOnClickListener(this);
        mEiHealthCheckup = (EclipseImageView) view.findViewById(R.id.ei_health_checkup);
        mEiHealthCheckup.setOnClickListener(this);
        mEiInfomationCollection = (EclipseImageView) view.findViewById(R.id.ei_infomation_collection);
        mEiInfomationCollection.setOnClickListener(this);
        mEiDoctorService = (EclipseImageView) view.findViewById(R.id.iv_self_check);
        mEiDoctorService.setOnClickListener(this);
        mEiEntertainment = (EclipseImageView) view.findViewById(R.id.ei_entertainment);
        mEiEntertainment.setOnClickListener(this);
        mCvHead = (CircleImageView) view.findViewById(R.id.cv_head);
        ivDoctorCall = view.findViewById(R.id.iv_doctor_call);
        ivDoctorCall.setOnClickListener(this);
        ivDoctorFamily = view.findViewById(R.id.iv_doctor_family);
        ivDoctorFamily.setOnClickListener(this);
        eiHealthEdu = view.findViewById(R.id.ei_health_edu);
        eiHealthEdu.setOnClickListener(this);
        mTvUserName = (TextView) view.findViewById(R.id.tv_user_name);

        eiRecommend = (EclipseImageView) view.findViewById(R.id.ei_recommend);
        eiRecommend.setOnClickListener(this);

        eiReservation = (EclipseImageView) view.findViewById(R.id.ei_reservation);
        eiReservation.setOnClickListener(this);

        eiMedicalHome = (EclipseImageView) view.findViewById(R.id.ei_medical_home);
        eiMedicalHome.setOnClickListener(this);
        eiRecommendNew = (EclipseImageView) view.findViewById(R.id.ei_entertainment_new);
        eiRecommendNew.setOnClickListener(this);

//        getPersonInfo();
    }

    private Boolean bindWacher = false;

    @Override
    public void onResume() {
        super.onResume();
        getPersonInfo();
    }

    //获取个人信息，得到网易账号登录所需的账号和密码
    private void getPersonInfo() {
        if ("123456".equals(UserSpHelper.getUserId())) {
            return;
        }
        final AppRouter appRouter = Routerfit.register(AppRouter.class);
        IUserEntityProvider userProvider = appRouter.getUserProvider();
        if (userProvider == null) {
            return;
        }
        userProvider.getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        Timber.i(user.toString());
                        UserSpHelper.setUserHypertensionHand(user.hypertensionHand);
                        UserSpHelper.setUserId(user.id);
                        UserSpHelper.setEqId(user.deviceId);
                        String wyyxId = user.wyyxId;
                        String wyyxPwd = user.wyyxPwd;

                        if (!TextUtils.isEmpty(user.watchCode)) {
                            bindWacher = true;
                        } else {
                            bindWacher = false;
                        }

                        if (TextUtils.isEmpty(wyyxId) || TextUtils.isEmpty(wyyxPwd)) {
                            Timber.e("获取网易账号信息出错");
                            return;
                        }

                        ICallProvider callProvider = appRouter.getCallProvider();
                        if (callProvider != null) {
                            callProvider.login(wyyxId, wyyxPwd);
                        }

//                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
//                                .addParam("userId", user.id)
//                                .build()
//                                .callAsync();

                        if (!TextUtils.isEmpty(user.avatar)) {
                            Glide.with(UM.getApp())
                                    .load(user.avatar)
                                    .into(mCvHead);
                        }
                        if (!TextUtils.isEmpty(user.name)) {
                            mTvUserName.setText(user.name);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_pernal:
                Routerfit.register(AppRouter.class).skipUserCenterActivity();
                break;
            case R.id.ei_health_checkup:
                gotoHealthMeasure();
                break;
            case R.id.ei_infomation_collection:
                //进入这个模块需要先完善性别信息
                Routerfit.register(AppRouter.class)
                        .getCheckUserInfoProvider()
                        .check(new CheckUserInfoProviderImp.CheckUserInfo() {
                            @Override
                            public void complete(UserEntity userEntity) {
                                startActivity(new Intent(getActivity(), HealthManageActivity.class));
                            }

                            @Override
                            public void incomplete(UserEntity entity, List<EUserInfo> args, String s) {
                                showNotMsgDiaglog(s);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }, EUserInfo.GENDER);
                break;
            case R.id.iv_self_check:
                IHuiQuanBodyTestProvider bodyTestProvider = Routerfit.register(AppRouter.class).getBodyTestProvider();
                if (bodyTestProvider != null && getActivity() != null) {
                    bodyTestProvider.gotoPage(getActivity());
                }
                break;
            case R.id.ei_entertainment:
                ToastUtils.showShort("敬请期待~");
//                quitApp();
                break;
            case R.id.iv_doctor_call:
                Routerfit.register(AppRouter.class).skipDoctorAskGuideActivity();
                break;
            case R.id.iv_doctor_family:
                Routerfit.register(AppRouter.class).skipHealthProfileActivity();
                break;
            case R.id.ei_health_edu:
//                Routerfit.register(AppRouter.class).skipAddHealthProfileActivity("");
                Routerfit.register(AppRouter.class).skipVideoListActivity(0);
                break;

            case R.id.ei_recommend://预约挂号
                ToastUtils.showShort("啊哦！预约挂号服务暂不可用，与医院信息系统对接开发后方可使用，敬请期待~");
                break;
            case R.id.ei_reservation://医护上门
                ToastUtils.showShort("啊哦！医护上门服务暂不可用，与医院信息系统对接开发后方可使用，敬请期待~");
                break;
            case R.id.ei_medical_home://退出
                quitApp();
                break;
            case R.id.ei_entertainment_new://娱乐
                Routerfit.register(AppRouter.class).skipRecreationEntranceActivity();
                break;

        }
    }

    private void showNotMsgDiaglog(String msg) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_not_person_msg)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.txt_msg, "完成每日任务需要先完善" + msg + "信息，方便我们为您生成详细报告。");
                        holder.setOnClickListener(R.id.btn_neg, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.btn_pos, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Routerfit.register(AppRouter.class).skipUserInfoActivity();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(getFragmentManager());
    }

    private void gotoHealthMeasure() {
        Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
//        Routerfit.register(AppRouter.class)
//                .getVertifyFaceProvider3()
//                .checkUserEntityAndVertifyFace(true, true, true, new VertifyFace2ProviderImp.VertifyFaceResult() {
//                    @Override
//                    public void success() {
//                        Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
//                    }
//
//                    @Override
//                    public void failed(String msg) {
//                        ToastUtils.showShort(msg);
//                    }
//                });
    }

    private void quitApp() {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        new AlertDialog(activity)
                .builder()
                .setMsg("确定退出当前账号吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
//                .setNegativeButtonColor(R.color.toolbar_bg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onProfileSignOff();
                        final AppRouter appRouter = Routerfit.register(AppRouter.class);
                        ICallProvider callProvider = appRouter.getCallProvider();
                        if (callProvider != null) {
                            callProvider.logout();
                        }
                        UserSpHelper.setToken(Global.TOURIST_TOKEN);
                        UserSpHelper.setEqId("");
                        appRouter.skipUserLogins2Activity();
//                        CC.obtainBuilder("com.gcml.auth").build().callAsync();
                        activity.finish();
                    }
                }).show();

    }

}
