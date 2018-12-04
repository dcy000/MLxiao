package com.example.han.referralproject.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.dialog.ChangeAccountDialog;
import com.example.han.referralproject.health.HealthDiaryActivity;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.lib_alarm_clock.ui.AlarmList2Activity;
import com.example.module_doctor_advisory.bean.Doctor;
import com.example.module_doctor_advisory.bean.RobotAmount;
import com.example.module_doctor_advisory.service.DoctorAPI;
import com.example.module_doctor_advisory.ui.CheckContractActivity;
import com.example.module_doctor_advisory.ui.OnlineDoctorListActivity;
import com.gcml.module_health_record.HealthRecordActivity;
import com.google.gson.Gson;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.AppUtils;
import com.gzq.lib_core.utils.DeviceUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.ml.edu.OldRouter;

import io.reactivex.functions.Action;

/**
 * Created by lenovo on 2018/3/12.
 */

public class PersonDetailFragment extends Fragment implements View.OnClickListener {


    public TextView tvUserName;
    public ImageView headImg;
    public ImageView mIvAlarm;

    public TextView signDoctorName;
    public TextView tvBalance;

    public TextView isSignDoctor;
    public ImageView recreation;

    public ImageView yuLe;

    public ImageView education;


    private ChangeAccountDialog mChangeAccountDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_person, container, false);
        MLVoiceSynthetize.startSynthesize(R.string.person_info);

        headImg = (ImageView) view.findViewById(R.id.per_image);
        recreation = (ImageView) view.findViewById(R.id.iv_laoren_yule);
        recreation.setOnClickListener(this);
        tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        view.findViewById(R.id.main_iv_health_class).setOnClickListener(this);
        isSignDoctor = (TextView) view.findViewById(R.id.doctor_status);
        isSignDoctor.setOnClickListener(this);
        education = (ImageView) view.findViewById(R.id.iv_youjiao_wenyu);
        education.setOnClickListener(this);
        yuLe = (ImageView) view.findViewById(R.id.main_iv_old);
        yuLe.setOnClickListener(this);
        view.findViewById(R.id.iv_message).setOnClickListener(this);
        view.findViewById(R.id.iv_check).setOnClickListener(this);
        view.findViewById(R.id.view_wifi).setOnClickListener(this);
        view.findViewById(R.id.iv_record).setOnClickListener(this);
        view.findViewById(R.id.iv_jiankang_riji).setOnClickListener(this);
        tvUserName = (TextView) view.findViewById(R.id.per_name);
        view.findViewById(R.id.iv_change_account).setOnClickListener(this);
        headImg.setOnClickListener(this);
        view.findViewById(R.id.tv_update).setOnClickListener(this);
        mIvAlarm = (ImageView) view.findViewById(R.id.iv_alarm);
        mIvAlarm.setOnClickListener(this);
        signDoctorName = (TextView) view.findViewById(R.id.doctor_name);
        ((TextView) view.findViewById(R.id.tv_update)).setText("检查更新 v" + Utils.getLocalVersionName(getActivity()));
        getActivity().registerReceiver(mReceiver, new IntentFilter("change_account"));
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
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
        getActivity().unregisterReceiver(mReceiver);
    }


    private void getData() {
        UserInfoBean user = Box.getSessionManager().getUser();

        tvUserName.setText(user.bname);

        Glide.with(Box.getApp())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder))
                .load(user.userPhoto)
                .into(headImg);
        if ("1".equals(user.state)) {
            isSignDoctor.setText("已签约");
        } else if ("0".equals(user.state)&& !TextUtils.isEmpty(user.doid)) {
            isSignDoctor.setText("待审核");
        } else {
            isSignDoctor.setText("未签约");
        }
        if (TextUtils.isEmpty(user.doid)) {
            signDoctorName.setText("暂无");
        } else {
            Box.getRetrofit(DoctorAPI.class)
                    .queryDoctorInfo(user.doid)
                    .compose(RxUtils.httpResponseTransformer())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new CommonObserver<Doctor>() {
                        @Override
                        public void onNext(Doctor doctor) {
                            if (!"".equals(doctor.getDoctername())) {
                                signDoctorName.setText(doctor.getDoctername());

                            }
                        }
                    });
        }

        Box.getRetrofit(DoctorAPI.class)
                .queryMoneyById(DeviceUtils.getIMEI())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<RobotAmount>() {
                    @Override
                    public void onNext(RobotAmount robotAmount) {
                        if (robotAmount.getAmount() != null) {
                            tvBalance.setText(String.format(getString(R.string.robot_amount), robotAmount.getAmount()));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_check://病症自查
                UserInfoBean user = Box.getSessionManager().getUser();
                DiseaseUser diseaseUser = new DiseaseUser(
                        user.bname,
                        user.sex.equals("男") ? 1 : 2,
                        Integer.parseInt(user.age) * 12,
                        user.userPhoto
                );
                String currentUser = new Gson().toJson(diseaseUser);
                Intent intent = new Intent(getActivity(), com.witspring.unitbody.ChooseMemberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
                break;
            case R.id.iv_message:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.iv_laoren_yule:
                OldRouter.routeToOldHomeActivity(getActivity());
                break;
            case R.id.iv_change_account:
                mChangeAccountDialog = new ChangeAccountDialog(getActivity());
                mChangeAccountDialog.show();
                break;
            case R.id.per_image:
                startActivity(new Intent(getActivity(), MyBaseDataActivity.class));
                break;
            case R.id.iv_record:
                startActivity(new Intent(getActivity(), HealthRecordActivity.class));
                break;
            case R.id.iv_jiankang_riji:
                startActivity(new Intent(getActivity(), HealthDiaryActivity.class));
                break;
            case R.id.tv_update:
                ((BaseActivity) getActivity()).showLoadingDialog("检查更新中");
                Box.getRetrofit(API.class)
                        .getAppVersion(AppUtils.getMeta("com.gcml.version") + "")
                        .compose(RxUtils.httpResponseTransformer())
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                ((BaseActivity) getActivity()).hideLoadingDialog();
                            }
                        })
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new CommonObserver<VersionInfoBean>() {
                            @Override
                            public void onNext(VersionInfoBean versionInfoBean) {
                                try {
                                    if (versionInfoBean != null && versionInfoBean.vid > AppUtils.getAppInfo().getVersionCode()) {
                                        new UpdateAppManager(getContext()).showNoticeDialog(versionInfoBean.url);
                                    } else {
                                        MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                                        ToastUtils.showShort("当前已经是最新版本了");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onError(ApiException ex) {
                                MLVoiceSynthetize.startSynthesize("当前已经是最新版本了");
                                ToastUtils.showShort("当前已经是最新版本了");
                            }
                        });
                break;
            case R.id.main_iv_health_class:
                startActivity(new Intent(getActivity(), VideoListActivity.class));
                break;
            case R.id.doctor_status:
                if ("未签约".equals(isSignDoctor.getText())) {
                    Intent intentStatus = new Intent(getActivity(), OnlineDoctorListActivity.class);
                    intentStatus.putExtra("flag", "contract");
                    startActivity(intentStatus);
                    return;
                }
                if ("待审核".equals(isSignDoctor.getText())) {
                    Intent intentStatus = new Intent(getActivity(), CheckContractActivity.class);
                    startActivity(intentStatus);
                }
                break;
            case R.id.iv_youjiao_wenyu:
                Intent intentYoujiao = new Intent(getActivity(), ChildEduHomeActivity.class);
                startActivity(intentYoujiao);
                break;
            case R.id.main_iv_old:
                OldRouter.routeToOldHomeActivity(getActivity());
                break;
            case R.id.iv_alarm:
                Intent intentAlarm = AlarmList2Activity.newLaunchIntent(getActivity());
                startActivity(intentAlarm);
                break;
        }
    }
}
