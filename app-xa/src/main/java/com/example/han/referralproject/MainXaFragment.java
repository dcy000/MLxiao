package com.example.han.referralproject;

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
import com.gcml.common.LazyFragment;
import com.gcml.common.constant.Global;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.ICallProvider;
import com.gcml.common.service.IHuiQuanBodyTestProvider;
import com.gcml.common.service.IUserEntityProvider;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.lib_widget.EclipseImageView;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainXaFragment extends LazyFragment implements View.OnClickListener {
    private LinearLayout mLlPernal;
    private EclipseImageView mEiHealthCheckup;
    private EclipseImageView mEiInfomationCollection;
    private EclipseImageView mEiDoctorService;
    private EclipseImageView mEiQuit;
    private EclipseImageView ivDoctorCall;
    private EclipseImageView ivDoctorFamily;
    private EclipseImageView eiHealthEdu;
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
        mEiQuit = (EclipseImageView) view.findViewById(R.id.ei_quit);
        mEiQuit.setOnClickListener(this);
        mCvHead = (CircleImageView) view.findViewById(R.id.cv_head);
        ivDoctorCall = view.findViewById(R.id.iv_doctor_call);
        ivDoctorCall.setOnClickListener(this);
        ivDoctorFamily = view.findViewById(R.id.iv_doctor_family);
        ivDoctorFamily.setOnClickListener(this);
        eiHealthEdu = view.findViewById(R.id.ei_health_edu);
        eiHealthEdu.setOnClickListener(this);
        mTvUserName = (TextView) view.findViewById(R.id.tv_user_name);
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
//                startActivity(new Intent(getActivity(), InquiryActivity.class));
//                CC.obtainBuilder("health_measure")
//                        .setActionName("To_HealthInquiryActivity")
//                        .build()
//                        .call();
                if (bindWacher) {
                    Routerfit.register(AppRouter.class).skipSlowDiseaseManagementActivity();
//                    startActivity(new Intent(getActivity(), HealthManageActivity.class));
                } else {
                    Routerfit.register(AppRouter.class).skipSlowDiseaseManagementTipActivity();
//                    startActivity(new Intent(getActivity(), HealthManageTipActivity.class));
                }
                break;
            case R.id.iv_self_check:
                IHuiQuanBodyTestProvider bodyTestProvider = Routerfit.register(AppRouter.class).getBodyTestProvider();
                if (bodyTestProvider != null && getActivity() != null) {
                    bodyTestProvider.gotoPage(getActivity());
                }
                break;
            case R.id.ei_quit:
                quitApp();
                break;
            case R.id.iv_doctor_call:
                ICallProvider callProvider = Routerfit.register(AppRouter.class).getCallProvider();
                if (callProvider != null && getActivity() != null) {
                    callProvider.call(getActivity(), "");
                }
                break;
            case R.id.iv_doctor_family:
                Routerfit.register(AppRouter.class).skipHealthProfileActivity();
                break;
            case R.id.ei_health_edu:
                Routerfit.register(AppRouter.class).skipVideoListActivity(0);
                break;

        }
    }

    private void gotoHealthMeasure() {
        final AppRouter appRouter = Routerfit.register(AppRouter.class);
        IUserEntityProvider userProvider = appRouter.getUserProvider();
        if (userProvider == null) {
            return;
        }
        userProvider.getUserEntity()
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
                            appRouter.skipChooseDetectionTypeActivity();
                        }
                    }
                });
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
