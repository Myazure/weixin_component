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
@Table(name = "ysw_ad_event_mat")
public class AdEventMat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "event_mat_id")
	private long id;
	
	@OneToOne(targetEntity = AdEvent.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "event_id")
	private AdEvent event;
	
	@OneToOne(targetEntity = AdMat.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "mat_id")
	private AdMat mat;

	@Column(name = "start_hour", columnDefinition = "SMALLINT")
	private Integer startHour;

	@Column(name = "end_hour", columnDefinition = "SMALLINT")
	private Integer endHour;

	@Column(name = "active", columnDefinition = "BIT", length = 1)
	private boolean active = false;

	public AdEventMat() {

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

	public AdMat getMat() {
		return mat;
	}

	public void setMat(AdMat mat) {
		this.mat = mat;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
