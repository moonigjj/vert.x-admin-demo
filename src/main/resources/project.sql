/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : dish

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-09-11 10:44:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dish_desk
-- ----------------------------
DROP TABLE IF EXISTS `dish_desk`;
CREATE TABLE `dish_desk` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` bigint(20) unsigned NOT NULL COMMENT '餐厅id',
  `desk_num` varchar(20) NOT NULL DEFAULT '' COMMENT '桌子编号',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '二维码地址',
  `remark` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
  `desk_status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '0空闲，1有人，99作废',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='桌号信息表';

-- ----------------------------
-- Records of dish_desk
-- ----------------------------
INSERT INTO `dish_desk` VALUES ('1', '1', '1', 'ssda', '10-12', '0', '2018-06-12 10:21:12', '2018-06-12 10:21:14');
INSERT INTO `dish_desk` VALUES ('2', '1', '2', 'www.dada.com', '4人', '1', '2018-06-12 08:15:44', '2018-06-13 06:25:32');
INSERT INTO `dish_desk` VALUES ('3', '1', '3', 'xxxxx', '10-12', '2', '2018-06-12 08:16:10', '2018-06-12 09:37:52');
INSERT INTO `dish_desk` VALUES ('4', '1', '10A', 'xxxxx', '10-12', '0', '2018-06-12 08:20:59', '2018-06-12 08:20:59');
INSERT INTO `dish_desk` VALUES ('5', '1', '10B', 'xxxxx', '10-12', '0', '2018-06-12 08:21:13', '2018-06-12 08:21:13');

-- ----------------------------
-- Table structure for dish_discount
-- ----------------------------
DROP TABLE IF EXISTS `dish_discount`;
CREATE TABLE `dish_discount` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` bigint(20) unsigned NOT NULL COMMENT '餐厅id',
  `discount_name` varchar(20) NOT NULL DEFAULT '' COMMENT '折扣名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='折扣表';

-- ----------------------------
-- Records of dish_discount
-- ----------------------------

-- ----------------------------
-- Table structure for dish_food
-- ----------------------------
DROP TABLE IF EXISTS `dish_food`;
CREATE TABLE `dish_food` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '餐厅id',
  `dish_name` varchar(50) NOT NULL DEFAULT '' COMMENT '菜品名称',
  `dish_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '菜品单价',
  `dish_discount_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '折扣价',
  `dish_icon` varchar(500) NOT NULL DEFAULT '' COMMENT '菜品图片',
  `dish_is_takeout` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '是否支持外卖0不支持，1支持',
  `remark` varchar(20) NOT NULL DEFAULT '' COMMENT '简介描述',
  `dish_status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '0创建，1上架，2下架',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='菜品表';

-- ----------------------------
-- Records of dish_food
-- ----------------------------
INSERT INTO `dish_food` VALUES ('1', '1', '麻辣小龙虾', '138.00', '100.94', 'fff', '1', '正宗潜江小龙虾', '1', '2018-05-21 17:55:59', '2018-05-21 17:56:02');
INSERT INTO `dish_food` VALUES ('2', '1', '香辣小龙虾', '158.00', '121.86', 'xxx', '1', '正宗口味', '1', '2018-05-22 15:06:42', '2018-05-22 15:06:44');
INSERT INTO `dish_food` VALUES ('3', '1', '蒜蓉小龙虾', '148.88', '128.88', 'www.dadad.com', '1', '蒜蓉，微辣', '2', '2018-06-14 07:44:25', '2018-06-14 08:39:59');

