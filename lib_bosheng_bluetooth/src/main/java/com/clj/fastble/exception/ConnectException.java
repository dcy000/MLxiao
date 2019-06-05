// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConnectException.java

package com.clj.fastble.exception;

import android.bluetooth.BluetoothGatt;

// Referenced classes of package com.clj.fastble.exception:
//			BleException

public class ConnectException extends BleException
{

	private BluetoothGatt bluetoothGatt;
	private int gattStatus;

	public ConnectException(BluetoothGatt bluetoothGatt, int gattStatus)
	{
		super(101, "Gatt Exception Occurred! ");
		this.bluetoothGatt = bluetoothGatt;
		this.gattStatus = gattStatus;
	}

	public int getGattStatus()
	{
		return gattStatus;
	}

	public ConnectException setGattStatus(int gattStatus)
	{
		this.gattStatus = gattStatus;
		return this;
	}

	public BluetoothGatt getBluetoothGatt()
	{
		return bluetoothGatt;
	}

	public ConnectException setBluetoothGatt(BluetoothGatt bluetoothGatt)
	{
		this.bluetoothGatt = bluetoothGatt;
		return this;
	}

	public String toString()
	{
		return (new StringBuilder()).append("ConnectException{gattStatus=").append(gattStatus).append(", bluetoothGatt=").append(bluetoothGatt).append("} ").append(super.toString()).toString();
	}
}
