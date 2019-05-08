package com.medlink.danbogh.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crazypumpkin.versatilerecyclerview.library.WheelRecyclerView;
import com.example.han.referralproject.R;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/alarm/details2/activity")
public class AlarmDetail2Activity extends ToolbarBaseActivity {

    public static Intent newLaunchIntent(Context context, long id) {
        Intent intent = new Intent(context, AlarmDetail2Activity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("id", id);
        return intent;
    }

    ImageView ivBack;
    Spinner spRepeat;
    EditText etContent;
    TextView tvCancel;
    TextView tvConfirm;
    WheelRecyclerView wrvHour;
    WheelRecyclerView wrvMinute;

    private AlarmRepository alarmRepository = new AlarmRepository();

    private AlarmModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail2);
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(R.string.medication_reminder);
        mRightView.setVisibility(View.GONE);
        long id = getIntent().getLongExtra("id", -1);
        if (id == -1) {
            mModel = new AlarmModel();
            Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            mModel.setHourOfDay(hourOfDay);
            mModel.setMinute(minute);
            show(mModel);
        } else {
            alarmRepository.findOneById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<AlarmModel>() {
                        @Override
                        public void onNext(AlarmModel alarmModel) {
                            mModel = alarmModel;
                            show(mModel);
                        }
                    });
        }
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIvBackClicked();
            }
        });
        tvCancel = findViewById(R.id.alarm_detail_tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvCancelClicked();
            }
        });
        tvConfirm = findViewById(R.id.alarm_detail_tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvConfirmClicked();
            }
        });
        spRepeat = findViewById(R.id.alarm_detail_sp_repeat);
        etContent = findViewById(R.id.alarm_detail_et_content);
        wrvHour = findViewById(R.id.wrv_hour);
        wrvMinute = findViewById(R.id.wrv_minute);
        spRepeat.setAdapter(new ArrayAdapter<>(this, R.layout.common_item_spinner_layout, getResources().getStringArray(R.array.repeats)));
        wrvHour.setData(provideHours());
        wrvMinute.setData(provideMinutes());
    }

    private List<String> provideMinutes() {
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes.add("0" + i);
            } else {
                minutes.add("" + i);
            }
        }
        return minutes;
    }

    private List<String> provideHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            } else {
                hours.add("" + i);
            }
        }
        return hours;
    }

    private void show(AlarmModel model) {
        wrvHour.setSelect(model.getHourOfDay());
        wrvMinute.setSelect(model.getMinute());
        int interval = model.getInterval();
        if (interval > 2) {
            for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                if (model.hasDayOfWeek(dayOfWeek)) {
                    interval = dayOfWeek + 3;
                }
            }
        }
        spRepeat.setSelection(interval);
        etContent.setText(model.getContent());
    }

    public void onIvBackClicked() {
        finish();
    }

    public void onTvCancelClicked() {
        finish();
    }

    public void onTvConfirmClicked() {
        Observable.just("updateAlarms")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        updateModel(); // 从 UI 更新 alarm
                        AlarmHelper.cancelAlarms(AlarmDetail2Activity.this);
                        return s;
                    }
                })
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<Object> apply(String s) throws Exception {
                        if (mModel.getId() < 0) {
                            return alarmRepository.add(mModel).subscribeOn(Schedulers.io());
                        } else {
                            return alarmRepository.update(mModel).subscribeOn(Schedulers.io());
                        }
                    }
                })
                .map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) throws Exception {
                        AlarmHelper.setupAlarms(AlarmDetail2Activity.this);
                        return o;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });

    }

    private void updateModel() {
        int hourOfDay = wrvHour.getSelected();
        int minute = wrvMinute.getSelected();
        String content = etContent.getText().toString().trim();
        mModel.setHourOfDay(hourOfDay);
        mModel.setMinute(minute);
        mModel.setContent(content);
        int interval = spRepeat.getSelectedItemPosition();
        if (interval == AlarmModel.INTERVAL_NONE) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            mModel.setTimestamp(calendar.getTimeInMillis());
        }
        if (interval > 2) {
            int dayOfWeek = interval - 3;
            mModel.addDayOfWeek(dayOfWeek);
        }
        mModel.setInterval(interval);
        mModel.setEnabled(true);
    }
}
