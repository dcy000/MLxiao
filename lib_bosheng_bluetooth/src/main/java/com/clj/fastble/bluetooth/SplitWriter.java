// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SplitWriter.java

package com.clj.fastble.bluetooth;

import android.os.*;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleMsg;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.exception.OtherException;
import com.clj.fastble.utils.BleLog;
import java.util.LinkedList;
import java.util.Queue;

// Referenced classes of package com.clj.fastble.bluetooth:
//			BleBluetooth, BleConnector

public class SplitWriter
{

	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private BleBluetooth mBleBluetooth;
	private String mUuid_service;
	private String mUuid_write;
	private byte mData[];
	private int mCount;
	private BleWriteCallback mCallback;
	private Queue mDataQueue;
	private int mTotalNum;

	public SplitWriter()
	{
		mHandlerThread = new HandlerThread("splitWriter");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()) {

			final SplitWriter this$0;

			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				if (msg.what == 51)
					write();
			}

			
			{
				this.this$0 = SplitWriter.this;
				super(x0);
			}
		};
	}

	public void splitWrite(BleBluetooth bleBluetooth, String uuid_service, String uuid_write, byte data[], BleWriteCallback callback)
	{
		mBleBluetooth = bleBluetooth;
		mUuid_service = uuid_service;
		mUuid_write = uuid_write;
		mData = data;
		mCount = BleManager.getInstance().getSplitWriteNum();
		mCallback = callback;
		splitWrite();
	}

	private void splitWrite()
	{
		if (mData == null)
			throw new IllegalArgumentException("data is Null!");
		if (mCount < 1)
		{
			throw new IllegalArgumentException("split count should higher than 0!");
		} else
		{
			mDataQueue = splitByte(mData, mCount);
			mTotalNum = mDataQueue.size();
			write();
			return;
		}
	}

	private void write()
	{
		if (mDataQueue.peek() == null)
		{
			release();
		} else
		{
			byte data[] = (byte[])mDataQueue.poll();
			mBleBluetooth.newBleConnector().withUUIDString(mUuid_service, mUuid_write).writeCharacteristic(data, new BleWriteCallback() {

				final SplitWriter this$0;

				public void onWriteSuccess(int current, int total, byte justWrite[])
				{
					int position = mTotalNum - mDataQueue.size();
					if (mCallback != null)
						mCallback.onWriteSuccess(position, mTotalNum, justWrite);
					if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper())
					{
						write();
					} else
					{
						Message message = mHandler.obtainMessage(51);
						mHandler.sendMessage(message);
					}
				}

				public void onWriteFailure(BleException exception)
				{
					if (mCallback != null)
						mCallback.onWriteFailure(new OtherException((new StringBuilder()).append("exception occur while writing: ").append(exception.getDescription()).toString()));
				}

			
			{
				this.this$0 = SplitWriter.this;
				super();
			}
			}, mUuid_write);
		}
	}

	private void release()
	{
		mHandlerThread.quit();
		mHandler.removeCallbacksAndMessages(null);
	}

	private static Queue splitByte(byte data[], int count)
	{
		if (count > 20)
			BleLog.w("Be careful: split count beyond 20! Ensure MTU higher than 23!");
		Queue byteQueue = new LinkedList();
		if (data != null)
		{
			int index = 0;
			do
			{
				byte rawData[] = new byte[data.length - index];
				System.arraycopy(data, index, rawData, 0, data.length - index);
				byte newData[];
				if (rawData.length <= count)
				{
					newData = new byte[rawData.length];
					System.arraycopy(rawData, 0, newData, 0, rawData.length);
					index += rawData.length;
				} else
				{
					newData = new byte[count];
					System.arraycopy(data, index, newData, 0, count);
					index += count;
				}
				byteQueue.offer(newData);
			} while (index < data.length);
		}
		return byteQueue;
	}





}
