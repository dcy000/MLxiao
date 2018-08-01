package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.facerecognition.AuthenticationActivity;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonDetailActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.yisuotang.HealthMallActivity;
import com.example.han.referralproject.yisuotang.MedicalConsultationActivity;
import com.example.han.referralproject.yisuotang.RecreationCenterActivity;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.call2.NimCallActivity;
import com.medlink.danbogh.call2.QianZui;
import com.medlink.danbogh.utils.T;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;


public class MainActivity extends BaseActivity implements View.OnClickListener, HttpListener<AccessToken> {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    private Handler mHandler = new Handler();

    SharedPreferences sharedPreferences;


    private ImageView mImageView6;
    private ImageView mBatteryIv;
    private BatteryBroadCastReceiver mBatteryReceiver;
    private ImageView yule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_yisuo_tang);

        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        mToolbar.setVisibility(View.GONE);
        mImageView1 = (ImageView) findViewById(R.id.robot_con);

        mImageView2 = (ImageView) findViewById(R.id.person_info);

        mImageView3 = (ImageView) findViewById(R.id.health_test);

        mImageView4 = (ImageView) findViewById(R.id.doctor_ask);

        mImageView5 = (ImageView) findViewById(R.id.health_class);
        mImageView6 = (ImageView) findViewById(R.id.call_family);
        yule = (ImageView) findViewById(R.id.yule_center);

        yule.setOnClickListener(this);
        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);
        mImageView6.setOnClickListener(this);
        mBatteryIv = (ImageView) findViewById(R.id.iv_battery);

        sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);
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

        if (!isMyServiceRunning(AssistiveTouchService.class)) {
            startService(new Intent(this, AssistiveTouchService.class));
        }

        initBDAK();

    }

    /**
     * 初始化百度AK
     */
    private void initBDAK() {
        AccessTokenModel model=new AccessTokenModel();
        model.getAccessToken(this);
    }

    @Override
    public void onSuccess(AccessToken data) {
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {
        Log.d("MainActivity", "onError:*********************************获取百度AK失败************************ ");
        T.show("初始化AK失败");
    }

    @Override
    public void onComplete() {

    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
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
                //暂时未用到
                v.setVisibility(View.GONE);
                break;
            case R.id.robot_con:
                //人机对话
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
            case R.id.person_info:
                //个人中心
                intent.setClass(getApplicationContext(), PersonDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.health_test:
                //健康监测==>先进入人脸识别===>识别成功之后进入  体检项目入口
                intent.setClass(getApplicationContext(), AuthenticationActivity.class);
                intent.putExtra("orderid", "0");
                intent.putExtra("from", "Test");
                startActivity(intent);

                //直接进入项目检测入口
//                startActivity(new Intent(mContext, Test_mainActivity.class));
                break;
            case R.id.doctor_ask:
                //医药咨询
                intent.setClass(getApplicationContext(), MedicalConsultationActivity.class);
                startActivity(intent);
                break;
            case R.id.health_class:
                //健康商城
                intent.setClass(getApplicationContext(), HealthMallActivity.class);
                startActivity(intent);
                break;
            case R.id.call_family:
                //呼叫家人
                NimCallActivity.launchNoCheck(this, MyApplication.getInstance().eqid);
                break;
            case R.id.yule_center:
                //娱乐中心
                startActivity(new Intent(this, RecreationCenterActivity.class));
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
        NimAccountHelper.getInstance().login(QianZui.qianZui + MyApplication.getInstance().userId, "123456", null);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
