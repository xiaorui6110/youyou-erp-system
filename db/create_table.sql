-- 创建数据库（指定字符集和排序规则，避免中文乱码）
CREATE DATABASE IF NOT EXISTS `youyou-erp-system`
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `youyou-erp-system`;

CREATE TABLE `youyou_user` (
    `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
    `login_name` varchar(255) NOT NULL COMMENT '登录用户名',
    `login_password` varchar(50) DEFAULT NULL COMMENT '登录密码',
    `user_name` varchar(255) NOT NULL COMMENT '用户姓名',
    `user_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
    `user_position` varchar(200) DEFAULT NULL COMMENT '用户职位',
    `user_department` varchar(255) DEFAULT NULL COMMENT '用户所属部门',
    `user_email` varchar(100) DEFAULT NULL COMMENT '用户电子邮箱',
    `user_phone` varchar(100) DEFAULT NULL COMMENT '用户手机号码',
    `user_status` tinyint(4) DEFAULT '0' COMMENT '用户状态，0正常，2封禁',
    `user_description` varchar(500) DEFAULT NULL COMMENT '用户描述信息',
    `user_remark` varchar(500) DEFAULT NULL COMMENT '用户备注',
    `is_manager` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否为管理者 0-管理者 1-员工',
    `is_system` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否系统自带数据',
    `is_leader` varchar(1) DEFAULT '0' COMMENT '是否经理，0-否，1-是',
    `weixin_open_id` varchar(100) DEFAULT NULL COMMENT '微信绑定id',
    `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
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






