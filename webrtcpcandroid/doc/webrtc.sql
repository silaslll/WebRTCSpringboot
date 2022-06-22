/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : webrtc

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 20/03/2021 14:17:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `s_id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('admin', '1', 1);
INSERT INTO `account` VALUES ('a', '1', 2);
INSERT INTO `account` VALUES ('b', '1', 3);
INSERT INTO `account` VALUES ('c', '1', 4);

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES (1, '第一个单位');
INSERT INTO `dept` VALUES (2, '第二个单位');

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `post_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `d_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, '办公室啊', 1);
INSERT INTO `post` VALUES (2, '蹲起', 1);

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`  (
  `staff_id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `p_id` int(11) NULL DEFAULT NULL,
  `post_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`staff_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES (1, '管理员', '1', 1, '13512345678', 1, 1);
INSERT INTO `staff` VALUES (2, '张三', '男', 22, '13512345673', 1, NULL);
INSERT INTO `staff` VALUES (3, '李四', '男', 33, '15011589558', 1, NULL);
INSERT INTO `staff` VALUES (4, 'c', '男', 33, '13512345672', 2, NULL);

-- ----------------------------
-- Table structure for temp
-- ----------------------------
DROP TABLE IF EXISTS `temp`;
CREATE TABLE `temp`  (
  `vcs_id` int(11) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of temp
-- ----------------------------
INSERT INTO `temp` VALUES (5, 'b');
INSERT INTO `temp` VALUES (5, 'a');
INSERT INTO `temp` VALUES (6, 'b');
INSERT INTO `temp` VALUES (6, 'a');
INSERT INTO `temp` VALUES (7, 'b');
INSERT INTO `temp` VALUES (7, 'a');
INSERT INTO `temp` VALUES (8, 'a');
INSERT INTO `temp` VALUES (8, 'b');
INSERT INTO `temp` VALUES (8, 'c');

-- ----------------------------
-- Table structure for vcs
-- ----------------------------
DROP TABLE IF EXISTS `vcs`;
CREATE TABLE `vcs`  (
  `vcs_id` int(11) NOT NULL AUTO_INCREMENT,
  `creater` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `vcs_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `vcs_state` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`vcs_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vcs
-- ----------------------------
INSERT INTO `vcs` VALUES (1, '1', '1', 1);
INSERT INTO `vcs` VALUES (2, 'a', '视频会议', 0);
INSERT INTO `vcs` VALUES (3, 'a', '视频会议', 0);
INSERT INTO `vcs` VALUES (4, 'a', '视频会议', 0);
INSERT INTO `vcs` VALUES (5, 'a', '视频会议', 0);
INSERT INTO `vcs` VALUES (6, 'a', '视频会议', 0);
INSERT INTO `vcs` VALUES (7, 'a', '视频会议的速度', 1);
INSERT INTO `vcs` VALUES (8, 'c', '视频会议2', 0);

SET FOREIGN_KEY_CHECKS = 1;
