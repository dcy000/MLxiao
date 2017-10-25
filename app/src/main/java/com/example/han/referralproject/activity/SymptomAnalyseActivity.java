package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.SymptomRecyclerAdapter;
import com.example.han.referralproject.bean.SymptomBean;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import java.util.ArrayList;

public class SymptomAnalyseActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private ArrayList<SymptomBean> mDataList = new ArrayList<>();
    private SymptomRecyclerAdapter mSymptomAdapter;
    public ImageView ivBack;

    public ImageView ImageView1;
    public ImageView ImageView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_analyse);
        /*ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_symptom);
        mSymptomAdapter = new SymptomRecyclerAdapter(mContext, mDataList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(mSymptomAdapter);
        findViewById(R.id.btn_analyse).setOnClickListener(this);
        NetworkApi.getAllSym(mGetAllSymCallback);



        ImageView1 = (ImageView) findViewById(R.id.icon_back);
        ImageView2 = (ImageView) findViewById(R.id.icon_home);

        ImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_analyse:
                String selectResult = mSymptomAdapter.getResult();
                if (!TextUtils.isEmpty(selectResult)){
                    NetworkApi.analyseSym(selectResult, mAnalyseCallback);
                }
                break;
        }
    }

    private NetworkManager.SuccessCallback mGetAllSymCallback = new NetworkManager.SuccessCallback<ArrayList<SymptomBean>>() {
        @Override
        public void onSuccess(ArrayList<SymptomBean> response) {
            if (response == null) {
                return;
            }
            speak(R.string.tips_symptom);
            mDataList.addAll(response);
            mSymptomAdapter.notifyDataSetChanged();
        }
    };

    private NetworkManager.SuccessCallback mAnalyseCallback = new NetworkManager.SuccessCallback<ArrayList<SymptomResultBean>>() {
        @Override
        public void onSuccess(ArrayList<SymptomResultBean> response) {
            if (response == null) {
                return;
            }
            Intent mResultIntent = new Intent(mContext, SymptomAnalyseResultActivity.class);
            mResultIntent.putExtra("result", response);
            startActivity(new Intent(mResultIntent));
        }
    };
}
