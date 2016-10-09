/*
Navicat MySQL Data Transfer

Source Server         : 127.1
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : myazure_weixin

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2016-10-09 02:34:39
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `myazure_weixin_official_account`
-- ----------------------------
DROP TABLE IF EXISTS `myazure_weixin_official_account`;
CREATE TABLE `myazure_weixin_official_account` (
  `official_account_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'App在本平台的ID编号，自增无符号',
  `user_id` bigint(11) NOT NULL COMMENT '平台用户ID',
  `app_id` varchar(50) NOT NULL COMMENT '微信app的ID',
  `username` varchar(30) DEFAULT NULL COMMENT '微信原始ID，公众号/订阅号原始id',
  `nickname` varchar(30) DEFAULT NULL COMMENT '公众号/订阅号的昵称',
  `alias` varchar(50) DEFAULT NULL COMMENT 'wechatID',
  `head_img_url` varchar(255) DEFAULT NULL,
  `qrcode_url` varchar(255) DEFAULT NULL,
  `authorized` bit(1) NOT NULL DEFAULT b'1',
  `service_type_info` varchar(255) DEFAULT NULL,
  `verify_type_info` varchar(255) DEFAULT NULL,
  `funcscope_category` varchar(255) DEFAULT NULL,
  `business_info` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(512) DEFAULT NULL COMMENT '授权后的刷新token',
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`official_account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of myazure_weixin_official_account
-- ----------------------------
INSERT INTO `myazure_weixin_official_account` VALUES ('14', '4', 'wx4fedca7c05a5bc36', 'gh_e86c013cb6a3', '建筑优工', 'JianZhu_YouGong', 'http://wx.qlogo.cn/mmopen/iacMoM6ia4CBnyMnHawFfag1b5YFStzcQbRYPZKzUia38XFEQ85n9Az9EkynFiapA7dfOtGvVK4mT7agES5rnFAWKt7G8vl6k47G/0', 'http://mmbiz.qpic.cn/mmbiz_jpg/rAialpiaibYhfcmkNhbOA7To91pk15f1PNwkcInXV3fgH2E1VibfcJUibvCQLdqrCAljkwjH62bwJqkGkhJtUxwqtNg/0', '', null, null, null, null, 'refreshtoken@@@10j-WBGeEdTzlq1tdjOLzIc4K3RICCKnwXN52kVSMjw', '2016-09-01 04:20:56', '2016-09-01 17:43:17');
INSERT INTO `myazure_weixin_official_account` VALUES ('15', '4', 'wxa4d09a2ba3c4f828', 'gh_a7f8ae6ac9ca', '素心瓷', 'SuXinCiQi', 'http://wx.qlogo.cn/mmopen/kfyEKDRrJDR0rMIxsevj6YtMIumTJ6sFyxgXgOz1YaSUy3ma6Lib79pGVnnpGBia0actsJCouTKYHeWwhnfyNhS9ic0rNASicIJic/0', 'http://mmbiz.qpic.cn/mmbiz/H1geLmb4otM9s4He8e2qk28xhaVpVl0icyqicDfMIdTE8uH1LIsxiabjpMR3kdJc7087KNYYTu7xIBBjiabp7EL6uw/0', '', null, null, null, null, 'refreshtoken@@@TNzjmsspRE9CSIiaBomRIIrmMyBNtZqEYvOfYpBgOXQ', '2016-09-01 12:32:53', '2016-10-06 10:13:17');

-- ----------------------------
-- Table structure for `myazure_weixin_official_account_wx_user`
-- ----------------------------
DROP TABLE IF EXISTS `myazure_weixin_official_account_wx_user`;
CREATE TABLE `myazure_weixin_official_account_wx_user` (
  `official_account_wx_user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `official_account_id` bigint(11) NOT NULL,
  `wx_user_id` bigint(11) NOT NULL,
  `open_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subscribed` bit(1) NOT NULL DEFAULT b'1',
  `subscribed_time` datetime DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `groupid` int(11) DEFAULT NULL,
  `tagid_list` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`official_account_wx_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of myazure_weixin_official_account_wx_user
-- ----------------------------

-- ----------------------------
-- Table structure for `myazure_weixin_user`
-- ----------------------------
DROP TABLE IF EXISTS `myazure_weixin_user`;
CREATE TABLE `myazure_weixin_user` (
  `user_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(15) NOT NULL COMMENT '用户名，检查过后当作salt值使用，用户名的hashcode作为salt',
  `password` varchar(100) NOT NULL COMMENT '经过用户名的hashcode前置密码的md5值',
  `role` varchar(15) NOT NULL COMMENT '角色ID或者角色值，',
  `active` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否激活',
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of myazure_weixin_user
-- ----------------------------
INSERT INTO `myazure_weixin_user` VALUES ('1', 'admin', '$2a$08$DB9k1GkMh61lcMHGk7lMRuenxsyGiJOXZeqoIqScjHzFaGNRt26gG', 'ADMIN', '', '2016-06-20 23:27:00', '2016-06-20 23:27:40');
INSERT INTO `myazure_weixin_user` VALUES ('2', 'test01', '$2a$08$DB9k1GkMh61lcMHGk7lMRuenxsyGiJOXZeqoIqScjHzFaGNRt26gG', 'USER', '', '2016-06-20 23:27:00', '2016-06-20 23:27:40');
INSERT INTO `myazure_weixin_user` VALUES ('3', 'test02', '$2a$10$zPTFygmVsrqaOhL1O7erSeKSCo7Bm5O/Py.wOqYSVht0UPEK/oR02', 'USER', '\0', '2016-06-21 11:47:01', '2016-06-21 11:47:01');
INSERT INTO `myazure_weixin_user` VALUES ('4', 'test03', '$2a$10$zPTFygmVsrqaOhL1O7erSeKSCo7Bm5O/Py.wOqYSVht0UPEK/oR02', 'USER', '', '2016-06-26 11:37:49', '2016-06-26 11:37:49');

-- ----------------------------
-- Table structure for `myazure_weixin_wx_user`
-- ----------------------------
DROP TABLE IF EXISTS `myazure_weixin_wx_user`;
CREATE TABLE `myazure_weixin_wx_user` (
  `wx_user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(50) DEFAULT NULL COMMENT '普通用户的标识，对当前公众号唯一',
  `union_id` varchar(50) DEFAULT NULL COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。',
  `subscribe` bit(1) DEFAULT b'1' COMMENT '用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户的昵称',
  `sex` int(1) unsigned DEFAULT '0' COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `language` varchar(20) DEFAULT NULL COMMENT '返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语',
  `city` varchar(255) DEFAULT NULL COMMENT '普通用户个人资料填写的城市',
  `province` varchar(255) DEFAULT NULL COMMENT '用户个人资料填写的省份',
  `country` varchar(255) DEFAULT NULL COMMENT '国家，如中国为CN',
  `head_img_url` varchar(255) DEFAULT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。',
  `subscribe_time` bigint(11) DEFAULT '0',
  `remark` varchar(255) DEFAULT NULL,
  `group_id` varchar(255) DEFAULT NULL,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`wx_user_id`),
  UNIQUE KEY `unionid` (`union_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of myazure_weixin_wx_user
-- ----------------------------
