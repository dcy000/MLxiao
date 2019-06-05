// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleConnectStateParameter.java

package com.clj.fastble.data;


public class BleConnectStateParameter
{

	private int status;
	private boolean isActive;

	public BleConnectStateParameter(int status)
	{
		this.status = status;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean active)
	{
		isActive = active;
	}
}
