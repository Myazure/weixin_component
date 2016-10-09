package org.myazure.weixin.handlers;

import java.awt.image.BufferedImage;

import org.myazure.weixin.constant.MyazureConstants;

import weixin.popular.api.QrcodeAPI;
import weixin.popular.bean.qrcode.QrcodeTicket;
/**
 * 
 * @author WangZhen
 *
 */
public class QrCodeHandler {
	/**
	 * 创建 二维码
	 * 
	 * @param access_token
	 *            access_token
	 * @param expire_seconds
	 *            最大不超过604800秒（即30天）
	 * @param scene_id
	 *            场景值ID，32位非0整型 最多10万个
	 * @return QrcodeTicket
	 */
	public QrcodeTicket qrcodeCreate(String JSONData, int expire_seconds, int scene_id) {
		if (expire_seconds > 0) {
			return QrcodeAPI.qrcodeCreateTemp(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, expire_seconds, scene_id);
		} else {
			return QrcodeAPI.qrcodeCreateFinal(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, scene_id);
		}
	}

	/**
	 * 下载二维码
	 * 
	 * @param ticket
	 *            内部自动 UrlEncode
	 * @return BufferedImage
	 */
	public BufferedImage downloadQrcodeImage(String ticket) {
		return QrcodeAPI.showqrcode(ticket);
	}

}
