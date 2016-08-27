package com.ysw.adsense.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;



public class AdGoodsEntity {

	@NotEmpty
	private long id;

	@NotEmpty
	private String name;

	private String description;

	private String link;

	private String imgUrl;

	@NotEmpty
	private long yswGoodsId;

	public AdGoodsEntity() {

	}

	@Override
	public String toString() {
		return "AdGoodsEntity [id=" + id + ", name=" + name + ", description="
				+ description + ", link=" + link + ", imgUrl=" + imgUrl
				+ ", yswGoodsId=" + yswGoodsId + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public long getYswGoodsId() {
		return yswGoodsId;
	}

	public void setYswGoodsId(long yswGoodsId) {
		this.yswGoodsId = yswGoodsId;
	}
}
