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
import com.example.han.referralproject.bean.MonthlyReport;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
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
 * Created by gzq on 2018/3/14.
 */

public class MonthlyReportActivity extends BaseActivity {
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private List<Fragment> fragments;
    private New_MonthlyReport1Fragment new_monthlyReportFragment1;
    private New_MonthlyReport2Fragment new_monthlyReport2Fragment;
    private New_MonthlyReport3Fragment new_monthlyReport3Fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("月生活记录");
        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<>();

        new_monthlyReportFragment1 = new New_MonthlyReport1Fragment();
        fragments.add(new_monthlyReportFragment1);
        new_monthlyReport2Fragment = new New_MonthlyReport2Fragment();
        fragments.add(new_monthlyReport2Fragment);
        new_monthlyReport3Fragment=new New_MonthlyReport3Fragment();
        fragments.add(new_monthlyReport3Fragment);
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
//        NetworkApi.getMonthReport(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<MonthlyReport>() {
//            @Override
//            public void onSuccess(MonthlyReport response) {
//                if (response != null) {
//                    fragment1.notifyData(response.mapq);
//                    fragment2.notifyData(response.mapq);
//                    fragment3.notifyData(response.mapq);
//                    fragment4.notifyData(response.mapq);
//                    fragment5.notifyData(response.mapq);
//                    Logger.e("返回测量数据成功" + response);
//                } else {
//                    ToastTool.showShort("暂无月报告");
//                    speak("主人，您的测量数据太少，我们还不能为您生成本月的报告。请您坚持每天测量，我们将在每月一号为您生成新报告");
//                }
//            }
//        }, new NetworkManager.FailedCallback() {
//            @Override
//            public void onFailed(String message) {
//                Logger.e("返回测量数据失败" + message);
//                ToastTool.showShort("暂无月报告");
//                speak("主人，您的测量数据太少，我们还不能为您生成本月的报告。请您坚持每天测量，我们将在每月一号为您生成新报告");
//            }
//        });
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 1);
        long monthAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.WeeklyOrMonthlyReport)
                .params("userId", MyApplication.getInstance().userId)
                .params("endTimeStamp", monthAgoTime)
                .params("num", "4")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("请求成功", "onSuccess: " + response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                WeeklyOrMonthlyReport weeklyOrMonthlyReport = new Gson().fromJson(data.toString(), WeeklyOrMonthlyReport.class);
                                new_monthlyReportFragment1.notifyData(weeklyOrMonthlyReport);
                                new_monthlyReport2Fragment.notifyData(weeklyOrMonthlyReport);
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
