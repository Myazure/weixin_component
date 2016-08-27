package com.ysw.adsense.utils;

public class LocationUtils {

	/**
	 * check if xy1&xy2 in range
	 * 
	 * @author WangZhen
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param range
	 * @return
	 */
	public static boolean inRange(double x1, double y1, double x2, double y2, double range) {
		double radLat1 = x1 * Math.PI / 180.0;
		double radLat2 = x2 * Math.PI / 180.0;
		double a = radLat1 - radLat2;
		double b = y1 * Math.PI / 180.0 - y2 * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378.137d;
		s = Math.round(s * 10000d) / 10000d;
		s = s * 1000;
		if (s <= range) {
			return true;
		}
		return false;
	}

	public static double distance(double x, double y, double xx, double yy) {
		double x1 = x;
		double x2 = xx;
		double y1 = y;
		double y2 = yy;
		double radLat1 = x1 * Math.PI / 180.0;
		double radLat2 = x2 * Math.PI / 180.0;
		double a = radLat1 - radLat2;
		double b = y1 * Math.PI / 180.0 - y2 * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378.137d;
		s = Math.round(s * 10000d) / 10000d;
		s = s * 1000;
		return s;
	}

	public static double distance(String x, String y, String xx, String yy) {
		return distance(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(xx), Double.parseDouble(yy));
	}

	public static double distance(String x, String y, double xx, double yy) {
		return distance(Double.parseDouble(x), Double.parseDouble(y), xx, yy);
	}

	public static boolean inRange(String x1, String y1, String x2, String y2, String range) {
		return inRange(Double.valueOf(x1), Double.valueOf(y1), Double.valueOf(x2), Double.valueOf(y2), Double.valueOf(range));
	}

	public static boolean inRange(String x1, String y1, Double x2, Double y2, Double range) {
		return inRange(Double.parseDouble(x1), Double.parseDouble(y1), x2, y2, range);
	}

	public static boolean inRange(String x1, String y1, Double x2, Double y2, Integer range) {
		return inRange(Double.parseDouble(x1), Double.parseDouble(y1), x2, y2, range);
	}

	public static boolean inRange(Double x1, Double y1, String x2, String y2, Double range) {
		return inRange(x1, y1, Double.parseDouble(x2), Double.parseDouble(y2), range);
	}

	public static boolean inRange(Double x1, Double y1, String x2, String y2, Integer range) {
		return inRange(x1, y1, Double.parseDouble(x2), Double.parseDouble(y2), range);
	}

	public static boolean inRange(String x1, String y1, Double x2, Double y2, String range) {
		return inRange(Double.parseDouble(x1), Double.parseDouble(y1), x2, y2, Double.parseDouble(range));
	}

	public static boolean inRange(Double x1, Double y1, String x2, String y2, String range) {
		return inRange(x1, y1, Double.parseDouble(x2), Double.parseDouble(y2), Double.parseDouble(range));
	}

}
