/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80017
Source Host           : localhost:3306
Source Database       : user1

Target Server Type    : MYSQL
Target Server Version : 80017
File Encoding         : 65001

Date: 2019-09-28 18:24:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user1_info`
-- ----------------------------
DROP TABLE IF EXISTS `user1_info`;
CREATE TABLE `user1_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(30) DEFAULT NULL COMMENT '用户名',
  `gender` int(11) DEFAULT '0' COMMENT '性别，0-未知 1-男 2-女',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user1_info
-- ----------------------------
INSERT INTO `user1_info` VALUES ('1', '张三', '1', '2019-09-28 16:41:03', '2019-09-28 16:41:05');
INSERT INTO `user1_info` VALUES ('3', '李四', '1', '2019-09-28 18:11:57', '2019-09-28 18:11:57');
