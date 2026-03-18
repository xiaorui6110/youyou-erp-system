-- 创建数据库（指定字符集和排序规则，避免中文乱码）
CREATE DATABASE IF NOT EXISTS `youyou-erp-system`
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `youyou-erp-system`;

-- 用户表
DROP TABLE IF EXISTS `youyou_user`;
CREATE TABLE `youyou_user` (
    `user_id` varchar(100) NOT NULL COMMENT '用户主键',
    `login_name` varchar(255) NOT NULL COMMENT '登录用户名',
    `login_password` varchar(50) DEFAULT NULL COMMENT '登录密码',
    `user_name` varchar(255) NOT NULL COMMENT '用户姓名',
    `user_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
    `user_position` varchar(200) DEFAULT NULL COMMENT '用户职位',
    `user_department` varchar(255) DEFAULT NULL COMMENT '用户所属部门',
    `user_email` varchar(100) DEFAULT NULL COMMENT '用户电子邮箱',
    `user_phone` varchar(100) DEFAULT NULL COMMENT '用户手机号码',
    `user_status` tinyint(4) DEFAULT '0' COMMENT '用户状态，0-正常，2-封禁',
    `user_description` varchar(500) DEFAULT NULL COMMENT '用户描述信息',
    `user_remark` varchar(500) DEFAULT NULL COMMENT '用户备注',
    `is_manager` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否为管理者 0-管理者 1-员工',
    `is_system` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否系统自带数据',
    `is_leader` varchar(1) DEFAULT '0' COMMENT '是否经理，0-否，1-是',
    `weixin_open_id` varchar(100) DEFAULT NULL COMMENT '微信绑定id',
    `tenant_id` varchar(100) DEFAULT NULL COMMENT '租户id',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` varchar(1) DEFAULT '0' COMMENT '删除标记，0-未删除，1-删除',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE UNIQUE INDEX idx_youyou_user_login_name ON youyou_user(login_name);
CREATE INDEX idx_youyou_user_tenant_status_deleted ON youyou_user(tenant_id, user_status, is_deleted);
CREATE INDEX idx_youyou_user_is_manager ON youyou_user(is_manager);
CREATE INDEX idx_youyou_user_is_leader ON youyou_user(is_leader);
CREATE INDEX idx_youyou_user_user_phone ON youyou_user(user_phone);
CREATE INDEX idx_youyou_user_user_email ON youyou_user(user_email);
CREATE INDEX idx_youyou_user_weixin_open_id ON youyou_user(weixin_open_id);
CREATE INDEX idx_youyou_user_user_department ON youyou_user(user_department);

-- 租户表
DROP TABLE IF EXISTS `youyou_tenant`;
CREATE TABLE `youyou_tenant` (
     `tenant_id` varchar(100) NOT NULL COMMENT '租户主键',
     `user_id` varchar(100) NOT NULL COMMENT '租户管理员用户id',
     `login_name` varchar(255) NOT NULL COMMENT '租户管理员登录名',
     `user_num_limit` int(11) NOT NULL DEFAULT 0 COMMENT '用户数量限制 0-不限制',
     `tenant_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '租户类型 0-免费 1-付费',
     `is_enabled`  tinyint(1) NOT NULL DEFAULT 1 COMMENT '启用状态 0-禁用 1-启用',
     `expire_time` datetime NULL DEFAULT NULL COMMENT '到期时间',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `tenant_remark` varchar(500) NULL DEFAULT NULL COMMENT '备注',
     `is_deleted` varchar(1) DEFAULT '0' COMMENT '删除标记，0-未删除，1-删除',
     PRIMARY KEY (`tenant_id`),
     UNIQUE KEY `uk_tenant_login_name` (`login_name`),
     INDEX `idx_tenant_user_id` (`user_id`),
     INDEX `idx_tenant_status` (`is_enabled`, `is_deleted`),
     INDEX `idx_tenant_expire` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表' ROW_FORMAT=DYNAMIC;

-- 用户/角色/模块关系表
DROP TABLE IF EXISTS `youyou_user_business`;
CREATE TABLE `youyou_user_business` (
    `user_business_id` varchar(100) NOT NULL COMMENT '用户/角色/模块关系主键',
    `relate_type` varchar(50) NOT NULL COMMENT '关系类别：user-用户，role-角色，module-模块',
    `key_id` varchar(100) NOT NULL COMMENT '主id（用户id/角色id/菜单id）',
    `relate_value` varchar(1000) DEFAULT NULL COMMENT '关联值（权限串/配置值）',
    `btn_str` varchar(2000) DEFAULT NULL COMMENT '按钮权限字符串',
    `tenant_id` varchar(100) DEFAULT NULL COMMENT '租户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` varchar(1) DEFAULT '0' COMMENT '删除标记，0-未删除，1-删除',
    PRIMARY KEY (`user_business_id`),
    INDEX `idx_type_key` (`relate_type`, `key_id`),
    INDEX `idx_tenant` (`tenant_id`),
    INDEX `idx_type_tenant` (`relate_type`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色-模块权限关系表' ROW_FORMAT=DYNAMIC;

-- 操作日志表
DROP TABLE IF EXISTS `youyou_log`;
CREATE TABLE `youyou_log` (
      `log_id` varchar(100) NOT NULL COMMENT '操作日志主键',
      `user_id` varchar(100) DEFAULT NULL COMMENT '操作用户id',
      `operation` varchar(500) DEFAULT NULL COMMENT '操作模块/操作描述',
      `client_ip` varchar(60) DEFAULT NULL COMMENT '客户端ip地址',
      `log_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '操作状态 操作状态 0-失败 1-成功',
      `log_content` varchar(5000)DEFAULT NULL COMMENT '操作详情/参数/返回结果',
      `tenant_id` varchar(100) DEFAULT NULL COMMENT '租户id',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `is_deleted` varchar(1) DEFAULT '0' COMMENT '删除标记，0-未删除，1-删除',
      PRIMARY KEY (`log_id`),
      INDEX `idx_user_id` (`user_id`),
      INDEX `idx_tenant_id` (`tenant_id`),
      INDEX `idx_create_time` (`create_time`),
      INDEX `idx_log_status` (`log_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统操作日志表' ROW_FORMAT=DYNAMIC;

-- 账户信息表
DROP TABLE IF EXISTS `youyou_account`;
CREATE TABLE `youyou_account` (
      `account_id` varchar(100) NOT NULL COMMENT '账户信息主键',
      `account_name` varchar(50) NOT NULL COMMENT '账户名称（如：现金、微信、支付宝、对公账户）',
      `serial_no` varchar(50) DEFAULT NULL COMMENT '账户编号/卡号后四位',
      `initial_amount` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '期初金额',
      `current_amount` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '当前余额',
      `account_remark` varchar(255) DEFAULT NULL COMMENT '账户备注',
      `is_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启用状态 0-禁用 1-启用',
      `account_sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序号（数字越小越靠前）',
      `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认账户 0-否 1-是',
      `tenant_id` varchar(100) DEFAULT NULL COMMENT '租户id',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `is_deleted` tinyint(1)  NOT NULL DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
      PRIMARY KEY (`account_id`),
      INDEX `idx_tenant_id` (`tenant_id`),
      INDEX `idx_is_enabled` (`is_enabled`),
      INDEX `idx_is_default` (`is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户信息表' ROW_FORMAT=DYNAMIC;

-- 财务主表
DROP TABLE IF EXISTS `youyou_account_head`;
CREATE TABLE `youyou_account_head` (
       `account_head_id` varchar(100) NOT NULL COMMENT '财务主表主键',
       `account_head_type` varchar(50) DEFAULT NULL COMMENT '类型(支出/收入/收款/付款/转账)',
       `organ_id` varchar(100) DEFAULT NULL COMMENT '单位id(收款/付款单位)',
       `hands_person_id` varchar(100) DEFAULT NULL COMMENT '经手人id',
       `account_head_creator` varchar(100) DEFAULT NULL COMMENT '操作员',
       `change_amount` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '变动金额(优惠/收款/付款/实付)',
       `discount_money` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '优惠金额',
       `total_price` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '合计金额',
       `account_id` varchar(100) DEFAULT NULL COMMENT '账户(收款/付款)',
       `bill_no` varchar(50) DEFAULT NULL COMMENT '单据编号',
       `bill_time` datetime DEFAULT NULL COMMENT '单据日期',
       `account_head_remark` varchar(1000) DEFAULT NULL COMMENT '备注',
       `file_name`  varchar(500) DEFAULT NULL COMMENT '附件名称',
       `account_head_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0未审核 1已审核 9审核中',
       `account_head_source` tinyint(1) NOT NULL DEFAULT 0 COMMENT '单据来源 0-pc 1-手机',
       `tenant_id` varchar(100) DEFAULT NULL COMMENT '租户id',
       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       `is_deleted` tinyint(1)  NOT NULL DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
       PRIMARY KEY (`account_head_id`),
       INDEX `idx_organ_id` (`organ_id`),
       INDEX `idx_account_id` (`account_id`),
       INDEX `idx_hands_person` (`hands_person_id`),
       INDEX `idx_bill_no` (`bill_no`),
       INDEX `idx_tenant_id` (`tenant_id`),
       INDEX `idx_bill_time` (`bill_time`),
       INDEX `idx_account_head_status` (`account_head_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务主表' ROW_FORMAT=DYNAMIC;

-- 财务子表
DROP TABLE IF EXISTS `youyou_account_item`;
CREATE TABLE `youyou_account_item` (
       `account_item_id` varchar(100) NOT NULL COMMENT '财务子表主键',
       `header_id` varchar(100) NOT NULL COMMENT '表头id',
       `account_id` varchar(100) DEFAULT NULL COMMENT '账户id',
       `in_out_item_id` varchar(100) DEFAULT NULL COMMENT '收支项目id',
       `bill_id` varchar(100) DEFAULT NULL COMMENT '单据id',
       `need_debt` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '应收欠款',
       `finish_debt` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '已收欠款',
       `each_amount` decimal(24,6) NOT NULL DEFAULT 0.000000 COMMENT '单项金额',
       `account_item_remark` varchar(500) DEFAULT NULL COMMENT '单据备注',
       `tenant_id` varchar(100) DEFAULT NULL COMMENT '租户id',
       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       `is_deleted` tinyint(1)  NOT NULL DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
       PRIMARY KEY (`account_item_id`),
       INDEX `idx_account_id` (`account_id`),
       INDEX `idx_header_id` (`header_id`),
       INDEX `idx_in_out_item` (`in_out_item_id`),
       INDEX `idx_bill_id` (`bill_id`),
       INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务子表' ROW_FORMAT=DYNAMIC;


