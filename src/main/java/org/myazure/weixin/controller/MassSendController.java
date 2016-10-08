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
import org.myazure.weixin.handlers.AuthorizeHandler;
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

	@RequestMapping(path = "/public/{appId}/masssend", method = RequestMethod.POST)
	public void publicMassSendTextMsg(@PathVariable("appId") String appId, HttpServletRequest request, HttpServletResponse response) throws IOException,
			AesException {
		LOG.debug("[MyazureWeixin]: /public/{appId}/masssend get a new request" + appId);
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
			LOG.debug("Recieve sucess \n" + requestString);
		} catch (Exception e) {
			LOG.debug("Recieve failed");
		}
		if (massSendReq == null) {
			return;
		}
		String appidString = massSendReq.getAppid();

		switch (massSendReq.getMsgType()) {
		case MassSendReq.newsMessage:
			LOG.debug("[MyazureWeixin]: massSendReq  {}", JSON.toJSONString(massSendReq));
			Cookie[] cookies = request.getCookies();
			LOG.debug("[MyazureWeixin]: cookies  {}", JSON.toJSONString(cookies));
			List<Article> articles = new ArrayList<NewsMessage.Article>();
			String toUser = massSendReq.getToUser();
			String tittleString = massSendReq.getTitle();
			String descriptionString = massSendReq.getDescription();
			String urlString = massSendReq.getNewsUrl();
			String pictureURLString = massSendReq.getThumbUrl();
			NewsMessage.Article ariticle = new NewsMessage.Article(tittleString, descriptionString, urlString, pictureURLString);
			articles.add(ariticle);
			NewsMessage newsMessage = new NewsMessage(toUser, articles);
			LOG.debug("[MyazureWeixin]: newsMessage  {}", JSON.toJSONString(newsMessage));
			LOG.debug("[MyazureWeixin]: articles  {}", JSON.toJSONString(articles));
			break;
		case MassSendReq.musicMessage:
			break;
		case MassSendReq.imageMessage:
			break;
		case MassSendReq.textMessage:
			break;
		case MassSendReq.vedioMessage:
			break;
		case MassSendReq.voiceMessage:
			break;
		default:
			break;
		}

		StatusResponse result = new StatusResponse();
		result.setCode(0);
		response.getOutputStream().print(new String(JSON.toJSONString(result).getBytes(), "ISO-8859-1"));
	}

	@RequestMapping(path = "/private/{appId}/masssend", method = RequestMethod.POST)
	public void privateMassSendTextMsg(@PathVariable("appId") String appId, HttpServletRequest request, HttpServletResponse response) throws IOException,
			AesException {

	}

}
