package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.facerecognition.AuthenticationActivity;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.video.VideoListActivity;
import com.google.gson.Gson;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimAccountHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivRobotDialog;
    private ImageView ivPersonalCenter;
    private ImageView ivHealthTesting;
    private ImageView ivContractService;
    private ImageView ivHealthKnowledge;
    private ImageView ivGuidanceRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hq_activity_main);
        mToolbar.setVisibility(View.GONE);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.hq_main_fl_status_bar);
        ivRobotDialog = (ImageView) findViewById(R.id.hq_main_iv_robot_dialog);
        ivPersonalCenter = (ImageView) findViewById(R.id.hq_main_iv_personal_center);
        ivHealthTesting = (ImageView) findViewById(R.id.hq_main_iv_health_testing);
        ivContractService = (ImageView) findViewById(R.id.hq_main_iv_contract_service);
        ivHealthKnowledge = (ImageView) findViewById(R.id.hq_main_iv_health_knowledge);
        ivGuidanceRegistration = (ImageView) findViewById(R.id.hq_main_iv_guidance_registration);
        ivRobotDialog.setOnClickListener(this);
        ivPersonalCenter.setOnClickListener(this);
        ivHealthTesting.setOnClickListener(this);
        ivContractService.setOnClickListener(this);
        ivHealthKnowledge.setOnClickListener(this);
        ivGuidanceRegistration.setOnClickListener(this);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                speak(R.string.tips_splash);
            }
        }, 1000);
        if (!isMyServiceRunning(AssistiveTouchService.class)) {
            Intent intent = new Intent(getApplicationContext(), AssistiveTouchService.class);
            startService(intent);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.hq_main_iv_robot_dialog:
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
            case R.id.hq_main_iv_personal_center:
                intent.setClass(getApplicationContext(), PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.hq_main_iv_health_testing:
                intent.setClass(getApplicationContext(), AuthenticationActivity.class);
                intent.putExtra("orderid", "0");
                intent.putExtra("from", "Test");
                startActivity(intent);
                break;
            case R.id.hq_main_iv_contract_service:
                intent.setClass(getApplicationContext(), DoctorAskGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.hq_main_iv_health_knowledge:
                intent.setClass(getApplicationContext(), VideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.hq_main_iv_guidance_registration:
                // 导诊
                DiseaseUser diseaseUser = new DiseaseUser(
                        LocalShared.getInstance(this).getUserName(),
                        LocalShared.getInstance(this).getSex().equals("男") ? 1 : 2,
                        Integer.parseInt(LocalShared.getInstance(this).getUserAge()) * 12,
                        LocalShared.getInstance(this).getUserPhoto()
                );
                String currentUser = new Gson().toJson(diseaseUser);
                intent.setClass(this, com.witspring.unitbody.ChooseMemberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        NimAccountHelper.getInstance().login("user_" + MyApplication.getInstance().userId, "123456", null);
        setEnableListeningLoop(false);
        super.onResume();
        NetworkApi.clueNotify(new NetworkManager.SuccessCallback<ArrayList<ClueInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<ClueInfoBean> response) {
                if (response == null || response.size() == 0) {
                    return;
                }
                List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
                //DataSupport.deleteAll(AlarmModel.class);
                for (ClueInfoBean itemBean : response) {
                    String[] timeString = itemBean.cluetime.split(":");
                    boolean isSetted = false;
                    for (AlarmModel itemModel : models) {
                        if (itemModel.getHourOfDay() == Integer.valueOf(timeString[0])
                                && itemModel.getMinute() == Integer.valueOf(timeString[1])
                                && itemModel.getContent() != null
                                && itemModel.getContent().equals(itemBean.medicine)) {
                            isSetted = true;
                            break;
                        }
                    }
                    if (!isSetted) {
                        AlarmHelper.setupAlarm(mContext, Integer.valueOf(timeString[0]), Integer.valueOf(timeString[1]), itemBean.medicine);
                    }
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //main activity no back
    }
}
