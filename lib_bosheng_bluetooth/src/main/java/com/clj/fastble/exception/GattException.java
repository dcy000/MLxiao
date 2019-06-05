// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GattException.java

package com.clj.fastble.exception;


// Referenced classes of package com.clj.fastble.exception:
//			BleException

public class GattException extends BleException
{

	private int gattStatus;

	public GattException(int gattStatus)
	{
		super(101, "Gatt Exception Occurred! ");
		this.gattStatus = gattStatus;
	}

	public int getGattStatus()
	{
		return gattStatus;
	}

	public GattException setGattStatus(int gattStatus)
	{
		this.gattStatus = gattStatus;
		return this;
	}

	public String toString()
	{
		return (new StringBuilder()).append("GattException{gattStatus=").append(gattStatus).append("} ").append(super.toString()).toString();
	}
}
