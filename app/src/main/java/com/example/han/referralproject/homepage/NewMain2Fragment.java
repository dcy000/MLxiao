package com.example.han.referralproject.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.cc.CCHealthMeasureActions;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.tcm.SymptomCheckActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.video.VideoListActivity;
import com.gcml.lib_utils.base.RecycleBaseFragment;
import com.gcml.lib_widget.EclipseImageView;
import com.gcml.old.auth.profile.PersonDetailActivity;
import com.google.gson.Gson;
import com.ml.edu.OldRouter;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 17:24
 * created by:gzq
 * description:主页第2页
 */
public class NewMain2Fragment extends RecycleBaseFragment implements View.OnClickListener {
    private EclipseImageView mIvPersonCenter;
    private EclipseImageView mIvHealthCourse;
    private EclipseImageView mEntertainmentCenter;
    private EclipseImageView mIvCommunicate;
    private EclipseImageView mIvCheckSelf;
    private EclipseImageView mIvShoppingMall;
    private EclipseImageView mIvAskDoctor;

    @Override
    protected int initLayout() {
        return R.layout.fragment_newmain2;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mIvPersonCenter = view.findViewById(R.id.iv_person_center);
        mIvPersonCenter.setOnClickListener(this);
        mIvHealthCourse = view.findViewById(R.id.iv_health_course);
        mIvHealthCourse.setOnClickListener(this);
        mEntertainmentCenter = view.findViewById(R.id.entertainment_center);
        mEntertainmentCenter.setOnClickListener(this);
        mIvCommunicate = view.findViewById(R.id.iv_communicate);
        mIvCommunicate.setOnClickListener(this);
        mIvCheckSelf = view.findViewById(R.id.iv_check_self);
        mIvCheckSelf.setOnClickListener(this);
        mIvShoppingMall = view.findViewById(R.id.iv_shopping_mall);
        mIvShoppingMall.setOnClickListener(this);
        mIvAskDoctor = view.findViewById(R.id.iv_ask_doctor);
        mIvAskDoctor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_person_center:
                startActivity(new Intent(getContext(), PersonDetailActivity.class));
                break;
            case R.id.iv_health_course:
                startActivity(new Intent(getActivity(), VideoListActivity.class));
//                CCHealthMeasureActions.jump2HealthIntelligentDetectionActivity();
                break;
            case R.id.entertainment_center:
//                OldRouter.routeToOldHomeActivity(getActivity());
            CC.obtainBuilder("app.component.recreation").build().callAsync();
                break;
            case R.id.iv_communicate:
                startActivity(new Intent(getContext(), SpeechSynthesisActivity.class));
                break;
            case R.id.iv_check_self:
               startActivity(new Intent(getContext(), SymptomCheckActivity.class));
                break;
            case R.id.iv_shopping_mall:
                startActivity(new Intent(getContext(), MarketActivity.class));
                break;
            case R.id.iv_ask_doctor:
                startActivity(new Intent(getContext(), DoctorAskGuideActivity.class));
                break;
        }
    }
}
