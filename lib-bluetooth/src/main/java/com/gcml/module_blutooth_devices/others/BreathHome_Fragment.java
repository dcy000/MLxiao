package com.gcml.module_blutooth_devices.others;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;
import com.gcml.module_blutooth_devices.utils.ToastUtils;
import com.google.gson.Gson;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/11 15:51
 * created by:gzq
 * description:TODO
 */
public class BreathHome_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    /**
     * 历史记录
     */
    private TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    private TextView mBtnVideoDemo;
    /**
     * 0.00
     */
    private TextView mTvGaoya;
    /**
     * 0.00
     */
    private TextView mTvDiya;
    /**
     * 0.00
     */
    private TextView mTvMaibo;
    private SearchWithDeviceGroupHelper helper;
    private BaseBluetoothPresenter bluetoothPresenter;
    private Bundle bundle;
    /**
     * 0%
     */
    private TextView mTvGaoyaPercent;
    /**
     * 0%
     */
    private TextView mTvDiyaPercent;
    /**
     * 0%
     */
    private TextView mTvMaiboPercent;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_breath_home;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        this.bundle = bundle;
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = (TextView) view.findViewById(R.id.tv_gaoya);
        mTvDiya = (TextView) view.findViewById(R.id.tv_diya);
        mTvMaibo = (TextView) view.findViewById(R.id.tv_maibo);
        mTvGaoyaPercent = (TextView) view.findViewById(R.id.tv_gaoya_percent);
        mTvDiyaPercent = (TextView) view.findViewById(R.id.tv_diya_percent);
        mTvMaiboPercent = (TextView) view.findViewById(R.id.tv_maibo_percent);
        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvMaibo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvGaoyaPercent.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiyaPercent.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvMaiboPercent.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public void onResume() {
        super.onResume();
        dealLogic();
    }

    public void dealLogic() {
        String address = null;
        String brand = null;
        String sp_others = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BREATH_HOME, "");
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

    private void inputBluetoothNameDialog(final String address) {
//        new InputDialog(getContext()).builder()
//                .setMsg("请输入仪器后背唯一识别码\n" + "字母请大写")
//                .setMsgColor(R.color.config_color_base_3)
//                .setEditHint("请输入设备唯一识别码")
//                .setPositiveButton("连接", new InputDialog.OnInputChangeListener() {
//                    @Override
//                    public void onInput(String s) {
//                        chooseConnectType(address, s.trim());
//                    }
//                }).show();
    }

    private void chooseConnectType(String address, String brand) {
        if (TextUtils.isEmpty(address) && TextUtils.isEmpty(brand)) {
            inputBluetoothNameDialog(address);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            bluetoothPresenter = new BreathHome_PresenterImp(this,
                    new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, brand), 0, 25, 170, 65);
        } else {
            bluetoothPresenter = new BreathHome_PresenterImp(this,
                    new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, brand), 0, 25, 170, 65);
        }
    }

    @Override
    public void updateData(String... datas) {
        BreathHomeResultBean breathHomeResultBean = new Gson().fromJson(datas[0], BreathHomeResultBean.class);
        mTvGaoya.setText(breathHomeResultBean.getPef());
        mTvDiya.setText(breathHomeResultBean.getFev1());
        mTvMaibo.setText(breathHomeResultBean.getFvc());
        mTvGaoyaPercent.setText(breathHomeResultBean.getPercentPEF());
        mTvDiyaPercent.setText(breathHomeResultBean.getPercentFEV1());
        mTvMaiboPercent.setText(breathHomeResultBean.getPercentFEV1_FVC());
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
        return mContext;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            ToastUtils.showShort("暂不提供该数据");
        } else if (i == R.id.btn_video_demo) {
            ToastUtils.showShort("暂无演示视频");
        } else {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bluetoothPresenter != null) {
            bluetoothPresenter.onDestroy();
            bluetoothPresenter=null;
        }
        if (helper != null) {
            helper.destroy();
            helper=null;
        }
    }
}
