package com.gcml.module_inquiry.inquiry.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.InquiryBaseFrament;

/**
 * Created by lenovo on 2019/3/21.
 */

public class PregnancyFragment extends InquiryBaseFrament implements View.OnClickListener {

    private ImageView mIvMan;
    private ImageView mIvWoman;
    private ImageView mIvRbMan;
    private ImageView mIvRbWoman;
    private TextView mTvBack;
    private TextView mTvGoForward;

    @Override
    protected int layoutId() {
        return R.layout.fragment_pregmancy;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mIvMan = view.findViewById(R.id.iv_sign_up_man);
        mIvWoman = view.findViewById(R.id.iv_sign_up_woman);
        mIvRbMan = view.findViewById(R.id.iv_rb_sign_up_man);
        mIvRbWoman = view.findViewById(R.id.iv_rb_sign_up_woman);
        mTvBack = view.findViewById(R.id.tv_sign_up_go_back);
        mTvGoForward = view.findViewById(R.id.tv_sign_up_go_forward);

        selectMan(true);

        mIvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMan(true);
            }
        });

        mIvWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMan(false);
            }
        });

        mTvBack.setOnClickListener(new FilterClickListener(this));
        mTvGoForward.setOnClickListener(new FilterClickListener(this));
    }

    private void selectMan(boolean select) {
        mIvRbMan.setSelected(select);
        mIvRbWoman.setSelected(!select);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (listenerAdapter != null) {
            if (id == R.id.tv_sign_up_go_back) {
                listenerAdapter.onBack("", null);
            } else if (id == R.id.tv_sign_up_go_forward) {
                listenerAdapter.onNext("5", mIvRbMan.isSelected() ? "0" : "1");
            }
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static PregnancyFragment newInstance(String param1, String param2) {
        PregnancyFragment fragment = new PregnancyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
