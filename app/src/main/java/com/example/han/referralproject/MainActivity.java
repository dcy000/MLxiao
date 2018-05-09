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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chenenyu.router.annotation.Route;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.facerecognition.AuthenticationActivity;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.personal.PersonDetailActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.alarm.AlarmModel;

import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.call2.NimCallActivity;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

@Route("app")
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Handler mHandler = new Handler();
    private ImageView mBatteryIv;
    private BatteryBroadCastReceiver mBatteryReceiver;
    private LocationClient mLocationClient;
    private String phoneNum = "";
    private String userName = "";
    private String sex = "";
    private String userAge = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        mToolbar.setVisibility(View.GONE);

        findViewById(R.id.robot_con).setOnClickListener(this);
        findViewById(R.id.person_info).setOnClickListener(this);
        findViewById(R.id.health_test).setOnClickListener(this);
        findViewById(R.id.doctor_ask).setOnClickListener(this);
        findViewById(R.id.health_class).setOnClickListener(this);
        findViewById(R.id.call_family).setOnClickListener(this);
        mBatteryIv = (ImageView) findViewById(R.id.iv_battery);
        findViewById(R.id.ll_anim).setOnClickListener(this);
        findViewById(R.id.call_120).setOnClickListener(this);
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
            Intent intent = new Intent(getApplicationContext(), AssistiveTouchService.class);
            startService(intent);
        }
        phoneNum = LocalShared.getInstance(MyApplication.getInstance()).getPhoneNum();
        userName = LocalShared.getInstance(MyApplication.getInstance()).getUserName();
        sex = LocalShared.getInstance(MyApplication.getInstance()).getSex();
        userAge = LocalShared.getInstance(MyApplication.getInstance()).getUserAge();
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
                v.setVisibility(View.GONE);
                break;
            case R.id.robot_con:
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
            case R.id.person_info:
                intent.setClass(getApplicationContext(), PersonDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.health_test://健康监测

                intent.setClass(getApplicationContext(), AuthenticationActivity.class);
                intent.putExtra("orderid", "0");
                intent.putExtra("from", "Test");
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
//                NetworkApi.PersonInfo(MyApplication.getInstance().eqid, new NetworkManager.SuccessCallback<UserInfo>() {
//                    @Override
//                    public void onSuccess(UserInfo response) {
//                        if (isFinishing() || isDestroyed()) {
//                            return;
//                        }
//                        NetworkApi.postTelMessage(response.tel, MyApplication.getInstance().userName, new NetworkManager.SuccessCallback<Object>() {
//                            @Override
//                            public void onSuccess(Object response) {
//
//                            }
//                        }, new NetworkManager.FailedCallback() {
//                            @Override
//                            public void onFailed(String message) {
//
//                            }
//                        });
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//
//                    }
//                });
                break;
            case R.id.call_120:
                initLocation();
                startLocation();
                break;
        }
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locOption.setCoorType("bd09ll");
        locOption.setIsNeedAddress(true);
        locOption.setOpenGps(true);
        locOption.setScanSpan(3000);
        //onStop()的时候杀死定位进程
        locOption.setIgnoreKillProcess(false);
        //不许收集崩溃信息
        locOption.SetIgnoreCacheException(false);
        mLocationClient.setLocOption(locOption);
    }

    private void startLocation() {
        if (mListener != null && mLocationClient != null) {
            mLocationClient.registerLocationListener(mListener);
        }
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mListener != null && mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mListener);
        }
    }

    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            stopLocation();
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String county = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String streetNumber = bdLocation.getStreetNumber();
            double latitude = bdLocation.getLatitude();//纬度
            double longitude = bdLocation.getLongitude();//经度
            String finAddress = province + city + street + streetNumber + "经度：" + longitude + "纬度：" + latitude;
            OkGo.<String>post(NetworkApi.EmergencyCall120)
                    .params("ak", "D10B99198B944BCC")
                    .params("telephone", phoneNum)
                    .params("alarmaddress", finAddress)
                    .params("alongitude", longitude)
                    .params("alatitude", latitude)
                    .params("name", userName)
                    .params("sex", sex.equals("0") ? "女" : "男")
                    .params("age", userAge)
                    .params("address", "")
                    .params("longitude", "")
                    .params("latitude", "")
                    .params("linkperson1", "")
                    .params("linktelephone1", "")
                    .params("linkperson2", "")
                    .params("linktelephone2", "")
                    .params("height", "")
                    .params("weight", "")
                    .params("blood", "")
                    .params("medicalhistory", "")
                    .params("allergy", "")
                    .params("drugs", "")
                    .params("hospital", "")
                    .params("carno1", "")
                    .params("carno2", "")
                    .params("carno3", "")
                    .params("remark", "")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            ToastTool.showLong("呼叫成功");
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Log.e("MainActivity", "onError: " + response.message());
                        }
                    });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

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
    protected void onPause() {
        super.onPause();
        stopLocation();
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
            findViewById(R.id.doctor_ask).performClick();
            return;
        }

        if (inSpell.matches(REGEX_GO_CLASS)) {
            findViewById(R.id.health_class).performClick();
            return;
        }

        if (inSpell.matches(REGEX_GO_PERSONAL_CENTER)) {
            findViewById(R.id.person_info).performClick();
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
