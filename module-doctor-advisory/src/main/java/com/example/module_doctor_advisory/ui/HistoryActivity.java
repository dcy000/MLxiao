package com.example.module_doctor_advisory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.module_doctor_advisory.R;
import com.example.module_doctor_advisory.adapter.HistoryAdapter;
import com.example.module_doctor_advisory.bean.YuYueInfo;
import com.example.module_doctor_advisory.utils.SpaceItemDecoration;
import com.example.module_doctor_advisory.utils.SpacesItemDecoration;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.ScreenUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends ToolbarBaseActivity {
    HistoryAdapter mHistoryAdapter;
    private RecyclerView mRecyclerView;
    private List<YuYueInfo> data;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_history;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mTitleText.setText(getString(R.string.yuyue_history));
        MLVoiceSynthetize.startSynthesize(R.string.yuye_history);
        mRecyclerView = (RecyclerView) findViewById(R.id.history_list);
        initData();
        initRV();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
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

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dp2px( -5)));
        mHistoryAdapter = new HistoryAdapter(data, getApplicationContext());
        mRecyclerView.setAdapter(mHistoryAdapter);

    }


}

