package com.gcml.module_health_record;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcml.module_health_record.bean.BUA;
import com.gcml.module_health_record.bean.BloodOxygenHistory;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.bean.BloodSugarHistory;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.bean.HeartRateHistory;
import com.gcml.module_health_record.bean.TemperatureHistory;
import com.gcml.module_health_record.bean.WeightHistory;
import com.gcml.module_health_record.dialog.BaseDialog;
import com.gcml.module_health_record.dialog.DialogClickSureListener;
import com.gcml.module_health_record.dialog.DialogImage;
import com.gcml.module_health_record.dialog.DialogWheelYearMonthDay;
import com.gcml.module_health_record.fragments.HealthRecordBUAFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodoxygenFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodpressureFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodsugarFragment;
import com.gcml.module_health_record.fragments.HealthRecordCholesterolFragment;
import com.gcml.module_health_record.fragments.HealthRecordECGFragment;
import com.gcml.module_health_record.fragments.HealthRecordHeartrateFragment;
import com.gcml.module_health_record.fragments.HealthRecordTemperatureFragment;
import com.gcml.module_health_record.fragments.HealthRecordWeightFragment;
import com.gcml.module_health_record.network.HealthRecordServer;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.QRCodeUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.TimeUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HealthRecordActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, HealthRecordBloodsugarFragment.BloodsugarSelectTime {
    private TextView mTvRecordQrcode;
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
    private int selectEndHour;
    private int selectEndMinnute;
    private int selectEndSecond;
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
    private LinearLayout mLlBack;
    private TextView mTvTopTitle;
    private ImageView mIvTopRight;
    private UserInfoBean user;

    public static void startActivity(Context context, int position) {
        Intent intent = new Intent(context, HealthRecordActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("position", position);
        context.startActivity(intent);
    }


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_recoed_activity_health_record;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceSynthetize.startSynthesize("主人，请查看您的健康数据");
        user = Box.getSessionManager().getUser();
        initDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
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
                        selectEndDay + " 23:59:59", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) + "";
                switch (temp) {
                    case "1":
                        //体温
                        getTemperatureData(startMillisecond, endMillisecond);
                        break;
                    case "2":
                        //血压
                        getBloodpressureData(startMillisecond, endMillisecond);
                        break;
                    case "3":
                        //心跳
                        getHeartRateData(startMillisecond, endMillisecond);
                        break;
                    case "4":
                        //血糖
                        getBloodsugarData(startMillisecond, endMillisecond);
                        break;
                    case "5":
                        //血氧
                        getBloodoxygenData(startMillisecond, endMillisecond);
                        break;
                    case "6":
                        //脉搏
                        break;
                    case "7":
                        //胆固醇
                        getCholesterolData(startMillisecond, endMillisecond);
                        break;
                    case "8":
                        //血尿酸
                        getBUAData(startMillisecond, endMillisecond);
                        break;
                    case "9":
                        //心电图
                        getEcgData(startMillisecond, endMillisecond);
                        break;
                    case "10":
                        //体重
                        getWeightData(startMillisecond, endMillisecond);
                        break;
                    default:
                        break;
                }
            }
        });
        dialogWheelYearMonthDay.show();
    }

    private void initDialog() {
        mDialoHealthRecordUnitView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.dialog_health_record_unit, null);
        mUnitDayDialoHealthRecordUnitView = mDialoHealthRecordUnitView
                .findViewById(R.id.unit_day);
        mUnitDayDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitWeekDialoHealthRecordUnitView = mDialoHealthRecordUnitView
                .findViewById(R.id.unit_week);
        mUnitWeekDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitMonthDialoHealthRecordUnitView = mDialoHealthRecordUnitView
                .findViewById(R.id.unit_month);
        mUnitMonthDialoHealthRecordUnitView.setOnClickListener(this);
        mUnitHalfYearDialoHealthRecordUnitView = mDialoHealthRecordUnitView
                .findViewById(R.id.unit_half_year);
        mUnitHalfYearDialoHealthRecordUnitView.setOnClickListener(this);

        baseDialog = new BaseDialog(this);
        baseDialog.setContentView(mDialoHealthRecordUnitView);
        baseDialog.setCanceledOnTouchOutside(true);

    }

    @Override
    public void initView() {
        mTvRecordQrcode = findViewById(R.id.tv_record_qrcode);
        mTvRecordQrcode.setOnClickListener(this);
        mRgHealthRecord = findViewById(R.id.rg_health_record);
        mRgHealthRecord.setOnCheckedChangeListener(this);
        mTvTimeUnit = findViewById(R.id.tv_time_unit);
        mTvTimeUnit.setOnClickListener(this);
        mTvTimeStart = findViewById(R.id.tv_time_start);
        mTvTimeStart.setOnClickListener(this);
        mTvTimeEnd = findViewById(R.id.tv_time_end);
        mTvTimeEnd.setOnClickListener(this);
        mLlSelectTime = findViewById(R.id.ll_select_time);
        mHealthRecordFl = findViewById(R.id.health_record_fl);
        mLlBack = findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(this);
        mTvTopTitle = findViewById(R.id.tv_top_title);
        mTvTopTitle.setText("健 康 数 据");
        mIvTopRight = findViewById(R.id.iv_top_right);
        mIvTopRight.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        selectEndYear = calendar.get(Calendar.YEAR);
        selectEndMonth = calendar.get(Calendar.MONTH) + 1;
        selectEndDay = calendar.get(Calendar.DATE);
        selectEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectEndMinnute = calendar.get(Calendar.MINUTE);
        selectEndSecond = calendar.get(Calendar.SECOND);
        endMillisecond = TimeUtils.string2Milliseconds(selectEndYear + "-" + selectEndMonth + "-" +
                        selectEndDay + "-" + selectEndHour + "-" + selectEndMinnute + "-" + selectEndSecond,
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")) + "";

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date weekAgoDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(weekAgoDate);
        String[] date = result.split("-");
        selectStartYear = Integer.parseInt(date[0]);
        selectStartMonth = Integer.parseInt(date[1]);
        selectStartDay = Integer.parseInt(date[2]);
        startMillisecond = TimeUtils.string2Milliseconds(selectStartYear + "-" + selectStartMonth + "-" +
                selectStartDay, new SimpleDateFormat("yyyy-MM-dd")) + "";

        mTvTimeStart.setText(selectStartYear + "年" + selectStartMonth + "月" + selectStartDay + "日");
        mTvTimeEnd.setText(selectEndYear + "年" + selectEndMonth + "月" + selectEndDay + "日");

        radioGroupPosition = getIntent().getIntExtra("position", 0);
        initFragments();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //默认选择第一个
        switch (radioGroupPosition) {
            case 0:
                //体温
                temp = "1";
                mRgHealthRecord.check(R.id.rb_record_temperature);
                fragmentTransaction.replace(R.id.health_record_fl, temperatureFragment).commit();
//                getTemperatureData(endMillisecond, startMillisecond);
                break;
            case 1:
                //血压
                temp = "2";
                mRgHealthRecord.check(R.id.rb_record_blood_pressure);
                fragmentTransaction.replace(R.id.health_record_fl, bloodpressureFragment).commit();
//                getBloodpressureData(endMillisecond, startMillisecond);
                break;
            case 2:
                //血糖
                temp = "4";
                mRgHealthRecord.check(R.id.rb_record_blood_glucose);
                fragmentTransaction.replace(R.id.health_record_fl, bloodsugarFragment).commit();
//                getBloodsugarData(endMillisecond, startMillisecond);
                break;
            case 3:
                //血氧
                temp = "5";
                mRgHealthRecord.check(R.id.rb_record_blood_oxygen);
                fragmentTransaction.replace(R.id.health_record_fl, bloodoxygenFragment).commit();
//                getBloodoxygenData(endMillisecond, startMillisecond);
                break;
            case 4:
                //心跳
                temp = "3";
                mRgHealthRecord.check(R.id.rb_record_heart_rate);
                fragmentTransaction.replace(R.id.health_record_fl, heartrateFragment).commit();
                break;
            case 5:
                //胆固醇
                temp = "7";
                mRgHealthRecord.check(R.id.rb_record_cholesterol);
                fragmentTransaction.replace(R.id.health_record_fl, cholesterolFragment).commit();
//                getHeartRateData(endMillisecond, startMillisecond);
                break;
            case 6:
                //血尿酸
                temp = "8";
                mRgHealthRecord.check(R.id.rb_record_bua);
                fragmentTransaction.replace(R.id.health_record_fl, buaFragment).commit();
//                getBUAData(endMillisecond, startMillisecond);
                break;
            case 7:
                //心电图
                temp = "9";
                mRgHealthRecord.check(R.id.rb_record_ecg);
                fragmentTransaction.replace(R.id.health_record_fl, ecgFragment).commit();
//                getEcgData(endMillisecond, startMillisecond);
                break;
            case 8:
                //体重
                temp = "10";
                mRgHealthRecord.check(R.id.rb_record_weight);
                fragmentTransaction.replace(R.id.health_record_fl, weightFragment).commit();
//                getWeightData(endMillisecond, startMillisecond);
                break;
            default:
                break;
        }

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @SuppressLint("CheckResult")
    private void getTemperatureData(String start, String end) {
//        HealthRecordNetworkApi.getTemperatureHistory(start, end, temp,
//                response -> temperatureFragment.refreshData(response, temp),
//                message -> temperatureFragment.refreshErrorData(message));

        Box.getRetrofit(HealthRecordServer.class)
                .getTemperatureHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<TemperatureHistory>>() {
                    @Override
                    public void onNext(List<TemperatureHistory> temperatureHistories) {
                        temperatureFragment.refreshData(temperatureHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        temperatureFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBloodoxygenData(String start, String end) {

        Box.getRetrofit(HealthRecordServer.class)
                .getBloodOxygenHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<BloodOxygenHistory>>() {
                    @Override
                    public void onNext(List<BloodOxygenHistory> bloodOxygenHistories) {
                        bloodoxygenFragment.refreshData(bloodOxygenHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        bloodoxygenFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBloodpressureData(String start, String end) {

        Box.getRetrofit(HealthRecordServer.class)
                .getBloodpressureHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<BloodPressureHistory>>() {
                    @Override
                    public void onNext(List<BloodPressureHistory> bloodPressureHistories) {
                        bloodpressureFragment.refreshData(bloodPressureHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        bloodpressureFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBloodsugarData(String start, String end) {
        Box.getRetrofit(HealthRecordServer.class)
                .getBloodSugarHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<BloodSugarHistory>>() {
                    @Override
                    public void onNext(List<BloodSugarHistory> bloodSugarHistories) {
                        bloodsugarFragment.refreshData(bloodSugarHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        bloodsugarFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBUAData(String start, String end) {

        Box.getRetrofit(HealthRecordServer.class)
                .getBUAHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<BUA>>() {
                    @Override
                    public void onNext(List<BUA> buas) {
                        buaFragment.refreshData(buas, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        buaFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getCholesterolData(String start, String end) {

        Box.getRetrofit(HealthRecordServer.class)
                .getCholesterolHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<CholesterolHistory>>() {
                    @Override
                    public void onNext(List<CholesterolHistory> cholesterolHistories) {
                        cholesterolFragment.refreshData(cholesterolHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        cholesterolFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getHeartRateData(String start, String end) {

        Box.getRetrofit(HealthRecordServer.class)
                .getHeartRateHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<HeartRateHistory>>() {
                    @Override
                    public void onNext(List<HeartRateHistory> heartRateHistories) {
                        heartrateFragment.refreshData(heartRateHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        heartrateFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getWeightData(String start, String end) {
        Box.getRetrofit(HealthRecordServer.class)
                .getWeight(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<WeightHistory>>() {
                    @Override
                    public void onNext(List<WeightHistory> weightHistories) {
                        weightFragment.refreshData(weightHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        weightFragment.refreshErrorData("暂无该项数据");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getEcgData(String start, String end) {
        Box.getRetrofit(HealthRecordServer.class)
                .getECGHistory(user.bid, start, end, temp)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<ECGHistory>>() {
                    @Override
                    public void onNext(List<ECGHistory> ecgHistories) {
                        ecgFragment.refreshData(ecgHistories, temp);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ecgFragment.refreshErrorData("暂无该项数据");
                    }
                });
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
        int i = v.getId();
        if (i == R.id.tv_time_unit) {
//            if (!baseDialog.isShowing())
//                baseDialog.show();

        } else if (i == R.id.tv_time_start) {
            initDatePicker(false);

        } else if (i == R.id.tv_time_end) {
            initDatePicker(true);

        } else if (i == R.id.unit_day) {
            baseDialog.dismiss();
            mTvTimeUnit.setText("天");

        } else if (i == R.id.unit_week) {
            baseDialog.dismiss();
            mTvTimeUnit.setText("周");

        } else if (i == R.id.unit_month) {
            baseDialog.dismiss();
            mTvTimeUnit.setText("月");

        } else if (i == R.id.unit_half_year) {
            baseDialog.dismiss();
            mTvTimeUnit.setText("半年");

        } else if (i == R.id.ll_back) {
            finish();
        } else if (i == R.id.iv_top_right) {
            emitEvent("skip2MainActivity");
        } else if (i == R.id.tv_record_qrcode) {

            String text = Box.getBaseUrl() + "ZZB/br/whole_informations?bid=" + user.bid + "&bname=" + user.bname;
            DialogImage dialogImage = new DialogImage(this);
            dialogImage.setImage(QRCodeUtils.creatQRCode(text, 600, 600));
            dialogImage.setDescription("扫一扫，下载详细报告");
            dialogImage.show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (checkedId == R.id.rb_record_temperature) {
            temp = "1";
            if (temperatureFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, temperatureFragment).commit();
            }
            getTemperatureData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_blood_pressure) {
            temp = "2";
            if (bloodpressureFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, bloodpressureFragment).commit();
            }
            getBloodpressureData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_blood_glucose) {
            temp = "4";
            if (bloodsugarFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, bloodsugarFragment).commit();
            }
            getBloodsugarData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_blood_oxygen) {
            temp = "5";
            if (bloodoxygenFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, bloodoxygenFragment).commit();
            }
            getBloodoxygenData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_heart_rate) {
            temp = "3";
            if (heartrateFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, heartrateFragment).commit();
            }
            getHeartRateData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_pulse) {
        } else if (checkedId == R.id.rb_record_cholesterol) {
            temp = "7";
            if (cholesterolFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, cholesterolFragment).commit();
            }
            getCholesterolData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_bua) {
            temp = "8";
            if (buaFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, buaFragment).commit();
            }
            getBUAData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_ecg) {
            temp = "9";
            if (ecgFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, ecgFragment).commit();
            }
            getEcgData(startMillisecond, endMillisecond);

        } else if (checkedId == R.id.rb_record_weight) {
            temp = "10";
            if (weightFragment != null) {
                fragmentTransaction.replace(R.id.health_record_fl, weightFragment).commit();
            }
            getWeightData(startMillisecond, endMillisecond);

        }
    }

    @Override
    public void requestData() {
        getBloodsugarData(startMillisecond, endMillisecond);
    }
}
