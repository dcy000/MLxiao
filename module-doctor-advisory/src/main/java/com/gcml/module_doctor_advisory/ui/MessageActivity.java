package com.gcml.module_doctor_advisory.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_doctor_advisory.R;
import com.gcml.module_doctor_advisory.adapter.MessageShowAdapter;
import com.gcml.module_doctor_advisory.bean.YzInfoBean;
import com.gcml.module_doctor_advisory.net.QianYueRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/activity/message/activity")
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健  康  顾  问  建  议");
        RecyclerView mRecyclerView = findViewById(R.id.rv_message);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageShowAdapter = new MessageShowAdapter(this, mDataList);
        mRecyclerView.setAdapter(messageShowAdapter);

        new QianYueRepository()
                .getYzList(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<YzInfoBean>>() {
                    @Override
                    public void onNext(List<YzInfoBean> yzInfoBeans) {
                        if (yzInfoBeans == null || yzInfoBeans.size() == 0) {
                            MLVoiceSynthetize.startSynthesize(UM.getApp(), getString(R.string.no_yz));
                            findViewById(R.id.view_empty_data).setVisibility(View.VISIBLE);
                            mBtnGo.setVisibility(View.GONE);
                            mTvEmptyDataTips.setText("啊哦!你还没有健康顾问建议");
                            return;
                        }
                        mDataList.addAll(yzInfoBeans);
                        messageShowAdapter.notifyDataSetChanged();
                        MLVoiceSynthetize.startSynthesize(UM.getApp(), yzInfoBeans.get(0).yz);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void initView() {
        mTvEmptyDataTips = (TextView) findViewById(R.id.tv_empty_data_tips);
        mBtnGo = (TextView) findViewById(R.id.btn_go);
        mBtnGo.setOnClickListener(this);
    }
}
