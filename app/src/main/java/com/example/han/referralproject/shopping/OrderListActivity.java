package com.example.han.referralproject.shopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.homepage.HospitalMainActivity;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.DensityUtils;
import com.example.han.referralproject.recyclerview.SpaceItemDecoration;
import com.example.han.referralproject.recyclerview.SpacesItemDecoration;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderListActivity extends BaseActivity implements View.OnClickListener {

    // public ImageView mImageView1;
    private List<Orders> mlist = new ArrayList<Orders>();
    OrderAdapter mOrderAdapter;
    private int mCurrPage = 1;
    private RecyclerView mRecyclerView;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);


        mToolbar.setVisibility(View.VISIBLE);


        mTitleText.setText(getString(R.string.orders_detail));


        speak(getString(R.string.order));


        mRecyclerView = findViewById(R.id.order_list);
        mSharedPreferences = getSharedPreferences(ConstantData.PERSON_MSG, Context.MODE_PRIVATE);

        NetworkApi.order_list("2", "0", "1", UserSpHelper.getUserName(), "1", "1000", new NetworkManager.SuccessCallback<ArrayList<Orders>>() {
            @Override
            public void onSuccess(ArrayList<Orders> response) {


                List<Orders> list = new ArrayList<Orders>();
                list.addAll(response);
                mlist.addAll(list);
                mOrderAdapter = new OrderAdapter(mlist, getApplicationContext());
                mRecyclerView.setAdapter(mOrderAdapter);


                setData();


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort(message);
            }
        });


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
        startActivity(new Intent(mContext, HospitalMainActivity.class));
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }


    public void setData() {
        /*  FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        mRecyclerView.setNestedScrollingEnabled(false);
        //设置布局管理器
        mRecyclerView.setLayoutManager(linearLayoutManager);*/

        mRecyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(0);
        mRecyclerView.addItemDecoration(decoration);

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(getApplicationContext(), -5)));

       /* mOrderAdapter.setOnItemClistListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {


            }
        });*/

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingUp;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // 表示在向上滑动
                isSlidingUp = dy > 0;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                // 屏幕滑动后停止（空闲状态）
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    // manager.getSpanCount() 跨度-》列数
                    int[] lastVisiblePosition = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);

                    // 获取recyclerview总共将会显示多少条数据（一共有多少个item）
                    int countItem = manager.getItemCount();
                    // 对数组进行升序排列
                    Arrays.sort(lastVisiblePosition);

                    // 获取数组中的最大值，即已显示的最大索引
                    int maxPosition = lastVisiblePosition[lastVisiblePosition.length - 1];

                    if ((countItem - 1) == maxPosition && isSlidingUp) {

                        if (mlist.size() >= 1300) {
                            mCurrPage += 1;


                            NetworkApi.order_list("2", "4", "1", mSharedPreferences.getString("userName", ""), mCurrPage + "", "1000", new NetworkManager.SuccessCallback<ArrayList<Orders>>() {
                                @Override
                                public void onSuccess(ArrayList<Orders> response) {


                                    List<Orders> list = new ArrayList<Orders>();
                                    list.clear();
                                    list = response;
                                    mlist.addAll(list);
                                    mOrderAdapter.notifyDataSetChanged();


                                }

                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {


                                }
                            });


                        }


                    }


                }
            }


        });


    }

}
