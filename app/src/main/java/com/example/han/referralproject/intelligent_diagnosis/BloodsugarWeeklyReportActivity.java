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
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.UserSpHelper;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BloodsugarWeeklyReportActivity extends BaseActivity {
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private List<Fragment> fragments;
    private BloodsugarWeeklyReport1Fragment new_fragment1;
    private BloodsugarWeeklyReport2Fragment new_fragment2;
    private New_WeeklyReport3Fragment new_fragment3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_bloodsugar_report);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("上周血糖报告");
        ButterKnife.bind(this);
        fragments = new ArrayList<>();

        new_fragment1 = new BloodsugarWeeklyReport1Fragment();
        fragments.add(new_fragment1);
        new_fragment2 = new BloodsugarWeeklyReport2Fragment();
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
        curr.set(Calendar.WEEK_OF_YEAR, curr.get(Calendar.WEEK_OF_YEAR) - 1);
        long weekAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.WeeklyOrMonthlyBloodsugar)
                .params("userId", UserSpHelper.getUserId())
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
                                WeeklyOrMonthlyBloodsugarReport weeklyOrMonthlyBloodsugarReport = new Gson().fromJson(data.toString(), WeeklyOrMonthlyBloodsugarReport.class);
                                new_fragment1.notifyData(weeklyOrMonthlyBloodsugarReport);
                                new_fragment2.notifyData(weeklyOrMonthlyBloodsugarReport);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort("暂无周报告");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e("请求失败", "onError: " + response.message());
                        ToastUtils.showShort("暂无周报告");
                    }
                });

    }
}
