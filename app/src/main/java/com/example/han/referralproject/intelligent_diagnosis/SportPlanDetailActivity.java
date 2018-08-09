package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class SportPlanDetailActivity extends BaseActivity {
    private ImageView detailImg;
    private TextView detailTitle;
    private TextView strength;
    private TextView consume;
    private TextView healthPrice;
    private RecyclerView encyclopediasList;
    private SportPlan.SportListBean data;
    private BaseQuickAdapter<EffertBean, BaseViewHolder> adapter;
    private List<EffertBean> mData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_detail);
        initView();
        setAdapter();
        dealData();
    }

    private void setAdapter() {
        adapter = new BaseQuickAdapter<EffertBean, BaseViewHolder>(R.layout.factor_details_item,mData) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, EffertBean s) {
                baseViewHolder.setText(R.id.tv_headline_influencing_factors,s.title);
                if (!TextUtils.isEmpty(s.content)) {
                    baseViewHolder.setText(R.id.tv_content,s.content);
                }else{
                    baseViewHolder.setText(R.id.tv_content,"无");
                }
            }
        };
        View head= LayoutInflater.from(this).inflate(R.layout.factor_details_head,null,false);
        TextView viewById = head.findViewById(R.id.tv_details_description);
        viewById.setText("运动小百科");
        adapter.addHeaderView(head);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setSmoothScrollbarEnabled(true);
        encyclopediasList.setLayoutManager(manager);
        encyclopediasList.setHasFixedSize(true);
        encyclopediasList.setNestedScrollingEnabled(false);
        encyclopediasList.setAdapter(adapter);
    }

    private void dealData() {
        if (data == null) {
            return;
        }
        String imgUrl = data.getImgUrl();
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(this)
                    .load(imgUrl)
                    .into(detailImg);
        }
        String name = data.getName();
        if (!TextUtils.isEmpty(name)) {
            detailTitle.setText(name);
            mTitleText.setText(name);
        }
        String sportLevel = data.getSportLevel();
        if (!TextUtils.isEmpty(sportLevel)) {
            switch (sportLevel) {
                case "1":
                    strength.setText("低");
                    break;
                case "2":
                    strength.setText("中");
                    break;
                case "3":
                    strength.setText("高");
                    break;
            }
        }
        String consumption = data.getConsumption();
        if (!TextUtils.isEmpty(consumption)) {
            String[] split = consumption.split("千卡");
            if (split.length > 1) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("<font color='#FF2D2D'>" + split[0] + "</font>千卡" + split[1]);
                consume.setText(Html.fromHtml(buffer.toString()));
            }else {
                consume.setText(consumption);
            }
        }
        String sportEffect = data.getSportEffect();
        if (!TextUtils.isEmpty(sportEffect)) {
            healthPrice.setText(sportEffect);
        }
        EffertBean hypertensionEffect = new EffertBean("对血压的好处", data.getHypertensionEffect());
        EffertBean diabetesEffect = new EffertBean("对血糖的好处", data.getDiabetesEffect());
        mData.add(hypertensionEffect);
        mData.add(diabetesEffect);
        adapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(data.getHypertensionEffect())){
            speak("主人，"+name+sportEffect+"对血压的好处"+data.getHypertensionEffect());
        }
        if (!TextUtils.isEmpty(data.getDiabetesEffect())){
            speak("主人，"+name+sportEffect+"对血糖的好处"+data.getDiabetesEffect());
        }
    }

    private void initView() {
        mData=new ArrayList<>();
        data = getIntent().getParcelableExtra("data");
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("");
        detailImg = findViewById(R.id.detail_img);
        detailTitle = findViewById(R.id.detail_title);
        strength = findViewById(R.id.strength);
        consume = findViewById(R.id.consume);
        healthPrice = findViewById(R.id.health_price);
        encyclopediasList = findViewById(R.id.encyclopedias_list);

    }
    public static class EffertBean{
        public String title;
        public String content;

        public EffertBean(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
