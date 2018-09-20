package com.gcml.common.recommend.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.business.R;
import com.gcml.common.recommend.adapter.RecommendAdapter;

import java.util.Arrays;
import java.util.List;

public class RencommendFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView tvLookMore;
    private TextView tvCommendText;
    private RecyclerView rvCommendGoods;
    private IChangToolbar iChangToolbar;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }
    public RencommendFragment() {
    }

    public static RencommendFragment newInstance(String param1, String param2) {
        RencommendFragment fragment = new RencommendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rencommend, container, false);
        bindView(view);
        bindData();
        return view;
    }


    private void bindView(View view) {
        tvCommendText = (TextView) view.findViewById(R.id.tv_commend_text);
        tvLookMore = (TextView) view.findViewById(R.id.tv_look_more);
        rvCommendGoods = (RecyclerView) view.findViewById(R.id.rv_commend_goods);

        tvLookMore.setOnClickListener(v -> {
        });

    }

    private void bindData() {

        GridLayoutManager layout = new GridLayoutManager(getActivity(), 3);
//        rvCommendGoods.addItemDecoration(new GridDividerItemDecoration(UiUtils.pt(108), 0));
        rvCommendGoods.setLayoutManager(layout);
        rvCommendGoods.setAdapter(new RecommendAdapter(R.layout.layout_recommend_item, getData()));
    }

    public List<Object> getData() {
        return Arrays.asList("药品名1", "药品名2", "药品名3");
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }
}
