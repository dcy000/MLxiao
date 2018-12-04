package com.example.module_mall.ui;


import android.content.Intent;
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
import com.example.module_mall.R;
import com.example.module_mall.adapter.Goods1Adapter;
import com.example.module_mall.bean.Goods;
import com.example.module_mall.service.MallAPI;
import com.gcml.lib_widget.recycleview.GridViewDividerItemDecoration;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afirez on 18-1-6.
 */

public class GoodsFragment extends Fragment {

    private RecyclerView rvGoods;
    private ArrayList<Goods> mData;
    private Goods1Adapter mAdapter;

    private int mPosition = 1;


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
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        rvGoods = view.findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvGoods.addItemDecoration(new GridViewDividerItemDecoration(0, 32));
        mData = new ArrayList<>();
        mAdapter = new Goods1Adapter(R.layout.goods_item, mData);
        rvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Goods goods = mData.get(position);
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                Intent intent = new Intent(activity, GoodDetailActivity.class);
                intent.putExtra("goods", goods);
                startActivity(intent);
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
        Box.getRetrofit(MallAPI.class)
                .getGoods(mPosition)
                .compose(RxUtils.<List<Goods>>httpResponseTransformer())
                .as(RxUtils.<List<Goods>>autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<Goods>>() {
                    @Override
                    public void onNext(List<Goods> goods) {
                        mData.addAll(goods);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

}


