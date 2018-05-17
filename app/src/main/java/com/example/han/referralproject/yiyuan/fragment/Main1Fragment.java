package com.example.han.referralproject.yiyuan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.video.VideoListActivity;
import com.google.gson.Gson;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;
import com.medlink.danbogh.utils.T;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/5/17.
 */

public class Main1Fragment extends Fragment {
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.iv_qianyue)
    ImageView ivQianyue;
    @BindView(R.id.celianglishi)
    ImageView celianglishi;
    @BindView(R.id.tijian)
    ImageView tijian;
    @BindView(R.id.zhengzhuangzichan)
    ImageView zhengzhuangzichan;
    @BindView(R.id.yishengjianyi)
    ImageView yishengjianyi;
    @BindView(R.id.jiankangketang)
    ImageView jiankangketang;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main1_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(final UserInfo response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(getActivity())
                                .load(response.getuser_photo())
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .tag(this)
                                .fit()
                                .into(ivHead);
                    }
                });
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    @OnClick({R.id.iv_head, R.id.iv_qianyue, R.id.celianglishi, R.id.tijian, R.id.zhengzhuangzichan, R.id.yishengjianyi, R.id.jiankangketang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                gotoPersonInfo();
                break;
            case R.id.iv_qianyue:
                gotoQianyueYiSheng();
                break;
            case R.id.celianglishi:
                gotoCeLiangLiShi();
                break;
            case R.id.tijian:
                // TODO: 2018/5/17
                break;
            case R.id.zhengzhuangzichan:
                gotoZhengZhuangZiCha();
                break;
            case R.id.yishengjianyi:
                gotoYiShengJianYi();
                break;
            case R.id.jiankangketang:
                gotoJianKangJiangTang();
                break;
        }
    }

    private void gotoPersonInfo() {
        startActivity(new Intent(getActivity(), MyBaseDataActivity.class));
    }


    private void gotoZhengZhuangZiCha() {
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
    }

    private void gotoYiShengJianYi() {
        startActivity(new Intent(getActivity(), MessageActivity.class));
    }

    private void gotoCeLiangLiShi() {
        startActivity(new Intent(getActivity(), HealthRecordActivity.class));
    }

    private void gotoJianKangJiangTang() {
        startActivity(new Intent(getActivity(), VideoListActivity.class));
    }

    private void gotoQianyueYiSheng() {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if ("1".equals(response.getState())) {
                    //已签约
                    startActivity(new Intent(getActivity(),
                            DoctorappoActivity.class));
                } else if ("0".equals(response.getState())
                        && (TextUtils.isEmpty(response.getDoctername()))) {
                    //未签约
                    Intent intent = new Intent(getActivity(),
                            OnlineDoctorListActivity.class);
                    intent.putExtra("flag", "contract");
                    startActivity(intent);
                } else {
                    // 待审核
                    Intent intent = new Intent(getActivity(),
                            CheckContractActivity.class);
                    startActivity(intent);
                }
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
