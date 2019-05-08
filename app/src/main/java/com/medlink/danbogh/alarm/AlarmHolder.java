package com.medlink.danbogh.alarm;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.common.utils.RxUtils;
import com.suke.widget.SwitchButton;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/9/25.
 */

public class AlarmHolder extends RecyclerView.ViewHolder
        implements SwitchButton.OnCheckedChangeListener {

    private final AlarmRepository alarmRepository;
    TextView tvNoon;
    SwitchButton sbEnableAlarm;
    TextView tvTime;
    TextView tvContent;
    ConstraintLayout clAlarm;

    public AlarmModel mModel;

    public AlarmHolder(View itemView, AlarmRepository alarmRepository) {
        super(itemView);
        this.alarmRepository = alarmRepository;
        tvNoon = itemView.findViewById(R.id.tv_noon);
        sbEnableAlarm = itemView.findViewById(R.id.sb_enable_alarm);
        tvTime = itemView.findViewById(R.id.tv_time);
        tvContent = itemView.findViewById(R.id.tv_content);
        clAlarm = itemView.findViewById(R.id.cl_alarm);
        clAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClAlarmClicked();
            }
        });
        clAlarm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onClAlarmLongClicked();
            }
        });
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

    public void onClAlarmClicked() {
        long id = mModel.getId();
        Context context = itemView.getContext();
        if (context != null) {
            ((AlarmList2Activity) context).openAlarmDetailActivity(id);
        }
    }

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
        mModel.setEnabled(isChecked);
        Context context = itemView.getContext();
        if (context != null) {
            alarmRepository.update(mModel)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            AlarmHelper.setupAlarms(context);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter((LifecycleOwner) context))
                    .subscribe(new DefaultObserver<Object>() {
                        @Override
                        public void onNext(Object o) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }
}
