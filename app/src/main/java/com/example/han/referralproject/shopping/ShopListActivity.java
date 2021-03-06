package com.example.han.referralproject.shopping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.DensityUtils;
import com.example.han.referralproject.recyclerview.SpaceItemDecoration;
import com.example.han.referralproject.recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ShopListActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout mLinearLayout1;
    LinearLayout mLinearLayout2;
    TextView mTextView1;
    TextView mTextView2;
    ImageView mImageView1;
    ImageView mImageView2;
    private RecyclerView mRecyclerView;
    ImageView mImageView3;
    ImageView mImageView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);


        mToolbar.setVisibility(View.VISIBLE);


        mTitleText.setText(getString(R.string.healthy_shopping));


        speak(getString(R.string.shop_good));

        mLinearLayout1 = findViewById(R.id.linearlayout1);
        mLinearLayout2 = findViewById(R.id.linearlayout2);
        mTextView1 = findViewById(R.id.healthy_equ);
        mTextView2 = findViewById(R.id.healthy_con);

        mImageView1 = findViewById(R.id.line_1);
        mImageView2 = findViewById(R.id.line_2);


        mRecyclerView = findViewById(R.id.shop_list);


        mLinearLayout1.setOnClickListener(this);
        mLinearLayout2.setOnClickListener(this);

        initData(2);
    }


    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearlayout1:

                mTextView1.setTextColor(Color.parseColor("#3E85FC"));
                mTextView2.setTextColor(Color.parseColor("#000000"));
                mImageView1.setVisibility(View.VISIBLE);
                mImageView2.setVisibility(View.GONE);
                initData(2);

                break;
            case R.id.linearlayout2:
                mTextView1.setTextColor(Color.parseColor("#000000"));
                mTextView2.setTextColor(Color.parseColor("#3E85FC"));
                mImageView1.setVisibility(View.GONE);
                mImageView2.setVisibility(View.VISIBLE);
                initData(1);

                break;

        }
    }

    private List<Goods> mlist = new ArrayList<Goods>();
    private ShopAdapter mShopAdapter;

    public void initData(int status) {

        NetworkApi.goods_list(status, new NetworkManager.SuccessCallback<ArrayList<Goods>>() {
            @Override
            public void onSuccess(ArrayList<Goods> response) {

                List<Goods> list = new ArrayList<Goods>();
                mlist.clear();
                list = response;
                mlist.addAll(list);
                setData();

                mShopAdapter.notifyDataSetChanged();

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });
    }


    public void setData() {

        if (null == mShopAdapter) {
            mRecyclerView.setHasFixedSize(true);
            final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

            mRecyclerView.setLayoutManager(layoutManager);
            SpacesItemDecoration decoration = new SpacesItemDecoration(1);
            mRecyclerView.addItemDecoration(decoration);

            mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(getApplicationContext(), 1)));

            mShopAdapter = new ShopAdapter(mlist, getApplicationContext());
            mRecyclerView.setAdapter(mShopAdapter);


            mShopAdapter.setOnItemClistListener(new ShopAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int postion) {

                    Intent intent = new Intent(ShopListActivity.this, GoodDetailActivity.class);
                    intent.putExtra("goods", mlist.get(postion));
                    startActivity(intent);

                }
            });


        }


    }

}
