package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.SymptomResultAdapter;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.LinearLayoutDividerItemDecoration;

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
            NetworkApi.getJibing(getIntent().getStringExtra("type"), new NetworkManager.SuccessCallback<DiseaseResult>() {
                @Override
                public void onSuccess(DiseaseResult response) {
                    SymptomResultBean.bqs bqs=new SymptomResultBean.bqs();
                    bqs.setBname(response.bname);
                    bqs.setEat(response.eat);
                    bqs.setReview(response.review);
                    bqs.setSuggest(response.suggest);
                    bqs.setSports(response.sports);
                    bqs.setGl("0");
                    mDataList.add(bqs);
                    adapter.notifyDataSetChanged();
                    speak("请点击查看详情了解更多信息");
                }
            }, new NetworkManager.FailedCallback() {
                @Override
                public void onFailed(String message) {
                    ToastUtils.show(message);
                }
            });
            return;
        } else {
            mDataList.addAll(mList);
            adapter.notifyDataSetChanged();
            StringBuilder mBuilder = new StringBuilder();
            for (SymptomResultBean.bqs itemBean : mDataList) {
                mBuilder.append(itemBean.getBname()).append("、");
            }
            speak(String.format(getString(R.string.tips_symptom_result), mBuilder.toString()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
    }
}
