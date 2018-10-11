package com.gcml.mall.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.mall.adapter.MallGoodsAdapter;
import com.gcml.mall.bean.CategoryBean;
import com.gcml.mall.R;
import com.gcml.mall.adapter.MallMenuAdapter;
import com.gcml.mall.bean.GoodsBean;
import com.gcml.mall.network.MallRepository;
import com.gcml.mall.widget.GridDividerItemDecoration;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MallActivity extends AppCompatActivity implements MallMenuAdapter.OnMenuClickListener {

    Context mContext;
    RecyclerView menuRecycler, goodsRecycler;
    TranslucentToolBar mToolBar;
    MallMenuAdapter menuAdapter;
    MallGoodsAdapter goodsAdapter;
    List<CategoryBean> menuList = new ArrayList<>();
    List<GoodsBean> goodsList = new ArrayList<>();
    MallMenuAdapter.OnMenuClickListener onMenuClickListener;
    MallRepository mallRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        onMenuClickListener = this;
        mContext = this;

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_mall);
        menuRecycler = findViewById(R.id.rv_mall_menu);
        goodsRecycler = findViewById(R.id.rv_mall_goods);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，欢迎来到健康商城", false);
        mToolBar.setData("健 康 商 城", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                finish();
            }
        });

        mallRepository = new MallRepository();
        getCategory();
        getMallGoods("1");
        goodsAdapter = new MallGoodsAdapter(R.layout.item_mall_goods, goodsList);
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean goods = goodsList.get(position);
//                CC.obtainBuilder("com.gcml.mall.mall.goods").addParam("goods", goods).build().callAsync();
                CC.obtainBuilder("com.gcml.mall.mall.order").build().callAsync();
            }
        });
        goodsRecycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        goodsRecycler.addItemDecoration(new GridDividerItemDecoration(16, 16));
        goodsRecycler.setAdapter(goodsAdapter);
    }


    @Override
    public void onMenuClick(int position) {
        getMallGoods(String.valueOf(menuList.get(position).mallProductTypeId));
    }

    @SuppressLint("CheckResult")
    private void getCategory() {
        mallRepository.categoryFromApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<CategoryBean>>() {
                    @Override
                    public void onNext(List<CategoryBean> body) {
                        super.onNext(body);
                        menuList = body;
                        menuAdapter = new MallMenuAdapter(mContext, menuList);
                        menuAdapter.setOnMenuClickListener(onMenuClickListener);
                        menuRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                        menuRecycler.setAdapter(menuAdapter);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getMallGoods(String state) {
        mallRepository.goodsFromApi(state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<GoodsBean>>() {
                    @Override
                    public void onNext(List<GoodsBean> body) {
                        super.onNext(body);
                        goodsList.clear();
                        goodsList.addAll(body);
                        goodsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }
}
