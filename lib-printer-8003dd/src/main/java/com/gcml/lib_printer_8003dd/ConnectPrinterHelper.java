package com.gcml.lib_printer_8003dd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.widget.Toast;

import com.gcml.lib_printer_8003dd.base.BluetoothC;
import com.gcml.lib_printer_8003dd.base.BluetoothSearchHelper;
import com.gcml.lib_printer_8003dd.base.SearchListener;
import com.gcml.lib_printer_8003dd.command.Command;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.io.UnsupportedEncodingException;

import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class ConnectPrinterHelper implements LifecycleObserver {

    private SupportActivity activity;
    private BluetoothSearchHelper searchHelper;
    private MySearchListener searchListener;
    private BluetoothService bluetoothService;
    private MyHandler handler;
    private IPrinterView view;
    private boolean isConnected=false;
    public boolean isConnected(){
        return isConnected;
    }
    @SuppressLint("RestrictedApi")
    public ConnectPrinterHelper(LifecycleOwner owner) {
        if (owner instanceof IPrinterView) {
            view = (IPrinterView) owner;
        }
        if (owner instanceof SupportActivity) {
            activity = (SupportActivity) owner;
        } else if (owner instanceof Fragment) {
            activity = ((Fragment) owner).getActivity();
        }
        BluetoothC.init(activity.getApplication());

        activity.getLifecycle().addObserver(this);
    }

    public void start() {
        if (activity == null) {
            throw new IllegalArgumentException("activity==null");
        }
        RxPermissions rxPermissions = new RxPermissions(activity);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            rxPermissions
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .observeOn(Schedulers.newThread())
                    .as(AutoDispose.<Boolean>autoDisposable(AndroidLifecycleScopeProvider.from(activity, Lifecycle.Event.ON_STOP)))
                    .subscribe(new DefaultObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                doAccept();
                            } else {
                                doRefuse();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            doAccept();
        }
    }

    private void doRefuse() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "操作蓝牙需要打开蓝牙权限", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkBluetoothState() {
        if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            return false;
        }
        return true;
    }

    @WorkerThread
    private void doAccept() {
        if (searchListener == null) {
            searchListener = new MySearchListener();
        }
        if (searchHelper == null) {
            searchHelper = new BluetoothSearchHelper();
        }
        searchHelper.searchClassic(5000, 5, searchListener, "Printer");

        if (handler == null) {
            handler = new MyHandler();
        }
        if (bluetoothService == null) {
            bluetoothService = new BluetoothService(activity, handler);
        }
    }

    class MySearchListener implements SearchListener {

        @Override
        public void onSearching(boolean isOn) {

        }

        @Override
        public void onNewDeviceFinded(BluetoothDevice newDevice) {

        }

        @Override
        public void obtainDevice(BluetoothDevice device) {
            if (searchHelper != null) {
                searchHelper.clear();
            }
            if (bluetoothService != null) {
                bluetoothService.connect(device);
            }
        }

        @Override
        public void noneFind() {

        }
    }
    public void initPrinter(){
        SendDataByte(Command.ESC_Init);
        SendDataByte(Command.LF);
    }
    public void printTitle(String title) throws UnsupportedEncodingException {
        Command.ESC_Align[2] = 0x01;
        SendDataByte(Command.ESC_Align);
        Command.GS_ExclamationMark[2] = 0x11;
        SendDataByte(Command.GS_ExclamationMark);
        SendDataByte(title.getBytes("GBK"));
    }

    public void printContent(String content) throws UnsupportedEncodingException {
        Command.ESC_Align[2] = 0x00;
        SendDataByte(Command.ESC_Align);
        Command.GS_ExclamationMark[2] = 0x00;
        SendDataByte(Command.GS_ExclamationMark);
        SendDataByte(content.getBytes("GBK"));
    }
    public void printBottom() throws UnsupportedEncodingException {
        SendDataByte("\n\n\n\n\n\n".getBytes("GBK"));
    }

    private static final String TAG = "ConnectPrinterHelper";
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            isConnected=true;
                            if (view != null) {
                                view.updateState("连接打印机成功");
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_WRITE:

                    break;
                case BluetoothService.MESSAGE_READ:

                    break;
                case BluetoothService.MESSAGE_DEVICE_NAME:
                    break;
                case BluetoothService.MESSAGE_TOAST:
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    isConnected=false;
                    if (view != null) {
                        view.updateState("打印机蓝牙已断开");
                    }
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    if (activity != null) {
                        Toast.makeText(activity, "Unable to connect device", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void SendDataByte(byte[] data) {
        if (bluetoothService==null){
            view.updateState("正在启动蓝牙服务");
            return;
        }
        if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            view.updateState("请先连接打印机！！！");
            return;
        }
        bluetoothService.write(data);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (bluetoothService != null) {
            if (bluetoothService.getState() == BluetoothService.STATE_NONE) {
                bluetoothService.start();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        isConnected=false;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (bluetoothService != null) {
            bluetoothService.stop();
        }
        if (searchHelper != null) {
            searchHelper.clear();
        }
        handler = null;
        bluetoothService = null;
        searchListener = null;
        searchHelper = null;
        activity = null;
    }
}
