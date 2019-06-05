// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleConnectState.java

package com.clj.fastble.data;


public final class BleConnectState extends Enum
{

	public static final BleConnectState CONNECT_IDLE;
	public static final BleConnectState CONNECT_CONNECTING;
	public static final BleConnectState CONNECT_CONNECTED;
	public static final BleConnectState CONNECT_FAILURE;
	public static final BleConnectState CONNECT_DISCONNECT;
	private int code;
	private static final BleConnectState $VALUES[];

	public static BleConnectState[] values()
	{
		return (BleConnectState[])$VALUES.clone();
	}

	public static BleConnectState valueOf(String name)
	{
		return (BleConnectState)Enum.valueOf(com/clj/fastble/data/BleConnectState, name);
	}

	private BleConnectState(String s, int i, int code)
	{
		super(s, i);
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

	static 
	{
		CONNECT_IDLE = new BleConnectState("CONNECT_IDLE", 0, 0);
		CONNECT_CONNECTING = new BleConnectState("CONNECT_CONNECTING", 1, 1);
		CONNECT_CONNECTED = new BleConnectState("CONNECT_CONNECTED", 2, 2);
		CONNECT_FAILURE = new BleConnectState("CONNECT_FAILURE", 3, 3);
		CONNECT_DISCONNECT = new BleConnectState("CONNECT_DISCONNECT", 4, 5);
		$VALUES = (new BleConnectState[] {
			CONNECT_IDLE, CONNECT_CONNECTING, CONNECT_CONNECTED, CONNECT_FAILURE, CONNECT_DISCONNECT
		});
	}
}
