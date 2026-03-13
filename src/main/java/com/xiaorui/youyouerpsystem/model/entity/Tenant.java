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
 * 租户表
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-13 20:19:45
 */
@Getter
@Setter
@ToString
@TableName("youyou_tenant")
public class Tenant implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户主键（雪花算法）
     */
    @TableId(value = "tenant_id", type = IdType.ASSIGN_ID)
    private String tenantId;

    /**
     * 租户管理员用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 租户管理员登录名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 用户数量限制 0-不限制
     */
    @TableField("user_num_limit")
    private Integer userNumLimit;

    /**
     * 租户类型 0-免费 1-付费
     */
    @TableField("tenant_type")
    private Boolean tenantType;

    /**
     * 启用状态 0-禁用 1-启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 到期时间
     */
    @TableField("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;

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
     * 备注
     */
    @TableField("tenant_remark")
    private String tenantRemark;

    /**
     * 删除标记 0-未删除 1-已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private String isDeleted;
}
