package com.example.han.referralproject.market;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.GoodsBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.shopping.Goods;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShanshiFragment extends Fragment {


    @BindView(R.id.list)
    RecyclerView list;
    Unbinder unbinder;
    private List<GoodsBean> mData;
    public ShanshiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shanshi, container, false);
        unbinder = ButterKnife.bind(this, view);
        mData=new ArrayList<>();
        initData();
        setAdapter();
        return view;
    }

    private void initData() {
        mData.add(new GoodsBean("血压计",R.drawable.xueyaji,"0.1"));
        mData.add(new GoodsBean("耳温枪",R.drawable.erwenqiang,"0.1"));
        mData.add(new GoodsBean("三合一",R.drawable.sanheyi,"0.1"));
        mData.add(new GoodsBean("酒精消毒片",R.drawable.jiujingxiaodu,"0.1"));
    }

    private void setAdapter() {
        list.setLayoutManager(new GridLayoutManager(getActivity(),3));
        list.addItemDecoration(new GridViewDividerItemDecoration(0,32));
        GoodsAdapter goodsAdapter=new GoodsAdapter(R.layout.goods_item,mData);
        list.setAdapter(goodsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
