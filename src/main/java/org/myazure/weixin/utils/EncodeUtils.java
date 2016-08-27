package org.myazure.weixin.utils;

import java.security.MessageDigest;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.alibaba.fastjson.JSON;

public class EncodeUtils {
	/**
	 * MD5 ENCODE
	 * 
	 * @param strSrc
	 * @param key
	 * @return
	 */
	public static String MD5Encode(String string) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] bytes = string.getBytes();
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(bytes);
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	public static String DESEncode(String string){
		return DESUtils.encode(string);
	}
	public static String DESEncode(Object object){
		return DESEncode(JSON.toJSONString(object));
	}
	
	public static String DESDecode(String string){
		return DESUtils.decode(string);
	}
	public static String DESDecode(Object object){
		return DESDecode(JSON.toJSONString(object));
	}
}
