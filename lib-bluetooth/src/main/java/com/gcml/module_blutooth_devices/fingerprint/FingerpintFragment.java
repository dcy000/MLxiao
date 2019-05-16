package com.gcml.module_blutooth_devices.fingerprint;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.display.ImageUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;

import java.util.ArrayList;

public class FingerpintFragment extends BluetoothBaseFragment implements View.OnClickListener {

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
    /**  */
    private TextView mResult;
    private ImageView mFingerprintImage;
    private Fingerprint_WeiEr_PresenterImp baseBluetoothPresenter;

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

    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        baseBluetoothPresenter = new Fingerprint_WeiEr_PresenterImp(this, new ArrayList<byte[]>());
    }


    @Override
    public void updateData(DetectionData detectionData) {
        //TODO:暂时不用
//        if (datas.length == 2) {
//            String data = datas[0];
//            if (data.equals("input")) {
//                mInputFeature.setText(datas[1]);
//            } else if (data.equals("validate")) {
//                mValidateFeature.setText(datas[1]);
//            } else if (data.equals("image")) {
//                mFingerprintImage.setImageBitmap(ImageUtils.convertStringToIcon(datas[1]));
//            }
//        }
    }

    @Override
    public void updateState(String state) {
        mResult.setText(state);
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
            baseBluetoothPresenter = null;
        }
    }
}
