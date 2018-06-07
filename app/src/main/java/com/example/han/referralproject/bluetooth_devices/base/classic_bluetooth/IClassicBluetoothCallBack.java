package com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth;

import java.io.InputStream;
import java.io.OutputStream;

public interface IClassicBluetoothCallBack {
	void call(int state);
	void writeData(int result, byte[] buffer, OutputStream outputStream);
	void readData(InputStream inputStream);
}
