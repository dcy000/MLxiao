// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BleLruHashMap.java

package com.clj.fastble.utils;

import com.clj.fastble.bluetooth.BleBluetooth;
import java.util.*;

public class BleLruHashMap extends LinkedHashMap
{

	private final int MAX_SIZE;

	public BleLruHashMap(int saveSize)
	{
		super((int)Math.ceil((double)saveSize / 0.75D) + 1, 0.75F, true);
		MAX_SIZE = saveSize;
	}

	protected boolean removeEldestEntry(Entry eldest)
	{
		if (size() > MAX_SIZE && (eldest.getValue() instanceof BleBluetooth))
			((BleBluetooth)eldest.getValue()).disconnect();
		return size() > MAX_SIZE;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		Entry entry;
		for (Iterator iterator = entrySet().iterator(); iterator.hasNext(); sb.append(String.format("%s:%s ", new Object[] {
	entry.getKey(), entry.getValue()
})))
			entry = (Entry)iterator.next();

		return sb.toString();
	}
}
