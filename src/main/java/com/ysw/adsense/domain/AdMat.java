package com.ysw.adsense.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ysw_mat")
public class AdMat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "mat_id")
	private long id;

	@Column(name = "mat_code")
	private String matCode;

	@Column(name = "mat_ip")
	private String matIp;

	@Column(name = "city")
	private String city;

	@Column(name = "description")
	private String description;

	@Column(name = "location_x")
	private Double locationX;

	@Column(name = "location_y")
	private Double locationY;

	public AdMat() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public String getMatIp() {
		return matIp;
	}

	public void setMatIp(String matIp) {
		this.matIp = matIp;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLocationX() {
		return locationX;
	}

	public void setLocationX(Double locationX) {
		this.locationX = locationX;
	}

	public Double getLocationY() {
		return locationY;
	}

	public void setLocationY(Double locationY) {
		this.locationY = locationY;
	}
}
