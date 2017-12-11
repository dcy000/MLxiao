package com.example.han.referralproject.shopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.DensityUtils;
import com.example.han.referralproject.recyclerview.Docter;
import com.example.han.referralproject.recyclerview.DoctorAdapter;
import com.example.han.referralproject.recyclerview.DoctorMesActivity;
import com.example.han.referralproject.recyclerview.RecoDocActivity;
import com.example.han.referralproject.recyclerview.SpaceItemDecoration;
import com.example.han.referralproject.recyclerview.SpacesItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderListActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView mImageView1;
    private List<Orders> mlist = new ArrayList<Orders>();
    OrderAdapter mOrderAdapter;
    private int mCurrPage = 1;
    private RecyclerView mRecyclerView;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        mImageView1 = (ImageView) findViewById(R.id.icon_back);



        mImageView1.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.order_list);

        mSharedPreferences = getSharedPreferences(ConstantData.PERSON_MSG, Context.MODE_PRIVATE);


        NetworkApi.order_list("2", "1", "1", mSharedPreferences.getString("userName", ""), "1", "4", new NetworkManager.SuccessCallback<ArrayList<Orders>>() {
            @Override
            public void onSuccess(ArrayList<Orders> response) {

                Log.e("==========", response.toString());

                List<Orders> list = new ArrayList<Orders>();
                mlist.clear();
                list.addAll(response);
                mlist.addAll(list);
                mOrderAdapter = new OrderAdapter(mlist, getApplicationContext());
                mRecyclerView.setAdapter(mOrderAdapter);

                setData();


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

                Log.e("=============", "失败");

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                finish();
                break;
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
                if (dy > 0) {
                    isSlidingUp = true;
                } else {
                    isSlidingUp = false;
                }
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

                        if (mlist.size() >= 4) {
                            mCurrPage += 1;


                            NetworkApi.order_list("2", "1", "1", mSharedPreferences.getString("userName", ""), mCurrPage + "", "4", new NetworkManager.SuccessCallback<ArrayList<Orders>>() {
                                @Override
                                public void onSuccess(ArrayList<Orders> response) {

                                    Log.e("==========", response.toString());

                                    List<Orders> list = new ArrayList<Orders>();
                                    list.clear();
                                    list = response;
                                    mlist.addAll(list);
                                    mOrderAdapter.notifyDataSetChanged();


                                }

                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {

                                    Log.e("=============", "失败");

                                }
                            });


                        }


                    }


                }
            }


        });


    }

}
