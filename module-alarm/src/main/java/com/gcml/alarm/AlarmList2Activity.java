package com.gcml.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.dialog.AlertDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/9/26.
 */
@Route(path = "/app/alarm/list/activity")
public class AlarmList2Activity extends ToolbarBaseActivity {

    ImageView ivBack;
    ImageView icon_home;
    RecyclerView rvAlarms;
    TextView tvAddAlarm;
    private AlarmsAdapter mAdapter;

    private AlarmRepository alarmRepository = new AlarmRepository();

    public static Intent newLaunchIntent(Context context) {
        Intent intent = new Intent(context, AlarmList2Activity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list2);
        mToolbar.setVisibility(View.GONE);
        ivBack = findViewById(R.id.iv_back);
        icon_home = findViewById(R.id.icon_home);
        rvAlarms = findViewById(R.id.alarm_list_rv_alarms);
        tvAddAlarm = findViewById(R.id.alarm_list_tv_add_alarm);
        tvAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvAddAlarmClicked();
            }
        });
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(R.string.medication_reminder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        rvAlarms.setLayoutManager(layoutManager);
        mAdapter = new AlarmsAdapter();
        mAdapter.alarmRepository = alarmRepository;
        rvAlarms.setAdapter(mAdapter);
        refresh();

        alarmRepository.getClue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<ClueInfoBean>>() {
                    @Override
                    public void onNext(List<ClueInfoBean> clueInfoBeans) {
                        StringBuilder mBuilder = new StringBuilder();
                        for (ClueInfoBean itemBean : clueInfoBeans) {
                            mBuilder.append(clueInfoBeans.get(0).doctername)
                                    .append("提醒您")
                                    .append(itemBean.cluetime)
                                    .append("吃")
                                    .append(itemBean.medicine);
                        }
                        String tips = mBuilder.toString();
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), tips);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请设置吃药提醒");
    }

    private void refresh() {
        alarmRepository.findAll()
                .map(new Function<List<AlarmEntity>, List<AlarmEntity>>() {
                    @Override
                    public List<AlarmEntity> apply(List<AlarmEntity> models) throws Exception {
                        return sort(models);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<AlarmEntity>>() {
                    @Override
                    public void onNext(List<AlarmEntity> models) {
                        mAdapter.replaceAll(models);
                    }
                });
    }

    private List<AlarmEntity> sort(List<AlarmEntity> models) {
        Iterator<AlarmEntity> iterator = models.iterator();
        ArrayList<AlarmEntity> list = new ArrayList<>();
        while (iterator.hasNext()) {
            AlarmEntity model = iterator.next();
            String tag = model.getTag();
            if (tag != null && !tag.isEmpty()) {
                iterator.remove();
            }
        }

        Collections.sort(models, new Comparator<AlarmEntity>() {
            @Override
            public int compare(AlarmEntity model1, AlarmEntity model2) {
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
        return models;
    }

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
            refresh();
        }
    }

    public void onDeleteAlarm(long id) {
        final long alarmId = id;
        new AlertDialog(this)
                .builder()
                .setMsg("您确定要删除吗?")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelAlarm(alarmId);
                    }
                }).show();
    }

    public void cancelAlarm(long alarmId) {
        Observable.just("cancelAlarm")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        AlarmHelper.cancelAlarms(AlarmList2Activity.this);
                        return s;
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return alarmRepository.delete(alarmId)
                                .map(new Function<Object, String>() {
                                    @Override
                                    public String apply(Object o) throws Exception {
                                        return s;
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                })
                .flatMap(new Function<String, ObservableSource<List<AlarmEntity>>>() {
                    @Override
                    public ObservableSource<List<AlarmEntity>> apply(String s) throws Exception {
                        return alarmRepository.findAll()
                                .map(new Function<List<AlarmEntity>, List<AlarmEntity>>() {
                                    @Override
                                    public List<AlarmEntity> apply(List<AlarmEntity> models) throws Exception {
                                        return sort(models);
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<AlarmEntity>>() {
                    @Override
                    public void accept(List<AlarmEntity> models) throws Exception {
                        mAdapter.replaceAll(models);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<AlarmEntity>>() {
                    @Override
                    public void onNext(List<AlarmEntity> models) {
                        AlarmHelper.setupAlarms(AlarmList2Activity.this);
                    }
                });

    }

    public void setAlarmEnabled(long id, boolean enabled) {
//        AlarmHelper.cancelAlarms(this);

//        AlarmEntity temp = new AlarmEntity();
//        temp.setEnabled(enabled);
//        temp.update(id);
//        AlarmHelper.setupAlarms(this);
    }

}
