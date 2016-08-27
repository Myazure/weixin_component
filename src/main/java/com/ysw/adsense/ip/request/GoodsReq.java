package com.ysw.adsense.ip.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : csyangchsh@gmail.com
 */
public class GoodsReq {

	@JsonProperty("mat_id")
	private String mat_id;
	@JsonProperty("goods_id")
	private long goodsId;

	public GoodsReq() {

	}

	public GoodsReq(String mat_id, long goodsId) {
		this.goodsId = goodsId;
		this.mat_id = mat_id;
	}

	public String getMat_id() {
		return mat_id;
	}

	public void setMat_id(String mat_id) {
		this.mat_id = mat_id;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

}
