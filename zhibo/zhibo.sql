/*
Navicat MySQL Data Transfer

Source Server         : zhibo
Source Server Version : 50553
Source Host           : localhost:3306
Source Database       : zhibo

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2019-05-23 09:02:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for fangjian
-- ----------------------------
DROP TABLE IF EXISTS `fangjian`;
CREATE TABLE `fangjian` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `fangjianname` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fangjian
-- ----------------------------
INSERT INTO `fangjian` VALUES ('2', '22', '11', '11');

-- ----------------------------
-- Table structure for shipin
-- ----------------------------
DROP TABLE IF EXISTS `shipin`;
CREATE TABLE `shipin` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `shipin_url` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shipin
-- ----------------------------
INSERT INTO `shipin` VALUES ('1', '7c283e39-844f-4457-a802-3af162020c5eba07fcb222cf547d107fae9f65458071.mp4', '11', '阿萨德');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `leixing` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '123456', '系统管理员');
INSERT INTO `user` VALUES ('2', '11', '11', '医生');
INSERT INTO `user` VALUES ('3', '22', '22', '普通用户');
INSERT INTO `user` VALUES ('4', '123456', '123456', '普通用户');
SET FOREIGN_KEY_CHECKS=1;
