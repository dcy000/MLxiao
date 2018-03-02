package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.YuYueInfo;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends BaseActivity {
    HistoryAdapter mHistoryAdapter;
    private RecyclerView mRecyclerView;
    private List<YuYueInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(getString(R.string.yuyue_history));

        speak(getString(R.string.yuye_history));
        mRecyclerView = (RecyclerView) findViewById(R.id.history_list);
        initData();
        initRV();
    }

    private void initData() {
        data = new ArrayList<>();
        ArrayList<YuYueInfo> allReservationHistory = DoctorappoActivity.allReservationHistory;
        for (int i = 0; i < allReservationHistory.size(); i++) {
            if ("已完成".equals(allReservationHistory.get(i).getState())
                    || "已拒绝".equals(allReservationHistory.get(i).getState())) {
                data.add(allReservationHistory.get(i));

            }
        }
    }


    public void initRV() {
        mRecyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(0);
        mRecyclerView.addItemDecoration(decoration);

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(getApplicationContext(), -5)));
        mHistoryAdapter = new HistoryAdapter(data, getApplicationContext());
        mRecyclerView.setAdapter(mHistoryAdapter);

    }


}