-- ----------------------------
-- Table structure for dish_order
-- ----------------------------
DROP TABLE IF EXISTS `dish_order`;
CREATE TABLE `dish_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` bigint(20) unsigned NOT NULL COMMENT '餐厅id',
  `order_num` varchar(64) NOT NULL DEFAULT '' COMMENT '对属于一张桌子的菜单的标识',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `desk_num` varchar(20) NOT NULL DEFAULT '' COMMENT '桌号，记录发出点菜要求的顾客所在桌子',
  `buyer_name` varchar(64) NOT NULL DEFAULT '' COMMENT '买家名字',
  `buyer_phone` varchar(32) NOT NULL DEFAULT '' COMMENT '买家电话',
  `buyer_address` varchar(128) NOT NULL DEFAULT '' COMMENT '买家地址',
  `dish_amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '菜品份数',
  `dish_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '菜品总价',
  `dish_pay` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '实际付款',
  `sale_status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '订单状态1有效 0无效  2完成',
  `pay_method` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '支付方式0微信支付，1支付宝支付，2现金支付',
  `pay_status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '支付状态0待付款，1已付款',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='菜品销售表';

-- ----------------------------
-- Records of dish_order
-- ----------------------------
INSERT INTO `dish_order` VALUES ('1', '1', 'wqeq', '1', '17', '佳佳', '', '', '3', '623.00', '400.00', '2', '0', '1', '2018-05-22 16:54:12', '2018-05-22 16:54:19');
INSERT INTO `dish_order` VALUES ('2', '1', 'weqeq', '1', '16', 'wenwe', '', '', '2', '435.00', '400.00', '2', '0', '1', '2018-05-24 13:44:13', '2018-05-24 13:44:17');
INSERT INTO `dish_order` VALUES ('7', '1', '453197861086035968', '1', '1', 'wdq23a', '', '', '4', '296.00', '2018.09', '2', '1', '2', '2018-06-04 06:06:30', '2018-06-04 06:06:30');
INSERT INTO `dish_order` VALUES ('8', '1', '453200572976201728', '1', '1', 'wdq23a', '', '', '4', '296.00', '2018.09', '2', '1', '2', '2018-06-04 06:17:17', '2018-06-04 06:17:17');
INSERT INTO `dish_order` VALUES ('9', '1', '453200634133348352', '1', '1', 'wdq23a', '', '', '4', '296.00', '2018.09', '2', '1', '2', '2018-06-04 06:17:31', '2018-06-04 06:17:31');
INSERT INTO `dish_order` VALUES ('10', '1', '456049911192879104', '1', '1', 'wdq23a', '', '', '4', '296.00', '2018.09', '2', '1', '2', '2018-06-12 02:59:32', '2018-06-12 02:59:32');

