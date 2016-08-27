package com.ysw.adsense.ip.response;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : csyangchsh@gmail.com
 */
public class GoodsRes {

	@JsonProperty("id")
	private long goodsId;

	@JsonProperty("price")
	private float price;

	@JsonProperty("number")
	private int number;

	public GoodsRes() {

	}

	public long getGoodsId() {
		return goodsId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
