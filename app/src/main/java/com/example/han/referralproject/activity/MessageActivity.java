package com.example.han.referralproject.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.MessageShowAdapter;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

public class MessageActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<YzInfoBean> mDataList = new ArrayList<>();
    private MessageShowAdapter messageShowAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mToolbar.setVisibility(View.VISIBLE);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_message);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageShowAdapter = new MessageShowAdapter(this, mDataList);
        mRecyclerView.setAdapter(messageShowAdapter);
        NetworkApi.getYzList(successCallback);

    }

    private NetworkManager.SuccessCallback successCallback = new NetworkManager.SuccessCallback<ArrayList<YzInfoBean>>() {
        @Override
        public void onSuccess(ArrayList<YzInfoBean> response) {
            if (response == null || response.size() == 0){
                MLVoiceSynthetize.startSynthesize(R.string.no_yz);
                return;
            }
            mDataList.addAll(response);
            messageShowAdapter.notifyDataSetChanged();
            MLVoiceSynthetize.startSynthesize(response.get(0).yz);
        }
    };
}
