package com.ysw.adsense.utils;

import java.util.Comparator;

/**
 * Map比较器
 * 
 * @author WangZhen
 *
 */
public class MapComparator implements Comparator<String> {
	public int compare(String me1, String me2) {
		return me1.compareTo(me2);
	}
}
