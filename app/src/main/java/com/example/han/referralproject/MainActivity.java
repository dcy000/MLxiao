package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.facerecognition.VideoDemo;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.video.VideoListActivity;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.alarm.AlarmModel;

import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.call2.NimCallActivity;
import com.medlink.danbogh.signin.SignInActivity;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    private Handler mHandler = new Handler();

    SharedPreferences sharedPreferences;


    private MediaPlayer mediaPlayer;//MediaPlayer对象
    private ImageView mImageView6;
    private ImageView mBatteryIv;
    private BatteryBroadCastReceiver mBatteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     /*   mediaPlayer = MediaPlayer.create(this, R.raw.face_register);

        mediaPlayer.start();//播放音乐*/

        if (isMyServiceRunning(AssistiveTouchService.class))

        {

        } else

        {
            Intent intent = new Intent(getApplicationContext(), AssistiveTouchService.class);
            startService(intent);
        }

        mToolbar.setVisibility(View.GONE);
        mImageView1 = (ImageView) findViewById(R.id.robot_con);

        mImageView2 = (ImageView) findViewById(R.id.person_info);

        mImageView3 = (ImageView) findViewById(R.id.health_test);

        mImageView4 = (ImageView) findViewById(R.id.doctor_ask);

        mImageView5 = (ImageView) findViewById(R.id.health_class);
        mImageView6 = (ImageView) findViewById(R.id.call_family);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);
        mImageView6.setOnClickListener(this);
        mBatteryIv = (ImageView) findViewById(R.id.iv_battery);

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
                //speak(getString(R.string.facc_register));
                speak(R.string.tips_splash);
                // speak(R.string.head_verify);

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
                intent.putExtra("from", "Test");
                intent.putExtra("jump", "1");
                startActivity(intent);

//                startActivity(new Intent(mContext, Test_mainActivity.class));
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
                NimCallActivity.launchNoCheck(this, MyApplication.getInstance().eqid);
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
}
