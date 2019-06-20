package com.gcml.health.measure.single_measure.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/2 9:29
 * created by: gzq
 * description: 心电选择界面
 */
public class ChooseECGDeviceFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private View view;
    private LinearLayout mLlKeruikang;
    private LinearLayout mLlBosheng;
    /**
     * 可瑞康心电
     */
    public static final int DEVICE_ECG_KERUIKANG=1;
    /**
     * 博声心电
     */
    public static final int DEVICE_ECG_BOSHENG=2;
    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_choose_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mLlKeruikang = (LinearLayout) view.findViewById(R.id.ll_keruikang);
        mLlKeruikang.setOnClickListener(this);
        mLlKeruikang.setVisibility(View.GONE);
        mLlBosheng = (LinearLayout) view.findViewById(R.id.ll_bosheng);
        mLlBosheng.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_keruikang) {
            SPUtil.put(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG,DEVICE_ECG_KERUIKANG);
            if (fragmentChanged!=null){
                Bundle bundle=new Bundle();
                bundle.putInt(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG,DEVICE_ECG_KERUIKANG);
                fragmentChanged.onFragmentChanged(this,bundle);
            }
        } else if (i == R.id.ll_bosheng) {
            SPUtil.put(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG,DEVICE_ECG_BOSHENG);
            Bundle bundle=new Bundle();
            bundle.putInt(BluetoothConstants.SP.SP_SAVE_DEVICE_ECG,DEVICE_ECG_BOSHENG);
            fragmentChanged.onFragmentChanged(this,bundle);
        } else {
        }
    }
}
