package com.example.han.referralproject.yiyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.han.referralproject.yiyuan.activity.YiYuanLoginActivity;
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
    @BindView(R.id.jiankangketang)
    ImageView jiankangketang;
    @BindView(R.id.shezhi)
    ImageView shezhi;
    @BindView(R.id.tuichu)
    ImageView tuichu;
    @BindView(R.id.liaotian)
    ImageView liaotian;
    Unbinder unbinder;
    @BindView(R.id.zhanghuchongzhi)
    ImageView zhanghuchongzhi;
    @BindView(R.id.wodedingdan)
    ImageView wodedingdan;

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

    @OnClick({R.id.shangcheng, R.id.jiankangketang, R.id.shezhi, R.id.tuichu, R.id.liaotian, R.id.zhanghuchongzhi, R.id.wodedingdan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shangcheng:
                gotoWoDeDingDan();
                break;
            case R.id.jiankangketang:
                gotoShangCheng();
                break;
            case R.id.shezhi:
                gotoSheZhi();
                break;
            case R.id.tuichu:
                gotoLiaoTian();
                break;
            case R.id.liaotian:
//                tuiChu();
                break;
            case R.id.zhanghuchongzhi:
                gotoZhangHuChongZhi();
                break;
            case R.id.wodedingdan:

                break;

        }
    }

    private void gotoZhangHuChongZhi() {
        startActivity(new Intent(getActivity(), PayActivity.class));
    }

    private void gotoWoDeDingDan() {
        startActivity(new Intent(getActivity(), OrderListActivity.class));
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

    private void gotoJianKangKeTang() {
        startActivity(new Intent(getActivity(), VideoListActivity.class));
    }


}
