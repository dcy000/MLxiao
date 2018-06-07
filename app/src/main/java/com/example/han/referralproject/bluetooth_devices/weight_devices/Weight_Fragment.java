package com.example.han.referralproject.bluetooth_devices.weight_devices;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.AllMeasureActivity;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BaseFragment;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.bloodoxygen_devices.Bloodoxygen_Chaosi_PresenterImp;
import com.example.han.referralproject.bluetooth_devices.bloodoxygen_devices.Bloodoxygen_Kangtai_PresenterImp;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;

public class Weight_Fragment extends BaseFragment implements IView, View.OnClickListener {
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvTizhong;
    private TextView mTvTizhi;
    private BaseBluetoothPresenter bluetoothPresenter;

    @Override
    protected int initLayout() {
        return R.layout.fragment_weight;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvTizhong = (TextView) view.findViewById(R.id.tv_tizhong);
        mTvTizhi = (TextView) view.findViewById(R.id.tv_tizhi);
        dealLogic(bundle);
    }

    private void dealLogic(Bundle bundle) {
        String tizhongMac = LocalShared.getInstance(getContext()).getTizhongMac();
        if (bundle != null) {
            String brand = bundle.getString(IPresenter.BRAND);
            if (TextUtils.isEmpty(tizhongMac)) {
                switch (brand) {
                    case "QING_HUA_TONG_FANG":
                        bluetoothPresenter = new Weight_Bodivis_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "VScale"));
                        break;
                    case "YI_KE":
                        bluetoothPresenter = new Weight_Yike_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "SHHC-60F1"));
                        break;
                }
            } else {
                switch (brand) {
                    case "QING_HUA_TONG_FANG":
                        bluetoothPresenter = new Weight_Bodivis_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, tizhongMac, "VScale"));
                        break;
                    case "YI_KE":
                        bluetoothPresenter = new Weight_Yike_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, tizhongMac, "SHHC-60F1"));
                        break;
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_health_history:
                break;
            case R.id.btn_video_demo:
                break;
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            mTvTizhong.setText(datas[0]);
        }
    }

    @Override
    public void updateState(String state) {
        ToastTool.showShort(state);
        ((AllMeasureActivity) getActivity()).speak(state);
    }

    @Override
    public void onStop() {
        super.onStop();
        bluetoothPresenter.onDestroy();
    }
}
