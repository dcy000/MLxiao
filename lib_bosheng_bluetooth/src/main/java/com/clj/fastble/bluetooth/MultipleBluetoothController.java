// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MultipleBluetoothController.java

package com.clj.fastble.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.utils.BleLruHashMap;
import java.util.*;

// Referenced classes of package com.clj.fastble.bluetooth:
//			BleBluetooth

public class MultipleBluetoothController
{

	private final BleLruHashMap bleLruHashMap = new BleLruHashMap(BleManager.getInstance().getMaxConnectCount());
	private final HashMap bleTempHashMap = new HashMap();

	public MultipleBluetoothController()
	{
	}

	public synchronized BleBluetooth buildConnectingBle(BleDevice bleDevice)
	{
		BleBluetooth bleBluetooth = new BleBluetooth(bleDevice);
		if (!bleTempHashMap.containsKey(bleBluetooth.getDeviceKey()))
			bleTempHashMap.put(bleBluetooth.getDeviceKey(), bleBluetooth);
		return bleBluetooth;
	}

	public synchronized void removeConnectingBle(BleBluetooth bleBluetooth)
	{
		if (bleBluetooth == null)
			return;
		if (bleTempHashMap.containsKey(bleBluetooth.getDeviceKey()))
			bleTempHashMap.remove(bleBluetooth.getDeviceKey());
	}

	public synchronized void addBleBluetooth(BleBluetooth bleBluetooth)
	{
		if (bleBluetooth == null)
			return;
		if (!bleLruHashMap.containsKey(bleBluetooth.getDeviceKey()))
			bleLruHashMap.put(bleBluetooth.getDeviceKey(), bleBluetooth);
	}

	public synchronized void removeBleBluetooth(BleBluetooth bleBluetooth)
	{
		if (bleBluetooth == null)
			return;
		if (bleLruHashMap.containsKey(bleBluetooth.getDeviceKey()))
			bleLruHashMap.remove(bleBluetooth.getDeviceKey());
	}

	public synchronized boolean isContainDevice(BleDevice bleDevice)
	{
		return bleDevice != null && bleLruHashMap.containsKey(bleDevice.getKey());
	}

	public synchronized boolean isContainDevice(BluetoothDevice bluetoothDevice)
	{
		return bluetoothDevice != null && bleLruHashMap.containsKey((new StringBuilder()).append(bluetoothDevice.getName()).append(bluetoothDevice.getAddress()).toString());
	}

	public synchronized BleBluetooth getBleBluetooth(BleDevice bleDevice)
	{
		if (bleDevice != null && bleLruHashMap.containsKey(bleDevice.getKey()))
			return (BleBluetooth)bleLruHashMap.get(bleDevice.getKey());
		else
			return null;
	}

	public synchronized void disconnect(BleDevice bleDevice)
	{
		if (isContainDevice(bleDevice))
			getBleBluetooth(bleDevice).disconnect();
	}

	public synchronized void disconnectAllDevice()
	{
		Map.Entry stringBleBluetoothEntry;
		for (Iterator iterator = bleLruHashMap.entrySet().iterator(); iterator.hasNext(); ((BleBluetooth)stringBleBluetoothEntry.getValue()).disconnect())
			stringBleBluetoothEntry = (Map.Entry)iterator.next();

		bleLruHashMap.clear();
	}

	public synchronized void destroy()
	{
		Map.Entry stringBleBluetoothEntry;
		for (Iterator iterator = bleLruHashMap.entrySet().iterator(); iterator.hasNext(); ((BleBluetooth)stringBleBluetoothEntry.getValue()).destroy())
			stringBleBluetoothEntry = (Map.Entry)iterator.next();

		bleLruHashMap.clear();
		Map.Entry stringBleBluetoothEntry;
		for (Iterator iterator1 = bleTempHashMap.entrySet().iterator(); iterator1.hasNext(); ((BleBluetooth)stringBleBluetoothEntry.getValue()).destroy())
			stringBleBluetoothEntry = (Map.Entry)iterator1.next();

		bleTempHashMap.clear();
	}

	public synchronized List getBleBluetoothList()
	{
		List bleBluetoothList = new ArrayList(bleLruHashMap.values());
		Collections.sort(bleBluetoothList, new Comparator() {

			final MultipleBluetoothController this$0;

			public int compare(BleBluetooth lhs, BleBluetooth rhs)
			{
				return lhs.getDeviceKey().compareToIgnoreCase(rhs.getDeviceKey());
			}

			public volatile int compare(Object obj, Object obj1)
			{
				return compare((BleBluetooth)obj, (BleBluetooth)obj1);
			}

			
			{
				this.this$0 = MultipleBluetoothController.this;
				super();
			}
		});
		return bleBluetoothList;
	}

	public synchronized List getDeviceList()
	{
		refreshConnectedDevice();
		List deviceList = new ArrayList();
		Iterator iterator = getBleBluetoothList().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			BleBluetooth BleBluetooth = (BleBluetooth)iterator.next();
			if (BleBluetooth != null)
				deviceList.add(BleBluetooth.getDevice());
		} while (true);
		return deviceList;
	}

	public void refreshConnectedDevice()
	{
		if (Build.VERSION.SDK_INT >= 18)
		{
			List bluetoothList = getBleBluetoothList();
			for (int i = 0; bluetoothList != null && i < bluetoothList.size(); i++)
			{
				BleBluetooth bleBluetooth = (BleBluetooth)bluetoothList.get(i);
				if (!BleManager.getInstance().isConnected(bleBluetooth.getDevice()))
					removeBleBluetooth(bleBluetooth);
			}

		}
	}
}
