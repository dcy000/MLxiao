package com.gcml.module_detection;

import android.arch.lifecycle.Observer;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.bloodpressure.BloodPressurePresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/module/detection/connect/activity")
public class ConnectActivity extends ToolbarBaseActivity implements IBluetoothView {
    private Animation mRefreshAnim;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private BaseQuickAdapter<BluetoothDevice, BaseViewHolder> adapter;
    private BaseBluetooth baseBluetooth;
    private BaseNiceDialog dialog;
    private boolean isConnected = false;
    private BluetoothDevice connectedDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect1);
        initView();
    }

    private void initView() {
        mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
        mRefreshAnim = AnimationUtils.loadAnimation(this, R.anim.common_wifi_refresh);
        baseBluetooth = new BloodPressurePresenter(this);
    }

    @Override
    protected void backMainActivity() {
        showBluetoothListDialog();
    }

    private void showBluetoothListDialog() {
        if (dialog == null)
            dialog = NiceDialog.init()
                    .setLayoutId(R.layout.dialog_bluetooth_devices_list)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                            if (isConnected) {
                                holder.getView(R.id.cl_connected).setVisibility(View.VISIBLE);
                                if (connectedDevice != null) {
                                    holder.setText(R.id.tv_device_connected, connectedDevice.getName());
                                }
                            } else {
                                holder.getView(R.id.cl_connected).setVisibility(View.GONE);
                            }
                            holder.getView(R.id.iv_refresh_device_list).startAnimation(mRefreshAnim);
                            RecyclerView rvDevices = holder.getView(R.id.rv_devices);
                            rvDevices.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
                            rvDevices.setAdapter(adapter = new BaseQuickAdapter<BluetoothDevice, BaseViewHolder>(R.layout.layout_item_dialog_device, devices) {
                                @Override
                                protected void convert(BaseViewHolder helper, BluetoothDevice item) {
                                    String name = DeviceDealUtils.parseBluetoothName(item);
                                    helper.setText(R.id.item_device_name, name == null ? "NULL" : name);
                                }
                            });
                            BluetoothStore.instance.findNewDevice.observe(dialog, new Observer<BluetoothDevice>() {
                                @Override
                                public void onChanged(@Nullable BluetoothDevice device) {
                                    if (!devices.contains(device)) {
                                        devices.add(device);
                                        if (adapter != null) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });

                            if (adapter != null) {
                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                                    }
                                });
                            }

                        }
                    })
                    .setWidth(700)
                    .setHeight(450)
                    .show(getSupportFragmentManager());
        else {
            if (!dialog.isVisible()) {
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
    public void discoveryNewDevice(BluetoothDevice device) {

    }

    @Override
    public void discoveryFinished() {

    }
}
