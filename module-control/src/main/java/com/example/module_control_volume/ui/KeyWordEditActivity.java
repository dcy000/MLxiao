package com.example.module_control_volume.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.module_control_volume.R;
import com.example.module_control_volume.adapter.KeyWordDifineRVAdapter;
import com.gcml.common.recommend.bean.get.KeyWordDefinevBean;
import com.gcml.common.utils.SharedPreferencesUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KeyWordEditActivity extends ToolbarBaseActivity implements KeyWordDifineRVAdapter.DeleteClickListener, View.OnClickListener {

    RecyclerView rvKeys;
    private KeyWordDifineRVAdapter adapter;
    private List<KeyWordDefinevBean> data=new ArrayList<>();
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);
        rvKeys=findViewById(R.id.rv_keys);
        initTitle();
        initData();
        initRV();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"你可以编辑的名义的关键字");
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

    private void initTitle() {
        mTitleText.setText("关键字编辑");
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setVisibility(View.GONE);
        mRightText.setText("完成");
        mRightText.setOnClickListener(this);
    }

    private void initRV() {
        rvKeys.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new KeyWordDifineRVAdapter(R.layout.item_key_define, data);
        adapter.setListener(this);
        rvKeys.setAdapter(adapter);
    }

    @Override
    public void onDeleteClick(int position) {
        data.remove(position);
        adapter.notifyDataSetChanged();
    }


    public static void StartMe(Context context, List<KeyWordDefinevBean> data,String pinyin) {
        Intent intent = new Intent(context, KeyWordEditActivity.class);
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
}
