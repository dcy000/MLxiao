// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleRssiCallback.java

package com.clj.fastble.callback;

import com.clj.fastble.exception.BleException;

// Referenced classes of package com.clj.fastble.callback:
//			BleBaseCallback

public abstract class BleRssiCallback extends BleBaseCallback
{

	public BleRssiCallback()
	{
	}

	public abstract void onRssiFailure(BleException bleexception);

	public abstract void onRssiSuccess(int i);
}
