// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleBluetooth.java

package com.clj.fastble.bluetooth;

import android.bluetooth.*;
import android.os.*;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.*;
import com.clj.fastble.data.*;
import com.clj.fastble.exception.*;
import com.clj.fastble.utils.BleLog;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package com.clj.fastble.bluetooth:
//			BleConnector, MultipleBluetoothController

public class BleBluetooth
{
	static final class LastState extends Enum
	{

		public static final LastState CONNECT_IDLE;
		public static final LastState CONNECT_CONNECTING;
		public static final LastState CONNECT_CONNECTED;
		public static final LastState CONNECT_FAILURE;
		public static final LastState CONNECT_DISCONNECT;
		private static final LastState $VALUES[];

		public static LastState[] values()
		{
			return (LastState[])$VALUES.clone();
		}

		public static LastState valueOf(String name)
		{
			return (LastState)BleBluetooth.LastState.valueOf(name);
		}

		static 
		{
			CONNECT_IDLE = new LastState("CONNECT_IDLE", 0);
			CONNECT_CONNECTING = new LastState("CONNECT_CONNECTING", 1);
			CONNECT_CONNECTED = new LastState("CONNECT_CONNECTED", 2);
			CONNECT_FAILURE = new LastState("CONNECT_FAILURE", 3);
			CONNECT_DISCONNECT = new LastState("CONNECT_DISCONNECT", 4);
			$VALUES = (new LastState[] {
				CONNECT_IDLE, CONNECT_CONNECTING, CONNECT_CONNECTED, CONNECT_FAILURE, CONNECT_DISCONNECT
			});
		}

		private LastState(String s, int i)
		{
			super(s, i);
		}
	}

	private final class MainHandler extends Handler
	{

		final BleBluetooth this$0;

		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1: // '\001'
			{
				disconnectGatt();
				refreshDeviceCache();
				closeBluetoothGatt();
				if (connectRetryCount < BleManager.getInstance().getReConnectCount())
				{
					BleLog.e((new StringBuilder()).append("Connect fail, try reconnect ").append(BleManager.getInstance().getReConnectInterval()).append(" Millisecond later").toString());
					connectRetryCount++;
					Message message = mainHandler.obtainMessage();
					message.what = 3;
					mainHandler.sendMessageDelayed(message, BleManager.getInstance().getReConnectInterval());
					break;
				}
				connectRetryCount = 0;
				lastState = LastState.CONNECT_FAILURE;
				BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);
				BleConnectStateParameter para = (BleConnectStateParameter)msg.obj;
				int status = para.getStatus();
				if (bleGattCallback != null)
					bleGattCallback.onConnectFail(bleDevice, new ConnectException(bluetoothGatt, status));
				break;
			}

			case 2: // '\002'
			{
				lastState = LastState.CONNECT_DISCONNECT;
				BleManager.getInstance().getMultipleBluetoothController().removeBleBluetooth(BleBluetooth.this);
				refreshDeviceCache();
				closeBluetoothGatt();
				removeRssiCallback();
				removeMtuChangedCallback();
				clearCharacterCallback();
				mainHandler.removeCallbacksAndMessages(null);
				BleConnectStateParameter para = (BleConnectStateParameter)msg.obj;
				boolean isActive = para.isActive();
				int status = para.getStatus();
				if (bleGattCallback != null)
					bleGattCallback.onDisConnected(isActive, bleDevice, bluetoothGatt, status);
				break;
			}

			case 3: // '\003'
			{
				connect(bleDevice, false, bleGattCallback);
				break;
			}

