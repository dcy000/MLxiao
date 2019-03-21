package com.gcml.module_inquiry.inquiry.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.FilterClickListener;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.picker.SelectAdapter;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.InquiryBaseFrament;

import java.util.ArrayList;
import java.util.List;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class HeightFragment extends InquiryBaseFrament implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView rvContent;
    private TextView tvUnit;
    private TextView tvGoBack;
    private TextView tvGoForward;

    private SelectAdapter adapter;
    private ArrayList<String> mStrings;
    protected int selectedPosition = 20;

    public HeightFragment() {
    }

    public static HeightFragment newInstance(String param1, String param2) {
        HeightFragment fragment = new HeightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_height;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rvContent = view.findViewById(R.id.rv_sign_up_content);
        tvUnit = view.findViewById(R.id.tv_sign_up_unit);
        tvGoBack = view.findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = view.findViewById(R.id.tv_sign_up_go_forward);

        tvGoBack.setOnClickListener(new FilterClickListener(this));
        tvGoForward.setOnClickListener(new FilterClickListener(this));

        tvUnit.setText("cm");
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        adapter = new SelectAdapter();
        adapter.setStrings(getStrings());
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvContent.smoothScrollToPosition(position);
            }
        });
        layoutManager.attach(rvContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                select((mStrings == null ? String.valueOf(position) : mStrings.get(position)));
            }
        });

        rvContent.setAdapter(adapter);
    }

    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 150; i < 200; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    public void select(String text) {
        ToastUtils.showShort(text);
    }


    @Override
    public void onClick(View v) {
        if (listenerAdapter != null) {
            if (v.getId() == R.id.tv_sign_up_go_back) {
                listenerAdapter.onBack();
            } else if (v.getId() == R.id.tv_sign_up_go_forward) {
                String height = mStrings.get(selectedPosition);
                listenerAdapter.onNext(height);
            }
        }

    }

}
