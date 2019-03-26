package com.gcml.mall.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.mall.R;
import com.gcml.mall.adapter.OrderListAdapter;
import com.gcml.mall.bean.OrderBean;
import com.gcml.mall.network.MallRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderListActivity extends AppCompatActivity {

    TranslucentToolBar mToolBar;
    PtrClassicFrameLayout mFrameLayout;
    RecyclerView mRecyclerView;
    MallRepository mallRepository;
    List<OrderBean> orderList;
    OrderListAdapter mOrderAdapter;
    int mCurrPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        mallRepository = new MallRepository();

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_order_list);
        mRecyclerView = findViewById(R.id.rv_order_list);
        mFrameLayout = findViewById(R.id.pt_order_list);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "欢迎来到订单列表", false);
        mToolBar.setData("订 单 列 表", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
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

        mFrameLayout.disableWhenHorizontalMove(true);
        mFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mCurrPage = 1;
                getOrderList(mCurrPage);
            }
        });

        orderList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(OrderListActivity.this));
        mOrderAdapter = new OrderListAdapter(orderList, OrderListActivity.this);
        mRecyclerView.setAdapter(mOrderAdapter);
        mOrderAdapter.setEnableLoadMore(true);
        mOrderAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mOrderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrPage += 1;
                getOrderList(mCurrPage);
            }
        }, mRecyclerView);
        getOrderList(mCurrPage);
    }

    @SuppressLint("CheckResult")
    private void getOrderList(int page) {
        mallRepository.orderFromApi("2", "0", "1", "fghnnny", String.valueOf(page), "1000")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<OrderBean>>() {
                    @Override
                    public void onNext(List<OrderBean> body) {
                        super.onNext(body);
                        if (mCurrPage == 1) {
                            orderList.clear();
                        }
                        orderList.addAll(body);
                        mOrderAdapter.setNewData(orderList);
                        mFrameLayout.refreshComplete();
                        mOrderAdapter.loadMoreEnd();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (mCurrPage > 1) {
                            mOrderAdapter.loadMoreEnd();
                        }
                        ToastUtils.showShort("请求失败");
                    }
                });
    }

}
