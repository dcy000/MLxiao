// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleNotifyCallback.java

package com.clj.fastble.callback;

import com.clj.fastble.exception.BleException;

// Referenced classes of package com.clj.fastble.callback:
//			BleBaseCallback

public abstract class BleNotifyCallback extends BleBaseCallback
{

	public BleNotifyCallback()
	{
	}

	public abstract void onNotifySuccess();

	public abstract void onNotifyFailure(BleException bleexception);

	public abstract void onCharacteristicChanged(byte abyte0[]);
}
