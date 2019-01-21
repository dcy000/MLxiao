package com.gcml.module_health_profile.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.R;

public class ZhongyiFollowupFragment extends RecycleBaseFragment implements View.OnClickListener {
    private ImageView mIvCircle;
    /**
     * 中医体质辨识
     */
    private TextView mTvCTitle;
    /**
     * 距离上次体检已过去10天
     */
    private TextView mTvCContent;
    /**
     * 立即开始
     */
    private TextView mBtnNewRecord;
    private ConstraintLayout mClHead;
    /**
     * 中医体质辨识记录
     */
    private TextView mTvTitle;
    private RecyclerView mRv;
    private String recordId;

    public static ZhongyiFollowupFragment instance(String recordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);
        ZhongyiFollowupFragment zhongyiFollowupFragment = new ZhongyiFollowupFragment();
        zhongyiFollowupFragment.setArguments(bundle);
        return zhongyiFollowupFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_zhongyi_followup;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        recordId=bundle.getString("recordId");
        mIvCircle = (ImageView) view.findViewById(R.id.iv_circle);
        mTvCTitle = (TextView) view.findViewById(R.id.tv_c_title);
        mTvCContent = (TextView) view.findViewById(R.id.tv_c_content);
        mBtnNewRecord = (TextView) view.findViewById(R.id.btn_new_record);
        mBtnNewRecord.setOnClickListener(this);
        mClHead = (ConstraintLayout) view.findViewById(R.id.cl_head);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_new_record) {
            CC.obtainBuilder("app.chinese.consititution").build().call();
        } else {
        }
    }
}
