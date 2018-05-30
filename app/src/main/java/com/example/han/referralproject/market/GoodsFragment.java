package com.example.han.referralproject.market;


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
import com.example.han.referralproject.R;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.shopping.GoodDetailActivity;
import com.example.han.referralproject.shopping.Goods;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

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
        NetworkApi.goods_list(mPosition, new NetworkManager.SuccessCallback<ArrayList<Goods>>() {
            @Override
            public void onSuccess(ArrayList<Goods> response) {
                mData.addAll(response);
                mAdapter.notifyDataSetChanged();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
//                T.show(message);
            }
        });
    }

}