-- ----------------------------
-- Table structure for dish_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `dish_order_detail`;
CREATE TABLE `dish_order_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_num` varchar(64) NOT NULL DEFAULT '' COMMENT '订单号',
  `dish_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '菜品id',
  `dish_name` varchar(64) NOT NULL DEFAULT '' COMMENT '商品名称',
  `dish_quantity` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商品数量',
  `dish_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  `dish_discount_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '折扣价',
  `dish_icon` varchar(512) NOT NULL DEFAULT '' COMMENT '商品小图',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='菜品销售详情表';

-- ----------------------------
-- Records of dish_order_detail
-- ----------------------------
INSERT INTO `dish_order_detail` VALUES ('1', 'wqeq', '1', '麻辣小龙虾', '1', '138.00', '106.12', 'ada', '2018-05-22 16:45:39', '2018-05-22 16:45:37');
INSERT INTO `dish_order_detail` VALUES ('2', 'wqeq', '2', '香辣小龙虾', '2', '316.00', '300.12', 'dsad', '2018-05-22 16:49:27', '2018-05-22 16:49:29');
INSERT INTO `dish_order_detail` VALUES ('3', '453197861086035968', '1', '麻辣小龙虾', '2', '138.00', '100.94', '', '2018-06-04 06:06:30', '2018-06-04 06:06:30');
INSERT INTO `dish_order_detail` VALUES ('4', '453197861086035968', '2', '香辣小龙虾', '2', '158.00', '121.86', '', '2018-06-04 06:06:30', '2018-06-04 06:06:30');
INSERT INTO `dish_order_detail` VALUES ('5', '453200572976201728', '1', '麻辣小龙虾', '2', '138.00', '100.94', '', '2018-06-04 06:17:17', '2018-06-04 06:17:17');
INSERT INTO `dish_order_detail` VALUES ('6', '453200572976201728', '2', '香辣小龙虾', '2', '158.00', '121.86', '', '2018-06-04 06:17:17', '2018-06-04 06:17:17');
INSERT INTO `dish_order_detail` VALUES ('7', '453200634133348352', '1', '麻辣小龙虾', '2', '138.00', '100.94', '', '2018-06-04 06:17:31', '2018-06-04 06:17:31');
INSERT INTO `dish_order_detail` VALUES ('8', '453200634133348352', '2', '香辣小龙虾', '2', '158.00', '121.86', '', '2018-06-04 06:17:31', '2018-06-04 06:17:31');
INSERT INTO `dish_order_detail` VALUES ('9', '456049911192879104', '1', '麻辣小龙虾', '2', '138.00', '100.94', '', '2018-06-12 02:59:32', '2018-06-12 02:59:32');
INSERT INTO `dish_order_detail` VALUES ('10', '456049911192879104', '2', '香辣小龙虾', '2', '158.00', '121.86', '', '2018-06-12 02:59:32', '2018-06-12 02:59:32');

-- ----------------------------
-- Table structure for dish_type
-- ----------------------------
DROP TABLE IF EXISTS `dish_type`;
CREATE TABLE `dish_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` bigint(20) unsigned NOT NULL COMMENT '餐厅id',
  `type_name` varchar(20) NOT NULL DEFAULT '' COMMENT '分类名称',
  `type_des` varchar(255) NOT NULL DEFAULT '' COMMENT '描述',
  `type_level` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '类别级别0是特殊，1是普通',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='菜品分类表';

-- ----------------------------
-- Records of dish_type
-- ----------------------------
INSERT INTO `dish_type` VALUES ('1', '0', '青菜', '季节', '1', '2018-05-17 14:41:10', '2018-05-17 14:41:12');
INSERT INTO `dish_type` VALUES ('2', '0', '套餐', '工作套餐，物美价廉', '1', '2018-05-17 14:42:02', '2018-05-17 14:42:04');

-- ----------------------------
-- Table structure for dish_variety
-- ----------------------------
DROP TABLE IF EXISTS `dish_variety`;
CREATE TABLE `dish_variety` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `variety_name` varchar(20) NOT NULL DEFAULT '' COMMENT '口味名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='菜品口味表';

-- ----------------------------
-- Records of dish_variety
-- ----------------------------
INSERT INTO `dish_variety` VALUES ('1', '微辣', '2018-05-21 15:34:06', '2018-05-21 15:34:09');
INSERT INTO `dish_variety` VALUES ('2', '中辣', '2018-05-21 15:34:28', '2018-05-21 15:34:30');
INSERT INTO `dish_variety` VALUES ('3', '辣', '2018-05-21 15:34:40', '2018-05-21 15:34:43');
INSERT INTO `dish_variety` VALUES ('4', '香辣', '2018-05-21 15:34:54', '2018-05-21 15:34:56');
INSERT INTO `dish_variety` VALUES ('5', '麻辣', '2018-05-21 15:35:05', '2018-05-21 15:35:07');

-- ----------------------------
-- Table structure for dish_weixin_account
-- ----------------------------
DROP TABLE IF EXISTS `dish_weixin_account`;
CREATE TABLE `dish_weixin_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` bigint(20) unsigned NOT NULL COMMENT '餐厅id',
  `openid` varchar(20) NOT NULL DEFAULT '' COMMENT '微信openid',
  `nick_name` varchar(64) NOT NULL DEFAULT '' COMMENT '微信呢称',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '微信头像地址',
  `salt` varchar(20) NOT NULL DEFAULT '' COMMENT '加密盐',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dish_weixin_account
-- ----------------------------
INSERT INTO `dish_weixin_account` VALUES ('1', '0', 'dawdadad21231', 'wdq23a', 'adaw', '6666666', '2018-05-22 16:41:27', '2018-05-22 16:41:31');

-- ----------------------------
-- Table structure for mer_type
-- ----------------------------
DROP TABLE IF EXISTS `mer_type`;
CREATE TABLE `mer_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type_name` varchar(20) NOT NULL DEFAULT '' COMMENT '商户类型名称',
  `type_dec` varchar(255) NOT NULL DEFAULT '' COMMENT '类型描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mer_type
-- ----------------------------

-- ----------------------------
-- Table structure for merchants
-- ----------------------------
DROP TABLE IF EXISTS `merchants`;
CREATE TABLE `merchants` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `mer_name` varchar(100) NOT NULL DEFAULT '' COMMENT '餐馆名称',
  `nation` varchar(20) NOT NULL DEFAULT '' COMMENT '所在国家',
  `province` varchar(20) NOT NULL DEFAULT '' COMMENT '所在省',
  `city` varchar(20) NOT NULL DEFAULT '' COMMENT '所在城市',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '街道',
  `mer_dec` varchar(255) NOT NULL DEFAULT '' COMMENT '商家描述',
  `mer_type` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '餐馆类型',
  `mer_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '商户电话',
  `mer_url` varchar(500) NOT NULL DEFAULT '' COMMENT '图片地址',
  `mer_begin_time` varchar(20) NOT NULL DEFAULT '' COMMENT '营业时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商户信息表';

