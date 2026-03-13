package com.xiaorui.youyouerpsystem.model.vo;


import com.xiaorui.youyouerpsystem.model.entity.Tenant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description: 租户VO
 * @author: xiaorui
 * @date: 2026-03-13 20:23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantVO extends Tenant implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户数量
     */
    private Integer userCount;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 过期时间
     */
    private String expireTime;

}
