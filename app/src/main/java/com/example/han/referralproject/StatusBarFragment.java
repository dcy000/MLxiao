package com.example.han.referralproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.homepage.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by afirez on 18-1-26.
 */

public class StatusBarFragment extends Fragment implements
        BatteryHelper.OnBatteryChangeListener,
        BatteryHelper.OnPowerConnectionChangeListener,
        ValueAnimator.AnimatorUpdateListener,
        Runnable, MainActivity.ShowStateBar {

    private static final String TAG = "StatusBarFragment";

    public static void show(FragmentManager fm, int id) {
        StatusBarFragment statusBarFragment = getInstance(fm, id);
        if (statusBarFragment.isHidden()) {
            fm.beginTransaction().show(statusBarFragment).commitNowAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }

    public static StatusBarFragment getInstance(FragmentManager fm, int id) {
        return (StatusBarFragment) getFragment(
                fm, id, StatusBarFragment.TAG);
    }

    public static Fragment getFragment(FragmentManager fm, int id, String tag) {
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new StatusBarFragment();
            fm.beginTransaction()
                    .add(id, fragment, tag)
                    .commitNowAllowingStateLoss();
            fm.executePendingTransactions();
        }
        return fragment;
    }

    public StatusBarFragment() {

    }

    private TextView tvTime;
    private ImageView ivChargingIndicator;
    private TextView tvBatteryPercent;
    private ImageView ivBatteryIndicator;
    private ImageView ivBatteryFullIndicator;

    private BatteryHelper mBatteryHelper;
    private boolean mPowerConnectedCache;
    private boolean mStoppedCache;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BatteryHelper.init(context);
        if (mBatteryHelper == null) {
            mBatteryHelper = new BatteryHelper();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_bar, container, false);
        tvTime = (TextView) view.findViewById(R.id.tv_status_bar_time);
        ivChargingIndicator = (ImageView) view.findViewById(R.id.iv_charging_indicator);
        tvBatteryPercent = (TextView) view.findViewById(R.id.tv_battery_percent);
        ivBatteryIndicator = (ImageView) view.findViewById(R.id.iv_battery_indicator);
        ivBatteryFullIndicator = (ImageView) view.findViewById(R.id.iv_battery_full_indicator);
        initView();
        return view;
    }

    private Drawable mDrawableTint;
    private Drawable mDrawable;

    private void initView() {
        ivChargingIndicator.setVisibility(mPowerConnectedCache ? View.VISIBLE : View.GONE);
        mDrawableTint = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_battery).mutate());
        mDrawable = ivBatteryIndicator.getDrawable();
        ((MainActivity) getActivity()).setShowStateBarListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBatteryHelper != null) {
            mBatteryHelper.setOnBatteryChangeListener(this);
            mBatteryHelper.setOnPowerConnectionChangeListener(this);
            mBatteryHelper.start();
        }
        mStoppedCache = false;
        refreshTime();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBatteryHelper != null) {
            mBatteryHelper.stop();
        }
        mStoppedCache = true;
        uiHandler.removeCallbacksAndMessages(null);
    }

    private void refreshTime() {
        if (!mStoppedCache) {
            long time = System.currentTimeMillis();
            tvTime.setText(formatTime(time));
            uiHandler.postDelayed(this, 20 * 1000);
        }
    }

    @Override
    public void run() {
        refreshTime();
    }

    private Date date;

    private SimpleDateFormat dateFormat;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    private String formatTime(long time) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(time);
        if (dateFormat == null) {
            //上午 12:32 1月26日 周五
            dateFormat = new SimpleDateFormat("a hh:mm yyyy年MM月dd日 E", Locale.CHINA);
        }
        return dateFormat.format(date);
    }

    @Override
    public void onBatteryChanged(int percent) {
        mPercentCache = percent;
        tvBatteryPercent.setText(mPercentCache + "%");
        mDrawable.setLevel(100 * mPercentCache);
    }


    @Override
    public void onBatteryStatusChanged(@BatteryHelper.Status int status) {
        boolean full = status == BatteryHelper.STATUS_FULL;
        ivBatteryFullIndicator.setVisibility(full ? View.VISIBLE : View.GONE);
        switch (status) {
            case BatteryHelper.STATUS_CHARGING:
                DrawableCompat.setTint(mDrawableTint, getResources().getColor(R.color.status_bar_battery_charging));
                break;
            case BatteryHelper.STATUS_LOW:
                DrawableCompat.setTint(mDrawableTint, getResources().getColor(R.color.status_bar_battery_low));
                break;
            case BatteryHelper.STATUS_NORMAL:
                DrawableCompat.setTint(mDrawableTint, getResources().getColor(R.color.status_bar_battery_normal));
                break;
        }
    }

    private int mPercentCache;

    private ValueAnimator animator;

    @Override
    public void onPowerConnectionChanged(boolean connected) {
        mPowerConnectedCache = connected;
        ivChargingIndicator.setVisibility(mPowerConnectedCache ? View.VISIBLE : View.GONE);
        int colorId = mPowerConnectedCache ? R.color.status_bar_battery_charging
                : R.color.status_bar_battery_normal;
        DrawableCompat.setTint(mDrawableTint, getResources().getColor(colorId));
        animateOrNot();
    }

    private void animateOrNot() {
        if (mPercentCache == 100) {
            if (animator != null) {
                animator.cancel();
                animator.removeUpdateListener(this);
                animator = null;
            }
            mDrawable.setLevel(mPercentCache * 100);
            return;
        }

        if (mPowerConnectedCache && !mStoppedCache) {
            if (animator == null) {
                animator = ValueAnimator.ofInt();
                animator.setDuration(1000);
                animator.setRepeatCount(0);
                animator.setStartDelay(1000);
                animator.addUpdateListener(this);
            }
            animator.setIntValues(mPercentCache, 100);
            animator.start();
        } else {
            if (animator != null) {
                animator.cancel();
                animator.removeUpdateListener(this);
                animator = null;
            }
            mDrawable.setLevel(mPercentCache * 100);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Integer percent = (Integer) animation.getAnimatedValue();
        Log.d(TAG, "charging animation: " + percent);
        mDrawable.setLevel(percent * 100);
        if (percent == 100) {
            uiHandler.post(mAnimateOrNotRunnable);
        }
    }

    private Runnable mAnimateOrNotRunnable = new Runnable() {
        @Override
        public void run() {
            animateOrNot();
        }
    };

    @Override
    public void showStateBar(boolean isshow) {
        if (isshow){
            tvTime.setVisibility(View.VISIBLE);
        }else{
            tvTime.setVisibility(View.INVISIBLE);
        }
    }
}
