// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleScanPresenter.java

package com.clj.fastble.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.*;
import android.text.TextUtils;
import com.clj.fastble.callback.BleScanPresenterImp;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleMsg;
import com.clj.fastble.utils.BleLog;
import com.clj.fastble.utils.HexUtil;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

// Referenced classes of package com.clj.fastble.scan:
//			BleScanner

public abstract class BleScanPresenter
	implements BluetoothAdapter.LeScanCallback
{
	private static final class ScanHandler extends Handler
	{

		private final WeakReference mBleScanPresenter;

		public void handleMessage(Message msg)
		{
			BleScanPresenter bleScanPresenter = (BleScanPresenter)mBleScanPresenter.get();
			if (bleScanPresenter != null && msg.what == 0)
			{
				BleDevice bleDevice = (BleDevice)msg.obj;
				if (bleDevice != null)
					bleScanPresenter.handleResult(bleDevice);
			}
		}

		ScanHandler(Looper looper, BleScanPresenter bleScanPresenter)
		{
			super(looper);
			mBleScanPresenter = new WeakReference(bleScanPresenter);
		}
	}


	private String mDeviceNames[];
	private String mDeviceMac;
	private boolean mFuzzy;
	private boolean mNeedConnect;
	private long mScanTimeout;
	private BleScanPresenterImp mBleScanPresenterImp;
	private List mBleDeviceList;
	private Handler mMainHandler;
	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private boolean mHandling;

	public BleScanPresenter()
	{
		mBleDeviceList = new ArrayList();
		mMainHandler = new Handler(Looper.getMainLooper());
	}

	private void handleResult(final BleDevice bleDevice)
	{
		mMainHandler.post(new Runnable() {

			final BleDevice val$bleDevice;
			final BleScanPresenter this$0;

			public void run()
			{
				onLeScan(bleDevice);
			}

			
			{
				this.this$0 = BleScanPresenter.this;
				bleDevice = bledevice;
				super();
			}
		});
		checkDevice(bleDevice);
	}

	public void prepare(String names[], String mac, boolean fuzzy, boolean needConnect, long timeOut, BleScanPresenterImp bleScanPresenterImp)
	{
		mDeviceNames = names;
		mDeviceMac = mac;
		mFuzzy = fuzzy;
		mNeedConnect = needConnect;
		mScanTimeout = timeOut;
		mBleScanPresenterImp = bleScanPresenterImp;
		mHandlerThread = new HandlerThread(com/clj/fastble/scan/BleScanPresenter.getSimpleName());
		mHandlerThread.start();
		mHandler = new ScanHandler(mHandlerThread.getLooper(), this);
		mHandling = true;
	}

	public boolean ismNeedConnect()
	{
		return mNeedConnect;
	}

	public BleScanPresenterImp getBleScanPresenterImp()
	{
		return mBleScanPresenterImp;
	}

	public void onLeScan(BluetoothDevice device, int rssi, byte scanRecord[])
	{
		if (device == null)
			return;
		if (!mHandling)
		{
			return;
		} else
		{
			Message message = mHandler.obtainMessage();
			message.what = 0;
			message.obj = new BleDevice(device, rssi, scanRecord, System.currentTimeMillis());
			mHandler.sendMessage(message);
			return;
		}
	}

	private void checkDevice(BleDevice bleDevice)
	{
		if (TextUtils.isEmpty(mDeviceMac) && (mDeviceNames == null || mDeviceNames.length < 1))
		{
			correctDeviceAndNextStep(bleDevice);
			return;
		}
		if (!TextUtils.isEmpty(mDeviceMac) && !mDeviceMac.equalsIgnoreCase(bleDevice.getMac()))
			return;
		if (mDeviceNames != null && mDeviceNames.length > 0)
		{
			AtomicBoolean equal = new AtomicBoolean(false);
			String as[] = mDeviceNames;
			int i = as.length;
			for (int j = 0; j < i; j++)
			{
				String name = as[j];
				String remoteName = bleDevice.getName();
				if (remoteName == null)
					remoteName = "";
				if (mFuzzy ? remoteName.contains(name) : remoteName.equals(name))
					equal.set(true);
			}

			if (!equal.get())
				return;
		}
		correctDeviceAndNextStep(bleDevice);
	}

	private void correctDeviceAndNextStep(final BleDevice bleDevice)
	{
		if (mNeedConnect)
		{
			BleLog.i((new StringBuilder()).append("devices detected  ------  name:").append(bleDevice.getName()).append("  mac:").append(bleDevice.getMac()).append("  Rssi:").append(bleDevice.getRssi()).append("  scanRecord:").append(HexUtil.formatHexString(bleDevice.getScanRecord())).toString());
			mBleDeviceList.add(bleDevice);
			mMainHandler.post(new Runnable() {

				final BleScanPresenter this$0;

				public void run()
				{
					BleScanner.getInstance().stopLeScan();
				}

			
			{
				this.this$0 = BleScanPresenter.this;
				super();
			}
			});
		} else
		{
			AtomicBoolean hasFound = new AtomicBoolean(false);
			Iterator iterator = mBleDeviceList.iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				BleDevice result = (BleDevice)iterator.next();
				if (result.getDevice().equals(bleDevice.getDevice()))
					hasFound.set(true);
			} while (true);
			if (!hasFound.get())
			{
				BleLog.i((new StringBuilder()).append("device detected  ------  name: ").append(bleDevice.getName()).append("  mac: ").append(bleDevice.getMac()).append("  Rssi: ").append(bleDevice.getRssi()).append("  scanRecord: ").append(HexUtil.formatHexString(bleDevice.getScanRecord(), true)).toString());
				mBleDeviceList.add(bleDevice);
				mMainHandler.post(new Runnable() {

					final BleDevice val$bleDevice;
					final BleScanPresenter this$0;

					public void run()
					{
						onScanning(bleDevice);
					}

			
			{
				this.this$0 = BleScanPresenter.this;
				bleDevice = bledevice;
				super();
			}
				});
			}
		}
	}

	public final void notifyScanStarted(final boolean success)
	{
		mBleDeviceList.clear();
		removeHandlerMsg();
		if (success && mScanTimeout > 0L)
			mMainHandler.postDelayed(new Runnable() {

				final BleScanPresenter this$0;

				public void run()
				{
					BleScanner.getInstance().stopLeScan();
				}

			
			{
				this.this$0 = BleScanPresenter.this;
				super();
			}
			}, mScanTimeout);
		mMainHandler.post(new Runnable() {

			final boolean val$success;
			final BleScanPresenter this$0;

			public void run()
			{
				onScanStarted(success);
			}

			
			{
				this.this$0 = BleScanPresenter.this;
				success = flag;
				super();
			}
		});
	}

	public final void notifyScanStopped()
	{
		mHandling = false;
		mHandlerThread.quit();
		removeHandlerMsg();
		mMainHandler.post(new Runnable() {

			final BleScanPresenter this$0;

			public void run()
			{
				onScanFinished(mBleDeviceList);
			}

			
			{
				this.this$0 = BleScanPresenter.this;
				super();
			}
		});
	}

	public final void removeHandlerMsg()
	{
		mMainHandler.removeCallbacksAndMessages(null);
		mHandler.removeCallbacksAndMessages(null);
	}

	public abstract void onScanStarted(boolean flag);

	public abstract void onLeScan(BleDevice bledevice);

	public abstract void onScanning(BleDevice bledevice);

	public abstract void onScanFinished(List list);


}
