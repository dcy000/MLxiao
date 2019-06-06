package com.gcml.mod_doc_advisory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.ScreenUtils;
import com.gcml.common.widget.SpaceItemDecoration;
import com.gcml.common.widget.SpacesItemDecoration;
import com.gcml.mod_doc_advisory.R;
import com.gcml.mod_doc_advisory.adapter.DoctorAdapter;
import com.gcml.mod_doc_advisory.adapter.SpinnerAdapter;
import com.gcml.mod_doc_advisory.bean.Docter;
import com.gcml.mod_doc_advisory.net.QianYueRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class RecoDocActivity extends ToolbarBaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<Docter> mlist;
    DoctorAdapter mDoctorAdapter;
    private int mCurrPage = 9;//默认加载9调数据

    public View mTvContractOffline;
    public TextView tvGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco_doc);
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

         mlist= new ArrayList<>();
        tvGoBack = findViewById(R.id.tv_sign_up_go_back);
        tvGoBack.setOnClickListener(this);

        mTvContractOffline = findViewById(R.id.tv_sign_up_contract_offline);
        mTvContractOffline.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.forum_list);


        Spinner sp = findViewById(R.id.spinner);
        Spinner sp1 = findViewById(R.id.spinner1);
        Spinner sp2 = findViewById(R.id.spinner2);

        setSpinner(sp,sp1,sp2);

        setAdapter();
        getData();

    }

    private void setSpinner(Spinner sp,Spinner sp1,Spinner sp2) {
        List<String> list = new ArrayList<String>();
        list.add("杭州");
        list.add("上海");
        list.add("北京");

        SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, list);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));
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
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));

            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        List<String> list2 = new ArrayList<String>();
        list2.add("萧山第一人民医院");
        list2.add("萧山开发区医院");
        SpinnerAdapter adapter2 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, list2);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getData() {
        new QianYueRepository()
                .doctor_list(0,mCurrPage)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<Docter>>() {
                    @Override
                    public void onNext(List<Docter> docters) {
                        mlist.clear();
                        mlist.addAll(docters);
                        mDoctorAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_sign_up_contract_offline) {
            Routerfit.register(AppRouter.class).skipOfflineActivity();

        } else if (i == R.id.tv_sign_up_go_back) {//                CCFaceRecognitionActions.jump2RegisterHead2XunfeiActivity(this);
            finish();

        }
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
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),getString(R.string.tips_doctor));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        SpacesItemDecoration decoration = new SpacesItemDecoration(-1);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dip2px(-5)));
        mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
        mRecyclerView.setAdapter(mDoctorAdapter);

        mDoctorAdapter.setOnItemClistListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Intent intent = new Intent(RecoDocActivity.this, DoctorMesActivity.class);
                intent.putExtra("docMsg", mlist.get(postion));
                intent.putExtra("sign", "0");
                startActivity(intent);

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {}

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                // 屏幕滑动后停止（空闲状态）
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition >= totalItemCount - 1 && visibleItemCount > 0) {
                    mCurrPage += 9;
                    getData();
                }
            }


        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDoctorAdapter = null;
    }
}
