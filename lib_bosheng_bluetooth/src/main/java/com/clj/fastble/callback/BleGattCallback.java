// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleGattCallback.java

package com.clj.fastble.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

public abstract class BleGattCallback extends BluetoothGattCallback
{

	public BleGattCallback()
	{
	}

	public abstract void onStartConnect();

	public abstract void onConnectFail(BleDevice bledevice, BleException bleexception);

	public abstract void onConnectSuccess(BleDevice bledevice, BluetoothGatt bluetoothgatt, int i);

	public abstract void onDisConnected(boolean flag, BleDevice bledevice, BluetoothGatt bluetoothgatt, int i);
}
