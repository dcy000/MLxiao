// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleScanRuleConfig.java

package com.clj.fastble.scan;

import com.clj.fastble.BleManager;
import java.util.UUID;

public class BleScanRuleConfig
{
	public static class Builder
	{

		private UUID mServiceUuids[];
		private String mDeviceNames[];
		private String mDeviceMac;
		private boolean mAutoConnect;
		private boolean mFuzzy;
		private long mTimeOut;

		public Builder setServiceUuids(UUID uuids[])
		{
			mServiceUuids = uuids;
			return this;
		}

		public transient Builder setDeviceName(boolean fuzzy, String name[])
		{
			mFuzzy = fuzzy;
			mDeviceNames = name;
			return this;
		}

		public Builder setDeviceMac(String mac)
		{
			mDeviceMac = mac;
			return this;
		}

		public Builder setAutoConnect(boolean autoConnect)
		{
			mAutoConnect = autoConnect;
			return this;
		}

		public Builder setScanTimeOut(long timeOut)
		{
			mTimeOut = timeOut;
			return this;
		}

		void applyConfig(BleScanRuleConfig config)
		{
			config.mServiceUuids = mServiceUuids;
			config.mDeviceNames = mDeviceNames;
			config.mDeviceMac = mDeviceMac;
			config.mAutoConnect = mAutoConnect;
			config.mFuzzy = mFuzzy;
			config.mScanTimeOut = mTimeOut;
		}

		public BleScanRuleConfig build()
		{
			BleScanRuleConfig config = new BleScanRuleConfig();
			applyConfig(config);
			return config;
		}

		public Builder()
		{
			mServiceUuids = null;
			mDeviceNames = null;
			mDeviceMac = null;
			mAutoConnect = false;
			mFuzzy = false;
			mTimeOut = 10000L;
		}
	}


	private UUID mServiceUuids[];
	private String mDeviceNames[];
	private String mDeviceMac;
	private boolean mAutoConnect;
	private boolean mFuzzy;
	private long mScanTimeOut;

	public BleScanRuleConfig()
	{
		mServiceUuids = null;
		mDeviceNames = null;
		mDeviceMac = null;
		mAutoConnect = false;
		mFuzzy = false;
		mScanTimeOut = 10000L;
	}

	public UUID[] getServiceUuids()
	{
		return mServiceUuids;
	}

	public String[] getDeviceNames()
	{
		return mDeviceNames;
	}

	public String getDeviceMac()
	{
		return mDeviceMac;
	}

	public boolean isAutoConnect()
	{
		return mAutoConnect;
	}

	public boolean isFuzzy()
	{
		return mFuzzy;
	}

	public long getScanTimeOut()
	{
		return mScanTimeOut;
	}






}
