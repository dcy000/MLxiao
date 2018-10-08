package com.example.han.referralproject.health;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health.model.DetectResult;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.yiyuan.bean.ExaminationReportBean;
import com.example.han.referralproject.yiyuan.bean.PersonInfoResultBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.UiUtils;
import com.ml.zxing.QrCodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetectResultActivity extends BaseActivity {

    @BindView(R.id.detect_tv_name_info)
    TextView mDetectTvNameInfo;
    @BindView(R.id.detect_tv_height_info)
    TextView mDetectTvHeightInfo;
    @BindView(R.id.detect_tv_weight_info)
    TextView mDetectTvWeightInfo;
    @BindView(R.id.detect_tv_gender_info)
    TextView mDetectTvGenderInfo;
    @BindView(R.id.detect_tv_age_info)
    TextView mDetectTvAgeInfo;
    @BindView(R.id.detect_tv_blood_type_info)
    TextView mDetectTvBloodTypeInfo;
    @BindView(R.id.detect_tv_result)
    TextView mDetectTvResult;
    @BindView(R.id.detect_tv_result_weight_info)
    TextView mDetectTvResultWeightInfo;
    @BindView(R.id.detect_tv_result_oxygen_info)
    TextView mDetectTvResultOxygenInfo;
    @BindView(R.id.detect_tv_result_oxygen_info_indicator)
    TextView mDetectTvResultOxygenInfoIndicator;
    @BindView(R.id.detect_tv_result_high_pressure_info)
    TextView mDetectTvResultHighPressureInfo;
    @BindView(R.id.detect_tv_result_high_pressure_info_indicator)
    TextView mDetectTvResultHighPressureInfoIndicator;
    @BindView(R.id.detect_tv_result_low_pressure_info)
    TextView mDetectTvResultLowPressureInfo;
    @BindView(R.id.detect_tv_result_temperate_info)
    TextView mDetectTvResultTemperateInfo;
    @BindView(R.id.detect_tv_result_sugar_info)
    TextView mDetectTvResultSugarInfo;
    @BindView(R.id.detect_tv_result_ecg_info)
    TextView mDetectTvResultEcgInfo;
    @BindView(R.id.detect_tv_result_health_zz_info)
    TextView detectTvResultHealthZzInfo;
    @BindView(R.id.ll_health_zz)
    LinearLayout llHealthZz;
    @BindView(R.id.detect_tv_result_pressure_zz_info)
    TextView detectTvResultPressureZzInfo;
    @BindView(R.id.ll_pressure_smoke)
    LinearLayout llPressureSmoke;
    @BindView(R.id.detect_tv_result_health_run_time_info)
    TextView detectTvResultHealthRunTimeInfo;
    @BindView(R.id.ll_health_run_time)
    LinearLayout llHealthRunTime;
    @BindView(R.id.detect_tv_result_health_salt_info)
    TextView detectTvResultHealthSaltInfo;
    @BindView(R.id.ll_health_salt)
    LinearLayout llHealthSalt;
    @BindView(R.id.detect_tv_result_health_drink_info)
    TextView detectTvResultHealthDrinkInfo;
    @BindView(R.id.ll_health_drink)
    LinearLayout llHealthDrink;
    @BindView(R.id.ll_pressure_pulse)
    LinearLayout llPressureulse;
    @BindView(R.id.detect_tv_result_health_smoke_info)
    TextView detectTvResultHealthSmokeInfo;
    @BindView(R.id.ll_health_smoke)
    LinearLayout llHealthSmoke;
    @BindView(R.id.tv_pressure_pulse)
    TextView tvPulse;
    @BindView(R.id.detect_tv_result_sugar_zz_info)
    TextView detectTvResultSugarZzInfo;
    @BindView(R.id.ll_sugar_zz)
    LinearLayout llSugarZz;
    @BindView(R.id.detect_tv_person_info)
    TextView detectTvPersonInfo;
    @BindView(R.id.detect_tv_name)
    TextView detectTvName;
    @BindView(R.id.detect_tv_age)
    TextView detectTvAge;
    @BindView(R.id.detect_tv_gender)
    TextView detectTvGender;
    @BindView(R.id.detect_tv_blood_type)
    TextView detectTvBloodType;
    @BindView(R.id.detect_tv_height)
    TextView detectTvHeight;
    @BindView(R.id.detect_tv_weight)
    TextView detectTvWeight;
    @BindView(R.id.detect_tv_result_weight)
    TextView detectTvResultWeight;
    @BindView(R.id.detect_tv_result_oxygen)
    TextView detectTvResultOxygen;
    @BindView(R.id.detect_tv_result_high_pressure)
    TextView detectTvResultHighPressure;
    @BindView(R.id.detect_tv_result_low_pressure)
    TextView detectTvResultLowPressure;
    @BindView(R.id.detect_tv_result_temperate)
    LinearLayout detectTvResultTemperate;
    @BindView(R.id.detect_tv_result_sugar)
    LinearLayout detectTvResultSugar;
    @BindView(R.id.detect_tv_result_ecg)
    LinearLayout detectTvResultEcg;
    @BindView(R.id.tv_ua_info)
    TextView tvUaInfo;
    @BindView(R.id.ll_ua)
    LinearLayout llUa;
    @BindView(R.id.tv_cholesterin_info)
    TextView tvCholesterinInfo;
    @BindView(R.id.ll_cholesterin)
    LinearLayout llCholesterin;
    @BindView(R.id.tv_heart_rate_info)
    TextView tvHeartRateInfo;
    @BindView(R.id.ll_heart_rate)
    LinearLayout llHeartRate;
    @BindView(R.id.tv_xinli_tiaozheng_info)
    TextView tvXinliTiaozhengInfo;
    @BindView(R.id.ll_xinli_tiaozheng)
    LinearLayout llXinliTiaozheng;
    @BindView(R.id.tv_yaowu_fucong_info)
    TextView tvYaowuFucongInfo;
    @BindView(R.id.ll_yaowu_fucong)
    LinearLayout llYaowuFucong;
    @BindView(R.id.tv_yaowu_buliang_info)
    TextView tvYaowuBuliangInfo;
    @BindView(R.id.ll_yaowu_buliang)
    LinearLayout llYaowuBuliang;
    @BindView(R.id.tv_tixuetang_info)
    TextView tvTixuetangInfo;
    @BindView(R.id.ll_dixuetang)
    LinearLayout llDixuetang;
    @BindView(R.id.tv_zhushiliang_info)
    TextView tvZhushiliangInfo;
    @BindView(R.id.ll_zhushiliang)
    LinearLayout llZhushiliang;
    @BindView(R.id.health_detect_iv_qrcode)
    ImageView healthDetectIvQrcode;
    @BindView(R.id.health_detect_ll_qrcode)
    LinearLayout healthDetectLlQrcode;
    @BindView(R.id.tv_zunyi_info)
    TextView tvZunyiInfo;
    @BindView(R.id.ll_zunyi)
    LinearLayout llZunyi;
    private String detectCategory;

    private String text = NetworkApi.BasicUrl + "/ZZB/br/whole_informations?bid="
            + MyApplication.getInstance().userId
            + "&bname=" + MyApplication.getInstance().userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_activity_result);
        ButterKnife.bind(this);
        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = QrCodeUtils.encodeQrCode(text, UiUtils.pt(400), UiUtils.pt(400));
                if (bitmap != null) {
                    Handlers.ui().post(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.health_detect_ll_qrcode).setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(R.id.health_detect_iv_qrcode)).setImageBitmap(bitmap);
                        }
                    });
                } else {
                    findViewById(R.id.health_detect_ll_qrcode).setVisibility(View.GONE);
                }
            }
        });
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("体  检  报  告");
        mRightView.setVisibility(View.GONE);
        mRightText.setVisibility(View.GONE);
        Intent intent = getIntent();
        detectCategory = getIntent().getStringExtra("detectCategory");
        /**
         * 上传json数据的原对象
         */
        DetectResult detectResult = new DetectResult();
        String tem = intent.getStringExtra("tem");
        tem = TextUtils.isEmpty(tem) ? "0" : tem;
        detectResult.setTemperAture(tem);
        detectResult.currentPhoto = getIntent().getStringExtra("detectHeadIcon");
        //糖尿病体检
        String hypoglycemia = getIntent().getStringExtra("dixuetang");
        String stapleFood = getIntent().getStringExtra("zhushi");
        detectResult.hypoglycemia = hypoglycemia;
        detectResult.stapleFood = stapleFood;


        //血压随访新加的
        detectResult.psychologicalRecovery = getIntent().getStringExtra("xinli");
        detectResult.drugCompliance = getIntent().getStringExtra("yaowuyicong");
        detectResult.drugAdverseReaction = getIntent().getStringExtra("yaowubuliang");
        detectResult.sportWeekTimes = intent.getStringExtra("times");
        detectResult.sportTime = intent.getStringExtra("minutes");


        //左手血压测量值
        String highPressure = intent.getStringExtra("highPressure");
        highPressure = TextUtils.isEmpty(highPressure) ? "0" : highPressure;
        String lowPressure = intent.getStringExtra("lowPressure");
        lowPressure = TextUtils.isEmpty(lowPressure) ? "0" : lowPressure;

        detectResult.leftHypertension = new DetectResult.LeftHypertensionBean(Integer.parseInt(highPressure), Integer.parseInt(lowPressure));

        //右手血压测量值
        String highPressureRight = intent.getStringExtra("highPressure_right");
        highPressureRight = TextUtils.isEmpty(highPressureRight) ? "0" : highPressureRight;
        String lowPressureRight = intent.getStringExtra("lowPressure_right");
        lowPressureRight = TextUtils.isEmpty(lowPressureRight) ? "0" : lowPressureRight;
        detectResult.rightHypertension = new DetectResult.RightHypertensionBean(Float.parseFloat(highPressureRight), Float.parseFloat(lowPressureRight));

        //血压随访脉搏
        detectResult.pulse = getIntent().getIntExtra("pulse", 0);

        String ecg = intent.getStringExtra("ecg");
        ecg = TextUtils.isEmpty(ecg) ? "0" : ecg;
        detectResult.setEcg(ecg);

        //健康体检
        detectResult.heartRate = getIntent().getIntExtra("heartRate", 0);

        String cholesterol = getIntent().getStringExtra("cholesterol");
        if (!TextUtils.isEmpty(cholesterol)) {
            detectResult.cholesterol = Float.parseFloat(cholesterol);
        } else {
            detectResult.cholesterol = 0;
        }

        String weight = intent.getStringExtra("weight");
        weight = TextUtils.isEmpty(weight) ? "0" : weight;
        detectResult.weight = Float.parseFloat(weight);
        long[] symptoms = intent.getLongArrayExtra("symptoms");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < symptoms.length; i++) {
            builder.append(symptoms[i]).append(",");
        }
        switch (detectCategory) {
            case "detectHealth":
                detectResult.setHealthExaminationType("0");
                detectResult.setHealthSymptom(builder.toString());
                break;
            case "detectPressure":
                detectResult.setHealthExaminationType("1");
                detectResult.setHypertensionSymptom(builder.toString());
                break;
            case "detectSugar":
                detectResult.setHealthExaminationType("2");
                detectResult.setDiabetesSymptom(builder.toString());
                break;
        }
