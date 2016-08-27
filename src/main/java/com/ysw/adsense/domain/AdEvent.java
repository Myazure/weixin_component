package com.ysw.adsense.domain;

import java.util.Date;

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
import javax.persistence.TemporalType;

@Entity
@Table(name = "ysw_ad_event")
public class AdEvent extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "event_id")
	private long id;
	
	@JoinColumn(name = "name")
	private String name;
	
	@JoinColumn(name = "description")
	private String description;

	@OneToOne(targetEntity = AdOfficialAccount.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "official_account_id")
	private AdOfficialAccount officialAccount;

	@OneToOne(targetEntity = AdGoods.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "goods_id")
	private AdGoods goods;

	@Column(name = "event_type", columnDefinition = "SMALLINT")
	private int eventType;
	
	@Column(name = "frequency", columnDefinition = "SMALLINT")
	private int frequency;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "start_hour", columnDefinition = "SMALLINT")
	private Integer startHour;

	@Column(name = "end_hour", columnDefinition = "SMALLINT")
	private Integer endHour;

	@Column(name = "safe_distance")
	private Integer safeDistance;
	
	@Column(name = "require_subscribed", columnDefinition = "BIT", length = 1)
	private boolean requireSubscribed = false;

	@Column(name = "qr_type", length = 1)
	private Integer qrType;

	@Column(name = "active", columnDefinition = "BIT", length = 1)
	private boolean active = false;

	public AdEvent() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AdOfficialAccount getOfficialAccount() {
		return officialAccount;
	}

	public void setOfficialAccount(AdOfficialAccount officialAccount) {
		this.officialAccount = officialAccount;
	}

	public AdGoods getGoods() {
		return goods;
	}

	public void setGoods(AdGoods goods) {
		this.goods = goods;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getStartHour() {
		return startHour;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public Integer getEndHour() {
		return endHour;
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

	public boolean isRequireSubscribed() {
		return requireSubscribed;
	}

	public void setRequireSubscribed(boolean requireSubscribed) {
		this.requireSubscribed = requireSubscribed;
	}

	public Integer getQrType() {
		return qrType;
	}

	public void setQrType(Integer qrType) {
		this.qrType = qrType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Integer getSafeDistance() {
		return safeDistance;
	}

	public void setSafeDistance(Integer safeDistance) {
		this.safeDistance = safeDistance;
	}
}
