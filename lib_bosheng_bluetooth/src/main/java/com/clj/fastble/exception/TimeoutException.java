// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TimeoutException.java

package com.clj.fastble.exception;


// Referenced classes of package com.clj.fastble.exception:
//			BleException

public class TimeoutException extends BleException
{

	public TimeoutException()
	{
		super(100, "Timeout Exception Occurred!");
	}
}
