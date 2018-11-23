package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.SymptomResultAdapter;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.recyclerview.LinearLayoutDividerItemDecoration;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

public class SymptomAnalyseResultActivity extends BaseActivity {
    private SymptomResultAdapter adapter;
    private ArrayList<SymptomResultBean.bqs> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_result_layout);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("自查报告");
        final ArrayList<SymptomResultBean.bqs> mDataList = new ArrayList<>();
        RecyclerView symptomResultRv = (RecyclerView) findViewById(R.id.rv_symptom_result);
        symptomResultRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        symptomResultRv.addItemDecoration(new LinearLayoutDividerItemDecoration(20, 0));
        symptomResultRv.setAdapter(adapter = new SymptomResultAdapter(mContext, mDataList));
        mList = (ArrayList<SymptomResultBean.bqs>) getIntent().getSerializableExtra("result");

        adapter.notifyDataSetChanged();
        if (mList == null || mList.size() == 0) {

            return;
        } else {
            mDataList.addAll(mList);
            adapter.notifyDataSetChanged();
            StringBuilder mBuilder = new StringBuilder();
            for (SymptomResultBean.bqs itemBean : mDataList) {
                mBuilder.append(itemBean.getBname()).append("、");
            }
            MLVoiceSynthetize.startSynthesize(String.format(getString(R.string.tips_symptom_result), mBuilder.toString()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
    }
}
