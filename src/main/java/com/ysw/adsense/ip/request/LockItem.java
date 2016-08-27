package com.ysw.adsense.ip.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : csyangchsh@gmail.com
 */
public class LockItem {

    @JsonProperty("goods_id")
    private long goods_id;
    @JsonProperty("goods_num")
    private int goods_num;


    public LockItem(long goods_id, int goods_num) {
        this.goods_id = goods_id;
        this.goods_num = goods_num;
    }


	public long getGoods_id() {
		return goods_id;
	}


	public void setGoods_id(long goods_id) {
		this.goods_id = goods_id;
	}


	public int getGoods_num() {
		return goods_num;
	}


	public void setGoods_num(int goods_num) {
		this.goods_num = goods_num;
	}

 
 
  
}
