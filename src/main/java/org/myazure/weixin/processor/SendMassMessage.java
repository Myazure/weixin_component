package org.myazure.weixin.processor;

import org.myazure.weixin.MyazureWeixinAPI;
import org.myazure.weixin.domain.MaOfficialAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.message.ImageMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.MusicMessage;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.bean.message.message.VideoMessage;
import weixin.popular.bean.message.message.VoiceMessage;
/**
 * 
 * @author WangZhen
 *@since V1.0
 */
public class SendMassMessage implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(SendMassMessage.class);
	
	@Autowired
	private MyazureWeixinAPI myazureWeixinAPI =new MyazureWeixinAPI();
	
	private String messageTypeString;
	private String toUserString;
	private Message message;
	private ImageMessage imageMessage;
	private MusicMessage musicMessage;
	private NewsMessage newsMessage;
	private TextMessage textMessage;
	private VideoMessage videoMessage;
	private VoiceMessage voiceMessage;
	private MaOfficialAccount oa;
	// 永久
	// 图片大小不超过2M，支持bmp/png/jpeg/jpg/gif格式，语音大小不超过5M，长度不超过60秒，支持mp3/wma/wav/amr格式
	// 临时
	// 上传的临时多媒体文件有格式和大小限制，如下：
	// 图片（image）: 2M，支持PNG\JPEG\JPG\GIF格式
	// 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
	// 视频（video）：10MB，支持MP4格式
	// 缩略图（thumb）：64KB，支持JPG格式

	public SendMassMessage(Message message,MaOfficialAccount _oa) {
		if (message == null) {
			return;
		}
		if (message.getTouser() == null) {
			return;
		}
		if (_oa==null) {
			return;
		}
		
		this.oa=_oa;
		this.toUserString = message.getTouser();
		switch (message.getClass().getName()) {
		case "weixin.popular.bean.message.message.ImageMessage":
			this.imageMessage = (ImageMessage) message;
			this.messageTypeString = "ImageMessage";
			break;
		case "weixin.popular.bean.message.message.MusicMessage":
			this.musicMessage = (MusicMessage) message;
			this.messageTypeString = "MusicMessage";
			break;
		case "weixin.popular.bean.message.message.NewsMessage":
			this.newsMessage = (NewsMessage) message;
			this.messageTypeString = "NewsMessage";
			break;
		case "weixin.popular.bean.message.message.TextMessage":
			this.textMessage = (TextMessage) message;
			this.messageTypeString = "TextMessage";
			break;
		case "weixin.popular.bean.message.message.VideoMessage":
			this.videoMessage = (VideoMessage) message;
			this.messageTypeString = "VideoMessage";
			break;
		case "weixin.popular.bean.message.message.VoiceMessage":
			this.voiceMessage = (VoiceMessage) message;
			this.messageTypeString = "VoiceMessage";
			break;
		default:
			this.message = message;
			this.messageTypeString = "Message";
			break;
		}

	}

	@Override
	public void run() {
		if (toUserString == null || messageTypeString == null) {
			LOG.error("[MyazureWeixin]: MessageType:[{}],toUserString：[{}],send ERR", messageTypeString, toUserString);
			return;
		}
		switch (toUserString) {
		case "all":
			this.toUserString="oQNg2w5uclIno9ivhPbysDzxM0UI";
			break;
		case "group":
			this.toUserString="oQNg2w5uclIno9ivhPbysDzxM0UI";
			break;
		case "active":
			this.toUserString="oQNg2w5uclIno9ivhPbysDzxM0UI";
			break;
		default:
			this.toUserString="oQNg2w5uclIno9ivhPbysDzxM0UI";
			break;
		}
		newsMessage.setTouser(toUserString);
		LOG.debug("[MyazureWeixin]: tosendMessage:[{}]", JSON.toJSONString(this.newsMessage));
		LOG.debug("[MyazureWeixin]: OA:[{}]", JSON.toJSONString(oa));
		LOG.debug("[MyazureWeixin]: AppToken:[{}]",  myazureWeixinAPI.getAuthorizerAccessTokenStr("wx4fedca7c05a5bc36"));
		MessageAPI.messageCustomSend(myazureWeixinAPI.getAuthorizerAccessTokenStr(oa.getAppId()), this.newsMessage);
		LOG.debug("[MyazureWeixin]: MessageType:[{}],sended", messageTypeString);
	}

}
