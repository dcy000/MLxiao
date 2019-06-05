// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleScanState.java

package com.clj.fastble.data;


public final class BleScanState extends Enum
{

	public static final BleScanState STATE_IDLE;
	public static final BleScanState STATE_SCANNING;
	private int code;
	private static final BleScanState $VALUES[];

	public static BleScanState[] values()
	{
		return (BleScanState[])$VALUES.clone();
	}

	public static BleScanState valueOf(String name)
	{
		return (BleScanState)Enum.valueOf(com/clj/fastble/data/BleScanState, name);
	}

	private BleScanState(String s, int i, int code)
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
		STATE_IDLE = new BleScanState("STATE_IDLE", 0, -1);
		STATE_SCANNING = new BleScanState("STATE_SCANNING", 1, 1);
		$VALUES = (new BleScanState[] {
			STATE_IDLE, STATE_SCANNING
		});
	}
}
