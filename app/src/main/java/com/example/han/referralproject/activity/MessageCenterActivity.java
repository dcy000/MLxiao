package com.example.han.referralproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.MessagesOfCenter;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.LinearLayoutDividerItemDecoration;
import com.example.han.referralproject.util.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gzq on 2018/3/7.
 */

public class MessageCenterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.yiZhu)
    RadioButton yiZhu;
    @BindView(R.id.xiaoXi)
    RadioButton xiaoXi;
    @BindView(R.id.rg_tabs)
    RadioGroup rgTabs;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.noData)
    LinearLayout noData;
    private String doctorName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("消息中心");

        doctorName = getIntent().getStringExtra("doctorName");
        list.setLayoutManager(new LinearLayoutManager(MessageCenterActivity.this));
        list.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 16, Color.TRANSPARENT));
        getYizhu();
        yiZhu.setOnClickListener(this);
        xiaoXi.setOnClickListener(this);


    }

    private BaseQuickAdapter<YzInfoBean, BaseViewHolder> adapterYizhu;
    private BaseQuickAdapter<MessagesOfCenter, BaseViewHolder> adapterXiaoxi;
    private ArrayList<YzInfoBean> responseYizhu;
    private ArrayList<MessagesOfCenter> responseXiaoxi;

    private void getYizhu() {
        rgTabs.check(R.id.yiZhu);
        responseYizhu=new ArrayList<>();
        list.setAdapter(adapterYizhu = new BaseQuickAdapter<YzInfoBean, BaseViewHolder>(R.layout.item_message_center, responseYizhu) {
            @Override
            protected void convert(BaseViewHolder helper, YzInfoBean item) {
                if (TextUtils.isEmpty(doctorName)) {
                    helper.setText(R.id.docotor_name, "未公开");
                } else {
                    helper.setText(R.id.docotor_name, doctorName);
                }
                if (!TextUtils.isEmpty(item.time))
                    helper.setText(R.id.time, TimeUtil.format(Long.parseLong(item.time)));
                if (!TextUtils.isEmpty(item.yz))
                    helper.setText(R.id.message, item.yz);
            }
        });

        adapterYizhu.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(MessageCenterActivity.this, MessageDetailsActivity.class)
                        .putExtra("doctorName", doctorName)
                        .putExtra("message", responseYizhu == null ? null : responseYizhu.get(position).yz));
            }
        });
        NetworkApi.getYzList(new NetworkManager.SuccessCallback<ArrayList<YzInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<YzInfoBean> response) {
                if (response != null && response.size() != 0) {
                    noData.setVisibility(View.GONE);
                    responseYizhu.addAll(response);
                    adapterYizhu.notifyDataSetChanged();
                } else {
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void getXiaoxi() {
        rgTabs.check(R.id.xiaoXi);
        responseXiaoxi=new ArrayList<>();
        list.setAdapter(adapterXiaoxi = new BaseQuickAdapter<MessagesOfCenter, BaseViewHolder>(R.layout.item_message_center, responseXiaoxi) {
            @Override
            protected void convert(BaseViewHolder helper, MessagesOfCenter item) {
                if (TextUtils.isEmpty(doctorName)) {
                    helper.setText(R.id.doctor_name, "未公开");
                } else {
                    helper.setText(R.id.doctor_name, doctorName);
                }
                if (!TextUtils.isEmpty(item.time))
                    helper.setText(R.id.time, TimeUtil.format(Long.parseLong(item.time)));
                if (!TextUtils.isEmpty(item.message))
                    helper.setText(R.id.message, item.message);


            }
        });
        adapterXiaoxi.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(MessageCenterActivity.this, MessageDetailsActivity.class)
                        .putExtra("doctorName", doctorName)
                        .putExtra("message", responseXiaoxi == null ? null : responseXiaoxi.get(position).message));
            }
        });
        NetworkApi.getMessages("10002", "0", new NetworkManager.SuccessCallback<ArrayList<MessagesOfCenter>>() {
            @Override
            public void onSuccess(ArrayList<MessagesOfCenter> response) {
                if (response != null && response.size() != 0) {
                    noData.setVisibility(View.GONE);
                    responseXiaoxi.addAll(response);
                    adapterXiaoxi.notifyDataSetChanged();
                } else {
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.yiZhu:
                getYizhu();
                break;
            case R.id.xiaoXi:
                getXiaoxi();
                break;
        }
    }
}
