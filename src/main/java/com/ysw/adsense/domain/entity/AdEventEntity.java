package com.ysw.adsense.domain.entity;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

public class AdEventEntity {

	@NotEmpty
	private Long id;

	@NotEmpty
	private String name;
	
	private String description;
	
	@NotEmpty
	private AdOfficialAccountEntity officialAccount;
	
	@NotEmpty
	private AdGoodsEntity goods;

	@NotEmpty
	private int event_type;
	
	private int frequency;

	private Date startDate;

	private Date endDate;

	private Integer startHour;

	private Integer endHour;

	private boolean requireSubscribed = false;

	private Integer qrType;

	private boolean active = false;

	@Override
	public String toString() {
		return "AdEventEntity [id=" + id + ", officialAccount="
				+ officialAccount.toString() + ", name=" + name + ", description="
				+ description + ", goods=" + goods.toString() + ", event_type="
				+ event_type + ", frequency=" + frequency + ", startDate="
				+ startDate + ", endDate=" + endDate + ", startHour="
				+ startHour + ", endHour=" + endHour + ", requireSubscribed="
				+ requireSubscribed + ", qrType=" + qrType + ", active="
				+ active + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AdOfficialAccountEntity getOfficialAccount() {
		return officialAccount;
	}

	public void setOfficialAccount(AdOfficialAccountEntity officialAccount) {
		this.officialAccount = officialAccount;
	}

	public AdGoodsEntity getGoods() {
		return goods;
	}

	public void setGoods(AdGoodsEntity goods) {
		this.goods = goods;
	}

	public int getEvent_type() {
		return event_type;
	}

	public void setEvent_type(int event_type) {
		this.event_type = event_type;
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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
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
}
