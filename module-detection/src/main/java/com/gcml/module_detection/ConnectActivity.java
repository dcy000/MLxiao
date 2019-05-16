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
import com.gcml.module_blutooth_devices.bloodsugar.BloodSugarPresenter;
import com.gcml.module_blutooth_devices.ecg.ECGPresenter;
import com.gcml.module_blutooth_devices.temperature.TemperaturePresenter;
import com.gcml.module_blutooth_devices.three.ThreeInOnePresenter;
import com.gcml.module_blutooth_devices.weight.WeightPresenter;
import com.gcml.module_detection.fragment.BloodOxygenFragment;
import com.gcml.module_detection.fragment.BloodSugarFragment;
import com.gcml.module_detection.fragment.BloodpressureFragment;
import com.gcml.module_detection.fragment.BloodsugarSearchFragment;
import com.gcml.module_detection.fragment.CholesterolFragment;
import com.gcml.module_detection.fragment.ECGFragment;
import com.gcml.module_detection.fragment.SearchAnimFragment;
import com.gcml.module_detection.fragment.TemperatureFragment;
import com.gcml.module_detection.fragment.UricAcidFragment;
import com.gcml.module_detection.fragment.WeightFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/module/detection/connect/activity")
public class ConnectActivity extends ToolbarBaseActivity implements IBluetoothView, DialogControlBluetooth {

    private BaseBluetooth baseBluetooth;
    private BluetoothListDialog dialog;
    private int detectionType;
    private BluetoothBaseFragment baseFragment;
    private boolean isAfterPause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect1);

        initView();
        dealSearchFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAfterPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAfterPause = true;
    }

    private void dealSearchFragment() {
        detectionType = getIntent().getIntExtra("detectionType", 22);
        switch (detectionType) {
            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                //血压
                mTitleText.setText("血 压 测 量");
                baseBluetooth = new BloodPressurePresenter(this);
                initSearchFragment("将血压仪佩戴好后按下测量键", "测量的同时机器人会自动连接蓝牙", R.drawable.searching_bloodpressure);
                break;
            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                //血氧
                mTitleText.setText("血 氧 测 量");
                baseBluetooth = new BloodOxygenPresenter(this);
                initSearchFragment("将血氧仪夹在手指上", "将血氧仪器夹在手指上，机器人会自动连接蓝牙", R.drawable.searching_bloodoxygen);
                break;
            case IBleConstants.MEASURE_WEIGHT:
                //体重
                mTitleText.setText("体 重 测 量");
                baseBluetooth = new WeightPresenter(this);
                initSearchFragment("站在体重秤上", "站上体重秤后，机器人会自动连接蓝牙", R.drawable.searching_weight);
                break;
            case IBleConstants.MEASURE_TEMPERATURE:
                //耳温
                mTitleText.setText("耳 温 测 量");
                baseBluetooth = new TemperaturePresenter(this);
                initSearchFragment("按下耳温枪START键开机，连接蓝牙", "连接成功后，将探头尽可能的伸入耳道内，按下START扫描键开始测量", R.drawable.searching_temperature);
                break;
            case IBleConstants.MEASURE_ECG:
                //心电
                mTitleText.setText("心 电 测 量");
                baseBluetooth = new ECGPresenter(this);
                initSearchFragment("将心电仪夹在两只手指中间", "将心电仪夹在两只手指中间，机器人会自动连接蓝牙", R.drawable.searching_ecg_bosheng);
                break;
            case IBleConstants.MEASURE_CHOLESTEROL:
                //胆固醇
                mTitleText.setText("胆 固 醇 测 量");
                baseBluetooth = new ThreeInOnePresenter(this);
                initSearchFragment("给三合一插上检测试纸", "测上试纸后，机器人会自动连接蓝牙", R.drawable.searching_three);
                break;
            case IBleConstants.MEASURE_URIC_ACID:
                //血尿酸
                mTitleText.setText("胆 固 醇 测 量");
                baseBluetooth = new ThreeInOnePresenter(this);
                initSearchFragment("给三合一插上检测试纸", "测上试纸后，机器人会自动连接蓝牙", R.drawable.searching_three);
                break;
            case IBleConstants.MEASURE_BLOOD_SUGAR:
                //测量血糖
                mTitleText.setText("血 糖 测 量");
                baseBluetooth = new BloodSugarPresenter(this);
                initBloodsugarSearchFragment();
                break;
        }
    }

    private void initBloodsugarSearchFragment() {
        baseFragment = new BloodsugarSearchFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                .replace(R.id.fl_container, baseFragment).commitAllowingStateLoss();
    }

    private void dealMeasureFragment() {
        switch (detectionType) {
            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                //血压
                baseFragment = new BloodpressureFragment();
                break;
            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                //血氧
                baseFragment = new BloodOxygenFragment();
                break;
            case IBleConstants.MEASURE_WEIGHT:
                //体重
                baseFragment = new WeightFragment();
                break;
            case IBleConstants.MEASURE_TEMPERATURE:
                //耳温
                baseFragment = new TemperatureFragment();
                break;
            case IBleConstants.MEASURE_ECG:
                //心电
                baseFragment = new ECGFragment();
                break;
            case IBleConstants.MEASURE_CHOLESTEROL:
                //胆固醇
                baseFragment = new CholesterolFragment();
                break;
            case IBleConstants.MEASURE_URIC_ACID:
                //血尿酸
                baseFragment = new UricAcidFragment();
                break;
            case IBleConstants.MEASURE_BLOOD_SUGAR:
                //血糖
                baseFragment = new BloodSugarFragment();
                break;
        }
        if (baseFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                    .addToBackStack(null)
                    .replace(R.id.fl_container, baseFragment).commitAllowingStateLoss();
        }
    }

    private void initSearchFragment(String mainTitle, String subTitle, int imgRes) {

        Bundle bundle = new Bundle();
        bundle.putString("mainTitle", mainTitle);
        bundle.putString("subTitle", subTitle);
        bundle.putInt("imgRes", imgRes);
        baseFragment = new SearchAnimFragment();
        baseFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                .replace(R.id.fl_container, baseFragment).commitAllowingStateLoss();
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

    }

    @Override
    public void unFindTargetDevice() {
        showUnSearchedDeviceDialog();
    }

    @Override
    public void connectSuccess(BluetoothDevice device, String bluetoothName) {
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
        popSearchFragment();
        if (dialog != null) {
            dialog.hideConnectedUI();
        }
    }

    private void popSearchFragment() {
        if (baseFragment instanceof BloodsugarSearchFragment) return;
        if (!(baseFragment instanceof SearchAnimFragment) && !isAfterPause) {
            getSupportFragmentManager().popBackStack();
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

    public BaseBluetooth getPresenter() {
        return baseBluetooth;
    }
}
