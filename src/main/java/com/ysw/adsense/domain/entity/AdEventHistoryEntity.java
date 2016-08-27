package com.ysw.adsense.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;



public class AdEventHistoryEntity {

	@NotEmpty
	private long id;

	@NotEmpty
	private AdEventEntity event;

	@NotEmpty
	private AdWxUserEntity wxUser;

	@NotEmpty
	private String action;

	private AdQREntity qr;

	private String details;

	public AdEventHistoryEntity() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AdEventEntity getEvent() {
		return event;
	}

	public void setEvent(AdEventEntity event) {
		this.event = event;
	}

	public AdWxUserEntity getWxUser() {
		return wxUser;
	}

	public void setWxUser(AdWxUserEntity wxUser) {
		this.wxUser = wxUser;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AdQREntity getQr() {
		return qr;
	}

	public void setQr(AdQREntity qr) {
		this.qr = qr;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
