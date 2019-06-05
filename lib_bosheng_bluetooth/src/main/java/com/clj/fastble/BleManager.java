// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleManager.java

package com.clj.fastble;

import android.app.Application;
import android.bluetooth.*;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import com.clj.fastble.bluetooth.BleBluetooth;
import com.clj.fastble.bluetooth.BleConnector;
import com.clj.fastble.bluetooth.MultipleBluetoothController;
import com.clj.fastble.bluetooth.SplitWriter;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.OtherException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.scan.BleScanner;
import com.clj.fastble.utils.BleLog;
import java.util.Iterator;
import java.util.List;

public class BleManager
{
	private static class BleManagerHolder
	{

		private static final BleManager sBleManager = new BleManager();



		private BleManagerHolder()
		{
		}
	}


	private Application context;
	private BleScanRuleConfig bleScanRuleConfig;
	private BluetoothAdapter bluetoothAdapter;
	private MultipleBluetoothController multipleBluetoothController;
	private BluetoothManager bluetoothManager;
	public static final int DEFAULT_SCAN_TIME = 10000;
	private static final int DEFAULT_MAX_MULTIPLE_DEVICE = 7;
	private static final int DEFAULT_OPERATE_TIME = 5000;
	private static final int DEFAULT_CONNECT_RETRY_COUNT = 0;
	private static final int DEFAULT_CONNECT_RETRY_INTERVAL = 5000;
	private static final int DEFAULT_MTU = 23;
	private static final int DEFAULT_MAX_MTU = 512;
	private static final int DEFAULT_WRITE_DATA_SPLIT_COUNT = 20;
	private static final int DEFAULT_CONNECT_OVER_TIME = 10000;
	private int maxConnectCount;
	private int operateTimeout;
	private int reConnectCount;
	private long reConnectInterval;
	private int splitWriteNum;
	private long connectOverTime;

	public BleManager()
	{
		maxConnectCount = 7;
		operateTimeout = 5000;
		reConnectCount = 0;
		reConnectInterval = 5000L;
		splitWriteNum = 20;
		connectOverTime = 10000L;
	}

	public static BleManager getInstance()
	{
		return BleManagerHolder.sBleManager;
	}

	public void init(Application app)
	{
		if (context == null && app != null)
		{
			context = app;
			if (isSupportBle())
				bluetoothManager = (BluetoothManager)context.getSystemService("bluetooth");
			bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			multipleBluetoothController = new MultipleBluetoothController();
			bleScanRuleConfig = new BleScanRuleConfig();
		}
	}

	public Context getContext()
	{
		return context;
	}

	public BluetoothManager getBluetoothManager()
	{
		return bluetoothManager;
	}

	public BluetoothAdapter getBluetoothAdapter()
	{
		return bluetoothAdapter;
	}

	public BleScanRuleConfig getScanRuleConfig()
	{
		return bleScanRuleConfig;
	}

	public MultipleBluetoothController getMultipleBluetoothController()
	{
		return multipleBluetoothController;
	}

	public void initScanRule(BleScanRuleConfig config)
	{
		bleScanRuleConfig = config;
	}

	public int getMaxConnectCount()
	{
		return maxConnectCount;
	}

	public BleManager setMaxConnectCount(int count)
	{
		if (count > 7)
			count = 7;
		maxConnectCount = count;
		return this;
	}

	public int getOperateTimeout()
	{
		return operateTimeout;
	}

	public BleManager setOperateTimeout(int count)
	{
		operateTimeout = count;
		return this;
	}

	public int getReConnectCount()
	{
		return reConnectCount;
	}

	public long getReConnectInterval()
	{
		return reConnectInterval;
	}

	public BleManager setReConnectCount(int count)
	{
		return setReConnectCount(count, 5000L);
	}

	public BleManager setReConnectCount(int count, long interval)
	{
		if (count > 10)
			count = 10;
		if (interval < 0L)
			interval = 0L;
		reConnectCount = count;
		reConnectInterval = interval;
		return this;
	}

