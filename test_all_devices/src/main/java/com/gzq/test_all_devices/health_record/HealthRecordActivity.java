package com.gzq.test_all_devices.health_record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.date_picker.DialogWheelYearMonthDay;
import com.gzq.test_all_devices.R;


public class HealthRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvRecordQrcode;
    private RadioButton mRbRecordTemperature;
    private RadioButton mRbRecordBloodPressure;
    private RadioButton mRbRecordBloodGlucose;
    private RadioButton mRbRecordBloodOxygen;
    private RadioButton mRbRecordHeartRate;
    private RadioButton mRbRecordPulse;
    private RadioButton mRbRecordCholesterol;
    private RadioButton mRbRecordBua;
    private RadioButton mRbRecordEcg;
    private RadioButton mRbRecordWeight;
    private RadioGroup mRgHealthRecord;
    private TextView mTvTimeUnit;
    private TextView mTvTimeStart;
    private TextView mTvTimeEnd;
    private LinearLayout mLlSelectTime;
    private FrameLayout mHealthRecordFl;
    private View mDialoHealthRecordUnitView;
    private TextView mUnitDayDialoHealthRecordUnitView;
    private TextView mUnitWeekDialoHealthRecordUnitView;
    private TextView mUnitMonthDialoHealthRecordUnitView;
    private TextView mUnitHalfYearDialoHealthRecordUnitView;
    private BaseDialog baseDialog;
    private static final String TAG = "HealthRecordActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_health_recoed);
        initView();
        initDialog();
    }

    private void initDatePicker() {
        DialogWheelYearMonthDay dialogWheelYearMonthDay = new DialogWheelYearMonthDay(this,
                1900, 2099,2022,8,25);
        dialogWheelYearMonthDay.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + dialogWheelYearMonthDay.getSelectorYear() + "." +
                        dialogWheelYearMonthDay.getSelectorMonth() + "." +
                        dialogWheelYearMonthDay.getSelectorDay());
                dialogWheelYearMonthDay.dismiss();
            }
        });
        dialogWheelYearMonthDay.getCancleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWheelYearMonthDay.dismiss();
            }
        });
        dialogWheelYearMonthDay.show();
    }

    private void initDialog() {
        baseDialog = new BaseDialog(this);
        baseDialog.setContentView(mDialoHealthRecordUnitView);
        baseDialog.setCanceledOnTouchOutside(true);

    }


    private void initView() {
        mTvRecordQrcode = (TextView) findViewById(R.id.tv_record_qrcode);
        mRbRecordTemperature = (RadioButton) findViewById(R.id.rb_record_temperature);
        mRbRecordBloodPressure = (RadioButton) findViewById(R.id.rb_record_blood_pressure);
        mRbRecordBloodGlucose = (RadioButton) findViewById(R.id.rb_record_blood_glucose);
        mRbRecordBloodOxygen = (RadioButton) findViewById(R.id.rb_record_blood_oxygen);
        mRbRecordHeartRate = (RadioButton) findViewById(R.id.rb_record_heart_rate);
        mRbRecordPulse = (RadioButton) findViewById(R.id.rb_record_pulse);
        mRbRecordCholesterol = (RadioButton) findViewById(R.id.rb_record_cholesterol);
        mRbRecordBua = (RadioButton) findViewById(R.id.rb_record_bua);
        mRbRecordEcg = (RadioButton) findViewById(R.id.rb_record_ecg);
        mRbRecordWeight = (RadioButton) findViewById(R.id.rb_record_weight);
        mRgHealthRecord = (RadioGroup) findViewById(R.id.rg_health_record);
        mTvTimeUnit = (TextView) findViewById(R.id.tv_time_unit);
        mTvTimeUnit.setOnClickListener(this);
        mTvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        mTvTimeStart.setOnClickListener(this);
        mTvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        mTvTimeEnd.setOnClickListener(this);
        mLlSelectTime = (LinearLayout) findViewById(R.id.ll_select_time);
        mHealthRecordFl = (FrameLayout) findViewById(R.id.health_record_fl);
        mDialoHealthRecordUnitView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialo_health_record_unit, null);
        mUnitDayDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView.findViewById(R.id.unit_day);
        mUnitDayDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitWeekDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView.findViewById(R.id.unit_week);
        mUnitWeekDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitMonthDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView.findViewById(R.id.unit_month);
        mUnitMonthDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitHalfYearDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView.findViewById(R.id.unit_half_year);
        mUnitHalfYearDialoHealthRecordUnitView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_time_unit:
                if (!baseDialog.isShowing())
                    baseDialog.show();
                break;
            case R.id.tv_time_start:
                initDatePicker();
                break;
            case R.id.tv_time_end:
                break;
            case R.id.unit_day:
                baseDialog.dismiss();
                mTvTimeUnit.setText("天");
                break;
            case R.id.unit_week:
                baseDialog.dismiss();
                mTvTimeUnit.setText("周");
                break;
            case R.id.unit_month:
                baseDialog.dismiss();
                mTvTimeUnit.setText("月");
                break;
            case R.id.unit_half_year:
                baseDialog.dismiss();
                mTvTimeUnit.setText("半年");
                break;
        }
    }
}
