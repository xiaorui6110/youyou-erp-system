package com.xiaorui.youyouerpsystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 账户信息表
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:05
 */
@Getter
@Setter
@ToString
@TableName("youyou_account")
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账户信息主键
     */
    @TableId(value = "account_id", type = IdType.ASSIGN_ID)
    private String accountId;

    /**
     * 账户名称（如：现金、微信、支付宝、对公账户）
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 账户编号/卡号后四位
     */
    @TableField("serial_no")
    private String serialNo;

    /**
     * 期初金额
     */
    @TableField("initial_amount")
    private BigDecimal initialAmount;

    /**
     * 当前余额
     */
    @TableField("current_amount")
    private BigDecimal currentAmount;

    /**
     * 账户备注
     */
    @TableField("account_remark")
    private String accountRemark;

    /**
     * 启用状态 0-禁用 1-启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 排序号（数字越小越靠前）
     */
    @TableField("account_sort")
    private Integer accountSort;

    /**
     * 是否默认账户 0-否 1-是
     */
    @TableField("is_default")
    private Boolean isDefault;

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
     * 删除标记 0-未删除 1-已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean isDeleted;
}
