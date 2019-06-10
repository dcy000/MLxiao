package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcml.common.LazyFragment;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.lib_widget.EclipseImageView;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainXaFragment extends LazyFragment implements View.OnClickListener {
    private LinearLayout mLlPernal;
    private EclipseImageView mEiHealthCheckup;
    private EclipseImageView mEiInfomationCollection;
    private EclipseImageView mEiDoctorService;
    private EclipseImageView mEiQuit;
    private EclipseImageView mWenZen;
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
        mEiDoctorService = (EclipseImageView) view.findViewById(R.id.ei_doctor_service);
        mEiDoctorService.setOnClickListener(this);
        mEiQuit = (EclipseImageView) view.findViewById(R.id.ei_quit);
        mEiQuit.setOnClickListener(this);
        mCvHead = (CircleImageView) view.findViewById(R.id.cv_head);
        mWenZen = view.findViewById(R.id.ei_doctor_wenzen);
        mWenZen.setOnClickListener(this);
        mTvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        view.findViewById(R.id.ei_health_edu).setOnClickListener(new FilterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ZenDuanActivity.class));
            }
        }));
//        getPersonInfo();
    }

    private Boolean bindWacher = false;

    @Override
    public void onResume() {
        super.onResume();
//        getPersonInfo();
    }

    //获取个人信息，得到网易账号登录所需的账号和密码
    private void getPersonInfo() {
        if ("123456".equals(UserSpHelper.getUserId())) {
            return;
        }
        Observable<UserEntity> rxUsers = null;
//        rxUsers = CC.obtainBuilder("com.gcml.auth.getUser")
//                .build()
//                .call()
//                .getDataItem("data");
        rxUsers.subscribeOn(Schedulers.io())
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
//                        NimAccountHelper.getInstance().login(wyyxId, wyyxPwd, null);
//                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
//                                .addParam("userId", user.id)
//                                .build()
//                                .callAsync();
                        if (user != null) {
                            if (!TextUtils.isEmpty(user.avatar)) {
                                Glide.with(UM.getApp())
                                        .load(user.avatar)
                                        .into(mCvHead);
                            }
                            if (!TextUtils.isEmpty(user.name)) {
                                mTvUserName.setText(user.name);
                            }
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
//                    startActivity(new Intent(getActivity(), HealthManageActivity.class));
                } else {
//                    startActivity(new Intent(getActivity(), HealthManageTipActivity.class));
                }
                break;
            case R.id.ei_doctor_service:
//                CC.obtainBuilder("health.profile").build().call();
                break;
            case R.id.ei_quit:
                quitApp();
                break;
            case R.id.ei_doctor_wenzen:
//                CC.obtainBuilder(KEY_INQUIRY).build().callAsync();
                break;

        }
    }

    private void gotoHealthMeasure() {
        Observable<UserEntity> rxUser = null;
//        result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
//        rxUser = result.getDataItem("data");
        rxUser.subscribeOn(Schedulers.io())
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
//                            CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(false);
                        }
                    }
                });
    }

    private void quitApp() {
        new AlertDialog(getActivity())
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
//                        NimAccountHelper.getInstance().logout();//退出网易IM
                        UserSpHelper.setToken("");
                        UserSpHelper.setEqId("");
//                        CC.obtainBuilder("com.gcml.auth").build().callAsync();
                        getActivity().finish();
                    }
                }).show();

    }

}
