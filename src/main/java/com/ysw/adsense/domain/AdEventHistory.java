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
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
@Table(name = "ysw_ad_event_history")
public class AdEventHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "event_history_id")
	private long id;

	@OneToOne(targetEntity = AdEvent.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "event_id")
	private AdEvent event;

	@OneToOne(targetEntity = AdWxUser.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "wx_user_id")
	private AdWxUser wxUser;

	@Column(name = "action")
	private String action;

	@OneToOne(targetEntity = AdQR.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "qr_id")
	private AdQR qr;

	@JoinColumn(name = "ip_order_no")
	private long ipOrderNo;
	
	
	@Column(name = "details")
	private String details;
	
	@Transient
	private AdMat adMat;

	public AdEventHistory() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AdEvent getEvent() {
		return event;
	}

	public void setEvent(AdEvent event) {
		this.event = event;
	}

	public AdWxUser getWxUser() {
		return wxUser;
	}

	public void setWxUser(AdWxUser wxUser) {
		this.wxUser = wxUser;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AdQR getQr() {
		return qr;
	}

	public void setQr(AdQR qr) {
		this.qr = qr;
	}

	public long getIpOrderNo() {
		return ipOrderNo;
	}

	public void setIpOrderNo(long ipOrderNo) {
		this.ipOrderNo = ipOrderNo;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public AdMat getAdMat() {
		return adMat;
	}

	public void setAdMat(AdMat adMat) {
		this.adMat = adMat;
	}
}
