package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcml.family.R;
import com.gcml.family.adapter.FamilyRelativeAdapter;
import com.gcml.family.bean.FamilyBean;

import java.util.ArrayList;
import java.util.List;

public class FamilyRelativeFragment extends Fragment {

    private View mView;
    private RecyclerView mRecycler;
    private FamilyRelativeAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_family_common, container, false);
        bindView();
        bindData();
        return mView;
    }

    private void bindView() {
        mRecycler = mView.findViewById(R.id.rv_family_content);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void bindData() {
        List<FamilyBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                list.add(new FamilyBean("郭志强", "孙子"));
            } else {
                list.add(new FamilyBean("曾庆森", "儿子"));
            }
        }
        mAdapter = new FamilyRelativeAdapter(R.layout.item_family_menu, list);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CC.obtainBuilder("app.component.family.detail").build().callAsync();
            }
        });
        mRecycler.setAdapter(mAdapter);
    }
}
