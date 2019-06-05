// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleConnector.java

package com.clj.fastble.bluetooth;

import android.bluetooth.*;
import android.os.*;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.*;
import com.clj.fastble.data.BleMsg;
import com.clj.fastble.data.BleWriteState;
import com.clj.fastble.exception.*;
import java.util.UUID;

// Referenced classes of package com.clj.fastble.bluetooth:
//			BleBluetooth

public class BleConnector
{

	private static final String UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
	private BluetoothGatt mBluetoothGatt;
	private BluetoothGattService mGattService;
	private BluetoothGattCharacteristic mCharacteristic;
	private BleBluetooth mBleBluetooth;
	private Handler mHandler;

	BleConnector(BleBluetooth bleBluetooth)
	{
		mBleBluetooth = bleBluetooth;
		mBluetoothGatt = bleBluetooth.getBluetoothGatt();
		mHandler = new Handler(Looper.getMainLooper()) {

			final BleConnector this$0;

			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				switch (msg.what)
				{
				default:
					break;

				case 17: // '\021'
				{
					BleNotifyCallback notifyCallback = (BleNotifyCallback)msg.obj;
					if (notifyCallback != null)
						notifyCallback.onNotifyFailure(new TimeoutException());
					break;
				}

				case 18: // '\022'
				{
					notifyMsgInit();
					BleNotifyCallback notifyCallback = (BleNotifyCallback)msg.obj;
					Bundle bundle = msg.getData();
					int status = bundle.getInt("notify_status");
					if (notifyCallback == null)
						break;
					if (status == 0)
						notifyCallback.onNotifySuccess();
					else
						notifyCallback.onNotifyFailure(new GattException(status));
					break;
				}

				case 19: // '\023'
				{
					BleNotifyCallback notifyCallback = (BleNotifyCallback)msg.obj;
					Bundle bundle = msg.getData();
					byte value[] = bundle.getByteArray("notify_value");
					if (notifyCallback != null)
						notifyCallback.onCharacteristicChanged(value);
					break;
				}

				case 33: // '!'
				{
					BleIndicateCallback indicateCallback = (BleIndicateCallback)msg.obj;
					if (indicateCallback != null)
						indicateCallback.onIndicateFailure(new TimeoutException());
					break;
				}

				case 34: // '"'
				{
					indicateMsgInit();
					BleIndicateCallback indicateCallback = (BleIndicateCallback)msg.obj;
					Bundle bundle = msg.getData();
					int status = bundle.getInt("indicate_status");
					if (indicateCallback == null)
						break;
					if (status == 0)
						indicateCallback.onIndicateSuccess();
					else
						indicateCallback.onIndicateFailure(new GattException(status));
					break;
				}

				case 35: // '#'
				{
					BleIndicateCallback indicateCallback = (BleIndicateCallback)msg.obj;
					Bundle bundle = msg.getData();
					byte value[] = bundle.getByteArray("indicate_value");
					if (indicateCallback != null)
						indicateCallback.onCharacteristicChanged(value);
					break;
				}

				case 49: // '1'
				{
					BleWriteCallback writeCallback = (BleWriteCallback)msg.obj;
					if (writeCallback != null)
						writeCallback.onWriteFailure(new TimeoutException());
					break;
				}

				case 50: // '2'
				{
					writeMsgInit();
					BleWriteCallback writeCallback = (BleWriteCallback)msg.obj;
					Bundle bundle = msg.getData();
					int status = bundle.getInt("write_status");
					byte value[] = bundle.getByteArray("write_value");
					if (writeCallback == null)
						break;
					if (status == 0)
						writeCallback.onWriteSuccess(1, 1, value);
					else
						writeCallback.onWriteFailure(new GattException(status));
					break;
				}

				case 65: // 'A'
				{
					BleReadCallback readCallback = (BleReadCallback)msg.obj;
					if (readCallback != null)
						readCallback.onReadFailure(new TimeoutException());
					break;
				}

				case 66: // 'B'
				{
					readMsgInit();
					BleReadCallback readCallback = (BleReadCallback)msg.obj;
					Bundle bundle = msg.getData();
					int status = bundle.getInt("read_status");
					byte value[] = bundle.getByteArray("read_value");
					if (readCallback == null)
						break;
					if (status == 0)
						readCallback.onReadSuccess(value);
					else
						readCallback.onReadFailure(new GattException(status));
					break;
				}

				case 81: // 'Q'
				{
					BleRssiCallback rssiCallback = (BleRssiCallback)msg.obj;
					if (rssiCallback != null)
						rssiCallback.onRssiFailure(new TimeoutException());
					break;
				}

				case 82: // 'R'
				{
					rssiMsgInit();
					BleRssiCallback rssiCallback = (BleRssiCallback)msg.obj;
					Bundle bundle = msg.getData();
					int status = bundle.getInt("rssi_status");
					int value = bundle.getInt("rssi_value");
					if (rssiCallback == null)
						break;
					if (status == 0)
						rssiCallback.onRssiSuccess(value);
					else
						rssiCallback.onRssiFailure(new GattException(status));
					break;
				}

				case 97: // 'a'
				{
					BleMtuChangedCallback mtuChangedCallback = (BleMtuChangedCallback)msg.obj;
					if (mtuChangedCallback != null)
						mtuChangedCallback.onSetMTUFailure(new TimeoutException());
					break;
				}

				case 98: // 'b'
				{
					mtuChangedMsgInit();
					BleMtuChangedCallback mtuChangedCallback = (BleMtuChangedCallback)msg.obj;
					Bundle bundle = msg.getData();
					int status = bundle.getInt("mtu_status");
					int value = bundle.getInt("mtu_value");
					if (mtuChangedCallback == null)
						break;
					if (status == 0)
						mtuChangedCallback.onMtuChanged(value);
					else
						mtuChangedCallback.onSetMTUFailure(new GattException(status));
					break;
				}
				}
			}

			
			{
				this.this$0 = BleConnector.this;
				super(x0);
			}
		};
	}

	private BleConnector withUUID(UUID serviceUUID, UUID characteristicUUID)
	{
		if (serviceUUID != null && mBluetoothGatt != null)
			mGattService = mBluetoothGatt.getService(serviceUUID);
		if (mGattService != null && characteristicUUID != null)
			mCharacteristic = mGattService.getCharacteristic(characteristicUUID);
		return this;
	}

	public BleConnector withUUIDString(String serviceUUID, String characteristicUUID)
	{
		return withUUID(formUUID(serviceUUID), formUUID(characteristicUUID));
	}

	private UUID formUUID(String uuid)
	{
		return uuid != null ? UUID.fromString(uuid) : null;
	}

	public void enableCharacteristicNotify(BleNotifyCallback bleNotifyCallback, String uuid_notify)
	{
		if (mCharacteristic != null && (mCharacteristic.getProperties() | 0x10) > 0)
		{
			handleCharacteristicNotifyCallback(bleNotifyCallback, uuid_notify);
			setCharacteristicNotification(mBluetoothGatt, mCharacteristic, true, bleNotifyCallback);
		} else
		if (bleNotifyCallback != null)
			bleNotifyCallback.onNotifyFailure(new OtherException("this characteristic not support notify!"));
	}

	public boolean disableCharacteristicNotify()
	{
		if (mCharacteristic != null && (mCharacteristic.getProperties() | 0x10) > 0)
			return setCharacteristicNotification(mBluetoothGatt, mCharacteristic, false, null);
		else
			return false;
	}

	private boolean setCharacteristicNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, boolean enable, BleNotifyCallback bleNotifyCallback)
	{
		if (gatt == null || characteristic == null)
		{
			notifyMsgInit();
			if (bleNotifyCallback != null)
				bleNotifyCallback.onNotifyFailure(new OtherException("gatt or characteristic equal null"));
			return false;
		}
		boolean success1 = gatt.setCharacteristicNotification(characteristic, enable);
		if (!success1)
		{
			notifyMsgInit();
			if (bleNotifyCallback != null)
				bleNotifyCallback.onNotifyFailure(new OtherException("gatt setCharacteristicNotification fail"));
			return false;
		}
		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(formUUID("00002902-0000-1000-8000-00805f9b34fb"));
		if (descriptor == null)
		{
			notifyMsgInit();
			if (bleNotifyCallback != null)
				bleNotifyCallback.onNotifyFailure(new OtherException("descriptor equals null"));
			return false;
		}
		descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		boolean success2 = gatt.writeDescriptor(descriptor);
		if (!success2)
		{
			notifyMsgInit();
			if (bleNotifyCallback != null)
				bleNotifyCallback.onNotifyFailure(new OtherException("gatt writeDescriptor fail"));
		}
		return success2;
	}

	public void enableCharacteristicIndicate(BleIndicateCallback bleIndicateCallback, String uuid_indicate)
	{
		if (mCharacteristic != null && (mCharacteristic.getProperties() | 0x10) > 0)
		{
			handleCharacteristicIndicateCallback(bleIndicateCallback, uuid_indicate);
			setCharacteristicIndication(mBluetoothGatt, mCharacteristic, true, bleIndicateCallback);
		} else
		if (bleIndicateCallback != null)
			bleIndicateCallback.onIndicateFailure(new OtherException("this characteristic not support indicate!"));
	}

	public boolean disableCharacteristicIndicate()
	{
		if (mCharacteristic != null && (mCharacteristic.getProperties() | 0x10) > 0)
			return setCharacteristicIndication(mBluetoothGatt, mCharacteristic, false, null);
		else
			return false;
	}

	private boolean setCharacteristicIndication(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, boolean enable, BleIndicateCallback bleIndicateCallback)
	{
		if (gatt == null || characteristic == null)
		{
			indicateMsgInit();
			if (bleIndicateCallback != null)
				bleIndicateCallback.onIndicateFailure(new OtherException("gatt or characteristic equal null"));
			return false;
		}
		boolean success1 = gatt.setCharacteristicNotification(characteristic, enable);
		if (!success1)
		{
			indicateMsgInit();
			if (bleIndicateCallback != null)
				bleIndicateCallback.onIndicateFailure(new OtherException("gatt setCharacteristicNotification fail"));
			return false;
		}
		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(formUUID("00002902-0000-1000-8000-00805f9b34fb"));
		if (descriptor == null)
		{
			indicateMsgInit();
			if (bleIndicateCallback != null)
				bleIndicateCallback.onIndicateFailure(new OtherException("descriptor equals null"));
			return false;
		}
		descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		boolean success2 = gatt.writeDescriptor(descriptor);
		if (!success2)
		{
			indicateMsgInit();
			if (bleIndicateCallback != null)
				bleIndicateCallback.onIndicateFailure(new OtherException("gatt writeDescriptor fail"));
		}
		return success2;
	}

	public void writeCharacteristic(byte data[], BleWriteCallback bleWriteCallback, String uuid_write)
	{
		if (data == null || data.length <= 0)
		{
			if (bleWriteCallback != null)
				bleWriteCallback.onWriteFailure(new OtherException("the data to be written is empty"));
			return;
		}
		if (mCharacteristic == null || (mCharacteristic.getProperties() & 0xc) == 0)
		{
			if (bleWriteCallback != null)
				bleWriteCallback.onWriteFailure(new OtherException("this characteristic not support write!"));
			return;
		}
		if (mCharacteristic.setValue(data))
		{
			handleCharacteristicWriteCallback(bleWriteCallback, uuid_write);
			if (!mBluetoothGatt.writeCharacteristic(mCharacteristic))
			{
				writeMsgInit();
				if (bleWriteCallback != null)
					bleWriteCallback.onWriteFailure(new OtherException("gatt writeCharacteristic fail"));
			}
		} else
		if (bleWriteCallback != null)
			bleWriteCallback.onWriteFailure(new OtherException("Updates the locally stored value of this characteristic fail"));
	}

	public void readCharacteristic(BleReadCallback bleReadCallback, String uuid_read)
	{
		if (mCharacteristic != null && (mCharacteristic.getProperties() & 2) > 0)
		{
			handleCharacteristicReadCallback(bleReadCallback, uuid_read);
			if (!mBluetoothGatt.readCharacteristic(mCharacteristic))
			{
				readMsgInit();
				if (bleReadCallback != null)
					bleReadCallback.onReadFailure(new OtherException("gatt readCharacteristic fail"));
			}
		} else
		if (bleReadCallback != null)
			bleReadCallback.onReadFailure(new OtherException("this characteristic not support read!"));
	}

	public void readRemoteRssi(BleRssiCallback bleRssiCallback)
	{
		handleRSSIReadCallback(bleRssiCallback);
		if (!mBluetoothGatt.readRemoteRssi())
		{
			rssiMsgInit();
			if (bleRssiCallback != null)
				bleRssiCallback.onRssiFailure(new OtherException("gatt readRemoteRssi fail"));
		}
	}

	public void setMtu(int requiredMtu, BleMtuChangedCallback bleMtuChangedCallback)
	{
		if (Build.VERSION.SDK_INT >= 21)
		{
			handleSetMtuCallback(bleMtuChangedCallback);
			if (!mBluetoothGatt.requestMtu(requiredMtu))
			{
				mtuChangedMsgInit();
				if (bleMtuChangedCallback != null)
					bleMtuChangedCallback.onSetMTUFailure(new OtherException("gatt requestMtu fail"));
			}
		} else
		if (bleMtuChangedCallback != null)
			bleMtuChangedCallback.onSetMTUFailure(new OtherException("API level lower than 21"));
	}

	private void handleCharacteristicNotifyCallback(BleNotifyCallback bleNotifyCallback, String uuid_notify)
	{
		if (bleNotifyCallback != null)
		{
			notifyMsgInit();
			bleNotifyCallback.setKey(uuid_notify);
			bleNotifyCallback.setHandler(mHandler);
			mBleBluetooth.addNotifyCallback(uuid_notify, bleNotifyCallback);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(17, bleNotifyCallback), BleManager.getInstance().getOperateTimeout());
		}
	}

	private void handleCharacteristicIndicateCallback(BleIndicateCallback bleIndicateCallback, String uuid_indicate)
	{
		if (bleIndicateCallback != null)
		{
			indicateMsgInit();
			bleIndicateCallback.setKey(uuid_indicate);
			bleIndicateCallback.setHandler(mHandler);
			mBleBluetooth.addIndicateCallback(uuid_indicate, bleIndicateCallback);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(33, bleIndicateCallback), BleManager.getInstance().getOperateTimeout());
		}
	}

	private void handleCharacteristicWriteCallback(BleWriteCallback bleWriteCallback, String uuid_write)
	{
		if (bleWriteCallback != null)
		{
			writeMsgInit();
			bleWriteCallback.setKey(uuid_write);
			bleWriteCallback.setHandler(mHandler);
			mBleBluetooth.addWriteCallback(uuid_write, bleWriteCallback);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(49, bleWriteCallback), BleManager.getInstance().getOperateTimeout());
		}
	}

	private void handleCharacteristicReadCallback(BleReadCallback bleReadCallback, String uuid_read)
	{
		if (bleReadCallback != null)
		{
			readMsgInit();
			bleReadCallback.setKey(uuid_read);
			bleReadCallback.setHandler(mHandler);
			mBleBluetooth.addReadCallback(uuid_read, bleReadCallback);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(65, bleReadCallback), BleManager.getInstance().getOperateTimeout());
		}
	}

	private void handleRSSIReadCallback(BleRssiCallback bleRssiCallback)
	{
		if (bleRssiCallback != null)
		{
			rssiMsgInit();
			bleRssiCallback.setHandler(mHandler);
			mBleBluetooth.addRssiCallback(bleRssiCallback);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(81, bleRssiCallback), BleManager.getInstance().getOperateTimeout());
		}
	}

	private void handleSetMtuCallback(BleMtuChangedCallback bleMtuChangedCallback)
	{
		if (bleMtuChangedCallback != null)
		{
			mtuChangedMsgInit();
			bleMtuChangedCallback.setHandler(mHandler);
			mBleBluetooth.addMtuChangedCallback(bleMtuChangedCallback);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(97, bleMtuChangedCallback), BleManager.getInstance().getOperateTimeout());
		}
	}

	public void notifyMsgInit()
	{
		mHandler.removeMessages(17);
	}

	public void indicateMsgInit()
	{
		mHandler.removeMessages(33);
	}

	public void writeMsgInit()
	{
		mHandler.removeMessages(49);
	}

	public void readMsgInit()
	{
		mHandler.removeMessages(65);
	}

	public void rssiMsgInit()
	{
		mHandler.removeMessages(81);
	}

	public void mtuChangedMsgInit()
	{
		mHandler.removeMessages(97);
	}
}
