package com.ysw.adsense.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;


public class AdQREntity {

	@NotEmpty
	private long id;

	private String qrTicket;

	private String qrUrl;

	private AdMatEntity mat;

	public AdQREntity() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQrTicket() {
		return qrTicket;
	}

	public void setQrTicket(String qrTicket) {
		this.qrTicket = qrTicket;
	}

	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}

	public AdMatEntity getMat() {
		return mat;
	}

	public void setMat(AdMatEntity mat) {
		this.mat = mat;
	}
}
