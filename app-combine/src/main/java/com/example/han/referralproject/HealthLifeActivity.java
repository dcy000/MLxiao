package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.Arrays;

@Route(path = "/app/health/life/activity")
public class HealthLifeActivity extends ToolbarBaseActivity {
    private RecyclerView mRvLayout;
    private String[] menus = {"中医体质", "寿命计算器", "色盲测试"};
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_life);
        initRecycleview();
    }

    private void initRecycleview() {
        mRvLayout = (RecyclerView) findViewById(R.id.rv_layout);
        mRvLayout.setLayoutManager(new GridLayoutManager(this, 2));
        mRvLayout.setAdapter(adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_layout_health_life, Arrays.asList(menus)) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
//                helper.setText(R.id.button, item);
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
