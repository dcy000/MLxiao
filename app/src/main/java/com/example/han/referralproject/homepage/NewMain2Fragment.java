package com.example.han.referralproject.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.health.intelligentdetection.HealthIntelligentDetectionActivity;
import com.example.han.referralproject.personal.PersonDetailActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.gzq.administrator.lib_common.base.BaseFragment;
import com.gzq.administrator.lib_common.custom_view.EclipseImageView;
import com.ml.edu.OldRouter;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 17:24
 * created by:gzq
 * description:主页第2页
 */
public class NewMain2Fragment extends BaseFragment implements View.OnClickListener {
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
//                startActivity(new Intent(getActivity(), FirstDiagnosisActivity.class));
                startActivity(new Intent(getActivity(), HealthIntelligentDetectionActivity.class));
//                startActivity(new Intent(getActivity(), VideoListActivity.class));
                break;
            case R.id.entertainment_center:
                OldRouter.routeToOldHomeActivity(getActivity());
                break;
            case R.id.iv_communicate:
                startActivity(new Intent(getContext(), SpeechSynthesisActivity.class));
//                CC.obtainBuilder("app.component.recreation").build().callAsync();
                break;
            case R.id.iv_check_self:
                DiseaseUser diseaseUser = new DiseaseUser(
                        LocalShared.getInstance(getActivity()).getUserName(),
                        LocalShared.getInstance(getActivity()).getSex().equals("男") ? 1 : 2,
                        Integer.parseInt(LocalShared.getInstance(getActivity()).getUserAge()) * 12,
                        LocalShared.getInstance(getActivity()).getUserPhoto()
                );
                String currentUser = new Gson().toJson(diseaseUser);
                Intent intent = new Intent(getActivity(), com.witspring.unitbody.ChooseMemberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
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
