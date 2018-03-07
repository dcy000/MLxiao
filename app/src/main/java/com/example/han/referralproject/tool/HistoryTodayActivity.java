package com.example.han.referralproject.tool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.adapter.HistoryTodayRVAdapter;
import com.example.han.referralproject.tool.xfparsebean.HistoryTodayBean;
import com.medlink.danbogh.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryTodayActivity extends AppCompatActivity {

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
        initData();
    }

    private void initData() {
        String[] question = {"历史上的今天发生了什么", "历史上的今天有什么大事", "今天的历史性事件有哪些"};
        XFSkillApi.getSkillData(question[new Random().nextInt(3)], new XFSkillApi.getDataListener() {
            @Override
            public void onSuccess(Object anwser, String briefly) {

            }

            @Override
            public void onSuccess(Object anwser) {
                final List<HistoryTodayBean> resultData = (List<HistoryTodayBean>) anwser;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!resultData.isEmpty()) {
                            data.addAll(resultData);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        String date = Utils.getDateToString(System.currentTimeMillis(), "yyyy-MM-dd");
        tvDate.setText(date);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistoryEvent.setLayoutManager(layout);
        adapter = new HistoryTodayRVAdapter(R.layout.item_history_today, data);
        rvHistoryEvent.setAdapter(adapter);
    }
}
