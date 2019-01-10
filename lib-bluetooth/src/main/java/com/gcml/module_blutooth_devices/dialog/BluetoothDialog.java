package com.gcml.module_blutooth_devices.dialog;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.BindDeviceBean;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.DeviceBrand;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BluetoothDialog extends AlertDialog implements LifecycleObserver, View.OnClickListener {
    private LifecycleOwner owner;
    private Context context;
    private TextView bindName;
    private TextView bindMac;
    private TextView bindBrand;
    private RecyclerView bluetoothRV;
    private Button btnCancel;
    private Button connectAuto;
    private List<BluetoothDevice> devices;
    private HashMap<BluetoothDevice, MyBluetoothRssi> rssis;
    private BaseQuickAdapter<BluetoothDevice, BaseViewHolder> adapter;
    private HashMap<String, String> brands;
    private HashMap<String, String> bindNameAddress = new HashMap<>();
    private int deviceType;

    private String[] bloodoxygenSplit;
    private String[] bloodpressureSplit;
    private String[] bloodsugarSplit;
    private String[] ecgSplit;
    private String[] temperatureSplit;
    private String[] weightSplit;
    private String[] threeSplit;
    private ChooseBluetoothDevice chooseBluetoothDevice;

    {
        String sp_bloodoxygen = (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_BLOODOXYGEN, "");
        if (!TextUtils.isEmpty(sp_bloodoxygen)) {
            bloodoxygenSplit = sp_bloodoxygen.split(",");
            if (bloodoxygenSplit.length == 2) {
                bindNameAddress.put(bloodoxygenSplit[0], bloodoxygenSplit[1]);
            }
        }

        String sp_bloodpressure = (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_BLOODPRESSURE, "");
        if (!TextUtils.isEmpty(sp_bloodpressure)) {
            bloodpressureSplit = sp_bloodpressure.split(",");
            if (bloodpressureSplit.length == 2) {
                bindNameAddress.put(bloodpressureSplit[0], bloodpressureSplit[1]);
            }
        }

        String sp_bloodsugar = (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_BLOODSUGAR, "");
        if (!TextUtils.isEmpty(sp_bloodsugar)) {
            bloodsugarSplit = sp_bloodsugar.split(",");
            if (bloodsugarSplit.length == 2) {
                bindNameAddress.put(bloodsugarSplit[0], bloodsugarSplit[1]);
            }
        }
        String sp_ecg = (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_ECG, "");
        if (!TextUtils.isEmpty(sp_ecg)) {
            ecgSplit = sp_ecg.split(",");
            if (ecgSplit.length == 2) {
                bindNameAddress.put(ecgSplit[0], ecgSplit[1]);
            }
        }
        String sp_temperature = (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_TEMPERATURE, "");
        if (!TextUtils.isEmpty(sp_temperature)) {
            temperatureSplit = sp_temperature.split(",");
            if (temperatureSplit.length == 2) {
                bindNameAddress.put(temperatureSplit[0], temperatureSplit[1]);
            }
        }
        String sp_weight = (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_WEIGHT, "");
        if (!TextUtils.isEmpty(sp_weight)) {
            weightSplit = sp_weight.split(",");
            if (weightSplit.length == 2) {
                bindNameAddress.put(weightSplit[0], weightSplit[1]);
            }
        }

        String sp_three = (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_THREE_IN_ONE, "");
        if (!TextUtils.isEmpty(sp_three)) {
            threeSplit = sp_three.split(",");
            if (threeSplit.length == 2) {
                bindNameAddress.put(threeSplit[0], threeSplit[1]);
            }
        }
    }

    public void setChooseBluetoothDeviceListener(ChooseBluetoothDevice deviceListener) {
        this.chooseBluetoothDevice = deviceListener;
    }

    protected BluetoothDialog(@NonNull Context context) {
        super(context);
        this.context = context;


    }

    public BluetoothDialog(@NonNull Context context, LifecycleOwner owner, int deviceType) {
        this(context);
        this.owner = owner;
        this.deviceType = deviceType;
        if (owner != null) {
            owner.getLifecycle().addObserver(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bluetooth);
        initView();
        initRV();
        dealBindDevice();
    }


    private void initView() {
        bindName = findViewById(R.id.bind_name);
        bindMac = findViewById(R.id.bind_mac);
        bindBrand = findViewById(R.id.bind_brand);
        bluetoothRV = findViewById(R.id.bluetooth_rv);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        connectAuto = findViewById(R.id.connect_auto);
        connectAuto.setOnClickListener(this);

    }

    private void initRV() {
        bluetoothRV.setLayoutManager(new LinearLayoutManager(context));
        bluetoothRV.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        bluetoothRV.setAdapter(adapter = new BaseQuickAdapter<BluetoothDevice, BaseViewHolder>(R.layout.item_bluetooth, devices) {
            @Override
            protected void convert(BaseViewHolder helper, BluetoothDevice item) {
                MyBluetoothRssi bluetoothRssi = rssis.get(item);
                if (bluetoothRssi == null) {
                    bluetoothRssi = new MyBluetoothRssi(helper);
                }
                BluetoothStore.getClient().readRssi(item.getAddress(), bluetoothRssi);
                helper.setText(R.id.bind_name, TextUtils.isEmpty(item.getName()) ? "None" : item.getName());
                helper.setText(R.id.bind_mac, item.getAddress());
                if (brands.keySet().contains(item.getName())) {
                    helper.setText(R.id.bind_brand, brands.get(item.getName()));
                } else {
                    helper.setText(R.id.bind_brand, "");
                }
                if (bindNameAddress.containsValue(item.getAddress())) {
                    helper.setText(R.id.isBind, "已绑定");
                } else {
                    helper.setText(R.id.isBind, "未绑定");
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (chooseBluetoothDevice != null) {
                    chooseBluetoothDevice.choosed(devices.get(position));
                }
            }
        });
    }

    private Observer<BindDeviceBean> bindDeviceBean = new Observer<BindDeviceBean>() {
        @Override
        public void onChanged(@Nullable BindDeviceBean bindDeviceBean) {
            bindName.setText(bindDeviceBean.getBluetoothName());
            bindMac.setText(bindDeviceBean.getBluetoothMac());
            bindBrand.setText("(" + bindDeviceBean.getBluetoothBrand() + ")");
        }
    };

    private void dealBindDevice() {
        BluetoothStore.bindDevice.observe(owner, bindDeviceBean);

        switch (deviceType) {
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                if (bloodpressureSplit == null) {
                    bindName.setText("暂无绑定设备");
                    return;
                }
                if (bloodpressureSplit.length == 2) {
                    bindName.setText(bloodpressureSplit[0]);
                    bindMac.setText(bloodpressureSplit[1]);
                    bindBrand.setText("(" + DeviceBrand.BLOODPRESSURE.get(bloodpressureSplit[0]) + ")");
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                if (bloodoxygenSplit == null) {
                    bindName.setText("暂无绑定设备");
                    return;
                }
                if (bloodoxygenSplit.length == 2) {
                    bindName.setText(bloodoxygenSplit[0]);
                    bindMac.setText(bloodoxygenSplit[1]);
                    bindBrand.setText("(" + DeviceBrand.BLOODOXYGEN.get(bloodoxygenSplit[0]) + ")");
                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                if (bloodsugarSplit == null) {
                    bindName.setText("暂无绑定设备");
                    return;
                }
                if (bloodsugarSplit.length == 2) {
                    bindName.setText(bloodsugarSplit[0]);
                    bindMac.setText(bloodsugarSplit[1]);
                    bindBrand.setText("(" + DeviceBrand.BLOODSUGAR.get(bloodsugarSplit[0]) + ")");
                }
                break;
            case IPresenter.MEASURE_ECG:
                if (ecgSplit == null) {
                    bindName.setText("暂无绑定设备");
                    return;
                }
                if (ecgSplit.length == 2) {
                    bindName.setText(ecgSplit[0]);
                    bindMac.setText(ecgSplit[1]);
                    bindBrand.setText("(" + DeviceBrand.ECG.get(ecgSplit[0]) + ")");
                }
                break;
            case IPresenter.MEASURE_TEMPERATURE:
                if (temperatureSplit == null) {
                    bindName.setText("暂无绑定设备");
                    return;
                }
                if (temperatureSplit.length == 2) {
                    bindName.setText(temperatureSplit[0]);
                    bindMac.setText(temperatureSplit[1]);
                    bindBrand.setText("(" + DeviceBrand.ECG.get(temperatureSplit[0]) + ")");
                }
                break;
            case IPresenter.MEASURE_WEIGHT:
                if (weightSplit == null) {
                    bindName.setText("暂无绑定设备");
                    return;
                }
                if (weightSplit.length == 2) {
                    bindName.setText(weightSplit[0]);
                    bindMac.setText(weightSplit[1]);
                    bindBrand.setText("(" + DeviceBrand.ECG.get(weightSplit[0]) + ")");
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            dismiss();
            if (chooseBluetoothDevice != null) {
                chooseBluetoothDevice.cancelSearch();
            }
        } else if (i == R.id.connect_auto) {
            dismiss();
            if (chooseBluetoothDevice != null) {
                chooseBluetoothDevice.autoConnect();
            }
        }
    }

    class MyBluetoothRssi implements BleReadRssiResponse {
        private BaseViewHolder helper;

        public MyBluetoothRssi(BaseViewHolder helper) {
            this.helper = helper;
        }

        @Override
        public void onResponse(int code, Integer data) {
            helper.setText(R.id.signal, data.toString());
        }
    }

    public synchronized void addDevice(BluetoothDevice device) {
        if (devices != null && !devices.contains(device)) {
            devices.add(device);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        if (devices == null) {
            devices = new ArrayList<>();
        }
        if (rssis == null) {
            rssis = new HashMap<>();
        }
        if (brands == null) {
            brands = new HashMap<>();
        }
        brands.clear();
        brands.putAll(DeviceBrand.BLOODOXYGEN);
        brands.putAll(DeviceBrand.BLOODPRESSURE);
        brands.putAll(DeviceBrand.BLOODSUGAR);
        brands.putAll(DeviceBrand.ECG);
        brands.putAll(DeviceBrand.TEMPERATURE);
        brands.putAll(DeviceBrand.WEIGHT);

        super.show();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        dismiss();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (rssis != null) {
            rssis.clear();
        }
        rssis = null;
        if (devices != null) {
            devices.clear();
        }
        devices = null;
        if (brands != null) {
            brands.clear();
        }
        brands = null;
        if (bindNameAddress != null) {
            bindNameAddress.clear();
        }
        bindNameAddress = null;
        if (owner != null) {
            BluetoothStore.bindDevice.removeObservers(owner);
            owner.getLifecycle().removeObserver(this);
        }
        owner = null;
    }
}
