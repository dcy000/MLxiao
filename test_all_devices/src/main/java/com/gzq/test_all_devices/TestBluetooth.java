package com.gzq.test_all_devices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.others.BreathHomeResultBean;
import com.gcml.module_blutooth_devices.others.BreathHome_PresenterImp;
import com.google.gson.Gson;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/18 14:30
 * created by:gzq
 * description:TODO
 */
public class TestBluetooth extends AppCompatActivity implements IView, View.OnClickListener {

    private BreathHome_PresenterImp presenterImp;
    /**
     * 男或者女
     */
    private EditText mEtSex;
    /**
     * 单位：岁
     */
    private EditText mEtAge;
    /**
     * 单位：cm
     */
    private EditText mEtHeight;
    /**
     * 单位：kg
     */
    private EditText mEtWeight;
    /**
     * 开始连接
     */
    private Button mBtnConnect;
    /**
     * 123L/min
     */
    private TextView mTvPef;
    /**
     * 123L/min
     */
    private TextView mTvPev1;
    /**
     * 123L/min
     */
    private TextView mTvFvc;
    /**
     * 123L/min
     */
    private TextView mTvMef75;
    /**
     * 123L/min
     */
    private TextView mTvMef50;
    /**
     * 123L/min
     */
    private TextView mTvMef25;
    /**
     * 123L/min
     */
    private TextView mTvMmef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bluetooth);
        initView();
    }

    @Override
    public void updateData(String... datas) {
        BreathHomeResultBean breathHomeResultBean = new Gson().fromJson(datas[0], BreathHomeResultBean.class);
        mTvPef.setText(breathHomeResultBean.getPef() + "L/min");
        mTvPev1.setText(breathHomeResultBean.getPev1() + "");
        mTvFvc.setText(breathHomeResultBean.getFvc());
        mTvMef75.setText(breathHomeResultBean.getMef75() + "L/s");
        mTvMef50.setText(breathHomeResultBean.getMef50() + "L/s");
        mTvMef25.setText(breathHomeResultBean.getMef25() + "L/s");
        mTvMmef.setText(breathHomeResultBean.getMmef() + "L/s");
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
    }

    @Override
    public Context getThisContext() {
        return this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenterImp != null) {
            presenterImp.onDestroy();
            presenterImp = null;
        }
    }

    private void initView() {
        mEtSex = (EditText) findViewById(R.id.et_sex);
        mEtAge = (EditText) findViewById(R.id.et_age);
        mEtHeight = (EditText) findViewById(R.id.et_height);
        mEtWeight = (EditText) findViewById(R.id.et_weight);
        mBtnConnect = (Button) findViewById(R.id.btn_connect);
        mBtnConnect.setOnClickListener(this);
        mTvPef = (TextView) findViewById(R.id.tv_pef);
        mTvPev1 = (TextView) findViewById(R.id.tv_pev1);
        mTvFvc = (TextView) findViewById(R.id.tv_fvc);
        mTvMef75 = (TextView) findViewById(R.id.tv_mef75);
        mTvMef50 = (TextView) findViewById(R.id.tv_mef50);
        mTvMef25 = (TextView) findViewById(R.id.tv_mef25);
        mTvMmef = (TextView) findViewById(R.id.tv_mmef);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_connect:
                checkParam();
                break;
        }
    }

    private void checkParam() {
        String sex = mEtSex.getText().toString().trim();
        String age = mEtAge.getText().toString().trim();
        String height = mEtHeight.getText().toString().trim();
        String weight = mEtWeight.getText().toString().trim();
        if (TextUtils.isEmpty(sex) || TextUtils.isEmpty(age) || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)) {
            ToastUtils.showShort("请填写必要参数");
            return;
        }
        presenterImp = new BreathHome_PresenterImp(this, new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC,
                "00:15:87:21:11:D9", "B810229665"),
                ("sex".equals("男") ? "0" : "1"), age, height, weight);
    }
}
