package com.zane.androidupnpdemo.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 电子病历加密参数
 * 
 * @author txy
 *
 */
public class AesUtil {
	public static final String KeyCode="_AesCryptography";
	/**
	 * 采用 AES-128-ECB zreopadding加密
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String encryptParam(String param, String sKey) throws Exception {

		byte[] raw = sKey.getBytes("utf-8");

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES"); // 采用AES 加密

		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"

		// 补齐0 修改补码方式为zreopadding
		int blockSize = cipher.getBlockSize();
		byte[] dataBytes = param.getBytes();
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}
		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(plaintext);

		return Base64Utils.encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	public static String desEncrypt(String param, String sKey) throws Exception {
		try {
			param = param.replace(" ", "+");
			byte[] encrypted1 = Base64Utils.decode(param);

			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes(), "AES");

			cipher.init(Cipher.DECRYPT_MODE, keyspec);

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			return originalString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * base64后在url加密
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String urlEncode(String param, String sKey) throws Exception {
		System.out.println("参数：" + param);
		System.out.println("--------");
		String str = encryptParam(param, sKey);
		System.out.println("64位加密：" + str);
		System.out.println("--------");
		System.out.println("urlEncode加密：" + URLEncoder.encode(str, "utf-8"));
		System.out.println("--------");
		return URLEncoder.encode(str, "utf-8");
	}
	
	public static String urlDecode(String param, String sKey)throws Exception{
		String decodeStr  = URLDecoder.decode(param, "utf-8");
		return desEncrypt(decodeStr, sKey);
	}

}
