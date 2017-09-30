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
import com.example.han.referralproject.bean.SymptomResultBean;

import java.util.ArrayList;

public class SymptomAnalyseResultActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_result_layout);
        findViewById(R.id.iv_back).setOnClickListener(this);
        ArrayList<SymptomResultBean> mDataList = (ArrayList<SymptomResultBean>) getIntent().getSerializableExtra("result");
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }
        RecyclerView symptomResultRv = (RecyclerView) findViewById(R.id.rv_symptom_result);
        symptomResultRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        symptomResultRv.setAdapter(new SymptomResultAdapter(mContext, mDataList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
