package com.example.han.referralproject.settting.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.settting.adapter.KeyWordRVAdapter;
import com.example.han.referralproject.settting.bean.KeyWordBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomKeyWordsActivity extends BaseActivity {

    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    private List<KeyWordBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_key_words);
        ButterKnife.bind(this);
        initData();
        initTitle();
        initRV();
    }

    private int setSpanSize(int position, List<KeyWordBean> data) {
        int count;
        if (data.get(position).title) {
            count = 5;
        } else {
            count = 1;
        }
        return count;
    }

    private void initRV() {
        GridLayoutManager lm = new GridLayoutManager(this, 5);
        rvItems.setLayoutManager(lm);
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position, data);
//                return 2;
            }
        });

        rvItems.setAdapter(new KeyWordRVAdapter(data));
    }

    private List<KeyWordBean> initData() {
        for (int i = 0; i < 20; i++) {
            KeyWordBean bean = new KeyWordBean();
            bean.itemName = "项目" + i;
            if (i == 0 || i == 8) {
                bean.title = true;
            }
            data.add(bean);
        }
        return data;
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("关键词自定义");
    }
}