//        detectResult.setSportCost(intent.getStringExtra("minutes"));
//        detectResult.setSportFrequency(intent.getStringExtra("times"));
        detectResult.setSaltIntake(intent.getStringExtra("salt"));
        detectResult.setWineDrink(intent.getStringExtra("drink"));
        detectResult.setSmoke(intent.getStringExtra("smoke"));
        String sugar = intent.getStringExtra("sugar");
        sugar = TextUtils.isEmpty(sugar) ? "0" : sugar;
        detectResult.setBloodSugar(Float.parseFloat(sugar));
        detectResult.setUserId(Integer.parseInt(LocalShared.getInstance(this).getUserId()));
        String url = NetworkApi.BasicUrl + "/ZZB/api/health/inquiry/examination/";
        OkGo.<String>post(url).headers("equipmentId", Utils.getDeviceId())
                .upJson(new Gson().toJson(detectResult))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body());
//                    String message = jsonObject.optString("message");
//                    if (!TextUtils.isEmpty(message)) {
//                        T.show(message);
//                    }
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }

                        String body = response.body();
                        ExaminationReportBean bean = new Gson().fromJson(body, ExaminationReportBean.class);
                        if (bean != null) {

                            if (bean.tag) {
                                if (bean.data != null) {
                                    dealOtherDataInfo(bean.data);
                                }

                            } else {
                                String message = bean.message;
                                if (!TextUtils.isEmpty(message)) {
                                    T.show(message);
                                }
                            }

                        }


                    }
                });

