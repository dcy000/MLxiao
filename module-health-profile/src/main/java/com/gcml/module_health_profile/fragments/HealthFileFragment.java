package com.gcml.module_health_profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.webview.EditHealthFileActivity;

public class HealthFileFragment extends RecycleBaseFragment implements View.OnClickListener {
    /**
     * 最新建档时间：2018年1月21日
     */
    private TextView mTvLastBuildTime;
    private TextView mTvLine;
    private LinearLayout mLlName;
    private LinearLayout mLlGender;
    private LinearLayout mLlCard;
    private LinearLayout mLlDisease;
    private LinearLayout mLlSignDoctor;
    /**
     * 编辑
     */
    private TextView mTvEdit;

    public static HealthFileFragment instance() {
        return new HealthFileFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_health_file;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mTvLastBuildTime = (TextView) view.findViewById(R.id.tv_last_build_time);
        mTvLine = (TextView) view.findViewById(R.id.tv_line);
        mLlName = (LinearLayout) view.findViewById(R.id.ll_name);
        mLlGender = (LinearLayout) view.findViewById(R.id.ll_gender);
        mLlCard = (LinearLayout) view.findViewById(R.id.ll_card);
        mLlDisease = (LinearLayout) view.findViewById(R.id.ll_disease);
        mLlSignDoctor = (LinearLayout) view.findViewById(R.id.ll_sign_doctor);
        mTvEdit = (TextView) view.findViewById(R.id.tv_edit_health_file);
        mTvEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_edit_health_file) {
            getActivity().startActivity(new Intent(getActivity(), EditHealthFileActivity.class));
        } else {
        }
    }
}
