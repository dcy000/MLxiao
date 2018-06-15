package com.example.han.referralproject.health;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health.model.DetectResult;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.bean.PersonInfoResultBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.UiUtils;
import com.medlink.danbogh.utils.Utils;
import com.ml.zxing.QrCodeUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
        DetectResult detectResult = new DetectResult();
        String tem = intent.getStringExtra("tem");
        tem = TextUtils.isEmpty(tem) ? "0.0" : tem;
        detectResult.setTemperAture(tem);

        //左手血压测量值
        String highPressure = intent.getStringExtra("highPressure");
        highPressure = TextUtils.isEmpty(highPressure) ? "0.0" : highPressure;
        String lowPressure = intent.getStringExtra("lowPressure");
        lowPressure = TextUtils.isEmpty(lowPressure) ? "0.0" : lowPressure;

        detectResult.leftHypertension = new DetectResult.LeftHypertensionBean(Integer.parseInt(highPressure), Integer.parseInt(lowPressure));

        //右手血压测量值
        String highPressureRight = intent.getStringExtra("highPressure_right");
        highPressureRight = TextUtils.isEmpty(highPressureRight) ? "0.0" : highPressureRight;
        String lowPressureRight = intent.getStringExtra("lowPressure_right");
        lowPressureRight = TextUtils.isEmpty(lowPressureRight) ? "0.0" : lowPressureRight;
        detectResult.rightHypertension = new DetectResult.RightHypertensionBean(Float.parseFloat(highPressureRight), Float.parseFloat(lowPressureRight));

        String ecg = intent.getStringExtra("ecg");
        ecg = TextUtils.isEmpty(ecg) ? "0.0" : ecg;
        detectResult.setEcg(ecg);
        String weight = intent.getStringExtra("weight");
        weight = TextUtils.isEmpty(weight) ? "0.0" : weight;
        detectResult.setWeight((int) Float.parseFloat(weight));
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
        detectResult.setSportCost(intent.getStringExtra("minutes"));
        detectResult.setSportFrequency(intent.getStringExtra("times"));
        detectResult.setSaltIntake(intent.getStringExtra("salt"));
        detectResult.setWineDrink(intent.getStringExtra("drink"));
        detectResult.setSmoke(intent.getStringExtra("smoke"));
        detectResult.setWeight((int) Float.parseFloat(weight));
        String sugar = intent.getStringExtra("sugar");
        sugar = TextUtils.isEmpty(sugar) ? "0.0" : sugar;
        detectResult.setBloodSugar(Float.parseFloat(sugar));
        String url = NetworkApi.BasicUrl + "/ZZB/api/health/inquiry/examination/";
        OkGo.<String>post(url).upJson(new Gson().toJson(detectResult)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    String message = jsonObject.optString("message");
                    if (!TextUtils.isEmpty(message)) {
                        T.show(message);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

//            String oxygen = intent.getStringExtra("oxygen");
//            mDetectTvResultOxygenInfo.setText(oxygen + "mmHg");
//            mDetectTvResultOxygenInfoIndicator.setText("偏低");


        mDetectTvResultWeightInfo.setText(weight + "  kg");
        if (detectCategory.equals("detectHealth")) {
            mDetectTvResultHighPressureInfo.setText("左臂:" + highPressure + " mmHg,   " + "右臂:" + highPressureRight + " mmHg,");
            mDetectTvResultLowPressureInfo.setText("左臂:" + lowPressure + " mmHg,   " + "右臂:" + lowPressureRight + " mmHg,");
        } else {
            mDetectTvResultHighPressureInfo.setText(highPressure + " mmHg");
            mDetectTvResultLowPressureInfo.setText(lowPressure + " mmHg");
        }

        mDetectTvResultTemperateInfo.setText(tem);
        mDetectTvResultSugarInfo.setText(sugar + "  mmol/L");
        mDetectTvResultEcgInfo.setText(ecg);
        initView();
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
                            mDetectTvAgeInfo.setText(data.age + "");
                            mDetectTvGenderInfo.setText(data.sex + "");


                            PersonInfoResultBean.DataBean.RecordBean record = data.record;
                            if (record != null) {
                                mDetectTvBloodTypeInfo.setText(record.bloodType + "型");
                                mDetectTvHeightInfo.setText(record.height);
                                mDetectTvWeightInfo.setText(record.weight);
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
