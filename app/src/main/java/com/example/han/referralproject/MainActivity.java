package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.lib_alarm_clock.AlarmHelper;
import com.example.lib_alarm_clock.bean.ClueInfoBean;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.personal.PersonDetailActivity;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.lib_alarm_clock.service.AlarmAPI;
import com.example.lib_alarm_clock.ui.AlarmList2Activity;
import com.example.module_doctor_advisory.ui.DoctorAskGuideActivity;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.ui.FaceSignInActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.Handlers;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.gzq.lib_core.bean.AlarmModel;
import com.medlink.danbogh.call2.NimCallActivity;
import com.medlink.danbogh.signin.SignInActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;


    private ImageView mImageView6;
    private ImageView mBatteryIv;
    private BatteryBroadCastReceiver mBatteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);

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

        Handlers.ui().postDelayed(new Runnable() {
            @Override
            public void run() {
                MLVoiceSynthetize.startSynthesize(R.string.tips_splash);
            }
        }, 1000);
        if (!isMyServiceRunning(AssistiveTouchService.class)) {
            startService(new Intent(this, AssistiveTouchService.class));
        }

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
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.robot_con:
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
            case R.id.person_info:
                intent.setClass(getApplicationContext(), PersonDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.health_test://健康监测
                vertifyWithFace();
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
                UserInfoBean user = Box.getSessionManager().getUser();
                NimCallActivity.launchNoCheck(this, user.eqid);
                break;
        }
    }

    private void vertifyWithFace() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                List<UserInfoBean> usersFromRoom = Box.getUsersFromRoom();
                if (usersFromRoom == null) {
                    emitter.onError(new Throwable("未检测到您的登录历史，请输入账号和密码登录"));
                    return;
                }
                StringBuilder userIds = new StringBuilder();

                for (UserInfoBean user : usersFromRoom) {
                    userIds.append(user.bid);
                }
                String substring = userIds.substring(0, userIds.length() - 1);
                emitter.onNext(substring);
            }
        }).flatMap(new Function<String, ObservableSource<HttpResult<ArrayList<UserInfoBean>>>>() {
            @Override
            public ObservableSource<HttpResult<ArrayList<UserInfoBean>>> apply(String s) throws Exception {
                return Box.getRetrofit(API.class)
                        .queryAllLocalUsers(s);
            }
        }).compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<ArrayList<UserInfoBean>>() {
                    @Override
                    public void onNext(ArrayList<UserInfoBean> users) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, FaceSignInActivity.class);
                        intent.putExtra("skip", true);
                        intent.putExtra("currentUser", true);
                        intent.putParcelableArrayListExtra("users", users);
                        startActivityForResult(intent, 1001);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showShort(ex.getMessage());
                        ActivityUtils.skipActivity(SignInActivity.class);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            int faceResult = data.getIntExtra(FaceConstants.KEY_AUTH_FACE_RESULT, 0);
            switch (faceResult) {
                case FaceConstants.AUTH_FACE_SUCCESS:
                    ActivityUtils.skipActivity(Test_mainActivity.class);
                    break;
                case FaceConstants.AUTH_FACE_FAIL:

                    break;
                case FaceConstants.AUTH_FACE_SKIP:
                    ActivityUtils.skipActivity(Test_mainActivity.class);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
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
        super.onResume();
        Box.getRetrofit(AlarmAPI.class)
                .getAllAlarmClocks(Box.getUserId())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<ClueInfoBean>>() {
                    @Override
                    public void onNext(List<ClueInfoBean> clueInfoBeans) {
                        if (clueInfoBeans == null || clueInfoBeans.size() == 0) {
                            return;
                        }
                        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
                        //DataSupport.deleteAll(AlarmModel.class);
                        for (ClueInfoBean itemBean : clueInfoBeans) {
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
