package com.gcml.module_health_record;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.common.utils.qrcode.QRCodeUtils;
import com.gcml.common.widget.base.dialog.DialogImage;
import com.gcml.module_health_record.bean.BUA;
import com.gcml.module_health_record.bean.BloodOxygenHistory;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.bean.BloodSugarHistory;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.bean.HeartRateHistory;
import com.gcml.module_health_record.bean.TemperatureHistory;
import com.gcml.module_health_record.bean.WeightHistory;
import com.gcml.module_health_record.cc.CCAppActions;
import com.gcml.module_health_record.fragments.HealthRecordBUAFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodoxygenFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodpressureFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodsugarFragment;
import com.gcml.module_health_record.fragments.HealthRecordCholesterolFragment;
import com.gcml.module_health_record.fragments.HealthRecordECGFragment;
import com.gcml.module_health_record.fragments.HealthRecordHeartrateFragment;
import com.gcml.module_health_record.fragments.HealthRecordTemperatureFragment;
import com.gcml.module_health_record.fragments.HealthRecordWeightFragment;
import com.gcml.module_health_record.network.HealthRecordRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/5 17:48
 * created by: gzq
 * description: TODO
 */
public class HealthRecordActivity2 extends AppCompatActivity implements View.OnClickListener, HealthRecordBloodsugarFragment.BloodsugarSelectTime {
    private LinearLayout mLlBack;
    private TextView mTvTopTitle;
    private ImageView mIvTopRight;
    private RecyclerView mRvMenu;
    private TextView mTvTimeUnit;
    private TextView mTvTimeStart;
    private TextView mTvTimeEnd;
    private LinearLayout mLlSelectTime;
    private FrameLayout mHealthRecordFl;
    private BaseQuickAdapter<MenuBean, BaseViewHolder> adapter;
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
    private View mDialoHealthRecordUnitView;
    private TextView mUnitDayDialoHealthRecordUnitView;
    private TextView mUnitWeekDialoHealthRecordUnitView;
    private TextView mUnitMonthDialoHealthRecordUnitView;
    private TextView mUnitHalfYearDialoHealthRecordUnitView;
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
    private String startMillisecond;
    private String endMillisecond;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_recoed_activity_health_record2);
        initFragments();
        initView();
        initMenu();
        initStart();
    }

    private void initStart() {
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
                fragmentTransaction.replace(R.id.health_record_fl, temperatureFragment).commit();
                getTemperatureData(endMillisecond, startMillisecond);
                break;
            case 1:
                //血压
                temp = "2";
                fragmentTransaction.replace(R.id.health_record_fl, bloodpressureFragment).commit();
                getBloodpressureData(endMillisecond, startMillisecond);
                break;
            case 2:
                //血糖
                temp = "4";
                fragmentTransaction.replace(R.id.health_record_fl, bloodsugarFragment).commit();
                getBloodsugarData(endMillisecond, startMillisecond);
                break;
            case 3:
                //血氧
                temp = "5";
                fragmentTransaction.replace(R.id.health_record_fl, bloodoxygenFragment).commit();
                getBloodoxygenData(endMillisecond, startMillisecond);
                break;
            case 4:
                //心跳
                temp = "3";
                fragmentTransaction.replace(R.id.health_record_fl, heartrateFragment).commit();
                getHeartRateData(endMillisecond,startMillisecond);
                break;
            case 5:
                //胆固醇
                temp = "7";
                fragmentTransaction.replace(R.id.health_record_fl, cholesterolFragment).commit();
                getCholesterolData(endMillisecond, startMillisecond);
                break;
            case 6:
                //血尿酸
                temp = "8";
                fragmentTransaction.replace(R.id.health_record_fl, buaFragment).commit();
                getBUAData(endMillisecond, startMillisecond);
                break;
            case 7:
                //心电图
                temp = "9";
                fragmentTransaction.replace(R.id.health_record_fl, ecgFragment).commit();
                getEcgData(endMillisecond, startMillisecond);
                break;
            case 8:
                //体重
                temp = "10";
                fragmentTransaction.replace(R.id.health_record_fl, weightFragment).commit();
                getWeightData(endMillisecond, startMillisecond);
                break;
            default:
                break;
        }
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
    private void initMenu() {
        List<MenuBean> menuBeans = new ArrayList<>();
        menuBeans.add(new MenuBean(0, "档案下载", false));
        menuBeans.add(new MenuBean(1, "体温", false));
        menuBeans.add(new MenuBean(2, "血压", false));
        menuBeans.add(new MenuBean(4, "血糖", false));
        menuBeans.add(new MenuBean(5, "血氧", false));
        menuBeans.add(new MenuBean(3, "心跳", false));
        menuBeans.add(new MenuBean(7, "胆固醇", false));
        menuBeans.add(new MenuBean(8, "血尿酸", false));
        menuBeans.add(new MenuBean(9, "心电图", false));
        menuBeans.add(new MenuBean(10, "体重", false));


        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvMenu.setAdapter(adapter = new BaseQuickAdapter<MenuBean, BaseViewHolder>(R.layout.health_record_item_menu, menuBeans) {
            @Override
            protected void convert(BaseViewHolder helper, MenuBean item) {
                helper.setText(R.id.tv_menu, item.getName());
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((TextView) view).setSelected(menuBeans.get(position).getSelected());
                dealClickItem(menuBeans.get(position).getId());
            }
        });
    }

    private void dealClickItem(int id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id){
            case 0:
                showQRDialog();
                break;
            case 1:
                temp = "1";
                temp = "1";
                if (temperatureFragment != null) {
                    fragmentTransaction.replace(R.id.health_record_fl, temperatureFragment).commit();
                }
                getTemperatureData(startMillisecond, endMillisecond);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
                default:
                    break;
        }
    }

    private void showQRDialog() {
        String text = HealthRecordNetworkApi.BasicUrl + "/ZZB/br/whole_informations?bid=" + UserSpHelper.getUserId() + "&bname=" + UserSpHelper.getUserName();
        DialogImage dialogImage = new DialogImage(this);
        dialogImage.setImage(QRCodeUtils.creatQRCode(text, 600, 600));
        dialogImage.setDescription("扫一扫，下载详细报告");
        dialogImage.show();
    }

    private void initView() {
        mLlBack = (LinearLayout) findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(this);
        mTvTopTitle = (TextView) findViewById(R.id.tv_top_title);
        mIvTopRight = (ImageView) findViewById(R.id.iv_top_right);
        mIvTopRight.setOnClickListener(this);
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        mTvTimeUnit = (TextView) findViewById(R.id.tv_time_unit);
        mTvTimeUnit.setOnClickListener(this);
        mTvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        mTvTimeStart.setOnClickListener(this);
        mTvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        mTvTimeEnd.setOnClickListener(this);
        mLlSelectTime = (LinearLayout) findViewById(R.id.ll_select_time);
        mHealthRecordFl = (FrameLayout) findViewById(R.id.health_record_fl);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_time_unit) {

        } else if (i == R.id.tv_time_start) {

        } else if (i == R.id.tv_time_end) {

        } else if (i == R.id.ll_back) {
            finish();
        } else if (i == R.id.iv_top_right) {
            CCAppActions.jump2MainActivity();
        } else {
        }
    }

    @Override
    public void requestData() {

    }

    @SuppressLint("CheckResult")
    private void getTemperatureData(String start, String end) {
        HealthRecordRepository
                .getTemperatureHistory(start, end, temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<TemperatureHistory>>() {
                    @Override
                    public void onNext(List<TemperatureHistory> temperatureHistories) {
                        temperatureFragment.refreshData(temperatureHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        temperatureFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBloodoxygenData(String start, String end) {

        HealthRecordRepository.getBloodOxygenHistory(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<BloodOxygenHistory>>() {
                    @Override
                    public void onNext(List<BloodOxygenHistory> bloodOxygenHistories) {
                        bloodoxygenFragment.refreshData(bloodOxygenHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        bloodoxygenFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBloodpressureData(String start, String end) {

        HealthRecordRepository.getBloodpressureHistory(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<BloodPressureHistory>>() {
                    @Override
                    public void onNext(List<BloodPressureHistory> bloodPressureHistories) {
                        bloodpressureFragment.refreshData(bloodPressureHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        bloodpressureFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBloodsugarData(String start, String end) {

        HealthRecordRepository.getBloodSugarHistory(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<BloodSugarHistory>>() {
                    @Override
                    public void onNext(List<BloodSugarHistory> bloodSugarHistories) {
                        bloodsugarFragment.refreshData(bloodSugarHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        bloodsugarFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBUAData(String start, String end) {

        HealthRecordRepository.getBUAHistory(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<BUA>>() {
                    @Override
                    public void onNext(List<BUA> buas) {
                        buaFragment.refreshData(buas, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        buaFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @SuppressLint("CheckResult")
    private void getCholesterolData(String start, String end) {

        HealthRecordRepository.getCholesterolHistory(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<CholesterolHistory>>() {
                    @Override
                    public void onNext(List<CholesterolHistory> cholesterolHistories) {
                        cholesterolFragment.refreshData(cholesterolHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        cholesterolFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getHeartRateData(String start, String end) {

        HealthRecordRepository.getHeartRateHistory(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<HeartRateHistory>>() {
                    @Override
                    public void onNext(List<HeartRateHistory> heartRateHistories) {
                        heartrateFragment.refreshData(heartRateHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        heartrateFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getWeightData(String start, String end) {

        HealthRecordRepository.getWeight(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<WeightHistory>>() {
                    @Override
                    public void onNext(List<WeightHistory> weightHistories) {
                        weightFragment.refreshData(weightHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        weightFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getEcgData(String start, String end) {

        HealthRecordRepository.getECGHistory(start,end,temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<ECGHistory>>() {
                    @Override
                    public void onNext(List<ECGHistory> ecgHistories) {
                        ecgFragment.refreshData(ecgHistories, temp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ecgFragment.refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
