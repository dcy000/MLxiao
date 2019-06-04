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
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.FenLeiInfoOutBean;
import com.gcml.module_yzn.ui.YiZhiTangDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by afirez on 18-1-6.
 */

public class ClassificationInfoFragment extends Fragment {

    private RecyclerView rvGoods;
    private List<FenLeiInfoOutBean.ItemBean> beans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;


    public static ClassificationInfoFragment newInstance(List<FenLeiInfoOutBean.ItemBean> position) {
        Bundle args = new Bundle();
        args.putSerializable("position", (Serializable) position);
        ClassificationInfoFragment fragment = new ClassificationInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ClassificationInfoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            beans = (List<FenLeiInfoOutBean.ItemBean>) arguments.getSerializable("position");
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
        mAdapter = new BaseQuickAdapter<FenLeiInfoOutBean.ItemBean, BaseViewHolder>(R.layout.zenduan_item, beans) {

            @Override
            protected void convert(BaseViewHolder helper, FenLeiInfoOutBean.ItemBean item) {
                helper.setText(R.id.name, item.name);
            }
        };
        rvGoods.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            FenLeiInfoOutBean.ItemBean item = beans.get(position);
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return;
            }
            Intent intent = new Intent(activity, YiZhiTangDetailActivity.class);
            intent.putExtra("itemUrl", item.link);
            startActivity(intent);
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


