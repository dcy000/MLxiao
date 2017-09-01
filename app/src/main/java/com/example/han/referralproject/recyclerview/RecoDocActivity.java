package com.example.han.referralproject.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoDocActivity extends AppCompatActivity {

    private ListView mateListView;
    private EditText editText;

    public String keyWords;
    private List<String> list = new ArrayList<String>();
    private List<String> mInfoList = new ArrayList<String>();

    private RecyclerView mRecyclerView;
    private List<Doctor> mlist = new ArrayList<Doctor>();
    DoctorAdapter mDoctorAdapter;
    private int mCurrPage = 0;
    Toolbar mToolBar;
    TextView mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco_doc);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_main);

        initToolBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.forum_list);

        List<String> list = new ArrayList<String>();
        list.add("杭州");
        list.add("上海");
        list.add("北京");
        SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, list);
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));
                ;
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        List<String> list1 = new ArrayList<String>();
        list1.add("萧山");
        list1.add("滨江");
        list1.add("西湖");
        SpinnerAdapter adapter1 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, list1);
        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));
                ;
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        List<String> list2 = new ArrayList<String>();
        list2.add("萧山第一人民医院");
        list2.add("萧山开发区医院");
        SpinnerAdapter adapter2 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, list2);
        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));
                ;
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        initData();

    }

    private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (TextView) findViewById(R.id.title_content);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //    mTitleText.setText(str);
        mTitleText.setTextSize(25);
        //    mTitleText.setGravity(Gravity.CENTER);

    }

    // 处理点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RecoDocActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
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


      /*  mDoctorAdapter = new DoctorAdapter(mlist, this);
        mRecyclerView.setAdapter(mDoctorAdapter);*/

        mDoctorAdapter.setOnItemClistListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Intent intent = new Intent(RecoDocActivity.this, DoctorMesActivity.class);
                intent.putExtra("docMsg", (Serializable) mlist.get(postion));
                startActivity(intent);

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
                        mCurrPage = mCurrPage + 1;
                        loadMore();
                        //   initData();
                    }


                }
            }


        });


    }

    public void loadMore() {

        RetrofitService retrofitService = RetrofitClient.getClient();
        // 创建有一个回调对象
        Call<List<Doctor>> call = retrofitService.ShowDocMsg("ShowDocServlet", 9 * mCurrPage);
        // 用回调对象发起请求
        call.enqueue(new Callback<List<Doctor>>() {

            // 回调方法
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    List<Doctor> list = new ArrayList<Doctor>();
                    list = response.body();
                    mlist.addAll(list);

                    mDoctorAdapter.notifyDataSetChanged();
                } else {

                }
            }

            // 返回http状态码非成功的回调方法
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

            }
        });

    }


    public void initData() {
        RetrofitService retrofitService = RetrofitClient.getClient();
        // 创建有一个回调对象
        Call<List<Doctor>> call = retrofitService.LoardMore("LoadMoreServlet", mCurrPage);
        // 用回调对象发起请求
        call.enqueue(new Callback<List<Doctor>>() {
            // 回调方法
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    List<Doctor> list = new ArrayList<Doctor>();
                    mlist.clear();
                    list = response.body();
                    mlist.addAll(list);
                    mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
                    mRecyclerView.setAdapter(mDoctorAdapter);

                    setData();

                    //    mContactAdapter.notifyDataSetChanged();

                } else {

                }
            }

            // 返回http状态码非成功的回调方法
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

            }
        });

    }

}
