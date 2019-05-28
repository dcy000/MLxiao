package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = "/app/health/life/activity")
public class HealthLifeActivity extends ToolbarBaseActivity {
    private RecyclerView mRvLayout;
    private BaseQuickAdapter<HealthLifeBean, BaseViewHolder> adapter;
    private List<HealthLifeBean> menus = new ArrayList<>();

    {
        menus.add(new HealthLifeBean("中医体质辨识", "测测你是什么体质？", R.drawable.ic_health_life_1));
        menus.add(new HealthLifeBean("寿命计算器", "算算你能活多久？", R.drawable.ic_health_life_1));
        menus.add(new HealthLifeBean("色盲测试图", "你看看能认出几张测试图？", R.drawable.ic_health_life_1));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_life);
        initRecycleview();
    }

    private void initRecycleview() {
        mTitleText.setText("健 康 生 活");
        mRvLayout = (RecyclerView) findViewById(R.id.rv_layout);
        mRvLayout.setLayoutManager(new GridLayoutManager(this, 2));
        mRvLayout.setAdapter(adapter = new BaseQuickAdapter<HealthLifeBean, BaseViewHolder>(R.layout.item_layout_health_life, menus) {
            @Override
            protected void convert(BaseViewHolder helper, HealthLifeBean item) {
//                helper.setText(R.id.button, item);
                helper.setText(R.id.main_title, item.getMainTitle());
                helper.setText(R.id.sub_title, item.getSubTitle());
                ((ImageView) helper.getView(R.id.iv_image)).setImageResource(item.getMenuImage());
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    //中医体质
                    Routerfit.register(AppRouter.class).skipOlderHealthManagementSerciveActivity();
                }
                if (position == 1) {
                    //寿命计算器
                }
                if (position == 2) {
                    //色盲测试
                }
            }
        });
    }
}
