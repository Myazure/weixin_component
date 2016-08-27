package com.ysw.adsense.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;


public class AdMatEntity {

	@NotEmpty
	private long id;

	@NotEmpty
	private String matCode;

	private String matIp;

	@NotEmpty
	private String city;

	private String description;

	@NotEmpty
	private Double locationX;

	@NotEmpty
	private Double locationY;

	public AdMatEntity() {

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