			case 7: // '\007'
			{
				disconnectGatt();
				refreshDeviceCache();
				closeBluetoothGatt();
				lastState = LastState.CONNECT_FAILURE;
				BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);
				if (bleGattCallback != null)
					bleGattCallback.onConnectFail(bleDevice, new TimeoutException());
				break;
			}

			case 4: // '\004'
			{
				connectRetryCount = 0;
				if (bluetoothGatt != null)
				{
					boolean discoverServiceResult = bluetoothGatt.discoverServices();
					if (!discoverServiceResult)
					{
						Message message = mainHandler.obtainMessage();
						message.what = 5;
						mainHandler.sendMessage(message);
					}
				} else
				{
					Message message = mainHandler.obtainMessage();
					message.what = 5;
					mainHandler.sendMessage(message);
				}
				break;
			}

			case 5: // '\005'
			{
				disconnectGatt();
				refreshDeviceCache();
				closeBluetoothGatt();
				lastState = LastState.CONNECT_FAILURE;
				BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);
				if (bleGattCallback != null)
					bleGattCallback.onConnectFail(bleDevice, new OtherException("GATT discover services exception occurred!"));
				break;
			}

			case 6: // '\006'
			{
				lastState = LastState.CONNECT_CONNECTED;
				isActiveDisconnect = false;
				BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);
				BleManager.getInstance().getMultipleBluetoothController().addBleBluetooth(BleBluetooth.this);
				BleConnectStateParameter para = (BleConnectStateParameter)msg.obj;
				int status = para.getStatus();
				if (bleGattCallback != null)
					bleGattCallback.onConnectSuccess(bleDevice, bluetoothGatt, status);
				break;
			}

			default:
			{
				super.handleMessage(msg);
				break;
			}
			}
		}

		MainHandler(Looper looper)
		{
			this$0 = BleBluetooth.this;
			super(looper);
		}
	}


	private BleGattCallback bleGattCallback;
	private BleRssiCallback bleRssiCallback;
	private BleMtuChangedCallback bleMtuChangedCallback;
	private HashMap bleNotifyCallbackHashMap;
	private HashMap bleIndicateCallbackHashMap;
	private HashMap bleWriteCallbackHashMap;
	private HashMap bleReadCallbackHashMap;
	private LastState lastState;
	private boolean isActiveDisconnect;
	private BleDevice bleDevice;
	private BluetoothGatt bluetoothGatt;
	private MainHandler mainHandler;
	private int connectRetryCount;
	private BluetoothGattCallback coreGattCallback;

	public BleBluetooth(BleDevice bleDevice)
	{
		bleNotifyCallbackHashMap = new HashMap();
		bleIndicateCallbackHashMap = new HashMap();
		bleWriteCallbackHashMap = new HashMap();
		bleReadCallbackHashMap = new HashMap();
		isActiveDisconnect = false;
		mainHandler = new MainHandler(Looper.getMainLooper());
		connectRetryCount = 0;
		coreGattCallback = new BluetoothGattCallback() {

			final BleBluetooth this$0;

			public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
			{
				super.onConnectionStateChange(gatt, status, newState);
				BleLog.i((new StringBuilder()).append("BluetoothGattCallback��onConnectionStateChange \nstatus: ").append(status).append('\n').append("newState: ").append(newState).append('\n').append("currentThread: ").append(Thread.currentThread().getId()).toString());
				bluetoothGatt = gatt;
				mainHandler.removeMessages(7);
				if (newState == 2)
				{
					Message message = mainHandler.obtainMessage();
					message.what = 4;
					mainHandler.sendMessageDelayed(message, 500L);
				} else
				if (newState == 0)
					if (lastState == LastState.CONNECT_CONNECTING)
					{
						Message message = mainHandler.obtainMessage();
						message.what = 1;
						message.obj = new BleConnectStateParameter(status);
						mainHandler.sendMessage(message);
					} else
					if (lastState == LastState.CONNECT_CONNECTED)
					{
						Message message = mainHandler.obtainMessage();
						message.what = 2;
						BleConnectStateParameter para = new BleConnectStateParameter(status);
						para.setActive(isActiveDisconnect);
						message.obj = para;
						mainHandler.sendMessage(message);
					}
			}

			public void onServicesDiscovered(BluetoothGatt gatt, int status)
			{
				super.onServicesDiscovered(gatt, status);
				BleLog.i((new StringBuilder()).append("BluetoothGattCallback��onServicesDiscovered \nstatus: ").append(status).append('\n').append("currentThread: ").append(Thread.currentThread().getId()).toString());
				bluetoothGatt = gatt;
				if (status == 0)
				{
					Message message = mainHandler.obtainMessage();
					message.what = 6;
					message.obj = new BleConnectStateParameter(status);
					mainHandler.sendMessage(message);
				} else
				{
					Message message = mainHandler.obtainMessage();
					message.what = 5;
					mainHandler.sendMessage(message);
				}
			}

			public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
			{
				super.onCharacteristicChanged(gatt, characteristic);
				Iterator iterator = bleNotifyCallbackHashMap.entrySet().iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					Map.Entry entry = (Map.Entry)iterator.next();
					Object callback = entry.getValue();
					if (callback instanceof BleNotifyCallback)
					{
						BleNotifyCallback bleNotifyCallback = (BleNotifyCallback)callback;
						if (characteristic.getUuid().toString().equalsIgnoreCase(bleNotifyCallback.getKey()))
						{
							Handler handler = bleNotifyCallback.getHandler();
							if (handler != null)
							{
								Message message = handler.obtainMessage();
								message.what = 19;
								message.obj = bleNotifyCallback;
								Bundle bundle = new Bundle();
								bundle.putByteArray("notify_value", characteristic.getValue());
								message.setData(bundle);
								handler.sendMessage(message);
							}
						}
					}
				} while (true);
				iterator = bleIndicateCallbackHashMap.entrySet().iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					Map.Entry entry = (Map.Entry)iterator.next();
					Object callback = entry.getValue();
					if (callback instanceof BleIndicateCallback)
					{
						BleIndicateCallback bleIndicateCallback = (BleIndicateCallback)callback;
						if (characteristic.getUuid().toString().equalsIgnoreCase(bleIndicateCallback.getKey()))
						{
							Handler handler = bleIndicateCallback.getHandler();
							if (handler != null)
							{
								Message message = handler.obtainMessage();
								message.what = 35;
								message.obj = bleIndicateCallback;
								Bundle bundle = new Bundle();
								bundle.putByteArray("indicate_value", characteristic.getValue());
								message.setData(bundle);
								handler.sendMessage(message);
							}
						}
					}
				} while (true);
			}

			public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
			{
				super.onDescriptorWrite(gatt, descriptor, status);
				Iterator iterator = bleNotifyCallbackHashMap.entrySet().iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					Map.Entry entry = (Map.Entry)iterator.next();
					Object callback = entry.getValue();
					if (callback instanceof BleNotifyCallback)
					{
						BleNotifyCallback bleNotifyCallback = (BleNotifyCallback)callback;
						if (descriptor.getCharacteristic().getUuid().toString().equalsIgnoreCase(bleNotifyCallback.getKey()))
						{
							Handler handler = bleNotifyCallback.getHandler();
							if (handler != null)
							{
								Message message = handler.obtainMessage();
								message.what = 18;
								message.obj = bleNotifyCallback;
								Bundle bundle = new Bundle();
								bundle.putInt("notify_status", status);
								message.setData(bundle);
								handler.sendMessage(message);
							}
						}
					}
				} while (true);
				iterator = bleIndicateCallbackHashMap.entrySet().iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					Map.Entry entry = (Map.Entry)iterator.next();
					Object callback = entry.getValue();
					if (callback instanceof BleIndicateCallback)
					{
						BleIndicateCallback bleIndicateCallback = (BleIndicateCallback)callback;
						if (descriptor.getCharacteristic().getUuid().toString().equalsIgnoreCase(bleIndicateCallback.getKey()))
						{
							Handler handler = bleIndicateCallback.getHandler();
							if (handler != null)
							{
								Message message = handler.obtainMessage();
								message.what = 34;
								message.obj = bleIndicateCallback;
								Bundle bundle = new Bundle();
								bundle.putInt("indicate_status", status);
								message.setData(bundle);
								handler.sendMessage(message);
							}
						}
					}
				} while (true);
			}

			public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
			{
				super.onCharacteristicWrite(gatt, characteristic, status);
				Iterator iterator = bleWriteCallbackHashMap.entrySet().iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					Map.Entry entry = (Map.Entry)iterator.next();
					Object callback = entry.getValue();
					if (callback instanceof BleWriteCallback)
					{
						BleWriteCallback bleWriteCallback = (BleWriteCallback)callback;
						if (characteristic.getUuid().toString().equalsIgnoreCase(bleWriteCallback.getKey()))
						{
							Handler handler = bleWriteCallback.getHandler();
							if (handler != null)
							{
								Message message = handler.obtainMessage();
								message.what = 50;
								message.obj = bleWriteCallback;
								Bundle bundle = new Bundle();
								bundle.putInt("write_status", status);
								bundle.putByteArray("write_value", characteristic.getValue());
								message.setData(bundle);
								handler.sendMessage(message);
							}
						}
					}
				} while (true);
			}

			public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
			{
				super.onCharacteristicRead(gatt, characteristic, status);
				Iterator iterator = bleReadCallbackHashMap.entrySet().iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					Map.Entry entry = (Map.Entry)iterator.next();
					Object callback = entry.getValue();
					if (callback instanceof BleReadCallback)
					{
						BleReadCallback bleReadCallback = (BleReadCallback)callback;
						if (characteristic.getUuid().toString().equalsIgnoreCase(bleReadCallback.getKey()))
						{
							Handler handler = bleReadCallback.getHandler();
							if (handler != null)
							{
								Message message = handler.obtainMessage();
								message.what = 66;
								message.obj = bleReadCallback;
								Bundle bundle = new Bundle();
								bundle.putInt("read_status", status);
								bundle.putByteArray("read_value", characteristic.getValue());
								message.setData(bundle);
								handler.sendMessage(message);
							}
						}
					}
				} while (true);
			}

			public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status)
			{
				super.onReadRemoteRssi(gatt, rssi, status);
				if (bleRssiCallback != null)
				{
					Handler handler = bleRssiCallback.getHandler();
					if (handler != null)
					{
						Message message = handler.obtainMessage();
						message.what = 82;
						message.obj = bleRssiCallback;
						Bundle bundle = new Bundle();
						bundle.putInt("rssi_status", status);
						bundle.putInt("rssi_value", rssi);
						message.setData(bundle);
						handler.sendMessage(message);
					}
				}
			}

			public void onMtuChanged(BluetoothGatt gatt, int mtu, int status)
			{
				super.onMtuChanged(gatt, mtu, status);
				if (bleMtuChangedCallback != null)
				{
					Handler handler = bleMtuChangedCallback.getHandler();
					if (handler != null)
					{
						Message message = handler.obtainMessage();
						message.what = 98;
						message.obj = bleMtuChangedCallback;
						Bundle bundle = new Bundle();
						bundle.putInt("mtu_status", status);
						bundle.putInt("mtu_value", mtu);
						message.setData(bundle);
						handler.sendMessage(message);
					}
				}
			}

			
			{
				this.this$0 = BleBluetooth.this;
				super();
			}
		};
		this.bleDevice = bleDevice;
	}

	public BleConnector newBleConnector()
	{
		return new BleConnector(this);
	}

	public synchronized void addConnectGattCallback(BleGattCallback callback)
	{
		bleGattCallback = callback;
	}

	public synchronized void removeConnectGattCallback()
	{
		bleGattCallback = null;
	}

	public synchronized void addNotifyCallback(String uuid, BleNotifyCallback bleNotifyCallback)
	{
		bleNotifyCallbackHashMap.put(uuid, bleNotifyCallback);
	}

	public synchronized void addIndicateCallback(String uuid, BleIndicateCallback bleIndicateCallback)
	{
		bleIndicateCallbackHashMap.put(uuid, bleIndicateCallback);
	}

	public synchronized void addWriteCallback(String uuid, BleWriteCallback bleWriteCallback)
	{
		bleWriteCallbackHashMap.put(uuid, bleWriteCallback);
	}

	public synchronized void addReadCallback(String uuid, BleReadCallback bleReadCallback)
	{
		bleReadCallbackHashMap.put(uuid, bleReadCallback);
	}

	public synchronized void removeNotifyCallback(String uuid)
	{
		if (bleNotifyCallbackHashMap.containsKey(uuid))
			bleNotifyCallbackHashMap.remove(uuid);
	}

	public synchronized void removeIndicateCallback(String uuid)
	{
		if (bleIndicateCallbackHashMap.containsKey(uuid))
			bleIndicateCallbackHashMap.remove(uuid);
	}

	public synchronized void removeWriteCallback(String uuid)
	{
		if (bleWriteCallbackHashMap.containsKey(uuid))
			bleWriteCallbackHashMap.remove(uuid);
	}

	public synchronized void removeReadCallback(String uuid)
	{
		if (bleReadCallbackHashMap.containsKey(uuid))
			bleReadCallbackHashMap.remove(uuid);
	}

	public synchronized void clearCharacterCallback()
	{
		if (bleNotifyCallbackHashMap != null)
			bleNotifyCallbackHashMap.clear();
		if (bleIndicateCallbackHashMap != null)
			bleIndicateCallbackHashMap.clear();
		if (bleWriteCallbackHashMap != null)
			bleWriteCallbackHashMap.clear();
		if (bleReadCallbackHashMap != null)
			bleReadCallbackHashMap.clear();
	}

	public synchronized void addRssiCallback(BleRssiCallback callback)
	{
		bleRssiCallback = callback;
	}

	public synchronized void removeRssiCallback()
	{
		bleRssiCallback = null;
	}

	public synchronized void addMtuChangedCallback(BleMtuChangedCallback callback)
	{
		bleMtuChangedCallback = callback;
	}

	public synchronized void removeMtuChangedCallback()
	{
		bleMtuChangedCallback = null;
	}

	public String getDeviceKey()
	{
		return bleDevice.getKey();
	}

	public BleDevice getDevice()
	{
		return bleDevice;
	}

	public BluetoothGatt getBluetoothGatt()
	{
		return bluetoothGatt;
	}

	public synchronized BluetoothGatt connect(BleDevice bleDevice, boolean autoConnect, BleGattCallback callback)
	{
		BleLog.i((new StringBuilder()).append("connect device: ").append(bleDevice.getName()).append("\nmac: ").append(bleDevice.getMac()).append("\nautoConnect: ").append(autoConnect).append("\ncurrentThread: ").append(Thread.currentThread().getId()).toString());
		addConnectGattCallback(callback);
		lastState = LastState.CONNECT_CONNECTING;
		if (Build.VERSION.SDK_INT >= 23)
			bluetoothGatt = bleDevice.getDevice().connectGatt(BleManager.getInstance().getContext(), autoConnect, coreGattCallback, 2);
		else
			bluetoothGatt = bleDevice.getDevice().connectGatt(BleManager.getInstance().getContext(), autoConnect, coreGattCallback);
		if (bluetoothGatt != null)
		{
			if (bleGattCallback != null)
				bleGattCallback.onStartConnect();
			Message message = mainHandler.obtainMessage();
			message.what = 7;
			mainHandler.sendMessageDelayed(message, BleManager.getInstance().getConnectOverTime());
		} else
		{
			disconnectGatt();
			refreshDeviceCache();
			closeBluetoothGatt();
			lastState = LastState.CONNECT_FAILURE;
			BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(this);
			if (bleGattCallback != null)
				bleGattCallback.onConnectFail(bleDevice, new OtherException("GATT connect exception occurred!"));
		}
		return bluetoothGatt;
	}

	public synchronized void disconnect()
	{
		isActiveDisconnect = true;
		disconnectGatt();
	}

	public synchronized void destroy()
	{
		lastState = LastState.CONNECT_IDLE;
		disconnectGatt();
		refreshDeviceCache();
		closeBluetoothGatt();
		removeConnectGattCallback();
		removeRssiCallback();
		removeMtuChangedCallback();
		clearCharacterCallback();
		mainHandler.removeCallbacksAndMessages(null);
	}

	private synchronized void disconnectGatt()
	{
		if (bluetoothGatt != null)
			bluetoothGatt.disconnect();
	}

	private synchronized void refreshDeviceCache()
	{
		try
		{
			Method refresh = android/bluetooth/BluetoothGatt.getMethod("refresh", new Class[0]);
			if (refresh != null && bluetoothGatt != null)
			{
				boolean success = ((Boolean)refresh.invoke(bluetoothGatt, new Object[0])).booleanValue();
				BleLog.i((new StringBuilder()).append("refreshDeviceCache, is success:  ").append(success).toString());
			}
		}
		catch (Exception e)
		{
			BleLog.i((new StringBuilder()).append("exception occur while refreshing device: ").append(e.getMessage()).toString());
			e.printStackTrace();
		}
	}

	private synchronized void closeBluetoothGatt()
	{
		if (bluetoothGatt != null)
			bluetoothGatt.close();
	}





















}
