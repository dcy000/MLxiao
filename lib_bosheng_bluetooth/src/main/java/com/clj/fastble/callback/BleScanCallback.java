// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleScanCallback.java

package com.clj.fastble.callback;

import com.clj.fastble.data.BleDevice;
import java.util.List;

// Referenced classes of package com.clj.fastble.callback:
//			BleScanPresenterImp

public abstract class BleScanCallback
	implements BleScanPresenterImp
{

	public BleScanCallback()
	{
	}

	public abstract void onScanFinished(List list);

	public void onLeScan(BleDevice bledevice)
	{
	}
}
