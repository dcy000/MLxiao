package com.gcml.auth.ui.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.recommend.bean.get.RobotAmount;
import com.gcml.common.recommend.bean.get.ServicePackageBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.app.AppUtils;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.sjtu.yifei.route.Routerfit;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2018/3/12.
 */

public class PersonDetailFragment extends Fragment implements View.OnClickListener {
    public String userId;

    public TextView tvUserName;
    public ImageView headImg;
    public ImageView mIvAlarm;

    public TextView signDoctorName;
    public TextView tvBalance;

    public TextView isSignDoctor;
    public ImageView recreation;

    public ImageView yuLe;

    public ImageView education;

    private UserRepository userRepository = new UserRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_person, container, false);
        userId = UserSpHelper.getUserId();
        headImg = view.findViewById(R.id.per_image);
        recreation = view.findViewById(R.id.iv_laoren_yule);
        recreation.setOnClickListener(this);
        tvBalance = view.findViewById(R.id.tv_balance);
        view.findViewById(R.id.main_iv_health_class).setOnClickListener(this);
        isSignDoctor = view.findViewById(R.id.doctor_status);
        isSignDoctor.setOnClickListener(this);
        education = view.findViewById(R.id.iv_youjiao_wenyu);
        education.setOnClickListener(this);
        yuLe = view.findViewById(R.id.main_iv_old);
        yuLe.setOnClickListener(this);
        view.findViewById(R.id.iv_message).setOnClickListener(this);
        view.findViewById(R.id.iv_check).setOnClickListener(this);
        view.findViewById(R.id.view_wifi).setOnClickListener(this);
        view.findViewById(R.id.iv_record).setOnClickListener(this);
        view.findViewById(R.id.iv_jiankang_riji).setOnClickListener(this);
        view.findViewById(R.id.iv_pay).setOnClickListener(this);
        view.findViewById(R.id.iv_order).setOnClickListener(this);
        view.findViewById(R.id.iv_shezhi).setOnClickListener(this);
        view.findViewById(R.id.iv_tools).setOnClickListener(this);
        tvUserName = view.findViewById(R.id.per_name);
        view.findViewById(R.id.iv_change_account).setOnClickListener(this);
        headImg.setOnClickListener(this);
        view.findViewById(R.id.tv_update).setOnClickListener(this);
        mIvAlarm = view.findViewById(R.id.iv_alarm);
        mIvAlarm.setOnClickListener(this);
        signDoctorName = view.findViewById(R.id.doctor_name);
        ((TextView) view.findViewById(R.id.tv_update)).setText("检查更新 v" + AppUtils.getAppInfo().getVersionName());
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(UserSpHelper.getUserId())) {
            ToastUtils.showShort("请重新登录");
            return;
        }
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        userRepository.queryServicePackage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new io.reactivex.observers.DefaultObserver<ServicePackageBean>() {
                    @Override
                    public void onNext(ServicePackageBean servicePackageBean) {
                        if (servicePackageBean != null) {
                            String type = servicePackageBean.getType();
                            if (type.equals("3")) {
                                //有套餐生效，跳转到测试界面
                                tvUserName.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(UM.getApp(), R.drawable.ic_vip), null, null, null);
                                return;
                            }
                        }
                        //没有套餐生效
                        tvUserName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //没有套餐生效
                        tvUserName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getData() {
        boolean empty = TextUtils.isEmpty(UserSpHelper.getUserId());
        if (empty) {
            String message = "请重新登录！";
            ToastUtils.showShort(message);
            Routerfit.register(AppRouter.class).skipAuthActivity();
        } else {
            getApiData();
        }
    }

    private void getApiData() {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        userRepository.getUserSignIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        UserSpHelper.setFaceId(user.xfid);
                        tvUserName.setText(user.name);
                        ImageLoader.with(activity)
                                .load(user.avatar)
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .into(headImg);
                        if (UserSpHelper.isNoNetwork()) {
                            isSignDoctor.setText("未签约");
                            return;
                        }
                        if ("1".equals(user.state)) {
                            isSignDoctor.setText("已绑定");
                        } else if ("0".equals(user.state)
                                && (TextUtils.isEmpty(user.doctorId))) {
                            isSignDoctor.setText("未绑定");
                        } else {
                            isSignDoctor.setText("待审核");
                        }
                    }
                });

        if (UserSpHelper.isNoNetwork()) {
            tvBalance.setText(String.format(getString(R.string.robot_amount), "0.00"));
            return;
        } else {
            userRepository.amount(DeviceUtils.getIMEI())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<RobotAmount>() {
                        @Override
                        public void onNext(RobotAmount robotAmount) {
                            if (robotAmount.getAmount() != null) {
                                tvBalance.setText(String.format(getString(R.string.robot_amount), robotAmount.getAmount()));
                            }
                        }
                    });
        }

        userRepository.doctor(UserSpHelper.getUserId())
                .doOnNext(new Consumer<Doctor>() {
                    @Override
                    public void accept(Doctor doctor) throws Exception {
                        UserSpHelper.setDoctor(doctor);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Doctor>() {
                    @Override
                    public void onNext(Doctor doctor) {
                        if (!"".equals(doctor.getDoctername())) {
                            signDoctorName.setText(doctor.getDoctername());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        signDoctorName.setText("暂无");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.iv_record
                && v.getId() != R.id.iv_change_account
                && UserSpHelper.isNoNetwork()) {
            ToastUtils.showShort("请使用有网模式登录");
            return;
        }
        int i = v.getId();
        if (i == R.id.iv_order) {//我的订单
            Routerfit.register(AppRouter.class).skipOldOrderListActivity();

        } else if (i == R.id.iv_pay) {//账户充值
//                ToastUtils.showShort("该功能暂未开放");
//                MLVoiceSynthetize.startSynthesize(MyApplication.getInstance(),"该功能暂未开放",false);
            Routerfit.register(AppRouter.class).skipPayActivity();
//                CC.obtainBuilder("com.gcml.mall.recharge").build().callAsync();

        } else if (i == R.id.iv_message) {//                startActivity(new Intent(getActivity(), MessageActivity.class));
            Routerfit.register(AppRouter.class).skipOldOrderListActivity();

        } else if (i == R.id.iv_shezhi) {
            Routerfit.register(AppRouter.class).skipSettingActivity();

        } else if (i == R.id.iv_laoren_yule) {
            Routerfit.register(AppRouter.class).skipHealthInquiryActivity();

        } else if (i == R.id.iv_change_account) {
            MobclickAgent.onProfileSignOff();
            Routerfit.register(AppRouter.class).getCallProvider().logout();
            UserSpHelper.setToken("");
            UserSpHelper.setEqId("");
            UserSpHelper.setUserId("");
            Routerfit.register(AppRouter.class).skipAuthActivity();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
//                mChangeAccountDialog = new ChangeAccountDialog(getActivity());
//                mChangeAccountDialog.show();

        } else if (i == R.id.per_image) {
            Routerfit.register(AppRouter.class).skipProfileInfoActivity();

        } else if (i == R.id.iv_record) {
            Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);

        } else if (i == R.id.iv_jiankang_riji) {
            Routerfit.register(AppRouter.class).skipTaskDialyContactActivity();

        } else if (i == R.id.tv_update) {
            Routerfit.register(AppRouter.class).getAppUpdateProvider().checkAppVersion(getContext(), true);

        } else if (i == R.id.doctor_status) {
            if ("未绑定".equals(isSignDoctor.getText().toString())) {
                Routerfit.register(AppRouter.class).skipOnlineDoctorListActivity("contract", "签 约 医 生", "PersonDetailFragment");
                return;
            }
            if ("待审核".equals(isSignDoctor.getText().toString())) {
                Routerfit.register(AppRouter.class).skipCheckContractActivity();
            }

        } else if (i == R.id.iv_alarm) {
            Routerfit.register(AppRouter.class).skipPayActivity();
        }
    }
}