/*
 Navicat Premium Data Transfer

 Source Server         : MySQL57
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 20/07/2019 09:38:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_record
-- ----------------------------
DROP TABLE IF EXISTS `chat_record`;
CREATE TABLE `chat_record`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `course_id` int(64) NOT NULL COMMENT '课程ID',
  `user_id` int(64) NOT NULL COMMENT '发送用户的ID',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送的内容',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `is_del` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK4ptyj6mbdovcaph2kmeogp67j`(`course_id`) USING BTREE,
  INDEX `FKrjo8np6nvdsl7kbx0l4rmgyn4`(`user_id`) USING BTREE,
  CONSTRAINT `FK4ptyj6mbdovcaph2kmeogp67j` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKrjo8np6nvdsl7kbx0l4rmgyn4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_record
-- ----------------------------
INSERT INTO `chat_record` VALUES (3, 21, 21, '我开直播教你们打LOL', NULL, NULL, NULL, NULL, b'0');
INSERT INTO `chat_record` VALUES (4, 23, 24, '5号全场唯一真预言家', NULL, NULL, NULL, NULL, b'0');

-- ----------------------------
-- Table structure for collection_course
-- ----------------------------
DROP TABLE IF EXISTS `collection_course`;
CREATE TABLE `collection_course`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `user_id` int(64) NULL DEFAULT NULL COMMENT '用户ID',
  `course_id` int(64) NULL DEFAULT NULL COMMENT '课程ID',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `is_del` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK4wijw7hpn6y65106uog6rmgr4`(`course_id`) USING BTREE,
  INDEX `FK2stafun8jjeuj1ca51u0xholy`(`user_id`) USING BTREE,
  CONSTRAINT `FK2stafun8jjeuj1ca51u0xholy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK4wijw7hpn6y65106uog6rmgr4` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collection_course
-- ----------------------------
INSERT INTO `collection_course` VALUES (4, 24, 21, '2019-07-18 13:39:15', NULL, NULL, NULL, b'0');
INSERT INTO `collection_course` VALUES (5, 24, 22, '2019-07-18 13:39:40', NULL, NULL, NULL, b'0');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名',
  `classification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程分类',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程信息',
  `teacher_id` int(64) NOT NULL COMMENT '讲师ID',
  `class_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课堂（直播间）ID',
  `status` int(10) NOT NULL COMMENT '课程状态',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `is_del` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKqwmcv0fcio215pf0ypi6ft96x`(`teacher_id`) USING BTREE,
  CONSTRAINT `FKqwmcv0fcio215pf0ypi6ft96x` FOREIGN KEY (`teacher_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (21, '英雄联盟', '游戏', NULL, 21, NULL, 0, '2019-07-18 13:34:58', NULL, '2019-07-19 18:57:19', NULL, b'0');
INSERT INTO `course` VALUES (22, 'LOL云顶之奕', '游戏', NULL, 21, NULL, 0, '2019-07-18 13:36:02', NULL, NULL, NULL, b'0');
INSERT INTO `course` VALUES (23, '狼人杀', '游戏', NULL, 24, NULL, 0, '2019-07-18 13:40:28', NULL, NULL, NULL, b'0');

-- ----------------------------
-- Table structure for history_course
-- ----------------------------
DROP TABLE IF EXISTS `history_course`;
CREATE TABLE `history_course`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `user_id` int(64) NOT NULL COMMENT '用户ID',
  `course_id` int(64) NOT NULL COMMENT '课程ID',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `is_del` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKiukhofy92ubcubnhkq1asey1b`(`course_id`) USING BTREE,
  INDEX `FKf68x1wl3j60bmvnd5v182wu0i`(`user_id`) USING BTREE,
  CONSTRAINT `FKf68x1wl3j60bmvnd5v182wu0i` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKiukhofy92ubcubnhkq1asey1b` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of history_course
-- ----------------------------
INSERT INTO `history_course` VALUES (1, 22, 21, '2019-07-19 18:59:07', NULL, '2019-07-19 18:59:07', NULL, b'0');
INSERT INTO `history_course` VALUES (2, 22, 22, '2019-07-19 18:59:24', NULL, '2019-07-19 18:59:24', NULL, b'0');

-- ----------------------------
-- Table structure for tic_account
-- ----------------------------
DROP TABLE IF EXISTS `tic_account`;
CREATE TABLE `tic_account`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'SDK帐号',
  `user_token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'SDK验证',
  `who_used` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用者',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `is_del` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tic_account
-- ----------------------------
INSERT INTO `tic_account` VALUES (1, 'Web_trtc_01', 'eJxlj0tPg0AYRff8CjJrox-zqGLiQguxtPjgUdu4mVBmINMq0GGgPuJ-N2ITSbzbc3Jv7qdl2zZKw*Q0y-O6qww3741E9qWNAJ38waZRgmeGEy3*QfnWKC15VhipB*gwxjDA2FFCVkYV6mis5IYbbXIOzkhqxY4PS78tFAAT6lI2VlQ5wDt-OQ0ib7OOLnxyE0c6U13f3d67T7N5QF3v3OzT6DqW8HLw4o*V7A9BGc5Y4ifr7V42GFdBG57BpJ7OxfY5BP24zGMqdg*LokwX-tVo0qhXebw1IYwyQtwR7aVuVV0NAgaHOZjAT5D1ZX0DcFpeug__', NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `tic_account` VALUES (2, 'Web_trtc_02', 'eJxlj81OhDAURvc8BWGrkQulKO4mDKYTB8gIOrhqEDpQ5C*lqKPx3VWcxCbe7Tn5Tu6Hpuu6kW6Ti7wohrmXVB5HZujXugHG*R8cR17SXFIkyn*QvY1cMJofJBMLtDDGNoDq8JL1kh-4ydizJyqFLCjYijSVz3Qp-a44ADZyPAerCq8WGAaP-mbnx6Qn7ZwGKycPfRJ-R9aDmSWNVSeD2XRwFz80M9nfJ8FuU68i0frVjVkfKzfr1tvQCyJIyDRbt1evZepmZ1FF3lsQlyhQkpJ37PSWi7CDEfIU*sLExId*EWywsGUj*DlD*9S*AFcnXl0_', NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `tic_account` VALUES (3, 'Web_trtc_03', 'eJxlj11rgzAARd-9FcFXx4j5qLOwp3YMq2bt1LV7ClZjFqQ2jXF0jP73MVeosPt6Dvdyvx0AgJsn2X1ZVcehs9x*aeGCOXChe3eDWqual5ZjU-*D4qyVEbxsrDAj9CmlCMKpo2rRWdWoq7EVe26NrTjEE6mvWz4u-bUQCBEmIaFTRckRpk-FItoshzUjaUOCVjfLdjULVXR4QIzl1bolHvRLNqCkeNcfXbCJZMzq53Mm*93Obr14n8Rea4zx3-JV8XIaFqdXmWZQYxoE8nEyadVBXG-NMCUU43BCP4Xp1bEbBQR96iMMf*M6F*cHM0teQQ__', NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `tic_account` VALUES (4, 'Web_trtc_04', 'eJxlj0FPg0AQRu-8CsJVY5bdHQUTD4ZiQMvBUG3hssFlS4amsMBK1cb-rmITSZzre-le5mjZtu2slulFIWX72hhh3rVy7GvbIc75H9QaS1EYwfryH1RvGnsliq1R-QRdAKCEzB0sVWNwiydjrV6E6Y0UhM*kodyJqfS7wgmhjPsc5gpWE0zCpyB*XOR57jGsk6I7yPCu*4Ax23g45Gm5qn0upZfpFl3KoqtDXC1HfIg6CGn0fK*5F59xgCyj*yC4rbv1jrhBSwajF0la3cySBvfq9NYlAw6M*TM6qn7AtpkESlz4jpGfc6xP6wtcHl5E', NULL, NULL, NULL, NULL, NULL, b'0');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像照片',
  `status` int(10) NOT NULL COMMENT '用户状态(0：离线/1：在线)',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户角色(Student/Teacher)',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户信息',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `is_del` bit(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (21, '富哥', '123', NULL, 0, 'Teacher', NULL, '2019-07-18 13:31:37', NULL, '2019-07-19 18:57:49', NULL, b'0');
INSERT INTO `user` VALUES (22, '爆胎', '123', NULL, 0, 'Student', NULL, '2019-07-18 13:32:10', NULL, '2019-07-19 19:00:27', NULL, b'0');
INSERT INTO `user` VALUES (23, 'Alan', '123', NULL, 0, 'Student', NULL, '2019-07-18 13:32:33', NULL, NULL, NULL, b'0');
INSERT INTO `user` VALUES (24, '阿鬼', '123', NULL, 0, 'Teacher', NULL, '2019-07-18 13:33:02', NULL, '2019-07-18 13:42:09', NULL, b'0');
INSERT INTO `user` VALUES (25, '林子', '123', NULL, 0, 'Student', NULL, '2019-07-18 13:33:21', NULL, NULL, NULL, b'0');
INSERT INTO `user` VALUES (26, '天一', '123', NULL, 0, 'Student', NULL, '2019-07-18 13:33:32', NULL, NULL, NULL, b'0');

SET FOREIGN_KEY_CHECKS = 1;
