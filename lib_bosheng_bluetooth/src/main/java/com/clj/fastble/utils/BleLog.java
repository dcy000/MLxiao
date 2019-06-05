// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleLog.java

package com.clj.fastble.utils;

import android.util.Log;

public final class BleLog
{

	public static boolean isPrint = true;
	private static String defaultTag = "FastBle";

	public BleLog()
	{
	}

	public static void d(String msg)
	{
		if (isPrint && msg != null)
			Log.d(defaultTag, msg);
	}

	public static void i(String msg)
	{
		if (isPrint && msg != null)
			Log.i(defaultTag, msg);
	}

	public static void w(String msg)
	{
		if (isPrint && msg != null)
			Log.w(defaultTag, msg);
	}

	public static void e(String msg)
	{
		if (isPrint && msg != null)
			Log.e(defaultTag, msg);
	}

}
