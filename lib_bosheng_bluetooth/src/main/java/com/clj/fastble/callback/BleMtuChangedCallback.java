// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleMtuChangedCallback.java

package com.clj.fastble.callback;

import com.clj.fastble.exception.BleException;

// Referenced classes of package com.clj.fastble.callback:
//			BleBaseCallback

public abstract class BleMtuChangedCallback extends BleBaseCallback
{

	public BleMtuChangedCallback()
	{
	}

	public abstract void onSetMTUFailure(BleException bleexception);

	public abstract void onMtuChanged(int i);
}
