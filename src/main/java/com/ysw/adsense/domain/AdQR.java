package com.ysw.adsense.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ysw_ad_qr")
public class AdQR extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "qr_id")
	private long id;

	@Column(name = "qr_ticket")
	private String qrTicket;

	@Column(name = "qr_url")
	private String qrUrl;

	@OneToOne(targetEntity = AdEvent.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "event_id")
	private AdEvent event;
	
	@OneToOne(targetEntity = AdMat.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "mat_id")
	private AdMat mat;

	public AdQR() {

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

	public AdMat getMat() {
		return mat;
	}

	public void setMat(AdMat mat) {
		this.mat = mat;
	}

	public AdEvent getEvent() {
		return event;
	}

	public void setEvent(AdEvent event) {
		this.event = event;
	}
}
