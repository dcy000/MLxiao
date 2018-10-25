package com.example.han.referralproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.cc.CCFaceRecognitionActions;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.old.auth.personal.PersonDetailActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.gcml.common.utils.display.ToastUtils;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimAccountHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;

@Deprecated
public class MainActivity extends BaseActivity implements View.OnClickListener, HttpListener<AccessToken> {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    private Handler mHandler = new Handler();

    SharedPreferences sharedPreferences;


    private ImageView mImageView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);

     /*   mediaPlayer = MediaPlayer.create(this, R.raw.face_register);

        mediaPlayer.start();//播放音乐*/
        mToolbar.setVisibility(View.GONE);
        mImageView1 = findViewById(R.id.robot_con);

        mImageView2 = findViewById(R.id.person_info);

        mImageView3 = findViewById(R.id.health_test);

        mImageView4 = findViewById(R.id.doctor_ask);

        mImageView5 = findViewById(R.id.health_class);
        mImageView6 = findViewById(R.id.call_family);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);
        mImageView6.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak(getString(R.string.facc_register));
                speak(R.string.tips_splash);
                // speak(R.string.head_verify);

            }
        }, 1000);
        initAToken();

    }

    private void initAToken() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(this);
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

                Bundle bundle = new Bundle();
                bundle.putString("orderid", "0");
                bundle.putString("from", "Test");
                CCFaceRecognitionActions.jump2FaceRecognitionActivity(this,bundle);
                break;
            case R.id.doctor_ask://医生咨询
                intent.setClass(getApplicationContext(), DoctorAskGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.health_class:
//                intent = new Intent(this, HealthIntelligentDetectionActivity.class);
//                startActivity(intent);
//                intent.setClass(getApplicationContext(), MarketActivity.class);
//                startActivityForResult(intent);
                break;
            case R.id.call_family://紧急呼叫家人
                startActivity(new Intent(this, SlowDiseaseManagementActivity.class));
                //呼叫
//                NimCallActivity.launchNoCheck(this, MyApplication.getInstance().eqid);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        //main activity no back
    }

    @Override
    protected void onResume() {
//        String userId = UserSpHelper.getUserId();
//        NimAccountHelper.getInstance().login("user_" + userId, "123456", null);
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
        //Toast.makeText(mContext, result, Toast.LENGTH_SHORT).showShort();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
    }

    @Override
    public void onSuccess(AccessToken data) {
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {
        ToastUtils.showShort("初始化AK失败");
    }

    @Override
    public void onComplete() {

    }

}
