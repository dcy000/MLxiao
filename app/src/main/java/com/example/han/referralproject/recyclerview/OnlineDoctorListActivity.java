package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineDoctorListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIconBack;
    private ImageView mIconHome;


    private RecyclerView mRecyclerView;
    private List<Docter> mlist = new ArrayList<Docter>();
    DoctorAdapter mDoctorAdapter;
    private int page = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreference;
    long countdown;
    private String mFlag;


    private int limit = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_doctor_list);

        initView();

        sharedPreferences = getSharedPreferences(ConstantData.ONLINE_TIME, Context.MODE_PRIVATE);

        sharedPreference = getSharedPreferences(ConstantData.ONLINE_ID, Context.MODE_PRIVATE);

        mFlag = getIntent().getStringExtra("flag");
        if ("contract".equals(mFlag)) {
            NetworkApi.doctor_list(0, limit, new NetworkManager.SuccessCallback<ArrayList<Docter>>() {
                @Override
                public void onSuccess(ArrayList<Docter> response) {
                    mlist.clear();
                    mlist.addAll(response);
                    mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
                    mRecyclerView.setAdapter(mDoctorAdapter);
                    setData();
                }

            }, new NetworkManager.FailedCallback() {
                @Override
                public void onFailed(String message) {

                }
            });

            return;
        }


        NetworkApi.onlinedoctor_list(1, "", page, 9, new NetworkManager.SuccessCallback<ArrayList<Docter>>() {
            @Override
            public void onSuccess(ArrayList<Docter> response) {

                List<Docter> list = new ArrayList<Docter>();
                mlist.clear();
                list.addAll(response);
                mlist.addAll(list);
                mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
                mRecyclerView.setAdapter(mDoctorAdapter);

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
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(-1);
        mRecyclerView.addItemDecoration(decoration);

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(getApplicationContext(), -5)));

        mDoctorAdapter.setOnItemClistListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                if ("contract".equals(mFlag)) {
                    Intent intent = new Intent(OnlineDoctorListActivity.this, DoctorMesActivity.class);
                    intent.putExtra("docMsg", (Serializable) mlist.get(postion));
                    startActivity(intent);
                    return;
                }

                if (!"".equals(sharedPreferences.getString("online_time", ""))) {
                    countdown = System.currentTimeMillis() - Long.parseLong(sharedPreferences.getString("online_time", ""));
                    if (countdown < 300000) {
                        if (mlist.get(postion).getDocterid().equals(sharedPreference.getString("online_id", ""))) {
                            jump(postion);
                        }
                    } else {
                        jump(postion);
                    }
                } else {
                    jump(postion);
                }

              /*  Intent intent = new Intent(OnlineDoctorListActivity.this, DoctorMesActivity.class);
                intent.putExtra("docMsg", (Serializable) mlist.get(postion));
                if (!"contract".equals(mFlag)) {
                    intent.putExtra("sign", "1");
                }
                startActivity(intent);*/
//                finish();

            }
        });

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
                        if ("contract".equals(mFlag)) {
                            limit += 9;
                            NetworkApi.doctor_list(0, limit, new NetworkManager.SuccessCallback<ArrayList<Docter>>() {
                                @Override
                                public void onSuccess(ArrayList<Docter> response) {
                                    mlist.clear();
                                    mlist.addAll(response);
                                    mDoctorAdapter.notifyDataSetChanged();
                                }

                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {

                                }
                            });
                            return;
                        }

                        if (mlist.size() >= 9) {
                            page += 1;

                            NetworkApi.onlinedoctor_list(1, "", page, 9, new NetworkManager.SuccessCallback<ArrayList<Docter>>() {
                                @Override
                                public void onSuccess(ArrayList<Docter> response) {
                                    mlist.addAll(response);
                                    mDoctorAdapter.notifyDataSetChanged();

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

    public void jump(int postion) {

        Intent intent = new Intent(OnlineDoctorListActivity.this, DoctorMesActivity.class);
        intent.putExtra("docMsg", (Serializable) mlist.get(postion));
        intent.putExtra("sign", "1");
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("contract".equals(mFlag)) {
            MLVoiceSynthetize.startSynthesize("主人，请选择您最信任的医生签约");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDoctorAdapter = null;
    }


    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mIconBack = (ImageView) findViewById(R.id.icon_back);
        mIconBack.setOnClickListener(this);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);

    }


    /*  private void getData() {
        NetworkApi.getAllDoctor(null, start + "", end + "", new NetworkManager.SuccessCallback<ArrayList<AllDoctor>>() {
                    @Override
                    public void onSuccess(ArrayList<AllDoctor> response) {
                        start+=limit;
                        end+=limit;
                        mData.addAll(response);
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    private void setAdapter() {
        mList.setLayoutManager(new GridLayoutManager(this,3));
        mList.setAdapter(adapter=new AllDoctorAdapter(mData,this));
        adapter.setOnItemClistListener(new AllDoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
//                startActivity(new Intent(OnlineDoctorListActivity.this,DoctorMesActivity.class)
//                        .putExtra("docMsg", (Serializable) mData.get(postion)));
            }
        });
    }



   */


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
