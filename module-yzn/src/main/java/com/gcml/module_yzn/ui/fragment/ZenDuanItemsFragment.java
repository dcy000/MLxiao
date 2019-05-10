package com.gcml.module_yzn.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.adapter.ItemAdapter;
import com.gcml.module_yzn.bean.OutBean;
import com.gcml.module_yzn.ui.YiZhiTangDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by afirez on 18-1-6.
 */

public class ZenDuanItemsFragment extends Fragment {

    private RecyclerView rvGoods;
    private List<OutBean.LinksBean.DataBean> beans = new ArrayList<>();
    private ItemAdapter mAdapter;


    public static ZenDuanItemsFragment newInstance(List<OutBean.LinksBean.DataBean> position) {
        Bundle args = new Bundle();
        args.putSerializable("position", (Serializable) position);
        ZenDuanItemsFragment fragment = new ZenDuanItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ZenDuanItemsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            beans = (List<OutBean.LinksBean.DataBean>) arguments.getSerializable("position");
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
        rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvGoods.addItemDecoration(new GridViewDividerItemDecoration(0, 32));
        mAdapter = new ItemAdapter(R.layout.zenduan_item, beans);
        rvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OutBean.LinksBean.DataBean item = beans.get(position);
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                Intent intent = new Intent(activity, YiZhiTangDetailActivity.class);
                intent.putExtra("itemUrl", item.link);
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
    }

}


