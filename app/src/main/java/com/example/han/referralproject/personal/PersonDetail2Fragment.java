package com.example.han.referralproject.personal;

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
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.tool.ToolsActivity;
import com.example.han.referralproject.video.VideoListActivity;
import com.ml.edu.OldRouter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/12.
 */

public class PersonDetail2Fragment extends Fragment {
    @BindView(R.id.iv_laoren_yule)
    ImageView ivLaorenYule;
    @BindView(R.id.iv_youjiao_wenyu)
    ImageView ivYoujiaoWenyu;
    @BindView(R.id.iv_shezhi)
    ImageView ivShezhi;
    @BindView(R.id.iv_tools)
    ImageView ivTools;
    Unbinder unbinder;
    @BindView(R.id.main_iv_health_class)
    ImageView mainIvHealthClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmeng_person_detail2, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_laoren_yule, R.id.iv_youjiao_wenyu, R.id.iv_shezhi, R.id.iv_tools, R.id.main_iv_health_class})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_laoren_yule:
                OldRouter.routeToOldHomeActivity(getActivity());
                break;
            case R.id.iv_youjiao_wenyu:
                break;
            case R.id.iv_shezhi:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.iv_tools:
                startActivity(new Intent(getActivity(), ToolsActivity.class));
            case R.id.main_iv_health_class:
                startActivity(new Intent(getActivity(), VideoListActivity.class));
                break;
        }
    }
}
