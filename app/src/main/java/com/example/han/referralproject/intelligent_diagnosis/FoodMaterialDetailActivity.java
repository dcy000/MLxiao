package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.intelligent_diagnosis.FoodMateratilDetail;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Administrator on 2018/5/14.
 */

public class FoodMaterialDetailActivity extends BaseActivity {
    private ImageView foodMaterialImg;
    private TextView foodMaterialName;
    private TextView mealAmount;
    private TextView fitPopulation;
    private TextView tabooPopulation;
    private TextView tvTip1;
    private RecyclerView label;
    private TextView tvTip2;
    private TextView nutritiveValue;
    private FoodMateratilDetail data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_material);
        initView();
        dealData();
    }

    private void dealData() {
        if (data==null){
            return;
        }
        String imgUrl = data.getImgUrl();
        if (!TextUtils.isEmpty(imgUrl)){
            Glide.with(this)
                    .load(imgUrl)
                    .into(foodMaterialImg);
        }
        String name = data.getName();
        if (!TextUtils.isEmpty(name)){
            foodMaterialName.setText(name);
        }
        String foodIntake = data.getFoodIntake();
        if (!TextUtils.isEmpty(foodIntake)) {
            mealAmount.setText(foodIntake);
        }
        String tabooHuman = data.getTabooHuman();
        if (!TextUtils.isEmpty(tabooHuman)) {
            tabooPopulation.setText(tabooHuman);
        }
        String suitableHuman = data.getSuitableHuman();
        if (!TextUtils.isEmpty(suitableHuman)) {
            fitPopulation.setText(suitableHuman);
        }
        String suitablePhysique = data.getSuitablePhysique();
        if (!TextUtils.isEmpty(suitablePhysique)){
            String[] split = suitablePhysique.split(",");
            GridLayoutManager manager=new GridLayoutManager(this,7);
            manager.setSmoothScrollbarEnabled(true);
            label.setLayoutManager(manager);
            label.setHasFixedSize(true);
            label.setNestedScrollingEnabled(false);
            label.addItemDecoration(new GridViewDividerItemDecoration(12,12));
            label.setAdapter(new BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_phsique, Arrays.asList(split)) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, String s) {
                    baseViewHolder.setText(R.id.content,s);
                }
            });
        }

        String introduce = data.getIntroduce().trim();
        String edibleEffect = data.getEdibleEffect().trim();
        nutritiveValue.setText(introduce+"\n"+edibleEffect);

    }

    private void initView() {
        data = getIntent().getParcelableExtra("data");
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("膳食计划");
        foodMaterialImg = (ImageView) findViewById(R.id.food_material_img);
        foodMaterialName = (TextView) findViewById(R.id.food_material_name);
        mealAmount = (TextView) findViewById(R.id.meal_amount);
        fitPopulation = (TextView) findViewById(R.id.fit_population);
        tabooPopulation = (TextView) findViewById(R.id.taboo_population);
        tvTip1 = (TextView) findViewById(R.id.tv_tip1);
        label = (RecyclerView) findViewById(R.id.label);
        tvTip2 = (TextView) findViewById(R.id.tv_tip2);
        nutritiveValue = (TextView) findViewById(R.id.nutritive_value);
    }
}
