package com.gcml.old;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.mall.R;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by afirez on 18-1-6.
 */

public class GoodsFragment extends Fragment {

    private RecyclerView rvGoods;
    private ArrayList<GoodBean> mData;
    private Goods1Adapter mAdapter;

    private int mPosition = 1;
    private View view;


    public static GoodsFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public GoodsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPosition = arguments.getInt("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods, container, false);
        rvGoods = view.findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        rvGoods.addItemDecoration(new GridViewDividerItemDecoration(0, 32));
        mData = new ArrayList<>();
        mAdapter = new Goods1Adapter(R.layout.goods_item, mData);
        rvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodBean goods = mData.get(position);
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                Routerfit.register(AppRouter.class).skipGoodDetailActivity(goods);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        GoodsRepository repository = new GoodsRepository();
        repository.goods(mPosition + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<GoodBean>>() {
                    @Override
                    public void onNext(List<GoodBean> goodBeans) {
                        view.findViewById(R.id.no_data).setVisibility(View.GONE);
                        mData.addAll(goodBeans);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                        view.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}


