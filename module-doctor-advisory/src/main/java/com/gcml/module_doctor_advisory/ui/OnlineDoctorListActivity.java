package com.gcml.module_doctor_advisory.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.ui.ScreenUtils;
import com.gcml.common.widget.SpaceItemDecoration;
import com.gcml.common.widget.SpacesItemDecoration;
import com.gcml.module_doctor_advisory.R;
import com.gcml.module_doctor_advisory.adapter.DoctorAdapter;
import com.gcml.module_doctor_advisory.bean.Docter;
import com.gcml.module_doctor_advisory.net.QianYueRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/online/doctor/list")
public class OnlineDoctorListActivity extends ToolbarBaseActivity {

    private RecyclerView mRecyclerView;
    private List<Docter> mlist = new ArrayList<>();
    DoctorAdapter mDoctorAdapter;
    private int page = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreference;
    long countdown;
    private String mFlag;

    private int limit = 9;
    private QianYueRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlag = getIntent().getStringExtra("flag");
        setContentView(R.layout.activity_online_doctor_list);
        mToolbar.setVisibility(View.VISIBLE);
        repository = new QianYueRepository();
        if ("contract".equals(mFlag)) {
            mTitleText.setText("视  频  咨  询");
        } else {
            String title = getIntent().getStringExtra("title");
            if (TextUtils.isEmpty(title))
                mTitleText.setText("在线健康顾问");
            else
                mTitleText.setText(title);
        }
        initView();

        sharedPreferences = getSharedPreferences("online_time", Context.MODE_PRIVATE);

        sharedPreference = getSharedPreferences("online_id", Context.MODE_PRIVATE);

        if ("contract".equals(mFlag)) {
            repository.doctor_list(0, limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<List<Docter>>() {
                        @Override
                        public void onNext(List<Docter> docters) {
                            mlist.clear();
                            mlist.addAll(docters);
                            mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
                            mRecyclerView.setAdapter(mDoctorAdapter);
                            setData();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            return;
        }

        repository.onlinedoctor_list(1, "", page, 9)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<ArrayList<Docter>>() {
                    @Override
                    public void onNext(ArrayList<Docter> docters) {

                        List<Docter> list = new ArrayList<Docter>();
                        mlist.clear();
                        list.addAll(docters);
                        mlist.addAll(list);
                        mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
                        mRecyclerView.setAdapter(mDoctorAdapter);

                        setData();
                    }

                    @Override
                    public void onError(Throwable e) {

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
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(-1);
        mRecyclerView.addItemDecoration(decoration);

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dip2px(-5)));

        mDoctorAdapter.setOnItemClistListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                if ("contract".equals(mFlag)) {
                    Intent intent = new Intent(OnlineDoctorListActivity.this, DoctorMesActivity.class);
                    intent.putExtra("docMsg", mlist.get(postion));
                    intent.putExtra("fromWhere", getIntent().getStringExtra("fromWhere"));
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
                startActivityForResult(intent);*/
//                finish();

            }
        });

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
                        if ("contract".equals(mFlag)) {
                            limit += 9;
                            repository.doctor_list(0, limit)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DefaultObserver<List<Docter>>() {
                                        @Override
                                        public void onNext(List<Docter> docters) {
                                            mlist.clear();
                                            mlist.addAll(docters);
                                            mDoctorAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                            return;
                        }

                        if (mlist.size() >= 9) {
                            page += 1;
                            repository.onlinedoctor_list(1, "", page, 9)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DefaultObserver<ArrayList<Docter>>() {
                                        @Override
                                        public void onNext(ArrayList<Docter> docters) {
                                            mlist.addAll(docters);
                                            mDoctorAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onError(Throwable e) {

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

    public void jump(int postion) {

        Intent intent = new Intent(OnlineDoctorListActivity.this, DoctorMesActivity.class);
        intent.putExtra("docMsg", mlist.get(postion));
        intent.putExtra("sign", "1");
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("contract".equals(mFlag)) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "请选择您最信任的健康顾问绑定");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDoctorAdapter = null;
    }


    private void initView() {

        mRecyclerView = findViewById(R.id.list);

    }


}
