package com.ysw.adsense.ip;



import com.ysw.adsense.ip.request.GoodsReq;
import com.ysw.adsense.ip.request.LockReq;
import com.ysw.adsense.ip.request.OrderReq;
import com.ysw.adsense.ip.response.GoodsJsonResponse;
import com.ysw.adsense.ip.response.SingleGoodsResponse;
import com.ysw.adsense.ip.response.StatusResponse;

/**
 * @author : csyangchsh@gmail.com
 */
public interface IPManagementClient {

	GoodsJsonResponse getAllGoodsOfMat(String matId);

	SingleGoodsResponse getGoodsOfMat(String matId, long goodsId);

	SingleGoodsResponse getGoodsOfMat(GoodsReq goodsReq);

	StatusResponse 	getLockGoods(LockReq lockReq);
	StatusResponse lockGoods(LockReq lockReq);

	StatusResponse customerOrder(OrderReq orderReq);
}
