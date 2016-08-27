package com.ysw.adsense.ip;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.ysw.adsense.configuration.AppUrlService;
import com.ysw.adsense.ip.request.GoodsReq;
import com.ysw.adsense.ip.request.LockReq;
import com.ysw.adsense.ip.request.OrderReq;
import com.ysw.adsense.ip.response.GoodsJsonResponse;
import com.ysw.adsense.ip.response.SingleGoodsResponse;
import com.ysw.adsense.ip.response.StatusResponse;
import com.ysw.adsense.utils.DESUtils;
import com.ysw.adsense.utils.EncodeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter;

/**
 * @author : csyangchsh@gmail.com
 */
@Service("ipManagementClient")
public class IPManagementClientImpl implements IPManagementClient {

	private RestTemplate restTemplate;

	private AppUrlService urlService;
	private String productListUrl;
	private String productInfoUrl;
	private String matLockUrl;
	private String matLockUri;
	private String orderUri;
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	public IPManagementClientImpl(AppUrlService urlService, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.urlService = urlService;
		
		
		
		
		this.productListUrl = this.urlService.getIpUrl() + "/platform/coopertive-matProductList.json?" + "mat_id={mat_id}";
		this.productInfoUrl = this.urlService.getIpUrl() + "/platform/coopertive-matProductInfo.json?mat_id={mat_id}&goods_id={goods_id}";
		
		
		
		this.matLockUrl = this.urlService.getIpUrl()
				+ "/platform/coopertive-matLockAmount.json?mat_id={mat_id}&type={type}&goods[0].number={goods_num}&goods[0].id={goods_id}";
		this.matLockUri = this.urlService.getIpUrl() + "/platform/coopertive-matLockAmount.json";
		this.orderUri = this.urlService.getIpUrl() + "/platform/coopertive-customerOrder.json";
	}

	@Override
	public GoodsJsonResponse getAllGoodsOfMat(String mat_id) {
		GoodsJsonResponse response = new GoodsJsonResponse();
		try {
			response = JSON.parseObject(restTemplate.getForObject(productListUrl, String.class, mat_id), GoodsJsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		return response;
	}

	@Override
	public SingleGoodsResponse getGoodsOfMat(String mat_id, long goodsId) {
		SingleGoodsResponse response = new SingleGoodsResponse();
		try {
			response = JSON.parseObject(restTemplate.getForObject(productInfoUrl, String.class, mat_id, goodsId), SingleGoodsResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		return response;
	}
	

	@Override
	public StatusResponse getLockGoods(LockReq lockReq) {
		StatusResponse response = new StatusResponse();
		try {
			response = JSON.parseObject(restTemplate.getForObject(matLockUrl,  String.class,lockReq.getMatId(),lockReq.getType(),lockReq.getGoods().get(0).getGoods_num(),lockReq.getGoods().get(0).getGoods_id()), StatusResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		return response;
	}


	@Override
	public StatusResponse lockGoods(LockReq lockReq) {
		StatusResponse response = new StatusResponse();
		String data = "";
		try {
			data = new String(JSON.toJSONString(lockReq).getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return response;
		}
		try {
			response = JSON.parseObject(restTemplate.postForObject(matLockUri, data, String.class), StatusResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		return response;
	}

	@Override
	public StatusResponse customerOrder(OrderReq orderReq) {
		StatusResponse response = new StatusResponse();
		response.setMessage("测试成功数据");
		response.setSuccess(true);
		// String fakeurlString = "http://localhost:8083/checkgoods";
		String data;
		try {
			 System.err.println("OrderString:");
			 System.err.println(JSON.toJSONString(orderReq));
			data = EncodeUtils.DESEncode(new String(JSON.toJSONString(orderReq).getBytes(), "ISO-8859-1"));
			 System.err.println("encodeString:");
			 System.err.println(data);
			String key = EncodeUtils.MD5Encode(data);
			
			System.out.println("data:"+DESUtils.decode(data));
			
			System.err.println("key:"+key);
			
			redisTemplate.opsForValue().set(key, orderReq.getOrder_info().get(0).getOrder_id());
			redisTemplate.expire(key, 1, TimeUnit.MINUTES);
//			String responseString = restTemplate.postForObject(orderUri, data, String.class);
//			 System.err.println(responseString);
//			  response=JSON.parseObject(responseString, StatusResponse.class);
			  return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		// return JSON.parseObject(restTemplate.postForObject(orderUri, data,
		// String.class), StatusResponse.class);
	}

	@Override
	public SingleGoodsResponse getGoodsOfMat(GoodsReq goodsReq) {
		return getGoodsOfMat(goodsReq.getMat_id(), goodsReq.getGoodsId());
	}
}
