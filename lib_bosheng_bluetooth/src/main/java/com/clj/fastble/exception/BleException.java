// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleException.java

package com.clj.fastble.exception;

import java.io.Serializable;

public abstract class BleException
	implements Serializable
{

	private static final long serialVersionUID = 0x6f1564f58c14d61cL;
	public static final int ERROR_CODE_TIMEOUT = 100;
	public static final int ERROR_CODE_GATT = 101;
	public static final int ERROR_CODE_OTHER = 102;
	private int code;
	private String description;

	public BleException(int code, String description)
	{
		this.code = code;
		this.description = description;
	}

	public int getCode()
	{
		return code;
	}

	public BleException setCode(int code)
	{
		this.code = code;
		return this;
	}

	public String getDescription()
	{
		return description;
	}

	public BleException setDescription(String description)
	{
		this.description = description;
		return this;
	}

	public String toString()
	{
		return (new StringBuilder()).append("BleException { code=").append(code).append(", description='").append(description).append('\'').append('}').toString();
	}
}
