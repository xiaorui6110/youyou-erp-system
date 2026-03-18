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
 * 系统操作日志表
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-15 15:07:14
 */
@Getter
@Setter
@ToString
@TableName("youyou_log")
public class Log implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 操作日志主键
     */
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private String logId;

    /**
     * 操作用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 操作模块/操作描述
     */
    @TableField("operation")
    private String operation;

    /**
     * 客户端ip地址
     */
    @TableField("client_ip")
    private String clientIp;

    /**
     * 操作状态 0-失败 1-成功
     */
    @TableField("log_status")
    private Boolean logStatus;

    /**
     * 操作详情/参数/返回结果
     */
    @TableField("log_content")
    private String logContent;

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
