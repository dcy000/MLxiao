package com.example.module_control_volume.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.module_control_volume.R;
import com.gcml.common.recommend.bean.get.KeyWordDefinevBean;
import com.gcml.common.utils.SharedPreferencesUtils;
import com.example.module_control_volume.wrap.FlowLayout;
import com.example.module_control_volume.wrap.ItemView;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class KeyWordEdit2Activity extends ToolbarBaseActivity implements View.OnClickListener, ItemView.IcClickListener {

    FlowLayout flow;
    private Intent intent;
    private List<KeyWordDefinevBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_word_edit2);
        flow=findViewById(R.id.flow);
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
        super.onClick(v);
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
