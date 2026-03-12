package com.xiaorui.youyouerpsystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-06 22:51:08
 */
@Getter
@Setter
@ToString
@TableName("youyou_user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户主键（雪花算法）
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private String userId;

    /**
     * 登录用户名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 登录密码
     */
    @TableField("login_password")
    private String loginPassword;

    /**
     * 用户姓名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户头像
     */
    @TableField("user_avatar")
    private String userAvatar;

    /**
     * 用户职位
     */
    @TableField("user_position")
    private String userPosition;

    /**
     * 用户所属部门
     */
    @TableField("user_department")
    private String userDepartment;

    /**
     * 用户电子邮箱
     */
    @TableField("user_email")
    private String userEmail;

    /**
     * 用户手机号码
     */
    @TableField("user_phone")
    private String userPhone;

    /**
     * 用户状态，0正常，2封禁
     */
    @TableField("user_status")
    private Byte userStatus;

    /**
     * 用户描述信息
     */
    @TableField("user_description")
    private String userDescription;

    /**
     * 用户备注
     */
    @TableField("user_remark")
    private String userRemark;

    /**
     * 是否为管理者 0-管理者 1-员工
     */
    @TableField("is_manager")
    private Byte isManager;

    /**
     * 是否系统自带数据
     */
    @TableField("is_system")
    private Byte isSystem;

    /**
     * 是否经理，0-否，1-是
     */
    @TableField("is_leader")
    private String isLeader;

    /**
     * 微信绑定id
     */
    @TableField("weixin_open_id")
    private String weixinOpenId;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 删除标记，0-未删除，1-删除（逻辑删除）
     */
    @TableLogic
    @TableField("is_deleted")
    private String isDeleted;
}
