// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleDevice.java

package com.clj.fastble.data;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class BleDevice
	implements Parcelable
{

	private BluetoothDevice mDevice;
	private byte mScanRecord[];
	private int mRssi;
	private long mTimestampNanos;
	public static final Creator CREATOR = new Creator() {

		public BleDevice createFromParcel(Parcel in)
		{
			return new BleDevice(in);
		}

		public BleDevice[] newArray(int size)
		{
			return new BleDevice[size];
		}

		public volatile Object[] newArray(int i)
		{
			return newArray(i);
		}

		public volatile Object createFromParcel(Parcel parcel)
		{
			return createFromParcel(parcel);
		}

	};

	public BleDevice(BluetoothDevice device)
	{
		mDevice = device;
	}

	public BleDevice(BluetoothDevice device, int rssi, byte scanRecord[], long timestampNanos)
	{
		mDevice = device;
		mScanRecord = scanRecord;
		mRssi = rssi;
		mTimestampNanos = timestampNanos;
	}

	protected BleDevice(Parcel in)
	{
		mDevice = (BluetoothDevice)in.readParcelable(android/bluetooth/BluetoothDevice.getClassLoader());
		mScanRecord = in.createByteArray();
		mRssi = in.readInt();
		mTimestampNanos = in.readLong();
	}

	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeParcelable(mDevice, flags);
		dest.writeByteArray(mScanRecord);
		dest.writeInt(mRssi);
		dest.writeLong(mTimestampNanos);
	}

	public int describeContents()
	{
		return 0;
	}

	public String getName()
	{
		if (mDevice != null)
			return mDevice.getName();
		else
			return null;
	}

	public String getMac()
	{
		if (mDevice != null)
			return mDevice.getAddress();
		else
			return null;
	}

	public String getKey()
	{
		if (mDevice != null)
			return (new StringBuilder()).append(mDevice.getName()).append(mDevice.getAddress()).toString();
		else
			return "";
	}

	public BluetoothDevice getDevice()
	{
		return mDevice;
	}

	public void setDevice(BluetoothDevice device)
	{
		mDevice = device;
	}

	public byte[] getScanRecord()
	{
		return mScanRecord;
	}

	public void setScanRecord(byte scanRecord[])
	{
		mScanRecord = scanRecord;
	}

	public int getRssi()
	{
		return mRssi;
	}

	public void setRssi(int rssi)
	{
		mRssi = rssi;
	}

	public long getTimestampNanos()
	{
		return mTimestampNanos;
	}

	public void setTimestampNanos(long timestampNanos)
	{
		mTimestampNanos = timestampNanos;
	}

}
