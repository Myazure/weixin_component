package org.myazure.weixin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myazure.weixin.MyazureWeixinAPI;
import org.myazure.weixin.configuration.AppUrlService;
import org.myazure.weixin.domain.MaOfficialAccount;
import org.myazure.weixin.handlers.AuthorizeHandler;
import org.myazure.weixin.processor.SendMassMessage;
import org.myazure.weixin.request.MassSendReq;
import org.myazure.weixin.response.StatusResponse;
import org.myazure.weixin.service.MaOfficialAccountService;
import org.myazure.weixin.service.MaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.MusicMessage;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.message.message.NewsMessage.Article;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.AesException;

/**
 * @author WangZhen Date: 16-07-08
 * @author WangZhen <wangzhenjjcn@gmail.com> Date 16-09-20
 */
@Controller
public class MassSendController {
	private static final Logger LOG = LoggerFactory.getLogger(MassSendController.class);
	@Autowired
	private MyazureWeixinAPI myazureWeixinAPI;
	@Autowired
	private AppUrlService urlService;
	@Autowired
	private MaUserService userService;
	@Autowired
	private MaOfficialAccountService maOfficialAccountService;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private AuthorizeHandler authorizeHandler;

	@RequestMapping(path = "/public/masssend", method = RequestMethod.POST)
	public void publicMassSendTextMsg(HttpServletRequest request, HttpServletResponse response) throws IOException, AesException {
		StatusResponse result = new StatusResponse();
		MassSendReq massSendReq = null;
		InputStream is;
		byte[] b = new byte[request.getContentLength()];
		request.getInputStream();
		try {
			is = request.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int count = -1;
			while ((count = is.read(b, 0, 4096)) != -1)
				outStream.write(b, 0, count);
			String requestString = "";
			requestString = new String(outStream.toByteArray(), "ISO-8859-1");
			massSendReq = JSON.parseObject(requestString, MassSendReq.class);
			LOG.debug("[MyazureWeixin]: Public MassSend Request Recieve sucess!");
		} catch (Exception e) {
			LOG.debug("[MyazureWeixin]: Public MassSend Request Recieve failed!");
			result.setCode(1);
			result.setSuccess(false);
			result.setMessage(result.getMessage().replace("成功", "失败") + " " + "Recieve request data failed");
			response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
			LOG.debug("[MyazureWeixin]: Failed MSG:[{}]", result.getMessage());
			return;
		}
		if (massSendReq == null || massSendReq.equals(new MassSendReq())) {
			result.setCode(2);
			result.setSuccess(false);
			result.setMessage(result.getMessage().replace("成功", "失败") + " " + "Recieve request data null");
			response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
			LOG.debug("[MyazureWeixin]: Failed MSG:[{}]", result.getMessage());
			return;
		}
		if (massSendReq.getAppid() == null || massSendReq.getAppid().isEmpty()) {
			result.setCode(3);
			result.setSuccess(false);
			result.setMessage(result.getMessage().replace("成功", "失败") + " " + "Recieve request Appid null");
			response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
			LOG.debug("[MyazureWeixin]: Failed MSG:[{}]", result.getMessage());
			return;
		}
		String appidString = massSendReq.getAppid();
		MaOfficialAccount oa = maOfficialAccountService.findByAppId(appidString);
		if (oa == null) {
			result.setCode(4);
			result.setSuccess(false);
			result.setMessage(result.getMessage().replace("成功", "失败") + " " + "OfficialAccount Not Authed");
			response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
			LOG.debug("[MyazureWeixin]: Failed MSG:[{}]", result.getMessage());
			return;
		}
		if (massSendReq.getMsgType() == null) {
			result.setCode(5);
			result.setSuccess(false);
			result.setMessage(result.getMessage().replace("成功", "失败") + " " + "Recieve request MsgType null");
			response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
			LOG.debug("[MyazureWeixin]: Failed MSG:[{}]", result.getMessage());
			return;
		}
		SendMassMessage sender = null;
		Message message = null;
		switch (massSendReq.getMsgType()) {
		case MassSendReq.newsMessage:
			if (massSendReq.getToUser() == null) {
				result.setMessage(result.getMessage() + " " + "toUser Not Found");
				result.setCode(22);
				break;
			}
			if (massSendReq.getTitle() == null) {
				result.setMessage(result.getMessage() + " " + "tittleString Not Found");
				result.setCode(23);
				break;
			}
			if (massSendReq.getDescription() == null) {
				result.setMessage(result.getMessage() + " " + "descriptionString Not Found");
				result.setCode(24);
				break;
			}
			if (massSendReq.getNewsUrl() == null) {
				result.setMessage(result.getMessage() + " " + "urlString Not Found");
				result.setCode(25);
				break;
			}
			if (massSendReq.getThumbUrl() == null) {
				result.setMessage(result.getMessage() + " " + "pictureURLString Not Found");
				result.setCode(26);
				break;
			}
			List<Article> articles = new ArrayList<NewsMessage.Article>();
			NewsMessage.Article ariticle = new NewsMessage.Article(massSendReq.getTitle(), massSendReq.getDescription(), massSendReq.getNewsUrl(),
					massSendReq.getThumbUrl());
			articles.add(ariticle);
			message = new NewsMessage(massSendReq.getToUser(), articles);
			break;
		case MassSendReq.musicMessage:
			if (massSendReq.getToUser() == null) {
				result.setMessage(result.getMessage() + " " + "toUser Not Found");
				result.setCode(22);
				break;
			}
			if (massSendReq.getTitle() == null) {
				result.setMessage(result.getMessage() + " " + "tittleString Not Found");
				result.setCode(23);
				break;
			}
			if (massSendReq.getDescription() == null) {
				result.setMessage(result.getMessage() + " " + "descriptionString Not Found");
				result.setCode(24);
				break;
			}
			if (massSendReq.getMusicUrl() == null) {
				result.setMessage(result.getMessage() + " " + "MusicUrlString Not Found");
				result.setCode(25);
				break;
			}
			if (massSendReq.getHqMusicUrl() == null) {
				result.setMessage(result.getMessage() + " " + "MusicHqUrl Not Found");
				result.setCode(26);
				break;
			}
			if (massSendReq.getThumb_media_id() == null) {
				result.setMessage(result.getMessage() + " " + "Thumb_media_id Not Found");
				result.setCode(27);
				break;
			}
			MusicMessage.Music music = new MusicMessage.Music(massSendReq.getTitle(), massSendReq.getDescription(), massSendReq.getMusicUrl(),
					massSendReq.getHqMusicUrl(), massSendReq.getThumb_media_id());
			message = new MusicMessage(massSendReq.getToUser(), music);
			result.setMessage(result.getMessage() + " " + "musicMessage Send sucess");
			break;
		case MassSendReq.imageMessage:
			result.setMessage(result.getMessage() + " " + "imageMessage Send sucess");
			break;
		case MassSendReq.textMessage:
			result.setMessage(result.getMessage() + " " + "textMessage Send sucess");
			break;
		case MassSendReq.vedioMessage:
			result.setMessage(result.getMessage() + " " + "vedioMessage Send sucess");
			break;
		case MassSendReq.voiceMessage:
			result.setMessage(result.getMessage() + " " + "voiceMessage Send sucess");
			break;
		default:
			result.setCode(9);
			result.setMessage(result.getMessage() + " Unknow msgType:" + massSendReq.getMsgType());
			break;
		}

		if (result.getCode() == 0) {
			result.setSuccess(true);
			response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
			LOG.debug("[MyazureWeixin]: SUCESS MSG:[{}]", result.getMessage());
			sender = new SendMassMessage(message);
			LOG.debug("[MyazureWeixin]: Start Send Processor:[{}]", System.currentTimeMillis());
			new Thread(sender).start();
			LOG.debug("[MyazureWeixin]: End Send Processor:[{}]", System.currentTimeMillis());
		} else {
			result.setSuccess(false);
			result.setMessage(result.getMessage().replace("成功", "失败"));
			response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
			LOG.debug("[MyazureWeixin]: Failed MSG:[{}]", result.getMessage());
		}
	}

	@RequestMapping(path = "/private/masssend", method = RequestMethod.POST)
	public void privateMassSendTextMsg(  HttpServletRequest request, HttpServletResponse response) throws IOException,
			AesException {

	}

}
