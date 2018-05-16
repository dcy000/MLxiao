package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.WeeklyReport;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.ToastUtils;
import com.example.han.referralproject.util.ToastTool;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by gzq on 2018/3/9.
 */

public class WeeklyReportActivity extends BaseActivity {
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private List<Fragment> fragments;
    private New_WeeklyReport1Fragment new_fragment1;
    private New_WeeklyReport2Fragment new_fragment2;
    private New_WeeklyReport3Fragment new_fragment3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_report);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("上周血压报告");
        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        new_fragment1 = new New_WeeklyReport1Fragment();
        fragments.add(new_fragment1);
        new_fragment2 = new New_WeeklyReport2Fragment();
        fragments.add(new_fragment2);
        new_fragment3 = new New_WeeklyReport3Fragment();
        fragments.add(new_fragment3);

        viewpage.setOffscreenPageLimit(3);

        viewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        circleIndicator.setViewPager(viewpage);
        getData();

    }

    private void getData() {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) - 7);
        long weekAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.WeeklyOrMonthlyReport)
                .params("userId", MyApplication.getInstance().userId)
                .params("endTimeStamp", weekAgoTime)
                .params("num", "1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("请求成功", "onSuccess: " + response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                WeeklyOrMonthlyReport weeklyOrMonthlyReport = new Gson().fromJson(data.toString(), WeeklyOrMonthlyReport.class);
                                new_fragment1.notifyData(weeklyOrMonthlyReport);
                                new_fragment2.notifyData(weeklyOrMonthlyReport);
//                                new_fragment3.notifyData(weeklyOrMonthlyReport);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastTool.showShort("暂无周报告");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e("请求失败", "onError: " + response.message());
                        ToastTool.showShort("暂无周报告");
                    }
                });

    }


}
