// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   HexUtil.java

package com.clj.fastble.utils;


public class HexUtil
{

	private static final char DIGITS_LOWER[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'a', 'b', 'c', 'd', 'e', 'f'
	};
	private static final char DIGITS_UPPER[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F'
	};

	public HexUtil()
	{
	}

	public static char[] encodeHex(byte data[])
	{
		return encodeHex(data, true);
	}

	public static char[] encodeHex(byte data[], boolean toLowerCase)
	{
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	protected static char[] encodeHex(byte data[], char toDigits[])
	{
		if (data == null)
			return null;
		int l = data.length;
		char out[] = new char[l << 1];
		int i = 0;
		int j = 0;
		for (; i < l; i++)
		{
			out[j++] = toDigits[(0xf0 & data[i]) >>> 4];
			out[j++] = toDigits[0xf & data[i]];
		}

		return out;
	}

	public static String encodeHexStr(byte data[])
	{
		return encodeHexStr(data, true);
	}

	public static String encodeHexStr(byte data[], boolean toLowerCase)
	{
		return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	protected static String encodeHexStr(byte data[], char toDigits[])
	{
		return new String(encodeHex(data, toDigits));
	}

	public static String formatHexString(byte data[])
	{
		return formatHexString(data, false);
	}

	public static String formatHexString(byte data[], boolean addSpace)
	{
		if (data == null || data.length < 1)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++)
		{
			String hex = Integer.toHexString(data[i] & 0xff);
			if (hex.length() == 1)
				hex = (new StringBuilder()).append('0').append(hex).toString();
			sb.append(hex);
			if (addSpace)
				sb.append(" ");
		}

		return sb.toString().trim();
	}

	public static byte[] decodeHex(char data[])
	{
		int len = data.length;
		if ((len & 1) != 0)
			throw new RuntimeException("Odd number of characters.");
		byte out[] = new byte[len >> 1];
		int i = 0;
		for (int j = 0; j < len;)
		{
			int f = toDigit(data[j], j) << 4;
			j++;
			f |= toDigit(data[j], j);
			j++;
			out[i] = (byte)(f & 0xff);
			i++;
		}

		return out;
	}

	protected static int toDigit(char ch, int index)
	{
		int digit = Character.digit(ch, 16);
		if (digit == -1)
			throw new RuntimeException((new StringBuilder()).append("Illegal hexadecimal character ").append(ch).append(" at index ").append(index).toString());
		else
			return digit;
	}

	public static byte[] hexStringToBytes(String hexString)
	{
		if (hexString == null || hexString.equals(""))
			return null;
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char hexChars[] = hexString.toCharArray();
		byte d[] = new byte[length];
		for (int i = 0; i < length; i++)
		{
			int pos = i * 2;
			d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}

		return d;
	}

	public static byte charToByte(char c)
	{
		return (byte)"0123456789ABCDEF".indexOf(c);
	}

	public static String extractData(byte data[], int position)
	{
		return formatHexString(new byte[] {
			data[position]
		});
	}

}
