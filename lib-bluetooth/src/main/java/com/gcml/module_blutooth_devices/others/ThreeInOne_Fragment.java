package com.gcml.module_blutooth_devices.others;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.display.ToastUtils;
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
    private boolean isMeasureBloodsugarFinished;
    private boolean isMeasureBUAFinished;
    private boolean isMeasureCholesterolFinished;
    /**
     * &lt;3.9
     */
    protected TextView mTitle11;
    /**
     * 3.9~6.1
     */
    protected TextView mTitle12;
    /**
     * &gt;6.1
     */
    protected TextView mTitle13;
    protected TextView mTitle1;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_three_in_one;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = view.findViewById(R.id.tv_gaoya);
        mTvDiya = view.findViewById(R.id.tv_diya);
        mTvMaibo = view.findViewById(R.id.tv_maibo);
        this.bundle = bundle;
        mTitle1 = (TextView) view.findViewById(R.id.title1);
        mTitle11 = (TextView) view.findViewById(R.id.title1_1);
        mTitle12 = (TextView) view.findViewById(R.id.title1_2);
        mTitle13 = (TextView) view.findViewById(R.id.title1_3);
    }

    @Override
    public void onResume() {
        super.onResume();
        dealLogic();
    }

    public void dealLogic() {
        String address = null;
        String brand = null;
        String sp_others = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE, "");
        if (!TextUtils.isEmpty(sp_others)) {
            String[] split = sp_others.split(",");
            if (split.length == 2) {
                brand = split[0];
                address = split[1];
                chooseConnectType(address, brand);
                return;
            }
        }
        if (bundle != null) {
            address = bundle.getString(IPresenter.DEVICE_BLUETOOTH_ADDRESS);
            brand = bundle.getString(IPresenter.BRAND);
            chooseConnectType(address, brand);
            return;
        }
        chooseConnectType(address, brand);
    }

    private void chooseConnectType(String address, String brand) {
        if (TextUtils.isEmpty(address)) {
            if (helper==null){
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_OTHERS);
            }
            helper.start();
        } else {
            if (bluetoothPresenter!=null){
                bluetoothPresenter.checkBlueboothOpened();
                return;
            }
            switch (brand) {
                case "BeneCheck":
                    bluetoothPresenter = new ThreeInOne_Self_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BeneCheck"));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_OTHERS);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_OTHERS);
            }
            clickVideoDemo(v);
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
//            mTvGaoya.setText("0.00");
//            mTvDiya.setText("0.00");
//            mTvMaibo.setText("0.00");
            isMeasureBloodsugarFinished = false;
            isMeasureBUAFinished = false;
            isMeasureCholesterolFinished = false;
            return;
        }
        if (datas.length == 2) {
            if (datas[0].equals("bloodsugar") && !isMeasureBloodsugarFinished) {
                isMeasureBloodsugarFinished = true;
                mTvGaoya.setText(datas[1]);
                onMeasureFinished(datas[0], datas[1]);
            } else if (datas[0].equals("bua") && !isMeasureBUAFinished) {
                isMeasureBUAFinished = true;
                mTvDiya.setText(datas[1]);
                onMeasureFinished(datas[0], datas[1]);
            } else if (datas[0].equals("cholesterol") && !isMeasureCholesterolFinished) {
                isMeasureCholesterolFinished = true;
                mTvMaibo.setText(datas[1]);
                onMeasureFinished(datas[0], datas[1]);
            }

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

    @Override
    public void onStop() {
        super.onStop();
        if (bluetoothPresenter != null) {
            bluetoothPresenter.onDestroy();
        }
        if (helper != null) {
            helper.destroy();
        }
    }

    public interface MeasureItemChanged {
        void onChanged(int position);
    }

    protected MeasureItemChanged measureItemChanged;

    public void setOnMeasureItemChanged(MeasureItemChanged measureItemChanged) {
        this.measureItemChanged = measureItemChanged;
    }
}
