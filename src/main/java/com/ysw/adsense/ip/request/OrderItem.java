package com.ysw.adsense.ip.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author : csyangchsh@gmail.com
 */
public class OrderItem {
    @JsonProperty("order_id")
    private String order_id;
    @JsonProperty("goods")
    private List<LockItem> goods;
    @JsonProperty("order_price")
    private BigDecimal order_price;
    @JsonProperty("order_date")
    private Date order_date;

    public OrderItem() {

    }

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public List<LockItem> getGoods() {
		return goods;
	}

	public void setGoods(List<LockItem> goods) {
		this.goods = goods;
	}

	public BigDecimal getOrder_price() {
		return order_price;
	}

	public void setOrder_price(BigDecimal order_price) {
		this.order_price = order_price;
	}

	public Date getOrder_date() {
		return order_date;
	}

	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}

   
}
