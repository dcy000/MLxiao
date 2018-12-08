package com.example.module_setting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.module_setting.R;
import com.example.module_setting.R2;
import com.example.module_setting.SharedPreferencesUtils;
import com.example.module_setting.adapter.KeyWordDifineRVAdapter;
import com.example.module_setting.bean.KeyWordDefinevBean;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.google.gson.Gson;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeyWordEditActivity extends ToolbarBaseActivity implements KeyWordDifineRVAdapter.DeleteClickListener, View.OnClickListener {

    @BindView(R2.id.rv_keys)
    RecyclerView rvKeys;
    private KeyWordDifineRVAdapter adapter;
    private List<KeyWordDefinevBean> data=new ArrayList<>();
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initRV();
        MLVoiceSynthetize.startSynthesize("主人,你可以编辑的名义的关键字");
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_key;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
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
