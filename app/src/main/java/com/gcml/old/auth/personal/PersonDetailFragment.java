package com.gcml.old.auth.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;

import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.Utils;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_health_record.HealthRecordActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2018/3/12.
 */

public class PersonDetailFragment extends Fragment implements View.OnClickListener {
    public String userId;

    public TextView tvUserName;
    public ImageView headImg;
    public ImageView mIvAlarm;

    SharedPreferences sharedPreferences;
    public TextView signDoctorName;
    public TextView tvBalance;

    public TextView isSignDoctor;
    public ImageView recreation;

    public ImageView yuLe;

    public ImageView education;


    private ChangeAccountDialog mChangeAccountDialog;

    SharedPreferences sharedPreferences1;

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
        sharedPreferences = getActivity().getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);
        sharedPreferences1 = getActivity().getSharedPreferences(ConstantData.PERSON_MSG, Context.MODE_PRIVATE);
        signDoctorName = view.findViewById(R.id.doctor_name);
        ((TextView) view.findViewById(R.id.tv_update)).setText("检查更新 v" + Utils.getLocalVersionName(getActivity()));
        getActivity().registerReceiver(mReceiver, new IntentFilter("change_account"));
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

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "change_account":
                    if (mChangeAccountDialog != null) {
                        mChangeAccountDialog.dismiss();
                    }
                    getData();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoading();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.unregisterReceiver(mReceiver);
        }
    }


    private void getData() {
        boolean empty = TextUtils.isEmpty(UserSpHelper.getUserId());
        if (empty) {
            String message = "请重新登录！";
            ToastUtils.showShort(message);
            CC.obtainBuilder("com.gcml.auth")
                    .build()
                    .callAsync();
        } else {
            getApiData();
        }
    }

    private void getApiData() {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        Observable<UserEntity> rxUsers = CC.obtainBuilder("com.gcml.auth.getUser")
                .build()
                .call()
                .getDataItem("data");
        rxUsers.subscribeOn(Schedulers.io())
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
                        if ("1".equals(user.state)) {
                            isSignDoctor.setText("已签约");
                        } else if ("0".equals(user.state)
                                && (TextUtils.isEmpty(user.doctorId))) {
                            isSignDoctor.setText("未签约");
                        } else {
                            isSignDoctor.setText("待审核");
                        }
                    }
                });

        NetworkApi.Person_Amount(Utils.getDeviceId(), new NetworkManager.SuccessCallback<RobotAmount>() {
            @Override
            public void onSuccess(final RobotAmount response) {
                if (response.getAmount() != null) {
                    tvBalance.setText(String.format(getString(R.string.robot_amount), response.getAmount()));
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });

        NetworkApi.DoctorInfo(UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<Doctor>() {
            @Override
            public void onSuccess(Doctor response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("doctor_id", response.getDocterid() + "");
                editor.putString("name", response.getDoctername());
                editor.putString("position", response.getDuty());
                editor.putString("feature", response.getDepartment());
                editor.putString("hospital", response.getHosname());
                editor.putString("service_amount", response.getService_amount());
                editor.putString("docter_photo", response.getDocter_photo());
                editor.commit();
                if (!"".equals(response.getDoctername())) {
                    signDoctorName.setText(response.getDoctername());
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                signDoctorName.setText("暂无");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_order:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.iv_pay:
                startActivity(new Intent(getActivity(), PayActivity.class));
//                CC.obtainBuilder("com.gcml.mall.recharge").build().callAsync();
                break;
            case R.id.iv_message:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.iv_shezhi:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.iv_laoren_yule:
                CC.obtainBuilder("health_measure")
                        .setActionName("To_HealthInquiryActivity")
                        .build()
                        .call();
//                OldRouter.routeToOldHomeActivity(getActivity());
                break;
            case R.id.iv_change_account:
                mChangeAccountDialog = new ChangeAccountDialog(getActivity());
                mChangeAccountDialog.show();
                break;
            case R.id.per_image:
                CC.obtainBuilder("com.gcml.auth.profileInfo").build().callAsync();
                break;
            case R.id.iv_record:
                startActivity(new Intent(getActivity(), HealthRecordActivity.class));
                break;
            case R.id.iv_jiankang_riji:
                CC.obtainBuilder("app.component.task.diary.contact").build().callAsync();
                break;
            case R.id.tv_update:
                showLoading("检查更新中");
                NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
                    @Override
                    public void onSuccess(VersionInfoBean response) {
                        dismissLoading();
                        if (getActivity() == null) {
                            return;
                        }
                        try {
                            if (response != null && response.vid > getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode) {
                                new UpdateAppManager(getActivity()).showNoticeDialog(response.url);
                            } else {
                                MLVoiceSynthetize.startSynthesize(getActivity().getApplicationContext(),
                                        "当前已经是最新版本了");
                                ToastUtils.showShort("当前已经是最新版本了");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        dismissLoading();
                        if (getActivity() == null) {
                            return;
                        }
                        MLVoiceSynthetize.startSynthesize(getActivity().getApplicationContext(),
                                "当前已经是最新版本了");
                        ToastUtils.showShort("当前已经是最新版本了");
                    }
                });
                break;
            case R.id.doctor_status:
                if ("未签约".equals(isSignDoctor.getText().toString())) {
                    Intent intentStatus = new Intent(getActivity(), OnlineDoctorListActivity.class);
                    intentStatus.putExtra("flag", "contract");
                    startActivity(intentStatus);
                    return;
                }
                if ("待审核".equals(isSignDoctor.getText().toString())) {
                    Intent intentStatus = new Intent(getActivity(), CheckContractActivity.class);
                    startActivity(intentStatus);
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mChangeAccountDialog != null) {
            mChangeAccountDialog.cancel();
        }
    }

    private LoadingDialog mLoadingDialog;

    private void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        if (getActivity() == null) {
            return;
        }
        mLoadingDialog = new LoadingDialog.Builder(getActivity())
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}
