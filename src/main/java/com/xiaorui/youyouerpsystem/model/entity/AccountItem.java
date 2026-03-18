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
 * 财务子表
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:27
 */
@Getter
@Setter
@ToString
@TableName("youyou_account_item")
public class AccountItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 财务子表主键
     */
    @TableId(value = "account_item_id", type = IdType.ASSIGN_ID)
    private String accountItemId;

    /**
     * 表头id
     */
    @TableField("header_id")
    private String headerId;

    /**
     * 账户id
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 收支项目id
     */
    @TableField("in_out_item_id")
    private String inOutItemId;

    /**
     * 单据id
     */
    @TableField("bill_id")
    private String billId;

    /**
     * 应收欠款
     */
    @TableField("need_debt")
    private BigDecimal needDebt;

    /**
     * 已收欠款
     */
    @TableField("finish_debt")
    private BigDecimal finishDebt;

    /**
     * 单项金额
     */
    @TableField("each_amount")
    private BigDecimal eachAmount;

    /**
     * 单据备注
     */
    @TableField("account_item_remark")
    private String accountItemRemark;

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
