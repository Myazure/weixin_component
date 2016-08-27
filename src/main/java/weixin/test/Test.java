package weixin.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class Test {
	 public static void main(String[] args) throws SocketException {
	        System.out.println(getRealIp());
	        System.out.println(getLocalIp());
	    }
	 
	    public static String getRealIp() throws SocketException {
	        String localip = null;// 本地IP，如果没有配置外网IP则返回它
	        String netip = null;// 外网IP
	 
	        Enumeration<NetworkInterface> netInterfaces = 
	            NetworkInterface.getNetworkInterfaces();
	        InetAddress ip = null;
	        boolean finded = false;// 是否找到外网IP
	        while (netInterfaces.hasMoreElements() && !finded) {
	            NetworkInterface ni = netInterfaces.nextElement();
	            Enumeration<InetAddress> address = ni.getInetAddresses();
	            while (address.hasMoreElements()) {
	                ip = address.nextElement();
	                if (!ip.isSiteLocalAddress() 
	                        && !ip.isLoopbackAddress() 
	                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
	                    netip = ip.getHostAddress();
	                    finded = true;
	                    break;
	                } else if (ip.isSiteLocalAddress() 
	                        && !ip.isLoopbackAddress() 
	                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
	                    localip = ip.getHostAddress();
	                }
	            }
	        }
	     
	        if (netip != null && !"".equals(netip)) {
	            return netip;
	        } else {
	            return localip;
	        }
	    }
	    

	    public static String getLocalIp() throws SocketException {
	        String localip = null;
	        Enumeration<NetworkInterface> netInterfaces = 
	            NetworkInterface.getNetworkInterfaces();
	        InetAddress ip = null;
	        while (netInterfaces.hasMoreElements()  ) {
	            NetworkInterface ni = netInterfaces.nextElement();
	            Enumeration<InetAddress> address = ni.getInetAddresses();
	            while (address.hasMoreElements()) {
	                ip = address.nextElement();
	                	if (ip.isSiteLocalAddress() 
	                        && !ip.isLoopbackAddress() 
	                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
	                    localip = ip.getHostAddress();
	                    break;
	                }
	            }
	        }
	            return localip;
	    }
//	public static void main(String[] args) {
//		String OrderString = "{\"customer_id\":0,\"mat_id\":\"ff80808155c4205a0155c60285e00000\",\"order_info\":[{\"goods\":[{\"goods_id\":1206,\"goods_num\":1}],\"order_date\":1472117920067,\"order_id\":\"WX_16007d62-e035-4472-b706-46d0c85ffc74\",\"order_price\":0}]}";
////		String encodeString = "rdYMp7tegsn50EUUA20prNKXz6yOu1EOrhc6fM95zxOpPzRnc5kEPHnoCCDKV6cPVFPSMjZyUmWgRE9X7DGdcl+0SxjBcWmxFDiMw5QGqZb7p3SJn0P3JEBq/dJcG0o/A++KJ7EsM0/FlCZRpuIvSTEOKY6nFmhCQxP4T6PVIXjDkct0JFfgLvwWTiwL5Dx0ohoR3ijcobMAZps7LW7Oicy6LfeUY9p10Ey7f454L8xkdTZP13WhvKmm3Qiy3wqD+YfccwD3rIjamV7yBvVg1TXPTrsRDOtgwzwQ/PByLT9NO2oATe0nYw==";
////		String deCodeString = DESUtils.decode(encodeString);
//		String data="";
//		try {
//			  data = EncodeUtils.DESEncode(new String(OrderString.getBytes(), "ISO-8859-1"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(DESUtils.decode(data));
//		try {
//			System.out.println(new String(DESUtils.decode(data).getBytes(), "ISO-8859-1"));
//			System.out.println(new String(DESUtils.decode(data).getBytes(), "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		
//	}

}
