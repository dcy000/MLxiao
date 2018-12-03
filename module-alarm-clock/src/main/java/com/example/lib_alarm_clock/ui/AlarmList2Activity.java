package com.example.lib_alarm_clock.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lib_alarm_clock.AlarmHelper;
import com.example.lib_alarm_clock.AlarmsAdapter;
import com.example.lib_alarm_clock.R;
import com.example.lib_alarm_clock.R2;
import com.example.lib_alarm_clock.bean.ClueInfoBean;
import com.example.lib_alarm_clock.service.AlarmAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.AlarmModel;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/9/26.
 */

public class AlarmList2Activity extends ToolbarBaseActivity {

    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.icon_home)
    ImageView icon_home;
    @BindView(R2.id.alarm_list_rv_alarms)
    RecyclerView rvAlarms;
    @BindView(R2.id.alarm_list_tv_add_alarm)
    TextView tvAddAlarm;
    private AlarmsAdapter mAdapter;
    public Unbinder mUnbinder;

    public static Intent newLaunchIntent(Context context) {
        Intent intent = new Intent(context, AlarmList2Activity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_alarm_list2;
    }

    @Override
    public void initParams(Intent intentArgument) {
        Box.getRetrofit(AlarmAPI.class)
                .getAllAlarmClocks(Box.getUserId())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<ClueInfoBean>>() {
                    @Override
                    public void onNext(List<ClueInfoBean> clueInfoBeans) {
                        if (clueInfoBeans == null || clueInfoBeans.size() == 0) {
                            return;
                        }
                        StringBuilder mBuilder = new StringBuilder();

                        for (ClueInfoBean itemBean : clueInfoBeans) {
                            mBuilder.append(clueInfoBeans.get(0).doctername).append("提醒您").append(itemBean.cluetime).append("吃").append(itemBean.medicine);
                        }
                        MLVoiceSynthetize.startSynthesize(mBuilder.toString());
                    }
                });
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        mTitleText.setText(R.string.medication_reminder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        rvAlarms.setLayoutManager(layoutManager);
        mAdapter = new AlarmsAdapter();
        rvAlarms.setAdapter(mAdapter);
        refresh();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize("主人，请设置吃药提醒");
    }

    private void refresh() {
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        if (models != null) {
            Iterator<AlarmModel> iterator = models.iterator();
            while (iterator.hasNext()) {
                AlarmModel model = iterator.next();
                String tag = model.getTag();
                if (tag != null && !tag.isEmpty()) {
                    iterator.remove();
                }
            }

            Collections.sort(models, new Comparator<AlarmModel>() {
                @Override
                public int compare(AlarmModel model1, AlarmModel model2) {
                    if (model1.getHourOfDay() > model2.getHourOfDay()
                            || (model1.getHourOfDay() == model2.getHourOfDay() &&
                            model1.getMinute() > model2.getMinute())) {
                        return 1;
                    }
                    if (model1.getHourOfDay() < model2.getHourOfDay()
                            || (model1.getHourOfDay() == model2.getHourOfDay() &&
                            model1.getMinute() < model2.getMinute())) {
                        return -1;
                    }
                    return 0;
                }
            });

            mAdapter.replaceAll(models);
        }
    }

    @OnClick(R2.id.alarm_list_tv_add_alarm)
    public void onTvAddAlarmClicked() {
        openAlarmDetailActivity(-1);
    }

    public void openAlarmDetailActivity(long id) {
        Intent intent = AlarmDetail2Activity.newLaunchIntent(this, id);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
            if (models != null) {
                Iterator<AlarmModel> iterator = models.iterator();
                while (iterator.hasNext()) {
                    AlarmModel model = iterator.next();
                    String tag = model.getTag();
                    if (tag != null && !tag.isEmpty()) {
                        iterator.remove();
                    }
                }

                Collections.sort(models, new Comparator<AlarmModel>() {
                    @Override
                    public int compare(AlarmModel model1, AlarmModel model2) {
                        if (model1.getHourOfDay() > model2.getHourOfDay()
                                || (model1.getHourOfDay() == model2.getHourOfDay() &&
                                model1.getMinute() > model2.getMinute())) {
                            return 1;
                        }
                        if (model1.getHourOfDay() < model2.getHourOfDay()
                                || (model1.getHourOfDay() == model2.getHourOfDay() &&
                                model1.getMinute() < model2.getMinute())) {
                            return -1;
                        }
                        return 0;
                    }
                });

                mAdapter.replaceAll(models);
            }
        }
    }

    public void onDeleteAlarm(long id) {
        final long alarmId = id;
        new AlertDialog(this)
                .builder()
                .setMsg("您确定要删除吗?")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelAlarm(alarmId);
                    }
                }).show();

    }

    public void cancelAlarm(long alarmId) {
        AlarmHelper.cancelAlarms(this);
        DataSupport.delete(AlarmModel.class, alarmId);
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        if (models != null) {
            Iterator<AlarmModel> iterator = models.iterator();
            while (iterator.hasNext()) {
                AlarmModel model = iterator.next();
                String tag = model.getTag();
                if (tag != null && !tag.isEmpty()) {
                    iterator.remove();
                }
            }

            Collections.sort(models, new Comparator<AlarmModel>() {
                @Override
                public int compare(AlarmModel model1, AlarmModel model2) {
                    if (model1.getHourOfDay() > model2.getHourOfDay()
                            || (model1.getHourOfDay() == model2.getHourOfDay() &&
                            model1.getMinute() > model2.getMinute())) {
                        return 1;
                    }
                    if (model1.getHourOfDay() < model2.getHourOfDay()
                            || (model1.getHourOfDay() == model2.getHourOfDay() &&
                            model1.getMinute() < model2.getMinute())) {
                        return -1;
                    }
                    return 0;
                }
            });

            mAdapter.replaceAll(models);
        }
        AlarmHelper.setupAlarms(this);
    }

    public void setAlarmEnabled(long id, boolean enabled) {
//        AlarmHelper.cancelAlarms(this);

        AlarmModel temp = new AlarmModel();
        temp.setEnabled(enabled);
        temp.update(id);
        AlarmHelper.setupAlarms(this);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
