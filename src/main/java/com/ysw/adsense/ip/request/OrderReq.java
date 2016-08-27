package com.ysw.adsense.ip.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author : csyangchsh@gmail.com
 */
public class OrderReq {

    @JsonProperty("customer_id")
    private long customer_id;
    @JsonProperty("mat_id")
    private String mat_id;
    @JsonProperty("order_info")
    private List<OrderItem> order_info;

    public OrderReq() {

    }

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public String getMat_id() {
		return mat_id;
	}

	public void setMat_id(String mat_id) {
		this.mat_id = mat_id;
	}

	public List<OrderItem> getOrder_info() {
		return order_info;
	}

	public void setOrder_info(List<OrderItem> order_info) {
		this.order_info = order_info;
	}

   
}
