package com.gcml.module_blutooth_devices.others;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/1 14:15
 * created by:gzq
 * description:TODO
 */
public class ThreeInOne_Fragment extends BluetoothBaseFragment implements View.OnClickListener, IView {
    /**
     * 历史记录
     */
    protected TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    protected TextView mBtnVideoDemo;
    /**
     * 0
     */
    private TextView mTvGaoya;
    /**
     * 0
     */
    private TextView mTvDiya;
    /**
     * 0
     */
    private TextView mTvMaibo;
    private SearchWithDeviceGroupHelper helper;
    private BaseBluetoothPresenter bluetoothPresenter;
    private Bundle bundle;
    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_three_in_one;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = (TextView) view.findViewById(R.id.tv_gaoya);
        mTvDiya = (TextView) view.findViewById(R.id.tv_diya);
        mTvMaibo = (TextView) view.findViewById(R.id.tv_maibo);
        this.bundle=bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        dealLogic();
    }

    public void dealLogic() {
        String address;
        String brand;
        if (bundle != null) {//该处是为测试使用的
            address = bundle.getString(IPresenter.DEVICE_BLUETOOTH_ADDRESS);
            brand = bundle.getString(IPresenter.BRAND);
            chooseConnectType(address, brand);
        } else {
            String sp_bloodoxygen = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE, null);
            if (TextUtils.isEmpty(sp_bloodoxygen)) {
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_OTHERS);
                helper.start();
            } else {
                String[] split = sp_bloodoxygen.split(",");
                if (split.length == 2) {
                    brand = split[0];
                    address = split[1];
                    chooseConnectType(address, brand);
                }

            }
        }
    }

    private void chooseConnectType(String address, String brand) {
        if (TextUtils.isEmpty(address)) {
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_OTHERS);
            helper.start();
        } else {
            switch (brand) {
                case "BeneCheck GL-0F8B0C":
                    bluetoothPresenter = new ThreeInOne_Self_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BeneCheck GL-0F8B0C"));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_TEMPERATURE);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_TEMPERATURE);
            }
            clickVideoDemo(v);
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            mTvGaoya.setText("0.00");
            mTvDiya.setText("0.00");
            mTvMaibo.setText("0.00");
            return;
        }
        if (datas.length == 2) {
            if (datas[0].equals("bloodsugar")) {
                mTvGaoya.setText(datas[1]);
            } else if (datas[0].equals("bua")) {
                mTvDiya.setText(datas[1]);
            } else if (datas[0].equals("cholesterol")) {
                mTvMaibo.setText(datas[1]);
            }
            onMeasureFinished(datas[0],datas[1]);
        }
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        if (dealVoiceAndJump != null) {
            dealVoiceAndJump.updateVoice(state);
        }
    }

    @Override
    public Context getThisContext() {
        return this.getContext();
    }
}
