package com.gcml.module_detection;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;

public class BluetoothListDialog extends BaseNiceDialog {
    private List<BluetoothDevice> devices = new ArrayList<>();
    private Animation mRefreshAnim;
    private Animation mDevicesListRefreshAnim;
    private BaseQuickAdapter<BluetoothDevice, BaseViewHolder> adapter;
    private ImageView mIvDevicesList;
    private LinkedHashSet<BluetoothDevice> deviceContainer = new LinkedHashSet<>(5);
    private ConstraintLayout mClConnectedUi;
    private TextView mConnectDeviceName;
    private ImageView mBtnSearchBluetooth;

    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_bluetooth_devices_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefreshAnim = AnimationUtils.loadAnimation(getContext(), R.anim.common_wifi_refresh);
        mDevicesListRefreshAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bluetooth_list_refresh);

        setWidth(700);
        setHeight(450);
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        mClConnectedUi = holder.getView(R.id.cl_connected);
        mConnectDeviceName = holder.getView(R.id.tv_device_connected);
        mIvDevicesList = holder.getView(R.id.iv_refresh_device_list);
        setAdapter(holder);

        //重新搜索蓝牙
        mBtnSearchBluetooth = holder.getView(R.id.iv_top_right_refresh);
        mBtnSearchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先把原来的列表清空 因为此时列表中的设备可能已经关闭，再次点击连接，则连接不成功
                deviceContainer.clear();
                devices.clear();
                if (adapter != null) adapter.notifyDataSetChanged();
                deviceContainer.clear();
                mBtnSearchBluetooth.startAnimation(mDevicesListRefreshAnim);
                BluetoothUtils.closeBluetooth();
                startDevicesListAnim();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //当Dialog显示的时候
        if (connected) {
            ToastUtils.showShort("设备已连接， 重新连接请点击刷新！");
            return;
        }
        startDevicesListAnim();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //当Dialog消失的时候
        stopDevicesListAnim();
        deviceContainer.clear();
        if (controlBluetooth != null) {
            controlBluetooth.dialogDismissed();
        }
    }

    private void setAdapter(ViewHolder holder) {
        RecyclerView rvDevices = holder.getView(R.id.rv_devices);
        rvDevices.setNestedScrollingEnabled(false);
        rvDevices.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setSmoothScrollbarEnabled(true);
        rvDevices.setLayoutManager(layout);
        rvDevices.setAdapter(adapter = new BaseQuickAdapter<BluetoothDevice, BaseViewHolder>(R.layout.layout_item_dialog_device, devices) {
            @Override
            protected void convert(BaseViewHolder helper, BluetoothDevice item) {
                String name = DeviceDealUtils.parseBluetoothName(item);
                helper.setText(R.id.item_device_name, name == null ? "NULL" : name);
            }
        });

        if (adapter != null) {
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    //尝试连接前判断点击的是否是我们自己的设备
                    BluetoothDevice device = devices.get(position);
                    boolean selfDevice = DeviceDealUtils.isSelfDevice(device);
                    if (selfDevice) {
                        if (controlBluetooth != null) {
                            controlBluetooth.connect(device);
                            dismiss();
                        }
                    } else {
                        ToastUtils.showShort("不支持非本公司设备连接");
                    }
                }
            });
        }
    }

    private Disposable countDownDisposable = Disposables.empty();

    public void startDevicesListAnim() {
        if (controlBluetooth != null) {
            controlBluetooth.search();
        }
        if (mIvDevicesList != null) {
            mIvDevicesList.startAnimation(mRefreshAnim);
        }
        //同时启动循环器
        countDownDisposable = RxUtils.rxCountDown(500, 4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        //如果没有新的设备进来，则不刷新列表，解决滑动列表卡顿问题
                        if (devices.size() < deviceContainer.size()) {
                            devices.clear();
                            devices.addAll(deviceContainer);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


    public void stopDevicesListAnim() {
        countDownDisposable.dispose();
        if (mIvDevicesList != null) {
            mIvDevicesList.clearAnimation();
        }
        if (mBtnSearchBluetooth != null) {
            mBtnSearchBluetooth.clearAnimation();
        }
    }

    public void addNewDevice(BluetoothDevice device) {
        //将没有蓝牙名字的设备过滤掉
        if (!TextUtils.isEmpty(device.getName())) {
            deviceContainer.add(device);
        }
    }

    public void showConnectedUI(BluetoothDevice device) {
        if (mClConnectedUi != null) {
            mClConnectedUi.setVisibility(View.VISIBLE);
            if (mConnectDeviceName != null) {
                String name = DeviceDealUtils.parseBluetoothName(device);
                mConnectDeviceName.setText(name == null ? "未知" : name);
            }
        }
    }

    public void hideConnectedUI() {
        if (mClConnectedUi != null) {
            mClConnectedUi.setVisibility(View.GONE);
        }
    }

    private DialogControlBluetooth controlBluetooth;

    public void setControlBluetoothListener(DialogControlBluetooth controlBluetooth) {
        this.controlBluetooth = controlBluetooth;
    }
}
