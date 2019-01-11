package com.zhang.hui.lib_recreation.tool.activtiy;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.common.utils.Utils;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.adapter.HistoryTodayRVAdapter;
import com.zhang.hui.lib_recreation.tool.other.XFSkillApi;
import com.zhang.hui.lib_recreation.tool.xfparsebean.HistoryTodayBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryTodayActivity extends AppCompatActivity {

    /**
     * 历史上的今天
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;
    /**
     * date
     */
    private TextView tvDate;
    private RecyclerView tvHistoryEvent;
    private ConstraintLayout constraintLayout3;

    private List<HistoryTodayBean> data = new ArrayList<>();
    private HistoryTodayRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_today);
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "您好，欢迎来到历史的今天", false);
        initView();
        initEvent();
        initData();
    }

    private void initData() {
        String[] question = {"历史上的今天发生了什么", "历史上的今天有什么大事", "今天的历史性事件有哪些"};
        XFSkillApi.getSkillData(question[new Random().nextInt(1)], new XFSkillApi.getDataListener() {

            @Override
            public void onSuccess(Object anwser, final String anwserText, String service, String question) {
                final List<HistoryTodayBean> resultData = (List<HistoryTodayBean>) anwser;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultData != null && resultData.size() != 0) {
                            data.addAll(resultData);
                            adapter.notifyDataSetChanged();
                            MLVoiceSynthetize.startSynthesize(getApplicationContext(), data.get(0).title + "," + data.get(0).description, false);
                        } else {
                            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "您好，我还不知道历史上的今天发生了什么事,我得去学习一下", false);
                        }
                    }
                });
            }
        });
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceRecognize.stop();
        MLVoiceSynthetize.stop();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvHistoryEvent = (RecyclerView) findViewById(R.id.tv_history_event);
        constraintLayout3 = (ConstraintLayout) findViewById(R.id.constraintLayout3);


        String date = Utils.getDateToString(System.currentTimeMillis(), "MM月dd日");
        tvDate.setText(date);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        tvHistoryEvent.setLayoutManager(layout);
        adapter = new HistoryTodayRVAdapter(R.layout.item_history_today, data);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MLVoiceSynthetize.stop();
                MLVoiceSynthetize.startSynthesize(view.getContext(), data.get(i).title + "," + data.get(i).description, false);
            }
        });
        tvHistoryEvent.setAdapter(adapter);
    }
}
