package com.gzq.test_all_devices.health_record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.date_picker.DialogWheelYearMonthDay;
import com.gzq.test_all_devices.R;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordBUAFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordBloodoxygenFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordBloodpressureFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordBloodsugarFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordCholesterolFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordECGFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordHeartrateFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordTemperatureFragment;
import com.gzq.test_all_devices.health_record.health_record_fragments.HealthRecordWeightFragment;
import com.gzq.test_all_devices.net.NetworkApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HealthRecordActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, HealthRecordBloodsugarFragment.BloodsugarSelectTime {
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
    private DialogWheelYearMonthDay dialogWheelYearMonthDay;
    private int selectStartYear;
    private int selectStartMonth;
    private int selectStartDay;
    private int selectEndYear;
    private int selectEndMonth;
    private int selectEndDay;
    private int radioGroupPosition;
    private HealthRecordTemperatureFragment temperatureFragment;
    private HealthRecordBloodpressureFragment bloodpressureFragment;
    private HealthRecordBloodsugarFragment bloodsugarFragment;
    private HealthRecordBloodoxygenFragment bloodoxygenFragment;
    private HealthRecordHeartrateFragment heartrateFragment;
    private HealthRecordCholesterolFragment cholesterolFragment;
    private HealthRecordBUAFragment buaFragment;
    private HealthRecordECGFragment ecgFragment;
    private HealthRecordWeightFragment weightFragment;
    private String temp;
    private String startMillisecond;
    private String endMillisecond;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_health_recoed);
        initView();
        initDialog();
    }

    private void initDatePicker(boolean isSelectEndTime) {
        if (dialogWheelYearMonthDay == null) {
            dialogWheelYearMonthDay = new DialogWheelYearMonthDay(this,
                    1900, 2099, selectStartYear, selectStartMonth, selectStartDay);
            dialogWheelYearMonthDay.setCanceledOnTouchOutside(false);
        }
        dialogWheelYearMonthDay.setOnClickCancelListener(null);
        dialogWheelYearMonthDay.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                int selectorYear = dialogWheelYearMonthDay.getSelectorYear();
                int selectorMonth = Integer.parseInt(dialogWheelYearMonthDay.getSelectorMonth());
                int selectorDay = Integer.parseInt(dialogWheelYearMonthDay.getSelectorDay());

                Log.e(TAG, "onClick: " + selectorYear + "." +
                        selectorMonth + "." +
                        selectorDay);
                if (isSelectEndTime) {
                    if (selectorYear < selectStartYear) {
                        ToastUtils.showShort("选择时间错误");
                    } else if (selectorYear == selectStartYear && selectorMonth < selectStartMonth) {
                        ToastUtils.showShort("选择时间错误");
                    } else if (selectorYear == selectStartYear
                            && selectorMonth == selectStartMonth
                            && selectorDay < selectStartDay) {
                        ToastUtils.showShort("选择时间错误");
                    } else {
                        selectEndYear = selectorYear;
                        selectEndMonth = selectorMonth;
                        selectEndDay = selectorDay;
                        mTvTimeEnd.setText(selectorYear + "年" + selectorMonth + "月"
                                + selectorDay + '日');
                        dialogWheelYearMonthDay.dismiss();
                    }
                } else {
                    if (selectorYear > selectEndYear) {
                        ToastUtils.showShort("选择时间错误");
                    } else if (selectorYear == selectStartYear && selectorMonth > selectEndMonth) {
                        ToastUtils.showShort("选择时间错误");
                    } else if (selectorYear == selectStartYear
                            && selectorMonth == selectStartMonth
                            && selectorDay > selectStartDay) {
                        ToastUtils.showShort("选择时间错误");
                    } else {
                        selectStartYear = selectorYear;
                        selectStartMonth = selectorMonth;
                        selectStartDay = selectorDay;
                        mTvTimeStart.setText(selectorYear + "年" + selectorMonth + "月"
                                + selectorDay + "日");
                        dialogWheelYearMonthDay.dismiss();
                    }
                }

                startMillisecond = TimeUtils.string2Milliseconds(selectStartYear
                        + "-" + selectStartMonth + "-" +
                        selectStartDay, new SimpleDateFormat("yyyy-MM-dd")) + "";
                endMillisecond = TimeUtils.string2Milliseconds(selectEndYear
                        + "-" + selectEndMonth + "-" +
                        selectEndDay, new SimpleDateFormat("yyyy-MM-dd")) + "";
                switch (temp) {
                    case "1"://体温
                        getTemperatureData(endMillisecond, startMillisecond);
                        break;
                    case "2"://血压
                        getBloodpressureData(endMillisecond, startMillisecond);
                        break;
                    case "3"://心跳
                        getHeartRateData(endMillisecond, startMillisecond);
                        break;
                    case "4"://血糖
                        getBloodsugarData(endMillisecond, startMillisecond);
                        break;
                    case "5"://血氧
                        getBloodoxygenData(endMillisecond, startMillisecond);
                        break;
                    case "6"://脉搏
                        break;
                    case "7"://胆固醇
                        getCholesterolData(endMillisecond, startMillisecond);
                        break;
                    case "8"://血尿酸
                        getBUAData(endMillisecond, startMillisecond);
                        break;
                    case "9"://心电图
                        getEcgData(endMillisecond, startMillisecond);
                        break;
                    case "10"://体重
                        getWeightData(endMillisecond, startMillisecond);
                        break;
                }
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
        mRgHealthRecord.setOnCheckedChangeListener(this);
        mTvTimeUnit = (TextView) findViewById(R.id.tv_time_unit);
        mTvTimeUnit.setOnClickListener(this);
        mTvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        mTvTimeStart.setOnClickListener(this);
        mTvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        mTvTimeEnd.setOnClickListener(this);
        mLlSelectTime = (LinearLayout) findViewById(R.id.ll_select_time);
        mHealthRecordFl = (FrameLayout) findViewById(R.id.health_record_fl);
        mDialoHealthRecordUnitView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.dialo_health_record_unit, null);
        mUnitDayDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView
                .findViewById(R.id.unit_day);
        mUnitDayDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitWeekDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView
                .findViewById(R.id.unit_week);
        mUnitWeekDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitMonthDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView
                .findViewById(R.id.unit_month);
        mUnitMonthDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitHalfYearDialoHealthRecordUnitView = (TextView) mDialoHealthRecordUnitView
                .findViewById(R.id.unit_half_year);
        mUnitHalfYearDialoHealthRecordUnitView.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        selectEndYear = calendar.get(Calendar.YEAR);
        selectEndMonth = calendar.get(Calendar.MONTH) + 1;
        selectEndDay = calendar.get(Calendar.DATE);
        startMillisecond = TimeUtils.string2Milliseconds(selectEndYear + "-" + selectEndMonth + "-" +
                selectEndDay, new SimpleDateFormat("yyyy-MM-dd")) + "";

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date weekAgoDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(weekAgoDate);
        String[] date = result.split("-");
        selectStartYear = Integer.parseInt(date[0]);
        selectStartMonth = Integer.parseInt(date[1]);
        selectStartDay = Integer.parseInt(date[2]);
        endMillisecond = TimeUtils.string2Milliseconds(selectStartYear + "-" + selectStartMonth + "-" +
                selectStartDay, new SimpleDateFormat("yyyy-MM-dd")) + "";

        mTvTimeStart.setText(selectStartYear + "年" + selectStartMonth + "月" + selectStartDay + "日");
        mTvTimeEnd.setText(selectEndYear + "年" + selectEndMonth + "月" + selectEndDay + "日");

        radioGroupPosition = getIntent().getIntExtra("position", 0);
        initFragments();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //默认选择第一个
        switch (radioGroupPosition) {
            case 0://体温
                temp = "1";
                mRgHealthRecord.check(R.id.rb_record_temperature);
                fragmentTransaction.replace(R.id.health_record_fl, temperatureFragment).commit();
                getTemperatureData(endMillisecond, startMillisecond);
                break;
            case 1://血压
                temp = "2";
                mRgHealthRecord.check(R.id.rb_record_blood_pressure);
                fragmentTransaction.replace(R.id.health_record_fl, bloodpressureFragment).commit();
                getBloodpressureData(endMillisecond, startMillisecond);
                break;
            case 2://血糖
                temp = "4";
                mRgHealthRecord.check(R.id.rb_record_blood_glucose);
                fragmentTransaction.replace(R.id.health_record_fl, bloodsugarFragment).commit();
                getBloodsugarData(endMillisecond, startMillisecond);
                break;
            case 3://血氧
                temp = "5";
                mRgHealthRecord.check(R.id.rb_record_blood_oxygen);
                fragmentTransaction.replace(R.id.health_record_fl, bloodoxygenFragment).commit();
                getBloodoxygenData(endMillisecond, startMillisecond);
                break;
            case 4://心跳
                temp = "3";
                mRgHealthRecord.check(R.id.rb_record_heart_rate);
                fragmentTransaction.replace(R.id.health_record_fl, heartrateFragment).commit();
                break;
            case 5://胆固醇
                temp = "7";
                mRgHealthRecord.check(R.id.rb_record_cholesterol);
                fragmentTransaction.replace(R.id.health_record_fl, cholesterolFragment).commit();
                getHeartRateData(endMillisecond, startMillisecond);
                break;
            case 6://血尿酸
                temp = "8";
                mRgHealthRecord.check(R.id.rb_record_bua);
                fragmentTransaction.replace(R.id.health_record_fl, buaFragment).commit();
                getBUAData(endMillisecond, startMillisecond);
                break;
            case 7://心电图
                temp = "9";
                mRgHealthRecord.check(R.id.rb_record_ecg);
                fragmentTransaction.replace(R.id.health_record_fl, ecgFragment).commit();
                getEcgData(endMillisecond, startMillisecond);
                break;
            case 8://体重
                temp = "10";
                mRgHealthRecord.check(R.id.rb_record_weight);
                fragmentTransaction.replace(R.id.health_record_fl, weightFragment).commit();
                getWeightData(endMillisecond, startMillisecond);
                break;
        }

    }

    private void getTemperatureData(String start, String end) {
        NetworkApi.getTemperatureHistory(start, end, temp,
                response -> temperatureFragment.refreshData(response, temp),
                message -> temperatureFragment.refreshErrorData(message));
    }

    private void getBloodoxygenData(String start, String end) {
        NetworkApi.getBloodOxygenHistory(start, end, temp,
                response -> bloodoxygenFragment.refreshData(response, temp),
                message -> bloodoxygenFragment.refreshErrorData(message));
    }

    private void getBloodpressureData(String start, String end) {
        NetworkApi.getBloodpressureHistory(start, end, temp,
                response -> bloodpressureFragment.refreshData(response, temp),
                message -> bloodpressureFragment.refreshErrorData(message));
    }

    private void getBloodsugarData(String start, String end) {
        NetworkApi.getBloodSugarHistory(start, end, temp,
                response -> bloodsugarFragment.refreshData(response, temp),
                message -> bloodsugarFragment.refreshErrorData(message));
    }

    private void getBUAData(String start, String end) {
        NetworkApi.getBUAHistory(start, end, temp,
                response -> buaFragment.refreshData(response, temp),
                message -> buaFragment.refreshErrorData(message));

    }

    private void getCholesterolData(String start, String end) {
        NetworkApi.getCholesterolHistory(start, end, temp,
                response -> cholesterolFragment.refreshData(response, temp),
                message -> cholesterolFragment.refreshErrorData(message));
    }

    private void getHeartRateData(String start, String end) {
        NetworkApi.getHeartRateHistory(start, end, temp,
                response -> heartrateFragment.refreshData(response, temp),
                message -> heartrateFragment.refreshErrorData(message));
    }

    private void getWeightData(String start, String end) {
        NetworkApi.getWeight(start, end, temp,
                response -> weightFragment.refreshData(response, temp),
                message -> weightFragment.refreshErrorData(message));
    }

    private void getEcgData(String start, String end) {
        NetworkApi.getECGHistory(start, end, temp,
                response -> ecgFragment.refreshData(response, temp),
                message -> ecgFragment.refreshErrorData(message));
    }

    private void initFragments() {
        temperatureFragment = new HealthRecordTemperatureFragment();
        bloodpressureFragment = new HealthRecordBloodpressureFragment();
        bloodsugarFragment = new HealthRecordBloodsugarFragment();
        bloodsugarFragment.setRequestBloodsugarData(this);
        bloodoxygenFragment = new HealthRecordBloodoxygenFragment();
        heartrateFragment = new HealthRecordHeartrateFragment();
        cholesterolFragment = new HealthRecordCholesterolFragment();
        buaFragment = new HealthRecordBUAFragment();
        ecgFragment = new HealthRecordECGFragment();
        weightFragment = new HealthRecordWeightFragment();
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
                initDatePicker(false);
                break;
            case R.id.tv_time_end:
                initDatePicker(true);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (checkedId) {
            case R.id.rb_record_temperature://体温
                temp = "1";
                if (temperatureFragment != null)
                    fragmentTransaction.replace(R.id.health_record_fl, temperatureFragment).commit();
                getTemperatureData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_blood_pressure://血压
                temp = "2";
                if (bloodpressureFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, bloodpressureFragment).commit();
                }
                getBloodpressureData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_blood_glucose://血糖
                temp = "4";
                if (bloodsugarFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, bloodsugarFragment).commit();
                }
                getBloodsugarData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_blood_oxygen://血氧
                temp = "5";
                if (bloodoxygenFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, bloodoxygenFragment).commit();
                }
                getBloodoxygenData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_heart_rate://心跳
                temp = "3";
                if (heartrateFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, heartrateFragment).commit();
                }
                getHeartRateData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_pulse://脉搏

                break;
            case R.id.rb_record_cholesterol://胆固醇
                temp = "7";
                if (cholesterolFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, cholesterolFragment).commit();
                }
                getCholesterolData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_bua://血尿酸
                temp = "8";
                if (buaFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, buaFragment).commit();
                }
                getBUAData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_ecg://心电
                temp = "9";
                if (ecgFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, ecgFragment).commit();
                }
                getEcgData(endMillisecond, startMillisecond);
                break;
            case R.id.rb_record_weight://体重
                temp = "10";
                if (weightFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, weightFragment).commit();
                }
                getWeightData(endMillisecond, startMillisecond);
                break;
        }
    }

    @Override
    public void requestData() {
        getBloodsugarData(startMillisecond, endMillisecond);
    }
}
