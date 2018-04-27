package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.facerecognition.AuthenticationActivity;
import com.example.han.referralproject.floatball.MyService;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.personal.PersonDetailActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.yunlian.EntertainmentCenterActivity;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.alarm.AlarmModel;

import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.call2.NimCallActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView speak2Robot;
    private ImageView personInfomation;
    private ImageView healthMeasure;
    private ImageView askDoctor;
    private ImageView shoppingMall;
    private Handler mHandler = new Handler();
    private ImageView callFamily;
    private ImageView mBatteryIv;
    private BatteryBroadCastReceiver mBatteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        mToolbar.setVisibility(View.GONE);
        initView();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak(R.string.tips_splash);
            }
        }, 1000);

        if (Build.VERSION.SDK_INT < 23) {
            if (!isMyServiceRunning(AssistiveTouchService.class)) {
                startService(new Intent(this, AssistiveTouchService.class));
            }
        } else {
            MyService.StartMe(this);

        }

    }

    private void initView() {
        speak2Robot = (ImageView) findViewById(R.id.robot_con);
        speak2Robot.setOnClickListener(this);
        personInfomation = (ImageView) findViewById(R.id.person_info);
        personInfomation.setOnClickListener(this);
        healthMeasure = (ImageView) findViewById(R.id.health_test);
        healthMeasure.setOnClickListener(this);
        askDoctor = (ImageView) findViewById(R.id.doctor_ask);
        askDoctor.setOnClickListener(this);
        shoppingMall = (ImageView) findViewById(R.id.health_class);
        shoppingMall.setOnClickListener(this);
        callFamily = (ImageView) findViewById(R.id.call_family);
        callFamily.setOnClickListener(this);
        mBatteryIv = (ImageView) findViewById(R.id.iv_battery);
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
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.robot_con:
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
            case R.id.person_info://个人中心
                intent.setClass(getApplicationContext(), PersonDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.health_test://健康监测
                intent.setClass(getApplicationContext(), AuthenticationActivity.class);
                intent.putExtra("orderid", "0");
                intent.putExtra("from", "Test");
                startActivity(intent);
                break;
            case R.id.doctor_ask://医生咨询
                intent.setClass(getApplicationContext(), DoctorAskGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.health_class:
                intent.setClass(getApplicationContext(), MarketActivity.class);
                startActivity(intent);
                break;
            case R.id.call_family://紧急呼叫家人
                //呼叫
//                NimCallActivity.launchNoCheck(this, MyApplication.getInstance().eqid);
                startActivity(new Intent(this, EntertainmentCenterActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBatteryReceiver = new BatteryBroadCastReceiver();
        registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBatteryReceiver);
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
            askDoctor.performClick();
            return;
        }

        if (inSpell.matches(REGEX_GO_CLASS)) {
            shoppingMall.performClick();
            return;
        }

        if (inSpell.matches(REGEX_GO_PERSONAL_CENTER)) {
            personInfomation.performClick();
        }
    }

    public class BatteryBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
                int powerValue = (level * 100) / scale;
                if (powerValue < 10) {
                    mBatteryIv.setImageResource(R.drawable.battery_0);
                } else if (powerValue < 20) {
                    mBatteryIv.setImageResource(R.drawable.battery_1);
                } else if (powerValue < 80) {
                    mBatteryIv.setImageResource(R.drawable.battery_2);
                } else {
                    mBatteryIv.setImageResource(R.drawable.battery_3);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
    }

}
