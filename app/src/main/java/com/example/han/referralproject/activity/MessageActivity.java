package com.example.han.referralproject.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.MessageShowAdapter;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<YzInfoBean> mDataList = new ArrayList<>();
    private MessageShowAdapter messageShowAdapter;
    /**
     * 啊哦!还没有数据
     */
    private TextView mTvEmptyDataTips;
    /**
     * 马上去测量
     */
    private TextView mBtnGo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("医  生  建  议");
        RecyclerView mRecyclerView = findViewById(R.id.rv_message);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        messageShowAdapter = new MessageShowAdapter(mContext, mDataList);
        mRecyclerView.setAdapter(messageShowAdapter);

        Box.getRetrofit(API.class)
                .getMedicalOrders(Box.getUserId())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<YzInfoBean>>() {
                    @Override
                    public void onNext(List<YzInfoBean> yzInfoBeans) {
                        if (yzInfoBeans == null || yzInfoBeans.size() == 0) {
                            MLVoiceSynthetize.startSynthesize(R.string.no_yz);
                            findViewById(R.id.view_empty_data).setVisibility(View.VISIBLE);
                            mBtnGo.setVisibility(View.GONE);
                            mTvEmptyDataTips.setText("啊哦!你还没有医生建议");
                            return;
                        }
                        mDataList.addAll(yzInfoBeans);
                        messageShowAdapter.notifyDataSetChanged();
                        MLVoiceSynthetize.startSynthesize(yzInfoBeans.get(0).yz);
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_go:
                break;
        }
    }

    private void initView() {
        mTvEmptyDataTips = (TextView) findViewById(R.id.tv_empty_data_tips);
        mBtnGo = (TextView) findViewById(R.id.btn_go);
        mBtnGo.setOnClickListener(this);
    }
}