	public int getSplitWriteNum()
	{
		return splitWriteNum;
	}

	public BleManager setSplitWriteNum(int num)
	{
		splitWriteNum = num;
		return this;
	}

	public long getConnectOverTime()
	{
		return connectOverTime;
	}

	public BleManager setConnectOverTime(long time)
	{
		if (time <= 0L)
			time = 100L;
		connectOverTime = time;
		return this;
	}

	public BleManager enableLog(boolean enable)
	{
		BleLog.isPrint = enable;
		return this;
	}

	public void scan(BleScanCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleScanCallback can not be Null!");
		if (!isBlueEnable())
		{
			BleLog.e("Bluetooth not enable!");
			callback.onScanStarted(false);
			return;
		} else
		{
			java.util.UUID serviceUuids[] = bleScanRuleConfig.getServiceUuids();
			String deviceNames[] = bleScanRuleConfig.getDeviceNames();
			String deviceMac = bleScanRuleConfig.getDeviceMac();
			boolean fuzzy = bleScanRuleConfig.isFuzzy();
			long timeOut = bleScanRuleConfig.getScanTimeOut();
			BleScanner.getInstance().scan(serviceUuids, deviceNames, deviceMac, fuzzy, timeOut, callback);
			return;
		}
	}

	public void scanAndConnect(BleScanAndConnectCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleScanAndConnectCallback can not be Null!");
		if (!isBlueEnable())
		{
			BleLog.e("Bluetooth not enable!");
			callback.onScanStarted(false);
			return;
		} else
		{
			java.util.UUID serviceUuids[] = bleScanRuleConfig.getServiceUuids();
			String deviceNames[] = bleScanRuleConfig.getDeviceNames();
			String deviceMac = bleScanRuleConfig.getDeviceMac();
			boolean fuzzy = bleScanRuleConfig.isFuzzy();
			long timeOut = bleScanRuleConfig.getScanTimeOut();
			BleScanner.getInstance().scanAndConnect(serviceUuids, deviceNames, deviceMac, fuzzy, timeOut, callback);
			return;
		}
	}

	public BluetoothGatt connect(BleDevice bleDevice, BleGattCallback bleGattCallback)
	{
		if (bleGattCallback == null)
			throw new IllegalArgumentException("BleGattCallback can not be Null!");
		if (!isBlueEnable())
		{
			BleLog.e("Bluetooth not enable!");
			bleGattCallback.onConnectFail(bleDevice, new OtherException("Bluetooth not enable!"));
			return null;
		}
		if (Looper.myLooper() == null || Looper.myLooper() != Looper.getMainLooper())
			BleLog.w("Be careful: currentThread is not MainThread!");
		if (bleDevice == null || bleDevice.getDevice() == null)
		{
			bleGattCallback.onConnectFail(bleDevice, new OtherException("Not Found Device Exception Occurred!"));
		} else
		{
			BleBluetooth bleBluetooth = multipleBluetoothController.buildConnectingBle(bleDevice);
			boolean autoConnect = bleScanRuleConfig.isAutoConnect();
			return bleBluetooth.connect(bleDevice, autoConnect, bleGattCallback);
		}
		return null;
	}

	public BluetoothGatt connect(String mac, BleGattCallback bleGattCallback)
	{
		BluetoothDevice bluetoothDevice = getBluetoothAdapter().getRemoteDevice(mac);
		BleDevice bleDevice = new BleDevice(bluetoothDevice, 0, null, 0L);
		return connect(bleDevice, bleGattCallback);
	}

	public void cancelScan()
	{
		BleScanner.getInstance().stopLeScan();
	}

	public void notify(BleDevice bleDevice, String uuid_service, String uuid_notify, BleNotifyCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleNotifyCallback can not be Null!");
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			callback.onNotifyFailure(new OtherException("This device not connect!"));
		else
			bleBluetooth.newBleConnector().withUUIDString(uuid_service, uuid_notify).enableCharacteristicNotify(callback, uuid_notify);
	}

