package com.example.han.referralproject.settting.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.settting.adapter.KeyWordRVAdapter;
import com.example.han.referralproject.settting.bean.KeyWordBean;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomKeyWordsActivity extends BaseActivity implements KeyWordRVAdapter.ClickItemInterface {

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
        speak("请选择要定义的关键词");
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
            }
        });
        rvItems.addItemDecoration(new GridViewDividerItemDecoration(20, 20));
        KeyWordRVAdapter adapter = new KeyWordRVAdapter(data);
        adapter.setListener(this);
        rvItems.setAdapter(adapter);
    }

    private List<KeyWordBean> initData() {
//        for (int i = 0; i < 20; i++) {
//            KeyWordBean bean = new KeyWordBean();
//            bean.itemName = "项目" + i;
//            if (i == 0 || i == 8) {
//                bean.title = true;
//            }
//            data.add(bean);
//        }
//        return data;
        //健康监测自定义
        initJianKangKey();
        initJiBenKey();
        initPersonKey();
        initDoctorKey();
        initShopKey();
        return data;
    }

    private void initShopKey() {
        KeyWordBean health = new KeyWordBean();
        health.title = true;
        health.itemName = "健康商城";
        data.add(health);
        for (int i = 0; i < this.shop.length; i++) {
            KeyWordBean bean = new KeyWordBean();
            bean.itemName = this.shop[i];
            data.add(bean);
        }


    }

    private void initDoctorKey() {
        KeyWordBean health = new KeyWordBean();
        health.title = true;
        health.itemName = "医生咨询";
        data.add(health);
        for (int i = 0; i < this.doctor.length; i++) {
            KeyWordBean bean = new KeyWordBean();
            bean.itemName = this.doctor[i];
            data.add(bean);
        }
    }

    private void initPersonKey() {
        KeyWordBean health = new KeyWordBean();
        health.title = true;
        health.itemName = "个人中心";
        data.add(health);
        for (int i = 0; i < this.person.length; i++) {
            KeyWordBean bean = new KeyWordBean();
            bean.itemName = this.person[i];
            data.add(bean);
        }

    }

    private void initJiBenKey() {
        KeyWordBean health = new KeyWordBean();
        health.title = true;
        health.itemName = "基本功能";
        data.add(health);
        for (int i = 0; i < this.jiben.length; i++) {
            KeyWordBean bean = new KeyWordBean();
            bean.itemName = this.jiben[i];
            data.add(bean);
        }
    }

    private void initJianKangKey() {
        KeyWordBean health = new KeyWordBean();
        health.title = true;
        health.itemName = "健康检测";
        data.add(health);
        for (int i = 0; i < this.health.length; i++) {
            KeyWordBean bean = new KeyWordBean();
            bean.itemName = this.health[i];
            data.add(bean);
        }
    }

    //    String[] health =getResources().getStringArray(R.array.key_jiankang_jiance);
//    String[] jiben = getApplicationContext().getResources().getStringArray(R.array.key_jiben_gongneng);
//    String[] person = getApplicationContext().getResources().getStringArray(R.array.key_geren_zhongxin);
//    String[] doctor = getApplicationContext().getResources().getStringArray(R.array.key_yisheng_ziuxn);
//    String[] shop = getApplicationContext().getResources().getStringArray(R.array.key_jiankang_shangcheng);

    String[] health = {"血压", "血氧", "体温", "血糖", "心电", "体重", "三合一"};
    String[] jiben = {"调大声音", "调小声音", "回到主界面", "删除", "上一步", "下一步"};
    String[] person = {"个人中心", "症状自查", "测量历史", "医生建议", "吃药提醒", "账户充值", "我的订单", "健康课堂", "娱乐", "收音机", "音乐"};
    String[] doctor = {"医生咨询", "签约医生", "在线医生"};
    String[] shop = {"健康商城"};

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("关键词自定义");
    }

    @Override
    public void onItemClick(int position) {
        String itemName = data.get(position).itemName;
//        SetKeyWordActivity.StartMe(this, itemName);
        SetKeyWord2Activity.StartMe(this, itemName);

    }
}
