package com.ysw.adsense.ip.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author : csyangchsh@gmail.com
 */
public class GoodsJsonResponse extends StatusResponse {

	@JsonProperty("data")
	private List<GoodsRes> data;

	public GoodsJsonResponse() {

	}

	public List<GoodsRes> getData() {
		return data;
	}

	public void setData(List<GoodsRes> data) {
		this.data = data;
	}
}
