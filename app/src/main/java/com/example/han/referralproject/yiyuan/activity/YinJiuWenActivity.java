package com.example.han.referralproject.yiyuan.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.settting.adapter.KeyWordRVAdapter;
import com.example.han.referralproject.settting.bean.KeyWordBean;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;
import com.example.han.referralproject.yiyuan.adpater.MultiChoiceTitleRVAdaper;
import com.example.han.referralproject.yiyuan.bean.MultipleChoiceBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YinJiuWenActivity extends BaseActivity implements KeyWordRVAdapter.ClickItemInterface {

    @BindView(R.id.rv)
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yin_jiu_wen);
        ButterKnife.bind(this);
        initTilte();
        initData();
        initRV();

    }

    private List<MultipleChoiceBean> data = new ArrayList<>();

    private void initData() {
        MultipleChoiceBean bean7 = new MultipleChoiceBean().setTitle("本周内是否有饮酒？").setTitle(true);
        MultipleChoiceBean bean1 = new MultipleChoiceBean().setTitle("本周内是否有饮酒？").setItemName("是").setSelected(false);
        MultipleChoiceBean bean2 = new MultipleChoiceBean().setTitle("本周内是否有饮酒？").setItemName("否").setSelected(true);

        MultipleChoiceBean bean8 = new MultipleChoiceBean().setTitle("请选择本周喝的酒").setTitle(true);
        MultipleChoiceBean bean3 = new MultipleChoiceBean().setTitle("请选择本周喝的酒").setItemName("白酒").setSelected(false).setMultipleChoice(true);
        MultipleChoiceBean bean4 = new MultipleChoiceBean().setTitle("请选择本周喝的酒").setItemName("料酒").setSelected(false).setMultipleChoice(true);
        MultipleChoiceBean bean5 = new MultipleChoiceBean().setTitle("请选择本周喝的酒").setItemName("啤酒").setSelected(false).setMultipleChoice(true);
        MultipleChoiceBean bean6 = new MultipleChoiceBean().setTitle("请选择本周喝的酒").setItemName("米酒").setSelected(false).setMultipleChoice(true);

        data.add(bean1);
        data.add(bean2);
        data.add(bean3);
        data.add(bean4);
        data.add(bean5);
        data.add(bean6);
        data.add(bean7);
        data.add(bean8);

    }

    private int setSpanSize(int position, List<MultipleChoiceBean> data) {
        int count;
        if (data.get(position).isTitle()) {
            count = 4;
        } else {
            count = 1;
        }
        return count;
    }

    private void initRV() {
        GridLayoutManager lm = new GridLayoutManager(this, 4);
        rv.setLayoutManager(lm);
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position, data);
            }
        });
        rv.addItemDecoration(new GridViewDividerItemDecoration(20, 20));
        MultiChoiceTitleRVAdaper adapter = new MultiChoiceTitleRVAdaper(data, this);
        adapter.setListener(this);
        rv.setAdapter(adapter);
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

    @Override
    public void onItemClick(int position) {

    }

}


