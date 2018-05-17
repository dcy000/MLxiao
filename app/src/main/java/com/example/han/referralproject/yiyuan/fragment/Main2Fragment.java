package com.example.han.referralproject.yiyuan.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.ml.edu.OldRouter;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/5/17.
 */

public class Main2Fragment extends Fragment {
    @BindView(R.id.shangcheng)
    ImageView shangcheng;
    @BindView(R.id.yule)
    ImageView yule;
    @BindView(R.id.shezhi)
    ImageView shezhi;
    @BindView(R.id.tuichu)
    ImageView tuichu;
    @BindView(R.id.liaotian)
    ImageView liaotian;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main2_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.shangcheng, R.id.yule, R.id.shezhi, R.id.tuichu, R.id.liaotian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shangcheng:
                gotoShangCheng();
                break;
            case R.id.yule:
                gotoYule();
                break;
            case R.id.shezhi:
                gotoSheZhi();
                break;
            case R.id.tuichu:
                TuiChu();
                break;
            case R.id.liaotian:
                gotoLiaoTian();
                break;
        }
    }

    private void TuiChu() {
        MobclickAgent.onProfileSignOff();
        NimAccountHelper.getInstance().logout();
        LocalShared.getInstance(getActivity()).loginOut();
        getActivity().startActivity(new Intent(getActivity(), ChooseLoginTypeActivity.class));
        getActivity().finish();
    }

    private void gotoSheZhi() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    private void gotoYule() {
        OldRouter.routeToOldHomeActivity(getActivity());
    }

    private void gotoShangCheng() {
        startActivity(new Intent(getActivity(), MarketActivity.class));
    }

    private void gotoLiaoTian() {
        startActivity(new Intent(getActivity(), SpeechSynthesisActivity.class));
    }
}
