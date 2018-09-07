package com.example.han.referralproject.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.gcml.lib_utils.base.RecycleBaseFragment;
import com.gcml.lib_widget.EclipseImageView;
import com.medlink.danbogh.alarm.AlarmList2Activity;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 17:24
 * created by:gzq
 * description:主页第3页
 */
public class NewMain3Fragment extends RecycleBaseFragment implements View.OnClickListener {

    private EclipseImageView mIvEntertainmentEnter;
    private EclipseImageView mIvEatMedicine;

    @Override
    protected int initLayout() {
        return R.layout.fragment_newmain3;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mIvEntertainmentEnter = (EclipseImageView) view.findViewById(R.id.iv_entertainment_enter);
        mIvEntertainmentEnter.setOnClickListener(this);
        mIvEatMedicine = (EclipseImageView) view.findViewById(R.id.iv_eat_medicine);
        mIvEatMedicine.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_entertainment_enter:
                //娱乐中心
                CC.obtainBuilder("app.component.recreation").build().callAsync();
                break;
            case R.id.iv_eat_medicine:
                Intent intentAlarm = AlarmList2Activity.newLaunchIntent(getActivity());
                startActivity(intentAlarm);
                break;
        }
    }
}
