package com.gcml.module_yzn.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.WenJuanOutBean;
import com.gcml.module_yzn.ui.YiZhiTangDetailActivity;

import java.io.Serializable;
import java.util.List;

public class WenJuanFragment extends Fragment {
    private static final String ARG_PARAM1 = "itemData";
    private static final String ARG_PARAM2 = "param2";

    private String mParam2;
    private RecyclerView rvItems;

    List<WenJuanOutBean.ItemBean> items;

    public WenJuanFragment() {
    }

    public static WenJuanFragment newInstance(List<WenJuanOutBean.ItemBean> param1, String param2) {
        WenJuanFragment fragment = new WenJuanFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (List<WenJuanOutBean.ItemBean>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wen_juan, container, false);
        rvItems = view.findViewById(R.id.rv_wenjuan_items);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        BaseQuickAdapter<WenJuanOutBean.ItemBean, BaseViewHolder> adapter = new BaseQuickAdapter<WenJuanOutBean.ItemBean,
                BaseViewHolder>(R.layout.zenduan_item, items) {
            @Override
            protected void convert(BaseViewHolder helper, WenJuanOutBean.ItemBean item) {
                helper.setText(R.id.name, item.title);
            }
        };
        rvItems.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            Intent intent = new Intent(getActivity(), YiZhiTangDetailActivity.class);
            intent.putExtra("itemUrl", items.get(position).link);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
