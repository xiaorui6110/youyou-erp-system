package com.xiaorui.youyouerpsystem.common.base;


import com.xiaorui.youyouerpsystem.utils.StringUtil;
import lombok.Data;

/**
 * @description: 分页数据封装
 * @author: xiaorui
 * @date: 2026-03-10 21:51
 **/

@Data
public class PageDomain {

    /**
     * 当前记录起始索引
     */
    private Integer currentPage;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc = "asc";

    /**
     * 分页参数合理化
     */
    private Boolean reasonable = true;

    public String getOrderBy() {
        if (StringUtil.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtil.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Boolean getReasonable() {
        if (StringUtil.isNull(reasonable)) {
            return Boolean.TRUE;
        }
        return reasonable;
    }

}
