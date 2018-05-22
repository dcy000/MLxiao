package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonDetail2Fragment;
import com.example.han.referralproject.settting.EventType;
import com.example.han.referralproject.settting.dialog.ClearCacheOrResetDialog;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.adpater.MainFragmentAdapter;
import com.example.han.referralproject.yiyuan.fragment.Main1Fragment;
import com.example.han.referralproject.yiyuan.fragment.Main2Fragment;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.internal.operators.parallel.ParallelRunOn;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fl_status_bar)
    FrameLayout flStatusBar;
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

        Looper.myQueue().addIdleHandler(idleHandler);
    }

    private volatile long mStartTime = -1;
    private ClearCacheOrResetDialog dialog;
    private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        private HandlerThread mHandlerThread;

        {
            mHandlerThread = new HandlerThread("bg");
            mHandlerThread.start();
        }

        public Handler mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 5:
                        showDialog(EventType.exit);
                        break;
                    case 10:
                        tuichu();
                        break;
                }
            }
        };


        @Override
        public boolean queueIdle() {
            long theTime = System.currentTimeMillis();
            Log.d("idleHandler", "queueIdle: " + theTime);
            if (mStartTime == -1) {
                mStartTime = theTime;
                mHandler.sendEmptyMessageDelayed(5, 5000);
                mHandler.sendEmptyMessageDelayed(10, 10000);
            } else {
                long duration = theTime - mStartTime;
                Log.d("duration", "durationTime:" + duration);
                if (duration < 5000) {
                    mHandler.removeMessages(5);
                    mHandler.removeMessages(10);
                } else if (duration < 10000) {
//                    mHandler.removeMessages(10);
                }
                mStartTime = -1;
            }
            return true;
        }


        private void showDialog(EventType type) {
            if (dialog == null) {
                dialog = new ClearCacheOrResetDialog(type);
            }
            dialog.setListener(new ClearCacheOrResetDialog.OnDialogClickListener() {
                @Override
                public void onClickConfirm(EventType type) {

                }

                @Override
                public void onClickCancel() {
                    mHandler.removeMessages(10);
                }
            });
            dialog.show(getFragmentManager(), "dialog");
        }


    };

    private void tuichu() {
        dialog.dismiss();
        Main2Fragment fragment = (Main2Fragment) fragments.get(1);
        fragment.tuiChu();
    }


    private void initView() {
        fragments.add(new Main1Fragment());
        fragments.add(new Main2Fragment());
        vp.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), fragments));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
    }

}
