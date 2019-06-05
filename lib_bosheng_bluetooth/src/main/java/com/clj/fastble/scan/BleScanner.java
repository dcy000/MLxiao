// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleScanner.java

package com.clj.fastble.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Looper;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.*;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.utils.BleLog;
import java.util.List;
import java.util.UUID;

// Referenced classes of package com.clj.fastble.scan:
//			BleScanPresenter

public class BleScanner
{
	private static class BleScannerHolder
	{

		private static final BleScanner sBleScanner = new BleScanner();



		private BleScannerHolder()
		{
		}
	}


	private BleScanState mBleScanState;
	private BleScanPresenter mBleScanPresenter;

	public BleScanner()
	{
		mBleScanState = BleScanState.STATE_IDLE;
		mBleScanPresenter = new BleScanPresenter() {

			final BleScanner this$0;

			public void onScanStarted(boolean success)
			{
				BleScanPresenterImp callback = mBleScanPresenter.getBleScanPresenterImp();
				if (callback != null)
					callback.onScanStarted(success);
			}

			public void onLeScan(BleDevice bleDevice)
			{
				if (mBleScanPresenter.ismNeedConnect())
				{
					BleScanAndConnectCallback callback = (BleScanAndConnectCallback)mBleScanPresenter.getBleScanPresenterImp();
					if (callback != null)
						callback.onLeScan(bleDevice);
				} else
				{
					BleScanCallback callback = (BleScanCallback)mBleScanPresenter.getBleScanPresenterImp();
					if (callback != null)
						callback.onLeScan(bleDevice);
				}
			}

			public void onScanning(BleDevice result)
			{
				BleScanPresenterImp callback = mBleScanPresenter.getBleScanPresenterImp();
				if (callback != null)
					callback.onScanning(result);
			}

			public void onScanFinished(List bleDeviceList)
			{
				if (mBleScanPresenter.ismNeedConnect())
				{
					final BleScanAndConnectCallback callback = (BleScanAndConnectCallback)mBleScanPresenter.getBleScanPresenterImp();
					if (bleDeviceList == null || bleDeviceList.size() < 1)
					{
						if (callback != null)
							callback.onScanFinished(null);
					} else
					{
						if (callback != null)
							callback.onScanFinished((BleDevice)bleDeviceList.get(0));
						final List list = bleDeviceList;
						(new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {

							final List val$list;
							final BleScanAndConnectCallback val$callback;
							final 1 this$1;

							public void run()
							{
								BleManager.getInstance().connect((BleDevice)list.get(0), callback);
							}

					
					{
						this.this$1 = 1.this;
						list = list1;
						callback = blescanandconnectcallback;
						super();
					}
						}, 100L);
					}
				} else
				{
					BleScanCallback callback = (BleScanCallback)mBleScanPresenter.getBleScanPresenterImp();
					if (callback != null)
						callback.onScanFinished(bleDeviceList);
				}
			}

			
			{
				this.this$0 = BleScanner.this;
				super();
			}
		};
	}

	public static BleScanner getInstance()
	{
		return BleScannerHolder.sBleScanner;
	}

	public void scan(UUID serviceUuids[], String names[], String mac, boolean fuzzy, long timeOut, BleScanCallback callback)
	{
		startLeScan(serviceUuids, names, mac, fuzzy, false, timeOut, callback);
	}

	public void scanAndConnect(UUID serviceUuids[], String names[], String mac, boolean fuzzy, long timeOut, BleScanAndConnectCallback callback)
	{
		startLeScan(serviceUuids, names, mac, fuzzy, true, timeOut, callback);
	}

	private synchronized void startLeScan(UUID serviceUuids[], String names[], String mac, boolean fuzzy, boolean needConnect, long timeOut, 
			BleScanPresenterImp imp)
	{
		if (mBleScanState != BleScanState.STATE_IDLE)
		{
			BleLog.w("scan action already exists, complete the previous scan action first");
			if (imp != null)
				imp.onScanStarted(false);
			return;
		} else
		{
			mBleScanPresenter.prepare(names, mac, fuzzy, needConnect, timeOut, imp);
			boolean success = BleManager.getInstance().getBluetoothAdapter().startLeScan(serviceUuids, mBleScanPresenter);
			mBleScanState = success ? BleScanState.STATE_SCANNING : BleScanState.STATE_IDLE;
			mBleScanPresenter.notifyScanStarted(success);
			return;
		}
	}

	public synchronized void stopLeScan()
	{
		BleManager.getInstance().getBluetoothAdapter().stopLeScan(mBleScanPresenter);
		mBleScanState = BleScanState.STATE_IDLE;
		mBleScanPresenter.notifyScanStopped();
	}

	public BleScanState getScanState()
	{
		return mBleScanState;
	}

}
