package com.example.han.referralproject.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.recommend.adapter.RecommendAdapter;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.UiUtils;

import java.util.Arrays;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {

    private TranslucentToolBar tbRecommendTitle;
    /**
     * 根据结果​​显示，您的血糖，血压偏高，小E给您推荐以下商品
     */
    private TextView tvCommendText;
    /**
     * 查看更多
     */
    private TextView tvLookMore;
    private RecyclerView rvCommendGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        bindView();
        bindData();

    }

    private void bindData() {
        tbRecommendTitle.setData("智 能 推 荐", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        startActivity(new Intent(RecommendActivity.this, MainActivity.class));
                    }
                });

        GridLayoutManager layout = new GridLayoutManager(this, 3);
//        rvCommendGoods.addItemDecoration(new GridDividerItemDecoration(UiUtils.pt(108), 0));
        rvCommendGoods.setLayoutManager(layout);
        rvCommendGoods.setAdapter(new RecommendAdapter(R.layout.recommend_item, getData()));
    }

    private List<Object> getData() {
        return Arrays.asList("药品名1", "药品名2", "药品名3");
    }


    private void bindView() {
        tbRecommendTitle = (TranslucentToolBar) findViewById(R.id.tb_recommend_title);
        tvCommendText = (TextView) findViewById(R.id.tv_commend_text);
        tvLookMore = (TextView) findViewById(R.id.tv_look_more);
        rvCommendGoods = (RecyclerView) findViewById(R.id.rv_commend_goods);

        tvLookMore.setOnClickListener(v -> {
            ToastUtils.showShort("点击了更多");
            startActivity(new Intent(this, MarketActivity.class));
        });

    }

}

