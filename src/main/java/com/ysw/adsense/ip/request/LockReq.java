package com.ysw.adsense.ip.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author : csyangchsh@gmail.com
 */
public class LockReq {
	public static int LOCK = 1;
	public static int UNLOCK = 2;
	@JsonProperty("mat_id")
	private String mat_id;
	@JsonProperty("type")
	private int type = 1;
	@JsonProperty("goods")
	private List<LockItem> goods;

	public LockReq() {

	}

	public String getMatId() {
		return mat_id;
	}

	public void setMatId(String mat_id) {
		this.mat_id = mat_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<LockItem> getGoods() {
		return goods;
	}

	public void setGoods(List<LockItem> goods) {
		this.goods = goods;
	}
}
