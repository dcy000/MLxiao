package com.gcml.module_detection;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenPresenter;
import com.gcml.module_blutooth_devices.bloodpressure.BloodPressurePresenter;
import com.gcml.module_detection.fragment.BloodpressureFragment;
import com.gcml.module_detection.fragment.SearchAnimFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

import timber.log.Timber;

@Route(path = "/module/detection/connect/activity")
public class ConnectActivity extends ToolbarBaseActivity implements IBluetoothView, DialogControlBluetooth {


    private BaseBluetooth baseBluetooth;
    private BluetoothListDialog dialog;
    private int detectionType;
    private BluetoothBaseFragment baseFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect1);

        initView();
        dealSearchFragment();
    }

    private void dealSearchFragment() {
        detectionType = getIntent().getIntExtra("detectionType", 22);

        switch (detectionType) {
            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                //血压
                mTitleText.setText("血 压 测 量");
                baseBluetooth = new BloodPressurePresenter(this);
                initFragment("将血压仪佩戴好后按下测量键", "测量的同时机器人会自动连接蓝牙", R.drawable.searching_bloodpressure);
                break;
            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                //血氧
                mTitleText.setText("血 氧 测 量");
                baseBluetooth = new BloodOxygenPresenter(this);
                initFragment("将血氧仪夹在手指上", "将血氧仪器夹在手指上，机器人会自动连接蓝牙", R.drawable.searching_bloodoxygen);
                break;
        }
    }

    private void dealMeasureFragment() {
        switch (detectionType) {
            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                //血压
                baseFragment = new BloodpressureFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                        .addToBackStack(null)
                        .replace(R.id.fl_container, baseFragment).commit();
                break;
            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                //血氧
                baseBluetooth = new BloodOxygenPresenter(this);
                break;
        }
    }

    private void initFragment(String mainTitle, String subTitle, int imgRes) {
        if (baseFragment instanceof BloodpressureFragment) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (baseFragment instanceof SearchAnimFragment) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("mainTitle", mainTitle);
        bundle.putString("subTitle", subTitle);
        bundle.putInt("imgRes", imgRes);
        baseFragment = new SearchAnimFragment();
        baseFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                .replace(R.id.fl_container, baseFragment).commit();
    }

    private void initView() {
        mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
    }

    @Override
    protected void backMainActivity() {
        showBluetoothListDialog();
    }

    private void showBluetoothListDialog() {
        if (dialog == null) {
            dialog = new BluetoothListDialog();
            dialog.setControlBluetoothListener(this);
            dialog.show(getSupportFragmentManager());
        } else {
            if (!dialog.isShow()) {
                dialog.show(getSupportFragmentManager());
            }
        }
    }

    @Override
    public void updateData(DetectionData detectionData) {

    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), state);
    }

    @Override
    public void discoveryStarted() {

    }

    @Override
    public void discoveryNewDevice(BluetoothDevice device) {
        if (dialog != null) {
            dialog.addNewDevice(device);
        }
    }

    @Override
    public void discoveryFinished(boolean isConnected) {
        if (dialog != null) {
            dialog.stopDevicesListAnim();
        }
        if (!isConnected) {
            showUnSearchedDeviceDialog();
        }
    }

    @Override
    public void connectSuccess(BluetoothDevice device) {
        mRightView.setImageResource(R.drawable.ic_bluetooth_connected);
        dealMeasureFragment();
        if (dialog != null) {
            dialog.showConnectedUI(device);
        }
    }


    @Override
    public void disConnected() {
        mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
        //同时回到尝试搜索的页面
        dealSearchFragment();
        if (dialog != null) {
            dialog.hideConnectedUI();
        }
    }

    @Override
    public void connectFailed() {
        mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
        //连接失败后，提示他主动连接
        connectedFailedTips();
        if (dialog != null) {
            dialog.hideConnectedUI();
        }
    }

    @Override
    public void search() {
        if (baseBluetooth != null) {
            baseBluetooth.startDiscovery(null);
        }
    }

    @Override
    public void connect(BluetoothDevice device) {
        if (baseBluetooth != null) {
            baseBluetooth.startConnect(device);
        }
    }

    @Override
    public void dialogDismissed() {
        if (baseBluetooth != null) {
            baseBluetooth.stopDiscovery();
        }
    }

    private void showUnSearchedDeviceDialog() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_bluetooth_unsearched)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        switch (detectionType) {
                            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开血压计开关");
                                break;
                            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开血氧仪开关");
                                break;
                            case IBleConstants.MEASURE_BLOOD_SUGAR:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开血糖仪开关");
                                break;
                            default:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开设备开关");
                                break;
                        }
                        holder.getView(R.id.btn_click).setOnClickListener(v -> {
                            dialog.dismiss();
                            //点击确定按钮后，再次进行搜索
                            search();
                        });

                    }
                })
                .setWidth(600)
                .setHeight(330)
                .show(getSupportFragmentManager());
    }

    private void connectedFailedTips() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_bluetooth_unsearched)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_msg, "连接蓝牙失败！\n请点击右上角蓝牙图标，尝试手动连接");
                        holder.getView(R.id.btn_click).setOnClickListener(v -> {
                            dialog.dismiss();
                        });

                    }
                })
                .setWidth(600)
                .setHeight(330)
                .show(getSupportFragmentManager());
    }
}
