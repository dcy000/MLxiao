package com.gcml.old;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.ScreenUtils;
import com.gcml.common.widget.SpaceItemDecoration;
import com.gcml.common.widget.SpacesItemDecoration;
import com.gcml.mall.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/mall/old/order/list/activity")
public class OrderListActivity extends ToolbarBaseActivity implements View.OnClickListener {

    // public ImageView mImageView1;
    private List<Orders> mlist = new ArrayList<Orders>();
    OrderAdapter mOrderAdapter;
    private int mCurrPage = 1;
    private RecyclerView mRecyclerView;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_order_list);
        mTitleText.setText("订  单  列  表");
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请查看您的订单");

        mRecyclerView = findViewById(R.id.order_list);
        mSharedPreferences = getSharedPreferences("person_message", Context.MODE_PRIVATE);
        GoodsRepository repository = new GoodsRepository();
        repository.order("2", "0", "1", UserSpHelper.getUserName(), "1", "1000")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<Orders>>() {
                    @Override
                    public void onNext(List<Orders> orders) {
                        List<Orders> list = new ArrayList<Orders>();
                        list.addAll(orders);
                        mlist.addAll(list);
                        mOrderAdapter = new OrderAdapter(mlist, getApplicationContext());
                        mRecyclerView.setAdapter(mOrderAdapter);


                        setData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

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

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dip2px(-5)));

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
                            GoodsRepository repository = new GoodsRepository();
                            repository.order("2", "4", "1", UserSpHelper.getUserName(), mCurrPage + "", "1000")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .as(RxUtils.autoDisposeConverter(OrderListActivity.this))
                                    .subscribe(new DefaultObserver<List<Orders>>() {
                                        @Override
                                        public void onNext(List<Orders> orders) {
                                            List<Orders> list = new ArrayList<Orders>();
                                            list.clear();
                                            list = orders;
                                            mlist.addAll(list);
                                            mOrderAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtils.showShort(e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });

                        }


                    }


                }
            }


        });


    }

}