	public void indicate(BleDevice bleDevice, String uuid_service, String uuid_indicate, BleIndicateCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleIndicateCallback can not be Null!");
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			callback.onIndicateFailure(new OtherException("This device not connect!"));
		else
			bleBluetooth.newBleConnector().withUUIDString(uuid_service, uuid_indicate).enableCharacteristicIndicate(callback, uuid_indicate);
	}

	public boolean stopNotify(BleDevice bleDevice, String uuid_service, String uuid_notify)
	{
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			return false;
		boolean success = bleBluetooth.newBleConnector().withUUIDString(uuid_service, uuid_notify).disableCharacteristicNotify();
		if (success)
			bleBluetooth.removeNotifyCallback(uuid_notify);
		return success;
	}

	public boolean stopIndicate(BleDevice bleDevice, String uuid_service, String uuid_indicate)
	{
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			return false;
		boolean success = bleBluetooth.newBleConnector().withUUIDString(uuid_service, uuid_indicate).disableCharacteristicIndicate();
		if (success)
			bleBluetooth.removeIndicateCallback(uuid_indicate);
		return success;
	}

	public void write(BleDevice bleDevice, String uuid_service, String uuid_write, byte data[], BleWriteCallback callback)
	{
		write(bleDevice, uuid_service, uuid_write, data, true, callback);
	}

	public void write(BleDevice bleDevice, String uuid_service, String uuid_write, byte data[], boolean split, BleWriteCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleWriteCallback can not be Null!");
		if (data == null)
		{
			BleLog.e("data is Null!");
			callback.onWriteFailure(new OtherException("data is Null!"));
			return;
		}
		if (data.length > 20 && !split)
			BleLog.w("Be careful: data's length beyond 20! Ensure MTU higher than 23, or use spilt write!");
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			callback.onWriteFailure(new OtherException("This device not connect!"));
		else
		if (split && data.length > 20)
			(new SplitWriter()).splitWrite(bleBluetooth, uuid_service, uuid_write, data, callback);
		else
			bleBluetooth.newBleConnector().withUUIDString(uuid_service, uuid_write).writeCharacteristic(data, callback, uuid_write);
	}

	public void read(BleDevice bleDevice, String uuid_service, String uuid_read, BleReadCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleReadCallback can not be Null!");
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			callback.onReadFailure(new OtherException("This device is not connected!"));
		else
			bleBluetooth.newBleConnector().withUUIDString(uuid_service, uuid_read).readCharacteristic(callback, uuid_read);
	}

	public void readRssi(BleDevice bleDevice, BleRssiCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleRssiCallback can not be Null!");
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			callback.onRssiFailure(new OtherException("This device is not connected!"));
		else
			bleBluetooth.newBleConnector().readRemoteRssi(callback);
	}

	public void setMtu(BleDevice bleDevice, int mtu, BleMtuChangedCallback callback)
	{
		if (callback == null)
			throw new IllegalArgumentException("BleMtuChangedCallback can not be Null!");
		if (mtu > 512)
		{
			BleLog.e("requiredMtu should lower than 512 !");
			callback.onSetMTUFailure(new OtherException("requiredMtu should lower than 512 !"));
			return;
		}
		if (mtu < 23)
		{
			BleLog.e("requiredMtu should higher than 23 !");
			callback.onSetMTUFailure(new OtherException("requiredMtu should higher than 23 !"));
			return;
		}
		BleBluetooth bleBluetooth = multipleBluetoothController.getBleBluetooth(bleDevice);
		if (bleBluetooth == null)
			callback.onSetMTUFailure(new OtherException("This device is not connected!"));
		else
			bleBluetooth.newBleConnector().setMtu(mtu, callback);
	}

	public boolean isSupportBle()
	{
		return Build.VERSION.SDK_INT >= 18 && context.getApplicationContext().getPackageManager().hasSystemFeature("android.hardware.bluetooth_le");
	}

	public void enableBluetooth()
	{
		if (bluetoothAdapter != null)
			bluetoothAdapter.enable();
	}

