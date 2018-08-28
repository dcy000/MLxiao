package com.gcml.mall.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.mall.adapter.MallGoodsAdapter;
import com.gcml.mall.bean.GoodsBean;
import com.gcml.mall.R;
import com.gcml.mall.adapter.MallMenuAdapter;
import com.gcml.mall.widget.GridDividerItemDecoration;

import java.util.ArrayList;

public class MallActivity extends AppCompatActivity implements MallMenuAdapter.OnMenuClickListener {

    RecyclerView menuRecycler, goodsRecycler;
    TranslucentToolBar mToolBar;
    MallMenuAdapter menuAdapter;
    MallGoodsAdapter goodsAdapter;
    ArrayList<String> menuList = new ArrayList<>();
    ArrayList<GoodsBean> goodsList = new ArrayList<>();
    MallMenuAdapter.OnMenuClickListener onMenuClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        onMenuClickListener = this;
        mToolBar = findViewById(R.id.tb_mall);
        menuRecycler = findViewById(R.id.rv_mall_menu);
        goodsRecycler = findViewById(R.id.rv_mall_goods);

        initData();
    }

    private void initData() {
        for (int i = 1; i < 21; i++) {
            menuList.add("第" + i + "条");
        }
        menuAdapter = new MallMenuAdapter(this, menuList);
        menuAdapter.setOnMenuClickListener(onMenuClickListener);
        menuRecycler.setLayoutManager(new LinearLayoutManager(this));
        menuRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        menuRecycler.setAdapter(menuAdapter);

        getMallGoods(0);
        goodsAdapter = new MallGoodsAdapter(R.layout.item_goods, goodsList);
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final LoadingDialog tipDialog;
                switch (position) {
                    case 0:
                        tipDialog = new LoadingDialog.Builder(MallActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                                .setTipWord("正在加载")
                                .create();
                        break;
                    case 1:
                        tipDialog = new LoadingDialog.Builder(MallActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("发送成功")
                                .create();
                        break;
                    case 2:
                        tipDialog = new LoadingDialog.Builder(MallActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("发送失败")
                                .create();
                        break;
                    case 3:
                        tipDialog = new LoadingDialog.Builder(MallActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_INFO)
                                .setTipWord("请勿重复操作")
                                .create();
                        break;
                    default:
                        tipDialog = new LoadingDialog.Builder(MallActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                                .setTipWord("正在加载")
                                .create();
                }
                tipDialog.show();
                goodsRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, 1000);
            }
        });
        goodsRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        goodsRecycler.addItemDecoration(new GridDividerItemDecoration(32, 32));
        goodsRecycler.setAdapter(goodsAdapter);
    }

    private void initToolBar() {
//        mToolBar.setData("健 康 商 城", R.drawable.common_icon_home, "返回", "", );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMenuClick(int position) {
        getMallGoods(position);
        goodsAdapter.notifyDataSetChanged();
    }

    private void getMallGoods(int position) {
//        NetworkApi.goods_list(mPosition, new NetworkManager.SuccessCallback<ArrayList<Goods>>() {
//            @Override
//            public void onSuccess(ArrayList<Goods> response) {
//                mData.addAll(response);
//                mAdapter.notifyDataSetChanged();
//            }
//        }, new NetworkManager.FailedCallback() {
//            @Override
//            public void onFailed(String message) {
//                ToastUtils.showShort(message);
//            }
//        });
        goodsList.clear();
        for (int i = position; i < 20; i++) {
            goodsList.add(new GoodsBean("第" + i + "个商品", R.drawable.placeholder, "" + i * 100.00));
        }
    }
}
