package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.YuYueInfo;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends BaseActivity {


    SharedPreferences sharedPreferences1;


    private List<YuYueInfo> mlist = new ArrayList<YuYueInfo>();
    HistoryAdapter mHistoryAdapter;
    private int mCurrPage = 1;
    private RecyclerView mRecyclerView;

    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mToolbar.setVisibility(View.VISIBLE);


        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);

        doctorId = sharedPreferences1.getString("doctor_id", "");


        mTitleText.setText(getString(R.string.yuyue_history));


        speak(getString(R.string.yuye_history));


        mRecyclerView = (RecyclerView) findViewById(R.id.history_list);


        NetworkApi.YuYue_history(MyApplication.getInstance().userId, doctorId, 1, 1, 8, new NetworkManager.SuccessCallback<ArrayList<YuYueInfo>>() {
            @Override
            public void onSuccess(ArrayList<YuYueInfo> response) {
                List<YuYueInfo> list = new ArrayList<YuYueInfo>();
                mlist.clear();
                list.addAll(response);
                mlist.addAll(list);
                mHistoryAdapter = new HistoryAdapter(mlist, getApplicationContext());
                mRecyclerView.setAdapter(mHistoryAdapter);

                setData();


            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


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

                        if (mlist.size() >= 8) {
                            mCurrPage += 1;


                            NetworkApi.YuYue_history(MyApplication.getInstance().userId, doctorId, 1, mCurrPage, 8, new NetworkManager.SuccessCallback<ArrayList<YuYueInfo>>() {
                                @Override
                                public void onSuccess(ArrayList<YuYueInfo> response) {

                                    List<YuYueInfo> list = new ArrayList<YuYueInfo>();
                                    list.clear();
                                    list = response;
                                    mlist.addAll(list);
                                    mHistoryAdapter.notifyDataSetChanged();

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

