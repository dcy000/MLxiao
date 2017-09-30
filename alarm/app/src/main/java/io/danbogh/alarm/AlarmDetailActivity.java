package io.danbogh.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AlarmDetailActivity extends AppCompatActivity {

    @BindView(R.id.alarm_detail_tp_alarm)
    TimePicker tpAlarm;
    @BindView(R.id.alarm_detail_et_content)
    EditText etContent;
    @BindView(R.id.alarm_detail_swc_weekly)
    MySwitch swcWeekly;
    @BindView(R.id.alarm_detail_swc_sunday)
    MySwitch swcSunday;
    @BindView(R.id.alarm_detail_swc_monday)
    MySwitch swcMonday;
    @BindView(R.id.alarm_detail_swc_tuesday)
    MySwitch swcTuesday;
    @BindView(R.id.alarm_details_swc_wednesday)
    MySwitch swcWednesday;
    @BindView(R.id.alarm_detail_swc_thursday)
    MySwitch swcThursday;
    @BindView(R.id.alarm_detail_swc_friday)
    MySwitch swcFriday;
    @BindView(R.id.alarm_detail_swc_saturday)
    MySwitch swcSaturday;
    @BindView(R.id.alarm_detail_tv_tone)
    TextView tvTone;
    @BindView(R.id.alarm_detail_tv_select_tone)
    TextView tvSelectTone;
    public Unbinder mUnbinder;
    private AlarmModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        mUnbinder = ButterKnife.bind(this);

        long id = getIntent().getExtras().getLong("id");
        if (id == -1) {
            mModel = new AlarmModel();
        } else {
            mModel = DataSupport.find(AlarmModel.class, id);
            tpAlarm.setCurrentMinute(mModel.getMinute());
            tpAlarm.setCurrentHour(mModel.getHourOfDay());
            etContent.setText(mModel.getContent());
            swcWeekly.setChecked(mModel.getInterval() == AlarmModel.INTERVAL_WEEK);
            swcSunday.setChecked(mModel.hasDayOfWeek(Calendar.SUNDAY));
            swcMonday.setChecked(mModel.hasDayOfWeek(Calendar.MONDAY));
            swcTuesday.setChecked(mModel.hasDayOfWeek(Calendar.TUESDAY));
            swcWednesday.setChecked(mModel.hasDayOfWeek(Calendar.WEDNESDAY));
            swcThursday.setChecked(mModel.hasDayOfWeek(Calendar.THURSDAY));
            swcFriday.setChecked(mModel.hasDayOfWeek(Calendar.FRIDAY));
            swcSaturday.setChecked(mModel.hasDayOfWeek(Calendar.SATURDAY));
            tvSelectTone.setText(mModel.getTone().toString());
        }
    }

    @OnClick(R.id.alarm_detail_btn_exit)
    public void onBtnExitClicked() {
        finish();
    }

    @OnClick(R.id.alarm_detail_btn_confirm)
    public void onBtnConfirmClicked() {
        updateModelFromLayout();
        AlarmHelper.cancelAlarms(this);
        if (mModel.getId() < 0) {
            mModel.save();
        } else {
            mModel.update(mModel.getId());
        }
        AlarmHelper.setupAlarms(this);
        setResult(RESULT_OK);
        finish();
    }

    private void updateModelFromLayout() {
        mModel.setMinute(tpAlarm.getCurrentMinute());
        mModel.setHourOfDay(tpAlarm.getCurrentHour());
        mModel.setContent(etContent.getText().toString().trim());
        if (swcWeekly.isChecked()) {
            mModel.setInterval(AlarmModel.INTERVAL_WEEK);
        }
        if (swcSunday.isChecked()) {
            mModel.addDayOfWeek(Calendar.SUNDAY);
        } else {
            mModel.clearDayOfWeek(Calendar.SUNDAY);
        }
        if (swcMonday.isChecked()) {
            mModel.addDayOfWeek(Calendar.MONDAY);
        } else {
            mModel.clearDayOfWeek(Calendar.MONDAY);
        }
        if (swcTuesday.isChecked()) {
            mModel.addDayOfWeek(Calendar.TUESDAY);
        } else {
            mModel.clearDayOfWeek(Calendar.TUESDAY);
        }
        if (swcWednesday.isChecked()) {
            mModel.addDayOfWeek(Calendar.WEDNESDAY);
        } else {
            mModel.clearDayOfWeek(Calendar.WEDNESDAY);
        }
        if (swcThursday.isChecked()) {
            mModel.addDayOfWeek(Calendar.THURSDAY);
        } else {
            mModel.clearDayOfWeek(Calendar.THURSDAY);
        }
        if (swcFriday.isChecked()) {
            mModel.addDayOfWeek(Calendar.FRIDAY);
        } else {
            mModel.clearDayOfWeek(Calendar.FRIDAY);
        }
        if (swcSaturday.isChecked()) {
            mModel.addDayOfWeek(Calendar.SATURDAY);
        } else {
            mModel.clearDayOfWeek(Calendar.SATURDAY);
        }
        mModel.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
