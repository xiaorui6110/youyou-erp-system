package com.xiaorui.youyouerpsystem.model.vo;


import com.xiaorui.youyouerpsystem.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description: 用户VO
 * @author: xiaorui
 * @date: 2026-03-12 20:53
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 机构名称
     */
    private String orgaName;

    /**
     * 机构id
     */
    private String orgaId;

    /**
     * 机构用户关联关系序号（用户在部门中排序）
     */
    private String orgaUserRelSeq;

    /**
     * 机构用户关联关系id
     */
    private String orgaUserRelId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户数量限制
     */
    private Integer userNumLimit;

    /**
     * 过期时间
     */
    private String expireTime;

    /**
     * 是否是领导
     */
    private String leaderFlagStr;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

}
