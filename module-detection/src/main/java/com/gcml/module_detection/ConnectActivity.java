package com.gcml.module_detection;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenPresenter;
import com.sjtu.yifei.annotation.Route;

import timber.log.Timber;

@Route(path = "/module/detection/connect/activity")
public class ConnectActivity extends ToolbarBaseActivity implements IBluetoothView, DialogControlBluetooth {


    private BaseBluetooth baseBluetooth;
    private BluetoothListDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect1);
        initView();
    }

    private void initView() {
        mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
        baseBluetooth = new BloodOxygenPresenter(this, false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new ConnectAnimFragment()).commit();
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

    }

    @Override
    public void discoveryStarted() {

    }

    @Override
    public void discoveryNewDevice(BluetoothDevice device) {
        if (dialog != null) {
            Timber.i(">>>>>>====" + device.getName() + "+++++" + device.getAddress());
            dialog.addNewDevice(device);
        }
    }

    @Override
    public void discoveryFinished() {
        if (dialog != null) {
            dialog.stopDevicesListAnim();
        }
    }

    @Override
    public void connectSuccess(BluetoothDevice device) {
        if (dialog != null) {
            dialog.showConnectedUI(device);
        }
    }


    @Override
    public void disConnected() {
        if (dialog != null) {
            dialog.hideConnectedUI();
        }
    }

    @Override
    public void connectFailed() {
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

}
