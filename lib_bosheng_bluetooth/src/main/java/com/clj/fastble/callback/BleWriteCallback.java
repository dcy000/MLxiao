// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleWriteCallback.java

package com.clj.fastble.callback;

import com.clj.fastble.exception.BleException;

// Referenced classes of package com.clj.fastble.callback:
//			BleBaseCallback

public abstract class BleWriteCallback extends BleBaseCallback
{

	public BleWriteCallback()
	{
	}

	public abstract void onWriteSuccess(int i, int j, byte abyte0[]);

	public abstract void onWriteFailure(BleException bleexception);
}
