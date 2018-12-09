package com.example.han.referralproject.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.MessageShowAdapter;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.service.API;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends ToolbarBaseActivity implements View.OnClickListener {
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
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_message;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mTvEmptyDataTips = (TextView) findViewById(R.id.tv_empty_data_tips);
        mBtnGo = (TextView) findViewById(R.id.btn_go);
        mBtnGo.setOnClickListener(this);
        mTitleText.setText("医  生  建  议");
        RecyclerView mRecyclerView = findViewById(R.id.rv_message);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageShowAdapter = new MessageShowAdapter(this, mDataList);
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

    @NonNull
    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }
}
