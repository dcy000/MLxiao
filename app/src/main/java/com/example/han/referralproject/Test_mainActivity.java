package com.example.han.referralproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.activity.AllMeasureActivity;
import com.example.han.referralproject.ecg.ECGCompatActivity;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_bluetooth.BluetoothConstants;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Test_mainActivity extends ToolbarBaseActivity implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    @BindView(R.id.ll_xueya)
    LinearLayout llXueya;
    @BindView(R.id.ll_xueyang)
    LinearLayout llXueyang;
    @BindView(R.id.ll_tiwen)
    LinearLayout llTiwen;
    @BindView(R.id.ll_xuetang)
    LinearLayout llXuetang;
    @BindView(R.id.cl_1)
    ConstraintLayout cl1;
    @BindView(R.id.ll_xindian)
    LinearLayout llXindian;
    @BindView(R.id.ll_tizhong)
    LinearLayout llTizhong;
    @BindView(R.id.ll_san)
    LinearLayout llSan;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.cl_2)
    ConstraintLayout cl2;
    private long lastClickTime = 0;

    private boolean isTest;

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        if (isTest) {
            backMainActivity();
        }
        finish();
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_test_main2;
    }

    @Override
    public void initParams(Intent intentArgument) {
        isTest = intentArgument.getBooleanExtra("isTest", false);
        MLVoiceSynthetize.startSynthesize(R.string.tips_test);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        llXueya.setOnClickListener(this);
        llXueyang.setOnClickListener(this);
        llXuetang.setOnClickListener(this);
        llXindian.setOnClickListener(this);
        llTizhong.setOnClickListener(this);
        llTiwen.setOnClickListener(this);
        llSan.setOnClickListener(this);
        llMore.setOnClickListener(this);



    }

    @NonNull
    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;


            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.ll_xueya:
                    Bundle bloodpressure = new Bundle();
                    bloodpressure.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_SPHYGMOMANOMETER);
                    ActivityUtils.skipActivity(AllMeasureActivity.class, bloodpressure);

                    break;
                case R.id.ll_xueyang:
                    Bundle bloodoxygen = new Bundle();
                    bloodoxygen.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_OXIMETER);
                    ActivityUtils.skipActivity(AllMeasureActivity.class, bloodoxygen);
                    break;
                case R.id.ll_tiwen:
                    Bundle tiwen = new Bundle();
                    tiwen.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_TEMPERATURE);
                    ActivityUtils.skipActivity(AllMeasureActivity.class, tiwen);
                    break;
                case R.id.ll_xuetang:
                    Bundle bloodsugar = new Bundle();
                    bloodsugar.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_BLOOD_GLUCOSE_METER);
                    ActivityUtils.skipActivity(AllMeasureActivity.class, bloodsugar);
                    break;
                case R.id.ll_xindian:
                    ECGCompatActivity.startActivity(this);
                    break;
                case R.id.ll_san:
                    Bundle triple = new Bundle();
                    triple.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_TRIPLE);
                    ActivityUtils.skipActivity(AllMeasureActivity.class, triple);
                    break;
                case R.id.ll_tizhong://体重
                    Bundle weight = new Bundle();
                    weight.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_WEIGHING_SCALE);
                    ActivityUtils.skipActivity(AllMeasureActivity.class, weight);
                    break;
                case R.id.ll_more://敬请期待
                    ToastUtils.showShort("敬请期待");
                    break;
            }
        }
    }
}
