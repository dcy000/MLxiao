package com.example.han.referralproject.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.referralproject.video.VideoListActivity;
import com.example.module_mall.ui.OrderListActivity;
import com.example.module_pay.ui.PayActivity;
import com.example.module_person.R2;
import com.example.module_setting.ui.SettingActivity;
import com.example.module_tools.ui.ToolsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/12.
 */

public class PersonDetail2Fragment extends Fragment {
    @BindView(R2.id.iv_pay)
    ImageView ivLaorenYule;
    @BindView(R2.id.iv_order)
    ImageView ivYoujiaoWenyu;
    @BindView(R2.id.iv_shezhi)
    ImageView ivShezhi;
    @BindView(R2.id.iv_tools)
    ImageView ivTools;
    Unbinder unbinder;
    @BindView(R2.id.main_iv_health_class)
    ImageView mainIvHealthClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.example.module_person.R.layout.fragmeng_person_detail2, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R2.id.iv_pay, R2.id.iv_order, R2.id.iv_shezhi, R2.id.iv_tools, R2.id.main_iv_health_class})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == com.example.module_person.R.id.iv_order) {
            startActivity(new Intent(getActivity(), OrderListActivity.class));

        } else if (i == com.example.module_person.R.id.iv_pay) {
            startActivity(new Intent(getActivity(), PayActivity.class));

        } else if (i == com.example.module_person.R.id.iv_shezhi) {
            startActivity(new Intent(getActivity(), SettingActivity.class));

        } else if (i == com.example.module_person.R.id.iv_tools) {
            startActivity(new Intent(getActivity(), ToolsActivity.class));

        } else if (i == com.example.module_person.R.id.main_iv_health_class) {
            startActivity(new Intent(getActivity(), VideoListActivity.class));

        }
    }
}
