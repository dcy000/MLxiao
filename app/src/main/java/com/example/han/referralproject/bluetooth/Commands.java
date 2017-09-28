package com.example.han.referralproject.bluetooth;

import android.text.format.Time;

public class Commands {

	private Time time = null;

	/**
	 * 包头
	 */
	public final static byte CMD_HEAD = 0x5A;
	/**
	 * 包类别
	 */
	public final static byte CMD_CATEGORY_ZERO = 0x00;
	public final static byte CMD_CATEGORY_ONE = 0x01;
	public final static byte CMD_CATEGORY_TWO = 0x02;
	public final static byte CMD_CATEGORY_THReE = 0x03;
	public final static byte CMD_CATEGORY_FOUR = 0x04;
	public final static byte CMD_CATEGORY_FIVE = 0x05;
	public final static byte CMD_CATEGORY_SIX = 0x06;
	/**
	 * 包长
	 */
	public final static byte CMD_LENGTH_TEN = 0x0A;
	public final static byte CMD_LENGTH_ELEVEN = 0x0B;
	public final static byte CMD_LENGTH_TWELVE = 0x0C;
	public final static byte CMD_LENGTH_THIRTEEN = 0x0D;
	public final static byte CMD_LENGTHY_FOURTEEN = 0x0E;
	public final static byte CMD_LENGTH_FIFTEEN = 0x0F;
	public final static byte CMD_SIXTEEN_THIRTEEN = 0x10;

	public final static byte[] datas = {0x5A, 0x0A, 0x03, 0x10, 0x05, 0x02, 0x0F, 0x21, 0x3B, (byte)0xEB};
	public final static byte[] xueyangDatas = {(byte)0xAA, 0x55, 0x0F, 0x03, (byte) 0x84, 0x01, (byte)0xE0};
	/**
	 * 血压
	 */
	byte[] cmdData = new byte[] {};

	public byte[] getSystemdate(byte cmdStart, byte cmdLength, byte cmdSort) {
		byte cmdCheck = 0;

		time = new Time();
		time.setToNow();
		cmdData = new byte[cmdLength];
		cmdData[0] = (byte) cmdStart;
		cmdData[1] = (byte) cmdLength;
		cmdData[2] = (byte) cmdSort;
		cmdData[3] = (byte) (time.year - 2000);
		cmdData[4] = (byte) ((byte) time.month + 1);
		cmdData[5] = (byte) time.monthDay;
		cmdData[6] = (byte) time.hour;
		cmdData[7] = (byte) time.minute;
		cmdData[8] = (byte) time.second;

		for (int i = 0; i < cmdData.length; i++) {
			cmdCheck += cmdData[i];
		}
		cmdData[9] = (byte) (cmdCheck + 2);
		// cmdData[9] = 0;
		// cmdData[10] = 0;cmdData= new byte[] {90, 11, 5, 15, 6, 1, 8, 56,
		// (byte) 192,0,0};
		return cmdData;
	}
}
