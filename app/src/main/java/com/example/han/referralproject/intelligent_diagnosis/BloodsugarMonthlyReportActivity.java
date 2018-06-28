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
import com.example.han.referralproject.util.ToastTool;
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
 * Created by Administrator on 2018/5/16.
 */

public class BloodsugarMonthlyReportActivity extends BaseActivity{
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private List<Fragment> fragments;
    private BloodsugarMonthlyReport1Fragment bloodsugarMonthlyReport1Fragment;
    private BloodsugarMonthlyReport2Fragment bloodsugarMonthlyReport2Fragment;
    private New_MonthlyReport3Fragment bloodsugarMonthlyReport3Fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodsugar_monthly_report);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("单疗程治疗计划");
        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<>();

        bloodsugarMonthlyReport1Fragment = new BloodsugarMonthlyReport1Fragment();
        fragments.add(bloodsugarMonthlyReport1Fragment);


        bloodsugarMonthlyReport2Fragment = new BloodsugarMonthlyReport2Fragment();
        fragments.add(bloodsugarMonthlyReport2Fragment);

        bloodsugarMonthlyReport3Fragment=new New_MonthlyReport3Fragment();
        fragments.add(bloodsugarMonthlyReport3Fragment);

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
        long monthAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.WeeklyOrMonthlyBloodsugar)
                .params("userId", MyApplication.getInstance().userId)
                .params("endTimeStamp", monthAgoTime)
                .params("num", "4")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("血糖月报告请求成功", "onSuccess: " + response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                WeeklyOrMonthlyBloodsugarReport weeklyOrMonthlyReport = new Gson().fromJson(data.toString(), WeeklyOrMonthlyBloodsugarReport.class);
                                bloodsugarMonthlyReport1Fragment.notifyData(weeklyOrMonthlyReport);
                                bloodsugarMonthlyReport2Fragment.notifyData(weeklyOrMonthlyReport);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastTool.showShort("暂无疗程报告");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e("请求失败", "onError: " + response.message());
                        ToastTool.showShort("暂无疗程报告");
                    }
                });
    }
}
