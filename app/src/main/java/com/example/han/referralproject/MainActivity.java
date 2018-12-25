package com.example.han.referralproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.login.ChoiceLoginTypeActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.adpater.MainFragmentAdapter;
import com.example.han.referralproject.yiyuan.fragment.CountdownDialog;
import com.example.han.referralproject.yiyuan.fragment.Main1Fragment;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.utils.T;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;

public class MainActivity extends BaseActivity implements CountdownDialog.Ontouch, HttpListener<AccessToken> {
    @BindView(R.id.fl_status_bar)
    FrameLayout flStatusBar;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private Handler mHandler = new Handler();
    List<Fragment> fragments = new ArrayList<>();
    @BindView(R.id.vp)
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        ButterKnife.bind(this);
        initEvent();
        initView();
        initBDAK();
    }

    private void initBDAK() {
        AccessTokenModel model = new AccessTokenModel();
        model.getAccessToken(this);
    }

    @Override
    public void onSuccess(AccessToken data) {
        Log.d("MainActivity", "onError:******获取百度AK成功******* ");
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {
        Log.d("MainActivity", "onError:******获取百度AK失败******* ");
        T.show("初始化AK失败");
    }

    @Override
    public void onComplete() {

    }

    private void initEvent() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak(R.string.tips_splash);
            }
        }, 1000);

        if (!isMyServiceRunning(AssistiveTouchService.class)) {
            startService(new Intent(this, AssistiveTouchService.class));
        }

//        Looper.myQueue().addIdleHandler(YiYuanIdleHandler.getInstance());
    }

    private void initView() {
        fragments.add(new Main1Fragment());
//        fragments.add(new Main2Fragment());
        vp.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), fragments));
        circleIndicator.setViewPager(vp);
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
    protected void onResume() {
//        NimAccountHelper.getInstance().login("gcmlylb_user_" + MyApplication.getInstance().userId, "123456", null);
        getUserYxAcountId();
        setEnableListeningLoop(false);
        super.onResume();
        NetworkApi.clueNotify(new NetworkManager.SuccessCallback<ArrayList<ClueInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<ClueInfoBean> response) {
                if (response == null || response.size() == 0) {
                    return;
                }
                List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
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

    private void getUserYxAcountId() {
        NetworkApi.getUserYxAcountId(MyApplication.getInstance().userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject result = new JSONObject(body);
                    boolean tag = result.optBoolean("tag");
                    if (tag) {
                        String account = result.getString("data");
                        NimAccountHelper.getInstance().login(account, "123456", null);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            showDialog();
        }
    };

    public void startAD() {
        handlerYiYuan.removeCallbacks(runnable);
        handlerYiYuan.postDelayed(runnable, time);
    }

    private Handler handlerYiYuan = new Handler();
    private long time = 1000 * 10;

    private void showDialog() {
        if (dialog == null) {
            dialog = new CountdownDialog();
        }
        dialog.setOntouch(this);
        dialog.show(getFragmentManager(), "tuichu");
    }

    @Override
    public void OnTouch() {
        if (dialog == null) {
            dialog.dismiss();
        }

    }

    @Override
    public void OnTime() {
        tuichu();
    }

    private void tuichu() {
        MobclickAgent.onProfileSignOff();
        NimAccountHelper.getInstance().logout();
        LocalShared.getInstance(MyApplication.getCurrentActivity()).loginOut();

        Activity currentActivity = MyApplication.getCurrentActivity();
        Intent intent = new Intent(currentActivity, ChoiceLoginTypeActivity.class);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }

    private CountdownDialog dialog;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                handlerYiYuan.removeCallbacks(runnable);
//                break;
//            case MotionEvent.ACTION_UP:
//                startAD();
//                break;
        }
        return super.onTouchEvent(event);
    }
}
