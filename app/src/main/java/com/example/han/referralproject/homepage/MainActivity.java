package com.example.han.referralproject.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.StatusBarFragment;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.lib_utils.thread.ThreadUtils;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimAccountHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 16:24
 * created by:gzq
 * description:新的主界面
 */
public class MainActivity extends BaseActivity {

    private ViewPager mViewpage;
    private LinearLayout mNewmainBottomIndicator;
    private View mIndicatorLeft;
    private View mIndicatorRight;
    private List<Fragment> fragments;
    private NewMain1Fragment newMain1Fragment;
    private NewMain2Fragment newMain2Fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        initView();
        initFragments();
        initViewpage();

    }

    private void initViewpage() {
        mViewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIndicatorLeft.setVisibility(View.VISIBLE);
                    mIndicatorRight.setVisibility(View.INVISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(false);
                    }
                } else if (position == 1) {
                    mIndicatorLeft.setVisibility(View.INVISIBLE);
                    mIndicatorRight.setVisibility(View.VISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        newMain1Fragment = new NewMain1Fragment();
        newMain2Fragment = new NewMain2Fragment();
        fragments.add(newMain1Fragment);
        fragments.add(newMain2Fragment);
    }

    private void initView() {
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
        mNewmainBottomIndicator = (LinearLayout) findViewById(R.id.newmain_bottom_indicator);
        mIndicatorLeft = (View) findViewById(R.id.indicator_left);
        mIndicatorRight = (View) findViewById(R.id.indicator_right);
//        speak(R.string.tips_splash);
    }

    @Override
    protected void onResume() {
        setEnableListeningLoop(false);
        super.onResume();
//        NimAccountHelper.getInstance().login("user_" +
//                MyApplication.getInstance().userId, "123456", null);
//        NetworkApi.clueNotify(new NetworkManager.SuccessCallback<ArrayList<ClueInfoBean>>() {
//            @Override
//            public void onSuccess(ArrayList<ClueInfoBean> response) {
//                if (response == null || response.size() == 0) {
//                    return;
//                }
//                List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
//                //DataSupport.deleteAll(AlarmModel.class);
//                for (ClueInfoBean itemBean : response) {
//                    String[] timeString = itemBean.cluetime.split(":");
//                    boolean isSetted = false;
//                    for (AlarmModel itemModel : models) {
//                        if (itemModel.getHourOfDay() == Integer.valueOf(timeString[0])
//                                && itemModel.getMinute() == Integer.valueOf(timeString[1])
//                                && itemModel.getContent() != null
//                                && itemModel.getContent().equals(itemBean.medicine)) {
//                            isSetted = true;
//                            break;
//                        }
//                    }
//                    if (!isSetted) {
//                        AlarmHelper.setupAlarm(mContext, Integer.valueOf(timeString[0]),
//                                Integer.valueOf(timeString[1]), itemBean.medicine);
//                    }
//                }
//            }
//        });
    }

    private ShowStateBar showStateBar;

    public void setShowStateBarListener(ShowStateBar showStateBar) {
        this.showStateBar = showStateBar;
    }

    public interface ShowStateBar {
        void showStateBar(boolean isshow);
    }
}