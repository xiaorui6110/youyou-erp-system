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
 * 财务主表
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:15
 */
@Getter
@Setter
@ToString
@TableName("youyou_account_head")
public class AccountHead implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 财务主表主键
     */
    @TableId(value = "account_head_id", type = IdType.ASSIGN_ID)
    private String accountHeadId;

    /**
     * 类型(支出/收入/收款/付款/转账)
     */
    @TableField("account_head_type")
    private String accountHeadType;

    /**
     * 单位id(收款/付款单位)
     */
    @TableField("organ_id")
    private String organId;

    /**
     * 经手人id
     */
    @TableField("hands_person_id")
    private String handsPersonId;

    /**
     * 操作员
     */
    @TableField("account_head_creator")
    private String accountHeadCreator;

    /**
     * 变动金额(优惠/收款/付款/实付)
     */
    @TableField("change_amount")
    private BigDecimal changeAmount;

    /**
     * 优惠金额
     */
    @TableField("discount_money")
    private BigDecimal discountMoney;

    /**
     * 合计金额
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * 账户(收款/付款)
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 单据编号
     */
    @TableField("bill_no")
    private String billNo;

    /**
     * 单据日期
     */
    @TableField("bill_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime billTime;

    /**
     * 备注
     */
    @TableField("account_head_remark")
    private String accountHeadRemark;

    /**
     * 附件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 状态 0未审核 1已审核 9审核中
     */
    @TableField("account_head_status")
    private Boolean accountHeadStatus;

    /**
     * 单据来源 0-pc 1-手机
     */
    @TableField("account_head_source")
    private Boolean accountHeadSource;

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
