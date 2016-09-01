package org.myazure.weixin.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;


public class DESUtils {

	@Value("${adsense.DESUtils.DES_KEY}")
	private static byte[] DES_KEY={ 21, 1, -110, 82, -32, -85, -128,-65 };

	/**
	 * 
	 * 数据加密，算法（DES）
	 * 
	 * @author WangZhen
	 * 
	 * @param data
	 *            要进行加密的数据
	 * @return 加密后的数据
	 */
	public static String encryptBasedDes(String data) {
		String encryptedData = "";
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec deskey = new DESKeySpec(DES_KEY);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(deskey);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedData;
	}

	/**
	 * 
	 * 数据解密，算法（DES）
	 * 
	 * @author WangZhen
	 * 
	 * @param cryptData
	 *            加密数据
	 * @return 解密后的数据
	 */
	public static String decryptBasedDes(String cryptData) {
		String decryptedData = null;
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec deskey = new DESKeySpec(DES_KEY);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(deskey);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
		} catch (Exception e) {
			throw new RuntimeException("DecryptErrorInfo：", e);
		}
		return decryptedData;
	}

	public static String encode(Object object) {
		String dataString = "";
		try {
			dataString = encryptBasedDes(JSON.toJSONString(object));
			return dataString;
		} catch (Exception e) {
			e.printStackTrace();
			return dataString;
		}
	}

	public static String decode(String string) {
		String dataString = string;
		try {
			dataString = decryptBasedDes(string);
			return dataString;
		} catch (Exception e) {
			e.printStackTrace();
			return dataString;
		}
	}


}
