package com.example.han.referralproject.health;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.util.LocalShared;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_activity_result);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("体  检  报  告");
        Intent intent = getIntent();
        if (intent != null && MyApplication.getInstance().account != null) {
            mDetectTvNameInfo.setText(MyApplication.getInstance().account.bname);
            mDetectTvHeightInfo.setText(MyApplication.getInstance().account.height + "cm");
            mDetectTvWeightInfo.setText(MyApplication.getInstance().account.weight + "kg");
            String sex = MyApplication.getInstance().account.sex;
            sex = TextUtils.isEmpty(sex) ? "女" : sex;
            mDetectTvGenderInfo.setText(sex);
            mDetectTvAgeInfo.setText(MyApplication.getInstance().account.age);
            mDetectTvBloodTypeInfo.setText(MyApplication.getInstance().account.blood_type + "型");
            mDetectTvResultWeightInfo.setText(MyApplication.getInstance().account.weight + "kg");
            mDetectTvResultOxygenInfo.setText(intent.getStringExtra("oxygen") + "mmHg");
//            mDetectTvResultOxygenInfoIndicator.setText("偏低");
            mDetectTvResultHighPressureInfo.setText(intent.getStringExtra("highPressure") + "mmHg");
            mDetectTvResultLowPressureInfo.setText(intent.getStringExtra("lowPressure") + "mmHg");
            mDetectTvResultTemperateInfo.setText(intent.getStringExtra("tem"));
            mDetectTvResultSugarInfo.setText(intent.getStringExtra("sugar") + "mmol/L");
            mDetectTvResultEcgInfo.setText(intent.getStringExtra("ecg"));
        }
    }
}
