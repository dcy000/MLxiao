package com.zane.androidupnpdemo.live_tv.tv_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.live_tv.GridViewDividerItemDecoration;
import com.zane.androidupnpdemo.live_tv.LiveBean;
import com.zane.androidupnpdemo.utils.AesUtil;

import java.util.List;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvChannelActivity extends CommonBaseActivity implements ITvList, View.OnClickListener {
    private TextView mHykdBack;
    private RecyclerView mTvList;
    private BaseQuickAdapter<LiveBean, BaseViewHolder> adapter;
    private ITvChannelPresenter tvChannelPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_list);
        initView();
        tvChannelPresenter = new TvChannelPresenterImp(this, new TvChannelInteractorImp());
        tvChannelPresenter.getChannels();
    }

    @Override
    public void showDialog() {
        Log.e("显示dialog", "showDialog: ");
    }

    @Override
    public void hideDialog() {
        Log.e("隐藏dialog", "hideDialog: ");
    }

    @Override
    public void fillData(List<LiveBean> channels) {
        mTvList.setLayoutManager(new GridLayoutManager(this, 2));
        mTvList.addItemDecoration(new GridViewDividerItemDecoration(10, 10));
        mTvList.setAdapter(adapter = new BaseQuickAdapter<LiveBean, BaseViewHolder>(R.layout.tv_item, channels) {
            @Override
            protected void convert(BaseViewHolder helper, LiveBean item) {
                helper.setText(R.id.tv_name, item.getTvName());
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("item被点击", "onItemClick: ");
                tvChannelPresenter.tvListItemClick(adapter, view, position);
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        mHykdBack = (TextView) findViewById(R.id.hykd_back);
        mTvList = (RecyclerView) findViewById(R.id.tv_list);
        mHykdBack.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvChannelPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hykd_back:
                finish();
                break;
        }
    }
}
