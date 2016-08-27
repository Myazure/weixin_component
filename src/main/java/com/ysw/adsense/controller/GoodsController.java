package com.ysw.adsense.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.ysw.adsense.ip.response.StatusResponse;
import com.ysw.adsense.utils.EncodeUtils;

/**
 * IPserver交互相关
 * 
 * @author WangZhen
 *
 */

@Controller
public class GoodsController {
	private static final Logger LOG = LoggerFactory.getLogger(GoodsController.class);
	final static int BUFFER_SIZE = 4096;

	@Autowired
	private StringRedisTemplate redisTemplate;

	// TODO : no login
	@RequestMapping(path = "/checkgoods", method = RequestMethod.POST)
	public void checkGoods(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("ISO-8859-1");
		response.setCharacterEncoding("ISO-8859-1");
//		System.out.println("req:"+request.getHeader("Content-Type"));
//		System.out.println("rsp:"+response.getHeader("Content-Type"));
		InputStream is;
		byte[] b = new byte[request.getContentLength()];
		try {
			is = request.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int count = -1;
			while ((count = is.read(b, 0, BUFFER_SIZE)) != -1)
				outStream.write(b, 0, count);
			String encodeString = "";
			String decodeString = "";
			encodeString = new String(outStream.toByteArray(), "ISO-8859-1");
			try {
				String key = EncodeUtils.MD5Encode(encodeString);
				String orderId = redisTemplate.opsForValue().get(key);
				decodeString=EncodeUtils.DESDecode(encodeString);
				if (decodeString==null||decodeString=="") {
					LOG.debug("validation failed key null :"+key);
					StatusResponse statusResponse = new StatusResponse("validation failed", 0, true);
//					StatusResponse statusResponse = new StatusResponse("validation failed", 2, false);
					response.setCharacterEncoding(request.getCharacterEncoding());
					response.setContentType(request.getContentType());
					sentResponse(response, statusResponse);
					return;
				}
				if (orderId == null) {
					LOG.debug("validation failed key null :"+key);
					StatusResponse statusResponse = new StatusResponse("validation failed", 0, true);
//					StatusResponse statusResponse = new StatusResponse("validation failed", 2, false);
					response.setCharacterEncoding(request.getCharacterEncoding());
					response.setContentType(request.getContentType());
					sentResponse(response, statusResponse);
					return;
				} else {
					LOG.debug("validation successful id:"+key);
					redisTemplate.delete(key);
					StatusResponse statusResponse = new StatusResponse("validation successful", 0, true);
					response.setCharacterEncoding(request.getCharacterEncoding());
					response.setContentType(request.getContentType());
					sentResponse(response, statusResponse);
					return;
				}
			} catch (Exception e) {
				LOG.debug("validation failed");
				LOG.debug(e.getMessage());
				e.printStackTrace();
				StatusResponse statusResponse = new StatusResponse("validation failed", 0, true);
//				StatusResponse statusResponse = new StatusResponse("validation failed",1, false);
				response.setCharacterEncoding(request.getCharacterEncoding());
				response.setContentType(request.getContentType());
				sentResponse(response, statusResponse);
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return;
	}

	protected void sentResponse(HttpServletResponse response, Object object) {
		try {
			response.getWriter().write(JSON.toJSONString(object));
			response.getWriter().close();
			return;
		} catch (IOException e) {
			LOG.debug(e.getMessage());
			e.printStackTrace();
		}
	}
}
