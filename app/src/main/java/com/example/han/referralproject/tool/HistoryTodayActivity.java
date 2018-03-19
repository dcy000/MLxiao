package com.example.han.referralproject.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.tool.adapter.HistoryTodayRVAdapter;
import com.example.han.referralproject.tool.other.XFSkillApi;
import com.example.han.referralproject.tool.xfparsebean.HistoryTodayBean;
import com.example.han.referralproject.voice.SpeechSynthesizerHelper;
import com.medlink.danbogh.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryTodayActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_history_event)
    RecyclerView rvHistoryEvent;
    private List<HistoryTodayBean> data = new ArrayList<>();
    private HistoryTodayRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_today);
        ButterKnife.bind(this);
        initView();
        speak("主人,欢迎来到历史的今天");
        initData();
        initEvent();
    }

    private void initEvent() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                        if (resultData != null && !resultData.isEmpty()) {
                            data.addAll(resultData);
                            adapter.notifyDataSetChanged();
                            speak(data.get(0).description);
                        } else {
                            speak("主人,我还不知道历史上的今天发生了什么事,我得去学习一下");
                        }
                    }
                });
            }
        });

//        假数据
//        for (int i = 0; i < 10; i++) {
//            HistoryTodayBean bean = new HistoryTodayBean();
//            bean.description = "哈哈时候哈哈时候哈哈时候哈哈时候哈哈" +
//                    "候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时" +
//                    "哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候" +
//                    "哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈" +
//                    "时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈" +
//                    "哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时" +
//                    "候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈" +
//                    "哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时" +
//                    "候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈哈哈时" +
//                    "候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈" +
//                    "时候时候候哈哈时候哈哈时候哈哈时候哈哈时候哈哈时候哈哈哈" +
//                    "哈时候哈哈时候哈哈时候哈哈时候哈哈" +
//                    "时候哈哈时候哈哈时候哈哈时候时候候哈哈时候哈哈时候哈哈" +
//                    "时候哈哈时候哈哈时候哈哈哈哈时候哈哈时候哈哈时候哈哈时" +
//                    "候哈哈时候哈哈时候哈哈时候哈哈时候时候";
//            bean.title = "sdf";
//            bean.imgs = new ArrayList<>();
//            bean.imgs.add("http://a0.att.hudong.com/32/26/20300542501236139721267074877_140.jpg");
//            data.add(bean);
//        }
//        adapter.notifyDataSetChanged();
    }

    private void initView() {
        String date = Utils.getDateToString(System.currentTimeMillis(), "MM月dd日");
        tvDate.setText(date);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistoryEvent.setLayoutManager(layout);
        adapter = new HistoryTodayRVAdapter(R.layout.item_history_today, data);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                SpeechSynthesizerHelper.stop();
                SpeechSynthesizerHelper.startSynthesize(view.getContext(), "事件" + data.get(i).title + "," + data.get(i).description);
            }
        });
        rvHistoryEvent.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
        SpeechSynthesizerHelper.stop();
    }
}
