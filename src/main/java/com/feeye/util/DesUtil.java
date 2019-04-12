package com.feeye.util;

import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DesUtil {

	private final static String DES = "DES";
	public static final String ENCODING_GBK = "GBK";

	/**
	 * Description 根据键值进行加密
	 *
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	}

	/**
	 * Description 根据键值进行解密
	 *
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
//	public static void main(String[] args) {
//		String data = "J027zBOF/VaixyUxS6LwzN1Fff6nNXkWoRjlaIXiJWdtNlApiCFEWrRAm1Rmmcun9AiLbYsn3B3B\n" +
//				"eEVRoOFfn202UCmIIURaXxIT0xk1rbRstHLqNmVuyTsTv+dD9mPGt8Gmd/WMxUU/GpqAcHemuQ==";
//		decrypt(data, )
//	}
	public static String decrypt(String data, String key) throws IOException, Exception {
		if (data == null) {
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = decoder.decodeBuffer(data);
		byte[] bt = decrypt(buf, key.getBytes());
		return new String(bt);
	}

	/**
	 * ECB解密,不要IV
	 *
	 * @param message Base64编码的密文
	 * @param key     密钥
	 * @return 明文
	 * @throws Exception
	 */
	public static String des3DecodeECB(String message, String key) throws Exception {
		Key deskey;
		byte[] keyBytes = key.getBytes(ENCODING_GBK);
		byte[] data = new BASE64Decoder().decodeBuffer(message);

		DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return new String(bOut, ENCODING_GBK);
	}

	/**
	 * ECB加密,不要IV
	 *
	 * @param message 明文
	 * @param key     密钥
	 * @return Base64编码的密文
	 * @throws Exception
	 */
	public static String des3EncodeECB(String message, String key) throws Exception {
		Key deskey;
		byte[] keyBytes = key.getBytes(ENCODING_GBK);
		byte[] data = message.getBytes(ENCODING_GBK);

		DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return new BASE64Encoder().encode(bOut);
	}


	/**
	 * Description 根据键值进行加密
	 *
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		/* 生成一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();

        /* 从原始密钥数据创建DESKeySpec对象 */
		DESKeySpec dks = new DESKeySpec(key);

        /* 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象 */
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

        /* Cipher对象实际完成加密操作 */
		Cipher cipher = Cipher.getInstance(DES);

        /* 用密钥初始化Cipher对象 */
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 *
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		/* 生成一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();

        /* 从原始密钥数据创建DESKeySpec对象 */
		DESKeySpec dks = new DESKeySpec(key);

        /* 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象 */
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

        /* Cipher对象实际完成解密操作 */
		Cipher cipher = Cipher.getInstance(DES);

        /* 用密钥初始化Cipher对象 */
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/***
	 * 将字符串格式化为>=8字节长度的字节数组，不足补0
	 *
	 * @param str
	 * @return
	 */
	private static byte[] format(String str) {
		byte[] bt = str.getBytes();
		if (bt.length < 8) {
			byte[] btnew = new byte[8];
			for (int i = 0; i < bt.length; i++) {
				btnew[i] = bt[i];
			}
			for (int i = bt.length; i < 8; i++) {
				btnew[i] = 0;
			}
			return btnew;
		}
		return bt;
	}
}
