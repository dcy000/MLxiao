// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleScanAndConnectCallback.java

package com.clj.fastble.callback;

import com.clj.fastble.data.BleDevice;

// Referenced classes of package com.clj.fastble.callback:
//			BleGattCallback, BleScanPresenterImp

public abstract class BleScanAndConnectCallback extends BleGattCallback
	implements BleScanPresenterImp
{

	public BleScanAndConnectCallback()
	{
	}

	public abstract void onScanFinished(BleDevice bledevice);

	public void onLeScan(BleDevice bledevice)
	{
	}
}
