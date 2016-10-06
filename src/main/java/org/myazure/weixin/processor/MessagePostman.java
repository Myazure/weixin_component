package org.myazure.weixin.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.Message;

public class MessagePostman implements Runnable {
	private String accessTokenString;
	private String appIdString;
	private Message msg2Send;
	private BaseResult resaultMsg;
	private static final Logger LOG = LoggerFactory.getLogger(MessagePostman.class);

	public MessagePostman() {
		this.accessTokenString = null;
		this.msg2Send = null;
	}

	public MessagePostman(Message msg) {
		this.setMsg2Send(msg);
		this.accessTokenString = null;
	}

	public MessagePostman(Message msg, String access_token) {
		this.setMsg2Send(msg);
		this.accessTokenString = access_token;
	}

	@Override
	public void run() {
		LOG.debug("[MyazureWeChat]:  StartSendMsg AppID:{},MSG:{}",appIdString,msg2Send);
		resaultMsg=MessageAPI.messageCustomSend(this.accessTokenString, this.msg2Send);
		LOG.debug("[MyazureWeChat]:  AppID:{},MSGErrResault:{}",appIdString,resaultMsg.getErrmsg());
	}

	public Message getMsg2Send() {
		return msg2Send;
	}

	public void setMsg2Send(Message msg2Send) {
		this.msg2Send = msg2Send;
	}

}
