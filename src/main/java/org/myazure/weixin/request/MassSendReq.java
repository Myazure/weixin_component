package org.myazure.weixin.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class MassSendReq {
	
	public static final String newsMessage="news"; 
	public static final String imageMessage="image"; 
	public static final String musicMessage="music"; 
	public static final String textMessage="text"; 
	public static final String vedioMessage="vedio"; 
	public static final String voiceMessage="voice"; 
	
	
	@JsonProperty("appid")
	private String appid;

	@JsonProperty("touser")
	private String toUser;

	@JsonProperty("msgtype")
	private String msgType;

	// MUSIC NEWS WANT IT
	@JsonProperty("title")
	private String title;

	@JsonProperty("description")
	private String description;

	// MUSIC Messgae
	@JsonProperty("musicurl")
	private String musicUrl;
	
	@JsonProperty("hqmusicurl")
	private String hqMusicUrl;
	
	@JsonProperty("thumbmediaid")
	private String thumb_media_id;

	
	// NEWS Message
	@JsonProperty("newsurl")
	private String newsUrl;

	@JsonProperty("thumburl")
	private String thumbUrl;

	
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	public String getHqMusicUrl() {
		return hqMusicUrl;
	}

	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}




}
