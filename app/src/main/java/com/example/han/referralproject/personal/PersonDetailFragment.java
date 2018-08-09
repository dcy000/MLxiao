package com.example.han.referralproject.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.User;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.dialog.ChangeAccountDialog;
import com.example.han.referralproject.health.HealthDiaryActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.video.VideoListActivity;
import com.gcml.module_health_record.HealthRecordActivity;
import com.google.gson.Gson;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.ml.edu.OldRouter;
import com.squareup.picasso.Picasso;

/**
 * Created by lenovo on 2018/3/12.
 */

public class PersonDetailFragment extends Fragment implements View.OnClickListener {
    public String userId;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mUser = (User) msg.obj;
                    tvUserName.setText(mUser.getBname());
                    break;
                case 1:

                    LocalShared.getInstance(getContext()).setXunfeiID(msg.obj + "");

                    break;
            }
            super.handleMessage(msg);
        }


    };

    User mUser;

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
        ((BaseActivity) getActivity()).speak(getString(R.string.person_info));

        userId = MyApplication.getInstance().userId;
        headImg = view.findViewById(R.id.per_image);
        recreation = view.findViewById(R.id.iv_laoren_yule);
        recreation.setOnClickListener(this);
        tvBalance = view.findViewById(R.id.tv_balance);
        ((BaseActivity) getActivity()).setEnableListeningLoop(false);
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
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {

                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = response.getXfid();
                mHandler.sendMessage(msg);


                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("userName", response.getBname());
                editor.commit();
                MyApplication.getInstance().userName = response.getBname();
                tvUserName.setText(response.getBname());
                //    tvBalance.setText(String.format(getString(R.string.robot_amount), response.getAmount())+"元");
                Picasso.with(getActivity())
                        .load(response.getuser_photo())
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .tag(this)
                        .fit()
                        .into(headImg);


                if ("1".equals(response.getState())) {
                    isSignDoctor.setText("已签约");
                } else if ("0".equals(response.getState()) && (TextUtils.isEmpty(response.getDoctername()))) {

                    isSignDoctor.setText("未签约");

                } else {
                    isSignDoctor.setText("待审核");

                }


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


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

        NetworkApi.DoctorInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<Doctor>() {
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
            case R.id.iv_check://病症自查
                DiseaseUser diseaseUser = new DiseaseUser(
                        LocalShared.getInstance(getActivity()).getUserName(),
                        LocalShared.getInstance(getActivity()).getSex().equals("男") ? 1 : 2,
                        Integer.parseInt(LocalShared.getInstance(getActivity()).getUserAge()) * 12,
                        LocalShared.getInstance(getActivity()).getUserPhoto()
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
                NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
                    @Override
                    public void onSuccess(VersionInfoBean response) {
                        ((BaseActivity) getActivity()).hideLoadingDialog();
                        try {
                            if (response != null && response.vid > getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode) {
                                new UpdateAppManager(getActivity()).showNoticeDialog(response.url);
                            } else {
                                ((BaseActivity) getActivity()).speak("当前已经是最新版本了");
                                Toast.makeText(getActivity(), "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        ((BaseActivity) getActivity()).hideLoadingDialog();
                        ((BaseActivity) getActivity()).speak("当前已经是最新版本了");
                        Toast.makeText(getActivity(), "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
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
