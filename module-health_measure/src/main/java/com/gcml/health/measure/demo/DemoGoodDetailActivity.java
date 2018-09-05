package com.gcml.health.measure.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.health.measure.R;
import com.gcml.lib_utils.base.ToolbarBaseActivity;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/4 16:35
 * created by:gzq
 * description:TODO
 */
public class DemoGoodDetailActivity extends ToolbarBaseActivity implements View.OnClickListener {

    private DemoGoodDetail data;
    private ImageView mGoodsImage;
    /**
     * 快速心电检测PC-80B
     */
    private TextView mGoodsName;
    /**
     * 198
     */
    private TextView mGoodsPrice;
    private LinearLayout mLinearlayout1;
    private ImageView mReduceMount;
    /**
     * 1
     */
    private TextView mGoodsMount;
    private ImageView mAddMount;
    /**
     * 总价：198
     */
    private TextView mGoodsSumPrice;
    /**
     * 购买
     */
    private Button mShopping;
    /**
     * 导联连续测量 17种分析结果 10小时存储 PC软件支持
     */
    private TextView mTvDescription;

    public static void startActivity(Context context, DemoGoodDetail detail) {
        context.startActivity(new Intent(context, DemoGoodDetailActivity.class)
                .putExtra("data", detail));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_good_detail);
        initView();
        mTitleText.setText("商 品 详 情");
        data = getIntent().getParcelableExtra("data");

        mGoodsImage.setImageResource(data.getImg());
        mGoodsName.setText(data.getTitle());
        mTvDescription.setText(data.getDescription());
        mGoodsPrice.setText(data.getPrice());
    }

    private void initView() {
        mGoodsImage = (ImageView) findViewById(R.id.goods_image);
        mGoodsName = (TextView) findViewById(R.id.goods_name);
        mGoodsPrice = (TextView) findViewById(R.id.goods_price);
        mLinearlayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
        mGoodsMount = (TextView) findViewById(R.id.goods_mount);
        mGoodsSumPrice = (TextView) findViewById(R.id.goods_sum_price);
        mTvDescription = (TextView) findViewById(R.id.tv_description);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.shopping) {
        } else {
        }
    }
}
