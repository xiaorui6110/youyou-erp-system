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
 * 用户-角色-模块权限关系表
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-15 14:12:41
 */
@Getter
@Setter
@ToString
@TableName("youyou_user_business")
public class UserBusiness implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户/角色/模块关系主键
     */
    @TableId(value = "user_business_id", type = IdType.ASSIGN_ID)
    private String userBusinessId;

    /**
     * 关系类别：user-用户，role-角色，module-模块
     */
    @TableField("relate_type")
    private String relateType;

    /**
     * 主id（用户id/角色id/菜单id）
     */
    @TableField("key_id")
    private String keyId;

    /**
     * 关联值（权限串/配置值）
     */
    @TableField("relate_value")
    private String relateValue;

    /**
     * 按钮权限字符串
     */
    @TableField("btn_str")
    private String btnStr;

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
     * 删除标记，0-未删除，1-删除
     */
    @TableLogic
    @TableField("is_deleted")
    private String isDeleted;
}
