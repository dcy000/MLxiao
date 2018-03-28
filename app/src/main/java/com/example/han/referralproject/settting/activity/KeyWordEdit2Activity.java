package com.example.han.referralproject.settting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.settting.SharedPreferencesUtils;
import com.example.han.referralproject.settting.bean.KeyWordDefinevBean;
import com.example.han.referralproject.settting.wrap.FlowLayout;
import com.example.han.referralproject.settting.wrap.ItemView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeyWordEdit2Activity extends BaseActivity implements View.OnClickListener, ItemView.IcClickListener {

    @BindView(R.id.flow)
    FlowLayout flow;
    private Intent intent;
    private List<KeyWordDefinevBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_word_edit2);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initFlowlayout();
    }

    private void initTitle() {
        mTitleText.setText("关键字编辑");
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setVisibility(View.GONE);
        mRightText.setText("完成");
        mRightText.setOnClickListener(this);
    }

    private void initData() {
        intent = getIntent();
        data = (List<KeyWordDefinevBean>) intent.getSerializableExtra("data");
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).show = true;
            }
        }
    }

    private void initFlowlayout() {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                ItemView view = new ItemView(this);
                view.setText(data.get(i).name);
                view.showICon(true);
                view.setListener(this);
                flow.addView(view);
            }
        }

    }


    public static void StartMe(Context context, List<KeyWordDefinevBean> data, String pinyin) {
        Intent intent = new Intent(context, KeyWordEdit2Activity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("pinyin", pinyin);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        finish();
        SharedPreferencesUtils.setParam(this, intent.getStringExtra("pinyin"), new Gson().toJson(data));
    }

    @Override
    public void onIcClick(View v) {
        flow.removeView(v);
        ItemView view = (ItemView) v;
        deleteItemData(data, view.getText());
    }

    private void deleteItemData(List<KeyWordDefinevBean> data, String text) {

        for (int i = 0; i < data.size(); i++) {
            if (text.equals(data.get(i).name)) {
                data.remove(i);
                return;
            }
        }
    }
}
