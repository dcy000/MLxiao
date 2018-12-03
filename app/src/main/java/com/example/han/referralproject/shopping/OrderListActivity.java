package com.example.han.referralproject.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.recyclerview.DensityUtils;
import com.example.han.referralproject.service.API;
import com.example.module_doctor_advisory.utils.SpaceItemDecoration;
import com.example.module_doctor_advisory.utils.SpacesItemDecoration;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderListActivity extends BaseActivity implements View.OnClickListener {

    // public ImageView mImageView1;
    private List<Orders> mlist = new ArrayList<Orders>();
    OrderAdapter mOrderAdapter;
    private int mCurrPage = 1;
    private RecyclerView mRecyclerView;
    private UserInfoBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);


        mToolbar.setVisibility(View.VISIBLE);


        mTitleText.setText(getString(R.string.orders_detail));


        MLVoiceSynthetize.startSynthesize(R.string.order);


        mRecyclerView = (RecyclerView) findViewById(R.id.order_list);
        user = Box.getSessionManager().getUser();
        Box.getRetrofit(API.class)
                .getOrderList("2", "0", "1", user.bname, "1", "1000")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<Orders>>() {
                    @Override
                    public void onNext(List<Orders> orders) {
                        List<Orders> list = new ArrayList<Orders>();
                        list.addAll(orders);
                        mlist.addAll(list);
                        mOrderAdapter = new OrderAdapter(mlist, getApplicationContext());
                        mRecyclerView.setAdapter(mOrderAdapter);


                        setData();
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
        startActivity(new Intent(mContext, MainActivity.class));
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

                        if (mlist.size() >= 1300) {
                            mCurrPage += 1;

                            Box.getRetrofit(API.class)
                                    .getOrderList("2", "4", "1", user.bname, mCurrPage + "", "1000")
                                    .compose(RxUtils.httpResponseTransformer())
                                    .as(RxUtils.autoDisposeConverter(OrderListActivity.this))
                                    .subscribe(new CommonObserver<List<Orders>>() {
                                        @Override
                                        public void onNext(List<Orders> orders) {

                                            List<Orders> list = new ArrayList<Orders>();
                                            list.clear();
                                            list = orders;
                                            mlist.addAll(list);
                                            mOrderAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }


                    }


                }
            }


        });


    }

}
