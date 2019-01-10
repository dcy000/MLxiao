package com.gcml.module_blutooth_devices.fingerprint_devices;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.display.ImageUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.bluetooth.BaseBluetooth;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;

import java.util.ArrayList;

public class Fingerpint_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {

    /**
     * 指纹录入
     */
    private Button mInputFingerprint;
    private TextView mInputFeature;
    /**
     * 指纹验证
     */
    private Button mValidateFingerprint;
    private TextView mValidateFeature;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    /**  */
    private TextView mResult;
    private ImageView mFingerprintImage;
    private Bundle bundle;
    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_fingerpint;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mInputFingerprint = view.findViewById(R.id.input_fingerprint);
        mInputFingerprint.setOnClickListener(this);
        mInputFeature = view.findViewById(R.id.input_feature);
        mValidateFingerprint = view.findViewById(R.id.validate_fingerprint);
        mValidateFingerprint.setOnClickListener(this);
        mValidateFeature = view.findViewById(R.id.validate_feature);
        mResult = view.findViewById(R.id.result);
        mFingerprintImage = view.findViewById(R.id.fingerprint_image);
        this.bundle=bundle;

    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        baseBluetoothPresenter = new Fingerprint_WeiEr_PresenterImp(this,
                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MIX, null, "zjwellcom"),new ArrayList<byte[]>());
//        dealLogic();
    }

    public void dealLogic() {
        String address = null;
        String brand = null;
        String sp_fingerprint = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_FINGERPRINT, "");
        if (!TextUtils.isEmpty(sp_fingerprint)) {
            String[] split = sp_fingerprint.split(",");
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
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.CONTROL_FINGERPRINT);
            helper.start();
        } else {
            switch (brand) {
                case "zjwellcom":
                    baseBluetoothPresenter = new Fingerprint_WeiEr_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MIX, address, "zjwellcom"),new ArrayList<byte[]>());
                    break;
            }
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            String data = datas[0];
            if (data.equals("input")) {
                mInputFeature.setText(datas[1]);
            } else if (data.equals("validate")) {
                mValidateFeature.setText(datas[1]);
            } else if (data.equals("image")) {
                mFingerprintImage.setImageBitmap(ImageUtils.convertStringToIcon(datas[1]));
            }
        }
    }

    @Override
    public void updateState(String state) {
        mResult.setText(state);
    }

    @Override
    public Context getThisContext() {
        return this.getContext();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.input_fingerprint) {
            baseBluetoothPresenter.collectFingers();
        } else if (i == R.id.validate_fingerprint) {
            if (TextUtils.isEmpty(mInputFeature.getText())) {
                ToastUtils.showShort("请先录入指纹");
                return;
            }
            baseBluetoothPresenter.validateFinger();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (baseBluetoothPresenter != null) {
            baseBluetoothPresenter.onDestroy();
            baseBluetoothPresenter=null;
        }
        if (helper != null) {
            helper.destroy();
            helper=null;
        }
    }
}
