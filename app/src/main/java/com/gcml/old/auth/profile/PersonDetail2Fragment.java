package com.gcml.old.auth.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.tool.ToolsActivity;
import com.example.han.referralproject.video.VideoListActivity;


/**
 * Created by lenovo on 2018/3/12.
 */

public class PersonDetail2Fragment extends Fragment {

    ImageView ivLaorenYule;

    ImageView ivYoujiaoWenyu;

    ImageView ivShezhi;

    ImageView ivTools;

    ImageView mainIvHealthClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmeng_person_detail2, container, false);
        mainIvHealthClass = (ImageView) view.findViewById(R.id.main_iv_health_class);
        ivTools = (ImageView) view.findViewById(R.id.iv_tools);
        ivShezhi = (ImageView) view.findViewById(R.id.iv_shezhi);
        ivYoujiaoWenyu = (ImageView) view.findViewById(R.id.iv_order);
        ivLaorenYule = (ImageView) view.findViewById(R.id.iv_pay);
        ivLaorenYule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked(v);
            }
        });
        ivYoujiaoWenyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked(v);
            }
        });
        ivShezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked(v);
            }
        });
        ivTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked(v);
            }
        });
        mainIvHealthClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked(v);
            }
        });
        return view;
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.iv_pay:
//                startActivity(new Intent(getActivity(), PayActivity.class));
                CC.obtainBuilder("com.gcml.mall.recharge").build().callAsync();
                break;
            case R.id.iv_shezhi:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.iv_tools:
                startActivity(new Intent(getActivity(), ToolsActivity.class));
                break;
            case R.id.main_iv_health_class:
                startActivity(new Intent(getActivity(), VideoListActivity.class));
                break;
        }
    }
}
