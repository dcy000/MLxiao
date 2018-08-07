package com.gcml.module_blutooth_devices.weight_devices;

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
import com.gcml.module_blutooth_devices.utils.SharePreferenceHelper;

import java.lang.reflect.InvocationTargetException;

public class Weight_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvTizhong;
    private TextView mTvTizhi;
    private BaseBluetoothPresenter bluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private Bundle bundle;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_weight;
    }

    @Override
    protected void initView(View view, final Bundle bundle) {

        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvTizhong = (TextView) view.findViewById(R.id.tv_tizhong);
        mTvTizhi = (TextView) view.findViewById(R.id.tv_tizhi);
        this.bundle = bundle;

    }

    @Override
    public void onResume() {
        super.onResume();
        dealLogic();
    }

    public void dealLogic() {
        String address = null;
        String brand = null;
        String sp_weight = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, "");
        if (!TextUtils.isEmpty(sp_weight)) {
            String[] split = sp_weight.split(",");
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
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_WEIGHT);
            helper.start();
        } else {
            switch (brand) {
                case "VScale":
                    bluetoothPresenter = new Weight_Bodivis_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "VScale"));
                    break;
                case "SHHC-60F1":
                    bluetoothPresenter = new Weight_Yike_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "SHHC-60F1"));
                    break;
                case "iChoice":
                    bluetoothPresenter = new Weight_Chaosi_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                    break;
                case "SENSSUN CLOUD":
                    bluetoothPresenter = new Weight_Xiangshan_EF895i_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "SENSSUN CLOUD"));
                    break;
                case "000FatScale01":
                    bluetoothPresenter = new Weight_Self_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "000FatScale01"));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_WEIGHT);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_WEIGHT);
            }
            clickVideoDemo(v);
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            onMeasureFinished(datas[0]);
            if (mTvTizhong != null) {
                mTvTizhong.setText(datas[0]);
            }
            String userHeight = null;
            try {
                userHeight = SharePreferenceHelper.getInstance().getLocalShared(getContext()).getUserHeight();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(userHeight)) {
                float parseFloat = Float.parseFloat(userHeight);
                float weight = Float.parseFloat(datas[0]);
                if (mTvTizhi != null) {
                    mTvTizhi.setText(String.format("%.2f", weight / (parseFloat * parseFloat / 10000)));
                }
            }
        }
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
//        ((AllMeasureActivity) getActivity()).speak(state);
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
        if (bluetoothPresenter != null)
            bluetoothPresenter.onDestroy();
        if (helper != null) {
            helper.destroy();
        }
    }
}