	public void disableBluetooth()
	{
		if (bluetoothAdapter != null && bluetoothAdapter.isEnabled())
			bluetoothAdapter.disable();
	}

	public boolean isBlueEnable()
	{
		return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
	}

	public BleDevice convertBleDevice(BluetoothDevice bluetoothDevice)
	{
		return new BleDevice(bluetoothDevice);
	}

	public BleDevice convertBleDevice(ScanResult scanResult)
	{
		if (scanResult == null)
			throw new IllegalArgumentException("scanResult can not be Null!");
		BluetoothDevice bluetoothDevice = scanResult.getDevice();
		int rssi = scanResult.getRssi();
		ScanRecord scanRecord = scanResult.getScanRecord();
		byte bytes[] = null;
		if (scanRecord != null)
			bytes = scanRecord.getBytes();
		long timestampNanos = scanResult.getTimestampNanos();
		return new BleDevice(bluetoothDevice, rssi, bytes, timestampNanos);
	}

	public BleBluetooth getBleBluetooth(BleDevice bleDevice)
	{
		if (multipleBluetoothController != null)
			return multipleBluetoothController.getBleBluetooth(bleDevice);
		else
			return null;
	}

	public BluetoothGatt getBluetoothGatt(BleDevice bleDevice)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			return bleBluetooth.getBluetoothGatt();
		else
			return null;
	}

	public List getBluetoothGattServices(BleDevice bleDevice)
	{
		BluetoothGatt gatt = getBluetoothGatt(bleDevice);
		if (gatt != null)
			return gatt.getServices();
		else
			return null;
	}

	public List getBluetoothGattCharacteristics(BluetoothGattService service)
	{
		return service.getCharacteristics();
	}

	public void removeConnectGattCallback(BleDevice bleDevice)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.removeConnectGattCallback();
	}

	public void removeRssiCallback(BleDevice bleDevice)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.removeRssiCallback();
	}

	public void removeMtuChangedCallback(BleDevice bleDevice)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.removeMtuChangedCallback();
	}

	public void removeNotifyCallback(BleDevice bleDevice, String uuid_notify)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.removeNotifyCallback(uuid_notify);
	}

	public void removeIndicateCallback(BleDevice bleDevice, String uuid_indicate)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.removeIndicateCallback(uuid_indicate);
	}

	public void removeWriteCallback(BleDevice bleDevice, String uuid_write)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.removeWriteCallback(uuid_write);
	}

	public void removeReadCallback(BleDevice bleDevice, String uuid_read)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.removeReadCallback(uuid_read);
	}

	public void clearCharacterCallback(BleDevice bleDevice)
	{
		BleBluetooth bleBluetooth = getBleBluetooth(bleDevice);
		if (bleBluetooth != null)
			bleBluetooth.clearCharacterCallback();
	}

	public BleScanState getScanSate()
	{
		return BleScanner.getInstance().getScanState();
	}

	public List getAllConnectedDevice()
	{
		if (multipleBluetoothController == null)
			return null;
		else
			return multipleBluetoothController.getDeviceList();
	}

	public int getConnectState(BleDevice bleDevice)
	{
		if (bleDevice != null)
			return bluetoothManager.getConnectionState(bleDevice.getDevice(), 7);
		else
			return 0;
	}

	public boolean isConnected(BleDevice bleDevice)
	{
		return getConnectState(bleDevice) == 2;
	}

	public boolean isConnected(String mac)
	{
		List list = getAllConnectedDevice();
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			BleDevice bleDevice = (BleDevice)iterator.next();
			if (bleDevice != null && bleDevice.getMac().equals(mac))
				return true;
		}

		return false;
	}

	public void disconnect(BleDevice bleDevice)
	{
		if (multipleBluetoothController != null)
			multipleBluetoothController.disconnect(bleDevice);
	}

	public void disconnectAllDevice()
	{
		if (multipleBluetoothController != null)
			multipleBluetoothController.disconnectAllDevice();
	}

	public void destroy()
	{
		if (multipleBluetoothController != null)
			multipleBluetoothController.destroy();
	}
}