-- ----------------------------
-- Records of merchants
-- ----------------------------
INSERT INTO `merchants` VALUES ('1', '佳佳菜馆', '中国', '浙江省', '临海', '山前村', '正宗潜江小龙虾', '0', '13471231123', 'ef', '2018-05-22');

-- ----------------------------
-- Table structure for pay_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_log`;
CREATE TABLE `pay_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_token` varchar(20) NOT NULL DEFAULT '' COMMENT '客户标识',
  `order_num` varchar(50) NOT NULL DEFAULT '' COMMENT '订单编号',
  `third_sn` varchar(50) NOT NULL DEFAULT '' COMMENT '第三方支付编号',
  `pay_time` datetime NOT NULL COMMENT '支付时间',
  `pay_method` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付方式',
  `pay_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付流水表';

-- ----------------------------
-- Records of pay_log
-- ----------------------------

-- ----------------------------
-- Table structure for wx_cms_fans
-- ----------------------------
DROP TABLE IF EXISTS `wx_cms_fans`;
CREATE TABLE `wx_cms_fans` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `open_id` varchar(100) NOT NULL DEFAULT '' COMMENT '微信open_id',
  `sub_status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '订阅状态，0取消订阅，1订阅',
  `sub_time` varchar(50) NOT NULL DEFAULT '' COMMENT '订阅时间',
  `nick_name` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `gender` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '性别',
  `language` varchar(50) NOT NULL DEFAULT '' COMMENT '语言',
  `country` varchar(30) NOT NULL DEFAULT '' COMMENT '国家',
  `province` varchar(30) NOT NULL DEFAULT '' COMMENT '省',
  `city` varchar(30) NOT NULL DEFAULT '' COMMENT '城市',
  `head_img` varchar(255) NOT NULL DEFAULT '' COMMENT '头像url',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(50) NOT NULL DEFAULT '' COMMENT '备注',
  `weixin_id` varchar(50) NOT NULL DEFAULT '' COMMENT '微信id',
  `account` varchar(100) NOT NULL DEFAULT '' COMMENT '微信账号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信粉丝信息表';

-- ----------------------------
-- Records of wx_cms_fans
-- ----------------------------
