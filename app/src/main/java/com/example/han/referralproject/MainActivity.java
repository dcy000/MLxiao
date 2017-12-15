package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.facerecognition.VideoDemo;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.recyclerview.AddAppoActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.video.MainVideoActivity;
import com.example.han.referralproject.video.VideoListActivity;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call.EMUIHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    private Handler mHandler = new Handler();

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isMyServiceRunning(AssistiveTouchService.class)) {

        } else {
            Intent intent = new Intent(getApplicationContext(), AssistiveTouchService.class);
            startService(intent);
        }

        mToolbar.setVisibility(View.GONE);
        mImageView1 = (ImageView) findViewById(R.id.robot_con);
        mImageView2 = (ImageView) findViewById(R.id.person_info);
        mImageView3 = (ImageView) findViewById(R.id.health_test);
        mImageView4 = (ImageView) findViewById(R.id.doctor_ask);
        mImageView5 = (ImageView) findViewById(R.id.health_class);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);


        if (isMyServiceRunning(AssistiveTouchService.class)) {


        } else {

            Intent intent = new Intent(getApplicationContext(), AssistiveTouchService.class);
            startService(intent);

        }


        findViewById(R.id.ll_anim).setOnClickListener(this);
        float pivotX = .5f; // 取自身区域在X轴上的中心点
        float pivotY = .5f; // 取自身区域在Y轴上的中心点
        //    new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); // 围绕自身的中心点进行旋转

        RotateAnimation tranAnimation = new RotateAnimation(-30, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        tranAnimation.setDuration(1000);
        tranAnimation.setRepeatCount(Animation.INFINITE);
        tranAnimation.setRepeatMode(Animation.REVERSE);
        findViewById(R.id.iv_anim).setAnimation(tranAnimation);
        tranAnimation.start();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak(R.string.tips_splash);
            }
        }, 1000);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivitySpeakFinish() {
        super.onActivitySpeakFinish();
        findViewById(R.id.ll_anim).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_anim:
                v.setVisibility(View.GONE);
                break;
            case R.id.robot_con:
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
            case R.id.person_info:
                intent.setClass(getApplicationContext(), PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.health_test://健康监测

                intent.setClass(getApplicationContext(), VideoDemo.class);
                intent.putExtra("sign", "0");
                intent.putExtra("orderid", "0");
                intent.putExtra("from","Test");
//                intent.setClass(getApplicationContext(), Test_mainActivity.class);
                startActivity(intent);
                break;
            case R.id.doctor_ask://医生咨询
//                intent.setClass(getApplicationContext(), MainVideoActivity.class);
//                startActivity(intent);
                if ("".equals(sharedPreferences.getString("name", ""))) {
                    Toast.makeText(getApplicationContext(), "请先查看是否与签约医生签约成功", Toast.LENGTH_SHORT).show();

                } else {
                    intent.setClass(getApplicationContext(), DoctorAskGuideActivity.class);
                    startActivity(intent);
                }

                //    EMUIHelper.callVideo(MyApplication.getInstance(), MyApplication.getInstance().emDoctorId);
                break;
            case R.id.health_class:
                intent.setClass(getApplicationContext(), VideoListActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        //main activity no back
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
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

    public static final String REGEX_GO_PERSONAL_CENTER = ".*(gerenzhongxin|wodeshuju).*";
    public static final String REGEX_GO_CLASS = ".*(jiankangketang|shipin).*";
    public static final String REGEX_SEE_DOCTOR = ".*(yisheng|zixun|kan|yuyue)(zixun|yisheng).*";
    public static final String REGEX_SET_ALARM = ".*(naozhong|tixingwochiyao).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        super.onSpeakListenerResult(result);
        //Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches(REGEX_SET_ALARM)) {
            Intent intent = AlarmList2Activity.newLaunchIntent(this);
            startActivity(intent);
            return;
        }

        if (inSpell.matches(REGEX_SEE_DOCTOR)) {
            mImageView4.performClick();
            return;
        }

        if (inSpell.matches(REGEX_GO_CLASS)) {
            mImageView5.performClick();
            return;
        }

        if (inSpell.matches(REGEX_GO_PERSONAL_CENTER)) {
            mImageView2.performClick();
        }
    }
}
