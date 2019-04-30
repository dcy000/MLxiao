package com.gcml.mall.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
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
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
@Route(path ="/mall/mall/activity")
public class MallActivity extends AppCompatActivity implements MallMenuAdapter.OnMenuClickListener {

    Context mContext;
    TextView goodsTextTip;
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
        goodsTextTip = findViewById(R.id.tv_mall_goods);
        menuRecycler = findViewById(R.id.rv_mall_menu);
        goodsRecycler = findViewById(R.id.rv_mall_goods);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "欢迎来到健康商城", false);
        mToolBar.setData("健 康 商 城", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                Routerfit.register(AppRouter.class).skipMainActivity();
                finish();
            }
        });

        mallRepository = new MallRepository();
        getCategory();
        getMallGoods("-1");
        goodsAdapter = new MallGoodsAdapter(R.layout.item_mall_goods, goodsList);
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean goods = goodsList.get(position);
                CC.obtainBuilder("com.gcml.mall.mall.goods").addParam("goods", goods).build().callAsync();
            }
        });
        goodsRecycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        goodsRecycler.addItemDecoration(new GridDividerItemDecoration(8, 8));
        goodsRecycler.setAdapter(goodsAdapter);
    }


    @Override
    public void onMenuClick(int position) {
        getMallGoods(String.valueOf(menuList.get(position).mallProductTypeId));
    }

    @SuppressLint("CheckResult")
    private void getCategory() {
        menuList.add(new CategoryBean(-1, "小E推荐", -1));
        mallRepository.categoryFromApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<CategoryBean>>() {
                    @Override
                    public void onNext(List<CategoryBean> body) {
                        super.onNext(body);
                        menuList.addAll(body);
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
        LoadingDialog dialog = new LoadingDialog.Builder(mContext)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        if (state.equals("-1")) {
            mallRepository.recommendFromApi(UserSpHelper.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            dialog.show();
                        }
                    })
                    .doOnTerminate(new Action() {
                        @Override
                        public void run() throws Exception {
                            dialog.dismiss();
                        }
                    })
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<List<GoodsBean>>() {
                        @Override
                        public void onNext(List<GoodsBean> body) {
                            goodsList.clear();
                            goodsList.addAll(body);
//                            goodsTextTip.setVisibility(View.VISIBLE);
                            goodsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            if (throwable.getMessage().equals("暂无记录")) {
                                goodsList.clear();
//                                goodsTextTip.setVisibility(View.VISIBLE);
                                goodsAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            mallRepository.goodsFromApi(state)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            dialog.show();
                        }
                    })
                    .doOnTerminate(new Action() {
                        @Override
                        public void run() throws Exception {
                            dialog.dismiss();
                        }
                    })
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<List<GoodsBean>>() {
                        @Override
                        public void onNext(List<GoodsBean> body) {
                            super.onNext(body);
                            goodsList.clear();
                            goodsList.addAll(body);
//                            goodsTextTip.setVisibility(View.GONE);
                            goodsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
