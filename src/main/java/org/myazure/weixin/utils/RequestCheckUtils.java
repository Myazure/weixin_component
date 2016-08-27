package org.myazure.weixin.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.myazure.weixin.service.BASE;

/**
 * DES加密Hash的Request检查
 * 
 * @author WangZhen
 * @since v1.0.0
 */
public class RequestCheckUtils {
	/**
	 * 通过request生成token
	 * 
	 * @param request
	 * @return {@link String}
	 * @throws UnsupportedEncodingException
	 */
	public static String getTokenStringByRequest(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, String[]> paramerMap = request.getParameterMap();
		String sortString = "";
		sortString = new String(convertStringsMapToString(paramerMap).getBytes(
				request.getCharacterEncoding() == null ? "UTF-8" : request.getCharacterEncoding()), "UTF-8");
		return abstractString(encryptString(sortString));
	}

	/**
	 * 检查request是否符合token验证算法
	 * 
	 * @param request
	 * @return {@link Boolean}
	 * @throws UnsupportedEncodingException
	 */
	public static boolean checkRequest(HttpServletRequest request) throws UnsupportedEncodingException {
		if (request.getParameter(BASE.TOKEN_STRING) == null) {
			return false;
		}
		Map<String, String[]> paramerMap = request.getParameterMap();
		if (paramerMap == null) {
			return false;
		}
		String sortString = "";
		sortString = new String(convertStringsMapToString(paramerMap).getBytes(
				request.getCharacterEncoding() == null ? "UTF-8" : request.getCharacterEncoding()), "UTF-8");
		if (request.getParameter(BASE.TOKEN_STRING).equals(abstractString(encryptString(sortString)))) {
			return true;
		}
		return false;
	}

	/**
	 * 将String键String[]值的MAP转为paramsString
	 * 
	 * @param map
	 * @return {@link String}
	 */
	public static String convertStringsMapToString(Map<String, String[]> map) {
		Map<String, String> targetMap = new TreeMap<String, String>();
		for (Entry<String, String[]> entry : map.entrySet()) {
			if (entry.getKey().equals(BASE.TOKEN_STRING)) {
				continue;
			}
			targetMap.put(entry.getKey(), joinString(",", entry.getValue()));
		}
		return convertStringMapToString(targetMap);
	}

	/**
	 * 将Sring String Map 转化为paramsString
	 * 
	 * @param map
	 * @return {@link String}
	 */
	public static String convertStringMapToString(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return "";
		}
		Map<String, String> resaultMap = new TreeMap<String, String>(new MapComparator());
		resaultMap.putAll(map);
		StringBuilder string = new StringBuilder();
		if (resaultMap.size() > 0) {
			string.append("?");
		}
		for (Entry<String, String> entry : resaultMap.entrySet()) {
			string.append(entry.getKey());
			string.append("=");
			string.append(entry.getValue());
			string.append("&");
		}
		return string.toString();
	}

	/**
	 * 加密算法（可修改AES等都可以）
	 * 
	 * @param string
	 * @return {@link String}
	 */
	private static String encryptString(String string) {
		return DESUtils.encryptBasedDes(string);
	}

	/**
	 * 信息摘要算法（可修改MD5等都可以）
	 * 
	 * @param string
	 * @return {@link String}
	 */
	private static String abstractString(String string) {
		return string.hashCode() + "";
	}

	/**
	 * 将URL的queryString转成Map
	 * 
	 * @author WangZhen
	 * @param queryString
	 * @return {@link Map}
	 */
	public static Map<String, String> convertQueryStingToMap(String queryString) {
		Map<String, String> map = new TreeMap<String, String>();
		String urlString = queryString;
		if (queryString.startsWith("?")) {
			urlString = queryString.substring(1);
		}
		if (urlString != null && urlString.indexOf("&") > -1 && urlString.indexOf("=") > -1) {
			String[] arrTemp = urlString.split("&");
			for (String str : arrTemp) {
				String[] qs = str.split("=");
				map.put(qs[0], qs[1]);
			}
		}
		return map;
	}

	/**
	 * 通过URL的queryString获得TokenString
	 * 
	 * @author WangZhen
	 * @param queryString
	 * @return {@link String}
	 */
	public static String getTokenStringByQueryString(String queryString) {
		Map<String, String> queryStringMap = convertQueryStingToMap(queryString);
		if (queryStringMap.containsKey(BASE.TOKEN_STRING)) {
			queryStringMap.remove(BASE.TOKEN_STRING);
		}
		return abstractString(encryptString(convertStringMapToString(queryStringMap)));
	}

	/**
	 * 在URL最后加上tokenString
	 * 
	 * @author WangZhen
	 * @param urlString
	 * @return {@link String}
	 */
	public static String getFullURLStringByURLString(String urlString) {
		String targetString = "";
		if (urlString != null && urlString.indexOf("&") > -1 && urlString.indexOf("=") > -1 && urlString.indexOf("?") > -1) {
			String paramString = urlString.substring(urlString.indexOf("?"));
			String URI = urlString.substring(0, urlString.indexOf("?"));
			Map<String, String> params = convertQueryStingToMap(paramString);
			params.remove(BASE.TOKEN_STRING);
			targetString = URI + convertStringMapToString(params) + "&" + BASE.TOKEN_STRING + "=" + getTokenStringByQueryString(paramString);
			return targetString;
		}
		return urlString;
	}

	/**
	 * 兼容java1.8的String的join方法
	 * 
	 * @param join
	 * @param strAry
	 * @return
	 */
	public static String joinString(String join, String[] strAry) {
		StringBuffer sb = new StringBuffer();
		if (strAry.length > 1) {
			sb.append("[");
		}
		for (int i = 0; i < strAry.length; i++) {
			if (i == (strAry.length - 1)) {
				sb.append(strAry[i]);
			} else {
				sb.append(strAry[i]).append(join);
			}
		}
		if (strAry.length > 1) {
			sb.append("]");
		}
		return sb.toString();
	}

}
