package com.medlink.danbogh.alarm;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.suke.widget.SwitchButton;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by lenovo on 2017/9/25.
 */

public class AlarmHolder extends RecyclerView.ViewHolder
        implements SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.tv_noon)
    TextView tvNoon;
    @BindView(R.id.sb_enable_alarm)
    SwitchButton sbEnableAlarm;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.cl_alarm)
    ConstraintLayout clAlarm;

    public AlarmModel mModel;

    public AlarmHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        sbEnableAlarm.setOnCheckedChangeListener(this);
    }

    public void onBind(AlarmModel model) {
        mModel = model;
        sbEnableAlarm.setChecked(model.isEnabled());
        tvContent.setText(mModel.getContent());
        tvNoon.setText(model.getHourOfDay() > 12 ? "下午" : "上午");
        String time = String.format(Locale.CHINA,
                "%02d : %02d", model.getHourOfDay(), model.getMinute());
        tvTime.setText(time);
    }

    @OnClick(R.id.cl_alarm)
    public void onClAlarmClicked() {
        long id = mModel.getId();
        Context context = itemView.getContext();
        if (context != null) {
            ((AlarmList2Activity) context).openAlarmDetailActivity(id);
        }
    }

    @OnLongClick(R.id.cl_alarm)
    public boolean onClAlarmLongClicked() {
        long id = mModel.getId();
        Context context = itemView.getContext();
        if (context != null) {
            ((AlarmList2Activity) context).onDeleteAlarm(id);
        }
        return true;
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        long id = mModel.getId();
        mModel.setEnabled(isChecked);
        Context context = itemView.getContext();
        if (context != null) {
            ((AlarmList2Activity) context).setAlarmEnabled(id, isChecked);
        }
    }
}
