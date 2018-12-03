package com.example.lib_alarm_clock.ui;

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
import com.example.lib_alarm_clock.AlarmHelper;
import com.example.lib_alarm_clock.R;
import com.example.lib_alarm_clock.R2;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.AlarmModel;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AlarmDetail2Activity extends ToolbarBaseActivity {

    public static Intent newLaunchIntent(Context context, long id) {
        Intent intent = new Intent(context, AlarmDetail2Activity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("id", id);
        return intent;
    }

    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.alarm_detail_sp_repeat)
    Spinner spRepeat;
    @BindView(R2.id.alarm_detail_et_content)
    EditText etContent;
    @BindView(R2.id.alarm_detail_tv_cancel)
    TextView tvCancel;
    @BindView(R2.id.alarm_detail_tv_confirm)
    TextView tvConfirm;
    //    @BindView(R.id.alarm_detail_tp_time)
//    TimePicker tpTime;
    @BindView(R2.id.wrv_hour)
    WheelRecyclerView wrvHour;
    @BindView(R2.id.wrv_minute)
    WheelRecyclerView wrvMinute;

    private Unbinder mUnbinder;

    private AlarmModel mModel;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_alarm_detail2;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);

        spRepeat.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, getResources().getStringArray(R.array.repeats)));
        wrvHour.setData(provideHours());
        wrvMinute.setData(provideMinutes());
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
        } else {
            mModel = DataSupport.find(AlarmModel.class, id);
        }
        show(mModel);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
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

    @OnClick(R2.id.iv_back)
    public void onIvBackClicked() {
        finish();
    }

    @OnClick(R2.id.alarm_detail_tv_cancel)
    public void onTvCancelClicked() {
        finish();
    }

    @OnClick(R2.id.alarm_detail_tv_confirm)
    public void onTvConfirmClicked() {
        updateModel();
        AlarmHelper.cancelAlarms(this);
        if (mModel.getId() < 0) {
            mModel.save();
        } else {
            mModel.update(mModel.getId());
        }
        AlarmHelper.setupAlarms(this);
        setResult(Activity.RESULT_OK);
        finish();
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
            Calendar calendar = Calendar.getInstance();
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

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