//            String oxygen = intent.getStringExtra("oxygen");
//            mDetectTvResultOxygenInfo.setText(oxygen + "mmHg");
//            mDetectTvResultOxygenInfoIndicator.setText("偏低");


        mDetectTvResultWeightInfo.setText(weight + " kg");
        if (detectCategory.equals("detectHealth")) {
            mDetectTvResultHighPressureInfo.setText("左臂:" + highPressure + " mmHg,   " + "右臂:" + highPressureRight + " mmHg");
            mDetectTvResultLowPressureInfo.setText("左臂:" + lowPressure + " mmHg,   " + "右臂:" + lowPressureRight + " mmHg");
        } else {
            mDetectTvResultHighPressureInfo.setText(highPressure + " mmHg");
            mDetectTvResultLowPressureInfo.setText(lowPressure + " mmHg");
        }

        mDetectTvResultTemperateInfo.setText(tem + "℃");
        mDetectTvResultSugarInfo.setText(sugar + "  mmol/L");
        String[] stringArray = getResources().getStringArray(R.array.ecg_measureres);
        mDetectTvResultEcgInfo.setText(stringArray[Integer.parseInt(ecg)]);

        tvHeartRateInfo.setText(detectResult.heartRate + "");
        tvCholesterinInfo.setText(detectResult.cholesterol + "");
        if (detectResult.psychologicalRecovery != null) {
            tvXinliTiaozhengInfo.setText(getResources().getStringArray(R.array.xltz)[Integer.parseInt(detectResult.psychologicalRecovery) - 1]);
        }
        if (detectResult.drugCompliance != null) {
            tvYaowuFucongInfo.setText(getResources().getStringArray(R.array.ywfc)[Integer.parseInt(detectResult.drugCompliance)]);
        }
        if (detectResult.drugAdverseReaction != null) {
            tvYaowuBuliangInfo.setText(getResources().getStringArray(R.array.ywbl)[Integer.parseInt(detectResult.drugAdverseReaction)]);
        }
        if (detectResult.hypoglycemia != null) {
            tvTixuetangInfo.setText(getResources().getStringArray(R.array.dxtfy)[Integer.parseInt(detectResult.hypoglycemia) - 1]);
        }
        String zunyi = getIntent().getStringExtra("zunyi");
        if (TextUtils.isEmpty(zunyi)) {
            tvZunyiInfo.setText(getResources().getStringArray(R.array.xltz)[Integer.parseInt(zunyi)]);
        }
        tvZhushiliangInfo.setText(stapleFood + "克");
        switch (detectCategory) {
            case "detectHealth":
                llXinliTiaozheng.setVisibility(View.GONE);
                llYaowuBuliang.setVisibility(View.GONE);
                llDixuetang.setVisibility(View.GONE);
                llZhushiliang.setVisibility(View.GONE);
                llXinliTiaozheng.setVisibility(View.GONE);
                llYaowuBuliang.setVisibility(View.GONE);
                llZunyi.setVisibility(View.GONE);

                llDixuetang.setVisibility(View.GONE);
                llZhushiliang.setVisibility(View.GONE);
                break;
            case "detectPressure":
                detectTvResultTemperate.setVisibility(View.GONE);
                detectTvResultSugar.setVisibility(View.GONE);
                detectTvResultEcg.setVisibility(View.GONE);
                llHeartRate.setVisibility(View.GONE);

                llUa.setVisibility(View.GONE);
                llCholesterin.setVisibility(View.GONE);

                llDixuetang.setVisibility(View.GONE);
                llZhushiliang.setVisibility(View.GONE);

                llDixuetang.setVisibility(View.GONE);
                llZhushiliang.setVisibility(View.GONE);

                break;
            case "detectSugar":
                detectTvResultTemperate.setVisibility(View.GONE);
                llUa.setVisibility(View.GONE);
                llCholesterin.setVisibility(View.GONE);
                llHealthSalt.setVisibility(View.GONE);
                break;
        }


        initView();
    }

    private void dealOtherDataInfo(ExaminationReportBean.DataBean data) {
        //健康体检症状
        if (data.healthSymptom == null) {
            llHealthZz.setVisibility(View.GONE);
        } else {
            llHealthZz.setVisibility(View.VISIBLE);
            detectTvResultHealthZzInfo.setText(data.healthSymptom.replaceAll("null", ""));
        }
        //高血压体检症状
        if (data.hypertensionSymptom == null) {
            llPressureSmoke.setVisibility(View.GONE);
        } else {
            llPressureSmoke.setVisibility(View.VISIBLE);
            detectTvResultPressureZzInfo.setText(data.hypertensionSymptom.replaceAll("null", ""));
        }

        //高血压体检  脉搏
        if (data.pulse == 0) {
            llPressureulse.setVisibility(View.GONE);
        } else {
            llPressureulse.setVisibility(View.VISIBLE);
            tvPulse.setText(data.pulse + "");
        }

        //糖尿病体检症状
        if (data.diabetesSymptom == null) {
            llSugarZz.setVisibility(View.GONE);
        } else {
            llSugarZz.setVisibility(View.VISIBLE);
            detectTvResultSugarZzInfo.setText(data.diabetesSymptom.replaceAll("null", ""));
        }

        //糖尿病症状

        //运动情况 三种都有
        detectTvResultHealthRunTimeInfo.setText(data.sportWeekTimes + "次/周," + data.sportTime + "分钟/次");
        //食盐摄入量
        if (data.saltIntake == null) {
            llHealthSalt.setVisibility(View.GONE);
        } else {
            llHealthSalt.setVisibility(View.VISIBLE);
            detectTvResultHealthSaltInfo.setText(data.saltIntake);
        }

        //饮酒情况
        if (data.wineDrink == null) {
            llHealthDrink.setVisibility(View.GONE);
        } else {
            llHealthDrink.setVisibility(View.VISIBLE);
            detectTvResultHealthDrinkInfo.setText(data.wineDrink + "两/日");
        }

        //吸烟情况  三种体检都有
        detectTvResultHealthSmokeInfo.setText(data.smoke + "支/日");

    }

    private void initView() {
        showLoadingDialog("正在加载中...");
        NetworkApi.getPersonalInfo(this, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response != null) {
                    Gson gson = new Gson();
                    PersonInfoResultBean bean = gson.fromJson(response.body(), PersonInfoResultBean.class);
                    if (bean != null) {
                        PersonInfoResultBean.DataBean data = bean.data;
                        if (data != null) {
                            mDetectTvNameInfo.setText(data.bname + "");
                            mDetectTvAgeInfo.setText(data.age + "岁");
                            mDetectTvGenderInfo.setText(data.sex + "");
                            //接口数据接口更改 身高体重使用外层数据
                            mDetectTvHeightInfo.setText(data.height + " cm");
                            mDetectTvWeightInfo.setText(data.weight + " kg");


                            PersonInfoResultBean.DataBean.RecordBean record = data.record;
                            if (record != null) {
                                if (TextUtils.isEmpty(record.bloodType)) {
                                    record.bloodType = "暂未填写";
                                }
                                mDetectTvBloodTypeInfo.setText(record.bloodType + "型");
//                                mDetectTvHeightInfo.setText(record.height);
//                                mDetectTvWeightInfo.setText(record.weight);
                            }
                        }


                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                T.show("网络繁忙");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });


    }
}
